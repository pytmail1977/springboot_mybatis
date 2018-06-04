/*
 * myProgram.cpp
 *
 *  Created on: 2017年9月25日
 *      Author: pyt64
 */

#include "myProgram.h"


/*
 * 功能：主程序初始化操作，包括变量初始化，链接数据库
 * 参数：
 * 无
 * 返回值：
 *@-1 链接数据库失败
 *@-2... 其它自定义错误码
 *@1 成功执行
 */
int init(void){


	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: init is called.\n");

	////////////////////////////////
	//实时遥测数据结构初始化
	///////////////////////////////
	bzero(&gMyProgramYcData,sizeof(ssyc_myProgram_t));

	///////////////////////////
	//退出控制变量初始化
	///////////////////////////
	gIntIsRun = 1;

	///////////////////////////
	//进程号、线程号初始化
	///////////////////////////
	gPid = 0;
	gTid = 0;

	///////////////////////////
	//数据库链接状态变量初始化
	///////////////////////////
	gIntIsDbConnected = 0;


	//////////////////////////
	//指令类型和指令编号记录变量初始化
	//////////////////////////
	gNewZllx = 0;
	gNewZlbh = 0;



	//程序执行状态初始化
	////////////////////////
	gIntMyProgramZt = MYPROGRAM_ZT_IDLE;
	////////////////////////////////
	//修改实时遥测量
	gMyProgramYcData.programZt = gIntMyProgramZt;



	/*
	//////////////////////
	//TCP应用层封包头gSYNC_HEADER初始化
	//////////////////////
	gSYNC_HEADER[0] = 0xAA;
    gSYNC_HEADER[1] = 0xFE;
    gSYNC_HEADER[2] = 0x55;
    gSYNC_HEADER[3] = 0xFE;
	gSYNC_HEADER[4] = 0x00;
    gSYNC_HEADER[5] = 0xFE;
    gSYNC_HEADER[6] = 0xFF;
    gSYNC_HEADER[7] = 0x53;
    gSYNC_HEADER[8] = 0x44;
    gSYNC_HEADER[9] = 0x53;
*/

	//////////////////////////
	//应用心跳初始化
	//////////////////////////
	gYySxxt = 0;


	//////////////////////////
	//指令计数初始化
	//////////////////////////
	gZlCount = 0;
	//修改实时遥测量
	gMyProgramYcData.zlJs = gZlCount;

	/////////////////////////
	//定时器超时次数初始化
	/////////////////////////
	gBaseTimerCount = 0;

	//////////////////
	//取进程和线程ID
	//////////////////
	//取进程ID
	gPid = getpid();
	tmpPrint(LOGFILE, "TMP---Pid in main thread is %d.\n",gPid);

	//取主线程ID
	//gTid = pthread_self();
	//tmpPrint(LOGFILE, "TMP---Tid in main thread is %lu.\n",gTid);



	//////////////////////////////
	//数据库链接变量初始化
	//////////////////////////////
	//initMysql(&gMysql[0]);

	initMysql(&gMysql[0]);
	initMysql(&gMysql[1]);

	////////////////////////////////
	//链接数据库，如果失败就返回-1
	///////////////////////////////
	//if (0>=connectDB()){
	//	return -1; //-1表示无法链接到数据库
	//}

	////////////////////////////////
	//链接数据库，如果失败就返回-1
	///////////////////////////////
	if (0>=connectDB(&gMysql[0])){
		return -1; //-1表示无法链接到数据库
	}else if (0>=connectDB(&gMysql[1])){
		closeDB(&gMysql[0]);
		return -1;
	}
	gIntIsDbConnected = 1;



	////////////////////////////////
	//统计信息初始化
	///////////////////////////////
	bzero(&gTotal,sizeof(total_t));


	return 1;
}


/*
 * 功能：退出前的操作，断开与数据库和服务的链接
 * 参数:
 * @int gYyYxzt指定在退出前，写入应用table_name_JKGL_YYZT的应用状态，包括RET_ERR_CONNECT_SERVER等
 * 返回值：
 * @无，无论更新应用状态或断开链接是否成功都继续关闭动作
 */
void quit(int gYyYxzt){


	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: exit is called.\n");


#ifdef _SETTIMER
	//停止基础定时器
	int ret = stopMainTimer(NULL);
	if (ret!=1){
		errorPrint(LOGFILE, "ERR---Can't stop base timer: %s\n", strerror(ret));
	}

#endif

	//更新应用状态表
	updataYYZT(gYyYxzt);




 	//程序退出前插入实时遥测数据表
 	insertSsYcSj();

	//未避免有其它线程的数据库操作未完成，在关闭数据库之前，暂停一会儿
	sleep(SLEEP_BEFORE_QUIT);


	//关闭数据库链接
	closeDB(&gMysql[0]);
	closeDB(&gMysql[1]);
	gIntIsDbConnected = 0;

	//释放数据库链接
	destroyMysql(&gMysql[0]);
	destroyMysql(&gMysql[1]);
}



/*
 * 功能：更新自身状态表，包括是否运行，是否联网，最近指令类型，最近指令编号，指令计数，程序执行状态
 * 参数：
 * @bool bInsert，为true表示第一次更新自有状态，插入一条新记录，否则false表示更新现有记录
 * 返回值：
 * @-1：数据库未链接；
 * @-2：insert/update失败；
 * @1：成功返回
 */
int updateSelfState(bool bInsert){


	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: updateSelfStat is called.\n");

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	tmpPrint(LOGFILE, "TMP---Update table %s.\n",table_name_SELF_MYPROGRAM_ZT);


	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;

	//构造一个插入自身状态的语句，如果是应用首次运行到这里，表中还没有记录，需要执行该语句插入一条记录
	string sqlInsertSelfState = "insert into "+
			string(table_name_SELF_MYPROGRAM_ZT)+
			" values(\'" +
			getDateString() +     //更新时间
			"'," +
			int2String(gPid) +	//gPid
			"," +
			int2String(gTid) +	//主线程tid
			"," +
			int2String(gIntIsDbConnected) +	//是否链接数据库
			"," +
			int2String(gNewZllx) + //指令类型
			"," +
			int2String(gNewZlbh) + //指令编号
			"," +
			int2String(gZlCount) + //指令计数
			"," +
			int2String(gIntMyProgramZt) + //程序执行状态
			")";



	//构造一条更新语句，如果不是首次，则已经有记录在表中，只需要更新它
	string sqlUpdateSelfState = "update SELF_MYPROGRAM_ZT set MYPROGRAM_ZT_GXSJ = \'"+
			getDateString() +
			"'"+
			" , MYPROGRAM_ZT_SJK_SFLJ = "+
			int2String(gIntIsDbConnected) +
			" , MYPROGRAM_ZT_NEW_ZLLX = "+
			int2String(gNewZllx)+
			" , MYPROGRAM_ZT_NEW_ZLBH = "+
			int2String(gNewZlbh)+
			" , MYPROGRAM_ZT_ZL_COUNT = "+
			int2String(gZlCount) +
			" , MYPROGRAM_ZT_SFSC = "+
			int2String(gIntMyProgramZt) +
			" where MYPROGRAM_ZT_PID = "+
			int2String(gPid) +
			" ;";


	//执行语句，更新程序自身状态表
	int res;
	if (bInsert)
		res= self_mysql_query(selfMysqlp, sqlInsertSelfState.c_str());
	else
		res= self_mysql_query(selfMysqlp, sqlUpdateSelfState.c_str());

    if (!res) {
         prgPrint(LOGFILE, "PRG---Insert/update self state of MyProgram, affact %d rows.\n",
                 self_mysql_affected_rows(selfMysqlp));
     } else {
         errorPrint(LOGFILE,  "ERR---Insert/update self state of MyProgram error %d: %s\n", self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return -2;
     }


    return 1;

}


/*
 * 功能：更新监控管理服务应用状态表
 * 参数：
 * @gYyYxzt：应用状态
 * 返回值：
 * @-1：数据库未链接；
 * @-2：insert/update失败；
 * @1：成功返回
 */
int updataYYZT(int gYyYxzt){


	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: updataYYZT is called.\n");

	tmpPrint(LOGFILE, "TMP---Update table %s.\n",table_name_JKGL_YYZT);

	gYySxxt++;


	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	string tablename = table_name_JKGL_YYZT;

	//构造一个更新sql语句
	string sqlUpdateYYZT = "update "+
			tablename +
			" set YY_PID = "+
			int2String(gPid) +
			" , YY_YXZT = "+
			int2String(gYyYxzt)+
			" , YY_SXXT = "+
			int2String(gYySxxt)+
			" where YY_ID = "+
			int2String(YYID_MYPROGRAM)+
			" ;";

	int ret = self_mysql_query(selfMysqlp, sqlUpdateYYZT.c_str());

	sqlPrint(LOGFILE, "SQL---Update yyzt: %s\n",sqlUpdateYYZT.c_str());

    if (!ret) {
         prgPrint(LOGFILE, "PRG---Update yyzt, affact %d rows.\n",
                 self_mysql_affected_rows(selfMysqlp));

     } else {
         errorPrint(LOGFILE,  "ERR---Update yyzt error %d: %s\n", self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return -2;
     }

	return 1;

}

/*
 * 功能：创建应用自用的状态表
 * 参数：
 * 无
 * 返回值：
 * @-1：数据库未链接；
 * @-2：删除旧表失败；
 * @-3：创建新表失败；
 */
int createSelfUsedTables(void){


	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: createSelfUsedTables is called.\n");

	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	int ret = self_mysql_query(selfMysqlp, dropTable_SELF_MYPROGRAM_ZT.c_str());

	sqlPrint(LOGFILE, "SQL---Drop self used tables: %s\n",dropTable_SELF_MYPROGRAM_ZT.c_str());

    if (!ret) {
         prgPrint(LOGFILE, "PRG---Drop self used tables, affact %d rows.\n",
                 self_mysql_affected_rows(selfMysqlp));

     } else {
         errorPrint(LOGFILE,  "ERR---Drop self used tables error %d: %s\n", self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return -2;
     }


	ret = self_mysql_query(selfMysqlp, createTable_SELF_MYPROGRAM_ZT.c_str());

	sqlPrint(LOGFILE, "SQL---Create self used tables: %s\n",createTable_SELF_MYPROGRAM_ZT.c_str());

    if (!ret) {
         prgPrint(LOGFILE, "PRG---create self used tables, affact %d rows.\n",
                 self_mysql_affected_rows(selfMysqlp));

     } else {
         errorPrint(LOGFILE,  "ERR---Create self used tables error %d: %s\n", self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return -3;
     }

    //updateSelfState(true);

	return 1;
}



/*
 * 功能：响应定时刷新信号，更新本地状态表和监控管理服务YY状态表
 * 参数：
 * @ int
 * 返回值
 * 无
 */
void onMainTimer(int)
{

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: onMainTimer is called.\n");

	gBaseTimerCount++;

	//根据定时器超时次数是偶数还是奇数分别调用指令执行和更新状态
	if(gBaseTimerCount%2 == 1){

		//if的这一半都要用“==1”来判是否到了触发时间

//处理指令
#ifdef _DEAL_WITH_ZL

	if(gBaseTimerCount%DEAL_WITH_ZL_INTERVAL == 1){
	    //msgPrint(LOGFILE, "PRG---It is time to deal with zl.\n");
	    prgPrint(LOGFILE, "PRG---It is time to deal with zl.\n");
	    //处理给自己的指令
	    dealWithAnUnreadZl();
	}
#endif


	}else{
		//if的这一半都要用“==0”来判是否到了触发时间




//更新应用状态
#ifdef _UPDATE_STATE

		if(gBaseTimerCount%UPDATE_STATE_INTERVAL == 0){
			//msgPrint(LOGFILE, "PRG---It is time to update state.\n");
			prgPrint(LOGFILE, "PRG---It is time to update state.\n");
		    //更新自身状态表
		    updateSelfState(false);
		    //更新监控管理服务应用状态表
		    updataYYZT(RET_RUNNING);
		}
#endif

	}//else


}

/*
 * 功能：启动监听退出信号机制
 * 参数：
 * 无
 * 返回值：
 * @-1：失败；
 * @1：成功；
 */
int startListenerToExitSignal(void){


	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: startListenerToExitSignal is called.\n");

    if(signal(SIGINT|SIGQUIT|SIGTERM, onSIGINT) == SIG_ERR)
    {
         errorPrint(LOGFILE, "register a listener for SIGINT|SIGQUIT|SIGTERM fail");
         return -1;
    }

    return 1;
}

/*
 * 功能：根据BASE_TIMER值设置基础定时器，绑定处理函数
 * 参数：
 * @pOldValue：用于返回定时器原始值
 * 返回值：
 * @1:设置成功；
 * @-1：设置失败;
 *
 */
int startMainTimer(struct itimerval * pOldValue){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: startMainTimer is called.\n");

	//注册一个定时器
    if(signal(SIGALRM , onMainTimer) == SIG_ERR)
    {
    	errorPrint(LOGFILE, "ERR---Register Timer callback function fail.\n");
    	return -1;
    }

    //立即触发定时器
    //alarm(0);

    //设置定时器按BASE_TIMER微秒周期触发定时器
	struct itimerval value;
    value.it_value.tv_sec = 0;
    value.it_value.tv_usec = BASE_TIMER_INTERVAL;
    value.it_interval.tv_sec = 0;
    value.it_interval.tv_usec = BASE_TIMER_INTERVAL;

    if (setitimer(ITIMER_REAL, &value, pOldValue) != 0){
    	errorPrint(LOGFILE, "ERR---Set timer err %d\n", errno);
    	return -1;
    }

    return 1;
}

/*
 * 功能：：取消定时器设置，恢复原值
 * 参数：
 * @pOldValue：用于传入定时器原始值;
 * 返回值：
 * @1:设置成功；
 * @-1：设置失败;
 *
 */
int stopMainTimer(struct itimerval * pOldValue){

    struct itimerval value;
    value.it_value.tv_sec = 0;
    value.it_value.tv_usec = 0;
    value.it_interval = value.it_value;
    int ret = setitimer(ITIMER_REAL, &value, pOldValue);
    if (0 == ret)
    	return 1;
    else
    	return -1;

}

/*
 * 功能：当收到退出信号时执行，将运行标记置为0
 * 参数
 * @int；
 * 返回值：
 * 无
 */

/*
 * 主函数
 */

void onSIGINT(int){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: onSIGINT is called.\n");

	//////////////////
	//将intRun置为0
	/////////////////
	gIntIsRun = 0; //以前置为-1，程序并不退出，应该置为0，各线程的循环才能终止;

}



int main(void) {

	/////////////////////
	//初始化随机数种子，用于调试时给每个函数的每次运行一个不重复的随机编号
	/////////////////////
	//为了临近两次调用getRandFuncSeq()的结果也不同，需要协调getRandAddr()函数，将srand((unsigned)time(NULL));语句统一放到函数外部，在main()中调用。
	srand((unsigned)time(NULL));


	/////////////////////
	//输出函数基本信息
	/////////////////////

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: main is called.\n");



	//////////////////
	//初始化
	//////////////////
	int ret = init();
	if (-1 == ret){
		prgPrint(LOGFILE, "PRG---Can't connect DB:%s\n", strerror(ret));
		quit(RET_ERR_CONNECT_DB);
		return RET_ERR_CONNECT_SERVER;//表示连库失败
	}


	//记录主线程pt
	gTotal.MainPt = pthread_self();

	////////////////////////////////
	//初始化完成后，插入实时遥测数据表
	////////////////////////////////
	insertSsYcSj();

	///////////////////////////////////
	//建立自用库表
	///////////////////////////////////
	ret = createSelfUsedTables();
	if (ret!=1){
		prgPrint(LOGFILE, "PRG---Can't create self used tables :%s\n", strerror(ret));
		quit(RET_ERR_CREATE_SELF_USED_TABLES);
		return RET_ERR_CREATE_SELF_USED_TABLES; //表示创建服务自用库表失败
	}

	//刷新自用状态表信息
	updateSelfState(true);

	//////////////////////////////
    //注册一个退出信号监听程序
	//////////////////////////////
	ret = startListenerToExitSignal();
	if (ret!=1){
		prgPrint(LOGFILE, "PRG---Can't start listener to exit signal :%s\n", strerror(ret));
		quit(RET_ERR_RIGIST_SIGNAL_LISENER);
		return RET_ERR_RIGIST_SIGNAL_LISENER; //表示注册退出信号监听程序失败
	}



    /////////////////////////////////////////////////////
	//启动基础定时器
    /////////////////////////////////////////////////////
#ifdef _SETTIMER
	//struct itimerval * pOldValue = new (struct itimerval);
	ret = startMainTimer(NULL);
	if (ret!=1){
		prgPrint(LOGFILE, "PRG---Can't start base timer: %s\n", strerror(ret));
		quit(RET_ERR_START_BASE_TIMER);
		return RET_ERR_START_BASE_TIMER; //表示设置定时器失败
	}
#endif






#ifdef _IGNORUNREADZLWHENRESTART

    //////////////////////////
    //开始处理前，清除所有未处理的指令
    /////////////////////////
    ignoreUnreadZLWhenStart();
#endif

    //////////////////////////
    //main主循环
    //////////////////////////
    main_loop_of_main_thread();




    /////////////////////////////
    //退出前执行必要的操作
    /////////////////////////////
    quit(RET_CLOSING); //退出前置为正在关闭，表示应用走完了正常关闭流程，最后是否关闭成功由监控管理判断。



    ///////////////////////
    //退出进程
    ///////////////////////
  	prgPrint(LOGFILE, "PRG---MyProgram: program is end. Bye!\n");
  	//return EXIT_SUCCESS;
  	exit(0);

}


/*
 * 功能：应用启动时，将指令表中所有给自己的未读指令置为抛弃,发给广播地址的指令不会被抛弃
 * 参数：
 * 无
 * 返回值：
 * @-1:失败；
 * @1:成功；
 */
int ignoreUnreadZLWhenStart(void){


	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: ignoreUnreadZLWhenStart is called.\n");

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	string strUpdate_ZL_ZXJG_WhenStart = "update " +
			string(table_name_YK_ZL) +
			" set ZL_ZXJG = "+
			int2String(ZXJG_PQ)+
			" where (YY_ID = "+
			int2String(YYID_MYPROGRAM)+
			" or YY_ID = "+
			int2String(YYID_GBDZ)+
			") and ZL_ZXJG = "+
			int2String(ZXJG_WD)+
			";";


	sqlPrint(LOGFILE, "SQL---Update unread zl of MyProgram to ignored: %s\n",strUpdate_ZL_ZXJG_WhenStart.c_str());


	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;

	int ret = self_mysql_query(selfMysqlp, strUpdate_ZL_ZXJG_WhenStart.c_str());

    if (!ret) {
         prgPrint(LOGFILE, "PRG---Update unread zl of SC to ignored, affact %d rows.\n",
                 self_mysql_affected_rows(selfMysqlp));
     } else {
         errorPrint(LOGFILE,  "ERR---Update unread zl of SC to ignored error %d: %s\n",
        		 self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return -1;
     }

	return 1;
}

/*
 * 功能：处理一个未读的指令
 * 参数：
 * 无
 * 返回值：
 * @0:查询不到未读指令；
 * @-1:数据库未连接，或者查询失败；
 * @1:查询成功；
 */
int dealWithAnUnreadZl(void){

	//fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: dealWithAnUnreadZl is called.\n");


	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	string tablename = table_name_YK_ZL;
	string sqlSelectZL = "select ZL_ID,YY_ID,ZL_LX,ZL_BH,ZL_NR from "+
			tablename +
			" where (YY_ID = "+
			int2String(YYID_MYPROGRAM)+
			" or YY_ID = "+
			int2String(YYID_GBDZ)+
			") and ZL_ZXJG = " +
			int2String(ZXJG_WD)+
			" order by ZL_ID"
			" ;";

	sqlPrint(LOGFILE, "SQL---Select from %s: %s \n",table_name_YK_ZL,sqlSelectZL.c_str());

	int ret = self_mysql_query(selfMysqlp, sqlSelectZL.c_str());
    if (!ret) {


         MYSQL_RES *mysql_result = self_mysql_store_result(selfMysqlp);

         int num_row = 0;
         //如果取到结果集就取行数
         if (NULL != mysql_result){
             num_row = self_mysql_num_rows(selfMysqlp,mysql_result);
             prgPrint(LOGFILE, "PRG---Select from %s %d rows.\n", table_name_YK_ZL,num_row);
         }

         //如果行数为0就释放记录并退出
         if(0 == num_row){
             tmpPrint(LOGFILE, "TMP---There is no unread ZL.\n");
             self_mysql_free_result(selfMysqlp,mysql_result);
        	 return 0;
         }



         int intZL_ID,intYY_ID,intZL_LX,intZL_BH;
         string strZL_NR = "";
         unsigned char ucharZL_NR[ZL_MAX_LENGTH];
         bzero(ucharZL_NR,ZL_MAX_LENGTH);

         //读取第一条指令
         MYSQL_ROW mysql_row=self_mysql_fetch_row(selfMysqlp,mysql_result);
         //指令计数自增
         gZlCount++;
     	//修改实时遥测量
     	gMyProgramYcData.zlJs = gZlCount;

     	//收到指令时插入实时遥测数据表
     	insertSsYcSj();

         prgPrint(LOGFILE, "PRG---Fetch a row.");

         if (mysql_row[0] != NULL)
        	 intZL_ID = atoi(mysql_row[0]);
         if (mysql_row[1] != NULL)
        	 intYY_ID = atoi(mysql_row[1]);
         if (mysql_row[2] != NULL)
        	 intZL_LX = atoi(mysql_row[2]);
         if (mysql_row[3] != NULL)
        	 intZL_BH = atoi(mysql_row[3]);


         if (mysql_row[4] != NULL){
        	 strZL_NR = mysql_row[4];


             int i;
             for (i=0;i<507;i++)
            	 ucharZL_NR[i] = mysql_row[4][i];

         }//if
         else{
    		 //反馈执行结果为指令格式错误
             return_ZL_ZXJG(intZL_ID,ZXJG_GSCW);
             msgPrint(LOGFILE, "MSG---error! get a ZL with null zlnr.\n");

         }

         self_mysql_free_result(selfMysqlp,mysql_result);

         //Debug输出指令参数和内容
         tmpPrint(LOGFILE, "TMP---YYID_MYPROGRAM = %d\n",YYID_MYPROGRAM);
         tmpPrint(LOGFILE, "TMP---YYID_GBDZ = %d\n",YYID_GBDZ);
         dataPrint(LOGFILE, "DAT---intZL_ID = %d, intYY_ID = %d, intZL_LX = %d, intZL_BH = %d \n",intZL_ID,intYY_ID,intZL_LX,intZL_BH);
         dataPrint(LOGFILE, "DAT---ZL_NR in string is %s\n",strZL_NR.c_str());

         gNewZllx = intZL_LX;
         gNewZlbh = intZL_BH;

         /////////////////////////////
         //对收到的报文进行处理
         /////////////////////////////


         //如果收到的是到YYID_MYPROGRAM的定向指令
         if (intYY_ID ==  YYID_MYPROGRAM){
        	 tmpPrint(LOGFILE, "TMP---Get a ZL for YYID_MYPROGRAM.\n");
        	 //判指令类型
        	 switch (intZL_LX){


        	 case ZLLX_JKGL://如果是管理指令
        		 //进一步判指令编号
            	 switch (intZL_BH){

            	 case ZLBH_JKGL_YYSJQL: //应用数据清理
            		 onZL_JKGL_YYSJQL(intZL_ID,ucharZL_NR);
            		 break;
            	 case ZLBH_JKGL_ZRSJSYTZ: //注入数据使用通知
            		 onZL_JKGL_ZRSJSYTZ(intZL_ID,ucharZL_NR);
            		 break;
            	 default:
            		 //反馈执行结果为指令类编号超出正确范围
             		 return_ZL_ZXJG(intZL_ID,ZXJG_ZLCCCLFW);
            		 msgPrint(LOGFILE, "MSG---Get an unknown ZLLX_JKGL for YYID_MYPROGRAM with error ZL_BH: %d.",intZL_BH);
            		 break;
            	 }

            	 break;

        	 case ZLLX_KZZL://如果是扩展指令
        		 //进一步判指令编号
            	 switch (intZL_BH){

            	 case ZLBH_KZZL_PZ: //配置指令
            		 onZL_KZZL_PZ(intZL_ID,ucharZL_NR);
            		 break;
            	 default:
            		 //反馈执行结果为指令类编号超出正确范围
            		 return_ZL_ZXJG(intZL_ID,ZXJG_ZLCCCLFW);
            		 msgPrint(LOGFILE, "MSG---Get an unknown ZLLX_KZZL for YYID_MYPROGRAM with error ZL_BH: %d.",intZL_BH);
             		 break;
            	 }

            	 break;

        	 default:
        		 //反馈执行结果为指令类型超出正确范围
        		 return_ZL_ZXJG(intZL_ID,ZXJG_ZLCCCLFW);
         		 msgPrint(LOGFILE, "MSG---Get an unknown ZL for YYID_MYPROGRAM with error ZL_LX: %d.",intZL_LX);
              	 break;
        	 }
        	 //反馈执行结果为

         }else  if(intYY_ID == YYID_GBDZ) { //收到的是到广播地址的指令
        	 tmpPrint(LOGFILE, "TMP---Get a ZL for YYID_GBDZ\n");
        	 if (intZL_LX == ZLLX_JKGL and intZL_BH == ZLBH_JKGL_JDYGJ){
        		 onZL_JKGL_JDYGJ(intZL_ID,ucharZL_NR);
        	 }else{
           		 //反馈执行结果为指令类型超出正确范围
           		 return_ZL_ZXJG(intZL_ID,ZXJG_ZLCCCLFW);
           		 msgPrint(LOGFILE, "MSG---Get an unknown ZL for YYID_GBDZ with error ZL_LX: %d or ZL_BH: %d.",intZL_LX,intZL_BH);
        	 }//else
         }else{
    		 //反馈执行结果为指令地址YYID超出处理范围
             return_ZL_ZXJG(intZL_ID,ZXJG_ZLCCCLFW);
             msgPrint(LOGFILE, "MSG---Get an Unknown ZL with YYID: %d.\n",intYY_ID);
         }



     } else {
         errorPrint(LOGFILE,  "ERR---Select from %s error %d: %s\n",  table_name_YK_ZL,
        		 self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return -1;
     }


	return 1;
}

/*
 * 功能：向指令表反馈指令执行结果
 * 参数：
 * @int zl_id：指令ID；
 * @int zl_zxjg：指令执行结果；
 * 返回值：
 * @-1:操作成功；
 * @1:操作失败；
 */
int return_ZL_ZXJG(int zl_id,int zl_zxjg){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: return_ZL_ZXJG is called.\n");

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	string strUpdate_ZL_ZXJG = "update " +
			string(table_name_YK_ZL) +
			" set ZL_ZXJG = "+
			int2String(zl_zxjg)+
			" where ZL_ID = "+
			int2String(zl_id)+
			";";


	sqlPrint(LOGFILE, "SQL---Update ZL_ZXJG: %s\n",strUpdate_ZL_ZXJG.c_str());


	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;

	int ret = self_mysql_query(selfMysqlp, strUpdate_ZL_ZXJG.c_str());

    if (!ret) {
         prgPrint(LOGFILE, "PRG---Update ZL_ZXJG, affact %d rows.\n",
                 self_mysql_affected_rows(selfMysqlp));
     } else {
         errorPrint(LOGFILE,  "ERR---Update ZL_ZXJG error %d: %s\n", self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return -1;
     }

	return 1;
}

/*
 * 功能：处理节点预关机指令
 * 参数：
 * @int zl_id：指令ID；（未使用）
 * @unsigned char* ucharZL_NR：指令内容；（未使用）
 * 返回值：
 * @1:成功；（没有别的返回值）
 */
int onZL_JKGL_JDYGJ(int zl_id,unsigned char* ucharZL_NR){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: onZL_JKGL_JDYGJ is called.\n");

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	//todo
	//完成自己在关机前需要做的操作

	prgPrint(LOGFILE, "PRG---Deal with a ZL JDYGJ\n");

	//广播指令不返回执行结果

	gIntIsRun = 0;

	return 1;
}


/*
 * 功能：处理注入数据使用指令
 * 参数：
 * @int zl_id：指令ID；（未使用）
 * @unsigned char* ucharZL_NR：指令内容；（未使用）
 * 返回值：
 * @1:成功；（没有别的返回值）
 */
int onZL_JKGL_ZRSJSYTZ(int zl_id,unsigned char* ucharZL_NR){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: onZL_JKGL_ZRSJSYTZ is called.\n");

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	//todo
	//完成注入数据使用
	//可使用子线程完成复杂操作
	createAThreadToDoSomething(zl_id,ucharZL_NR);

	prgPrint(LOGFILE, "PRG---Deal with a ZL ZRSJSYTZ\n");

	//return_ZL_ZXJG(zl_id,ZXJG_JS);
	return_ZL_ZXJG(zl_id,ZXJG_CG);
	return 1;
}


/*
 * 功能：处理应用数据清理指令
 * 参数：
 * @int zl_id：指令ID；（未使用）
 * @unsigned char* ucharZL_NR：指令内容；（未使用）
 * 返回值：
 * @-1：数据库未连接，或操作失败；
 * @-2:文件清理失败；
 * @1:成功；
 */
int onZL_JKGL_YYSJQL(int zl_id,unsigned char* ucharZL_NR){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: onZL_JKGL_YYSJQL is called.\n");

	prgPrint(LOGFILE, "PRG---Deal with a ZL YYSJQL\n");

	return_ZL_ZXJG(zl_id,ZXJG_JS);


	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;


	//todo
	//清理所有程序产生的文件


	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	//清理应用自身的数据表，如果有多个需要依次执行（注意外键约束顺序）
	string strTruncateSCWJ = "truncate table " +
			string(table_name_SCJ_SCWJ) +
			";";


	sqlPrint(LOGFILE, "SQL---Truncate table %s: %s \n",table_name_SCJ_SCWJ, strTruncateSCWJ.c_str());

	int ret = self_mysql_query(selfMysqlp, strTruncateSCWJ.c_str());

    if (!ret) {
         prgPrint(LOGFILE, "PRG---Truncate table %s, affact %d rows.\n", table_name_SCJ_SCWJ,
                 self_mysql_affected_rows(selfMysqlp));
     } else {
         errorPrint(LOGFILE,  "ERR---Truncate table %s error %d: %s\n", table_name_SCJ_SCWJ,self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return_ZL_ZXJG(zl_id,ZXJG_SB);
         return -1;
     }

	return_ZL_ZXJG(zl_id,ZXJG_CG);
	return 1;
}


/*
 * 功能：处理配置指令
 * 参数：
 * @int zl_id：指令ID；（未使用）
 * @unsigned char* ucharZL_NR：指令内容；
 * 返回值：
 * @-1：数据库未连接，或操作失败；
 * @1:成功；
 */
int onZL_KZZL_PZ(int zl_id,unsigned char* ucharZL_NR){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: onZL_KZZL_PZ is called.\n");

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-C-Can not get mysql connection for this thread!\n");
		return -1;
	}

	prgPrint(LOGFILE, "PRG---Deal with a ZL KZZL\n");

	return_ZL_ZXJG(zl_id,ZXJG_JS);

	char* charTmp = (char*)ucharZL_NR;

	sqlPrint(LOGFILE, "SQL---Execute expended sql: %s\n",charTmp);


	//如果数据库未连接就返回
	if(gIntIsDbConnected!=1)
		return -1;

	int ret = self_mysql_query(selfMysqlp, charTmp);

    if (!ret) {
         prgPrint(LOGFILE, "PRG---Execute expended sql, affact %d rows.\n",
                 self_mysql_affected_rows(selfMysqlp));
     } else {
         errorPrint(LOGFILE,  "ERR---Execute expended sql error %d: %s\n",  self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
         return_ZL_ZXJG(zl_id,ZXJG_SB);
         return -1;
     }
	return_ZL_ZXJG(zl_id,ZXJG_CG);
	return 1;
}


//从子线程返回
//因主线程不等待子线程返回，并取结果，所以取消对参数pRet的new和赋值操作
void threadReturn(int arg){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: threadReturn is called.\n");

	/*
	int *pRet =  (int*)malloc(sizeof(int));
	*pRet = arg;
	*/
	msgPrint(LOGFILE,"MSG-S-subthread is going to be closed.\n");

	//在退出前，清理子线程的线程号
	gTotal.SubPt = 0;
	//修改状态为空闲
	gMyProgramYcData.programZt = 0;

	//pthread_exit((void*)pRet);
	pthread_exit(NULL);
}



/*
 * 功能：主线程循环，处理发给本应用的指令，在socket进行收发
 * 参数：
 * 无
 * 返回值：
 * 无
 */
void main_loop_of_main_thread(void){

	fucPrint(LOGFILE, "FUC+++myProgram.cpp FUNC: main_loop_of_main_thread is called.\n");



	 while(gIntIsRun){
		 //todo
		 //主线程循环



#ifndef _LOOP
    	//调试，如果没有链接到服务器则暂不循环
    	break;
#else
    	sleep(SLEEP_SC_LOOP);
#endif


	 }//while

}


////向实时遥测数据表插入实时遥测数据
void insertSsYcSj(void){

	fucPrint(LOGFILE, "FUC++++++YK.c FUNC: insertSsYcSj is called.\n");

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR---Can not get mysql connection for this thread!\n");
		return;
	}

	int sizeofgSsYcData = sizeof(ssyc_myProgram_t);

	char * from = (char *)&gMyProgramYcData;

	tmpPrint(LOGFILE,"TMP---String before Escaped is: \"%s\" (%d)\n", from, sizeofgSsYcData);


    char to[sizeofgSsYcData*2+1];
    bzero(to,sizeofgSsYcData*2+1);
    unsigned long len;

    len = mysql_real_escape_string(&selfMysqlp->mysql, to, from, sizeofgSsYcData);
    to[len] = '\0';
    tmpPrint(LOGFILE,"TMP---Escaped string is: \"%s\" (%lu)\n", to, len);

    tmpPrint(LOGFILE,"TMP---len after escape: %lu\n",len);
    tmpPrint(LOGFILE,"TMP---strlen(to): %zu\n",strlen(to));


    //向实时遥测数据表插入记录/////////////////////////////////////////
	//构造insert语句
	string strInsertSql = "insert into " +
			string(table_name_YC_SSYCSJ) +
			"( YY_ID, SSYC_CD, SSYC_NR) " +
			" values(" +
			int2String(YYID_MYPROGRAM) +
			", " +
			int2String(sizeofgSsYcData)+
			", \'" +
			to +
			"\');";

	sqlPrint(LOGFILE,"SQL---Insert into %s: %s \n",table_name_YC_SSYCSJ, strInsertSql.c_str());

	//执行到延时遥测数据表的插入操作
	int ret = self_mysql_query(selfMysqlp, strInsertSql.c_str());
    if (!ret) {

         prgPrint(LOGFILE,"PRG---Insert into %s, affact %d rows.\n", table_name_YC_SSYCSJ,
        		 self_mysql_affected_rows(selfMysqlp));

    }//if (!ret)
    else {
    	//如果插入延时遥测数据失败，继续运行
        errorPrint(LOGFILE, "ERR---Insert into %s error %d: %s\n",table_name_YC_SSYCSJ, self_mysql_errno(selfMysqlp), self_mysql_error(selfMysqlp));
    }//else


}

/*
 * 子线程处理函数
 */
void* execSubThread(void *arg){


	fucPrint(LOGFILE, "FUC+++SC.cpp FUNC: execSubThread is called.\n");


	//获得入口参数
	arg_t *argTmp =(arg_t*)arg;
	int zl_id = argTmp->zl_id;

	unsigned char ucharZL_NR[ZL_MAX_LENGTH];
	int i;
	for (i=0;i<ZL_MAX_LENGTH;i++){
		ucharZL_NR[i] = *(argTmp->ucharZL_NR+i);
	}

	printf("argTmp->zl_id = %d; argTmp->ucharZL_NR = %s\n",argTmp->zl_id,argTmp->ucharZL_NR);
	printf("ucharZL_NR = %s\n",ucharZL_NR);

	//在进入时，记录子线程的线程号，在退出时，清理子线程的线程号
	gTotal.SubPt = pthread_self();

	//取得本线程对应的数据库链接
	mysql_t * selfMysqlp = NULL;
	selfMysqlp = getMysql();
	if (NULL == selfMysqlp){
		errorPrint(LOGFILE,"ERR-S-Can not get mysql connection for this thread!\n");
		threadReturn(RET_OTHER_MISTAKE);
	}

	//todo
	//执行子线程操作

	if(1){
		//如果成功
		threadReturn(RET_MYPROGRAM_SUCCESS);
		return NULL;
	}else{
		//如果失败
		 threadReturn(RET_OTHER_MISTAKE);
		 return NULL;
	}

}

/*
 * 创建一个线程完成某些操作
 */
int createAThreadToDoSomething(int zl_id,unsigned char* ucharZL_NR){


	fucPrint(LOGFILE, "FUC+++SC.cpp FUNC: createAThreadToDoSomething is called.\n");


	// 创建子线程//////////////////
	pthread_t gTid;

	arg_t argTmp;
	argTmp.zl_id = zl_id;
	argTmp.ucharZL_NR = ucharZL_NR;

	pthread_attr_t attr;
	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr,PTHREAD_CREATE_DETACHED);
	int ret = pthread_create(&gTid, &attr, execSubThread, &argTmp);

	if ( 0 != ret )
	{
	    prgPrint(LOGFILE,"PRG---Can't create sub thread execSubThread: %s\n", strerror(ret));
	    return -1;//表示启动子线程失败
	}

	msgPrint(LOGFILE,"MSG---onZL_SCJ_SCKS Waiting for subThreads execSubThread return.\n");


	return 1;

}

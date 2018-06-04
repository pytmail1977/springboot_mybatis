/*
 * commonTool.c
 *
 *  Created on: 2015年1月26日
 *      Author: pyt64
 */


#include  "commonTool.h"


/*
 * 功能：连接到数据库
 * 参数：
 * @Mysql：数据库句柄，来自全局变量；
 *
 * @ DB_HOST：数据库主机地址，来自宏；
 * @ DB_USER：数据库用户名，来自宏；
 * @ DB_PASS：数据库口令，来自宏；
 * @ DB_NAME：数据库名	来自宏；
 * 返回值：
 * @-1：调用mysql_init失败，或调用mysql_real_connect失败；
 * @1：连接成功；
 *
 */
int connectDB(mysql_t * Mysql){

	fucPrint(LOGFILE,"FUC++++++commonTool.cpp FUNC: connectDB is called.\n");


	if (NULL == mysql_init(&Mysql->mysql)) {    //分配和初始化MYSQL对象
        errorPrint(LOGFILE,"mysql_init(): %s\n", mysql_error(&Mysql->mysql));
        return -1;
    }


	bool my_true= true;
	mysql_options(&Mysql->mysql,MYSQL_OPT_RECONNECT,&my_true);

    //尝试与运行在主机上的MySQL数据库引擎建立连接
    if (NULL == mysql_real_connect(&Mysql->mysql,
    			DB_HOST,
                DB_USER,
                DB_PASS,
                DB_NAME,
                0,
                NULL,
                0)) {
        errorPrint(LOGFILE,"mysql_real_connect(): %s\n", mysql_error(&Mysql->mysql));
        return -1;
    }

    //设置为自动commit
    mysql_autocommit(&Mysql->mysql,true);
    msgPrint(LOGFILE,"MSG-T-Connected database successfully!\n");
    return 1;
}

/*
 * 功能：关闭到数据库的连接
 * 参数：
 * @gMysql：数据库句柄，来自全局变量
 *
 * 返回值：
 * 无
 *
 */
void closeDB(mysql_t * Mysql){

	fucPrint(LOGFILE,"FUC++++++commonTool.cpp FUNC: closeDB is called.\n");

	if (1==gIntIsDbConnected){
		mysql_close(&(Mysql->mysql));
		//gIntIsDbConnected = 0;

	}
}


/*
 * 功能：初始化mysql_t中的锁
 * 参数：
 * @mysql_t* pMYsql：自定义数据库连接句柄结构
 * 返回值：
 * 无
 */
void initMysql(mysql_t* pMYsql){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: initMysql is called.\n");
	worm_mutex_init(&(pMYsql->mutex));

}

/*
 * 功能：销毁mysql_t中的锁
 * @mysql_t* pMYsql：自定义数据库连接句柄结构
 * 返回值：
 * 无
 */
void destroyMysql(mysql_t* pMYsql){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: destroyMysql is called.\n");

	worm_mutex_destroy(&(pMYsql->mutex));
}

/*
 * 功能：封装mysql_affected_rows
 */
my_ulonglong STDCALL self_mysql_affected_rows(mysql_t* pMYsql){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_affected_rows is called.\n");

	my_ulonglong STDCALL ret;

	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_affected_rows(&pMYsql->mysql);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;
}

/*
 * 功能：封装mysql_query
 */
int		STDCALL self_mysql_query(mysql_t* pMYsql, const char *q){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_query is called.\n");

	int ret;

	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_query(&pMYsql->mysql,q);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;
}

/*
 * 功能：封装mysql_real_query
 */
int		STDCALL self_mysql_real_query(mysql_t* pMYsql, const char *q,unsigned long length){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_real_query is called.\n");

	int ret;

	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_real_query(&pMYsql->mysql,q,length);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;

}

/*
 * 功能：封装mysql_errno
 */
unsigned int STDCALL self_mysql_errno(mysql_t* pMYsql){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_errno is called.\n");

	unsigned int STDCALL ret;
	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_errno(&pMYsql->mysql);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;

}

/*
 * 封装mysql_error
 */
const char * STDCALL self_mysql_error(mysql_t* pMYsql){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_error is called.\n");

	const char * STDCALL ret;

	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_error(&pMYsql->mysql);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;

}

/*
 * 功能：封装mysql_store_result
 */
MYSQL_RES *     STDCALL self_mysql_store_result(mysql_t* pMYsql){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_store_result is called.\n");

	MYSQL_RES *     STDCALL ret;
	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_store_result(&pMYsql->mysql);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;

}

/*
 * 功能：封装mysql_num_rows
 */
my_ulonglong STDCALL self_mysql_num_rows(mysql_t *pMYsql,MYSQL_RES *res){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_num_rows is called.\n");

	my_ulonglong STDCALL ret;

	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_num_rows(res);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;

}


/*
 * 功能：封装mysql_free_result
 */
void		STDCALL self_mysql_free_result(mysql_t *pMYsql,MYSQL_RES *result){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_free_result is called.\n");

	//void		STDCALL ret;

	worm_mutex_lock(&pMYsql->mutex);
	mysql_free_result(result);
	worm_mutex_unlock(&pMYsql->mutex);


}


/*
 * 功能：封装mysql_fetch_row
 */
MYSQL_ROW	STDCALL self_mysql_fetch_row(mysql_t *pMYsql,MYSQL_RES *result){

	fucPrint(LOGFILE, "FUC++++++commonTool.cpp FUNC: self_mysql_fetch_row is called.\n");

	MYSQL_ROW ret;

	worm_mutex_lock(&pMYsql->mutex);
	ret = mysql_fetch_row(result);
	worm_mutex_unlock(&pMYsql->mutex);

	return ret;
}


//pyt 20150326 修改了线程ID跟踪方法
/*
 * 功能：打印时间和线程ID
 * 参数：
 * @funcSeq：标示函数某次调用的随机数；
 * 返回值：
 * 无
 */
void prePrint(){

	time_t nowtime;
	struct tm * tm;
	time(&nowtime);
	tm = localtime(&nowtime);

	printf("(%4d-%02d-%02d, %02d:%02d:%02d) ",tm->tm_year+1900,tm->tm_mon+1,tm->tm_mday,tm->tm_hour,tm->tm_min,tm->tm_sec);

}


/*
 * 功能：以16bit为单位计算校验和
 * 参数
 * @__uint16_t * buf：缓冲区指针；
 * @int len：缓冲区长度；
 * 返回值：
 * 校验和；
 */
__uint16_t checkSum16(__uint16_t * buf, int len)
{
	//
	//fucPrint(LOGFILE, "FUC++++++*commonTool.c FUNC: checkSum16 is called.\n");

	__uint64_t  sum = 0;
	while (len > 1)
	{
		sum += *buf++;
		len -= sizeof(__uint16_t);
	}
	if(len)
	{
		sum += *((__uint8_t*)buf);
	}

	sum = (sum >> 16) + (sum & 0xffff);
	sum += (sum >> 16);
	return (__uint16_t)(~sum);
}

/*
 * 功能：单字节按位运算，用于以8bit为单位计算校验和
 * 参数
 * @abyte 待计算字节；
 * 返回值：
 * 单字节计算值；
 */
__uint8_t  calcrc_1byte(__uint8_t  abyte)
{
	int i;
	__uint8_t  crc_1byte;
	 crc_1byte = 0;                //设定crc_1byte初值为0
	 for(i = 0; i < 8; i++){
	  if(((crc_1byte^abyte)&0x01))
	  {
		   crc_1byte^=0x18;
		   crc_1byte>>=1;
		   crc_1byte|=0x80;
	  }
	  else
		   crc_1byte>>=1;
		   abyte>>=1;
	 }

	 return crc_1byte;
}

/*
 * 功能：8位CRC校验
 * 参数：
 * @buf：代校验字符数组
 * @len：待校验长度
 * 返回值
 * @0:返回的crc为0，则数据传输正确
 */

__uint8_t calcrc_bytes(__uint8_t  *buf, __uint64_t  len)
{
	__uint8_t  crc=0;
	 while(len--) //len为总共要校验的字节数
	 {
	  crc=calcrc_1byte(crc^*buf++);
	 }
	 return crc;  //若最终返回的crc为0，则数据传输正确
}


/*
 * 功能：对两个UChar数组进行拷贝
 * 参数
 * @const __uint8_t *src：源缓冲区；
 * @__uint8_t *dst：目标缓冲区；
 * @int count：拷贝字节数；
 * 返回值：
 * 无
 */
void copyUCharArray(const __uint8_t *src, __uint8_t *dst, int count)
{
	//
	//fucPrint(LOGFILE, "FUC++++++*commonTool.c FUNC: copyUCharArray is called.\n");

	int i;
	for (i=0;i<count;i++)
	{
		dst[i]=src[i];
	}
}





/*
 * 功能：初始化锁
 * 参数：
 * @worm_mutex_t *pWormMutex：锁指针；
 * 返回值：
 * 无
 */
//void wormhole_mutex_init(worm_mutex_t *pWormMutex,void* pointer){
void wormhole_mutex_init(worm_mutex_t *pWormMutex){

	if(!pWormMutex)
		return;

	pWormMutex->pt = 0;//-1; //表示没有人锁 //2017-5-19改为0
	pWormMutex->funcSeq = -1; //表示没有人锁
	pthread_mutex_init(&pWormMutex->mutex,NULL);

}

/*
 * 功能：销毁锁
 * 参数：
 * @worm_mutex_t *pWormMutex：锁指针；
 * 返回值：
 * 无
 */
void wormhole_mutex_destroy(worm_mutex_t *pWormMutex){
	if(!pWormMutex)
		return;

	pthread_mutex_destroy(&pWormMutex->mutex);
	pWormMutex->pt = 0; //表示没有人锁
	pWormMutex->funcSeq = -1; //表示没有人锁

}

/*
 * 功能：请求锁
 * 参数：
 * @worm_mutex_t *pWormMutex：锁指针；
 * 返回值：
 * 无
 */
void wormhole_mutex_lock(worm_mutex_t *pWormMutex){

	pthread_t selfPt= pthread_self();
	//pthread_t selfPt=1999;

	if(!pWormMutex)
		return;

	//如果正被本线程锁住
	if(selfPt == pWormMutex->pt)
		return;

	//否则
	pthread_mutex_lock(&pWormMutex->mutex);
	pWormMutex->pt = selfPt;
}


/*
 * 功能：释放锁
 * 参数：
 * @worm_mutex_t *pWormMutex：锁指针；
 * 返回值：
 * 无
 */
void wormhole_mutex_unlock(worm_mutex_t *pWormMutex){

	pthread_t selfPt= pthread_self();

	if(!pWormMutex)
		return;

	//如果未被锁住
	if(0 == pWormMutex->pt){
		return;
	}


	//如果正被本线程锁住且锁住的是本函数实体
	if( (selfPt == pWormMutex->pt) ){
		pWormMutex->pt = 0;
		//pWormMutex->funcSeq = -1;
		pthread_mutex_unlock(&pWormMutex->mutex);

	}else{
		msgPrint(LOGFILE, "MSG-T-wormhole_mutex_unlock: given mutex %lu is locked by thread %lu, this thread %lu can not unlock it!!!!!!!!!!!!!!!\n",long(&pWormMutex->mutex),(unsigned long int)(pWormMutex->pt),(unsigned long int)(selfPt));
	}


}

/*
 * 功能：尝试请求锁
 * 参数：
 * @worm_mutex_t *pWormMutex：锁指针；
 * 返回值：
 * @-1：传入的锁指针为空；
 * @0：此锁被本线程锁住；
 * @其它：调用pthread_mutex_trylock返回值；
 */
int  wormhole_mutex_trylock(worm_mutex_t *pWormMutex){

	pthread_t selfPt= pthread_self();

	if(!pWormMutex)
		return -1;
	//如果正被本线程锁住
	if(selfPt == pWormMutex->pt)
		return 0;
	//否则
	int ret;
	ret = pthread_mutex_trylock(&pWormMutex->mutex);
	if(0 == ret){
		pWormMutex->pt = selfPt;
	}

	return ret;


}

/*
 * 功能：查询谁锁定了指定的锁
 * 参数：
 * @worm_mutex_t *pWormMutex：锁指针；
 * 返回值：
 * 无
 */
//pyt20150520 将返回锁线程，改为输出当前的锁是被哪个线程的那个函数实例所锁住的
void wormhole_mutex_wholock(worm_mutex_t *pWormMutex){


	msgPrint(LOGFILE, "MSG-T-wormhole_mutex_wholock: mutex %lu is locked by thread %lu function %d.\n",(long int)(&pWormMutex->mutex),(unsigned long int)(pWormMutex->pt),pWormMutex->funcSeq);

}



/*
 * 功能：获取当前时间，并标示为易于写入数据库的字符串格式
 * 参数：
 * 无
 * 返回值：
 * 字符串表示的日期时间“年-月-日 时：分：秒”
 */
string getDateString(void){

	time_t timep;
	time(&timep); //获取time_t类型的当前时间
	tm *tmp = gmtime(&timep);
	string mytime = int2String(1900 + tmp->tm_year) + "-" + int2String(1+tmp->tm_mon) + "-" + int2String(tmp->tm_mday) + " " + int2String(tmp->tm_hour) + ":" + int2String(tmp->tm_min) + ":" + int2String(tmp->tm_sec);
	//dataPrint(LOGFILE, mytime.c_str());
	return mytime;

}

/*
 * 功能：整形转字符串
 * 参数：
 * @int n：待转的整形
 * 返回值：
 * 转换后的字符串（string类型）
 */
string int2String(int n)
{

	ostringstream stream;
	stream<<n; //n为int类型
	return stream.str();
}


//根据调用此函数所在的线程，返回适当的数据库链接
mysql_t * getMysql(void){

	fucPrint(LOGFILE,"FUC++++++commonTool.c FUNC: getMysql is called.\n");

	pthread_t pt = pthread_self();
	if (pt == gTotal.MainPt){
		return &gMysql[0];
	}else if (pt == gTotal.SubPt){
		return &gMysql[1];
	}else{
		return NULL;
	}
}

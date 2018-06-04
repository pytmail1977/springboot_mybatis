/*
 * const.h
 *
 *  Created on: 2015年1月25日
 *      Author: pyt64
 */

#ifndef CONST_H_
#define CONST_H_

#include "complier.h"

//////////////////////
//有关自身节点的参数
//////////////////////
#define DEVICE_ID  0x1E //设备地址，30-33为4个云计算节点，设备ID分别对应0x1E,0x1F,0x20,0x21

//////////////////////
//集成单元地址、服务端口
//////////////////////
#ifdef _CONNECT_TO_MY_SERVER
//集成单元的地址
#define NETSERVER_ADDR1 "192.168.1.31"
#define NETSERVER_ADDR2 "192.168.1.31"
#else
//集成单元的地址
#define NETSERVER_ADDR1 "192.168.1.10"
#define NETSERVER_ADDR2 "192.168.1.10"
#endif

//指令转发服务端口 略

//数传机控制服务端口 略

//数传机数据服务端口 略



//////////////////////
//日志文件
//////////////////////
#define LOGFILE stdout

//////////////////////
//数据库
//////////////////////
//数据库服务所在主机地址
#ifdef _USEOTHERDB
#define DB_HOST "192.168.1.31"
#else
#define DB_HOST "127.0.0.1"
#endif
//数据库名称
#define DB_NAME "node"
//数据库用户
#define DB_USER "root"
//数据库口令
#define DB_PASS "pyt999"


//////////////////////
//TCP应用层包头
//////////////////////
#define LEN_OF_TCP_HEAD  20 //TCP上的应用层格式——传输同步头长度
#define LEN_OF_TCP_SYNC 10//同步头长度
#define LEN_OF_TCP_LENGTH  4 //TCP上的应用层格式——传输同步头长度


//////////////////////
//关键服务应用ID和广播地址
//////////////////////
#define YYID_CKFW 0x01	//测控服务
#define YYID_JKGL 0x02	//平台监控及管理
#define YYID_LRXJ 0x04	//灵瑞相机服务
#define YYID_SCFW 0x05	//数传机客户端
#define YYID_ZRGL 0x06	//注入管理
#define YYID_PTSJ 0x07	//平台数据处理
#define YYID_SJTB 0x08	//NTP时间同步
#define YYID_GBDZ 0xFF	//广播地址


/////////////////////////
//MYPROGRAM的应用ID
//////////////////////////
#define YYID_MYPROGRAM 0xF1


//////////////////////
//指令类型
//////////////////////
#define ZLLX_CKFW 0x00	//集成单元测控指令
#define ZLLX_JKGL 0x02	//监控及管理指令
#define ZLLX_SJXJ 0x03	//手机相机指令
#define ZLLX_LRXJ 0x04	//灵瑞相机指令
#define ZLLX_SCJ  0x05	//数传指令
#define ZLLX_ZRGL 0x06	//注入指令
#define ZLLX_FLCX 0x09	//分离成像指令
#define ZLLX_KZZL 0x20	//扩展指令类型

//////////////////////
//指令编号
//////////////////////
//集成单元测控指令的指令编号 略

//监控及管理指令的指令编号
#define ZLBH_JKGL_JDSJQL 0x01 //节点数据清理
#define ZLBH_JKGL_YYZQDSN 0x02 //应用自启动使能
#define ZLBH_JKGL_YYQDTZ 0x03 //应用启动停止
#define ZLBH_JKGL_YYSXKZ 0x04 //应用属性控制（保活、不保活）
#define ZLBH_JKGL_YYAZ 0x05 //应用安装
#define ZLBH_JKGL_YYXZ 0x06 //应用卸载
#define ZLBH_JKGL_YYSJQL 0x07 //应用数据清理 //各服务需要处理
#define ZLBH_JKGL_JDYGJ 0x08 //节点预关机 //各服务需要处理，但是广播地址，不需要写回执行结果
#define ZLBH_JKGL_ZRSJSYTZ 0x09 //注入数据使用通知 //各服务需要处理

//手机相机指令的指令编号 略

//灵瑞相机指令的指令编号 略

//数传指令的指令编号 略

//注入指令的指令编号 略

//分离成像指令的指令编号 略

//扩展指令的指令编号
#define ZLBH_KZZL_PZ 0x01 //配置指令


//////////////////////
//指令执行结果
//////////////////////
#define ZXJG_WD 0//未读
#define ZXJG_JS 1//接受
#define ZXJG_CG 2//执行成功
#define ZXJG_PQ -1//重启抛弃未执行指令
#define ZXJG_TJBJB -2//条件不具备
#define ZXJG_ZLCCCLFW -3//指令超出处理范围内
#define ZXJG_SB -4//执行失败
#define ZXJG_GSCW -5 //指令格式错误，如指令内容为空

//////////////////////
//数据库表名
//////////////////////
//关键服务公用表
#define table_name_JKGL_GLPZ  "GJFW_JKGL_GLPZ"//
#define table_name_JKGL_YYZT  "GJFW_JKGL_YYZT"//
#define table_name_LRXJ_SLT  "GJFW_LRXJ_SLT"//
#define table_name_LRXJ_XJZT  "GJFW_LRXJ_XJZT"//
#define table_name_LRXJ_YST  "GJFW_LRXJ_YST"//
#define table_name_LRXJ_ZP  "GJFW_LRXJ_ZP"//
#define table_name_PTSJFW_CGSJ  "GJFW_PTSJFW_CGSJ"//
#define table_name_CGSJ_GPS  "GJFW_PTSJFW_CGSJ_GPS"//
#define table_name_PTSJFW_XWSJ  "GJFW_PTSJFW_XWSJ"//
#define table_name_SCJ_SCWJ  "GJFW_SCJ_SCWJ"//
#define table_name_SZ_SZSJ  "GJFW_SZ_SZSJ"//
#define table_name_SZ_SZWJ  "GJFW_SZ_SZWJ"//
#define table_name_YC_SSYCLSSJ  "GJFW_YC_SSYCLSSJ"//
#define table_name_YC_YSYCPZX  "GJFW_YC_YSYCPZX"//
#define table_name_YK_ZL  "GJFW_YK_ZL"//
#define table_name_YC_YSYCSJ  "GJFW_YC_YSYCSJ"//
#define table_name_YC_SSYCSJ "GJFW_YC_SSYCSJ" //实时遥测数据表

//myProgram私有表
#define table_name_SELF_MYPROGRAM_ZT "SELF_MYPROGRAM_ZT" //程序私有表 状态表


//////////////////////
//程序main()返回值，更新监控管理服务应用状态表时填写的应用状态字段值
//////////////////////
#define RET_NOTRUNNING 0 //0表示未运行
#define RET_RUNNING 2 //2表示正常运行
#define RET_CLOSING 3 //3表示正在正常关闭，之后由监控管理应用检查关闭情况，确认关闭成功后，由其置为RET_NOTRUNNING
#define RET_ERR_CONNECT_DB -1 //-1表示连库失败
#define RET_ERR_CONNECT_SERVER -2 //-2表示链接服务失败
#define RET_ERR_RIGIST_SIGNAL_LISENER -3 //-3表示注册信号监听程序失败
#define RET_ERR_CREATE_SELF_USED_TABLES -4 // -4表示创建服务自用库表失败
#define RET_ERR_START_BASE_TIMER -5 //-5表示启动基础定时器失败



//////////////////////
//长度、大小
//////////////////////

//存放已收socket数据的缓冲长度
#define BUFFER_SIZE 1024

//指令头长度
#define ZL_HEAD_LENGTH 5
//指令帧长度
#define ZL_MAX_LENGTH 512
//延时遥测采集sql长度
#define YSYC_CJFF_SQL_MAX_LENGTH 1024
//采集周期相同的延时遥测配置项统一处理，这里规定一个周期范围，在这个范围内的配置项归拢到同一个时间点处理，超出这个范围的不做处理
#define YSYC_PZX_CJZQ_MAX_RANGE 10
//通过执行SQL产生的数传组织文件的最大长度
#define SQL_OUTFILE_MAXSIZE 10240000 //最大10MB
//加在数传组织文件上的文件头部长度
//////////////////////
#define SQL_OUTFILE_HEAD_LENGTH 240 //固定240个字节的文件头部
//在待数传文件（带240字节头部）上增加的数传头长度
#define SC_FILE_HEAD_LENGTH 20

//时间
//////////////////////
//基础定时器的微秒数
#define BASE_TIMER_INTERVAL 500000

#define NUM_OF_BASE_TIMER_TICK_FOR_A_SECOND 1000000/BASE_TIMER_INTERVAL
//状态更新定时器超时时间
#define UPDATE_STATE_INTERVAL (5*(NUM_OF_BASE_TIMER_TICK_FOR_A_SECOND)) //5s器更新一次状态表

//指令执行定时器超时时间
#define DEAL_WITH_ZL_INTERVAL (5*(NUM_OF_BASE_TIMER_TICK_FOR_A_SECOND)) //5s执行一次指令



//////////////////////
//休眠时间
//////////////////////
//退出程序前等待的时间，避免有其他线程的数据库操作未完成
#define SLEEP_BEFORE_QUIT 1 //1s
//主线程每次循环休眠时间
#define SLEEP_SC_LOOP 30 //1s这个休眠时间决定了socket接收（不包括发送）和指令处理的动作频率 该循环目前没有用



////////////////////////
//定义报文数据输出格式
////////////////////////

#define HEX_FORM "%02hhx "
#define DEC_FORM "%03d "
#define UL_FORM "%lu"

//////////////////////
//遥测包头（外部不再使用TCP应用层包头）
//////////////////////
#define LEN_OF_SSYC_SYNC 2 //实时遥测包的同步头长度
#define LEN_OF_SSYC_HEAD 4 //实时遥测包的包头长度
#define SSYC_SYNC_0 0xEB //实时遥测同步头byte1
#define SSYC_SYNC_1 0x90 //实时遥测同步头byte2



//////////////////////
//execMyProgram()返回值
//todo 自定义
//////////////////////
#define RET_MYPROGRAM_SUCCESS 1 //表示处理成功
#define RET_OTHER_MISTAKE -999//其他错误


//////////////////////
//定义程序状态（示例）
//////////////////////
#define MYPROGRAM_ZT_IDLE 0
#define MYPROGRAM_ZT_PREPARING 1
#define MYPROGRAM_ZT_PREPARED 2
#define MYPROGRAM_ZT_SCING 3
#endif /* CONST_H_ */

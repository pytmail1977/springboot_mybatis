/*
 * myProgram.h
 *
 *  Created on: 2017年9月25日
 *      Author: pyt64
 */

#ifndef MYPROGRAM_H_
#define MYPROGRAM_H_

#include "include.h"
#include "includecpp.h"
#include "complier.h"
#include "const.h"
#include "debug.h"
#include "struct.h"
#include "global.h"
#include "commonTool.h"



/////////////////////////////////////////////
//变量
////////////////////////////////////////////

//自用库表创建sql语句

string dropTable_SELF_MYPROGRAM_ZT = "drop table IF EXISTS " + string(table_name_SELF_MYPROGRAM_ZT) + ";";
string createTable_SELF_MYPROGRAM_ZT = "create table IF NOT EXISTS " + string(table_name_SELF_MYPROGRAM_ZT) +
"(	\
   MYPROGRAM_ZT_GXSJ         datetime, 	\
   MYPROGRAM_ZT_PID          int	not null,\
   MYPROGRAM_ZT_TID          int	default 0,\
   MYPROGRAM_ZT_SJK_SFLJ     int default 0,	\
   MYPROGRAM_ZT_SOCKET_SFLJ    int default 0,	\
   MYPROGRAM_ZT_NEW_ZLLX    int default 0,	\
   MYPROGRAM_ZT_NEW_ZLBH    int default 0,	\
   MYPROGRAM_ZT_ZL_COUNT		int default 0,	\
   MYPROGRAM_ZT_SFSC			int default 0,  \
   primary key (MYPROGRAM_ZT_PID)	\
);";



/////////////////////////////////////////////
//函数
////////////////////////////////////////////

////////////////////////////
//主函数及主循环函数
////////////////////////////
int main(void);
//主线程循环
void main_loop_of_main_thread(void);



////////////////////////////
//初始化及回收相关函数
////////////////////////////
//初始化
int init(void);
//退出前处理
void quit(int yy_zt);
//创建自用库表
int createSelfUsedTables(void);


////////////////////////////
//定时器管理相关函数
////////////////////////////
//启动基础定时器
int startMainTimer(struct itimerval * pOldValue);
//停止基础定时器
int stopMainTimer(struct itimerval * pOldValue);
//启动监听退出信号机制
int startListenerToExitSignal(void);
//向实时遥测数据表插入记录
void insertSsYc(void);

////////////////////////////
//定时器处理相关函数
////////////////////////////
//响应基础定时器超时
void onMainTimer(int);
//更新自身状态表
int updateSelfState(void);
//更新关键服务-监控管理-应用状态表
int updataYYZT(int yy_yxzt);
//当收到退出信号时执行，将运行标记置为-1
void onSIGINT(int);


////////////////////////////
//指令处理相关函数
////////////////////////////
//处理一个未读的指令
int dealWithAnUnreadZl(void);
//处理应用数据清理指令
int onZL_JKGL_YYSJQL(int zl_id,unsigned char* ucharZL_NR);
//处理节点预关机指令
int onZL_JKGL_JDYGJ(int zl_id,unsigned char* ucharZL_NR);
//处理注入数据使用指令 目前什么也不做
int onZL_JKGL_ZRSJSYTZ(int zl_id,unsigned char* ucharZL_NR);
//处理配置指令
int onZL_KZZL_PZ(int zl_id,unsigned char* ucharZL_NR);
//向指令表写入指令的执行结果
int return_ZL_ZXJG(int zl_id,int zl_zxjg);
//应用启动时，将指令表中所有给自己的未读指令置为抛弃
int ignoreUnreadZLWhenStart(void);


//向实时遥测数据表插入实时遥测数据
void insertSsYcSj(void);

////////////////////////////
//多线程相关
///////////////////////////

//创建一个线程完成某些操作
int createAThreadToDoSomething(int zl_id,unsigned char* ucharZL_NR);

//子线程处理函数
void* execSubThread(void *arg);




#endif /* MYPROGRAM_H_ */

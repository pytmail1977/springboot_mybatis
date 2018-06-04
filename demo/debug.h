/*
 * debug.h
 *
 *  Created on: 2017年5月16日
 *      Author: pyt64
 */

#ifndef DEBUG_H_
#define DEBUG_H_

#include "complier.h"
#include "commonTool.h"




//////////////////////
//Debug输出函数使用的辅助信息打印函数
//////////////////////
//定义时间、线程信息输出
#define infoPrint prePrint();


///////////////////////
//定义用于Debug的输出函数
///////////////////////

//调试输出
#ifdef _DEBUGPRINT
#define prgPrint infoPrint fprintf //主要用于调试流程
#define dataPrint infoPrint fprintf  //主要用于调试数据
#define sqlPrint infoPrint fprintf  //主要用于调试SQL
#define tmpPrint infoPrint fprintf //主要用于调试 	临时的提示信息 如进程id，等
#define debugPrint infoPrint fprintf //主要用于临时调试

//定义用于帧、包输出的函数
#define debugPrint_KzPackage //fprintf //主要用于调试发送的数传控制遥测包
#define debugPrint_SjPackage //fprintf //主要用于调试发送的数传数据遥测包

//定义用于显示调用关系的输出函数
#define fucPrint //infoPrint fprintf
//定义用于消息显示的输出函数
#define msgPrint infoPrint fprintf
//定义用于错误提示的输出函数
#define errorPrint infoPrint fprintf


//定义用于普通输出格式的输出函数
#define normalPrint fprintf

#else
#define prgPrint //infoPrint fprintf //主要用于调试流程 类似 IT TIME TO DO ……
#define dataPrint //infoPrint fprintf  //主要用于调试数据
#define sqlPrint //infoPrint fprintf  //主要用于调试SQL
#define tmpPrint //infoPrint fprintf //主要用于调试 	临时的提示信息 如进程id，等
#define debugPrint //infoPrint fprintf //主要用于临时调试

//定义用于帧、包输出的函数
#define debugPrint_KzPackage //fprintf //主要用于调试发送的数传控制遥测包
#define debugPrint_SjPackage //fprintf //主要用于调试发送的数传数据遥测包

//定义用于显示调用关系的输出函数
#define fucPrint //infoPrint fprintf
//定义用于消息显示的输出函数
#define msgPrint //infoPrint fprintf
//定义用于错误提示的输出函数
#define errorPrint //infoPrint fprintf


//定义用于普通输出格式的输出函数
#define normalPrint //fprintf
#endif


//////////////////////
//加锁控制
//////////////////////

#ifdef _LOCK
#define worm_mutex_init wormhole_mutex_init
#define worm_mutex_destroy wormhole_mutex_destroy
#define worm_mutex_lock wormhole_mutex_lock
#define worm_mutex_unlock wormhole_mutex_unlock
#define worm_mutex_trylock wormhole_mutex_trylock
#else
#define worm_mutex_init //wormhole_mutex_init
#define worm_mutex_destroy //wormhole_mutex_destroy
#define worm_mutex_lock //wormhole_mutex_lock
#define worm_mutex_unlock //wormhole_mutex_unlock
#define worm_mutex_trylock //wormhole_mutex_trylock
#endif

#endif /* DEBUG_H_ */


#ifndef STRUCT_H_
#define STRUCT_H_


#include "include.h"
#include "complier.h"
#include "const.h"
#include "debug.h"

#pragma pack(1)

//遥控指令头部各字段类型
typedef __uint8_t YKZL_SBID_t;
typedef __uint8_t YKZL_YYID_t;
typedef __uint8_t YKZL_ZLLX_t;
typedef __uint8_t YKZL_ZLBH_t;
typedef __uint8_t YKZL_YL_t;



//遥控指令
typedef struct struct_ZLFrameHeader_s{
	YKZL_SBID_t sbid;//设备ID
	YKZL_YYID_t yyid;//应用ID
	YKZL_ZLLX_t zllx;//指令类型
	YKZL_ZLBH_t zlbh;//指令编号
	YKZL_YL_t yl;//预留
	//__int8_t zlnr[507];
}struct_ZLFrameHeader_t;


//函数随机序号（用于识别函数实例）
typedef __int32_t funcSeq_t;

//锁
typedef struct worm_mutex_s{
	pthread_mutex_t mutex; //锁
	pthread_t pt; //当前由哪个线程锁定
	funcSeq_t funcSeq; //当前由哪个线程的哪个函数实例锁定
}worm_mutex_t;



//socket
typedef struct netsocket_s{
	__int8_t client_sockfd; //socketfd
	struct sockaddr_in remote_addr; //服务器端网络地址结构体
} netsocket_t;

//mysql
typedef struct mysql_s{
	MYSQL   mysql;
	worm_mutex_t mutex; //锁
}mysql_t;



//文件头结构，加在文件上
typedef struct fileHead_s{
	__uint8_t fileHeadSync[10]; //文件头标志，固定为AA-55-00-FF-23-49-53-43-41-53
	__uint8_t nodeId;//存储的智能节点ID
	__uint8_t reservation1;//保留1
	__uint32_t time_s;//生成时间 秒
	__uint32_t time_us;//生成时间 微秒
	__uint8_t fileType;//文件类型
	__uint32_t fileSize;//文件长度
	__uint8_t reservation2[53]; //保留2
	__uint16_t crc16;
	__uint8_t aboutNr[160]; //保留2
	__uint8_t reservation3; //保留3
}fileHead_t;



//定义按需遥测数据头
typedef struct ssyc_head_s{
	__uint8_t header[LEN_OF_SSYC_SYNC];
	__uint8_t yyId;
	__uint8_t length;
}ssyc_head_t;

//定义按需遥测结构体
typedef struct ssyc_myProgram_s{
	/*
	 * 自行定义
	 */
	__uint16_t zlJs;//指令计数
	__uint8_t programZt;//自定义程序状态

}ssyc_myProgram_t;

typedef struct 	arg_s{
	int zl_id;
	unsigned char* ucharZL_NR;
}arg_t;

//统计信息
typedef struct total_s{
	//（主线程）pt
	pthread_t MainPt;
	//子线程pt
	pthread_t SubPt;
}total_t;


#pragma pack()

#endif /* STRUCT_H_ */

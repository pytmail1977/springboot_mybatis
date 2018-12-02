package org.iscas.tj2.pyt.springboot_mybatis.service;

import java.util.List;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.iscas.tj2.pyt.springboot_mybatis.Const;
import org.iscas.tj2.pyt.springboot_mybatis.SceneType;
import org.iscas.tj2.pyt.springboot_mybatis.controller.LoginController;
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserMapper;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.iscas.tj2.pyt.springboot_mybatis.model.message.resp.TextMessage;
import org.iscas.tj2.pyt.springboot_mybatis.scene_state.State;
import org.iscas.tj2.pyt.springboot_mybatis.scene_state.StateStack;
import org.iscas.tj2.pyt.springboot_mybatis.scene_state.StateTransfer;
import org.iscas.tj2.pyt.springboot_mybatis.util.MessageUtil;
import org.iscas.tj2.pyt.springboot_mybatis.util.TextMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("JHSBC") //用于标注业务层组件
public class CoreServiceImplJHSBC implements ICoreService {	
	@Autowired
    private TextMessageUtil textMessageUtil;
	
	//2018-11-18 由于不直接操作数据库，注释掉成员DbService db
	//数据库操作对象
	//private DbService db = new DbService();
	//新建工程的步骤
	int stepOfNewProj = 0; //0:未进入创建流程；1、2、3、4:用户在创建
	
	//2018-11-18 取消成员stateTransfer和stateStack
/*	
	//状态转移对象
	StateTransfer stateTransfer = null;	
	//初始化状态栈
	StateStack stateStack = new StateStack(Const.maxStackDepth);*/
	
	//2018-11-18 新增成员stateService
	//状态服务
	StateService stateService = new StateService();
	
	//存放定义新工程所用的信息
	Map<String, String> mapProjInfo = new HashMap<String, String>();
	
	//debug用变量
	//人机交互轮数
	int round = 0;
	
	//2018-11-18 移除StateProccedue定义
/*	//2018-11-17 传入reqContent参数给transferState
	//2018-11-17 将返回值由void改为String
	public String StateProccedue(String reqContent) {
		System.out.println("Current state is: "+stateTransfer.getNowState().getStrComment());
		//System.out.println("Please Input your order:");

		String[] strArray = reqContent.split(" ");
		String strOrder = strArray[0];
		String strArg1 = (strArray.length>1?strArray[1]:null);
		String strArg2 = (strArray.length>2?strArray[2]:null);
		
		stateTransfer.transferState(reqContent,strOrder, strArg1, strArg2);
		String strReturn = stateTransfer.getNowState().getRespContent();
		return strReturn;
	}*/
	
	@Override
	public String processRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String respMessage = null;
		// 初始化返回的文本消息内容
		String respContent = "";
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			//输出是第几轮交互
    		System.out.println("第"+(round++)+"轮交互：");
    		
    		//2018-11-18 取消以下直接处理
 /*   		//从数据库查询用户信息
        	User user = null;
        	user = db.getUserInfo(fromUserName);
        	int idUser = 0;
        	//如果没有查到就插入一条用户记录
        	if (null == user) {
        		User newUser = new User();
        		newUser.setWeixinId_User(fromUserName);
        		//int idUser = db.insertNewUser(newUser);
        		db.insertNewUser(newUser);        		
        		idUser  = newUser.getId_User();
        		
        	}else{//如果查询到就记录到状态栈中
        		idUser = user.getId_User();    			
        	}//if (null == user) 

        	//如果查询idUser错误，或新增用户失败则返回
        	if(0 == idUser)
        		return "新增用户失败";
        	     	
    		//获取用户idUser成功后，如果是首次登录（状态栈为空），就记录到状态栈中，并用该状态初始化stateTransfer
    		if (stateStack.sizeof() == 0) {
    			State state = new State(idUser,fromUserName,0,"用户:"+fromUserName,SceneType.STUser);
    			stateStack.push(state);
    			
    			stateTransfer = new StateTransfer(state);
    			
    		}
        	        	
        	//在返回语开头加上下文信息
        	respContent = stateStack.getContext();*/
    		
    		//2018-11-18 改为引用stateService类的方法OnUserLogin（）
    		respContent = stateService.StateServiceOnUserLogin(fromUserName);
        				
			// 回复文本消息，注意此处TextMessage是在包org.iscas.tj2.pyt.springboot_mybatis.model.message.resp.TextMessage中定义的
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);


			// 接收文本消息内容
			String reqContent = requestMap.get("Content");
			
			// 根据收到的信息种类自动回复消息			
			// 如果不是文本则回复……
			//去掉对图像、音频、连接等处理，统一在前面作为非文本消息回复无法处理
			if (!msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
	            respContent += "无法处理您发送的非文本消息！";
	            textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);	
	            return respMessage;
			}
				
            //如果用户发送表情，则回复同样表情。
            if (isQqFace(reqContent)) {
                respContent += reqContent;
                textMessage.setContent(respContent);
                // 将文本消息对象转换成xml字符串
                respMessage = textMessageUtil.messageToxml(textMessage);
                return respMessage;
            } 
			// 如果是其它文本则进一步处理
			//进入状态机处理
			respContent = stateService.StateProccedue(reqContent);
    		System.out.println(respContent);
    		textMessage.setContent(respContent);
    		respMessage = textMessageUtil.messageToxml(textMessage);
    		return respMessage;
            
            //2018-11-18 注释掉所有switch (reqContent)的内容 
            /*
            switch (reqContent) {
                case "1#": {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("您好，我是小牛，请回复数字选择服务：").append("\n\n");
                    buffer.append("11# 可查看软件定义卫星技术联盟网址").append("\n");
                    buffer.append("12# 可查看用户信息").append("\n");
                    buffer.append("13# 可查看用户的工程").append("\n");
                    buffer.append("14# 可创建新的工程").append("\n");
                    buffer.append("cd 工程id 可进入相应工程").append("\n");
                    buffer.append("任何时刻回复“1#”退回到此菜单").append("\n");
                    //TODO 状态栈的退回
                    respContent = String.valueOf(buffer);
                    System.out.println(respContent);
                    textMessage.setContent(respContent);
                    respMessage = textMessageUtil.messageToxml(textMessage);
                    break;
                }

                case "11#": {
                    //测试网址回复                	
                    respContent = "<a href=\"http://www.sdsalliance.net\">软件定义卫星技术联盟</a>";
                    System.out.println(respContent);
                    textMessage.setContent(respContent);
                    // 将文本消息对象转换成xml字符串
                    respMessage = textMessageUtil.messageToxml(textMessage);
                    break;
                }
                
                case "12#": {
                    //反馈用户信息
            		String strContent = "用户信息:\n" + "微信Id:"+ user.getWeixinId_User() +  "; 帐号:" + user.getAccount_User() + "; 用户姓名:" + user.getName_User() + "; email:" + user.getEmail_User();
            		System.out.println(strContent);
                    textMessage.setContent(strContent);
                    // 将文本消息对象转换成xml字符串
                    respMessage = textMessageUtil.messageToxml(textMessage);
                    break;
                }
                case "13#": {                	
					// 反馈用户的工程信息
					System.out.println("从数据库查询用户的工程信息：");
					List<Project> projects = db.getProjectsInfo(fromUserName);
					String strContent = "您的工程:";
					Iterator <Project> iter= projects.iterator();
					int i=0;
					while(iter.hasNext()) {
						i++;
						strContent += "\n工程"+i+":\n";
						Project proj = iter.next();
						strContent += "工程id:" + proj.getIdProject() + "; 工程名:"+proj.getNameProject() + "; 工程版本:" + proj.getVersionProject();
					}

					System.out.println(strContent);
					textMessage.setContent(strContent);
					// 将文本消息对象转换成xml字符串
					respMessage = textMessageUtil.messageToxml(textMessage);					
					break;

                }
                case "14#": {
                    //创建工程
                	stepOfNewProj = 1;
                	respContent = "请输入工程名";
                	System.out.println(respContent);
                	textMessage.setContent(respContent);
                	respMessage = textMessageUtil.messageToxml(textMessage);
                    break;
                }
                default: {
                	switch (stepOfNewProj){                	
                		case 0:
                			//进入状态机处理
                    		//respContent = "";
                			respContent = StateProccedue(reqContent);
                    		System.out.println(respContent);
                    		textMessage.setContent(respContent);
                    		respMessage = textMessageUtil.messageToxml(textMessage);
                    		break;
                    	//其余情况进入工程创建流程
                		case 1:
                			//进入新建工程第1步
                			mapProjInfo.put("NameProject", reqContent);
                    		stepOfNewProj = 2;
                    		respContent = "输入工程版本：";
                    		System.out.println(respContent);
                    		textMessage.setContent(respContent);
                    		respMessage = textMessageUtil.messageToxml(textMessage);
                    		break;
                		case 2:
                			mapProjInfo.put("VersionProject", reqContent);
                    		stepOfNewProj = 3;
                    		respContent = "输入工程注释：";
                    		System.out.println(respContent);
                    		textMessage.setContent(respContent);
                    		respMessage = textMessageUtil.messageToxml(textMessage);
                    		break;
                		case 3:
                			mapProjInfo.put("MemoProject", reqContent);
                    		stepOfNewProj = 4;
                    		respContent = "此工程是否开源（n:否，1:y）：";
                    		System.out.println(respContent);
                    		textMessage.setContent(respContent);
                    		respMessage = textMessageUtil.messageToxml(textMessage);
                    		break;
                		case 4:
                			if("y" == reqContent || "Y" == reqContent) {
                				mapProjInfo.put("OpenSource","1");
                			}else if("n" == reqContent || "N" == reqContent) {
                				mapProjInfo.put("OpenSource", "0");
                			}
                        	Project proj = new Project();
                        	proj.setNameProject(mapProjInfo.get("NameProject"));
                        	proj.setMemoProject(mapProjInfo.get("MemoProject"));
                        	//proj.setOpensourceProject(Integer.parseInt(mapProjInfo.get("OpenSource")));
                        	proj.setVersionProject(mapProjInfo.get("VerstionProject"));
                        	int ret = db.createProject(idUser,proj);
                        	if (0 == ret) {
                        		respContent = "工程已成功创建";            		
                        	}else {
                        		respContent = "工程创建失败";
                        		switch (ret) {
                        		case -1: 
                        			respContent += ":insert Project失败";
                        			break;
                        		case -2:
                        			respContent += ":insert Permission失败";
                        			break;
                        		case -3:
                        			respContent += ":insert Role失败";
                        			break;
                        		case -4:
                        			respContent += ":insert RolePRMSRelation失败";
                        			break;
                        		case -5:
                        			respContent += ":insert UserRoleRelation失败";
                        			break;
                        		default:
                        			break;
                        		}//switch(ret)                        		                                		
                        	}//if (0 == ret){...else                        	
                        	System.out.println(respContent);
                            textMessage.setContent(respContent);
                            // 将文本消息对象转换成xml字符串
                            respMessage = textMessageUtil.messageToxml(textMessage);
                            stepOfNewProj = 0;
                            break;
                		default:
                            respContent = "很抱歉，小牛无法处理你的命令。\n\n回复“1#”显示帮助信息";
                            textMessage.setContent(respContent);
                            // 将文本消息对象转换成xml字符串
                            respMessage = textMessageUtil.messageToxml(textMessage);
                            break;
                      }//switch (stepOfNewProj){
                }//default:
            }//switch (reqContent) {
*/

	    } catch (Exception e) {
	        e.printStackTrace();
	        respContent = "小牛处理请求异常，请稍候再试！";
	    }

	        return respMessage;
	}		
	/**
	 * 判断是否是QQ表情
	 *
	 * @param content
	 * @return
	 */
	public static boolean isQqFace(String content) {
	        boolean result = false;

        // 判断QQ表情的正则表达式
        String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
        Pattern p = Pattern.compile(qqfaceRegex);
        Matcher m = p.matcher(content);
        if (m.matches()) {
        	result = true;
        }
        return result;
	}
}

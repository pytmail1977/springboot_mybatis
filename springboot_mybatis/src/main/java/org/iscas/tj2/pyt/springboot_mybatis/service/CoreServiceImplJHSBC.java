package org.iscas.tj2.pyt.springboot_mybatis.service;

import java.util.List;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.iscas.tj2.pyt.springboot_mybatis.controller.LoginController;
import org.iscas.tj2.pyt.springboot_mybatis.dao.UserMapper;
import org.iscas.tj2.pyt.springboot_mybatis.domain.Project;
import org.iscas.tj2.pyt.springboot_mybatis.domain.User;
import org.iscas.tj2.pyt.springboot_mybatis.model.message.resp.TextMessage;
import org.iscas.tj2.pyt.springboot_mybatis.util.MessageUtil;
import org.iscas.tj2.pyt.springboot_mybatis.util.TextMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("JHSBC") //用于标注业务层组件
public class CoreServiceImplJHSBC implements ICoreService {	
    private TextMessageUtil textMessageUtil;	
	private DbService db = new DbService();
	int state = 0;
	int idUser = 0;

	Map<String, String> map = new HashMap<String, String>();
	
	@Override
	public String processRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "小牛处理请求异常，请稍候再试！";
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			//从数据库查询用户信息，记录Id_User
    		System.out.println("从数据库查询用户信息：");
        	User user = db.getUserInfo(fromUserName);
        	idUser = user.getId_User();
			
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
			// 如果是文本则回复文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //如果用户发送表情，则回复同样表情。
                if (isQqFace(reqContent)) {
                    respContent = reqContent;
                    textMessage.setContent(respContent);
                    // 将文本消息对象转换成xml字符串
                    respMessage = textMessageUtil.messageToxml(textMessage);
                } else {
                    //回复固定消息
                    switch (reqContent) {
                        case "1#": {
                            StringBuffer buffer = new StringBuffer();
                            buffer.append("您好，我是小牛，请回复数字选择服务：").append("\n\n");
                            buffer.append("11# 可查看软件定义卫星技术联盟网址").append("\n");
                            buffer.append("12# 可查看用户信息").append("\n");
                            buffer.append("13# 可查看用户的工程").append("\n");
                            buffer.append("14# 可创建新的工程").append("\n");
                            buffer.append("回复“1#”显示此帮助菜单").append("\n");
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
                        	state = 1;
                        	//System.out.println("请输入工程名");
                        	
                        	respContent = "请输入工程名";
                        	System.out.println(respContent);
                        	textMessage.setContent(respContent);
                        	respMessage = textMessageUtil.messageToxml(textMessage);
                            break;
                        }
                        default: {
                        	switch (state){
                        		case 1:
                        			map.put("NameProject", reqContent);
                            		state = 2;
                            		respContent = "输入工程版本：";
                            		System.out.println(respContent);
                            		textMessage.setContent(respContent);
                            		respMessage = textMessageUtil.messageToxml(textMessage);
                            		break;
                        		case 2:
                        			map.put("VersionProject", reqContent);
                            		state = 3;
                            		respContent = "输入工程注释：";
                            		System.out.println(respContent);
                            		textMessage.setContent(respContent);
                            		respMessage = textMessageUtil.messageToxml(textMessage);
                            		break;
                        		case 3:
                        			map.put("MemoProject", reqContent);
                            		state = 4;
                            		respContent = "此工程是否开源（n:否，1:y）：";
                            		System.out.println(respContent);
                            		textMessage.setContent(respContent);
                            		respMessage = textMessageUtil.messageToxml(textMessage);
                            		break;
                        		case 4:
                        			if("y" == reqContent || "Y" == reqContent) {
                        				map.put("OpenSource","1");
                        			}else if("n" == reqContent || "N" == reqContent) {
                        				map.put("OpenSource", "0");
                        			}
                                	Project proj = new Project();
                                	proj.setNameProject(map.get("NameProject"));
                                	proj.setMemoProject(map.get("MemoProject"));
                                	//proj.setOpensourceProject(Integer.parseInt(map.get("OpenSource")));
                                	proj.setVersionProject(map.get("VerstionProject"));
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
                                    state = 0;
                                    break;
                        		default:
                                    respContent = "很抱歉，小牛不理解你说的话。\n\n回复“1#”显示帮助信息";
                                    textMessage.setContent(respContent);
                                    // 将文本消息对象转换成xml字符串
                                    respMessage = textMessageUtil.messageToxml(textMessage);
                                    break;
                              }//switch (state){
                        }//default:
                    }//switch (reqContent) {
                }//if (isQqFace(reqContent)) {...else {
			}//if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

	        // 图片消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
	            respContent = "无法处理您发送的图片消息！";
	            textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);
	        }
	        // 地理位置消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
	            respContent = "无法处理您发送的地理位置消息！";
	            textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);
	        }
	        // 链接消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
	            respContent = "无法处理您发送的链接消息！";textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);

	        }
	        // 音频消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
	            respContent = "无法处理您发送的音频消息！";
	            textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
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

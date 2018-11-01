package org.iscas.tj2.pyt.springboot_mybatis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.iscas.tj2.pyt.springboot_mybatis.controller.LoginController;
//import org.iscas.tj2.pyt.springboot_mybatis.model.message.req.TextMessage;
import org.iscas.tj2.pyt.springboot_mybatis.model.message.resp.Article;
import org.iscas.tj2.pyt.springboot_mybatis.model.message.resp.NewsMessage;
import org.iscas.tj2.pyt.springboot_mybatis.model.message.resp.TextMessage;
import org.iscas.tj2.pyt.springboot_mybatis.util.BaseMessageUtilI;
import org.iscas.tj2.pyt.springboot_mybatis.util.MessageUtil;
import org.iscas.tj2.pyt.springboot_mybatis.util.TextMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CoreService") //用于标注业务层组件
public class CoreServiceImpl implements CoreService {
	
	@Autowired
    private TextMessageUtil textMessageUtil;
	@Override
	public String processRequest(HttpServletRequest request) {
	    //private static Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);

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
			// 回复文本消息，注意此处TextMessage是在包org.iscas.tj2.pyt.springboot_mybatis.model.message.resp.TextMessage中定义的
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			// 创建图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);

			List<Article> articleList = new ArrayList<Article>();

			// 接收文本消息内容
			String content = requestMap.get("Content");
			
			// 根据收到的信息种类自动回复消息
			// 如果是文本则回复文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //如果用户发送表情，则回复同样表情。
                if (isQqFace(content)) {
                    respContent = content;
                    textMessage.setContent(respContent);
                    // 将文本消息对象转换成xml字符串
                    respMessage = textMessageUtil.messageToxml(textMessage);
                } else {
                    //回复固定消息
                    switch (content) {

                        case "1": {
                            StringBuffer buffer = new StringBuffer();
                            buffer.append("您好，我是小牛，请回复数字选择服务：").append("\n\n");
                            buffer.append("11 可查看测试单图文").append("\n");
                            buffer.append("12 可测试多图文发送").append("\n");
                            buffer.append("13 可测试网址").append("\n");

                            buffer.append("或者您可以尝试发送表情").append("\n\n");
                            buffer.append("回复“1”显示此帮助菜单").append("\n");
                            respContent = String.valueOf(buffer);
                            textMessage.setContent(respContent);
                            respMessage = textMessageUtil.messageToxml(textMessage);
                            break;
                        }
                        case "11": {
                            //测试单图文回复
                        	//增加日志
                            Logger log = LoggerFactory.getLogger(LoginController.class);
                            log.info("测试单图文回复");
                            Article article = new Article();
                            article.setTitle("微信公众帐号——小牛的编程助手");
                            // 图文消息中可以使用QQ表情、符号表情
                            article.setDescription("这是测试有没有换行\n\n如果有空行就代表换行成功\n\n点击图文可以跳转到百度首页");
                            // 将图片置为空
                            article.setPicUrl("https://mmbiz.qpic.cn/mmbiz_jpg/Dz5tz72vntWuu9ibek2Diaa1WYYCvpM1kibdhL2jQr0xia4fiaS0m9MrRBNQB3Xyic31ReWiczpBnfH8HDe8Q6ZVC18dQ/0?wx_fmt=jpeg");
                            article.setUrl("http://network.pconline.com.cn/1106/11067984.html");
                            articleList.add(article);
                            newsMessage.setArticleCount(articleList.size());
                            newsMessage.setArticles(articleList);
                            
                            respMessage = textMessageUtil.messageToxml(textMessage);
                            break;
                        }
                        case "12": {
                            //多图文发送
                            Article article1 = new Article();
                            article1.setTitle("我国首颗软件定义卫星\"天智一号\"研制进展顺利 将于今年下半年发射！\n");
                            article1.setDescription("");
                            article1.setPicUrl("http://n.sinaimg.cn/news/crawl/96/w537h359/20180410/XGg5-fyvtmxe8881158.jpg");
                            article1.setUrl("http://news.sina.com.cn/gov/2018-04-10/doc-ifyvtmxe8884554.shtml");

                            Article article2 = new Article();
                            article2.setTitle("中国首颗软件定义卫星“天智一号”将于下半年发射！");
                            article2.setDescription("");
                            article2.setPicUrl("http://n.sinaimg.cn/translate/33/w500h333/20180407/LpJp-fyvtmxc6022038.jpg");
                            article2.setUrl("http://news.sina.com.cn/o/2018-04-07/doc-ifyuwqez6323417.shtml");

                            Article article3 = new Article();
                            article3.setTitle("专家：软件定义卫星将让普通大众通过智能机访问使用");
                            article3.setDescription("");
                            //article3.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
                            article3.setUrl("http://tech.sina.com.cn/d/s/2018-04-08/doc-ifyvtmxe1020279.shtml");

                            articleList.add(article1);
                            articleList.add(article2);
                            articleList.add(article3);
                            newsMessage.setArticleCount(articleList.size());
                            newsMessage.setArticles(articleList);
                            respMessage = textMessageUtil.messageToxml(textMessage);
                            break;
                        }

                        case "13": {
                            //测试网址回复
                            respContent = "<a href=\"http://www.sdsalliance.net\">软件定义卫星技术联盟</a>";
                            textMessage.setContent(respContent);
                            // 将文本消息对象转换成xml字符串
                            respMessage = textMessageUtil.messageToxml(textMessage);
                            break;
                        }

                        default: {
                            respContent = "很抱歉，现在小牛暂时无法提供此功能给您使用。\n\n回复“1”显示帮助信息";
                            textMessage.setContent(respContent);
                            // 将文本消息对象转换成xml字符串
                            respMessage = textMessageUtil.messageToxml(textMessage);
                        }
                    }
                }
			}

	        // 图片消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
	            respContent = "您发送的是图片消息！";
	            textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);
	        }
	        // 地理位置消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
	            respContent = "您发送的是地理位置消息！";
	            textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);
	        }
	        // 链接消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
	            respContent = "您发送的是链接消息！";textMessage.setContent(respContent);
	            // 将文本消息对象转换成xml字符串
	            respMessage = textMessageUtil.messageToxml(textMessage);

	        }
	        // 音频消息
	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
	            respContent = "您发送的是音频消息！";
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

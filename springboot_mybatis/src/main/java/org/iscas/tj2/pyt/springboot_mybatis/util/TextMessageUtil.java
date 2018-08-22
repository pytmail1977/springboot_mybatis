package org.iscas.tj2.pyt.springboot_mybatis.util;

import java.util.Date;

import org.iscas.tj2.pyt.springboot_mybatis.model.message.TextMessage;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

@Service("TextMessageUtil")
public class TextMessageUtil extends MessageUtil implements BaseMessageUtilI<TextMessage> {

	@Override
	public String messageToxml(TextMessage  message) {
		// TODO Auto-generated method stub
		//return null;
		XStream xstream  = new XStream();
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);

	}

	@Override
	public String initMessage(String FromUserName, String ToUserName) {
		// TODO Auto-generated method stub
		//return null;
		TextMessage text = new TextMessage();
		text.setToUserName(FromUserName);
		text.setFromUserName(ToUserName);
		text.setContent("欢迎来到小牛的编程助手");
		text.setCreateTime(new Date().getTime());
		text.setMsgType("text");
	        return  messageToxml(text);

	}

}

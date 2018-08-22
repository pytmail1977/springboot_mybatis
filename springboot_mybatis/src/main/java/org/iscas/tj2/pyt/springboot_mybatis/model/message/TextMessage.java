package org.iscas.tj2.pyt.springboot_mybatis.model.message;

public class TextMessage extends BaseMessage {

	private String Content;//文本消息内容
	
	private String MsgId;//消息id，64位整型

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
    
	public String getMsgId() {
		return MsgId;
	}
 
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	
	public TextMessage(){
		
	}
 
	
	public TextMessage(String toUserName, String fromUserName,
			long createTime, String msgType, String content, String msgId) {
		super();
		ToUserName = toUserName;
		FromUserName = fromUserName;
		CreateTime = createTime;
		MsgType = msgType;
		Content = content;
		MsgId = msgId;
	}

}

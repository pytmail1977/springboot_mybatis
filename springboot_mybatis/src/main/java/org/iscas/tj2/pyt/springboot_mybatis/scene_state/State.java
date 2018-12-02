package org.iscas.tj2.pyt.springboot_mybatis.scene_state;

import org.iscas.tj2.pyt.springboot_mybatis.SceneType;
import org.springframework.beans.factory.annotation.Autowired;

public class State {

	private String strTable; // 上下文所涉及的表名
	private int intId; //在相关表中的id
	private String strComment; //上下文描述
	private int intDepth; //上下文深度
	private SceneType sceneType;//状态所属类型，需在构造函数中初始化
	
	//2018-11-18 取消这两个成员
	//增加两项，分别是最近的用户输入，和最近给用户的输出
	//private String reqContent;
	//private String respContent;
	
	/**
	 * 构造函数
	 * @param intId
	 * @param strTable
	 * @param intDepth
	 * @param strComment
	 */
	public State(int intId, String strTable,int intDepth, String strComment,SceneType sceneType) {
		this.intId = intId;
		this.strTable = strTable;
		this.intDepth = intDepth;
		this.strComment = strComment;
		this.sceneType = sceneType;
	}

	public String getStrTable() {
		return strTable;
	}
	
	public String getStrComment() {
		return strComment;
	}	
	public Integer getIntId() {
		return intId;
	}

	public Integer getIntDepth() {
		return intDepth;
	}

	
	public void setStrTable(String strTable) {
		this.strTable = strTable;
	}
	
	public void setStrComment(String strComment) {
		this.strComment = strComment;
	}	
	
	public void setIntId(Integer intId) {
		this.intId = intId;
	}

	public void setIntDepeth(Integer intDepth) {
		this.intDepth = intDepth;
	}

	public SceneType getSceneType() {
		return sceneType;
	}

	public void setSceneType(SceneType sceneType) {
		this.sceneType = sceneType;
	}



/*	public String getRespContent() {
		return respContent;
	}

	public void setRespContent(String respContent) {
		this.respContent = respContent;
	}

	public String getReqContent() {
		return reqContent;
	}

	public void setReqContent(String reqContent) {
		this.reqContent = reqContent;
	}*/
	
}

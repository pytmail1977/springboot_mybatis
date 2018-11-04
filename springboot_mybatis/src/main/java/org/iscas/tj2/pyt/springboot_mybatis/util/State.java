package org.iscas.tj2.pyt.springboot_mybatis.util;

import org.springframework.beans.factory.annotation.Autowired;

public class State {

	private String strTable; // 上下文所涉及的表名
	private int intId; //在相关表中的id
	private String strComment; //上下文描述
	private int intDepth; //上下文深度
	public State(int intId, String strTable,int intDepth, String strComment) {
		this.intId = intId;
		this.strTable = strTable;
		this.intDepth = intDepth;
		this.strComment = strComment;
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
	
}

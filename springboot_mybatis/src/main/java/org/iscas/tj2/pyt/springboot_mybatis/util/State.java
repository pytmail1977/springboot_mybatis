package org.iscas.tj2.pyt.springboot_mybatis.util;

import org.iscas.tj2.pyt.springboot_mybatis.StateType;
import org.springframework.beans.factory.annotation.Autowired;

public class State {

	private String strTable; // 上下文所涉及的表名
	private int intId; //在相关表中的id
	private String strComment; //上下文描述
	private int intDepth; //上下文深度
	private StateType stateType;//状态所属类型，需在构造函数中初始化
	
	/**
	 * 构造函数
	 * @param intId
	 * @param strTable
	 * @param intDepth
	 * @param strComment
	 */
	public State(int intId, String strTable,int intDepth, String strComment,StateType stateType) {
		this.intId = intId;
		this.strTable = strTable;
		this.intDepth = intDepth;
		this.strComment = strComment;
		this.stateType = stateType;
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

	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
	
}

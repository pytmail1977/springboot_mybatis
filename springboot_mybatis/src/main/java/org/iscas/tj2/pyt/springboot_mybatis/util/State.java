package org.iscas.tj2.pyt.springboot_mybatis.util;

import org.springframework.beans.factory.annotation.Autowired;

public class State {

	@Autowired
	private String strTable;
	private int intId;
	public State(int intId, String strTable) {
		this.intId = intId;
		this.strTable = strTable;
	}
	
}

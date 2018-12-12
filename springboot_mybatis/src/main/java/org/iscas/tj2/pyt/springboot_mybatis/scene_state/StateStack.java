package org.iscas.tj2.pyt.springboot_mybatis.scene_state;

import org.iscas.tj2.pyt.springboot_mybatis.SceneType;

public class StateStack {

	private int max_size;
	private int top;
	private State[ ] stack;
	
	//为按场景的枚举类型返回中文，定义如下数组
	private String[] STCNAME = {
			"用户","结构","结构字段","类型","工程","函数","函数语句","函数变量","函数变量字段","全局变量","全局变量字段","共用对象","共用对象(管理员模式)"
	};

	
	public StateStack(int size) {
		// TODO Auto-generated constructor stub
		this.max_size = size;
		this.top = 0;
		this.stack = new State[max_size];
		
	}
	
	public int push(State state) {
		stack[top++] = state;
		return top;
	}	
	
	public State pop() {
		return stack[top--];
	}
	
	public int sizeof() {
		return top;
	}
	
	public void clean() {
		top = 0;
	}
	
	//增加一个返回当前上下文提示字符串的方法
	public String getContext() {
		String strTmp = "";
		for(int i = 0;i<top; i++) {
			strTmp += stack[i].getStrComment();
			strTmp += "/";
		}
		strTmp += ":>";
		return strTmp;		
	}
	
	//2018-11-18 增加一个返回当前场景的函数
/*	public String getPwd() {
		String strTmp = "";
		for(int i = 0;i<top; i++) {
			//strTmp += stack[i].getSceneType().toString();
			strTmp += STCNAME[stack[i].getSceneType().ordinal()]+stack[i].getIntId();
			strTmp += "/";
		}
		strTmp += ":>";
		return strTmp;		
	}*/
	//2018-12-12 如果没有第1层栈，或第1层栈不是STUser那么把第0层栈（即共用对象显示出来），否则不显示。
	public String getPwd() {
		String strTmp = "";
		int begin = 0;
		if(top>1?stack[1].getSceneType().equals(SceneType.STUser):false) {
			begin = 1;
		}
		for(int i = begin;i<top; i++) {
			//strTmp += stack[i].getSceneType().toString();
			strTmp += STCNAME[stack[i].getSceneType().ordinal()]+(i==0?"":stack[i].getIntId());
			strTmp += "/";
		}
		strTmp += ":>";
		return strTmp;		
	}

	/**
	 * 返回当前状态栈的用户id
	 * @return
	 * -1:当前用户栈为空或指定深度超出了范围
	 * 其它：当前栈的第depth层的id
	 */
	public int getWhoseId(int depth) {
		if(top>=depth && depth>=0) {
			return stack[depth].getIntId();
		}else
			return -1;
	}
	
	
	/**
	 * 取当前状态
	 * @return
	 */
	public State getCurrentState() {
		return stack[top-1];
	}



}

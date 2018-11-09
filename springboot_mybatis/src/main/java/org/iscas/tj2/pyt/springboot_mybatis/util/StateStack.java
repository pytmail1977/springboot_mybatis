package org.iscas.tj2.pyt.springboot_mybatis.util;

public class StateStack {

	private int max_size;
	private int top;
	private State[ ] stack;
	
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
		return stack[top];
	}



}

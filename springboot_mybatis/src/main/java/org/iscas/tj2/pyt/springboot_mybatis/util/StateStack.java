package org.iscas.tj2.pyt.springboot_mybatis.util;

public class StateStack {

	private int max_size;
	private int top;
	private State[ ] stack;
	
	public StateStack(int size) {
		// TODO Auto-generated constructor stub
		this.max_size = size;
		this.top = -1;
		this.stack = new State[max_size];		
	}
	
	public int push(State state) {
		stack[top++] = state;
		return top;
	}
	

}

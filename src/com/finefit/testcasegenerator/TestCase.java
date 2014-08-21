package com.finefit.testcasegenerator;


import java.io.PrintStream;

public class TestCase {
	
	private State state;
	private Operation operation;
	 
	public TestCase(Operation operation, State state) {
		this.operation = operation;
		this.state = state;
	}

	public State getState() { return state; }

	public Operation getOperation() {
		return operation;
	}

	public String getOperationName() {
		return operation.getName();
	}

	public void print(PrintStream out) {
		out.print(operation.getName()); 
		operation.printCall(state, out);
		out.println(" -> ");
	}

}

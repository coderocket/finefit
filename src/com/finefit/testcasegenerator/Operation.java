package com.finefit.testcasegenerator;

import edu.mit.csail.sdg.alloy4compiler.ast.Func;

public class Operation {

	private Func operation;
	
	public Operation (Func operation){
		this.operation = operation;
	}

	public Func getOperation() {
		return operation;
	}

	public void setOperation(Func operation) {
		this.operation = operation;
	}
	
	public String getName(){
		return this.operation.label;
	}
}

package com.finefit.oracle;

import com.finefit.testcasegenerator.Operation;
import com.finefit.testcasegenerator.SystemState;
import com.finefit.testcasegenerator.TestCase;

import kodkod.ast.Formula;
import kodkod.engine.Evaluator;

public class TestOracle {

	private SystemState currentSystemState = null; //currentInstance
	private Evaluator evaluator = null;
	private Formula formula = null;
	private Operation operation = null;
	
	public TestOracle(SystemState instance, SystemState instanceToEvaluate, TestCase testCase){
		this.currentSystemState = instance;
		this.formula = testCase.getFormula(testCase.getOperation());
		this.evaluator = new Evaluator(instanceToEvaluate.getState());
		this.operation = testCase.getOperation();
	}
	
	public boolean isValid(){
		if(evaluator.evaluate(formula)){
			return true;
		}
		return false;
	}
}

package com.finefit.sutinterface;

import kodkod.instance.Instance;
import kodkod.instance.Universe;
import com.finefit.testcasegenerator.SystemState;
import com.finefit.testcasegenerator.TestCase;
import com.finefit.testcasegenerator.StateVariables;
import com.finefit.testcasegenerator.State;

public interface SUT {
	 
	public class InvalidNumberOfArguments extends Exception {}
    public class NoSuchOperation extends Exception {}

    public State initialize(State state);
    public State applyOperation(TestCase testCase) throws InvalidNumberOfArguments, NoSuchOperation;
}

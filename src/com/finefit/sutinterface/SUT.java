package com.finefit.sutinterface;

import kodkod.instance.Instance;
import kodkod.instance.Universe;
import com.finefit.testcasegenerator.SystemState;
import com.finefit.testcasegenerator.TestCase;
import com.finefit.testcasegenerator.StateVariables;

public interface SUT {
	 
	public class InvalidNumberOfArguments extends Exception {}
    public class NoSuchOperation extends Exception {}
    public class NoDataException extends Exception { public NoDataException(String msg){super(msg);}}
    public SystemState initialize(Universe universe,Instance args);
    public SystemState applyOperation(TestCase testCase, Instance solutionArgs) throws InvalidNumberOfArguments, NoSuchOperation, NoDataException;
}

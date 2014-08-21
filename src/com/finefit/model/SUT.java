package com.finefit.model;

public interface SUT {
	 
		public class InvalidNumberOfArguments extends Exception {}
    public class NoSuchOperation extends Exception {}

    public State initialize(State state);
    public State applyOperation(TestCase testCase) throws InvalidNumberOfArguments, NoSuchOperation;
}

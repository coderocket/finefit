package com.finefit.testcasegenerator;

import kodkod.instance.Universe;
import com.finefit.entitydefiner.EntityLocator;


public class TestCaseGenerator {

	public static ConstraintSolver constraintSolver;
	private EntityLocator entityLocator;//alloyParser
	
	public TestCaseGenerator(String specification){
		this.entityLocator = new EntityLocator(specification);
		this.constraintSolver = new ConstraintSolver(
				entityLocator.getWorld());
	}
	 
	public TestCase generateTestCase(SystemState currentState){
		return new TestCase(currentState);
	}
	
	public SystemState getInitialArgs(){
		return new SystemState(constraintSolver.getSolution().instance());
	}	
	
	public Universe getUniverse(){
		return constraintSolver.getUniverse();
	}
	
}

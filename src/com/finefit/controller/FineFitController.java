package com.finefit.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import kodkod.engine.Solution;
import com.finefit.oracle.TestOracle;
import com.finefit.reporter.Reporter;
import com.finefit.sutinterface.SUT;
import com.finefit.sutinterface.SUT.InvalidNumberOfArguments;
import com.finefit.sutinterface.SUT.NoDataException;
import com.finefit.sutinterface.SUT.NoSuchOperation;
import com.finefit.testcasegenerator.SystemState;
import com.finefit.testcasegenerator.TestCase;
import com.finefit.testcasegenerator.TestCaseGenerator;
import com.finefit.translator.Translator;

public class FineFitController {
	
	public static void main(String[] args) throws ParseException, InvalidNumberOfArguments, NoSuchOperation, NoDataException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		checkNumberOfArgs(args);
		String tabularSystemSpec = args[0];
		String systemDriverClassName = args[1];
		String systemDataFileName = args[2];
		
		// --------- Translate Tabular Specification File ----------
		Translator translator = new Translator();
		
		String systemSpec = translator.translateSpecificationFile(tabularSystemSpec);
		// --------- END Translate Tabular Specification File ------

		// --------- Generate TestCase -----------------------------
		TestCaseGenerator testCaseGenerator = new TestCaseGenerator(systemSpec);

		SUT sut = (SUT) getSutObject(systemDriverClassName, systemDataFileName);// run the selected sut (from arguments) 
		
		// --------- Initializing system state and generate Test Case ---- 
		SystemState currentState = sut.initialize(testCaseGenerator.getUniverse(), testCaseGenerator.getInitialArgs().getState());
		TestCase testCase = testCaseGenerator.generateTestCase(currentState);

		// --------- Testing loop ----------------------------------------
		boolean foundError = false;
		while (!foundError) {

			Solution solution = testCase.findSolution();
			boolean isSolutionSatisfiable = testCase.isSolutionSatisfiable(solution);
			Reporter reporter = new Reporter(new SystemState(solution.instance()), testCase);
 
			if (isSolutionSatisfiable) {

				System.out.print("Current System State: ");
				testCase.getSystemState().printSystemStateRelations();
				System.out.println("\nApplying on Operation - " + testCase.getOperationName() + " -> ");
				SystemState nextState = sut.applyOperation(testCase, solution.instance());
				System.out.print("Next System State: ");
				nextState.printSystemStateRelations();
				SystemState stateToEvaluate = new SystemState(testCase.relateSystemState(nextState.getState(), solution.instance()));

				TestOracle testOracle = new TestOracle(new SystemState(solution.instance()), stateToEvaluate, testCase);
				if (testOracle.isValid()) { 
					currentState = nextState;
					testCase = testCaseGenerator.generateTestCase(currentState);
					reporter.printOnSuccessTestTrace();
				} else {
					reporter.printOnFailureTestTrace();
					foundError = true;
				}
			} else {
				foundError = true;
			}
		}
	}
	
	private static void checkNumberOfArgs(String[] args){
		if(args.length != 3){
			throw new IllegalArgumentException("Wrong number of arguments were given");
		}
	}
	
	private static Object getSutObject(String javaClassName, String dataFileName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException{
		String sutPackagename = javaClassName.replace("Driver", "").toLowerCase();
		String str = "com.finefit.sut." + sutPackagename + "." + javaClassName;
		Class<?> myClass = Class.forName(str);
		Constructor<?> constructor = myClass.getConstructor(String.class);
		Object sutObject = constructor.newInstance(dataFileName);
		System.out.println("Testing " + sutObject.getClass().getSimpleName().replace("Driver", "") + "\n");
		return sutObject;
	}
}

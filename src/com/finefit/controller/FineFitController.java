package com.finefit.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
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

final static String SYSTEM_SPECIFICATION = "SystemSpecification.als";
	
	public static void main(String[] args) throws ParseException, InvalidNumberOfArguments, NoSuchOperation, NoDataException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, MalformedURLException, FileNotFoundException {
         	if(args.length != 2) {
                        System.out.println("Wrong number of arguments. Required: <specification> <driver>");
			System.exit(1);
		}

		String tabularSystemSpec = args[0];
		String systemDriverClassName = args[1];
		
		// --------- Translate Tabular Specification File ----------
		
		Translator.translate(tabularSystemSpec, SYSTEM_SPECIFICATION);
	
		// --------- Generate TestCase -----------------------------
		TestCaseGenerator testCaseGenerator = new TestCaseGenerator(SYSTEM_SPECIFICATION);

		SUT sut = (SUT) getSutObject(systemDriverClassName);// run the selected sut (from arguments) 
		
		// --------- Initializing system state  ---- 
		SystemState currentState = sut.initialize(testCaseGenerator.getUniverse(), testCaseGenerator.getInitialArgs().getState());
	
		// --------- Testing loop ----------------------------------------
		
		System.out.println("	init ->");
		currentState.printSystemStateRelations();
		
		TestCase testCase = testCaseGenerator.generateTestCase(currentState);
		
		boolean foundError = false;
		while (!foundError) {

			Solution solution = testCase.findSolution();
			boolean isSolutionSatisfiable = testCase.isSolutionSatisfiable(solution);
			Reporter reporter = new Reporter(new SystemState(solution.instance()), testCase);

			if (isSolutionSatisfiable) {
				
				System.out.print("	" + testCase.getOperation().getName());
				testCase.getOperation().printCall(solution.instance(), System.out);
				System.out.println(" ->");
				SystemState nextState = sut.applyOperation(testCase, solution.instance());
				nextState.printSystemStateRelations();
				
				SystemState stateToEvaluate = new SystemState(testCase.relateSystemState(nextState.getState(), solution.instance()));

				TestOracle testOracle = new TestOracle(new SystemState(solution.instance()), stateToEvaluate, testCase);
				if (testOracle.isValid()) { 
					currentState = nextState; 
					testCase = testCaseGenerator.generateTestCase(currentState);
				} else {
					System.out.println("\nSTATE DISCREPANCY");
					foundError = true;
				}
			} else {
				System.out.println("\nDEADLOCK");
				foundError = true;
			}
		}
	}
	
	private static Object getSutObject(String javaClassName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException, MalformedURLException{
            
            File file = new File(".");
            URL url = file.toURI().toURL();          // file:/c:/myclasses/
            URL[] urls = new URL[]{url};
            ClassLoader loader = new URLClassLoader(urls);
            
            Class driver = loader.loadClass(javaClassName);
            return driver.getConstructor().newInstance();
            
	}
}

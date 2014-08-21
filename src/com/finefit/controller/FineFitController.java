package com.finefit.controller;

import java.util.Random;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import kodkod.engine.Solution;
import kodkod.instance.Instance;
import kodkod.instance.Bounds;
import kodkod.util.ints.IndexedEntry;
import kodkod.instance.TupleSet;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompParser;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;

import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4compiler.translator.BoundsExtractor;


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

import com.finefit.testcasegenerator.Model;
import com.finefit.testcasegenerator.TestCaseGeneratorX;
import com.finefit.testcasegenerator.State;
import com.finefit.oracle.TestOracleX;


public class FineFitController {

final static String SYSTEM_SPECIFICATION = "SystemSpecification.als";
	
	public static void main(String[] args) { 

		if(args.length != 2) {
      System.out.println("Wrong number of arguments. Required: <specification> <driver>");
			System.exit(1);
		}

		String tabularSystemSpec = args[0];
		String systemDriverClassName = args[1];

		try {

			Translator.translate(tabularSystemSpec, SYSTEM_SPECIFICATION);
			Model model = new Model(SYSTEM_SPECIFICATION);
			TestCaseGeneratorX testCaseGenerator = new TestCaseGeneratorX(model);
			TestOracleX testOracle = new TestOracleX();

			Random RNG = new Random();

			SUT sut = (SUT) getSutObject(systemDriverClassName); // run the selected sut (from arguments)

			Set<TestCase> candidates = testCaseGenerator.first();

			if (candidates.isEmpty()) {
				System.out.println("Error: Could not find an initial valid state. Youre model is probably inconsistent.");
				System.exit(1);
			}

			TestCase initialTestCase = any(RNG, candidates);

			initialTestCase.print(System.out);

			State prevState = null;
			State currState = sut.initialize(initialTestCase.getState());

			System.out.println(currState);

			boolean behavior_is_valid = testOracle.verify(model.context(), initialTestCase.getOperation(), currState, currState);

			candidates = testCaseGenerator.next(currState);

			while (behavior_is_valid && !candidates.isEmpty()) { 
					TestCase testcase = any(RNG, candidates);
					testcase.print(System.out);
					prevState = currState;
					currState = sut.applyOperation(testcase);
					System.out.println(currState);
					behavior_is_valid = testOracle.verify(model.context(), testcase.getOperation(), prevState, currState);
					candidates = testCaseGenerator.next(currState);
			}
			
			if (!behavior_is_valid)
				System.out.println("\nError: Discrepancy.");
			else 
				System.out.println("\nError: Deadlock.");

		} catch(Exception err) {
			err.printStackTrace();
		}
	}

/*
		try {
			CompModule world = CompUtil.parseEverything_fromFile(new A4Reporter(), null, SYSTEM_SPECIFICATION);
			A4Solution s = TranslateAlloyToKodkod.execute_command(new A4Reporter(), world.getAllReachableSigs(), world.getAllCommands().get(0), new A4Options());
			Bounds b = new BoundsExtractor(s).getBounds();
			System.out.println(b);
		}
		catch(Err e) {
			e.printStackTrace();
		}
	}	


		// --------- Generate TestCase -----------------------------
		TestCaseGenerator testCaseGenerator = new TestCaseGenerator(SYSTEM_SPECIFICATION);

		SUT sut = (SUT) getSutObject(systemDriverClassName);// run the selected sut (from arguments) 
		
		// --------- Initializing system state  ---- 
		SystemState initialState = testCaseGenerator.getInitialArgs();
		SystemState currentState = sut.initialize(initialState.getState().universe(), initialState.getState()); 
		addInts(currentState.getState(), initialState.getState());

		// --------- Testing loop ----------------------------------------
		
		System.out.println("	init ->");
		currentState.printSystemStateRelations();
		//System.out.println(currentState.getState());
		
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
				//System.out.println(currentState.getState());
				
				SystemState stateToEvaluate = new SystemState(testCase.relateSystemState(nextState.getState(), solution.instance()));
				addInts(stateToEvaluate.getState(), solution.instance());

				TestOracle testOracle = new TestOracle(new SystemState(solution.instance()), stateToEvaluate, testCase);
				if (testOracle.isValid()) { 
					currentState = nextState; 
					testCase = testCaseGenerator.generateTestCase(currentState);
				} else {
					System.out.println("\nSTATE DISCREPANCY");
					System.out.println(solution.instance());
					System.out.println(stateToEvaluate.getState());
					foundError = true;
				}
			} else {
				System.out.println("\nDEADLOCK");
				foundError = true;
			}
		}
	}
*/
	
	private static Object getSutObject(String javaClassName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException, MalformedURLException{
            
            File file = new File(".");
            URL url = file.toURI().toURL();          // file:/c:/myclasses/
            URL[] urls = new URL[]{url};
            ClassLoader loader = new URLClassLoader(urls);
            
            Class driver = loader.loadClass(javaClassName);
            return driver.getConstructor().newInstance();
            
	}

	private static void addInts(Instance inst, Instance instWithInts) {
		Iterator<IndexedEntry<TupleSet> > p = instWithInts.intTuples().iterator();
    while (p.hasNext()) {
   		IndexedEntry<TupleSet> e = p.next();
			inst.add(e.index(), e.value());     
    }
	}

	private static TestCase any(Random RNG, Set<TestCase> candidates) {
		int index = RNG.nextInt(candidates.size());
		Iterator<TestCase> p = candidates.iterator();
		while (index != 0) {
			p.next(); 
			--index;
		}
		return p.next();
	}
}

package com.finefit.controller;

import java.util.Random;
import java.util.Set;
import java.util.Iterator;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import com.finefit.reporter.Reporter;
import com.finefit.sutinterface.SUT;
import com.finefit.translator.Translator;
import com.finefit.testcasegenerator.TestCase;
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

       private static Object getSutObject(String javaClassName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException, MalformedURLException {

            File file = new File(".");
            URL url = file.toURI().toURL();          // file:/c:/myclasses/
            URL[] urls = new URL[]{url};
            ClassLoader loader = new URLClassLoader(urls);

            Class driver = loader.loadClass(javaClassName);
            return driver.getConstructor().newInstance();
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

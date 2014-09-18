/* Copyright 2014 David Faitelson

This file is part of FineFit.

FineFit is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

FineFit is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with FineFit. If not, see <http://www.gnu.org/licenses/>.

*/

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
import com.finefit.model.SUT;
import com.finefit.model.TestCase;
import com.finefit.model.Model;
import com.finefit.model.State;
import com.finefit.translator.Translator;
import com.finefit.testcasegenerator.TestCaseGenerator;
import com.finefit.oracle.TestOracle;
import com.finefit.reporter.Reporter;
import com.finefit.reporter.PrintStreamReporter;

public class FineFitController {

final static String SYSTEM_SPECIFICATION = "SystemSpecification.als";
final static String VERSION = "1.0";
	
	public static void main(String[] args) { 

		if(args.length != 2) {
      System.out.println("This is FineFit version " + VERSION + "\n\nWrong number of arguments. Required: <specification> <driver>");
			System.exit(1);
		}

		String tabularSystemSpec = args[0];
		String systemDriverClassName = args[1];

		try {

			Translator.translate(tabularSystemSpec, SYSTEM_SPECIFICATION, null); // TODO: add the commons CLI options library to support command line options (the last parameter is a placeholder for the finefit home directory, currently we use the FINEFIT_HOME environment variable.

			Model model = new Model(SYSTEM_SPECIFICATION);
			TestCaseGenerator testCaseGenerator = new TestCaseGenerator(model);
			TestOracle testOracle = new TestOracle();
			Reporter reporter = new PrintStreamReporter(System.out);

			Random RNG = new Random();

			SUT sut = (SUT) getSutObject(systemDriverClassName); // run the selected sut (from arguments)

			Set<TestCase> candidates = testCaseGenerator.first();

			if (candidates.isEmpty()) {
				System.out.println("Error: Could not find an initial valid state. Youre model is probably inconsistent.");
				System.exit(1);
			}

			TestCase initialTestCase = any(RNG, candidates);

			State prevState = null;
			State currState = initialTestCase.getState().clone();
			currState.read(sut.initialize(initialTestCase.getState()));

			reporter.report(initialTestCase, currState);

			boolean behavior_is_valid = testOracle.verify(model, initialTestCase.getOperation(), currState, currState);

			candidates = testCaseGenerator.next(currState);

			while (behavior_is_valid && !candidates.isEmpty()) { 
					TestCase testcase = any(RNG, candidates);
					prevState = currState;
					currState = testcase.getState().clone();
					currState.read(sut.applyOperation(testcase));
					reporter.report(testcase, currState);
					behavior_is_valid = testOracle.verify(model, testcase.getOperation(), prevState, currState);
					candidates = testCaseGenerator.next(currState);
			}
			
			if (!behavior_is_valid)
				reporter.report_discrepancy();
			else 
				reporter.report_deadlock();

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

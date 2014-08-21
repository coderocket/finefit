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

/*

TODO: add configuration options to display all candidates
*/

package com.finefit.testcasegenerator;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import kodkod.engine.Solution;
import kodkod.engine.Solution.Outcome;
import kodkod.ast.Formula;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4.Err;
import com.finefit.model.Model;
import com.finefit.model.Operation;
import com.finefit.model.TestCase;
import com.finefit.model.State;

public class TestCaseGenerator {

	Model model;
	ConstraintSolver solver;

	public TestCaseGenerator(Model model) {
		this.model =  model;
		solver = new ConstraintSolver();
	}

	public Set<TestCase> first() throws Err {

		Operation p = model.getInit();

		Set<TestCase> candidates = new HashSet<TestCase>();

		Solution solution = solver.solve(model.context(), p.getFormula(model.context()));
		if (solution != null && (solution.outcome() == Outcome.SATISFIABLE || solution.outcome() == Outcome.TRIVIALLY_SATISFIABLE))
			candidates.add(new TestCase(p, new State(solution.instance())));
		return candidates;
	}

	public Set<TestCase> next(State currentState) {
	
		Set<TestCase> candidates = new HashSet<TestCase>();

		for(Operation p : model.operations()) {
			Iterator<Solution> it = solver.solve(model.context(), p.getFormula(model.context()), currentState);
			while (it.hasNext() ) {
				Solution solution = it.next();
				if (solution != null && (solution.outcome() == Outcome.SATISFIABLE || solution.outcome() == Outcome.TRIVIALLY_SATISFIABLE))
					candidates.add(new TestCase(p, new State(solution.instance())));
			}
		}
		return candidates;
	}

}

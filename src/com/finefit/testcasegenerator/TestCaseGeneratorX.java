/*

TODO: add configuration options to display all candidates
*/

package com.finefit.testcasegenerator;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import kodkod.engine.Solution;
import kodkod.ast.Formula;
import kodkod.engine.Solution.Outcome;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4.Err;

public class TestCaseGeneratorX {

	Model model;
	ConstraintSolverX solver;

	public TestCaseGeneratorX(Model model) {
		this.model =  model;
		solver = new ConstraintSolverX();
	}

	public Set<TestCase> first() throws Err {

		Operation p = model.getInit();

		Set<TestCase> candidates = new HashSet<TestCase>();

		Solution solution = solver.solve(model.context(), p.getFormula(model.context()));
		if (solution != null && (solution.outcome() == Outcome.SATISFIABLE || solution.outcome() == Outcome.TRIVIALLY_SATISFIABLE))
			candidates.add(new TestCase(p, new State(solution.instance())));
		return candidates;
	}

	public Set<TestCase> next(State state) {
	
		Set<TestCase> candidates = new HashSet<TestCase>();

		for(Operation p : model.operations()) {
			Iterator<Solution> it = solver.solve(model.context(), p.getFormula(model.context()), state);
			while (it.hasNext() ) {
				Solution solution = it.next();
				if (solution != null && (solution.outcome() == Outcome.SATISFIABLE || solution.outcome() == Outcome.TRIVIALLY_SATISFIABLE))
					candidates.add(new TestCase(p, new State(solution.instance())));
			}
		}
		return candidates;
	}

}

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
		int limit = 3; // TODO: hardcoded for now, make a configuration parameter later
	
		Set<TestCase> candidates = new HashSet<TestCase>();

		for(Operation p : model.operations()) {
			System.out.print("OPERATION: " + p.getName() + ": ");
			Iterator<Solution> it = solver.solve(model.context(), p.getFormula(model.context()), state);
			while (it.hasNext() && limit > 0) {
				Solution solution = it.next();
				if (solution != null && (solution.outcome() == Outcome.SATISFIABLE || solution.outcome() == Outcome.TRIVIALLY_SATISFIABLE))
					candidates.add(new TestCase(p, new State(solution.instance())));
				--limit;
				System.out.print("*");
			}
			System.out.println("");
/*
			Solution solution = solver.solve(model.context(), p.getFormula(model.context()), state);
			if (solution != null && (solution.outcome() == Outcome.SATISFIABLE || solution.outcome() == Outcome.TRIVIALLY_SATISFIABLE))
				candidates.add(new TestCase(p, new State(solution.instance())));
*/
		}	

		return candidates;
	}

}

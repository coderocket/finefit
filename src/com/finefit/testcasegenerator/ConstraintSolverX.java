package com.finefit.testcasegenerator;

import java.util.Iterator;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.Bounds;
import kodkod.instance.Universe;
import kodkod.instance.TupleFactory;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.BoundsExtractor;

public class ConstraintSolverX {
	
	Solver solver;

	public ConstraintSolverX() {
    solver = new Solver();
    solver.options().setSolver(SATFactory.DefaultSAT4J);
	}

	public Solution solve(A4Solution context, Formula formula) {

 		Bounds bounds = new BoundsExtractor(context).getBounds();

    Relation boundNextState = getBoundsRelationByName(bounds, "StateOrder/Ord.Next");

    // update the bounds to have next State relation order as: State$0, State$1

    TupleFactory factory = bounds.universe().factory();
    bounds.bound(boundNextState, factory.setOf(factory.tuple("State$0", "State$1")), factory.setOf(factory.tuple("State$0", "State$1")));

		return solver.solve(formula, bounds);
	}

	public Iterator<Solution> solve(A4Solution context, Formula formula, State state) {
		
    Bounds bounds = state.restrict(new BoundsExtractor(context).getBounds().clone());

    Relation boundNextState = getBoundsRelationByName(bounds, "StateOrder/Ord.Next");

    //update the bounds to have next State relation order as: State$0, State$1

    TupleFactory factory = bounds.universe().factory();
    bounds.bound(boundNextState, factory.setOf(factory.tuple("State$0", "State$1")), factory.setOf(factory.tuple("State$0", "State$1")));

		return solver.solveAll(formula, bounds);
	}

  private Relation getBoundsRelationByName(Bounds bounds,
      String boundsRelationName) {
    for (Relation r : bounds.relations()) {
      if (r.name().equals(boundsRelationName)) {
        return r;
      }
    }
    return null;
  }


}

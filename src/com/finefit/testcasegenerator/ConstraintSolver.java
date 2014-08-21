package com.finefit.testcasegenerator;

import java.util.Iterator;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.Bounds;
import kodkod.instance.Universe;
import kodkod.instance.Instance;
import kodkod.instance.TupleFactory;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import kodkod.ast.Relation;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.BoundsExtractor;
import com.finefit.model.State;

public class ConstraintSolver {
	
	Solver solver;

	public ConstraintSolver() {
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
		
    Bounds bounds = restrict(state.instance(), new BoundsExtractor(context).getBounds().clone());

    Relation boundNextState = getBoundsRelationByName(bounds, "StateOrder/Ord.Next");

    //update the bounds to have next State relation order as: State$0, State$1

    TupleFactory factory = bounds.universe().factory();
    bounds.bound(boundNextState, factory.setOf(factory.tuple("State$0", "State$1")), factory.setOf(factory.tuple("State$0", "State$1")));

		return solver.solveAll(formula, bounds);
	}

  private Bounds restrict(Instance instance, Bounds bounds) {

    TupleFactory factory = instance.universe().factory();

    for (Relation r : bounds.relations()) {
      if (instance.contains(r)) {
        TupleSet originalUpperBound = bounds.upperBound(r);
        /*
         * remove from the upper bound all tuples that involve the
         * current state
         */
        TupleSet upperBound = factory.noneOf(originalUpperBound.arity());
        Iterator<Tuple> p = originalUpperBound.iterator();
        while (p.hasNext()) {
          Tuple t = p.next();
          String a = (String) t.atom(0);
          if (!a.equals("State$0")) {
            upperBound.add(t);
          }
        }
        /*
         * set the lower bound to the current state and add the lower
         * bound to the upper bound
         */
        upperBound.addAll(instance.tuples(r));
        bounds.bound(r, instance.tuples(r), upperBound);
      }
    }

    return bounds;
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

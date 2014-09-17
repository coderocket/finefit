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
import com.finefit.translator.Constants;

public class ConstraintSolver {
	
	Solver solver;

	public ConstraintSolver() {
    solver = new Solver();
    solver.options().setSolver(SATFactory.DefaultSAT4J);
	}

	public Solution solve(A4Solution context, Formula formula) {

 		Bounds bounds = new BoundsExtractor(context).getBounds();

    Relation boundNextState = getBoundsRelationByName(bounds, Constants.STATE_SIG + "Order/Ord.Next");

    // update the bounds to have next State relation order as: State.CURR, State.NEXT

    TupleFactory factory = bounds.universe().factory();
    bounds.bound(boundNextState, factory.setOf(factory.tuple(State.CURR, State.NEXT)), factory.setOf(factory.tuple(State.CURR, State.NEXT)));

		return solver.solve(formula, bounds);
	}

	public Iterator<Solution> solve(A4Solution context, Formula formula, Instance instance) {
		
    Bounds bounds = restrict(instance, new BoundsExtractor(context).getBounds().clone());

    Relation boundNextState = getBoundsRelationByName(bounds, Constants.STATE_SIG + "Order/Ord.Next");

    //update the bounds to have next State relation order as: State.CURR, State.NEXT

    TupleFactory factory = bounds.universe().factory();
    bounds.bound(boundNextState, factory.setOf(factory.tuple(State.CURR, State.NEXT)), factory.setOf(factory.tuple(State.CURR, State.NEXT)));

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
          if (!a.equals(State.CURR)) {
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

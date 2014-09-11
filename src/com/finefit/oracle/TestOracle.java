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

package com.finefit.oracle;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import kodkod.instance.Bounds;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.ast.Relation;
import kodkod.ast.Formula;
import kodkod.engine.Evaluator;
import kodkod.ast.Expression;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4.Err;
import com.finefit.model.Model;
import com.finefit.model.State;
import com.finefit.model.Operation;
import com.finefit.translator.Constants;

public class TestOracle {

	public TestOracle() {}
	
	public boolean verify(Model model, Operation operation, State currentState, State nextState) throws Err {

		Instance instance = combine(currentState.instance(), nextState.instance());

		// add all the atoms as global relations to the instance. this makes it possible to use them in expressions.

		Instance root_instance = model.context().debugExtractKInstance();
		
		for(ExprVar a : model.context().getAllAtoms()) { 

	    for(Map.Entry<Relation, TupleSet> e : root_instance.relationTuples().entrySet()) {
				if (e.getKey().name().equals(a.label))
					instance.add(e.getKey(), e.getValue());
			}
		}

		Expr e = CompUtil.parseOneExpression_fromString(model.module(), operation.getAlloyCall(nextState));


		return new Evaluator(instance).evaluate((Formula)TranslateAlloyToKodkod.alloy2kodkod(model.context(), e)) && compareOutputs(nextState.instance(), nextState);

	}

  private Instance combine(Instance instance, Instance other) {

    Instance result = instance.clone();
    TupleFactory factory = instance.universe().factory();

    // clear all the state variables from result

    for(Map.Entry<Relation, TupleSet> e : result.relationTuples().entrySet())
    {
      if (e.getKey().name().startsWith("this/" + Constants.STATE_SIG + "."))
        result.add(e.getKey(), factory.noneOf(e.getValue().arity()));

    }

    // iterate over this and collect for each relation all the tuples that are bound to State.CURR

    collect(result, instance, State.CURR);

    // iterate over other and add to result all the tuples that are bound to State.CURR but with State.NEXT instead.

    collect(result, other, State.NEXT);

    return result;

  }

  // collect into result the tuples of all state variables in src
  // but replace their state component with newStateName

  private void collect(Instance result, Instance src, String newStateName) {

    TupleFactory factory = src.universe().factory();

    for(Map.Entry<Relation, TupleSet> e : src.relationTuples().entrySet())
    {
      if (e.getKey().name().startsWith("this/" + Constants.STATE_SIG + "."))
      {
        TupleSet tuples = result.tuples(e.getKey()).clone();
        for(Tuple t : e.getValue()) {
          if (t.atom(0).equals(State.CURR)) {
            Object[] atoms = new Object[t.arity()];
            atoms[0] = newStateName;
            for(int i = 1;i < t.arity(); i++) {
              atoms[i] = t.atom(i);
            }
            tuples.add(factory.tuple(atoms));
          }
        }
        result.add(e.getKey(), tuples);
      }
    }
  }

	// check that the outputs provided by the SUT match those that appear in the instance.

	private boolean compareOutputs(Instance instance, State fromSUT) {

    for(Map.Entry<Relation, TupleSet> e : instance.relationTuples().entrySet())
    {
      if (e.getKey().name().startsWith("$"+Constants.OUTPUT_PREFIX))
      {
				TupleSet ts = fromSUT.getOutput(e.getKey().name().replaceFirst("\\$",""));
				if (ts == null) return false;
				if (!ts.equals(e.getValue()))	return false;
			}
		}
		return true;
	}
}

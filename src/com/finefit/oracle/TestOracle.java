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
import com.finefit.model.Model;
import com.finefit.model.State;
import com.finefit.model.Operation;

public class TestOracle {

	public TestOracle() {}
	
	public boolean verify(Model model, Operation operation, State currentState, State nextState) {

		Instance instance = combine(currentState.instance(), nextState.instance());

		return new Evaluator(instance).evaluate(operation.getFormula(model.context()));
	}

  private Instance combine(Instance instance, Instance other) {

    Instance result = instance.clone();
    TupleFactory factory = instance.universe().factory();

    // clear all the state variables from result

    for(Map.Entry<Relation, TupleSet> e : result.relationTuples().entrySet())
    {
      if (e.getKey().name().startsWith("this/State."))
        result.add(e.getKey(), factory.noneOf(e.getValue().arity()));

    }

    // iterate over this and collect for each relation all the tuples that are bound to State$0

    collect(result, instance, "State$0");

    // iterate over other and add to result all the tuples that are bound to State$0 but with State$1 instead.

    collect(result, other, "State$1");

    return result;

  }

  // collect into result the tuples of all state variables in src
  // but replace their state component with newStateName

  private void collect(Instance result, Instance src, String newStateName) {

    TupleFactory factory = src.universe().factory();

    for(Map.Entry<Relation, TupleSet> e : src.relationTuples().entrySet())
    {
      if (e.getKey().name().startsWith("this/State."))
      {
        TupleSet tuples = result.tuples(e.getKey()).clone();
        for(Tuple t : e.getValue()) {
          if (t.atom(0).equals("State$0")) {
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
}

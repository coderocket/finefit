
package com.finefit.testcasegenerator;

import java.util.Iterator;
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

public class State {

	private Instance instance;

	public State(Instance instance) {
		this.instance = instance;
	}

	public State clone() {
		return new State(instance.clone());
	}

	public Instance instance() { return instance; }

	public TupleFactory factory() { return instance.universe().factory(); }

	public void add(String name, int arity, List<Tuple> tuples) {
			TupleFactory factory = instance.universe().factory();
			if (!tuples.isEmpty())
				instance.add(getVar(name), factory.setOf(tuples));
			else
				instance.add(getVar(name), factory.noneOf(arity));
	}

	public Relation getVar(String varName) {
		for (Relation r : instance.relations()) {
    	if (r.name().equals("this/State." + varName)) {
      	return r;
    	}
    }
    throw new RuntimeException("Could not find state variable " + varName);
	}

	public Bounds restrict(Bounds bounds) {

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

  /*

    Create a new state from this and other. The new state
    contains for each state variable the tuples it has in this joined with State$0
  	and the tuples it has in other joined with State$1.

  */

	public String getArg(String argName) {
    for (Map.Entry<Relation,TupleSet> e : instance.relationTuples().entrySet()) {
    	if (e.getKey().name().equals("$" + argName)) {
      	return (String)e.getValue().iterator().next().atom(0);
    	}
    }
    throw new RuntimeException("Could not find the argument " + argName);
  }

	public String toString() {
		return instance.toString();
	}

}

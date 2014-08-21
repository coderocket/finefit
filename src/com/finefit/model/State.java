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


package com.finefit.model;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
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

	public String getArg(String argName) {
    for (Map.Entry<Relation,TupleSet> e : instance.relationTuples().entrySet()) {
    	if (e.getKey().name().equals("$" + argName)) {
      	return (String)e.getValue().iterator().next().atom(0);
    	}
    }
    throw new RuntimeException("Could not find the argument " + argName);
  }

	public void print(PrintStream out) {

    for(Map.Entry<Relation, TupleSet> e : instance.relationTuples().entrySet())
    {
      if (e.getKey().name().startsWith("this/State."))
      {
				out.print(e.getKey().name().replace("this/State.","") + " = "); 
				printTuples(e.getValue());
				System.out.println("");
			}
		}
	}

	public void printTuples(TupleSet tuples) {
	
		TupleFactory factory = instance.universe().factory();
		
		Iterator<Tuple> p = tuples.iterator();
		System.out.print("[");
		if (p.hasNext()) {
			printTuple(removeState0(p.next(), factory));
		}

		while(p.hasNext()) {
			System.out.print(", ");
			printTuple(removeState0(p.next(), factory));
		}

		System.out.print("]");

	}
	
	public void printTuple(Tuple t) {
		if (t.arity() == 1)
			System.out.print(t.atom(0));
		else
			System.out.print(t);
	}
	
	public Tuple removeState0(Tuple t, TupleFactory factory) {
		List<Object> newTuple = new ArrayList<Object>();
		for (int i =0;i<t.arity();i++) {
			if (!t.atom(i).equals("State$0"))
				newTuple.add(t.atom(i));
		}
		if (newTuple.size() == 0)
			return t;
		else 
			return factory.tuple(newTuple);
	}

	public String toString() {
		return instance.toString();
	}

}

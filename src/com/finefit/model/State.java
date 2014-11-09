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
import java.util.HashMap;
import java.util.Map.Entry;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.ast.Relation;

import com.finefit.translator.Constants;

public class State {

	public static final String CURR = Constants.STATE_SIG + "$0";
	public static final String NEXT = Constants.STATE_SIG + "$1";

	private Instance instance;
	private Map<String, TupleSet> sut_outputs;

	public State(Instance instance) {
		this.instance = instance;
		this.sut_outputs = new HashMap<String, TupleSet>();
	}

	public State(Instance instance, Map<String, TupleSet> sut_outputs) {
		this.instance = instance;
		this.sut_outputs = sut_outputs;
	}

	public State clone() {
		return new State(instance.clone(), sut_outputs);
	}

	public Instance instance() { return instance; }

	public TupleFactory factory() { return instance.universe().factory(); }

	public void read(com.finefit.sut.State sutstate) {
		for(Map.Entry<String, com.finefit.sut.State.Table> e : sutstate.state_tables()) {
			com.finefit.sut.State.Table t = e.getValue();
			prepend(t, CURR);
			add(e.getKey(), e.getValue().numCols()+1, table2tuples(t));
		}
		for(Map.Entry<String, com.finefit.sut.State.Table> e : sutstate.output_tables()) {
			addOutput(e.getKey(), e.getValue().numCols(), table2tuples(e.getValue())); 
		}
	}

	private void prepend(com.finefit.sut.State.Table t, String atom) {
		for(List<String> row : t.rows()) {
			row.add(0, atom);
		}
	}

	private List<Tuple> table2tuples(com.finefit.sut.State.Table t) {
		List<Tuple> tuples = new ArrayList<Tuple>();
		for(List<String> row : t.rows()) {
			tuples.add(row2tuple(row));
		}
		return tuples;
	}

	private Tuple row2tuple(List<String> row) {
		return instance.universe().factory().tuple(row);
	}

	public void add(String name, int arity, List<Tuple> tuples) {
			TupleFactory factory = instance.universe().factory();
			if (!tuples.isEmpty())
				instance.add(getVar(name), factory.setOf(tuples));
			else
				instance.add(getVar(name), factory.noneOf(arity));
	}

	public void addOutput(String sugar_name, String value) {
		TupleFactory factory = instance.universe().factory();
		List<Tuple> r = new ArrayList<Tuple>(); 
		r.add(factory.tuple(value));
    addOutput(sugar_name, 1, r);
	}

	public void addOutput(String sugar_name, int arity, List<Tuple> tuples) {
			String name = sugar_name.replaceAll("!$", Constants.OUTPUT_SUFFIX);
			TupleFactory factory = instance.universe().factory();
			if (!tuples.isEmpty())
				sut_outputs.put(name, factory.setOf(tuples));
			else
				sut_outputs.put(name, factory.noneOf(arity));
	}

	public TupleSet getOutput(String name) { return sut_outputs.get(name); }


	public Relation getVar(String varName) {
		for (Relation r : instance.relations()) {
    	if (r.name().equals("this/" + Constants.STATE_SIG + "." + varName)) {
      	return r;
    	}
    }
    throw new RuntimeException("Could not find state variable " + varName);
	}

	public String getArg(String argName) {
    for (Map.Entry<Relation,TupleSet> e : instance.relationTuples().entrySet()) {
    	if (e.getKey().name().equals("$"+ argName.replaceAll("\\?", Constants.INPUT_SUFFIX))) {
				return tupleSet2Alloy(e.getValue());
    	}
    }
    throw new RuntimeException("Could not find the argument " + argName);
  }

	private String noneOfArity(int n) {
			String s = "none";
			for(int i =1;i < n;i++) {
				s += " -> none";
			}
			return s;
	}

	private String tupleSet2Alloy(TupleSet ts) {

			if (ts.size() == 0) 
				return noneOfArity(ts.arity());
			else {	
				Iterator<Tuple> it = ts.iterator();
				String s = tuple2Alloy(it.next());
				while(it.hasNext()) {
					s += " + " + tuple2Alloy(it.next());
				}
				return s;
			}
	}

	private String tuple2Alloy(Tuple t) {
		String s = t.atom(0).toString();
		for (int i = 1; i < t.arity(); i++) {
			s+= " -> " + t.atom(i);
		}
		return s;
	}

	public void print(PrintStream out) {

    for(Map.Entry<Relation, TupleSet> e : instance.relationTuples().entrySet())
    {
      if (e.getKey().name().startsWith("this/" + Constants.STATE_SIG + ".")) 
      {
				out.print(e.getKey().name().replace("this/" + Constants.STATE_SIG + ".","") + " = "); 
				printTuples(e.getValue());
				System.out.println("");
			}
		}
		for(Map.Entry<String, TupleSet> p : sut_outputs.entrySet()) {
			out.print(p.getKey().replaceAll(Constants.OUTPUT_SUFFIX +"$", "") + "! = ");
			printTuples(p.getValue());
			System.out.println("");
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
			if (!t.atom(i).equals(CURR))
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

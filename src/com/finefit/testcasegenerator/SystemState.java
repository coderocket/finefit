package com.finefit.testcasegenerator;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.TupleSet;
import kodkod.instance.TupleFactory;
import kodkod.instance.Tuple;

public class SystemState {

	private Instance systemState;
	
	public SystemState(Instance systemState){
		this.systemState = systemState;
	}

	public Instance getState() {
		return systemState;
	}

	public void setState(Instance systemState) {
		this.systemState = systemState;
	}
	
	public void printSystemStateRelations() {
		Map<Relation,TupleSet> relationTuples = systemState.relationTuples();
		Set<Entry<Relation, TupleSet>> set = relationTuples.entrySet();
		
		
		Iterator<Entry<Relation, TupleSet> > iterSetOfTuples = set.iterator();
		
		while(iterSetOfTuples.hasNext()) {
			Entry<Relation, TupleSet> relationEntry = iterSetOfTuples.next();
			Relation relation = relationEntry.getKey();
			System.out.print(relation.toString().replace("this/State.", "") + " = ");
			printTuples(relationEntry.getValue());
			System.out.println("");
		}
	}
	
	public void printTuples(TupleSet tuples) {
	
		TupleFactory factory = systemState.universe().factory();
		
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
		return factory.tuple(newTuple);
	}
}

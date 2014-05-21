package com.finefit.testcasegenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.TupleSet;

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
	
	public void printSystemStateRelations(){
		Map<Relation,TupleSet>relationTuples = this.systemState.relationTuples();
		Set<Entry<Relation, TupleSet>> set = relationTuples.entrySet();
		
		Iterator<Entry<Relation, TupleSet>> iterSetOfTuples = set.iterator();
		
		while(iterSetOfTuples.hasNext()){
			Entry<Relation, TupleSet> relationEntry = iterSetOfTuples.next();
			Relation relation = relationEntry.getKey();
			System.out.println(relation.toString().replace("this/State.", "") + " = " + relationEntry.getValue());
		}
	}
	
	
}

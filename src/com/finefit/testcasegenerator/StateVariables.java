package com.finefit.testcasegenerator;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import kodkod.ast.Relation;
import kodkod.instance.Instance;

public class StateVariables {

	Map<String, Relation> vars;

	public StateVariables(Instance inst) {
		vars = new HashMap<String, Relation>();
		for (Relation r : inst.relations()) {
			vars.put(r.name(), r);
		}
	}
	
	public Relation get(String name) {
		return vars.get("this/State." + name);
	}
}

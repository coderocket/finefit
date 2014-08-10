package com.finefit.testcasegenerator;

import static kodkod.engine.Solution.Outcome.SATISFIABLE;
import static kodkod.engine.Solution.Outcome.TRIVIALLY_SATISFIABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.Bounds;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprQt;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

public class TestCase {
	
	private String operationName;
	private Operation operation;
	private SystemState instance;
	private Universe universe;
	private Bounds bounds;
	private Solver solver;
	private ConstraintSolver constraintSolver;
	 
	public TestCase(SystemState systemState){
		this.constraintSolver = TestCaseGenerator.constraintSolver;
		this.operation = this.constraintSolver.getRandomOperation();
		this.operationName = operation.getName();
		this.instance = systemState;
		this.bounds = constraintSolver.getBounds();
		this.universe = constraintSolver.getUniverse();
		this.solver = new Solver();
		solver.options().setSolver(SATFactory.DefaultSAT4J); 
	}
	
	public String getArg(Instance args, String argName) {
	
      for (Map.Entry<Relation,TupleSet> e : args.relationTuples().entrySet()) {
		if (e.getKey().name().equals("$" + argName)) {
			return (String)e.getValue().iterator().next().atom(0);
		}
      }
	
		throw new RuntimeException("Could not find User Name in arguments");
    }

	public Solver getSolver(){
		return this.solver;
	}
	
	public Universe getUniverse() {
		return universe;
	}

	public void setUniverse(Universe universe) {
		this.universe = universe;
	}
	
	public Bounds getBounds() {
		return bounds;
	}

	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	public SystemState getSystemState() {
		return instance;
	}

	public void setSystemState(SystemState instance) {
		this.instance = instance;
	}

	public String getOperationName() {
		return operationName;
	}
	
	public boolean isSolutionSatisfiable(Solution sol){
		if (sol == null || (sol.outcome() != SATISFIABLE && sol.outcome() != TRIVIALLY_SATISFIABLE)) {
			
			System.out.println("NO SATISFIABLE SOLUTION EXISTS!");
			System.out.println("DEADLOCK");
			return false;
		}
		return true;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public A4Solution getA4Solution(){
		return this.constraintSolver.getA4Solution();
	}
	
	public Formula getFormula(Operation operation){
		
		Formula formula = null;
		try {
			formula = (Formula) TranslateAlloyToKodkod.alloy2kodkod(getA4Solution(), ExprQt.Op.SOME
					.make(null, null, operation.getOperation().decls, operation.getOperation().getBody()));
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formula;
	}
	
	// used to reproduce test scenario
	public Operation setOperationByName(String funcLabel){
		for(Operation op: constraintSolver.getListOfOperations()){
			if(op.getOperation().label.replace("this/", "").equalsIgnoreCase(funcLabel)){
				return op; 
			}
		}
		System.err.println("Operation: " + funcLabel + " wasn't found!" );
		return null;
	}
	
	public Instance relateSystemState(Instance instanceFromSut, Instance instanceFromSolver){
		TupleFactory factory = instanceFromSut.universe().factory();
		List<Tuple> listOfTuples = new ArrayList();
		Instance theNewInstance = new Instance(instanceFromSut.universe());
		Instance newInstanceToEvaluator = null;
		
		// copy the solver instance into map, on this map i'm updating the relations to have the tuples from 
		//sut and from the solver to match the expected system state
		// i did this because the returned map from the instance is unmodifiable so i couldn't make any actions on it directly
		Map<Relation,TupleSet> solverMap = instanceFromSolver.relationTuples();
		Set<Entry<Relation, TupleSet>> solverSet = solverMap.entrySet();
		Iterator<Entry<Relation, TupleSet>> solverItrEntry = solverSet.iterator();
		Map<Relation, TupleSet> copyFromSolverMap = new HashMap<Relation, TupleSet>();
		while(solverItrEntry.hasNext()){
			Entry<Relation, TupleSet> solverEntry = solverItrEntry.next();
			copyFromSolverMap.put(solverEntry.getKey(), solverEntry.getValue());
		}
		// end copy of instanceFromSolver
		
		// go over the instanceFromSut and update the relations to have State$1 (next state)
		Map<Relation,TupleSet> relationTuples = instanceFromSut.relationTuples();
		Set<Entry<Relation, TupleSet>> set = relationTuples.entrySet();
		
		Iterator<Entry<Relation, TupleSet>> iterSetOfTuples = set.iterator();
		
		while(iterSetOfTuples.hasNext()){
			Entry<Relation, TupleSet> relationEntry = iterSetOfTuples.next();
			TupleSet tupleSet = relationEntry.getValue();
			
			Iterator<Tuple> iterTupleSet = tupleSet.iterator();
			
			while (iterTupleSet.hasNext()){
				Tuple mytup = iterTupleSet.next();
				String atom = mytup.toString();
				String atoms[] = atom.split(",");
				String newAtom[] = new String[atoms.length];
				newAtom[0] = "State$1";
				for(int i = 1; i< atoms.length; i++ ){
					String str = atoms[i].trim();
					if(str.contains("]")){
						str = str.replaceAll("\\]", "");
					}
					if(str.contains("[")){
						str = str.replaceAll("\\[", "");
					}
					newAtom[i] = str;
				}
				listOfTuples.add(factory.tuple(newAtom));
			}
			
			// go over the instanceFromSolver and get the tuples from the solver instance (without the next state: State$1)
			Map<Relation,TupleSet> relationTuplesFromSolver = instanceFromSolver.relationTuples();
			Set<Entry<Relation, TupleSet>> setFromSolver = relationTuplesFromSolver.entrySet();
			
			Iterator<Entry<Relation, TupleSet>> iterSetOfTuplesFromSolver = setFromSolver.iterator();
			while(iterSetOfTuplesFromSolver.hasNext()){
				Entry<Relation, TupleSet> relationEntryFromSolver = iterSetOfTuplesFromSolver.next();
				TupleSet tupleSetFromSolver = relationEntryFromSolver.getValue();
				String relationName = relationEntryFromSolver.getKey().toString();
				if(relationEntry.getKey().toString().equals(relationName)){
					Iterator<Tuple> iterTupleSetFromSolver = tupleSetFromSolver.iterator();
					while(iterTupleSetFromSolver.hasNext()){
						Tuple tuple = iterTupleSetFromSolver.next();
						String atom = tuple.toString();
						String atoms[] = atom.split(",");
						String newAtom[] = new String[atoms.length];
						String theState = atoms[0];
						for(int i = 0; i < atoms.length; i++ ){
							String str = atoms[i].trim();
							if(str.contains("]")){
								str = str.replaceAll("\\]", "");
							}
							if(str.contains("[")){
								str = str.replaceAll("\\[", "");
							}
							newAtom[i] = str;
						}
						if(!theState.contains("State$1")){ 
							listOfTuples.add(factory.tuple(newAtom));
						}
					}
				}
			}
		
			// add updated tuplesets and solver tuplesets to the copyMap
			if(!listOfTuples.isEmpty()){
				copyFromSolverMap.put(relationEntry.getKey(), factory.setOf(listOfTuples));
			}else{
				copyFromSolverMap.put(relationEntry.getKey(), factory.noneOf(relationEntry.getValue().arity()));
			}
			
			
			listOfTuples = new ArrayList();
		}
		
		// copy the new map into new Instance including the inputs, groups and all other relations
		Set<Entry<Relation, TupleSet>> setOfNewInstance = copyFromSolverMap.entrySet();
		Iterator<Entry<Relation, TupleSet>> itrOfNewRelation = setOfNewInstance.iterator();
		newInstanceToEvaluator = new Instance(instanceFromSolver.universe());
		while(itrOfNewRelation.hasNext()){
			Entry<Relation, TupleSet> newEntry = itrOfNewRelation.next();
			newInstanceToEvaluator.add(newEntry.getKey(), newEntry.getValue());
		}
		
		return newInstanceToEvaluator;
	}
	
	public Solution findSolution(){
		
		Bounds restricted_bounds = getBounds().clone();
		Relation boundNextState = getBoundsRelationByName(restricted_bounds, "StateOrder/Ord.Next");
		TupleFactory factory = getUniverse().factory();
		//update the bounds to have next State relation order as: State$0, State$1
		restricted_bounds.bound(boundNextState, factory.setOf(factory.tuple("State$0", "State$1")), 
				factory.setOf(factory.tuple("State$0", "State$1")));
		
		restrictToCurrentState(restricted_bounds, getSystemState().getState());
		return getSolver().solve(getFormula(operation), restricted_bounds);
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
	
	private void restrictToCurrentState(Bounds bounds, Instance currentState) {

		TupleFactory factory = currentState.universe().factory();
		
		for (Relation r : bounds.relations()) {
			if (currentState.contains(r)) {
				TupleSet originalUpperBound = bounds.upperBound(r);
				/*
				 * remove from the upper bound all tuples that involve the
				 * current state
				 */
				TupleSet upperBound = factory.noneOf(originalUpperBound
						.arity());
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
				upperBound.addAll(currentState.tuples(r));
				bounds.bound(r, currentState.tuples(r), upperBound);
			}
		}
	}
}

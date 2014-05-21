package com.finefit.reporter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.finefit.testcasegenerator.SystemState;
import com.finefit.testcasegenerator.TestCase;
import kodkod.ast.Relation;
import kodkod.instance.TupleSet;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;

public class Reporter {

	private SystemState currentSystemState = null;
	private TestCase testCase = null;
	
	public Reporter(SystemState currentSystemState, TestCase testCase){
		this.currentSystemState = currentSystemState;
		this.testCase = testCase;
	}
	
	private Map<String, String> extractTestInput(){
		Map<String, TupleSet> vars = new HashMap<String, TupleSet>();
		for (Map.Entry<Relation,TupleSet> e : currentSystemState.getState().relationTuples().entrySet()) {
			vars.put(e.getKey().name(), e.getValue());
		}
		
		Map<String, String> myMap = new HashMap<>();
		//we need to go over the declarations and on the  map {s.instance().relationTuples()} and find the same variable names so we can use them as input
		for(Decl decl: testCase.getOperation().getOperation().decls){
			
			TupleSet name = vars.get("$" + decl.get().label);
			String nameStr = (String) name.iterator().next().atom(0);
			myMap.put(decl.get().label, nameStr);
		}

		return myMap;
	}
	
	public String getCurrentTime() {
		String timeStamp = new SimpleDateFormat("d/MM/yyy HH:mm:ss.sss").format(Calendar.getInstance().getTime());
		return timeStamp;
	}
	
	private void printTestInput(){
		Map<String,String> myMap = extractTestInput();
		System.out.println("Operation Name: " + getOperationName());
		for (Map.Entry entry : myMap.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
	}
	
	public void printOnFailureTestTrace(){
			System.out.println("---------------------");
			System.out.println(getCurrentTime());
			System.out.println("Test Failed -> INPUT:"); 
			printTestInput();
			System.err.println("STATE DISCREPANCY");
			System.out.println("---------------------\n");
	}
	
	public void printOnSuccessTestTrace(){
			System.out.println("---------------------");
			System.out.println(getCurrentTime());
			System.out.println("Test Passed -> INPUT:");
			printTestInput();
			System.out.println("---------------------\n");
	}
	
	private String getOperationName(){
		return this.testCase.getOperationName();
	}
}

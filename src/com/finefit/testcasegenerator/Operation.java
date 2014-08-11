package com.finefit.testcasegenerator;

import java.util.Map;
import java.util.Map.Entry;
import java.io.PrintStream;
import edu.mit.csail.sdg.alloy4compiler.ast.Func;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;
import kodkod.instance.Instance;
import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.TupleSet;

public class Operation {

	private Func operation;
	
	public Operation (Func operation){
		this.operation = operation;
	}

	public Func getOperation() {
		return operation;
	}

	public void setOperation(Func operation) {
		this.operation = operation;
	}
	
	public String getName(){
		return this.operation.label.replace("this/", "");
	}

	public String getArg(Instance args, String argName) {
	
      for (Map.Entry<Relation,TupleSet> e : args.relationTuples().entrySet()) {
		if (e.getKey().name().equals("$" + argName)) {
			return (String)e.getValue().iterator().next().atom(0);
		}
      }
	
		throw new RuntimeException("Could not find variable name " + argName);
    }

	public void printCall(Instance args, PrintStream out) {
		
		for(Decl decl: operation.decls) {
			if (!decl.get().label.equals("s") && !decl.get().label.equals("s'"))
				//out.print(decl.get().label + " = " + getArg(args, decl.get().label) + ";");
				out.print("." + getArg(args, decl.get().label));
		}
	}
}


package com.finefit.spec;

import java.io.PrintStream;
import java.util.List;
import com.finefit.spec.State;

public class Operation {
	String name;
	String params;
	String[] frame;
	GuardedExpr[] body;

	public Operation(String name, String params, String[] frame, GuardedExpr[] body) {
		this.name = name;
		this.params = params;
	 	this.frame = frame;
	 	this.body = body;
	}

	public void print(State state, PrintStream out) {
		out.print("pred " + name + "[s',s:State");
		printParams(out);
		out.println("] {");
		out.println("s' = s.next");
		out.println("s != s'");
		printPrecondition(state,out);
		for (GuardedExpr e : body) 
			e.print(frame, state, out);
		out.println("}");
	}

	private void printParams(PrintStream out) {

		if (params != "") {
			out.print(", " + params);
		}
	}

	private void printPrecondition(State state, PrintStream out) {
		out.print("(");
		if (body.length > 0)
			body[0].printGuard(state, out);
		for (int i =1; i < body.length;i++) {
			out.print(" or ");
			body[i].printGuard(state, out);
		}
		out.println(")");
	}
}

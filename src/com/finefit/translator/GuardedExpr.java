
package com.finefit.translator;

import java.io.PrintStream;

public class GuardedExpr {
	String guard;
	String[] exprs;

	public GuardedExpr(String guard, String[] exprs) {
		this.guard = guard;
		this.exprs = exprs;
	}

	public void print(String[] frame, State state, PrintStream out) {
      printGuard(state, out);
      out.print(" => {\n");
      printExpr(frame, state, out);
			out.println("}");
	}

	public void printGuard(State state, PrintStream out) {
		out.print("(");
		state.print(guard, "s", out);
		out.print(")");
	}

	private void printExpr(String[] frame, State state, PrintStream out) {
		for (int i = 0 ; i < frame.length; i++) {
			state.print(frame[i] + " = ", "s'", out);
			state.print(exprs[i], "s", out);
			out.println("");
		}
	}
}

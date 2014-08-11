package com.finefit.spec;

import java.io.PrintStream;
import java.util.List;
import com.finefit.spec.State;

public class Invariant {
	List<String> preds;

	public Invariant(List<String> preds) {
		this.preds = preds;
	}

	public void print(State state, PrintStream out) {
		out.println("pred inv[s:State] {");
		for(String p : preds) {
			state.print(p,"s",out); out.println("");
		}
		out.println("}");
	}
}

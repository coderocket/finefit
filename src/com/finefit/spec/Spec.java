package com.finefit.spec;

import java.io.PrintStream;

public class Spec {
	State state;
	Invariant invariant;
	Sig[] sigs;
	Operation[] operations; 

	public Spec(State state, Invariant invariant, Sig[] sigs, Operation[] operations) {
		this.state = state;
		this.invariant = invariant;
		this.sigs = sigs;
		this.operations = operations;
	}

	public void print(PrintStream out) {
		out.println("open util/ordering[State] as StateOrder");
		for (Sig s : sigs) 
			s.print(out);
		state.print(out);
		invariant.print(state, out);
		for (Operation p : operations) 
			p.print(state, out);
		out.println("run {} for 3 but 2 State");
	}
}

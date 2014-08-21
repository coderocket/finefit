/* Copyright 2014 David Faitelson

This file is part of FineFit.

FineFit is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

FineFit is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with FineFit. If not, see <http://www.gnu.org/licenses/>.

*/

package com.finefit.translator;

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
		out.print("run {} for 3 but ");
		for (Sig s : sigs) {
			s.printScope(out);
			out.print(", ");
		}
		out.println(" 2 State");
	}
}

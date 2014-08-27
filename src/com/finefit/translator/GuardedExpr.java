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
		state.print(guard, State.CURR_VAR, out);
		out.print(")");
	}

	private void printExpr(String[] frame, State state, PrintStream out) {
		for (int i = 0 ; i < frame.length; i++) {
			state.print(frame[i] + " = ", State.NEXT_VAR, out);
			state.print(exprs[i], State.CURR_VAR, out);
			out.println("");
		}
	}
}

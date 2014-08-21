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
import java.util.List;

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
		state.printXi(frame, out);
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

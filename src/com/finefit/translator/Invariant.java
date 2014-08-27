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

public class Invariant {
	List<String> preds;

	public Invariant(List<String> preds) {
		this.preds = preds;
	}

	public void print(State state, PrintStream out) {
		out.println("pred inv[" + Constants.CURR_VAR + ":" + Constants.STATE_SIG +"] {");
		for(String p : preds) {
			state.print(p,Constants.CURR_VAR,out); out.println("");
		}
		out.println("}");
	}
}

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

public class Sig {
	String name;
	int scope;
	public Sig(String name, int scope) {
		this.name = name;
		this.scope = scope;
	}

	public void print(PrintStream out) {
		if (!name.equals("Int") && !name.equals("seq")) // Int, seq are predefined signatures, don't create sigs for them
			out.println("sig " + name + " {}");
	}

	public void printScope(PrintStream out) {
		if (name.equals("Int") || name.equals("seq")) // Alloy refuses 'exactly' for Int, seq
			out.print(" " + scope + " " + name);
		else
			out.print("exactly " + scope + " " + name);
	}
}

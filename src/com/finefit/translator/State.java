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
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.regex.Pattern;

public class State {

	Map<String, String> stateVariables;
	Pattern tokenizer;

	public State(Map<String, String> stateVariables) {
		this.stateVariables = stateVariables;
		tokenizer = Pattern.compile("\\b");
	}

	public void printXi(String[] frame, PrintStream out) {
		 
		Set<String> unchanged = new HashSet<String>(stateVariables.keySet());
		unchanged.removeAll(new HashSet<String>(Arrays.asList(frame)));
		for (String n : unchanged) {
			print(n, Constants.NEXT_VAR, out);
			out.print(" = ");
			print(n, Constants.CURR_VAR, out);
			out.print("\n");
		}
	}

	public void print(PrintStream out) {
		out.println("sig " + Constants.STATE_SIG + " {");
		for(Map.Entry<String, String> p : stateVariables.entrySet()) {
			out.println(p.getKey() + " : " + p.getValue() + ", ");
		}
		out.println("}");
	}

	/* print s on out but replace every occurrence of a state 
		variable v in s by the expression (<prefix>.v) where <prefix> 
		is a parameter */

	public void print(String s, String prefix, PrintStream out) {
		String[] tokens = tokenizer.split(s,0);
		for (int i=0; i < tokens.length; i++)
			if (stateVariables.containsKey(tokens[i]))
				out.print("(" + prefix + "." + tokens[i] + ")");
			else
				out.print(tokens[i]);
	}
}

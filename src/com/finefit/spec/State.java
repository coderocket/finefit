
package com.finefit.spec;

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
			print(n, "s'", out);
			out.print(" = ");
			print(n, "s", out);
			out.print("\n");
		}
	}

	public void print(PrintStream out) {
		out.println("sig State {");
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

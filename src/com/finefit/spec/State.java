
package com.finefit.spec;

import java.io.PrintStream;
import java.util.Map;
import java.util.regex.Pattern;

public class State {
	Map<String, String> stateVariables;
	Pattern tokenizer;

	public State(Map<String, String> stateVariables) {
		this.stateVariables = stateVariables;
		tokenizer = Pattern.compile("\\b");
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

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
		out.println("sig " + name + " {}");
	}

	public void printScope(PrintStream out) {
		out.print("exactly " + scope + " " + name);
	}
}

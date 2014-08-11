
package com.finefit.spec;

import java.io.PrintStream;

public class Param {
	String name;
	String type;

	public Param(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public void print(PrintStream out) {
		out.print(name + " : " + type);
	}
}

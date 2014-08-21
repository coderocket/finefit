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

package com.finefit.model;


import java.io.PrintStream;

public class TestCase {
	
	private State state;
	private Operation operation;
	 
	public TestCase(Operation operation, State state) {
		this.operation = operation;
		this.state = state;
	}

	public State getState() { return state; }

	public Operation getOperation() {
		return operation;
	}

	public String getOperationName() {
		return operation.getName();
	}

	public void print(PrintStream out) {
		out.print(operation.getName()); 
		operation.printCall(state, out);
		out.println(" -> ");
	}

}

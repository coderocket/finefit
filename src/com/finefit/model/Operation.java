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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.io.PrintStream;
import kodkod.ast.Relation;
import kodkod.ast.Formula;
import kodkod.instance.Instance;
import kodkod.instance.Instance;
import kodkod.instance.TupleSet;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Func;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprQt;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprHasName;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;


public class Operation {

	private Func operation;
	
	public Operation (Func operation){
		this.operation = operation;
	}

	public Func getOperation() {
		return operation;
	}

	public void setOperation(Func operation) {
		this.operation = operation;
	}
	
	public String getName(){
		return this.operation.label.replace("this/", "");
	}

	public Formula getFormula(A4Solution context) {
	 Formula formula = null;
    try {
      formula = (Formula) TranslateAlloyToKodkod.alloy2kodkod(context, ExprQt.Op.SOME
          .make(null, null, operation.decls, operation.getBody()));
    } catch (Err e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return formula;
	}

	public String getAlloyCall(State state) {
		
		String[] args = new String[operation.count()];

		int i = 0;
		for(Decl decl : operation.decls) {
			for(ExprHasName n : decl.names) {
				args[i] = state.getArg(n.label); 
				i++;
			}
		}
 
		String call = getName() + "["; 

		if (args.length > 0) 
			call += args[0];

		for(i = 1; i < args.length; i++) 
			call += ", " + args[i];

		call += "]";
		return call;
	}
			
	public void printCall(State state, PrintStream out) {

		for(Decl decl: operation.decls) {
			String value = state.getArg(decl.get().label);

			if (!value.equals(State.CURR) && !value.equals(State.NEXT))
				out.print("." + value);
		}
	}
}

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

import java.util.ArrayList;
import java.util.List;
import edu.mit.csail.sdg.alloy4compiler.ast.Func;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;


public class Model {

	CompModule world;
	A4Solution context;

	public Model(String modelFileName) throws Err {
		world =  CompUtil.parseEverything_fromFile(new A4Reporter(), null, modelFileName);
		context = TranslateAlloyToKodkod.execute_command(new A4Reporter(), world.getAllReachableSigs(), world.getAllCommands().get(0), new A4Options());
 		for(ExprVar a:context.getAllAtoms()) { world.addGlobal(a.label, a); }
	}

	public A4Solution context() {
		return context; 
	}

	public CompModule module() { return world; }

	public Operation getInit() {

    for (Func func: world.getAllFunc()) {
      if(func.isPred && func.count() >= 2 && func.label.equals("this/init")) { 
        return new Operation(func);
    	}
		}
    throw new RuntimeException("Model Error: Missing init operation");
	}

	public List<Operation> operations() {
    List<Operation> ops = new ArrayList<>();

    for (Func func: world.getAllFunc()) {
			//TODO: the exclusion of 'init' is an ugly hack until we parse init as a separate entity.

      if(func.isPred && func.count() >= 2 && !func.label.equals("this/init")){ // Operation is pred with more than 2 parameters
        ops.add(new Operation(func));

    	}
		}
    return ops;
  }
}

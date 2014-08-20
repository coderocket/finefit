
package com.finefit.testcasegenerator;

import java.util.ArrayList;
import java.util.List;
import edu.mit.csail.sdg.alloy4compiler.ast.Func;
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
	}

	public A4Solution context() {
		return context; 
	}

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

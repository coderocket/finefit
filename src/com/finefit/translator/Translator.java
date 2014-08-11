package com.finefit.translator;

import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import fit.Parse;
import com.finefit.base.FileUtils;
import com.finefit.spec.Sig;
import com.finefit.spec.State;
import com.finefit.spec.Invariant;
import com.finefit.spec.GuardedExpr;
import com.finefit.spec.Operation;
import com.finefit.spec.Spec;

public class Translator {

	public Translator(){ }
	
  public static void translate(String htmlFileName, String alloyFileName) throws ParseException, FileNotFoundException {

		Sig[] sigs = null;
		State state = null;
		Invariant invariant = null;
		List<Operation> operations = new ArrayList<Operation>();
	
    Parse parse = new Parse(FileUtils.readFile(htmlFileName));

    while (parse != null) {

			Parse name = parse.at(0,0,0);
      switch (name.text()) {
      case "sample.OperationFixture":
        operations.add(parseOperation(parse.at(0,1)));
				break;
      case "Invariant":
        invariant = parseInvariant(parse.at(0,1));
				break;
      case "Atom":
				sigs = parseSigs(parse.at(0,1));
        break;
      case "Relation":
				state = parseState(parse.at(0,1));
        break;
      }
      parse = parse.more;
    }

		if (state == null)
			throw new RuntimeException("Invalid specification: No State table");

		Spec spec = new Spec(state, invariant, sigs, operations.toArray(new Operation[operations.size()]));
		
		spec.print(new PrintStream(alloyFileName));
  }

	private static Operation parseOperation(Parse p) {
		String name = p.at(0,0).text();
		String params = p.at(1,0).text();
		String[] frame = parseFrame(p.at(2));	
		GuardedExpr[] body = parseBody(p.at(1));
		return new Operation(name, params, frame, body);
	}

	private static String[] parseFrame(Parse p) {
		List<String> varNames = new ArrayList<String>();
		while (p != null) {
			varNames.add(p.at(0,0).text());
			p = p.more;
		}
		return varNames.toArray(new String[varNames.size()]);
	}

	private static GuardedExpr[] parseBody(Parse p) {

		int numCols = p.at(0,0).size() - 1;
		assert(numCols > 0);
		GuardedExpr[] exprs = new GuardedExpr[numCols];

		for (int j = 0 ; j < numCols; j++) {
			exprs[j] = parseGuardedExpr(p, j+1);
		} 

		return exprs;
	}
		
	private static GuardedExpr parseGuardedExpr(Parse p, int j) {
			int numRows = p.size();
			String[] exprs = new String[numRows-1];
			String guard = p.at(0,j).text();
			p = p.more;
			for (int i = 0; i < numRows-1; i++) {
				exprs[i] = p.at(0,j).text();
				p = p.more;	
			}
			return new GuardedExpr(guard, exprs);
	}

	private static Sig[] parseSigs(Parse p) {
		List<Sig> sigs = new ArrayList<Sig>();
		while (p != null) {
			String name = p.at(0,0).text();
			int scope = Integer.parseInt(p.at(0,1).text());
			sigs.add(new Sig(name, scope));
			p = p.more;	
		}		
		return sigs.toArray(new Sig[sigs.size()]);
	}

	private static State parseState(Parse p) {
		Map<String, String> stateVars = new HashMap<String,String>();
		while (p != null) {
			String name = p.at(0,0).text();
			String type = p.at(0,1).text();
			stateVars.put(name,type);
			p = p.more;	
		}		
		return new State(stateVars);
	}

	private static Invariant parseInvariant(Parse p) {
		List<String> preds = new ArrayList<String>();
		while (p != null) {
			preds.add(p.at(0,0).text());
			p = p.more;	
		}		
		return new Invariant(preds);
	}
}

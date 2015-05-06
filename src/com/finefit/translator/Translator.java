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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.text.ParseException;
import fit.Parse;

public class Translator {

  public static void translate(String htmlFileName, String alloyFileName, String finefit_home) throws ParseException, IOException {

		Sig[] sigs = null;
		State state = null;
		Invariant invariant = null;
		List<Operation> operations = new ArrayList<Operation>();
		List<Enumeration> enumerations = new ArrayList<Enumeration>();
	
    Parse parse = new Parse(readFile(htmlFileName, Charset.defaultCharset()));

    while (parse != null) {

			Parse name = parse.at(0,0,0);

      if (name.text().equals("Operation"))
        operations.add(parseOperation(parse.at(0,1)));
			else if (name.text().equals("Invariant name"))
        invariant = parseInvariant(parse.at(0,1));
			else if (name.text().equals("Atom"))
				sigs = parseSigs(parse.at(0,1));
			else if (name.text().equals("State variable"))
				state = parseState(parse.at(0,1));
			else if (name.text().equals("Enumeration"))
				enumerations.add(parseEnumeration(parse.at(0,1)));
      
      parse = parse.more;
    }

		if (state == null)
			throw new RuntimeException("Invalid specification: No State table");

		String syntax_err_report = "";
		for(Operation p : operations) {
			syntax_err_report = syntax_err_report + p.check_syntax(state);
		}

		if (!syntax_err_report.equals("")) throw new ParseException("\n"+syntax_err_report, 0);

		Spec spec = new Spec(state, invariant, sigs, operations.toArray(new Operation[0]), enumerations.toArray(new Enumeration[0]));
		
		PrintStream out = new PrintStream(alloyFileName);
		print_header(out);
		print_libraries(finefit_home, out);
		spec.print(out);
  }

	private static void print_header(PrintStream out) {
  	out.println("open util/ordering[" + Constants.STATE_SIG + "] as " + Constants.STATE_SIG + "Order");
	}

	private static void print_libraries(String finefit_home, PrintStream out) throws FileNotFoundException, IOException {

		// copy the finefit.als module to the current directoy.

    if (finefit_home == null) {
      finefit_home = System.getenv("FINEFIT_HOME");
      if (finefit_home == null) {
        System.err.println(
                    "Error: Can't find finefit.als.\n\n"
                  + "You must either provide the FineFit home directory on the command line\n"
                  + "or set the FINEFIT_HOME environment variable.");
        System.exit(1);
      }
    }

		String lib = readFile(finefit_home+"/finefit.als", Charset.defaultCharset());
		PrintStream libfile = new PrintStream("finefit.als");
		libfile.println(lib);
		libfile.close();
	
    out.println("open finefit");
	}

	private static Operation parseOperation(Parse p) {

		String name = p.at(0,0).text();
		String params = p.at(1,0).text();

		int tree_height = getHeight(p.at(1,0)); 
		
		String[] frame = parseFrame(p.at(1+tree_height));	
		GuardedExpr[] body = parseBody(p.at(1));
		return new Operation(name, params, frame, body);
	}

	private static String[] parseFrame(Parse p) {
		List<String> varNames = new ArrayList<String>();
		while (p != null) { 
			if (p.at(0,0).text().matches("\\S\\S*"))  { // ignore empty rows
				varNames.add(p.at(0,0).text());
			}
			p = p.more;
		}
		return varNames.toArray(new String[varNames.size()]);
	}

	private static GuardedExpr[] parseBody(Parse p) {

		int tree_height = getHeight(p.at(0,0)); 
		
		Flattener.Table table = makeTable(p.at(0), tree_height);

		String[] guards = Flattener.flatten(table);

		GuardedExpr[] exprs = new GuardedExpr[guards.length];

		// move p to the row that holds the first state variable 
		p = p.at(tree_height);

		for (int j = 0 ; j < guards.length; j++) {
			exprs[j] = parseGuardedExpr(p, guards[j], j+1);
		} 

		return exprs;
	}
		
	private static GuardedExpr parseGuardedExpr(Parse p, String guard, int j) {
			int numRows = p.size();
			String[] exprs = new String[numRows];
			for (int i = 0; i < numRows; i++) {
				exprs[i] = p.at(0,j).text();
				p = p.more;	
			}
			return new GuardedExpr(guard, exprs);
	}

	private static int getWidth(Parse p) {
		return getDim(p, "colspan");
	}

	private static int getHeight(Parse p) {
		return getDim(p, "rowspan");
	}

	private static int getDim(Parse p, String dimname) {
		String value = getAttr(p.tag, dimname);
		if (value != null)
			return Integer.parseInt(value);
		else		
			return 1;
	}

	private static Flattener.Cell[] makeRow(Parse col) {

		Flattener.Cell[] cells = new Flattener.Cell[col.size()];

		for(int j = 0; j < cells.length;j++) {
			cells[j] = new Flattener.Cell(getWidth(col), col.text());
			col = col.more;
		}

		return cells;
	}

	private static Flattener.Table makeTable(Parse row, int height) {

			Flattener.Table table = new Flattener.Table(height);

			if (height > 0) { // the first row starts with the parameter list

				table.setRow(0, makeRow(row.at(0, 1)));
			}

			for(int i = 1; i < height; i++) {
				table.setRow(i, makeRow(row.at(i, 0)));
			}

			return table;
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
			preds.add(p.at(0,1).text());
			p = p.more;	
		}		
		return new Invariant(preds);
	}

	private static Enumeration parseEnumeration(Parse p) {
		List<String> instances = new ArrayList<String>();
		String sigName = p.at(0,0).text();
		p = p.more;
		while (p != null) {
			instances.add(p.at(0,0).text());
			p = p.more;
		}
		return new Enumeration(sigName, instances.toArray(new String[0]));
	}

	static String readFile(String path, Charset encoding) 
 	 throws IOException {

 	 	byte[] encoded = Files.readAllBytes(Paths.get(path));
 	 	return new String(encoded, encoding);
	}

	// Returns the value of the HTML attribute 'name' if it appears in 'tag' or null otherwise.
	// For example, if tag is <Table foo=2> and name is foo then getAttr returns 2

	static String getAttr(String tag, String name) {
		Matcher matcher = Pattern.compile(name + "[\\s]*=[\\s]*\"?(\\w)+\"?").matcher(tag);
		if (matcher.find()) {
			return matcher.group(1);
		}
		else
			return null;
	}
}

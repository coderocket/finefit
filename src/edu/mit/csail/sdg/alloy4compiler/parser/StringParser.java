package edu.mit.csail.sdg.alloy4compiler.parser;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import edu.mit.csail.sdg.alloy4.Err;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StringParser {
	public static boolean valid_alloy_expr(String expr) {
      Map<String,String> fc = new LinkedHashMap<String,String>();
      fc.put("", "run {\n"+expr+"}"); // We prepend the line "run{"
	try {
      CompModule m = CompParser.alloy_parseStream(new ArrayList<Object>(), null, fc, null, -1, "", "", 1);
		return m.getAllFunc().size() > 0;
	}
	catch(Err err) {
	}
	catch(FileNotFoundException err) {
	}
	catch(IOException err) {
	}
	return false;
	}
}

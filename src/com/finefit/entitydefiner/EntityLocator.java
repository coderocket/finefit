package com.finefit.entitydefiner;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompParser;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;

public class EntityLocator extends CompParser {

	private CompModule world = null;

	public EntityLocator(String fileName) {
		this.world = parseAlloyFile(fileName);
	}

	private CompModule parseAlloyFile(String fileName) {
		A4Reporter a4Reporter = new A4Reporter();
		try {
			return CompUtil
					.parseEverything_fromFile(a4Reporter, null, fileName);
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public CompModule getWorld() {
		return this.world;
	}
}

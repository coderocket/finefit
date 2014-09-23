
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

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class TreeNode {
	TreeNode[] children;
	String predicate;

	public TreeNode(String predicate) {
			this.children = new TreeNode[0];
			this.predicate = predicate;
	}

	public TreeNode(TreeNode[] children, String predicate) {
			this.children = children;
			this.predicate = predicate;
	}

	public List<String> expand() {

		List<String> fullPreds = new ArrayList<String>();

		if (children.length == 0)
			fullPreds.add("(" + predicate + ")");
		else {
			for(TreeNode c : children) {
				for(String s : c.expand()) {
					fullPreds.add("(" + predicate + ") and " + s);
				}	  
			}
		}

		return fullPreds;
	}

	public void print(PrintStream out) {
			out.print("(&& ");
			out.print(predicate);	
			for(TreeNode t : children) {
				out.print(" ");
				t.print(out);
			}
			out.print(")");
	}	
}


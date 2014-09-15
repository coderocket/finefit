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

import java.io.PrintStream;

public class Flattener {

	static public class Table {

		private Cell[][] cells;

	  public Table(int height) {
			cells = new Cell[height][];
		}

		public void setRow(int i, Cell[] cs) {
			cells[i] = cs;
		}

		public Cell cell(int i, int j) {
			return cells[i][j];
		}

		public Cell[] row(int i) {
			return cells[i];
		}

		public int height() {
			return cells.length;
		}

		public void print(PrintStream out) {
			for(int i =0; i < cells.length; i++) {
				for(int j =0; j < cells[i].length; j++) {
						out.print(cells[i][j].predicate);	out.print('\t');	
				}
				out.print('\n');
			}
		}
	}

	static public class Cell {
		public int span;
		public String predicate;

		public Cell(int span, String predicate) {
			this.span = span;
			this.predicate = predicate;
		}
	}

	/* Takes a table of cells that represents a forest and flattens the forest by
	conjoining all the ancestors of each leaf. */
	

  public static String[] flatten(Table table) {

		if (table.height() == 0)
			return new String[0];


		int h = table.height();

		// Create an array of leaf tree nodes from the bottom row.

		TreeNode[] forrest = new TreeNode[table.row(h-1).length];

		/* We maintain the following invariant:

			The forrest array holds the forest that starts at row h and ends at
			the tree's leafs.
	
		*/

		for(int i = 0; i < forrest.length; ++i) {
			forrest[i] = new TreeNode(table.cell(h-1, i).predicate);
		}
		--h;

		// at this point the invariant holds for h = tree.height()-1

		while(h > 0) {
			
			// Create an array of tree nodes from row (h-1), taking their
			// children from the current forrest array.

			TreeNode[] new_forrest = new TreeNode[table.row(h-1).length];

			for (int i = 0, j = 0; i < new_forrest.length; ++i) {
				TreeNode[] children = new TreeNode[table.cell(h-1,i).span];
				int k = 0; 
				while(k < children.length) {
					children[k++] = forrest[j++];
				}
				new_forrest[i] = new TreeNode(children, table.cell(h-1,i).predicate);
			}
			forrest = new_forrest;
			--h;
		}

		// Finally, expand the predicates by traversing the forest from the forrest
		// to the leaves.

    String[] ps = new String[table.row(table.height()-1).length];

		int i = 0;
    for(TreeNode t : forrest) {
      for(String s : t.expand()) {
        ps[i++] = s;
      }
    }

		return ps;
	}
}

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

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class SutState {

	public class Table {

		int num_cols;

		Set<List<String> > rows;

		public Table(int numCols) {
			num_cols = numCols;
			rows = new HashSet<List<String> >();
		}

		public void add(String ... args) {
			List<String> row = new ArrayList<String>();
			for(String a : args) {
				row.add(a);	
			}
			assert(row.size() == num_cols);
			rows.add(row);
		}

		public int numCols() { return num_cols; }

		public Set<List<String> > rows() { return rows; }
	}

	Map<String, Table> vars;

	public SutState() {
		vars = new HashMap<String, Table>();
	}

	public Table add(String varName, int arity) {
		Table t = new Table(arity);
		vars.put(varName, t);
		return t;
	}

	public Table get(String varName) {
		return vars.get(varName);
	}

	public Set<Map.Entry<String, Table> > tables() { return vars.entrySet(); }
}

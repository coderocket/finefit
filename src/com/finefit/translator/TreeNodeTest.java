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

public class TreeNodeTest {

	public static void main(String[] args) {

		TreeNode[] leafs = new TreeNode[5];
		leafs[0] = new TreeNode(new TreeNode[0], "f");
		leafs[1] = new TreeNode(new TreeNode[0], "g");
		leafs[2] = new TreeNode(new TreeNode[0], "h");
		leafs[3] = new TreeNode(new TreeNode[0], "i");
		leafs[4] = new TreeNode(new TreeNode[0], "j");

		TreeNode t = new TreeNode(leafs, "x");

		t.print(System.out);
	}
}

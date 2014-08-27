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

package com.finefit.reporter;

import java.io.PrintStream;
import com.finefit.model.TestCase;
import com.finefit.model.State;

public class PrintStreamReporter implements Reporter {

		private PrintStream out;

		public PrintStreamReporter(PrintStream out) { this.out = out; }

		public void report(TestCase testcase, State state) {
			testcase.print(out);
			out.println("");
			state.print(out);
			out.println("");
		}

		public void report_deadlock() {
			out.println("\nError: Deadlock.");
		}

		public void report_discrepancy() {
			out.println("\nError: Discrepancy.");
		}
}

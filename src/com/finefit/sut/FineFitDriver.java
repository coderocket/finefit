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

package com.finefit.sut;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import com.finefit.model.TestCase;

public abstract class FineFitDriver implements SUT {

		protected Map<String, Operation > ops = new HashMap<String, Operation >();

		protected Map<String, String> exceptions = new HashMap<String, String>();

		protected abstract State retrieve();
    protected abstract void setup_sut(); 
    protected abstract void setup_operation_table();
    protected abstract void setup_exception_table();
		protected abstract void init_sut(com.finefit.model.State args);

    @Override
    public State applyOperation(TestCase testCase) throws Exception {

			String operationName = testCase.getOperationName(); 
			com.finefit.model.State args = testCase.getState(); 

			Operation op = ops.get(operationName);
			if (op == null)
				throw new NoSuchOperation(operationName);

			String report = "OK$0";
			State outputs = new State();

			try {
				op.apply(args, outputs);
			}
			catch(Exception err) {
				String code = exceptions.get(err.getClass().getName());
				if (code != null)
					report = code + "$0";
				else
					throw err;
			}

			outputs.add_output("report!", 1).add(report);
			State state = retrieve(); 
			state.add(outputs);

			return state;
		}

		@Override
		public State initialize(com.finefit.model.State args) {
			setup_sut();
			setup_operation_table();
			setup_exception_table();
			init_sut(args);
			return retrieve();
		}
}


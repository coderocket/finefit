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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.finefit.model.SUT;
import com.finefit.model.Operation;
import com.finefit.model.TestCase;
import com.finefit.model.State;

  public class FineFitDriver implements SUT {

		private Login sut;
		
    public FineFitDriver() {
			sut = new Login();
    }

    @Override
    public State initialize(State state) {
      sut.init(); 
      return sut.retrieve(state);
    }

    @Override
    public State applyOperation(TestCase testCase) throws InvalidNumberOfArguments, NoSuchOperation {

			
      String operationName = testCase.getOperationName();
			State state = testCase.getState();

      if (operationName.equals("Login")) {
      	String userName = state.getArg("u");
				sut.login(userName);

      } else if (operationName.equals("Logout")) {
      	String userName = state.getArg("u");
				sut.logout(userName);

      } else if (operationName.equals("Register")) {
      	String userName = state.getArg("u");
				sut.register(userName);

      } else if (operationName.equals("UnRegister")) {
      	String userName = state.getArg("u");
				sut.unregister(userName);
      }
      else throw new NoSuchOperation();

      return sut.retrieve(state);
    }

  }


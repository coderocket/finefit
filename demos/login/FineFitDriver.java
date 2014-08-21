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
import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;
import com.finefit.sutinterface.SUT;
import com.finefit.testcasegenerator.Operation;
import com.finefit.testcasegenerator.SystemState;
import com.finefit.testcasegenerator.TestCase;
import com.finefit.testcasegenerator.StateVariables;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;

  public class FineFitDriver implements SUT {

		private Login sut;
		
    public FineFitDriver() {
			sut = new Login();
    }

    @Override
    public SystemState initialize(Universe universe,Instance args) {
      sut.init(); 
      return new SystemState(sut.retrieve(universe, new StateVariables(args)));
    }

    @Override
    public SystemState applyOperation(TestCase testCase, Instance args)
        throws InvalidNumberOfArguments, NoSuchOperation,
        NoDataException {

      Operation operation = testCase.getOperation();
      String operationName = operation.getName();

      if (operationName.equals("Login")) {
      	String userName = operation.getArg(args,"u");
				sut.login(userName);

      } else if (operationName.equals("Logout")) {
      	String userName = operation.getArg(args,"u");
				sut.logout(userName);

      } else if (operationName.equals("Register")) {
      	String userName = operation.getArg(args,"u");
				sut.register(userName);

      } else if (operationName.equals("UnRegister")) {
      	String userName = operation.getArg(args,"u");
				sut.unregister(userName);
      }
      else throw new NoSuchOperation();

      return new SystemState(sut.retrieve(testCase.getUniverse(), new StateVariables(args)));
    }

  }


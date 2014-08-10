
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
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;

  public class FineFitDriver implements SUT {

		private Login sut;
		
    public FineFitDriver() {
			sut = new Login();
    }

    @Override
    public SystemState initialize(Universe universe,Instance args) {
      sut.init(); 
      return new SystemState(sut.retrieve(universe, args));
    }

    @Override
    public SystemState applyOperation(TestCase testCase, Instance args)
        throws InvalidNumberOfArguments, NoSuchOperation,
        NoDataException {

      Operation operation = testCase.getOperation();
      String operationName = operation.getName();

      if (operationName.equals("Login")) {
      	String userName = testCase.getArg(args,"u");
				sut.login(userName);

      } else if (operationName.equals("Logout")) {
      	String userName = testCase.getArg(args,"u");
				sut.logout(userName);

      } else if (operationName.equals("Register")) {
      	String userName = testCase.getArg(args,"u");
				sut.register(userName);

      } else if (operationName.equals("UnRegister")) {
      	String userName = testCase.getArg(args,"u");
				sut.unregister(userName);
      }
      else throw new NoSuchOperation();

      return new SystemState(sut.retrieve(testCase.getUniverse(), args));
    }

  }


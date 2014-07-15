
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
      String operationName = operation.getOperation().label.replace("this/", "");

      if (operationName.equals("Login")) {
      	String userName = extractUserName(operation, args);
				sut.login(userName);

      } else if (operationName.equals("Logout")) {
      	String userName = extractUserName(operation, args);
				sut.logout(userName);

      } else if (operationName.equals("Register")) {
      	String userName = extractUserName(operation, args);
				sut.register(userName);

      } else if (operationName.equals("UnRegister")) {
      	String userName = extractUserName(operation, args);
				sut.unregister(userName);
      }
      else throw new NoSuchOperation();

      return new SystemState(sut.retrieve(testCase.getUniverse(), args));
    }

    private String extractUserName(Operation func, Instance args) {
      String userName = "";
      Map<String, TupleSet> vars = new HashMap<String, TupleSet>();
      for (Map.Entry<Relation,TupleSet> e : args.relationTuples().entrySet()) {
        vars.put(e.getKey().name(), e.getValue());
      }

      List<String> listOfParams = new ArrayList<String>();
      int i = 0;
      //we need to go over the declarations and on the  map {s.instance().relationTuples()} and find the same variable names so we can use them as input
      for(Decl decl: func.getOperation().decls){
        System.out.println(decl.get().label + " = " + vars.get("$" + decl.get().label));

        TupleSet name = vars.get("$" + decl.get().label);
        String nameStr = (String) name.iterator().next().atom(0);

        listOfParams.add(i,nameStr);
        i++;
      }

      userName = listOfParams.get(listOfParams.size()-1);
      return userName;
    }
  }


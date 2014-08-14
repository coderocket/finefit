
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

		private PhotoAlbum sut;
		
    public FineFitDriver() {
			sut = new PhotoAlbum();
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

			if (operationName.equals("addPhoto")) {
				String pid = operation.getArg(args, "pid");
				try {
				sut.AddPhoto(pid);
				}
				catch(PhotoAlbum.PhotoExists err) {}
				catch(PhotoAlbum.ContainerIsFull err) {}
			}
			else throw new NoSuchOperation();

			return new SystemState(sut.retrieve(testCase.getUniverse(), new StateVariables(args)));
    }

  }


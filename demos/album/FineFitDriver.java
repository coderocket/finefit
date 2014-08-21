
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
import com.finefit.testcasegenerator.State;

  public class FineFitDriver implements SUT {

		private PhotoAlbum sut;
		
    public FineFitDriver() {
			sut = new PhotoAlbum();
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

			if (operationName.equals("addPhoto")) {
				String pid = state.getArg("pid");
				try {
					sut.AddPhoto(pid);
				}
				catch(PhotoAlbum.PhotoExists err) {}
				catch(PhotoAlbum.ContainerIsFull err) {}
			}
			else if (operationName.equals("removePhoto")) {
				int i = Integer.parseInt(state.getArg("i"));
				sut.RemovePhoto(i);
			}
			else throw new NoSuchOperation();

			return sut.retrieve(state);
    }

  }


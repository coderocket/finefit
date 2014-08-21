
import com.finefit.model.SUT;
import com.finefit.model.Operation;
import com.finefit.model.TestCase;
import com.finefit.model.State;

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


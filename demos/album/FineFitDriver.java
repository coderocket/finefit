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

import java.util.List;
import java.util.ArrayList;

import com.finefit.sut.SUT;
import com.finefit.sut.State;
import com.finefit.sut.NoSuchOperation;
import com.finefit.sut.InvalidNumberOfArguments;
import com.finefit.model.TestCase;

public class FineFitDriver implements SUT {

		private PhotoAlbum sut;
		
    public FineFitDriver() {
			sut = new PhotoAlbum();
    }

    @Override
    public State initialize(com.finefit.model.State args) {
			sut.init();
      return sut.retrieve();
    }

    @Override
    public State applyOperation(TestCase testCase) throws Exception {

			String operationName = testCase.getOperationName(); 
			com.finefit.model.State args = testCase.getState();
			State outputs = new State();

			if (operationName.equals("addPhoto")) {
				String pid = args.getArg("pid");
				String result = "0";  
				try {
					sut.AddPhoto(pid);
					result = "OK$0";
				}
				catch(PhotoAlbum.PhotoExists err) {
					result = "ALREADY_IN$0";
				}
				catch(PhotoAlbum.ContainerIsFull err) {
					result = "ALBUM_FULL$0";
				}

				outputs.add_output("result!", 1).add(result);
			}
			else if (operationName.equals("removePhoto")) {
				int i = Integer.parseInt(args.getArg("i"));
				sut.RemovePhoto(i);
			}
			else if (operationName.equals("save")) {
				sut.Save();
			}
			else throw new NoSuchOperation(operationName);

			State state = sut.retrieve();
			state.add(outputs);
			return state;
    }

}


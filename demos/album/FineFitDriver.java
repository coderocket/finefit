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

import kodkod.instance.TupleFactory;
import kodkod.instance.Tuple;

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
			State state = testCase.getState().clone();
			if (operationName.equals("addPhoto")) {
				String pid = state.getArg("pid");
				String result = "0";  
				try {
					sut.AddPhoto(pid);
					result = "1";
				}
				catch(PhotoAlbum.PhotoExists err) {}
				catch(PhotoAlbum.ContainerIsFull err) {}

				TupleFactory factory = state.factory();
				List<Tuple> r = new ArrayList<Tuple>(); r.add(factory.tuple(result));
				state.addOutput("result!", 1, r);
			}
			else if (operationName.equals("removePhoto")) {
				int i = Integer.parseInt(state.getArg("i"));
				sut.RemovePhoto(i);
			}
			else if (operationName.equals("save")) {
				sut.Save();
			}
			else throw new NoSuchOperation();


			return sut.retrieve(state);
    }

}


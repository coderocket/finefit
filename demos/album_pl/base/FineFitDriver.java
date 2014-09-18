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

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.finefit.model.SUT;
import com.finefit.model.Operation;
import com.finefit.model.TestCase;
import com.finefit.model.State;
import com.finefit.model.SutState;

public class FineFitDriver implements SUT {

		private ArrayPhotoAlbum sut;
		
    public FineFitDriver() {
			sut = new ArrayPhotoAlbum(5);
    }

    @Override
    public SutState initialize(State args) {
			sut = new ArrayPhotoAlbum(5);
      return sut.retrieve();
    }

    @Override
    public SutState applyOperation(TestCase testCase) throws Exception {

			String operationName = testCase.getOperationName(); 
			State args = testCase.getState();

			SutState outputs = new SutState();

			if (operationName.equals("addPhoto")) {
				String id = args.getArg("p");

				outputs.add_output("report!", 1);

				String report = "1";

				try {
					Photo p = sut.addPhoto(id);
					IdMap.instance().associate(p, id);
				}
        catch(PhotoAlbum.PhotoExists err) { report = "-1"; }
        catch(PhotoAlbum.AlbumIsFull err) { report = "-2"; }

				outputs.get_output("report!").add(report);
			}
			else if (operationName.equals("viewPhotos")) {

				Set<Photo> photos = sut.viewPhotos(); 

				outputs.add_output("result!", 1);

				for(Photo p : photos){
					outputs.get_output("result!").add(IdMap.instance().obj2atom(p));
				}
			}
			else 
				throw new NoSuchOperation(operationName);


			SutState state = sut.retrieve();
			state.add(outputs);
			return state;
    }
}


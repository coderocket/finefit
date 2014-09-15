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

import kodkod.instance.TupleFactory;
import kodkod.instance.Tuple;

import com.finefit.model.SUT;
import com.finefit.model.Operation;
import com.finefit.model.TestCase;
import com.finefit.model.State;

public class FineFitDriver implements SUT {

		private ArrayPhotoAlbum sut;
		
    public FineFitDriver() {
			sut = null;
    }

    @Override
    public State initialize(State state) {
			String owner_name = state.getArg("OWNER_NAME");
			String owner_passwd = state.getArg("OWNER_PASSWD");
			sut = new ArrayPhotoAlbum(5, owner_name, owner_passwd);
      return sut.retrieve(state);
    }

    @Override
    public State applyOperation(TestCase testCase) throws InvalidNumberOfArguments, NoSuchOperation {


			String operationName = testCase.getOperationName(); 
			State state = testCase.getState().clone();

			TupleFactory factory = state.factory();

			if (operationName.equals("addPhoto")) {
				String id = state.getArg("p");

				String result = "1";

				try {
					Photo p = sut.addPhoto(id);
					IdMap.instance().associate(p, id);
				}
        catch(PhotoAlbum.PhotoExists err) { result = "-1"; }
        catch(PhotoAlbum.AlbumIsFull err) { result = "-2"; }
        catch(PhotoAlbum.OwnerNotLoggedIn err) { result = "-3"; }


				List<Tuple> r = new ArrayList<Tuple>(); r.add(factory.tuple(result));
				state.addOutput("report!", 1, r);
			}
			else if (operationName.equals("viewPhotos")) {

				Set<Photo> photos = sut.viewPhotos(); 
				List<Tuple> r = new ArrayList<Tuple>(); 
				for(Photo p : photos){
					r.add(factory.tuple(IdMap.instance().obj2atom(p)));
				}
				state.addOutput("result!", 1, r);
			}
			else 
				throw new NoSuchOperation();


			return sut.retrieve(state);
    }

}


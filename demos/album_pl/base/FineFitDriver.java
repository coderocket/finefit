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

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.finefit.sut.SUT;
import com.finefit.sut.NoSuchOperation;
import com.finefit.sut.InvalidNumberOfArguments;
import com.finefit.sut.Operation;
import com.finefit.sut.State;
import com.finefit.sut.IdMap;
import com.finefit.model.TestCase;

public class FineFitDriver implements SUT {

    private Map<String, Operation > ops = new HashMap<String, Operation >();

		void setup_operation_table() {

      ops.put("addPhoto", new Operation() {
				PhotoAlbum s = sut;
        public void apply(com.finefit.model.State args, State outputs) throws Exception {
          String id = args.getArg("p");
          Photo p = s.addPhoto(id);
          IdMap.instance().associate(p, id);
        } });

      ops.put("viewPhotos", new Operation() {
				PhotoAlbum s = sut;
        public void apply(com.finefit.model.State args, State outputs) throws Exception {

          outputs.add_output("result!", 1);

          Set<Photo> photos = s.viewPhotos();
          
          for(Photo p : photos) {
						outputs.get_output("result!").add(IdMap.instance().obj2atom(p));
          }
				} });
		}

		private ArrayPhotoAlbum sut;
		
    @Override
    public State initialize(com.finefit.model.State args) {
			sut = new ArrayPhotoAlbum(5);
			setup_operation_table();
      return sut.retrieve();
    }

    @Override
    public State applyOperation(TestCase testCase) throws Exception {

      String operationName = testCase.getOperationName();
      com.finefit.model.State args = testCase.getState();

      Operation op = ops.get(operationName);
      if (op == null)
        throw new NoSuchOperation(operationName);

      String report = "OK$0";
      State outputs = new State();

      try {
        op.apply(args, outputs);
      }
      catch(PhotoAlbum.PhotoExists err) { report = "PHOTO_EXISTS$0"; }
      catch(PhotoAlbum.AlbumIsFull err) { report = "ALBUM_FULL$0"; }

      outputs.add_output("report!", 1).add(report);

			State state = sut.retrieve();
			state.add(outputs);
			return state;
    }
}


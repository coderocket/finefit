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
import com.finefit.sut.*;
import com.finefit.sut.FineFitDriver;

public class PhotoAlbumDriver extends FineFitDriver {

		protected void setup_operation_table() {

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

		@Override protected void setup_sut() {
			sut = new ArrayPhotoAlbum(5);
		}

    @Override
    public void init_sut(com.finefit.model.State args) { }

	  @Override public State retrieve() { return sut.retrieve(); }

		protected void setup_exception_table() {
			exceptions.put("PhotoAlbum$PhotoExists", "PHOTO_EXISTS$0");
			exceptions.put("PhotoAlbum$AlbumIsFull", "ALBUM_FULL$0");
		}

}


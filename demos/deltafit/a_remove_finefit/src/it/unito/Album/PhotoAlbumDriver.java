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

package albumsimplecore.a_remove_finefit.it.unito.Album;

import java.util.Set;
import java.util.HashSet;
import com.finefit.sut.*;
import com.finefit.sut.FineFitDriver;

public class PhotoAlbumDriver extends FineFitDriver {

		/*** modified by DRemoveFineFit
		*/

		protected void setup_exception_table() {

      exceptions.put("albumsimplecore.a_remove_finefit.it.unito.Album.PhotoAlbum$PhotoExists", "PHOTO_EXISTS");
      exceptions.put("albumsimplecore.a_remove_finefit.it.unito.Album.PhotoAlbum$AlbumIsFull", "ALBUM_FULL");

			// DRemoveFineFit

      exceptions.put("java.lang.IllegalArgumentException", "NO_PHOTO");
		}

    protected void setup_operation_table() {

			ops.put("addPhoto", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					String id = args.getArg("p?");
					Photo p = sut.addPhoto(id); 
					IdMap.instance().associate(p, id);
				} });

			ops.put("viewPhoto", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {

					outputs.add_output("result!", 1);

					Set<Photo> photos = sut.viewPhotos(); 
					for(Photo p : photos){
						outputs.get_output("result!").add(IdMap.instance().obj2atom(p));
					}
				 } });

			/*** added by DRemoveFineFit
			*/
      ops.put("removePhoto", new Operation() {
        PhotoAlbum s = sut;
        public void apply(com.finefit.model.State args, State outputs) throws Exception {
          int i = Integer.parseInt(args.getArg("l?"));
          sut.removePhoto(i);
        } });

		}

		private ArrayPhotoAlbum sut;

		@Override protected void setup_sut() {
			sut = new ArrayPhotoAlbum(5);
		}

		@Override public State retrieve() { return sut.retrieve(); }

		@Override public void init_sut(com.finefit.model.State args) {
		}
}


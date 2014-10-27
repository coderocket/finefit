
package albumsimplecore.a_base_finefit.it.unito.Album;

import java.util.Set;
import com.finefit.sut.*;
import com.finefit.sut.FineFitDriver;

/*** added by DBaseFineFit
 */
public class PhotoAlbumDriver extends FineFitDriver {
	private ArrayPhotoAlbum sut;
	protected void setup_sut() {
		sut = new ArrayPhotoAlbum(5);
	}
	public void init_sut(com.finefit.model.State args) {
	}
	public State retrieve() {
		return sut.retrieve();
	}
	protected void setup_operation_table() {
		ops.put("addPhoto", new Operation() {
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws
				Exception {
					String id = args.getArg("p");
					Photo p = s.addPhoto(id);
					IdMap.instance().associate(p, id);
				}
			});
		ops.put("viewPhotos", new Operation() {
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws
				Exception {
					outputs.add_output("result!", 1);
					Set<Photo> photos = s.viewPhotos();
					for(Photo p : photos) {
						outputs.get_output("result!").add(IdMap.instance().obj2atom(p));
					}
				}
			});
	}
	protected void setup_exception_table() {
		exceptions.put("PhotoAlbum$PhotoExists", "PHOTO_EXISTS$0");
		exceptions.put("PhotoAlbum$AlbumIsFull", "ALBUM_FULL$0");
	}
}

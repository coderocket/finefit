package albumsimplecore.a_remove.it.unito.Album;

import java.util.Set;
/*** added by DBase* modified by DRemove
 */
public interface PhotoAlbum {
	public Photo addPhoto(String image);
	public Set<Photo> viewPhotos();
	public class PhotoExists extends RuntimeException {
	}
	public class AlbumIsFull extends RuntimeException {
	}
	/*** added by DRemove
	 */
	public void removePhoto(int location);
}
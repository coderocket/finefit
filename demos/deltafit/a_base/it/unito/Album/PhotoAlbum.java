package albumsimplecore.a_base.it.unito.Album;

import java.util.Set;
/*** added by DBase
 */
public interface PhotoAlbum {
	public Photo addPhoto(String image);
	public Set<Photo> viewPhotos();
	public class PhotoExists extends RuntimeException {
	}
	public class AlbumIsFull extends RuntimeException {
	}
}
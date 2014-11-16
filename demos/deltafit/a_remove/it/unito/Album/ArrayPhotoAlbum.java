package albumsimplecore.a_remove.it.unito.Album;

import java.util.Set;
import java.util.HashSet;
/*** added by DBase* modified by DRemove
 */
public class ArrayPhotoAlbum implements PhotoAlbum {
	private int size = 0;
	private Photo [] photoAt;
	public ArrayPhotoAlbum(int maxSize) {
		if(maxSize < 1) throw new IllegalArgumentException("IllegalSize");
		photoAt = new Photo[maxSize];
	}
	boolean imageIsInAlbum(String image) {
		for(int i = 0;
			i < size;
			i ++) {
			Photo p = photoAt[i];
			if(p.getImage().equals(image)) return true;
		}
		return false;
	}
	public Photo addPhoto(String image) {
		if(image == null) throw new IllegalArgumentException("NullImage");
		if(size == photoAt.length) throw new AlbumIsFull();
		if(imageIsInAlbum(image)) throw new PhotoExists();
		Photo new_photo = new Photo(image);
		photoAt[size] = new_photo;
		size = size + 1;
		return new_photo;
	}
	public Set<Photo> viewPhotos() {
		Set<Photo> result = new HashSet<Photo>();
		for(int i = 0;
			i < size;
			i ++) {
			result.add(photoAt[i]);
		}
		return result;
	}
	/*** added by DRemove
	 */
	public void removePhoto(int location) {
		if((location < 0) ||(size <= location)) throw new
		IllegalArgumentException("IllegalLocation");
		photoAt[location] = photoAt[size - 1];
		photoAt[size - 1] = null;
		size = size - 1;
	}
}

import java.util.Set;
import java.lang.RuntimeException;

interface PhotoAlbum {

    class PhotoExists extends RuntimeException {}
    class AlbumIsFull extends RuntimeException {}

	Photo addPhoto(String image) throws IllegalArgumentException;

	// MISTAKE: String view Photos();
	Set<Photo> viewPhotos();
}


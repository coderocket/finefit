
import java.util.Set;

interface PhotoAlbum {

    class PhotoExists extends Exception {}
    class AlbumIsFull extends Exception {}

	Photo addPhoto(String image) throws IllegalArgumentException, AlbumIsFull, PhotoExists;

	// MISTAKE: String view Photos();
	Set<Photo> viewPhotos();
}


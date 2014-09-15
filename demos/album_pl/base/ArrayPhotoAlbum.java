
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import kodkod.instance.TupleFactory;
import kodkod.instance.Tuple;
import com.finefit.model.State;

class ArrayPhotoAlbum implements PhotoAlbum {

	int size = 0; // number of photos in the album
	Photo[] photoAt; // photos in the album are at locations 0..size-1; other locations contain null

	public ArrayPhotoAlbum(int maxSize) {
		if (maxSize < 1)
			throw new IllegalArgumentException("IllegalSize");
		photoAt = new Photo[maxSize];
	}

	public boolean imageIsInAlbum(String image) {
		int i = 0;
		boolean foundIt = false;
		while (i < size && !foundIt) {
			Photo p = photoAt[i];
			if (p.getImage().equals(image)) foundIt = true;
				i++;
		}
		return foundIt;
	}

	public Photo addPhoto(String image) throws IllegalArgumentException, AlbumIsFull, PhotoExists {
		if (image == null)
			throw new IllegalArgumentException("NullImage");

		if (size == photoAt.length)
			throw new AlbumIsFull();

		if (imageIsInAlbum(image))
			throw new PhotoExists();

		Photo new_photo = new Photo(image);
		photoAt[size] = new_photo;
		size = size + 1;
		return new_photo;
	}

	public Set<Photo> viewPhotos() {
		// MISTAKE: new HashSet(<Photo>);
		Set<Photo> result = new HashSet<Photo>();
		for (int i = 0; i < size; i++) { 
			// MISTAKE: result = result.add(photoAt[i]);
			result.add(photoAt[i]); 
		}
		return result;
	}

	public State retrieve(State prevState) {

		TupleFactory factory = prevState.factory();
		State currentState = prevState.clone();

		List<Tuple> photoAtTuples = new ArrayList();
		for (int i = 0; i < size; i++) {
			photoAtTuples.add(factory.tuple("State$0" , "" + i, IdMap.instance().obj2atom(photoAt[i]))); 
		}

		currentState.add("photoAt", 3, photoAtTuples);
		return currentState;
	}
}

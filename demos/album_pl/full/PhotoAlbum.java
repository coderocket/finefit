
import java.util.Set;
import java.lang.RuntimeException;

interface PhotoAlbum {

    class PhotoExists extends RuntimeException {}
    class AlbumIsFull extends RuntimeException {}

	Photo addPhoto(String image) throws IllegalArgumentException;

	// MISTAKE: String view Photos();
	Set<Photo> viewPhotos(); 

	// from DRemove

	public void removePhoto(int location); 

	// from DOwner

		class OwnerNotLoggedIn extends RuntimeException {}
		class AlreadyLogged extends RuntimeException {}
		class AuthFailed extends RuntimeException {}
		class NotAuthorized extends RuntimeException {}
		class RemoveOwner extends RuntimeException {}

	public void login(String name, String password); 
	public void logout();

	// from DGroups

		class MissingUsers extends RuntimeException {}
		class MissingGroup extends RuntimeException {}
		class RemoveOwnerGroup extends RuntimeException {}

	public User updateUser(String name, String password); 
	public Group updateGroup(String name, Set<String> memberNames); 
	public void removeUser(String name); 
	public void removeGroup(String name); 
	public void updatePhotoGroup(int location, String groupName); 
}


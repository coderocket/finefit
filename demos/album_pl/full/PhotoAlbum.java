
import java.util.Set;

interface PhotoAlbum {

    class PhotoExists extends Exception {}
    class AlbumIsFull extends Exception {}

	Photo addPhoto(String image) throws IllegalArgumentException, AlbumIsFull, PhotoExists, OwnerNotLoggedIn;

	// MISTAKE: String view Photos();
	Set<Photo> viewPhotos() throws NotAuthorized;

	// from DRemove

	public void removePhoto(int location) throws OwnerNotLoggedIn;

	// from DOwner

		class OwnerNotLoggedIn extends Exception {}
		class AlreadyLogged extends Exception {}
		class AuthFailed extends Exception {}
		class NotAuthorized extends Exception {}
		class RemoveOwner extends Exception {}

	public void login(String name, String password) throws AlreadyLogged, AuthFailed; 
	public void logout();

	// from DGroups

		class MissingUsers extends Exception {}
		class MissingGroup extends Exception {}
		class RemoveOwnerGroup extends Exception {}

	public User updateUser(String name, String password) throws NotAuthorized;
	public Group updateGroup(String name, Set<String> memberNames) throws NotAuthorized, MissingUsers;
	public void removeUser(String name) throws NotAuthorized ;
	public void removeGroup(String name)  throws NotAuthorized, MissingGroup, RemoveOwnerGroup;
	public void updatePhotoGroup(int location, String groupName) throws OwnerNotLoggedIn, MissingGroup;
}


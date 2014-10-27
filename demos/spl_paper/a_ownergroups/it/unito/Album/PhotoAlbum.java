package albumsimplecore.a_ownergroups.it.unito.Album;

import java.util.Set;
/*** added by DBase* modified by DOwner* modified by DGroups
 */
public interface PhotoAlbum {
	public Photo addPhoto(String image);
	public Set<Photo> viewPhotos();
	public class PhotoExists extends RuntimeException {
	}
	public class AlbumIsFull extends RuntimeException {
	}
	/*** added by DOwner
	 */
	public void login(String name, String password);
	/*** added by DOwner
	 */
	public void logout();
	/*** added by DOwner
	 */
	public class AlreadyLogged extends RuntimeException {
	}
	/*** added by DOwner
	 */
	public class AuthFailed extends RuntimeException {
	}
	/*** added by DOwner
	 */
	public class NotAuthorized extends RuntimeException {
	}
	/*** added by DOwner
	 */
	public class OwnerNotLoggedIn extends RuntimeException {
	}
	/*** added by DGroups
	 */
	public User updateUser(String name, String password);
	/*** added by DGroups
	 */
	public Group updateGroup(String name, Set<String> memberNames);
	/*** added by DGroups
	 */
	public void removeUser(String name);
	/*** added by DGroups
	 */
	public void removeGroup(String name);
	/*** added by DGroups
	 */
	public void updatePhotoGroup(int location, String groupName);
	/*** added by DGroups
	 */
	public class MissingUser extends RuntimeException {
	}
	/*** added by DGroups
	 */
	public class MissingUsers extends RuntimeException {
	}
	/*** added by DGroups
	 */
	public class MissingGroup extends RuntimeException {
	}
	/*** added by DGroups
	 */
	public class RemoveOwnerGroup extends RuntimeException {
	}
	/*** added by DGroups
	 */
	public class RemoveOwner extends RuntimeException {
	}
}
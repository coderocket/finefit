package albumsimplecore.a_removeowner.it.unito.Album;

import java.util.Set;
/*** added by DBase* modified by DRemove* modified by DOwner
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
}
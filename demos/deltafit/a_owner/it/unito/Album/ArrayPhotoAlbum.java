package albumsimplecore.a_owner.it.unito.Album;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
/*** added by DBase* modified by DOwner
 */
public class ArrayPhotoAlbum implements PhotoAlbum {
	private int size = 0;
	private Photo [] photoAt;
	boolean imageIsInAlbum(String image) {
		for(int i = 0;
			i < size;
			i ++) {
			Photo p = photoAt[i];
			if(p.getImage().equals(image)) return true;
		}
		return false;
	}
	/*** modified by DOwner
	 */
	public Photo addPhoto(String image) {
		if(! isOwnerLoggedIn()) throw new OwnerNotLoggedIn();
		return addPhoto_original0(image);
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
	/*** added by DOwner
	 */
	private User owner;
	/*** added by DOwner
	 */
	private User loggedUser;
	/*** added by DOwner
	 */
	private Map<String, User> users;
	/*** added by DOwner
	 */
	public ArrayPhotoAlbum(int maxSize, String ownerName, String ownerPwd) {
		if(maxSize < 1) throw new IllegalArgumentException("IllegalSize");
		photoAt = new Photo[maxSize];
		this.owner = new User(ownerName, ownerPwd);
		users = new HashMap<String, User>();
		users.put(ownerName, owner);
	}
	/*** added by DOwner
	 */
	private boolean isOwnerLoggedIn() {
		return loggedUser == owner;
	}
	/*** added by DOwner
	 */
	public void login(String name, String password) {
		if(loggedUser != null) throw new AlreadyLogged();
		if((! users.containsKey(name)) ||(!
				users.get(name).getPassword().equals(password))) throw new AuthFailed();
		loggedUser = users.get(name);
	}
	/*** added by DOwner
	 */
	public void logout() {
		loggedUser = null;
	}
	/*** modified by DOwner
	 */
	public Photo addPhoto_original0(String image) {
		if(image == null) throw new IllegalArgumentException("NullImage");
		if(size == photoAt.length) throw new AlbumIsFull();
		if(imageIsInAlbum(image)) throw new PhotoExists();
		Photo new_photo = new Photo(image);
		photoAt[size] = new_photo;
		size = size + 1;
		return new_photo;
	}
}
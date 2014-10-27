package albumsimplecore.a_ownergroups.it.unito.Album;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
/*** added by DBase* modified by DOwner* modified by DGroups
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
	/*** modified by DOwner* modified by DGroups
	 */
	public Photo addPhoto(String image) {
		Photo p = addPhoto_original2(image);
		p.setGroup(groups.get(OWNER_GROUP_NAME));
		return p;
	}
	/*** modified by DGroups
	 */
	public Set<Photo> viewPhotos() {
		if(loggedUser == null) throw new NotAuthorized();
		Set<Photo> result = new HashSet<Photo>();
		for(int i = 0;
			i < size;
			i ++) {
			if(photoAt[i].getGroup().getMembers().contains(loggedUser)) {
				result.add(photoAt[i]);
			}
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
	/*** added by DGroups
	 */
	private Map<String, Group> groups;
	/*** added by DGroups
	 */
	private static final String OWNER_GROUP_NAME = "Owner";
	/*** added by DGroups
	 */
	public ArrayPhotoAlbum(int maxSize, String ownerName, String ownerPwd) {
		if(maxSize < 1) throw new IllegalArgumentException("IllegalSize");
		photoAt = new Photo[maxSize];
		owner = new User(ownerName, ownerPwd);
		users = new HashMap<String, User>();
		users.put(ownerName, owner);
		groups = new HashMap<String, Group>();
		groups.put(OWNER_GROUP_NAME, new Group(OWNER_GROUP_NAME, owner));
	}
	/*** added by DGroups
	 */
	public User updateUser(String name, String password) {
		if((name == null) ||(password == null)) throw new
		IllegalArgumentException();
		if(! isOwnerLoggedIn()) throw new NotAuthorized();
		if(users.containsKey(name)) {
			users.get(name).setPassword(password);
			return null;
		}
		else {
			User user = new User(name, password);
			users.put(name, user);
			return user;
		}
	}
	/*** added by DGroups
	 */
	public Group updateGroup(String name, Set<String> memberNames) {
		if((name == null) ||(memberNames == null)) throw new
		IllegalArgumentException();
		if(! isOwnerLoggedIn()) throw new NotAuthorized();
		if(! users.keySet().containsAll(memberNames)) throw new MissingUsers();
		Set<User> members = new HashSet<User>();
		for(String n : memberNames) {
			members.add(users.get(n));
		}
		if(groups.containsKey(name)) {
			groups.get(name).setMembers(members);
			return null;
		}
		else {
			Group new_group = new Group(name, members);
			groups.put(name, new_group);
			return new_group;
		}
	}
	/*** added by DGroups
	 */
	public void removeUser(String userName) {
		if(userName == null) throw new IllegalArgumentException();
		if(! isOwnerLoggedIn()) throw new NotAuthorized();
		if(! users.keySet().contains(userName)) throw new MissingUser();
		if(userName.equals(owner.getName())) throw new RemoveOwner();
		User user = users.get(userName);
		for(Group g : groups.values()) {
			g.getMembers().remove(user);
		}
		users.remove(userName);
	}
	/*** added by DGroups
	 */
	public void removeGroup(String name) {
		if(! isOwnerLoggedIn()) throw new NotAuthorized();
		if(name.equals(OWNER_GROUP_NAME)) throw new RemoveOwnerGroup();
		if((name == null) ||(! groups.containsKey(name))) throw new MissingGroup();
		boolean hasPhoto = false;
		int i = 0;
		while((! hasPhoto) &&(i < size)) {
			hasPhoto =(photoAt[i].getGroup() == groups.get(name));
			i ++;
		}
		if(hasPhoto) throw new IllegalArgumentException("GroupHasPhoto" +(i - 1));
		groups.remove(name);
	}
	/*** added by DGroups
	 */
	public void updatePhotoGroup(int location, String groupName) {
		if(! isOwnerLoggedIn()) throw new OwnerNotLoggedIn();
		if((location < 0) ||(size <= location)) throw new
		IllegalArgumentException("IllegalLocation");
		if(! groups.containsKey(groupName)) throw new MissingGroup();
		photoAt[location].setGroup(groups.get(groupName));
	}
	/*** modified by DOwner* modified by DGroups
	 */
	public Photo addPhoto_original2(String image) {
		if(! isOwnerLoggedIn()) throw new OwnerNotLoggedIn();
		return addPhoto_original0(image);
	}
}
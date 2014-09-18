
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import com.finefit.model.SutState;

class ArrayPhotoAlbum implements PhotoAlbum {

	int size = 0; // number of photos in the album
	Photo[] photoAt; // photos in the album are at locations 0..size-1; other locations contain null

/* Removed by DOwner:

	public ArrayPhotoAlbum(int maxSize) {
		if (maxSize < 1)
			throw new IllegalArgumentException("IllegalSize");
		photoAt = new Photo[maxSize];
	}
*/

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

	public Photo addPhoto$DOwner(String image) throws IllegalArgumentException, AlbumIsFull, PhotoExists {
		if (image == null)
			throw new IllegalArgumentException("NullImage");

		if (size == photoAt.length)
			throw new AlbumIsFull();

		if (imageIsInAlbum(image))
			throw new PhotoExists();

		Photo new_photo = new Photo(image);
		photoAt[size] = new_photo;
		size = size + 1;

		// from DGroups:
		photoAt[size-1].setGroup(groups.get(ownerGroupName));

		return new_photo;
	}

	public Set<Photo> viewPhotos() throws NotAuthorized {

		if (loggedUser == null) 
			throw new NotAuthorized();

		// MISTAKE: new HashSet(<Photo>);
		Set<Photo> result = new HashSet<Photo>();
		for (int i = 0; i < size; i++) { 
			// MISTAKE: result = result.add(photoAt[i]);
			result.add(photoAt[i]); 
		}
		return result;
	}

	// from DRemove

	public void removePhoto$Downer(int location) {

		if ((location < 0) || (size <= location)) 
			throw new IllegalArgumentException("IllegalLocation");

		photoAt[location] = photoAt[size-1];
		photoAt[size-1] = null;
		size = size -1;
	}

	public void removePhoto(int location) throws OwnerNotLoggedIn {
		if (!isOwnerLoggedIn()) 
			throw new OwnerNotLoggedIn(); 
		removePhoto$Downer(location);
	}

	// from DOwner

	User owner;

	/* Removed by DGroups: 
	boolean ownerLoggedIn = false;
	*/

	public Photo addPhoto(String image) throws IllegalArgumentException, AlbumIsFull, PhotoExists, OwnerNotLoggedIn {
		if (!isOwnerLoggedIn()) 
			throw new OwnerNotLoggedIn();	

		return addPhoto$DOwner(image);
	}

	public ArrayPhotoAlbum(int maxSize, User owner, Group ownerGroup) {
		if (maxSize < 1)
			throw new IllegalArgumentException("IllegalSize");
		photoAt = new Photo[maxSize];
		this.owner = owner;
		users = new HashMap<String,User>();
		groups = new HashMap<String,Group>();
		users.put(owner.getName(), owner);
		ownerGroupName = ownerGroup.getName();
		groups.put(ownerGroupName, ownerGroup);
	}

	public boolean isOwnerLoggedIn() { return loggedUser == owner; }

	public void login(String name, String password) throws AlreadyLogged, AuthFailed {
		if (loggedUser != null)
			throw new AlreadyLogged();
		if ((!users.containsKey(name)) || (!users.get(name).getPassword().equals(password)))
			throw new AuthFailed();
		loggedUser = users.get(name); 
	}

	public void logout() { loggedUser = null; }

	// from DGroups

	Map<String, User> users;
	Map<String, Group> groups;
	User loggedUser;
	String ownerGroupName;

	public User updateUser(String name, String password) throws NotAuthorized {
		if ((name==null) || (password==null)) 
			throw new IllegalArgumentException();

		if (!isOwnerLoggedIn()) 
			throw new NotAuthorized();

		if (users.containsKey(name)) {
			users.get(name).setPassword(password);
			return null;
		}
		else { 
			User user = new User(name, password);
			users.put(name, user);
			return user;
		}
	}

	public Group updateGroup(String name, Set<String> memberNames) throws NotAuthorized, MissingUsers {

		if ((name==null) || (memberNames==null)) 
			throw new IllegalArgumentException();

		if (!isOwnerLoggedIn()) 
			throw new NotAuthorized();

		if (!users.keySet().containsAll(memberNames)) 
			throw new MissingUsers();

		Set<User> members = new HashSet<User>();
		for (String n : memberNames) { members.add(users.get(n)); }
		/* An error detected by FineFit 

		if (users.containsKey(name)) {

		*/
		if (groups.containsKey(name)) {
			groups.get(name).setMembers(members);
			return null;
		}
		else {
			Group new_group = new Group(name, members); 
			groups.put(name, new_group);
			return new_group;
		}
	}

	public void removeUser(String name) {

	}

	public void removeGroup(String name) throws NotAuthorized, MissingGroup, RemoveOwnerGroup {

/* An error detect by FineFit (the same as for updatePhotoGroup) */

		if (!isOwnerLoggedIn()) 
			throw new NotAuthorized();

		if (name.equals(ownerGroupName))
			throw new RemoveOwnerGroup();

		if ((name ==null) || (!groups.containsKey(name)))
			throw new MissingGroup();

		boolean hasPhoto = false;
		int i = 0;
		while ((!hasPhoto) && (i < size)) {
			hasPhoto = (photoAt[i].getGroup() == groups.get(name));
			i++;
		}

		if (hasPhoto) 
			throw new IllegalArgumentException("GroupHasPhoto"+(i-1));

		groups.remove(name);
	}

	public void updatePhotoGroup(int location, String groupName) throws OwnerNotLoggedIn, MissingGroup {

/* An error detected by FineFit: originally we first tested the location and only then if the
owner is logged in. However the spec insists that when the owner is not logged in the error
should be NOT_AUTH regardless of the value of the index. 

*/
		if (!isOwnerLoggedIn()) 
			throw new OwnerNotLoggedIn();

		if ((location < 0) || (size <= location)) 
			throw new IllegalArgumentException("IllegalLocation");

		if (!groups.containsKey(groupName)) 
			throw new MissingGroup();

		photoAt[location].group = groups.get(groupName);
	}


/* FineFit testing code */

	public SutState retrieve() {

		SutState state = new SutState();

		state.add_state("photoAt", 2);

		for (int i = 0; i < size; i++) {
			state.get_state("photoAt").add("" + i, IdMap.instance().obj2atom(photoAt[i]));
		}

		state.add_state("ownerName", 1).add(owner.getName());

		state.add_state("ownerGroupName", 1).add(ownerGroupName);

		state.add_state("users", 2);
		for(Map.Entry<String, User> e : users.entrySet()) {
			state.get_state("users").add(e.getKey(), IdMap.instance().obj2atom(e.getValue()));
		}

		state.add_state("groups", 2);
		for(Map.Entry<String, Group> e : groups.entrySet()) {
			state.get_state("groups").add(e.getKey(), IdMap.instance().obj2atom(e.getValue()));
		}

		state.add_state("loggedIn", 1);

		if (loggedUser != null)
			state.get_state("loggedIn").add(loggedUser.getName());

		state.add_state("passwords", 2);

		for(User u : users.values()) {
			state.get_state("passwords").add(IdMap.instance().obj2atom(u), u.getPassword());
		}

		state.add_state("groupPhotos", 2);
		int i = 0;
		while(i < size) {
			state.get_state("groupPhotos").add(IdMap.instance().obj2atom(photoAt[i]), IdMap.instance().obj2atom(photoAt[i].getGroup()));
			++i;
		}

		state.add_state("members", 2);
		for(Group g : groups.values()) {
			for(User u : g.getMembers()) {
				state.get_state("members").add(IdMap.instance().obj2atom(g), IdMap.instance().obj2atom(u));
			}
		}

		return state;
	}

}

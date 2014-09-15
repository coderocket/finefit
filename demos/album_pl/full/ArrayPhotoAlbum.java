
import java.util.Map;
import java.util.HashMap;
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

	public ArrayPhotoAlbum(int maxSize, String ownerName, String ownerPwd) {
		if (maxSize < 1)
			throw new IllegalArgumentException("IllegalSize");
		photoAt = new Photo[maxSize];
		owner = new User(ownerName, ownerPwd);
		users = new HashMap<String,User>();
		groups = new HashMap<String,Group>();
		users.put(ownerName, owner);
		Set<User> ownerGroupUsers = new HashSet<User>();
		ownerGroupUsers.add(owner);
		groups.put("Owner", new Group("Owner", ownerGroupUsers));
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

	public void updateUser(String name, String password) throws NotAuthorized {
		if ((name==null) || (password==null)) 
			throw new IllegalArgumentException();
		if (!isOwnerLoggedIn()) 
			throw new NotAuthorized();
		if (users.containsKey(name)) 
			users.get(name).setPassword(password);
		else
			users.put(name, new User(name, password));
	}

	public void updateGroup(String name, Set<String> memberNames) throws NotAuthorized, MissingUsers {

		if ((name==null) || (memberNames==null)) 
			throw new IllegalArgumentException();

		if (!isOwnerLoggedIn()) 
			throw new NotAuthorized();

		if (!users.keySet().containsAll(memberNames)) 
			throw new MissingUsers();

		Set<User> members = new HashSet<User>();
		for (String n : memberNames) { members.add(users.get(n)); }
		if (users.containsKey(name)) 
			groups.get(name).setMembers(members);
		else
			groups.put(name, new Group(name, members));
	}

	public void removeUser(String name) {

	}

	public void removeGroup(String name) throws NotAuthorized, MissingGroup {

		if (name.equals("Owner")) 
			throw new IllegalArgumentException("CannotRemoveOwnerGroup");

		if (!isOwnerLoggedIn()) 
			throw new NotAuthorized();

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

		if ((location < 0) || (size <= location)) 
			throw new IllegalArgumentException("IllegalLocation");

		if (!isOwnerLoggedIn()) 
			throw new OwnerNotLoggedIn();

		if (!groups.containsKey(groupName)) 
			throw new MissingGroup();

		photoAt[location].group = groups.get(groupName);
	}


/* FineFit testing code */

	public State retrieve(State prevState) {

		TupleFactory factory = prevState.factory();
		State currentState = prevState.clone();

		List<Tuple> photoAtTuples = new ArrayList<Tuple>();
		for (int i = 0; i < size; i++) {
			photoAtTuples.add(factory.tuple("State$0" , "" + i, IdMap.instance().obj2atom(photoAt[i]))); 
		}

		currentState.add("photoAt", 3, photoAtTuples);

		// from DOwner:

		List<Tuple> ownerTuple = new ArrayList<Tuple>();
		ownerTuple.add(factory.tuple("State$0", IdMap.instance().obj2atom(owner)));

		currentState.add("owner", 2, ownerTuple);

		// from DGroups:

		List<Tuple> usersTuples = new ArrayList<Tuple>();

		for(Map.Entry<String, User> e : users.entrySet()) {
			usersTuples.add(factory.tuple("State$0", e.getKey(), IdMap.instance().obj2atom(e.getValue())));
		}

		currentState.add("users", 3, usersTuples);

		List<Tuple> groupsTuples = new ArrayList<Tuple>();

		for(Map.Entry<String, Group> e : groups.entrySet()) {
			groupsTuples.add(factory.tuple("State$0", e.getKey(), IdMap.instance().obj2atom(e.getValue())));
		}

		currentState.add("groups", 3, groupsTuples);

		return currentState;
	}

}

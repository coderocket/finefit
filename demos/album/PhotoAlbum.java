
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.lang.Exception;
import java.util.Scanner;

import kodkod.instance.Universe;
import kodkod.instance.Instance;
import kodkod.instance.TupleFactory;
import kodkod.instance.Tuple;
import kodkod.ast.Relation;

import com.finefit.testcasegenerator.StateVariables;

public class PhotoAlbum {

    public class PhotoExists extends Exception {}
    public class ContainerIsFull extends Exception {}

    static int MAX_SIZE = 3; // maximal number of photos in the album

    List<Photo> photoAt;
    Set<String> deletedPhotos;

    public Instance retrieve(Universe universe, StateVariables stateVars) {
	TupleFactory factory = universe.factory();
	Instance instance = new Instance(universe);

	List<Tuple> photoAtTuples = new ArrayList();
	List<Tuple> toAddTuples = new ArrayList();
	List<Tuple> existingTuples = new ArrayList();
	int i = 0;
	for (Photo p : photoAt) {
	    photoAtTuples.add(factory.tuple("State$0", "" + i, p.getImage()));
	    if (p.getStatus() == Photo.Status.New)
		toAddTuples.add(factory.tuple("State$0", p.getImage()));
	    else if (p.getStatus() == Photo.Status.Old)
		existingTuples.add(factory.tuple("State$0", p.getImage()));
	    ++i;
	}

	List<Tuple> deletedPhotosTuples = new ArrayList();
	for (String key : deletedPhotos) {
	    deletedPhotosTuples.add( factory.tuple("State$0",key));
	    existingTuples.add( factory.tuple("State$0",key));
	}

	if (!photoAtTuples.isEmpty())
	    instance.add(stateVars.get("album"), factory.setOf(photoAtTuples)); 
	if (!toAddTuples.isEmpty())
	    instance.add(stateVars.get("toAdd"), factory.setOf(toAddTuples));
	if (!existingTuples.isEmpty())
	    instance.add(stateVars.get("existing"), factory.setOf(existingTuples));
	if (!deletedPhotosTuples.isEmpty())
	    instance.add(stateVars.get("toDelete"), factory.setOf(deletedPhotosTuples));

	return instance;
    }

    public PhotoAlbum() {
	photoAt = new ArrayList<Photo>();
	deletedPhotos = new HashSet<String>();
    }

	public void init() {
		photoAt = new ArrayList<Photo>();
		deletedPhotos = new HashSet<String>();
	}
		

    private boolean imageIsInAlbum(String image) {
	Iterator<Photo> i = photoAt.iterator();

	boolean foundIt = false;
	while (i.hasNext() && !foundIt) {
	    Photo p = i.next();
	    if (p.getImage().equals(image))
		foundIt = true;
	}
	return foundIt;
    }

    public void AddPhoto(String image) throws PhotoExists, ContainerIsFull {

	if (photoAt.size() == MAX_SIZE)
	    throw new ContainerIsFull();

	if (imageIsInAlbum(image)) 
	    throw new PhotoExists();
	
	if (deletedPhotos.contains(image))
	    deletedPhotos.remove(image);

	photoAt.add(new Photo(image));

    }

    public void ReplacePhoto(String image, int location) throws PhotoExists {
	
	assert location >= 0;
	assert location < photoAt.size();

	if (imageIsInAlbum(image)) 
	    throw new PhotoExists();
	
	Photo p = photoAt.get(location);

	if (p.getStatus() == Photo.Status.New) {
	    photoAt.set(location, new Photo(image));

	    // todo: remove this statement to inject a defect into the code
	    if (deletedPhotos.contains(image))
		;//deletedPhotos.remove(image);

	} else {
	    assert p.getStatus() == Photo.Status.Old;
	    
	    photoAt.set(location, new Photo(image));
	    p.setStatus(Photo.Status.Old);

	    if (deletedPhotos.contains(image))
		deletedPhotos.remove(image);
	}
    }

    public void RemovePhoto(int location) {

	assert location >= 0;
	assert location < photoAt.size();

	Photo p = photoAt.remove(location);
	if (p.getStatus() == Photo.Status.Old)
	    deletedPhotos.add(p.getImage());
    }

    public void Save() {
	for (Photo p : photoAt) {
	    if (p.getStatus() == Photo.Status.New) {
		p.setStatus(Photo.Status.Old);
		System.out.println("add:" + p.getImage());
	    }
	}
	for (String pid  : deletedPhotos) {
	    System.out.println("delete:" + pid);
	}
	deletedPhotos.clear();
    }

    public void doAddPhoto(Scanner in) {
	System.out.println("Enter image:");
	String image = in.nextLine();
	try {
	    AddPhoto(image);
	}
	catch(Exception e) {
	    System.out.println("error: " + e.toString());
	}
    }

    public void doReplacePhoto(Scanner in) {
	System.out.println("Enter image:");
	String image = in.nextLine();
	System.out.println("Enter location:");
	int location = in.nextInt();
	try {
	    ReplacePhoto(image, location);
	}
	catch(Exception e) {
	    System.out.println("error: " + e.toString());
	}
    }

    public void doRemovePhoto(Scanner in) {
	System.out.println("Enter location:");
	int location = in.nextInt();
	try {
	    RemovePhoto(location);
	}
	catch(Exception e) {
	    System.out.println("error: " + e.toString());
	}
    }

    public void doSave(Scanner in) {
	Save();
    }

    public void displayState() {
	System.out.println(photoAt.toString());
    }

    public static void main(String[] args) {

	PhotoAlbum album = new PhotoAlbum();

	int choice = 0;
	Scanner in = new Scanner (System.in);

	while (choice != 5) {

	    System.out.print("Select action:\n" 
			     + "1. Add a photo\n"
			     + "2. Replace a photo\n"
			     + "3. Remove a photo\n"
			     + "4. Save the album\n"
			     + "5. Exit\n");
	    
	    choice = in.nextInt();
	    in.nextLine(); // consume the newline

	    while (choice <1 || choice > 5) {
		System.out.println("Please choose an option between 1-5");
		choice = in.nextInt();
		in.nextLine();
	    }

	    switch(choice) {
	    case 1:
		album.doAddPhoto(in);
		album.displayState();
		break;
	    case 2:
		album.doReplacePhoto(in);
		album.displayState();
		break;
	    case 3:
		album.doRemovePhoto(in);
		album.displayState();
		break;
	    case 4:
		album.doSave(in);
		album.displayState();
		break;
	    case 5:
		break;
	    default:
		assert false;
	    }
	}
    }
}

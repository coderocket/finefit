package albumsimplecore.a_removeowner.it.unito.Album;

/*** added by DBase
 */
public class Photo {
	private String image;
	public Photo(String theImage) {
		if(theImage == null) throw new IllegalArgumentException("NullImage");
		image = theImage;
	}
	public String getImage() {
		return image;
	}
	public String toString() {
		return image;
	}
}
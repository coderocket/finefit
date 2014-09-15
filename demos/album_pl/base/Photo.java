
class Photo {
	String image; // represents the bitmap image
	public Photo(String theImage) {
		if (theImage == null)
				throw new IllegalArgumentException("NullImage");
		image = theImage;
	}
	public String getImage() { return image; }
	public String toString() { return image; }
}


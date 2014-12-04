package albumsimplecore.a_owner_remove_groups_finefit.it.unito.Album;


/*** added by DBase* modified by DGroups
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
	/*** added by DGroups
	 */
	private Group group;
	/*** added by DGroups
	 */
	Group getGroup() {
		return group;
	}
	/*** added by DGroups
	 */
	void setGroup(Group group) {
		this.group = group;
	}
}

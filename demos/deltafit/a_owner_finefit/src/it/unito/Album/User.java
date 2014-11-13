package albumsimplecore.a_owner_finefit.it.unito.Album;

/*** added by DOwner
 */
class User {
	private String name;
	private String password;
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String p) {
		password = p;
	}
}

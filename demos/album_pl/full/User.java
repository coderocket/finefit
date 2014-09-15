
class User {

	String name;
	String password;
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() { return name; }
	public String getPassword() { return password; }
	// MISTAKE: was String setPassword...
	public void setPassword(String p) { password = p; }
}

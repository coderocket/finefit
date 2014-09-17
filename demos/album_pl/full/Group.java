
import java.util.Set;
import java.util.HashSet;

class Group {
	String name;
	Set<User> members;

	public Group(String name, User owner) {
		this.name = name;
		this.members = new HashSet<User>();
		this.members.add(owner);
	}

	public Group(String name, Set<User> members) {
		this.name = name;
		this.members = members;
	}

	public String getName() { return name;}
	public Set<User> getMembers() { return members; }
	void setMembers(Set<User> members) { this.members = members; }
}

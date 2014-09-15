
import java.util.Set;

class Group {
	String name;
	Set<User> members;

	public Group(String name, Set<User> members) {
		this.name = name;
		this.members = members;
	}

	public Set<User> getMembers() { return members; }
	void setMembers(Set<User> members) { this.members = members; }
}

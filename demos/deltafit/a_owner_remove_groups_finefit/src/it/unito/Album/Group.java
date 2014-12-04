package albumsimplecore.a_owner_remove_groups_finefit.it.unito.Album;

import java.util.Set;
import java.util.HashSet;
/*** added by DGroups
 */
class Group {
	private String name;
	private Set<User> members;
	Group(String name, User owner) {
		this.name = name;
		this.members = new HashSet<User>();
		this.members.add(owner);
	}
	Group(String name, Set<User> members) {
		this.name = name;
		this.members = members;
	}
	String getName() {
		return name;
	}
	Set<User> getMembers() {
		return members;
	}
	void setMembers(Set<User> members) {
		this.members = members;
	}
}

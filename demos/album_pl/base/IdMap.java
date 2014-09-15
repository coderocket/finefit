import java.util.Map;
import java.util.HashMap;

class IdMap {
	Map<Object, String> obj2atom;
	Map<String, Object> atom2obj;

	static IdMap theMap;
	
	public IdMap() {
		obj2atom = new HashMap<Object, String>();
		atom2obj = new HashMap<String, Object>();
	}

	public static IdMap instance() { 
		if (theMap == null) 
			theMap = new IdMap();
		return theMap; }

	public String obj2atom(Object obj)  {
		return obj2atom.get(obj); }

	public Object atom2obj(String id) {
		return atom2obj.get(id); }

	public void associate(Object obj, String id) {
		obj2atom.put(obj, id);
		atom2obj.put(id, obj);
	}
}

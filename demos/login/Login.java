
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;
import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;
import com.finefit.testcasegenerator.StateVariables;

public class Login {

	Set<String> registered;
	Set<String> loggedIn;
	
	public void init() {
		registered = new HashSet<String>();
		loggedIn = new HashSet<String>();
	}

	public void login(String uname) {
		loggedIn.add(uname);
	}

	public void logout(String uname) {
		loggedIn.remove(uname);
	}

	public void register(String uname) {
		registered.add(uname);
	}

	public void unregister(String uname) {
		registered.remove(uname);
	}

	public Instance retrieve(Universe universe, StateVariables stateVars) {
		TupleFactory factory = universe.factory();
		Instance instance = new Instance(universe);
	
		List<Tuple> loggedinTuples = new ArrayList();
		List<Tuple> registeredTuples = new ArrayList();

		for (String s : loggedIn) {
			loggedinTuples.add(factory.tuple("State$0", s));
		}
		for (String s : registered) {
			registeredTuples.add(factory.tuple("State$0", s));
		}
		
		if(!loggedinTuples.isEmpty()){
			instance.add(stateVars.get("loggedin"), factory.setOf(loggedinTuples));
		}else {
			instance.add(stateVars.get("loggedin"), factory.noneOf(2));
		}
		if(!registeredTuples.isEmpty()){
			instance.add(stateVars.get("registered"), factory.setOf(registeredTuples)); 
		}else{
			instance.add(stateVars.get("registered"), factory.noneOf(2));
		}
		
		return instance;
	}
}

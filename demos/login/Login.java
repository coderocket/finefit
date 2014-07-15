
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

public class Login {

	public void init() {}

	public void login(String uname) {}

	public void logout(String uname) {}

	public void register(String uname) {}

	public void unregister(String uname) {}

  public Instance retrieve(Universe universe, Instance args) {
  	TupleFactory factory = universe.factory();
  	Instance instance = new Instance(universe);
	
	List<Tuple> loggedinTuples = new ArrayList();
	List<Tuple> registeredTuples = new ArrayList();

/*	
	registeredTuples.add(factory.tuple("State$0",user.getUserName()));
	loggedinTuples.add(factory.tuple("State$0",user.getUserName()));
	*/
	
		if(!loggedinTuples.isEmpty()){
			instance.add(getRelationByName(args,"loggedin"), factory.setOf(loggedinTuples));
		}else {
			instance.add(getRelationByName(args, "loggedin"), factory.noneOf(2));
		}
		if(!registeredTuples.isEmpty()){
			instance.add(getRelationByName(args,"registered"), factory.setOf(registeredTuples)); 
		}else{
			instance.add(getRelationByName(args, "registered"), factory.noneOf(2));
		}
		
		return instance;
	}
  
  public Relation getRelationByName(Instance instance, String relationName){
		
		Map<Relation,TupleSet>relationTuples = instance.relationTuples();
		Set<Entry<Relation, TupleSet>> set = relationTuples.entrySet();
		
		Iterator<Entry<Relation, TupleSet>> iterSetOfTuples = set.iterator();
		
		while(iterSetOfTuples.hasNext()){
			Entry<Relation, TupleSet> relationEntry = iterSetOfTuples.next();
			Relation relation = relationEntry.getKey();
			if(relation.name().contains(relationName)){
				return relation;
			}
		}
		return null;
	}

}

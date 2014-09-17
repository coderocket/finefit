/* Copyright 2014 David Faitelson

This file is part of FineFit.

FineFit is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

FineFit is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with FineFit. If not, see <http://www.gnu.org/licenses/>.

*/

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import kodkod.instance.TupleFactory;
import kodkod.instance.Tuple;

import com.finefit.model.SUT;
import com.finefit.model.Operation;
import com.finefit.model.TestCase;
import com.finefit.model.State;
import com.finefit.model.SutState;

public class FineFitDriver implements SUT {

		private ArrayPhotoAlbum sut;
		
    public FineFitDriver() {
			sut = null;
    }

    @Override
    public State initialize(State state) {
			String owner_name = state.getArg("OWNER_NAME");
			String owner_passwd = state.getArg("OWNER_PASSWD");
			String owner_id = state.getArg("OWNER");
			String owner_group_id = state.getArg("OWNER_GROUP");
			String owner_group_name = state.getArg("OWNER_GROUP_NAME");
			User owner = new User(owner_name, owner_passwd);
			Group owner_group = new Group(owner_group_name, owner);
			IdMap.instance().associate(owner, owner_id);
			IdMap.instance().associate(owner_group, owner_group_id);
			sut = new ArrayPhotoAlbum(5, owner, owner_group);

			SutState sutstate = sut.retrieve();
			State newstate = state.clone();
			newstate.read(sutstate);

      return newstate;
    }

    @Override
    public State applyOperation(TestCase testCase) throws InvalidNumberOfArguments, NoSuchOperation {

			String operationName = testCase.getOperationName(); 
			State state = testCase.getState().clone();

			TupleFactory factory = state.factory();

			if (operationName.equals("login")) {

				String name = state.getArg("n");
				String pass = state.getArg("p");

				String report = "OK$0";

				try {
					sut.login(name,pass);
				}
        catch(PhotoAlbum.AlreadyLogged err) { report = "ALREADY_IN$0"; }
        catch(PhotoAlbum.AuthFailed err) { report = "AUTH_FAILED$0"; }

				state.addOutput("report!", report);
			} 
			else if (operationName.equals("addPhoto")) {
				String id = state.getArg("p");

				String report = "OK$0";

				try {
					Photo p = sut.addPhoto(id);
					IdMap.instance().associate(p, id);
				}
        catch(PhotoAlbum.PhotoExists err) { report = "PHOTO_EXISTS$0"; }
        catch(PhotoAlbum.AlbumIsFull err) { report = "ALBUM_FULL$0"; }
        catch(PhotoAlbum.OwnerNotLoggedIn err) { report = "NOT_AUTH$0"; }


				state.addOutput("report!", report);
			} 
			else if (operationName.equals("updateGroup")) {

				String n = state.getArg("n");

				String nuser = state.getArg("nu");

				String new_group_id = state.getArg("g");
					
				String report = "OK$0";

				try {
					Set<String> nusers = new HashSet<String>();
					nusers.add(nuser);
					Group group = sut.updateGroup(n, nusers);

					if (group != null) // a new group was created
						IdMap.instance().associate(group, new_group_id);
				}
        catch(PhotoAlbum.NotAuthorized err) { report = "NOT_AUTH$0"; }
        catch(PhotoAlbum.MissingUsers err) { report = "MISSING_USERS$0"; }

				state.addOutput("report!", report);
			}
			else if (operationName.equals("updateUser")) {
				String n = state.getArg("n");
				String p = state.getArg("p");
				String new_user_id = state.getArg("u");
					
				String report = "OK$0";

				try {
					User user = sut.updateUser(n,p);

					if (user != null) // a new user was created
						IdMap.instance().associate(user, new_user_id);
				}
        catch(PhotoAlbum.NotAuthorized err) { report = "NOT_AUTH$0"; }

				state.addOutput("report!", report);
			}
			else if (operationName.equals("removePhoto")) {
				int i = Integer.parseInt(state.getArg("i"));
					
				String report = "OK$0";

				try {
					sut.removePhoto(i);
				}
				catch(IllegalArgumentException err) { report = "NO_PHOTO$0"; }
        catch(PhotoAlbum.OwnerNotLoggedIn err) { report = "NOT_AUTH$0"; }


				state.addOutput("report!", report);
			}
			else if (operationName.equals("removeGroup")) {
				String name = state.getArg("n");
					
				String report = "OK$0";

				try {
					sut.removeGroup(name);
				}
        catch(PhotoAlbum.NotAuthorized err) { report = "NOT_AUTH$0"; }
        catch(PhotoAlbum.MissingGroup err) { report = "NO_GROUP$0"; }
        catch(PhotoAlbum.RemoveOwnerGroup err) { report = "REM_OWNER_GROUP$0"; }

				state.addOutput("report!", report);
			}
			else if (operationName.equals("updatePhotoGroup")) {
				int i = Integer.parseInt(state.getArg("i"));
				String name = state.getArg("n");
					
				String report = "OK$0";

				try {
					sut.updatePhotoGroup(i, name);
				}
        catch(PhotoAlbum.OwnerNotLoggedIn err) { report = "NOT_AUTH$0"; }
        catch(PhotoAlbum.MissingGroup err) { report = "NO_GROUP$0"; }
				catch(IllegalArgumentException err) { report = "NO_PHOTO$0"; }

				state.addOutput("report!", report);
			}
			else if (operationName.equals("viewPhotos")) {

				String report = "OK$0";
				List<Tuple> result = new ArrayList<Tuple>(); 

				try {
					Set<Photo> photos = sut.viewPhotos(); 
					for(Photo p : photos){
						result.add(factory.tuple(IdMap.instance().obj2atom(p)));
					}
				}
				catch(PhotoAlbum.NotAuthorized err) { report = "NOT_AUTH$0"; }

				state.addOutput("result!", 1, result);
				state.addOutput("report!", report);
			}
			else 
				throw new NoSuchOperation(operationName);

			SutState sutstate = sut.retrieve();
			State newstate = state.clone();
			newstate.read(sutstate);
			return newstate;
    }

}


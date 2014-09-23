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

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import com.finefit.sut.SUT;
import com.finefit.sut.NoSuchOperation;
import com.finefit.sut.InvalidNumberOfArguments;
import com.finefit.sut.Operation;
import com.finefit.sut.State;
import com.finefit.sut.IdMap;
import com.finefit.model.TestCase;

public class FineFitDriver implements SUT {

		private static Map<String, Operation<PhotoAlbum> > ops = new HashMap<String, Operation<PhotoAlbum> >();

		static {
    	setup_operation_table(); 
		}

    static void setup_operation_table() {

			ops.put("login", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {
					sut.login(args.getArg("n"), args.getArg("p")); } });

			ops.put("addPhoto", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {
					String id = args.getArg("p");
					Photo p = sut.addPhoto(id); 
					IdMap.instance().associate(p, id);
				} });

			ops.put("updateGroup", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {
					String n = args.getArg("n");
					String nuser = args.getArg("nu");
					String new_group_id = args.getArg("g");
					
					Set<String> nusers = new HashSet<String>();
					nusers.add(nuser);
					Group group = sut.updateGroup(n, nusers);

					if (group != null) // a new group was created
						IdMap.instance().associate(group, new_group_id);
				
				} });

			ops.put("updateUser", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {
					String n = args.getArg("n");
					String p = args.getArg("p");
					String new_user_id = args.getArg("u");
					
					User user = sut.updateUser(n,p);

					if (user != null) // a new user was created
						IdMap.instance().associate(user, new_user_id);

				 } });

			ops.put("removePhoto", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {
					int i = Integer.parseInt(args.getArg("i"));
					sut.removePhoto(i);
				} });

			ops.put("removeGroup", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {
					sut.removeGroup(args.getArg("n")); 
				} });

			ops.put("updatePhotoGroup", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {
					int i = Integer.parseInt(args.getArg("i"));
					String name = args.getArg("n");
					
					sut.updatePhotoGroup(i, name);
				} });

			ops.put("viewPhotos", new Operation<PhotoAlbum>() { 
				public void apply(PhotoAlbum sut, com.finefit.model.State args, State outputs) throws Exception {

					outputs.add_output("result!", 1);

					Set<Photo> photos = sut.viewPhotos(); 
					for(Photo p : photos){
						outputs.get_output("result!").add(IdMap.instance().obj2atom(p));
					}
				 } });
		}

		private ArrayPhotoAlbum sut;
		
    public FineFitDriver() {
			sut = null;
    }

    @Override
    public State initialize(com.finefit.model.State args) {

			String owner_name = args.getArg("OWNER_NAME");
			String owner_passwd = args.getArg("OWNER_PASSWD");
			String owner_id = args.getArg("OWNER");
			String owner_group_id = args.getArg("OWNER_GROUP");
			String owner_group_name = args.getArg("OWNER_GROUP_NAME");

			User owner = new User(owner_name, owner_passwd);
			Group owner_group = new Group(owner_group_name, owner);
			IdMap.instance().associate(owner, owner_id);
			IdMap.instance().associate(owner_group, owner_group_id);

			sut = new ArrayPhotoAlbum(5, owner, owner_group);

			return sut.retrieve();
    }

    @Override
    public State applyOperation(TestCase testCase) throws Exception {

			String operationName = testCase.getOperationName(); 
			com.finefit.model.State args = testCase.getState(); 

			Operation<PhotoAlbum> op = ops.get(operationName);
			if (op == null)
				throw new NoSuchOperation(operationName);

			String report = "OK$0";
			State outputs = new State();

			try {
				op.apply(sut, args, outputs);
			}
      catch(PhotoAlbum.AlreadyLogged err) { report = "ALREADY_IN$0"; }
      catch(PhotoAlbum.AuthFailed err) { report = "AUTH_FAILED$0"; }
      catch(PhotoAlbum.PhotoExists err) { report = "PHOTO_EXISTS$0"; }
      catch(PhotoAlbum.AlbumIsFull err) { report = "ALBUM_FULL$0"; }
      catch(PhotoAlbum.OwnerNotLoggedIn err) { report = "NOT_AUTH$0"; }
			catch(IllegalArgumentException err) { report = "NO_PHOTO$0"; }
      catch(PhotoAlbum.MissingGroup err) { report = "NO_GROUP$0"; }
      catch(PhotoAlbum.RemoveOwnerGroup err) { report = "REM_OWNER_GROUP$0"; }
      catch(PhotoAlbum.NotAuthorized err) { report = "NOT_AUTH$0"; }
      catch(PhotoAlbum.MissingUsers err) { report = "MISSING_USERS$0"; }

			outputs.add_output("report!", 1).add(report);
			State state = sut.retrieve();
			state.add(outputs);

			return state;
		}
}


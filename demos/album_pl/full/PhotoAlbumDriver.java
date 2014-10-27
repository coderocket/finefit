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
import com.finefit.sut.*;
import com.finefit.sut.FineFitDriver;

public class PhotoAlbumDriver extends FineFitDriver {

		protected void setup_exception_table() {

      exceptions.put("PhotoAlbum$AlreadyLogged", "ALREADY_IN$0");
      exceptions.put("PhotoAlbum$AuthFailed", "AUTH_FAILED$0");
      exceptions.put("PhotoAlbum$PhotoExists", "PHOTO_EXISTS$0");
      exceptions.put("PhotoAlbum$AlbumIsFull", "ALBUM_FULL$0");
      exceptions.put("PhotoAlbum$OwnerNotLoggedIn", "NOT_AUTH$0");
			exceptions.put("java.lang.IllegalArgumentException", "NO_PHOTO$0");
      exceptions.put("PhotoAlbum$MissingGroup", "NO_GROUP$0");
      exceptions.put("PhotoAlbum$RemoveOwnerGroup", "REM_OWNER_GROUP$0");
      exceptions.put("PhotoAlbum$NotAuthorized", "NOT_AUTH$0");
      exceptions.put("PhotoAlbum$MissingUsers", "MISSING_USERS$0");
		}

    protected void setup_operation_table() {

			ops.put("login", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					s.login(args.getArg("n"), args.getArg("p")); } });

			ops.put("addPhoto", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					String id = args.getArg("p");
					Photo p = sut.addPhoto(id); 
					IdMap.instance().associate(p, id);
				} });

			ops.put("updateGroup", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					String n = args.getArg("n");
					String nuser = args.getArg("nu");
					String new_group_id = args.getArg("g");
					
					Set<String> nusers = new HashSet<String>();
					nusers.add(nuser);
					Group group = sut.updateGroup(n, nusers);

					if (group != null) // a new group was created
						IdMap.instance().associate(group, new_group_id);
				
				} });

			ops.put("updateUser", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					String n = args.getArg("n");
					String p = args.getArg("p");
					String new_user_id = args.getArg("u");
					
					User user = sut.updateUser(n,p);

					if (user != null) // a new user was created
						IdMap.instance().associate(user, new_user_id);

				 } });

			ops.put("removePhoto", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					int i = Integer.parseInt(args.getArg("i"));
					sut.removePhoto(i);
				} });

			ops.put("removeGroup", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					sut.removeGroup(args.getArg("n")); 
				} });

			ops.put("updatePhotoGroup", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {
					int i = Integer.parseInt(args.getArg("i"));
					String name = args.getArg("n");
					
					sut.updatePhotoGroup(i, name);
				} });

			ops.put("viewPhotos", new Operation() { 
				PhotoAlbum s = sut;
				public void apply(com.finefit.model.State args, State outputs) throws Exception {

					outputs.add_output("result!", 1);

					Set<Photo> photos = sut.viewPhotos(); 
					for(Photo p : photos){
						outputs.get_output("result!").add(IdMap.instance().obj2atom(p));
					}
				 } });

		}

		private ArrayPhotoAlbum sut;

		@Override protected void setup_sut() {
			sut = new ArrayPhotoAlbum(5);
		}

		@Override public State retrieve() { return sut.retrieve(); }

		@Override public void init_sut(com.finefit.model.State args) {

			String owner_name = args.getArg("OWNER_NAME");
			String owner_passwd = args.getArg("OWNER_PASSWD");
			User owner = new User(owner_name, owner_passwd);
			String owner_id = args.getArg("OWNER");
			IdMap.instance().associate(owner, owner_id);

			sut.setOwner(owner);

			String owner_group_id = args.getArg("OWNER_GROUP");
			String owner_group_name = args.getArg("OWNER_GROUP_NAME");
			Group owner_group = new Group(owner_group_name, owner);
			IdMap.instance().associate(owner_group, owner_group_id);

			sut.setOwnerGroup(owner_group);

		}
}


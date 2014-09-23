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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.finefit.sut.State;

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

	public State retrieve() {

		State currentState = new State();
	
		currentState.add_state("loggedin", 1);
		currentState.add_state("registered", 1);

		for (String s : loggedIn) {
			currentState.get_state("loggedin").add(s);
		}

		for (String s : registered) {
			currentState.get_state("registered").add(s);
		}
		
		return currentState;
	}
}

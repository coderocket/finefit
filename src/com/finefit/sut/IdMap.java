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

package com.finefit.sut;

import java.util.Map;
import java.util.HashMap;

public class IdMap {
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

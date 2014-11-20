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


public class Photo {

    public enum Status {Old, New};

    Status status;
    String image; // represents the bitmap image 

    public Photo(String image) { status = Status.New; this.image = image; }

    public Status getStatus() { return status; }
    public void setStatus(Status s) {  status = s; }
    public String getImage() { return image; }

    public String toString() { return image; }
}
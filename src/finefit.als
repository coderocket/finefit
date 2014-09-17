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

module finefit

pred true {	all x : none | no x }

pred false {	some x : none | no x }

fun ran[r : univ -> univ] : univ { univ.r }

fun dom[r : univ -> univ] : univ {	r.univ }

fun index[f:Int -> univ, i:Int] : Int
{
	#{j : f.univ | j < i}
}

fun ith[f:Int -> univ, i : Int] : Int
{
	{ j : f.univ | index[f,j] = i }
}

fun squash[f : Int -> univ] : seq univ
{
	{ i : Int, x : univ | 0<= i and i < #f and (ith[f,i] -> x) in f }
}



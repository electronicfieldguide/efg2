/**
 * Adapted from 
    Michael Synovic
    on: 01/12/2003
    
    This is a Javascript implementation of the Java Hashtable object.
    Copyright (C) 2003  Michael Synovic

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA	

    Contructor(s):
     Set()
              Creates a new, empty Set
    
    Method(s):
     void clear() 
              Clears this Set so that it contains no keys. 
     boolean contains(String key) 
              Tests if the specified element is  in this set. 
     boolean isEmpty() 
              Tests if this set has no values. 
     void add(String object) 
			add this element to the set
      void remove(String object) 
              Removes the element from Set
     int size() 
              Returns the number of elements in this Set. 
*/
function Set(){
    this.set = new Array();
}

/* privileged functions */
    
Set.prototype.clear = function(){
      this.set = new Array();
}              
Set.prototype.contains = function(element){
    var exists = false;
    for (var i in this.set) {
        if (i == element && this.set[i] != null) {
            exists = true;
            break;
        }       
    }
    return exists;
}
Set.prototype.isEmpty = function(){
    return (parseInt(this.size()) == 0) ? true : false;
}
Set.prototype.add = function(element){
    if (element == null) {
        return;
        }else{
        	if(!this.contains(element)){
            	this.set[element] = element;
        	}
     }
 }
Set.prototype.remove = function(element){
      if(this.contains(element)){
  		this.set[element] = null;
      }
}
Set.prototype.size = function(){
    var size = 0;
    for (var i in this.set) {
        if (this.set[i] != null)
            size ++;
    }
    return size;
}    
             
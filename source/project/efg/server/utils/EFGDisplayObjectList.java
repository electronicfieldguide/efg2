/**
 * 
 */
package project.efg.server.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import project.efg.util.utils.EFGDisplayObject;

/**
 * @author kasiedu
 *
 */
public class EFGDisplayObjectList {
	
	private SortedSet set;
	public EFGDisplayObjectList(){
		this.set = Collections.synchronizedSortedSet(new TreeSet());
	}
	public boolean add(EFGDisplayObject obj){
		return this.set.add(obj);
	}
	public Iterator getIterator(){
		return set.iterator();
	}	
	public int getCount(){
		return this.set.size();
	}
}

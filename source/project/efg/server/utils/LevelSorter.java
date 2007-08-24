/**
 * 
 */
package project.efg.server.utils;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author jacob.asiedu
 *
 */
public class LevelSorter {
	private SortedMap sortedMap;
	public LevelSorter(){
		this.sortedMap = new TreeMap();
	}
	public void add2Map(String key, String value){
		if(key == null || value == null){
			return;
		}
		SortedSet set = null;
		if(this.sortedMap.containsKey(key)){
			set = (SortedSet)this.sortedMap.get(key);
		}
		else{
			set = new TreeSet();
		}
		set.add(value);
		this.sortedMap.put(key,set);
	}
	public int getSize(){
		return this.sortedMap.size();
	}
}

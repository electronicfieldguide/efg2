/**
 * 
 */
package project.efg.util;

import java.util.Comparator;
import java.util.Iterator;

/**
 * @author kasiedu
 *
 */
public interface EFGDocumentSorter {
	
	/**
	 * 
	 * @param sorter - Sort the given parameter into the sortedSet
	 * @see sortedSet
	 */
	public abstract void sort(Comparator compare ,Object sorter);
/**
 * 
 * @return a iterator for the sortedSet
 * @see sortedSet
  */
	public abstract Iterator getIterator();

}
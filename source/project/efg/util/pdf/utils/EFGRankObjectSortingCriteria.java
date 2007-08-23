/**
 * 
 */
package project.efg.util.pdf.utils;

import java.util.Comparator;

import project.efg.util.pdf.interfaces.EFGRankObject;

/**
 * @author jacob.asiedu
 *
 */
public class EFGRankObjectSortingCriteria implements Comparator{
	
	public EFGRankObjectSortingCriteria(){
		
	}
	/**
	 * 
	 */
	public int compare(Object o1, Object o2) {
		if(o1 != null & o2 != null){
			EFGRankObject co1 = (EFGRankObject)o1;
			EFGRankObject co2 = (EFGRankObject)o2;
			
			return  (co1.getRank() - co2.getRank());
		}
		return -1;

	}
}

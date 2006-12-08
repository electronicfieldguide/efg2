/**
 * 
 */
package project.efg.servlets.efgInterface;

import project.efg.efgDocument.StatisticalMeasuresType;

/**
 * @author kasiedu
 *
 */
public abstract class HandleStatInputAbstract {

	protected StatisticalMesureComparatorInterface measureComparator;

	/**
	 * 
	 */
	public HandleStatInputAbstract(StatisticalMesureComparatorInterface measureComparator) {
		this.measureComparator = measureComparator;
	}

	public abstract boolean isInRange(StatisticalMeasuresType dbStats, String userValues);
}
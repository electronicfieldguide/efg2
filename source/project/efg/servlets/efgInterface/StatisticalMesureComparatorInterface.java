/**
 * 
 */
package project.efg.servlets.efgInterface;

import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.servlets.efgInterface.OperatorInterface;

/**
 * @author kasiedu
 *
 */
public interface StatisticalMesureComparatorInterface {

	/**
	 * Is databaseValue operator userValue?
	 * 
	 * @param operator - The operator 
	 * @param userValue
	 * @param databaseValue
	 * @return true
	 * @throws Exception if userValue=null or databaseValue=null
	 */
	//userValue,databaseValue
	public abstract boolean isInRange(OperatorInterface operator,
			StatisticalMeasureType userValue,
			StatisticalMeasureType databaseValue) throws Exception;

	/**
	 * is databaseValue.getMax() >= userValue.getMax()?
	 * @param databaseValue
	 * @param userValue
	 * @return true databaseValue.getMax() >= userValue.getMax();
	 * @throws Exception if databaseValue or userValue is null
	 */
	public abstract boolean checkGreaterThanOrEquals(
			StatisticalMeasureType databaseValue,
			StatisticalMeasureType userValue) throws Exception;

	/**
	 * is databaseValue.getMax() > userValue.getMax()?
	 * @param databaseValue
	 * @param userValue
	 * @return true databaseValue.getMax() > userValue.getMax();
	 * @throws Exception if databaseValue or userValue is null
	 */
	public abstract boolean checkGreaterThan(
			StatisticalMeasureType databaseValue,
			StatisticalMeasureType userValue) throws Exception;

	/**
	 * is databaseValue.getMin() < userValue.getMin()?
	 * @param databaseValue
	 * @param userValue
	 * @return true databaseValue.getMin() < userValue.getMin();
	 * @throws Exception if databaseValue or userValue is null
	 */
	public abstract boolean checkLessThan(StatisticalMeasureType databaseValue,
			StatisticalMeasureType userValue) throws Exception;
	public abstract boolean checkLessThanOrEquals(StatisticalMeasureType databaseValue, 
			StatisticalMeasureType userValue) throws Exception;
	/**
	 * is databaseValue.equals(userValue)?
	 * 
	 * @param databaseValue
	 * @param userValue
	 * @return true if databaseValue.getMin() == userValue.getMin() &&
	 * databaseValue.getMax() == userValue.getMax() && 
	 
	 * @throws  databaseValue == null or 
	 * userValue == null
	 */
	public abstract boolean checkEquals(StatisticalMeasureType databaseValue,
			StatisticalMeasureType userValue) throws Exception;

}
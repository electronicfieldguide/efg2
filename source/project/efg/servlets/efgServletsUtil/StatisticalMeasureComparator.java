/**
 * $Id$
 * $Name$
 *
 * Copyright (c) 2006  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */
package project.efg.servlets.efgServletsUtil;



import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.servlets.efgInterface.OperatorInterface;

/**
 * @author kasiedu
 *
 */
public class StatisticalMeasureComparator {
	
	public StatisticalMeasureComparator(){
		
	}
	/**
	 * Is object1 operator object2?
	 * 
	 * @param operator - The operator associated with object1 
	 * @param object1
	 * @param object2
	 * @return true
	 * if object2.getMin() == object2.getMax()
	 * return checkEquals(object1,object2)
	 * else
	 *  return object1.getMax() <= object2.getMax() &&
	 * object1.getMin() >= object1.getMin();
	 * @throws Exception if object1=null or object2=null
	 * or object1.getUnits() == null or object2.getUnits== null 
	 */
	public boolean isInRange(
			OperatorInterface operator,
			StatisticalMeasureType object1, 
			StatisticalMeasureType object2
			) throws Exception{
		//log.debug("Min val of object1: " + object1.getMin());
		//log.debug("Max val of object1: " + object1.getMax());
		//log.debug("Min val of object2: " + object2.getMin());
		//log.debug("Max val of object2: " + object2.getMax());
		
		//log.debug("is object1 in the range of object2?");
		//log.debug("Operator is: " + operator.toString());
		if (operator instanceof GreaterThanOperator) {
			return this.checkGreaterThan(object2,object1);
		}
		if (operator instanceof GreaterThanOrEqualsOperator) {
			return this.checkGreaterThanOrEquals(object2,object1);
		}
		if (operator instanceof LessThanOperator) {
			return this.checkLessThan(object2,object1);
		}
		if (operator instanceof LessThanOrEqualsOperator) {
			return this.checkLessThanOrEquals(object2,object1);
		}
		if(object2.getMax() == object2.getMin()){
			/*log.debug(
					"Since object2 has both min and max " + 
					"equal we will check for direct equality "+ 
					"between object1 and object2!!"
					);*/
			return this.checkEquals(object1,object2);
			
		}
		/*log.debug("Since object2 has both min and max not equal " +
				"we will check for indirect range");*/
		if(checkGreaterThan(object1,object2)){//means object1.getMax() > object2.getMax()
			//so not in range
			return false;
			
		}
		if(checkLessThan(object1,object2)){//means object1.getMin() < object2.getMin()
		 return false;	
		}
		return true;
	}
	/**
	 * is object1.getMax() >= object2.getMax()?
	 * @param object1
	 * @param object2
	 * @return true object1.getMax() >= object2.getMax();
	 * @throws Exception if object1 or object2 is null
	 */
	public boolean checkGreaterThanOrEquals(StatisticalMeasureType object1, 
			StatisticalMeasureType object2) throws Exception {
		return (this.checkGreaterThan(object1,object2) || 
				this.checkEquals(object1,object2));
	
	}
	/**
	 * is object1.getMax() > object2.getMax()?
	 * @param object1
	 * @param object2
	 * @return true object1.getMax() > object2.getMax();
	 * @throws Exception if object1 or object2 is null
	 */
	public boolean checkGreaterThan(StatisticalMeasureType object1, 
			StatisticalMeasureType object2) throws Exception {
		return object1.getMax() > object2.getMax();
	}
	/**
	 * is object1.getMin() < object2.getMin()?
	 * @param object1
	 * @param object2
	 * @return true object1.getMin() < object2.getMin();
	 * @throws Exception if object1 or object2 is null
	 */
	public boolean checkLessThan(StatisticalMeasureType object1, 
			StatisticalMeasureType object2) throws Exception {
		return object1.getMin() < object2.getMin();
	}
	
	/**
	 * is object1.getMin() <= object2.getMin()?
	 * @param object1
	 * @param object2
	 * @return true object1.getMin() <= object2.getMin();
	 * @throws Exception if object1.getUnits() == null,
	 *  object1 == null or 
	 * object2 == null
	 */
	public boolean checkLessThanOrEquals(StatisticalMeasureType object1, 
			StatisticalMeasureType object2) throws Exception {
		return (this.checkLessThan(object1,object2) ||
				this.checkEquals(object1,object2));
	}
	/**
	 * is object1.equals(object2)?
	 * 
	 * @param object1
	 * @param object2
	 * @return true if object1.getMin() == object2.getMin() &&
	 * object1.getMax() == object2.getMax() && 
	 * object1.getUnits().equalsIgnoreCase(object2.getUnits())
	 * @throws Exception if object1.getUnits() == null, object1 == null or 
	 * object2 == null
	 */
	public boolean checkEquals(StatisticalMeasureType object1, 
			StatisticalMeasureType object2)throws Exception{
		if(object1.getUnits() == null){
			throw new Exception("object1 has null units");
		}
		if(object2.getUnits() == null){
			object2.setUnits("");
		}
		return (object1.getMin() == object2.getMin() &&
		 object1.getMax() == object2.getMax() && 
		object1.getUnits().equalsIgnoreCase(object2.getUnits()));
	}
	
	
	

}

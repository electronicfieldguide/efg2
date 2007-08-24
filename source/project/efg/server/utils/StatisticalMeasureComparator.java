/**
 * $Id: StatisticalMeasureComparator.java,v 1.1.1.1 2007/08/01 19:11:26 kasiedu Exp $
 * $Name:  $
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
package project.efg.server.utils;



import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.server.interfaces.StatisticalMesureComparatorInterface;
import project.efg.util.interfaces.OperatorInterface;
import project.efg.util.utils.GreaterThanOperator;
import project.efg.util.utils.GreaterThanOrEqualsOperator;
import project.efg.util.utils.LessThanOperator;
import project.efg.util.utils.LessThanOrEqualsOperator;

/**
 * @author kasiedu
 *
 */
public class StatisticalMeasureComparator implements StatisticalMesureComparatorInterface {
	
	public StatisticalMeasureComparator(){
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.StatisticalMesureComparatorInterface#isInRange(project.efg.servlets.efgInterface.OperatorInterface, project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)
	 */
	//userValue,databaseValue
	public boolean isInRange(
			OperatorInterface operator,
			StatisticalMeasureType userValue, 
			StatisticalMeasureType databaseValue
			) throws Exception{
		
		if (operator instanceof GreaterThanOperator) {
			return this.checkGreaterThan(databaseValue,userValue);
		}
		if (operator instanceof GreaterThanOrEqualsOperator) {
			return this.checkGreaterThanOrEquals(databaseValue,userValue);
		}
		if (operator instanceof LessThanOperator) {
			return this.checkLessThan(databaseValue,userValue);
		}
		if (operator instanceof LessThanOrEqualsOperator) {
			return this.checkLessThanOrEquals(databaseValue,userValue);
		}
		//default make sure it is in range
		return this.checkGreaterThanOrEquals(databaseValue,userValue) &&
		this.checkLessThanOrEquals(databaseValue,userValue);
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.StatisticalMesureComparatorInterface#checkGreaterThanOrEquals(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)
	 */
	public boolean checkGreaterThanOrEquals(StatisticalMeasureType databaseValue, 
			StatisticalMeasureType userValue) throws Exception {
	
		
		StatisticalMeasureType maxdatabaseValue = new StatisticalMeasureType();
		maxdatabaseValue.setMax(databaseValue.getMax());
		maxdatabaseValue.setMin(maxdatabaseValue.getMax());
		
		
		
		return (this.checkGreaterThan(databaseValue,userValue) ||
				this.checkEquals(maxdatabaseValue,userValue)
		);
		
	
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.StatisticalMesureComparatorInterface#checkGreaterThan(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)
	 */
	public boolean checkGreaterThan(StatisticalMeasureType databaseValue, 
			StatisticalMeasureType userValue) throws Exception {
	
		return databaseValue.getMax() > userValue.getMax();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.StatisticalMesureComparatorInterface#checkLessThan(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)
	 */
	public boolean checkLessThan(StatisticalMeasureType databaseValue, 
			StatisticalMeasureType userValue) throws Exception {
		
		
		
		return databaseValue.getMin() < userValue.getMin();
	}
	
	/**
	 * is databaseValue.getMin() <= userValue.getMin()?
	 * @param databaseValue
	 * @param userValue
	 * @return true databaseValue.getMin() <= userValue.getMin();
	 * @throws 
	 *  databaseValue == null or 
	 * userValue == null
	 */
	public boolean checkLessThanOrEquals(StatisticalMeasureType databaseValue, 
			StatisticalMeasureType userValue) throws Exception {
		
		StatisticalMeasureType mindatabaseValue = new StatisticalMeasureType();
		mindatabaseValue.setMin(databaseValue.getMin());
		mindatabaseValue.setMax(mindatabaseValue.getMin());
		
	
		
		

		return (this.checkLessThan(databaseValue,userValue) ||
				this.checkEquals(mindatabaseValue,userValue)
		);
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.StatisticalMesureComparatorInterface#checkEquals(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)
	 */
	public boolean checkEquals(StatisticalMeasureType databaseValue, 
			StatisticalMeasureType userValue)throws Exception{
		
		return (databaseValue.getMin() == userValue.getMin()) && 
		(databaseValue.getMax() == userValue.getMax());
	}
	
	
	

}

/**
 * $Id$
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
package project.efg.server.interfaces;

import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.util.interfaces.OperatorInterface;

public interface StatisticalMesureComparatorInterface
{

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
			StatisticalMeasureType userValue) throws Exception;}

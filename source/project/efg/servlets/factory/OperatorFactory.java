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
package project.efg.servlets.factory;

import project.efg.servlets.efgInterface.OperatorInterface;
import project.efg.servlets.efgServletsUtil.DefaultOperator;
import project.efg.servlets.efgServletsUtil.EqualsOperator;
import project.efg.servlets.efgServletsUtil.GreaterThanOperator;
import project.efg.servlets.efgServletsUtil.GreaterThanOrEqualsOperator;
import project.efg.servlets.efgServletsUtil.LessThanOperator;
import project.efg.servlets.efgServletsUtil.LessThanOrEqualsOperator;
import project.efg.servlets.efgServletsUtil.PlusOperator;

/**
 * @author kasiedu
 *
 */
public class OperatorFactory {
	/**
	 * 
	 * @param type - The type of operator to create
	 * @return the type of operator created
	 */
	public static synchronized OperatorInterface getInstance(String type){
		
		
			if(type == null){
				return new DefaultOperator();
			}
			if(type.trim().startsWith("+")){
				return new PlusOperator();
			}
			/*if(type.trim().startsWith("-")){
				return new MinusOperator();
			}*/
			if(type.trim().startsWith("<=")){
				return new LessThanOrEqualsOperator();
			}
			if(type.trim().startsWith(">=")){
				return new GreaterThanOrEqualsOperator();
			}
			if(type.trim().startsWith("=")){
				return new EqualsOperator();
			}
			if(type.trim().startsWith("<")){
				return new LessThanOperator();
			}
			if(type.trim().startsWith(">")){
				return new GreaterThanOperator();
			}
			return new DefaultOperator();
		
	}

}

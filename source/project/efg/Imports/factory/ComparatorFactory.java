package project.efg.Imports.factory;
/**
 * $Id$
 * $Name$
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
import java.util.Comparator;



import project.efg.Imports.efgImportsUtil.ComparatorImplDefault;

public class ComparatorFactory  {
	
	/**
	 * @param type - The type of Comparator to create
	 * Introspection is used to create the class 
	 * so pass in the full class name 
	 * A project.efg.ComparatorImplDefault instance is 
	 * returned if the type supplied cannot be found.
	 * @return a CaseInsensitive Comparator object
	 */
	public static Comparator getComparator(String type){
		
			String mutex ="";
			synchronized (mutex) {
				try{
				//log.debug("Comparator Type: " + type);
				
				Class cls =  Class.forName(type);
				//log.debug("Returning Comparator class: " + cls.getName());
				return (java.util.Comparator)cls.newInstance(); 
				}
				catch(Exception ee){
					//log.debug("Will return default");
				}
				return new ComparatorImplDefault();
			}
	}
}

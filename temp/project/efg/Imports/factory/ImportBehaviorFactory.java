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
package project.efg.Imports.factory;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.ImportBehaviorImplNew;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.Imports.efgInterface.ImportBehavior;

/**
 * @author kasiedu
 *
 */
public class ImportBehaviorFactory {
	
	static Logger log;
	static {
		try {
			log = Logger.getLogger(ImportBehaviorFactory.class);
		} catch (Exception ee) {
		}
	}
	public static synchronized ImportBehavior getImportBehavior(EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj, 
			 String behaviorType){
		try{
			Class cls =  Class.forName(behaviorType);
		       Constructor constructor
		         = cls.getConstructor( new Class[]{ 
		        		 project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface.class,
		        		 project.efg.Imports.efgInterface.EFGDatasourceObjectInterface.class } 
		         );
		       Object importB
		         = constructor.newInstance( new Object[]{ lists,obj});
		       return (ImportBehavior)importB; 
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.debug("Returning default!!");
		}
		
		return new ImportBehaviorImplNew(lists,obj);
		
	}
	

}
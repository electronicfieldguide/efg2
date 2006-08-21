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
package project.efg.Imports.efgImpl;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.ChoiceCommandAbstract;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.Imports.efgInterface.ImportBehavior;
import project.efg.Imports.factory.ImportBehaviorFactory;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class ChoiceCommandReplaceImpl extends ChoiceCommandAbstract {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(ChoiceCommandAbstract.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public ChoiceCommandReplaceImpl() {
		super();
		log.debug("Creating " + this.getClass().getName());
		
	}
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.ChoiceCommandAbstract#createImportBehavior(project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface, project.efg.Imports.efgInterface.EFGDatasourceObjectInterface, java.lang.String)
	 */
	public ImportBehavior createImportBehavior(
			EFGDatasourceObjectListInterface lists, 
			EFGDatasourceObjectInterface obj 
			) {
		String behaviorType =
			EFGImportConstants.EFGProperties.
			getProperty("importReplaceBehavior");
			log.debug("BehaviorType: " + behaviorType);
			return ImportBehaviorFactory.getImportBehavior(
				lists,
				obj, 
				behaviorType
				);
	}
}

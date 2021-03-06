/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
package project.efg.client.impl.nogui;

import org.apache.log4j.Logger;

import project.efg.client.factory.gui.GUIFactory;
import project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface;
import project.efg.client.interfaces.gui.ImportBehavior;
import project.efg.client.interfaces.nogui.ChoiceCommandAbstract;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class ChoiceCommandExistImpl extends ChoiceCommandAbstract {
	static Logger log;
	static {
		try {
		log = Logger.getLogger(ChoiceCommandExistImpl.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public ChoiceCommandExistImpl() {
		super();
	}
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.ChoiceCommandAbstract#createImportBehavior(project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface, project.efg.util.interfaces.EFGDatasourceObjectInterface, java.lang.String)
	 */
	public ImportBehavior createImportBehavior(EFGDatasourceObjectListInterface lists, 
			EFGDatasourceObjectInterface obj) {
		String behaviorType= 
			EFGImportConstants.EFGProperties.getProperty("importUseMetadataBehavior");
		log.debug("BehaviorType: " + behaviorType);
		return 
		GUIFactory.getImportBehavior(
			lists,
			obj, 
			behaviorType
			);
	}
	
}

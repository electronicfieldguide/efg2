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

import project.efg.client.factory.gui.SpringGUIFactory;
import project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface;
import project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface;
import project.efg.client.interfaces.gui.ImportBehavior;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;


/**
 * @author kasiedu
 *
 */
public class ImportBehaviorImplNew extends ImportBehavior {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImportBehaviorImplNew.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public ImportBehaviorImplNew(EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj) {
		super(lists,obj);
		log.debug("Creating " + this.getClass().getName());
	}

	
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.ImportIntoDatabase#importIntoDatabase(project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface, project.efg.util.interfaces.EFGDatasourceObjectInterface, java.lang.String)
	 */
	public EFGDatasourceObjectStateInterface importIntoDatabase() {
		EFGDatasourceObjectStateInterface state =
			SpringGUIFactory.getFailureObject();
		if(this.checkLists()){
			log.debug("CheckLists returns true");
			boolean bool = this.lists.addEFGDatasourceObject(this.obj,this);
			if(bool){//factory
				state = SpringGUIFactory.getSuccessObject();
				
			}
		}
		this.obj.setState(state);
		return state;
	}

}

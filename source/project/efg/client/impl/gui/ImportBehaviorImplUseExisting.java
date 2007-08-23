/**
 * $Id: ImportBehaviorImplUseExisting.java,v 1.1.1.1 2007/08/01 19:11:14 kasiedu Exp $
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
package project.efg.client.impl.gui;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import project.efg.client.factory.gui.SpringGUIFactory;
import project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface;
import project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface;
import project.efg.client.interfaces.gui.ImportBehavior;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;


/**
 * @author kasiedu
 *
 */
public class ImportBehaviorImplUseExisting extends ImportBehavior {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImportBehaviorImplUseExisting.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public ImportBehaviorImplUseExisting(EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj) {
		super(lists,obj);
	}

	
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.ImportIntoDatabase#importIntoDatabase(project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface, project.efg.util.interfaces.EFGDatasourceObjectInterface, java.lang.String)
	 */
	public EFGDatasourceObjectStateInterface importIntoDatabase() {
		EFGDatasourceObjectStateInterface state =
			SpringGUIFactory.getFailureObject();
		if(this.checkLists()){
			if (this.lists.getCount() > 0) {
				String [] possibleValues = this.getAlphabeticallySortedList();
				Object selectedValue =  JOptionPane.showInputDialog(null, "Select one", "Input",
						JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
						possibleValues[0]);
				if (selectedValue != null) {
					//log.debug("Selectedvalue: " + selectedValue.toString());
					this.obj.setTemplateDisplayName(selectedValue.toString()
							.trim());
					boolean bool =  this.lists.addEFGDatasourceObject(this.obj,this);
					if(bool){
						state = SpringGUIFactory.getSuccessObject();
						this.responseMessage.append(this.obj.getDisplayName() + " ");
						String message = EFGImportConstants.EFGProperties
						.getProperty("SynopticKeyTree.add.success");
						this.responseMessage.append(message);
						//log.debug(this.responseMessage.toString());
					}
				}	
				else{
					state =SpringGUIFactory.getNeutralObject();
					log.debug("Selected value is null");
					this.responseMessage.append(EFGImportConstants.EFGProperties
							.getProperty("SynopticKeyTree.terminateImport"));
				}
			}
		}
		this.obj.setState(state);
		return state;
	}

}

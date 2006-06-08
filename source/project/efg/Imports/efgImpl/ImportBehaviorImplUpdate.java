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

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface;
import project.efg.Imports.efgInterface.ImportBehavior;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class ImportBehaviorImplUpdate extends ImportBehavior {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImportBehaviorImplUpdate.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public ImportBehaviorImplUpdate(EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj) {
		super(lists, obj);
		
	}
	
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.ImportIntoDatabase#importIntoDatabase(project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface, project.efg.Imports.efgInterface.EFGDatasourceObjectInterface, java.lang.String)
	 */
	public EFGDatasourceObjectStateInterface importIntoDatabase() {
		String message = null;
		EFGDatasourceObjectStateInterface state =
			this.stateFactory.getFailureObject();//use a fctory
		
		if (this.lists.getCount() > 0) {
			String [] possibleValues = this.getAlphabeticallySortedList();
			Object selectedValue =  JOptionPane.showInputDialog(null, "Select one", "Input",
					JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
					possibleValues[0]);
			if (selectedValue != null) {
				log.debug("Selectedvalue: " + selectedValue.toString());
				this.obj.setTemplateDisplayName(selectedValue.toString()
						.trim());
				log.debug("DisplayName is: " + this.obj.getDisplayName());
				this.obj.setDisplayName(selectedValue.toString()
						.trim());
				boolean bool = this.lists.addEFGDatasourceObject(this.obj, true);
				if(bool){
					state = this.stateFactory.getSuccessObject(); 
					this.responseMessage.append(this.obj.getDisplayName() + " ");
					message = EFGImportConstants.EFGProperties
					.getProperty("SynopticKeyTree.update.success");
					this.responseMessage.append(message);
					log.debug(this.responseMessage.toString());
				}
			}
			else{
				state =this.stateFactory.getNeutralObject(); 
				log.debug("Selectedvalue is null");
				message = EFGImportConstants.EFGProperties
					.getProperty("SynopticKeyTree.terminateImport");
			this.responseMessage.append(message);
			log.error(this.responseMessage.toString());
			}
		}
		else{
			message = EFGImportConstants.EFGProperties
			.getProperty("SynopticKeyTree.updateTerminate");
			this.responseMessage.append(message);
			log.debug(this.responseMessage.toString());
		}
		this.obj.setState(state);
		return state;
	}
}

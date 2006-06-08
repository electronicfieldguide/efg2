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

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.ChoiceCommandAbstract;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class ImportCommandFactory {
	final static String importOnlyCommand = 
		EFGImportConstants.EFGProperties.getProperty("ImportDialog.importOnlyCmd");
	final static String importUseMetadataCommand = 
		EFGImportConstants.EFGProperties.getProperty("ImportDialog.importUseMetadataCmd");
	final static String updateCommand =
		EFGImportConstants.EFGProperties.getProperty("ImportDialog.updateCmd");
	
	static Logger log;
	static {
		try {
			log = Logger.getLogger(ImportCommandFactory.class);
		} catch (Exception ee) {
		}
	}
	public static synchronized ChoiceCommandAbstract
	getImportCommand(String command){
		String selected = null;
		if (importOnlyCommand.equalsIgnoreCase(command)) {
			selected = 
				EFGImportConstants.EFGProperties.getProperty("importOnlyCommand");
		} else if (importUseMetadataCommand.equalsIgnoreCase(command)) {
			selected = 
				EFGImportConstants.EFGProperties.getProperty("importUseMetadataCommand");
		} else if (updateCommand.equalsIgnoreCase(command)) {
			selected =
				EFGImportConstants.EFGProperties.getProperty("importUpdateCommand");
		} else {
			log.debug("No command was selected");
			return null;
			/*selected = 
				EFGImportConstants.EFGProperties.getProperty("importDefaultCommand");
				*/
		}
		try{
			Class cls =  Class.forName(selected);
			return (ChoiceCommandAbstract)cls.newInstance();
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;	
	}
	
}

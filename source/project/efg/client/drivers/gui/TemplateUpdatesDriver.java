/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2007  University of Massachusetts Boston
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
package project.efg.client.drivers.gui;

import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import project.efg.client.drivers.nogui.TemplateLoader;
import project.efg.util.factory.TemplateModelFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.TemplateModelHandler;


/**
 * @author kasiedu
 *
 */
public class TemplateUpdatesDriver {
/**
 * 
 */
protected static void updateDB(DBObject dbObject) {
	StringBuffer  buffer = new StringBuffer("SELECT ");
	buffer.append(EFGImportConstants.QUERY_STR);
	buffer.append(" FROM ");
	buffer.append(EFGImportConstants.TEMPLATE_TABLE);
	try {
		TemplateModelHandler temp = 
			TemplateModelFactory.createImportTemplateHandler(dbObject);
		List list = temp.executeQueryForList(buffer.toString(), 1);
		if(list != null && list.size() > 0){
			
			return;
		}
	} catch (Exception e1) {//means the table is already updated
		
	}
	TemplateLoader tloader = new TemplateLoader(dbObject);
	tloader.run();
}
	public static void main(String[] args) {
		//cause properties to load
		String urldb1 = 
			EFGImportConstants.EFGProperties.getProperty("dburl");
		System.out.println("urldb: " + urldb1);
		DBObject dbObject1 = new DBObject(
				urldb1,"root",
				"kw7679as");
		TemplateUpdatesDriver.updateDB(dbObject1);
		
		
		if(args.length == 2 ){
			Properties properties = EFGImportConstants.EFGProperties;
			String userName = args[0];
			String password = args[1];
			
			String urldb = 
				EFGImportConstants.EFGProperties.getProperty("dburl");
			DBObject dbObject = new DBObject(
					urldb,userName,
					password);
			TemplateUpdatesDriver.updateDB(dbObject);
			
		}
		else{
			JOptionPane.showMessageDialog(null, 
				"Database updates failed. Please re-install application",
					"Database update failure", JOptionPane.ERROR_MESSAGE);
		}
	}
}

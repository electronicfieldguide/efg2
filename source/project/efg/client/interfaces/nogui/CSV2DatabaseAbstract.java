package project.efg.client.interfaces.nogui;

/* $Id: CSV2DatabaseAbstract.java,v 1.1.1.1 2007/08/01 19:11:16 kasiedu Exp $
* $Name:  $
* Created: Tue Feb 28 13:14:19 2006
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
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
* Imports a csv file into a relational database
* 
*/


import org.apache.log4j.Logger;

import project.efg.client.interfaces.gui.ImportBehavior;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.EFGUtils;

public abstract class CSV2DatabaseAbstract {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(CSV2DatabaseAbstract.class);
		} catch (Exception ee) {
		}
	}
	protected String[] legalNames;
	protected DBObject dbObject;
	protected EFGDatasourceObjectInterface datasource;
	protected EFGDataExtractorInterface dataExtractor;
	protected ImportBehavior isUpdate;
	
	
	

	

	/**
	 * @param datasource -
	 *            An object
	 * @param isUpdate -
	 *            true if a table is being updated, false otherwise.
	 * 
	 * Updates in the current implementation means the following: i) The data in
	 * the database will be replaced by the data being imported. ii) The column
	 * names of the data being imported should match both the column names and
	 * the number of field names of the data in the database.
	 * 
	 * If isUpdate is set to 'true' then point(ii) above should also be 'true'
	 * otherwise the update will fail. If isUpdate is set to 'false' the system
	 * will: (i) Replace the data in the database if it exists with the current
	 * data. *@param dsObject - Contains the name of the table to use or create -
	 * The display name of an existing Table whose metadata table should be used
	 * by the current table. - A human readable string to use as display name on
	 * user interfaces for instance. Defaults to the name of the data table.
	 * 
	 * If dsObject.getTemplateDisplayName() is not null and not the empty string
	 * and the update flag is set to false System must verify that : (i)The
	 * system must use this parameter to find the metadata table to use for
	 * importing the user supplied dataTable.
	 * 
	 * (ii)each of the field names of the dataTable must be found in the 'name'
	 * column of the metadataTable(retrieved in i above) in the database.
	 * (iii)the number of fields of the data being imported equals the number of
	 * rows of the metadataTable in the database. (iv) If any of the conditions
	 * in (i)-(iii) is false System should abort the importation of data.
	 * Otherwise system should import the data
	 * 
	 * else If the update flag is set to true.The system must: (i) Ignore the
	 * dsObject.getTemplateDisplayName() (ii) Verify that a table called
	 * tableName parameter already exists in the database. (iii)Verify that a
	 * table called tableName parameter is associated with a metadataTable
	 * (iv)Verify that points (ii) and (iii) of the if clause above is true. (v)
	 * If any of the conditions in (i)-(iv) is false system should abort the
	 * imports.
	 * 
	 * If displayName is null or the empty string, system must make it the same
	 * as the dataTable name unless isUpate is 'true'. If
	 * dsObject.getTemplateDisplayName() is null or the empty String system must
	 * create a corresponding metadata table for the data being imported.
	 * 
	 * This implementation assumes that a database table already exists to hold
	 * the tables to be created in this implementation.
	 */
	/*public CSV2DatabaseAbstract(EFGDatasourceObjectInterface datasource,
			EFGDataExtractorInterface dataExtractor,DBObject dbObject,
			ImportBehavior isUpdate) {
		this.dataExtractor = dataExtractor;
		this.datasource = datasource;
		this.isUpdate = isUpdate;
		this.dbObject = dbObject;
		
	}*/
	public CSV2DatabaseAbstract() {
	}
	public  void setDatasource(EFGDatasourceObjectInterface datasource) {
		this.datasource = datasource;
	}
	public  void setDataExtractor(EFGDataExtractorInterface dataExtractor) {
		this.dataExtractor = dataExtractor;
	}
	public  void setDBObject(DBObject dbObject) {
		this.dbObject = dbObject;
	}
	public  void setISUpdate(ImportBehavior isUpdate) {
		this.isUpdate = isUpdate;
	}

	
	/**
	 * Import the current data into your database.
	 * 
	 * @return true if import was successful. 
	 * false otherwise
	 */
	public abstract boolean import2Database();

	/**
	 * @return the display name for the current database table
	 */
	public String getDisplayName() {
		return this.datasource.getDisplayName();
	}

	/**
	 * Translate the header names supplied by the data to be imported to field
	 * names that your database can understand. The default implementation is to
	 * change the name to a string that could be used as a java identifier. 
	 * 
	 * @param headers -
	 *            A String array of names to be used as table headers for the
	 *            table about to be created.
	 */
	protected String[] translateHeaders(String[] headers) {
		if (headers == null) {
			return null;
		}
		try {
			//convert headers to a set
			//perhaps this is where you should bale out
			//need to make sure headers are unique
			String[] meta = new String[headers.length];
			for (int i = 0; i < headers.length; i++) {
				String title = headers[i];
				
				String newTitle = EFGUtils.encodeToFieldName(title);
				
				//make sure header does not already exists
				meta[i] = newTitle.trim().toLowerCase();
			}
			return meta;
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
		return null;
	}
	/**
	 * @return a list of legal names for each field in the data. Must be
	 *         implemented to return a list of headers that can be used as field
	 *         names in your database.
	 * @see translateHeaders(String[])
	 */
	protected String[] getLegalNames() {
		
		if (this.legalNames == null) {
			try {
				this.legalNames = this.translateHeaders(dataExtractor.getFieldNames());
			} catch (Exception e) {
				
				log.error(e.getMessage());
			}
		}
		return this.legalNames;
	}



	
}//

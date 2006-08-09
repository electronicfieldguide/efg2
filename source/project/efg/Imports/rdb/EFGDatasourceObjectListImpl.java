/**
 * EFGDatasourceObjectLists.java
 *
 ** $Id$
 * $Name$
 * 
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
 * Created: Sun Feb 19 09:22:49 2006
 *
 */
package project.efg.Imports.rdb;

import java.io.File;
import java.net.URI;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgInterface.CSV2DatabaseAbstract;
import project.efg.Imports.efgInterface.EFGDataExtractorInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.factory.DatabaseAbstractFactory;
import project.efg.Imports.factory.EFGDataExtractorFactory;
import project.efg.Imports.factory.EFGDatasourceObjectFactory;
import project.efg.Imports.factory.EFGRowMapperFactory;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGImportConstants;

public class EFGDatasourceObjectListImpl extends
		EFGDatasourceObjectListInterface {
	

	private JdbcTemplate jdbcTemplate;
	private DataSourceTransactionManager txManager;
	
	private EFGRowMapperInterface rowMapper;

	

	private String efgRDBTable;



	public EFGDatasourceObjectListImpl(DBObject dbObject) {
		super(dbObject);
		this.efgRDBTable = EFGImportConstants.EFGProperties.getProperty("ALL_EFG_RDB_TABLES");
		this.txManager = this.getTransactionManager(this.dbObject);
		this.jdbcTemplate = this.getJDBCTemplate(this.dbObject);
		this.rowMapper = EFGRowMapperFactory.getRowMapper();
		this.populateLists();
	}
	/**
	 * Remove an object from the database
	 * 
	 * @param datasource -
	 *            The datasource object to remove
	 * @return true if the datasource object was part of the list and was
	 *         removed , false otherwise.
	 */
	public boolean removeEFGDatasourceObject(
			EFGDatasourceObjectInterface datasource) {
		return removeEFGDatasourceObject(datasource.getDisplayName());
	}
	/**
	 * Remove an object with the given display name from the database
	 * 
	 * @param datasource -
	 *            The datasource object to remove
	 * @return true if the datasource object was part of the list and was
	 *         removed , false otherwise.
	 */
	public boolean removeEFGDatasourceObject(String displayName) {
		return this.deleteTable(displayName);
	}

	/**
	 * Add an object to the database
	 * 
	 * @param datasource -
	 *            The datasource object to add
	 * @param isUpdate -
	 *            true,If there is an update to a data table.
	 * @return true if this datasource was successfully added, false otherwise
	 */
	public boolean addEFGDatasourceObject(
			EFGDatasourceObjectInterface datasource, boolean isUpdate) {
	
	
		try {
			//log.debug("Delimiter: " + EFGImportConstants.EFGProperties.getProperty("delimiter"));
			String delimiter =EFGImportConstants.EFGProperties.getProperty("delimiter").trim();
			char[] delimArr = delimiter.toCharArray();
			//log.debug("After char delimiter");
			EFGDataExtractorInterface extractor = 
				EFGDataExtractorFactory.getDataExtractor(
					datasource.getDataName(),
					delimArr[0]
					);
			//log.debug("after extractor");
			//use abstract method call
			CSV2DatabaseAbstract table = 
				DatabaseAbstractFactory.getDatabaseObject(datasource,
					extractor,
					dbObject,
					isUpdate);
			
			//log.debug("after csv2Database");
			
			boolean bool = table.import2Database();
			if (bool) {
				//add only if it is not already present
				//if the object already exists then replace it
				int find = this.findObjectIndex(datasource.getDisplayName());
				if(find > -1){
					this.lists.remove(find);
				}
				this.lists.add(datasource);
				
				datasource.setState(stateFactory.getSuccessObject());
				return true;
			}
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
		}
		datasource.setState(stateFactory.getFailureObject());
		return false;
	}



	/**
	 * @param oldDisplayName -
	 *            the display name to replace.
	 * @param freshDisplayName-
	 *            the display name tio be used to replace the old one.
	 * @return true if the display name for the object is successfully changed.
	 */
	public boolean replaceDisplayName(String oldDisplayName,
			String freshDisplayName) {
		try {

			int index = this.findObjectIndex(oldDisplayName);
			if (index > -1) {
				//log.debug("About to replace oldDisplayName: '" + oldDisplayName
				//		+ "'");
				//log.debug("with new DisplayName: '" + freshDisplayName + "'");
				EFGDatasourceObjectInterface obj = (EFGDatasourceObjectInterface) this.lists
						.get(index);
				
				String query = "UPDATE " + this.efgRDBTable
						+ " SET DISPLAY_NAME = \"" + freshDisplayName + "\" "
						+ "WHERE DISPLAY_NAME = \"" + oldDisplayName + "\"";
				// will hold the query
				
				this.executeStatement(query);
				obj.setDisplayName(freshDisplayName);
				return true;
			} else {
				//log.debug("DisplayName: " + oldDisplayName + " not in lists");
			}

		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);

		}
		return false;
	}
	/**
	 * @param object
	 * @return
	 */
	private DataSourceTransactionManager getTransactionManager(DBObject dbObject){
		if(this.txManager == null){
			this.txManager = EFGRDBImportUtils.getTransactionManager(dbObject);
		}
		return this.txManager;
	}
	private int executeStatement(String query) throws Exception {
		return EFGRDBImportUtils.executeStatement(this.txManager,
				this.jdbcTemplate,
				query);
	}

	private List executeQueryForList(String query,int numberOfColumns)
	throws Exception {
			return  this.rowMapper.mapRows(
					this.jdbcTemplate,
					query,
					numberOfColumns
					);
	}
	
	private JdbcTemplate getJDBCTemplate(DBObject dbObject) {
		if (this.jdbcTemplate == null) {
			//log.debug("Creating new Template");
			this.jdbcTemplate = EFGRDBImportUtils.getJDBCTemplate(dbObject);
		}
		return this.jdbcTemplate;
	}

	
	/**
	 * Check if a Table exist in Database.
	 * 
	 * @param tableName
	 *            the name of the table
	 * @return true if the table exists, false otherwise
	 * FIX ME
	 */
	private boolean isExistTable(String tableName) {
		final String tabName = tableName;
		try {
			java.sql.ResultSet rst = 
				(java.sql.ResultSet)JdbcUtils.extractDatabaseMetaData(
						this.jdbcTemplate.getDataSource(), 
						new DatabaseMetaDataCallback() {
				  public Object processMetaData(DatabaseMetaData dbmd) throws SQLException {
				    String[] types = {"TABLE"};
				    return dbmd.getTables(null, null, tabName, types);
				  }
				});
			if (rst.next()) {
				rst.close();
				return true;
			}
		} catch (Exception ex) {
			LoggerUtilsServlet.logErrors(ex);
		}
		return false;
	}

	/**
	 * Populate the lists with objects from the database
	 */
	private void populateLists() {
		if (!isExistTable(this.efgRDBTable)) {
			return;
		}

		try {
			// check if there is a table with that name
			StringBuffer query = new StringBuffer();
			query.append("SELECT DS_METADATA, ORIGINAL_FILE_NAME," +
					" DISPLAY_NAME FROM ");
			query.append(this.efgRDBTable);
			List list =  EFGRDBImportUtils.executeQueryForList(
					this.jdbcTemplate,query.toString(),3);
					
			
			//log.debug("Size of list on user interface: " + list.size());
			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iter.next();
				String md_name=queue.getObject(0);
				String name=queue.getObject(1);
				String display=queue.getObject(2);
				EFGDatasourceObjectInterface doInterface = 
					EFGDatasourceObjectFactory.getEFGDatasourceObject(URI.create(EFGUtils
						.reverseParseEFGSEP(name)), md_name, display);
				this.lists.add(doInterface);
			}
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
		}
	}

	private boolean delete(String tableName) throws Exception {
		if ((tableName == null) || (tableName.trim().equals(""))) {
			//log.error("Display name must not be null or the empty String");
			return false;
		}
		this.executeStatement("DROP TABLE IF EXISTS " + tableName);

		return true;
	}
	/**
	 * 
	 * @param datafn
	 *            the data fileName to check .
	 * @param metadatafn
	 *            the metadata fileName to check .
	 * @return true if delete was successful.
	 */
	private boolean deleteTable(String displayName) {
		if ((displayName == null) || (displayName.trim().equals(""))) {
			//log.error("Display name must not be null or the empty String");
			return false;
		}

		boolean isDone = false;
		StringBuffer query = null;
		try {
			// get the row from the efgRDBTable if it exists
			query = new StringBuffer("SELECT DS_DATA, DS_METADATA FROM ");
			query.append(this.efgRDBTable);
			query.append(" WHERE DISPLAY_NAME = \"");
			query.append(displayName);
			query.append("\"");
			//log.debug("About to execute query: " + query.toString());
			
			java.util.List list = this.executeQueryForList(query.toString(),
					2);
			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface)iter.next();
				String datafn = queue.getObject(0);
				//log.debug("Data table to be removed: " + datafn);
				String metadatafn = queue.getObject(1);
				//log.debug("MetaData table to be removed: " + metadatafn);
				this.delete(datafn);
				this.delete(metadatafn);

				// remove the row from the efgRDBTable
				String qr = "DELETE FROM " + this.efgRDBTable
						+ " WHERE DS_DATA = \"" + datafn + "\";";
				this.executeStatement(qr);

				int index = this.findObjectIndex(displayName);
				if (index > -1) {
					this.lists.remove(index);
				}
				//also delete the file if it exists
				
				try{
					File templatesFiles = new File(this.getTemplateConfig() + 
							datafn +
							EFGImportConstants.XML_EXT);
					if(templatesFiles.exists()){
						boolean bool = templatesFiles.delete();
						if(!bool){
							throw new Exception("Application could not delete the template " +
									templatesFiles.getAbsolutePath());
						}
					}
				}
				catch(Exception ee){
					LoggerUtilsServlet.logErrors(ee);
				}
				isDone = true;
			}
		} catch (Exception ex) {
			LoggerUtilsServlet.logErrors(ex);
		}

		return isDone;
	}
	
}// EFGDatasourceObjectList


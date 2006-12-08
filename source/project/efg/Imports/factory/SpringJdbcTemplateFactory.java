package project.efg.Imports.factory;
/* $Id$
* $Name$
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

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.util.EFGImportConstants;
import java.util.Hashtable;

public class SpringJdbcTemplateFactory {
	
	
	private static Hashtable jdbcTemplateTable = new Hashtable();
	private static Hashtable transactionManagerTable = new Hashtable();
	private static Hashtable datasourceTable = new Hashtable();
	/**
	 * Returns a jdbcTemplate object needed to make database queries.
	 * 
	 * @param dbObject - contains information to connect to the database.
	 * @param notEFGDB - True if we need information from a database other than the
	 * efg database. false otherwise
	 * @return a jdbcTemplate for the current database url
	 */
	public static synchronized JdbcTemplate  getJdbcTemplateInstance(DBObject dbObject){
		DriverManagerDataSource datasource = getDatasource(dbObject);
		if(datasource == null){
			return null;
		}
		String url = dbObject.getURL().trim().toLowerCase();
		if(!jdbcTemplateTable.containsKey(url)){
		  jdbcTemplateTable.put(url, new JdbcTemplate(datasource));
		}
		return (JdbcTemplate)jdbcTemplateTable.get(url);
	}
	private static DriverManagerDataSource getDatasource(DBObject dbObject){
		if(dbObject == null){
			//log.error("DbObject is null");
			return null;
		}
		if(dbObject.getURL() == null){
			//log.error("DbObject.url is null");
			return null;
		}
		String url = dbObject.getURL().trim().toLowerCase();
		DriverManagerDataSource datasource = null;
		if(!datasourceTable.containsKey(url)){
			datasource = createDatasource(dbObject);
			if(datasource == null){
				return null;
			}
			datasourceTable.put(url,datasource);
		}
		return (DriverManagerDataSource)datasourceTable.get(url); 
	}
    public static synchronized DataSourceTransactionManager getTransactionManager(DBObject dbObject){
    	DriverManagerDataSource datasource = getDatasource(dbObject);
		if(datasource == null){
			return null;
		}
		String url = dbObject.getURL().trim().toLowerCase();
		if(!transactionManagerTable.containsKey(url)){
    	  transactionManagerTable.put(url, new DataSourceTransactionManager(datasource));
		}
    	return (DataSourceTransactionManager)transactionManagerTable.get(url);
    }
	private static DriverManagerDataSource createDatasource(DBObject dbObject){
		DriverManagerDataSource dataSource =
			new DriverManagerDataSource();
		dataSource.setDriverClassName(EFGImportConstants.EFGProperties.getProperty("dbDriverName"));
		dataSource.setUsername(dbObject.getUserName());
		dataSource.setPassword(dbObject.getPassword());
		dataSource.setUrl(dbObject.getURL());
		//try to get a connection return null if you cannot get it
		try{
			dataSource.getConnection();	
		}
		catch(Exception sql){
			//log.error("Could not obtain a connection from Database server");
			dataSource = null;
		}
		return dataSource;
	}
	

}

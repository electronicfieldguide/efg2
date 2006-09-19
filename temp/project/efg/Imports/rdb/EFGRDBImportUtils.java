/**
 * $Id$
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

package project.efg.Imports.rdb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.factory.EFGRowMapperFactory;
import project.efg.util.EFGImportConstants;


/**
 * This class is a utility class for creating and populating Relational
 * Databases for EFG. It is the responsibility of user to have a properties file
 * "RDBProps.properties" created with the relevant information before this class
 * is used.
 */
public class EFGRDBImportUtils implements EFGRDBConstants {

	

	
	private static Properties EFGProperties =null;
	private static EFGRowMapperInterface rowMapper;
	private static Set sqlKeywords;
	
	public static synchronized DataSourceTransactionManager
	getTransactionManager(DBObject dbObject){
		return project.efg.Imports.factory.SpringJdbcTemplateFactory.getTransactionManager(dbObject);
	}
	/**
	 * 
	 * @param dbObject -holds enough information to connect to database
	 * @return a JdbcTemplate object to be used to query database
	 */
	public static synchronized JdbcTemplate getJDBCTemplate(DBObject dbObject){
		
		return project.efg.Imports.factory.SpringJdbcTemplateFactory.getJdbcTemplateInstance(dbObject);
	}
	public static synchronized int executeStatement(DataSourceTransactionManager txManager,
			JdbcTemplate jdbcTemplate,
			String query) throws Exception {
		int ret = -1;
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);

		try{
			ret = jdbcTemplate.update(query);
			txManager.commit(status);
		}
		catch(Exception ee){
			txManager.rollback(status);
		}
		return ret;
	}
	
	public static synchronized List executeQueryForList(JdbcTemplate jdbcTemplate,
			String query,
			int numberOfColumns)
	throws Exception {
		return  getRowMapper().mapRows(jdbcTemplate,query,numberOfColumns);
	}
	/**
	 * Initializes connection to Database, sets the driver etc Parameters are
	 * read from a properties file (RDBProps.properties) in the same directory
	 * as this file. Properties file should have the following information:
	 * dbusername=your_username dbpasswd=your_password dburl=your_url(should
	 * include Database name and port number dbDriverName = your_jdbc_drivername
	 */
	public static synchronized void init() {
		//loop through properties file and recursively call method that adds them
		try {
			EFGProperties = System.getProperties();
			addPropertyFiles();
			addSQLKeyWords();
			
			} catch (Exception e) {
		   	e.printStackTrace();
		
		}
	}
	public static Set getSQLKeyWords(){
		if(sqlKeywords == null){
			addSQLKeyWords();
		}
		return sqlKeywords;
	}
	public static Properties getProperties(){
		if(EFGProperties == null){
			init();
		}
		return EFGProperties;
	}
	private static  EFGRowMapperInterface getRowMapper(){
		if(rowMapper == null){
			rowMapper = EFGRowMapperFactory.getRowMapper();
		}
		return rowMapper;
	}
	private static void addSQLKeyWords(){
		sqlKeywords = new HashSet();
		String sql =  
			EFGProperties.getProperty(EFGImportConstants.SQL_KEYWORD_KEY);
		
		//String[] csv = sql.split(EFGImportConstants.COMMASEP);
		String[] csv = EFGImportConstants.commaPattern.split(sql);
		for(int i=0; i < csv.length; i++){
			sqlKeywords.add(csv[i].trim().toLowerCase());
		}
	}
	/**
	 * Reads,loads an sets application wide properties file
	 * from all files located in the properties directory.
	 * 
	 * @return true if all of the files were read and loaded successfully
	 */
	private static boolean addPropertyFiles(){
		try{
		 			URL propsURL = 
				project.efg.Imports.rdb.EFGRDBImportUtils.class.getResource("/properties");
		
			String dir = URLDecoder.decode(propsURL.getFile(),"UTF-8");
	
			File file = new File(dir);		
			if(!file.isDirectory()){
				throw new Exception("Properties directory could not be found!!");
			}
			
			processFiles(file);
			return true;
		}
		catch(Exception ee){
			ee.printStackTrace();
		
		}
		return false;
	}
	/**
	 * recursively iterates over all files in the properties directory
	 * 
	 * @param file
	 *            the current property file
	 */
	private static void processFiles(File file) throws Exception{
		String[] propertyFiles = file.list();
	
		for(int i=0; i < propertyFiles.length; i++){
			String currentFile = propertyFiles[i];
			File newFile = new File(file,currentFile);

			if(newFile.isDirectory()){
				processFiles(newFile);
			}
			else{
				try {
				//	//log.debug("about to process file: " + newFile.getAbsolutePath());
					InputStream input = newFile.toURL().openStream();
					EFGProperties = new Properties(EFGProperties);
					EFGProperties.load(input);
					if(input != null){
						input.close();
					}
				} catch (MalformedURLException e) {
				
					throw e;
				} catch (IOException e) {
				
					throw e;
				}
			}
		}
	}
	
	

}

// $Log$
// Revision 1.1.2.2  2006/09/19 22:36:38  kasiedu
// no message
//
// Revision 1.1.2.4  2006/09/10 12:02:28  kasiedu
// no message
//
// Revision 1.1.2.3  2006/08/09 18:55:24  kasiedu
// latest code conforms to what exists on Panda
//
// Revision 1.1.2.2  2006/06/21 04:21:37  kasiedu
// Fixed some errors that did not show up in eclipse in build file
//
// Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
// New files
//
// Revision 1.2 2006/01/26 04:20:46 kasiedu
// no message
//
// Revision 1.1.1.1 2006/01/25 21:03:42 kasiedu
// Release for Costa rica
//
// Revision 1.1.1.1 2003/10/17 17:03:05 kimmylin
// no message
//
// Revision 1.8 2003/08/20 18:45:41 kimmylin
// no message
//


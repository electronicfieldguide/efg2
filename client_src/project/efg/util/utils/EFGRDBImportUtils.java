/**
 * $Id$
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

package project.efg.util.utils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.EFGRDBConstants;
import project.efg.util.interfaces.EFGRowMapperInterface;



/**
 * This class is a utility class for creating and populating Relational
 * Databases for EFG. It is the responsibility of user to have a properties file
 * "RDBProps.properties" created with the relevant information before this class
 * is used.
 */
public class EFGRDBImportUtils implements EFGRDBConstants {

	private static Properties EFGProperties;
	private static EFGRowMapperInterface rowMapper;
	private static Set sqlKeywords;
	
	static{
		init();
	}
	/**
	 * 
	 * @param dbObject
	 * @return
	 */
	public static synchronized DataSourceTransactionManager
	getTransactionManager(DBObject dbObject){
			return project.efg.util.factory.SpringJdbcTemplateFactory.getTransactionManager(dbObject);
	}
	/**
	 * 
	 * @param dbObject -holds enough information to connect to database
	 * @return a JdbcTemplate object to be used to query database
	 */
	public static synchronized JdbcTemplate getJDBCTemplate(DBObject dbObject){
		
		return project.efg.util.factory.SpringJdbcTemplateFactory.getJdbcTemplateInstance(dbObject);
	}
	/**
	 * 
	 * @param txManager
	 * @param jdbcTemplate
	 * @param query
	 * @return
	 * @throws Exception
	 */
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
	/**
	 * 
	 * @param jdbcTemplate
	 * @param query
	 * @param numberOfColumns
	 * @return
	 * @throws Exception
	 */
	public static synchronized List executeQueryForList(JdbcTemplate jdbcTemplate,
			String query,
			int numberOfColumns)
	throws Exception {

		return  getRowMapper().mapRows(jdbcTemplate,query,numberOfColumns);
	}
	/**
	 * 
	 * @return
	 */
	private static  EFGRowMapperInterface getRowMapper(){
		if(rowMapper == null){
			rowMapper = SpringFactory.getRowMapper();
		}
		return rowMapper;
	}
	/**
	 * Reads,loads an sets application wide properties file
	 * from all files located in the properties directory.
	 * 
	 * @return true if all of the files were read and loaded successfully
	 */
	private static File loadPropertiesDirectory(){
		try{
		 		URL propsURL = 
				project.efg.util.utils.EFGRDBImportUtils.class.getResource("/properties");
		
			String dir = URLDecoder.decode(propsURL.getFile(),"UTF-8");
	
			File file = new File(dir);		
			if(!file.isDirectory()){
				throw new Exception("Properties directory could not be found!!");
			}
			return file;
		}
		catch(Exception ee){
			ee.printStackTrace();
		
		}
		return null;
	}

	/**
	 * 
	 *
	 */
	public static synchronized void init() {
		File file = loadPropertiesDirectory();
		EFGProperties props = new EFGProperties(file);
		EFGProperties = props.getProperties();
		sqlKeywords= props.getSQLKeyWords();
	}
	/**
	 * 
	 * @return
	 */
	public static Set getSQLKeyWords(){
		return sqlKeywords;
	}
	/**
	 * 
	 * @return
	 */
	public static Properties getProperties(){
		return EFGProperties;
	}
}

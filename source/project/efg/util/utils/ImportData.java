/**
 * 
 */
package project.efg.util.utils;

/**
 * @author kasiedu
 *
 */

	/**
	 * Copyright Isocra Ltd 2004
	 * You can use, modify and freely distribute this file as long as you credit Isocra Ltd.
	 * There is no explicit or implied guarantee of functionality associated with this file, 
	 * use it at your own risk.
	 * 
	 * Modified for efg by J. Asiedu
	 */



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;


	/**
	 * This class connects to a database and dumps all the tables and contents out to stdout in the 
	 * form of
	 * a set of SQL executable statements
	 */
	public class ImportData {
		private boolean isCreate=false;
		private StringBuffer currentCreateBuffer;
		private JdbcTemplate jdbcTemplate;
		private DBObject dbObject;
		static Logger log = null;
		static {
			try {
				log = Logger.getLogger(ImportData.class);
			} catch (Exception ee) {
			}
		}
		public ImportData(DBObject object) {
			this.dbObject = object;
			this.jdbcTemplate = this.getJDBCTemplate(this.dbObject);
		}
		private JdbcTemplate getJDBCTemplate(DBObject dbObject) {
			if (this.jdbcTemplate == null) {
				// log.debug("Creating new Template");
				this.jdbcTemplate = EFGRDBImportUtils.getJDBCTemplate(dbObject);
			}
			return this.jdbcTemplate;
		}
		public void importData(BufferedReader in){
		  try {
		       String str=null;			        
		       while ((str = in.readLine()) != null) {
		    	   processQuery(str);
		       }
		      this.changeTableNamesInRDBTableToLowerCase();
		    } catch (UnsupportedEncodingException e) {
		    	log.error(e.getMessage());
		    } catch (IOException e) {
		    	log.error(e.getMessage());
		    }
		}
		/**
		 * 
		 */
		private void changeTableNamesInRDBTableToLowerCase() {
			String property = EFGImportConstants.EFGProperties.getProperty("efg.rdb.tolowercase","");
			if(property.trim().equals("")){
				try {
					List listOfDataTableNames = EFGRDBImportUtils.executeQueryForList(
							this.jdbcTemplate, getRDBQuery(), 2);
					for (java.util.Iterator iter = listOfDataTableNames.iterator(); iter
							.hasNext();) {
						EFGQueueObjectInterface field = (EFGQueueObjectInterface) iter
								.next();
						String ds_data = field.getObject(0);
						String ds_metadata = field.getObject(1);
						
						String query = makeQuery(ds_data,ds_metadata);
						try{
						 this.jdbcTemplate.update(query);
						}
						catch(Exception ccc){
						
						} 
					}
					EFGImportConstants.EFGProperties.setProperty(
							"efg.rdb.tolowercase","done");
					writeToFile();					
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
		/**
		 * 
		 */
		private void writeToFile() {
			
				try{
				 	URL propsURL = 
						this.getClass().getResource("/properties");
				
					String dir = URLDecoder.decode(propsURL.getFile(),"UTF-8");
			
					File file = new File(dir);		
					if(!file.isDirectory()){
						throw new Exception("Properties directory could not be found!!");
					}
					 BufferedWriter out = null;
					   try {
						   String filename = EFGImportConstants.EFGProperties.getProperty(
									"efg.rdb.tolowercase.file","efgrdblowercase.properties");

							File f = new File(dir,filename);
					       out = new BufferedWriter(
					        		new FileWriter(f));
					        out.write("efg.rdb.tolowercase=done");
					        out.write("\n");
					        out.flush();
					        out.close();
					    } catch (IOException e) {
					    	try{
					    		if(out != null ){
					    			out.close();
					    		}
					    	}
					    	catch(Exception ee){
					    		
					    	}
					    	log.error(e.getMessage());
					    }					
				}
				catch(Exception ee){
					log.error(ee.getMessage());
				}
		}
		/**
		 * @param ds_data
		 * @param ds_metadata
		 * @return
		 */
		private String makeQuery(
				String ds_data, 
				String ds_metadata) {
			StringBuffer buffer = 
				new StringBuffer();
			
			buffer.append("UPDATE ");
			buffer.append(
					EFGImportConstants.EFGProperties.getProperty(
			"ALL_EFG_RDB_TABLES"));			
			buffer.append(" Set DS_DATA='");
			buffer.append(ds_data.toLowerCase());			
			buffer.append("',DS_METADATA='");
			buffer.append(ds_metadata.toLowerCase());
			buffer.append("'");
			buffer.append(" WHERE DS_DATA='");
			buffer.append(ds_data);
			buffer.append("'");
			return buffer.toString();
		}
		/**
		 * @return
		 */
		private String getRDBQuery() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT DISTINCT DS_DATA,DS_METADATA FROM ");
			buffer.append(EFGImportConstants.EFGProperties.getProperty(
			"ALL_EFG_RDB_TABLES"));
			return buffer.toString();
		}
		/**
		 * @param str
		 */
		private void processQuery(String line){
			if(line == null){
				return;
			}
			String toLower = line.toLowerCase();
			toLower.trim();
			if("".equals(toLower)){
				this.isCreate = false;
			}
			else if(toLower.startsWith("create")){
				this.currentCreateBuffer = new StringBuffer();
				this.currentCreateBuffer.append(line);				
				this.isCreate = true;
			}
			else if((toLower.startsWith("insert")) ||
					(toLower.startsWith("lock")) ||
					(toLower.startsWith("drop")) ||
					(toLower.startsWith("unlock"))){
				this.isCreate = false;
				this.executeQuery(line);
			}
			else if(toLower.startsWith(");")){
				if(this.isCreate){
					this.currentCreateBuffer.append(line);					
					this.executeQuery(
							this.currentCreateBuffer.toString()
							);
					this.isCreate = false;
				}
			}
			else if((toLower.startsWith("--")) || 
					(toLower.startsWith("/*"))){
				this.isCreate = false;
			}
			else{
				if(this.isCreate){//issue create command
					this.currentCreateBuffer.append(line);
				}
			}
		}
		/**
		 * 
		 * @param query
		 */
		private void executeQuery(String query) {
			try {
				String queryN = query.trim();
				if(queryN.indexOf(EFGImportConstants.MEDIUMTEXT) > -1){
					queryN = replaceString(queryN);
				}
				this.jdbcTemplate.update(query);
				
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
			}
		}
		/**
		 * @param queryN
		 * @param text
		 * @return
		 */
		private String replaceString(
				String queryN) {
			return queryN.replaceAll(EFGImportConstants.MEDIUMTEXT,
					EFGImportConstants.TEXT);
		}	
	}
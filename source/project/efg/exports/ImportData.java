/**
 * 
 */
package project.efg.exports;

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
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.rdb.EFGRDBImportUtils;
import project.efg.util.EFGImportConstants;

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
		    } catch (UnsupportedEncodingException e) {
		    	log.error(e.getMessage());
		    } catch (IOException e) {
		    	log.error(e.getMessage());
		    }
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

				this.jdbcTemplate.execute(query);
				
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
					EFGImportConstants.MEDIUMTEXT);
		}	
	}
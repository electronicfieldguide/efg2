/**
 * 
 */
package project.efg.util.utils;



	/**
	 * Copyright Isocra Ltd 2004
	 * You can use, modify and freely distribute 
	 * this file as long as you credit Isocra Ltd.
	 * There is no explicit or implied guarantee of 
	 * functionality associated with this file, 
	 * use it at your own risk.
	 * 
	 * Modified for EFG by Kasiedu
	 */



import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGRowMapperInterface;


	/**
	 * This class connects to a database and dumps all 
	 * the tables and contents out to stdout in the 
	 * form of
	 * a set of SQL executable statements
	 */
	public class ExportData {
		
		private JdbcTemplate jdbcTemplate;
		private EFGRowMapperInterface rowMapper;
		
		private EFGDBMetadata dbMetadata;
		static Logger log = null;
		static {
			try {
				log = Logger.getLogger(ExportData.class);
			} catch (Exception ee) {
			}
		}
		/**
		 * 
		 * @param dbObject
		 */
		public ExportData(DBObject dbObject) {			
			this.rowMapper = SpringFactory.getRowMapper();
			this.jdbcTemplate = EFGRDBImportUtils.getJDBCTemplate(dbObject);
		}
		/**
		 * 
		 *
		 */
		public ExportData() {	
			this.jdbcTemplate = new JdbcTemplate(EFGRDBUtils.getDatasource());
			this.rowMapper = SpringFactory.getRowMapper();
		}
		/**
		 * @param string
		 * @return
		 */
		private String escapeQuotes(String string) {
			if (string == null) {
				return null;
			}
			
			return string.trim().replaceAll("\"", "\\\\\"");
		}
		/**
		 * 
		 * @param string
		 * @return
		 */
		public String escapeCharacters(String string) {
			if (string == null) {
				return null;
			}
			for(int i = 0; i < SQLEscapeCharacters.keyCharacters.size();i++) {
				String current = (String)SQLEscapeCharacters.keyCharacters.get(i);
				string = string.replaceAll(current, "\\\\"+current);
			}
			return string;
		}
	
		/**
		 * 
		 * @param props
		 * @return
		 */
	    public  String dumpDB(String tableName) {
	    	
	    	
	        // Default to not having a quote character	       
	        try {
	        	this.dbMetadata = new EFGDBMetadata(tableName);
	        	StringBuffer result = new StringBuffer();
	        	result.append(
	        			(String)JdbcUtils.extractDatabaseMetaData
	        			(
	        			this.jdbcTemplate.getDataSource(),
	        			this.dbMetadata
	        			)
	        			);
	        	
	        	String query = "SELECT * FROM "+tableName;
	        	result.append(dumpTable(tableName, query));
	        	return result.toString();
	  
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	           log.error(e.getMessage());
	        }	      
	        return null;
	    }
		/**
		 * 
		 * @param metaData
		 * @return
		 * @throws SQLException
		 * @throws Exception
		 */
	    private  String getColumnNames( 
	    		SqlRowSetMetaData metaData) throws SQLException, Exception {
	    	StringBuffer result = new StringBuffer();
	    	String[] columnNames = metaData.getColumnNames(); 
           for (int i=0; i<columnNames.length; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                String columnName =columnNames[i];
                if (columnName== null) {
                    throw new Exception("Column name cannot be null");
                }
                   result.append(columnName);
            }
           return result.toString();
	    }


	    /** dump this particular table to the string buffer 
	     * @param query
	     * */
	    public String dumpTable( String tableName, String query)throws Exception {
	    	
	       
	    	StringBuffer result = new StringBuffer();
			SqlRowSet rs = this.rowMapper.mapRows(
					this.jdbcTemplate, query);
	
			
			SqlRowSetMetaData metaData = rs.getMetaData(); 
			 int columnCount = metaData.getColumnCount();
	        String columnNames = getColumnNames(metaData);
	        result.append("\n--");
	        result.append("\n-- Dumping data for table ");
	        result.append(tableName);
	        result.append("\n--\n");
	        result.append("/*!40000 ALTER TABLE ");
	        result.append(tableName);
	        result.append(" DISABLE KEYS */;");
	
	
	        // Now we can output the actual data
	       
	        result.append("\nLOCK TABLES " + tableName + " WRITE;\n");
	       
	        while (rs.next()) {
	            result.append("INSERT INTO ");
	            result.append(tableName.toLowerCase());
	            result.append(" (");
	            result.append(columnNames);
	            result.append(") ");
	            result.append(" VALUES (");
	            
	            for (int i=0; i<columnCount; i++) {
	                if (i > 0) {
	                    result.append(", ");
	                }
	                Object value = rs.getObject(i+1);
	                if (value == null) {
	                    result.append("NULL");
	                } else {
	                	if(value instanceof byte[]){//blobs are handled elsewhere
	                		result.append("NULL");
	                	}
	                	else{
	                    String outputValue = value.toString();
	                    outputValue = escapeQuotes(outputValue);
	                   
	                    result.append("\"");
	                    result.append(outputValue);
	                    result.append("\"");
	                	}	                        
	                }
	            }
	            result.append(");\n");
	        }
	        result.append("\nUNLOCK TABLES;");
	        return replaceString(result.toString());
		}
		/**
		 * @param queryN
		 * @param text
		 * @return
		 */
		private String replaceString(
				String queryN) {
			return queryN.replaceAll(
					EFGImportConstants.MEDIUMTEXT,
					EFGImportConstants.TEXT);
		}	
	}
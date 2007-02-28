/**
 * 
 */
package project.efg.Imports.efgImportsUtil;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.MetaDataAccessException;

import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class EFGDBMetadata implements DatabaseMetaDataCallback {
	
	private String tableName;
	
	public String getTableName() {
		return this.tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 
	 */
	public EFGDBMetadata(String tableName) {
		this.setTableName(tableName);
	}
	private String getPrimaryKey(DatabaseMetaData dbMetaData) throws SQLException {
	StringBuffer result= new StringBuffer();
    try {
    	
        ResultSet primaryKeys = 
        	dbMetaData.getPrimaryKeys(null, null, this.getTableName());
        // What we might get:
        // TABLE_CAT String => table catalog (may be null)
        // TABLE_SCHEM String => table schema (may be null)
        // TABLE_NAME String => table name
        // COLUMN_NAME String => column name
        // KEY_SEQ short => sequence number within primary key
        // PK_NAME String => primary key name (may be null)
        String primaryKeyName = null;
        StringBuffer primaryKeyColumns = new StringBuffer();
        while (primaryKeys.next()) {
            String thisKeyName = primaryKeys.getString("PK_NAME");
            if ((thisKeyName != null && primaryKeyName == null)
                    || (thisKeyName == null 
                    		&& primaryKeyName != null)
                    || (thisKeyName != null 
                    		&& ! thisKeyName.equals(primaryKeyName))
                    || (primaryKeyName != null 
                    		&& ! primaryKeyName.equals(thisKeyName))) {
                // the keynames aren't the same, so output all that we 
            	//have so far (if anything)
                // and start a new primary key entry
                if (primaryKeyColumns.length() > 0) {
                    // There's something to output
                    result.append(",\n    PRIMARY KEY ");
                    result.append("(");
                    result.append(primaryKeyColumns.toString());
                    result.append(")");
                }
                // Start again with the new name
                primaryKeyColumns = new StringBuffer();
                primaryKeyName = thisKeyName;
            }
            // Now append the column
            if (primaryKeyColumns.length() > 0) {
                primaryKeyColumns.append(", ");
            }
            primaryKeyColumns.append(primaryKeys.getString("COLUMN_NAME"));
        }
        if (primaryKeyColumns.length() > 0) {
            // There's something to output
            result.append(",\n    PRIMARY KEY ");
            result.append(" ("+primaryKeyColumns.toString()+")");
        }
    } catch (SQLException e) {
        // NB you will get this exception with the JDBC-ODBC link because it says
        // [Microsoft][ODBC Driver Manager] Driver does not support this function
        System.err.println("Unable to get primary keys for table "+
        		tableName+" because "+e);
    }
    
    return result.toString();
	}
	private String getColumns(DatabaseMetaData dbMetaData) throws SQLException {
		StringBuffer resultsColumn = new StringBuffer();
		String columnNameQuote = "";
		ResultSet tableMetaData = dbMetaData.getColumns(null, null, this.getTableName(), "%");
        boolean firstLine = true;
        while (tableMetaData.next()) {
            if (firstLine) {
                firstLine = false;
            } else {
                // If we're not the first line, then finish the previous line 
            	//with a comma
                resultsColumn.append(",\n");
            }
            String columnName = tableMetaData.getString("COLUMN_NAME");
            String columnType = tableMetaData.getString("TYPE_NAME");
            boolean isMedium = false;
            if(columnType.equalsIgnoreCase(EFGImportConstants.MEDIUMTEXT)) {
            	columnType = "TEXT";
            	isMedium = true;
            }
            else if(columnType.toLowerCase().indexOf("medium") > -1) {
            	int ind = columnType.toLowerCase().indexOf("medium");
            	columnType = columnType.substring(ind+6,columnType.length());
            	isMedium = true;
            }
            // WARNING: this may give daft answers for some types on some 
            //databases (eg JDBC-ODBC link)
            int columnSize = tableMetaData.getInt("COLUMN_SIZE");
            String nullable = tableMetaData.getString("IS_NULLABLE");
            String nullString = "NULL";
            if ("NO".equalsIgnoreCase(nullable)) {
                nullString = "NOT NULL";
            }
            resultsColumn.append("    ");
            resultsColumn.append(columnNameQuote);
            resultsColumn.append(columnName);
            resultsColumn.append(columnNameQuote);
            resultsColumn.append(" ");
            
            resultsColumn.append(columnType);
            if(!isMedium) {
            	resultsColumn.append(" (");
            	resultsColumn.append(columnSize);
            	resultsColumn.append(")");
            }
            resultsColumn.append(" ");
            resultsColumn.append(nullString);
        }
        tableMetaData.close();
        return resultsColumn.toString();
		
	}
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.support.DatabaseMetaDataCallback#processMetaData(java.sql.DatabaseMetaData)
	 */
	public Object processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {
			StringBuffer results = new StringBuffer();

		/*	--
			-- Table structure for table `efg_glossary_tables`
			--

			DROP TABLE IF EXISTS `efg_glossary_tables`;*/
			results.append("\n\n-- ");
			results.append("\n\n-- Table structure for table `"+this.getTableName() + "`");
			results.append("\n\n-- ");
			results.append("\nDROP TABLE IF EXISTS ");
			results.append(this.getTableName());
			results.append(";");
			results.append("\nCREATE TABLE "+this.getTableName()+" (\n");

			results.append(this.getColumns(dbmd));
			
			results.append(this.getPrimaryKey(dbmd));
			 results.append("\n);\n");
	        return results.toString();
	}
	
}

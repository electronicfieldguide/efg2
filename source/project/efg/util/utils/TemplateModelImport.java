/**
 * 
 */
package project.efg.util.utils;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;


/**
 * @author jacob.asiedu
 *
 */
public class TemplateModelImport extends TemplateModelHandler{
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(TemplateModelImport.class);
		} catch (Exception ee) {
		}
	}
	
	private DBObject dbObject;
	private JdbcTemplate jdbcTemplate;
	public TemplateModelImport(DBObject dbObject){
		super();
		
		this.dbObject = dbObject;
		this.jdbcTemplate = EFGRDBImportUtils.getJDBCTemplate(this.dbObject);
		
	}
	public boolean executePreparedStatement(String updateStatement,
			PreparedStatementSetter p )throws Exception{
		try{
			this.jdbcTemplate.update( updateStatement,p);
			return true;
		}
		catch(Exception ee){
		
			log.error(ee.getMessage());
			throw ee;
		}	
	}
	public int executeStatement(String query) throws Exception {

		return this.jdbcTemplate.update(query);
	}
	public java.util.List executeQueryForList(String query, int numberOfColumns)
	throws Exception {
		return EFGRDBImportUtils.executeQueryForList(this.jdbcTemplate, query,
		numberOfColumns);
	}
	public  SqlRowSet executeQueryForRowSet(String queryString) throws Exception{
		try{
			return this.jdbcTemplate.queryForRowSet(queryString);
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			throw ee;
		}
		
	}
	/* (non-Javadoc)
	 * @see project.efg.util.utils.TemplateModelHandler#executeBatchPreparedStatement(java.lang.String, org.springframework.jdbc.core.BatchPreparedStatementSetter)
	 */
	public boolean executeBatchPreparedStatement(String updateStatement, 
			BatchPreparedStatementSetter p) {
		try{
			this.jdbcTemplate.batchUpdate(updateStatement,p);
			return true;
		}
		catch(Exception ee){
			log.error(ee.getMessage());
		}	
		return false;
	}
}

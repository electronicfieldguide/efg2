/**
 * 
 */
package project.efg.Imports.rdb;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.util.TemplateModelHandler;

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
	public int executeStatement(String query) throws Exception {

		return this.jdbcTemplate.update(query);
	}
	public java.util.List executeQueryForList(String query, int numberOfColumns)
	throws Exception {
		return EFGRDBImportUtils.executeQueryForList(this.jdbcTemplate, query,
		numberOfColumns);
	}
	

}

/**
 * 
 */
package project.efg.util.utils;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.QueryExecutorInterface;


/**
 * @author jacob.asiedu
 *
 */
public class TemplateModelExport extends TemplateModelHandler{
	private QueryExecutorInterface queryExecutor;
	public TemplateModelExport(){
		super();
		this.queryExecutor = SpringFactory.getQueryExecutor();
	}
	/**
	 * 
	 */
	public int executeStatement(String query) throws Exception {
		return this.queryExecutor.executeUpdate(query);
		
	}
	public boolean executePreparedStatement(String updateStatement,
			PreparedStatementSetter p )throws Exception{
		try{
			return this.queryExecutor.executePreparedStatement( updateStatement,p);
		}
		catch(Exception ee){	
			throw ee;
		}
	}
	/**
	 * 
	 */
	public java.util.List executeQueryForList(String query, 
			int numberOfColumns)
	throws Exception {
		return this.queryExecutor.executeQueryForList(query,numberOfColumns);
	}
	/* (non-Javadoc)
	 * @see project.efg.util.utils.TemplateModelHandler#executeBatchPreparedStatement(java.lang.String, org.springframework.jdbc.core.BatchPreparedStatementSetter)
	 */
	public boolean executeBatchPreparedStatement(String updateStatement, 
			BatchPreparedStatementSetter p) {
		try{
			return this.queryExecutor.executeBatchUpdate(updateStatement,p);
		}
		catch(Exception ee){
			
		}	
		return false;
	}
	public  SqlRowSet executeQueryForRowSet(String queryString)throws Exception{
		try {
			return this.queryExecutor.executeQueryForRowSet(queryString);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}

/**
 * 
 */
package project.efg.util.interfaces;

import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author jacob.asiedu
 *
 */
public interface QueryExecutorInterface {

	public abstract int executeUpdate(String query);

	public abstract List executeQueryForList(String query, int numberOfColumns)
			throws Exception;

	public abstract SqlRowSet executeQueryForRowSet(String query)
			throws Exception;
	
	public abstract boolean executePreparedStatement(String preparedStatementQuery,
			PreparedStatementSetter setter);
	
	public abstract  JdbcTemplate getJDBCTemplate();

	/**
	 * @param updateStatement
	 * @param p
	 */
	public abstract boolean executeBatchUpdate(String updateStatement, BatchPreparedStatementSetter p);

}
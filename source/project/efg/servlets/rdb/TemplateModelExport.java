/**
 * 
 */
package project.efg.servlets.rdb;

import project.efg.util.TemplateModelHandler;

/**
 * @author jacob.asiedu
 *
 */
public class TemplateModelExport extends TemplateModelHandler{
	private QueryExecutor queryExecutor;
	public TemplateModelExport(){
		super();
		this.queryExecutor = new QueryExecutor();
	}
	/**
	 * 
	 */
	public int executeStatement(String query) throws Exception {
		queryExecutor.executeQueryForList(query,1);
		return -1;
	}
	/**
	 * 
	 */
	public java.util.List executeQueryForList(String query, 
			int numberOfColumns)
	throws Exception {
		return queryExecutor.executeQueryForList(query,numberOfColumns);
	}
}

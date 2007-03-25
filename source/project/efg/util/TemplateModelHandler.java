/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;




/**
 * @author kasiedu
 * 
 */
public abstract class TemplateModelHandler{
	

	protected String templateName;
	


	public TemplateModelHandler() {
		this.templateName = 
			EFGImportConstants.TEMPLATE_TABLE.toLowerCase();
		
	}
	



	// Template Objects
	/**
	 * Create EFGRDTable if it does not already exists
	 * 
	 * @return true if query executed successfully.
	 */
	
	/**
	 *  Remove this key form the database
	 *  @param datasourceName - Remove all keys created for this data source
	 *  
	 * @see project.efg.util.TemplateModelHandler#removeFromDB(java.lang.String)
	 */
	public boolean removeFromDB(String datasourceName) {
		if(!doChecks(datasourceName)){
			return false;
		}
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM ");
		query.append(this.templateName);
		query.append(" WHERE ");
		query.append(EFGImportConstants.DATASOURCE_NAME);
		query.append("=");
		query.append("\"");
		query.append(datasourceName.toLowerCase());
		query.append("\"");
		try{
			this.executeStatement(query.toString());
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 *  Remove this key form the database
	 *  @param datasourceName - Remove all keys created for this data source
	 *  
	 * @see project.efg.util.TemplateModelHandler#removeFromDB(java.lang.String)
	 */
	public boolean removeTemplateFromDB(String datasourceName, String templateDisplayName) {
		if(!doChecks(datasourceName)){
			return false;
		}
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM ");
		query.append(this.templateName.toLowerCase());
		query.append(" WHERE ");
		query.append(EFGImportConstants.DATASOURCE_NAME);
		query.append("=");
		query.append("\"");
		query.append(datasourceName.toLowerCase());
		query.append(" and ");
		query.append(EFGImportConstants.DISPLAY_NAME);
		query.append("=");
		query.append("\"");
		query.append(templateDisplayName);
		query.append("\"");
		try{
			this.executeStatement(query.toString());
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 * Update this key with this object
	 * @param key
	 * @param displayName
	 * @return true if update was successful
	 */
	public boolean changeDisplayName(String datasourceName, 
			String displayName){
		
		if(!doChecks(datasourceName)){
			return false;
		}
		if(!doChecks(displayName)){
			return false;
		}
		
	
	
		
		StringBuffer query = new StringBuffer();
		query.append("UPDATE ");
		query.append(this.templateName.toLowerCase());
		query.append(" SET ");
		query.append(EFGImportConstants.DISPLAY_NAME);
		query.append("=");
		query.append("\"");
		query.append(displayName);
		query.append("\"");
		query.append(" WHERE ");
		query.append(EFGImportConstants.DATASOURCE_NAME);
		query.append("=");
		query.append("\"");
		query.append(datasourceName.toLowerCase());
		query.append("\"");
		try {
			this.executeStatement(query.toString());
			return true;
		
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
		}
		return false;
	}
	private boolean createEFG2TemplatesTable() {
		
		if((this.templateName == null) || 
				(this.templateName.trim().equals(""))){
			
			return false;
		}
			
			StringBuffer query = new StringBuffer();
			
			// PUT IN PROPERTIES FILE
			query.append("CREATE TABLE IF NOT EXISTS ");
			query.append(templateName.toLowerCase());
		
			query.append("( ");
			query.append(EFGImportConstants.TEMPLATE_KEY);
			query.append(" VARCHAR(255) not null,");
			query.append(EFGImportConstants.GUID);
			query.append(" VARCHAR (255), ");
			query.append(EFGImportConstants.DISPLAY_NAME);
			query.append(" VARCHAR(255), ");
			query.append(EFGImportConstants.DATASOURCE_NAME);
			query.append(" VARCHAR(255), ");
			query.append(EFGImportConstants.TEMPLATE_NAME);
			query.append(" VARCHAR(255) "); 
			query.append(")");
			try{
				this.executeStatement(query.toString());
				return true;
			}
			catch (Exception e) {
				
			}
			return true;
}
	/**
	 * Associate this key to this template in the database
	 *@param key  A unique key
	 *@param templateObject - The object associated with this key
	 *@return true if successful
	 * @see project.efg.util.TemplateModelHandler#add2DB(java.lang.String, project.efg.util.TemplateObject)
	 */
	public boolean add2DB(String key, TemplateObject templateObject) {
		if(!doChecks(key)){
			
			return false;
		}
		
		if(templateObject == null){
			
			return false;
		}
		
		EFGDisplayObject displayObject = templateObject.getDisplayObject();
		
		if(displayObject == null){
			
			return false;
		}
		String datasourceName = displayObject.getDatasourceName();
		if((datasourceName == null) || (datasourceName.trim().equals(""))){
			
			return false;
		}
		String guid = templateObject.getGUID();
		if(guid == null){
			guid = "";
		}
		String tempName = templateObject.getTemplateName();
		if(tempName == null){
			tempName = "";
		}
		
		String displayName = displayObject.getDisplayName();
		if(displayName == null){
			displayName = "";
		}
		createEFG2TemplatesTable();
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO ");
		query.append(this.templateName.toLowerCase()); 
		query.append(" "); 
		query.append("( ");
		query.append(getHeaderQuery());
		query.append(")");
		query.append(" VALUES(");
		query.append("\"");
		query.append(key);
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(guid);
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(displayName);
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(datasourceName.toLowerCase());
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(tempName);
		query.append("\"");
		query.append(")");
		
		try{
			this.executeStatement(query.toString());
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
		
	}
	private Hashtable getTemplateObjectMap(String mapLocation) {
		String mutex = "";
		synchronized (mutex) {
			ObjectInputStream in = null;
			Hashtable currentMap = null;
			
			File file = new File(mapLocation);
			if (!file.exists()) {
				
				return null;
			}
		
		
				// lock file
				try {
					in = new ObjectInputStream(new FileInputStream(file));
					currentMap = (Hashtable) in.readObject();
					in.close();
					return currentMap;
					
				} catch (Exception ee) {
					ee.printStackTrace();
					LoggerUtilsServlet.logErrors(ee);
				}
				return null;

			
		}
	}
	/**
	 * Associate this key to this template in the database
	 *@param key  A unique key
	 *@param templateObject - The object associated with this key
	 *@return true if successful
	 * @see project.efg.util.TemplateModelHandler#add2DB(java.lang.String, project.efg.util.TemplateObject)
	 */
	public void loadHashTable2DB() {
		String key = null;
		TemplateObject templateObject = null;
		createEFG2TemplatesTable();
		
		String catHome = EFGUtils.getServerHome();
		if(catHome == null){
			return;
		}
		createEFG2TemplatesTable();
		StringBuffer buffer = new StringBuffer();
		buffer.append(catHome);
		buffer.append("/");
		buffer.append(EFGImportConstants.EFG_WEB_APPS);
		buffer.append("/");
		buffer.append(EFGImportConstants.EFG_APPS);
		buffer.append("/");
		buffer.append("WEB-INF");
		buffer.append("/");
		buffer.append("templateMap.out");
		Hashtable table = this.getTemplateObjectMap(buffer.toString());
		if(table == null){
			return;
		}
		Iterator mapIter = table.keySet().iterator();
		
		while (mapIter.hasNext()) {
			key = (String) mapIter.next();
			templateObject= (TemplateObject)table.get(key);
		
		//get the stuff and unserialize it
		if(templateObject == null){
			
			continue;
		}
		
		EFGDisplayObject displayObject = templateObject.getDisplayObject();
		String datasourceName = displayObject.getDatasourceName();
	
		String guid = templateObject.getGUID();
		if(guid == null){
			guid = "";
		}
		String tempName = templateObject.getTemplateName();
		if(tempName == null){
			tempName = "";
		}
		
		String displayName = displayObject.getDisplayName();
		if(displayName == null){
			displayName = "";
		}
		
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO ");
		query.append(this.templateName.toLowerCase()); 
		query.append(" "); 
		query.append("( ");
		query.append(getHeaderQuery());
		query.append(")");
		query.append(" VALUES(");
		query.append("\"");
		query.append(key);
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(guid);
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(displayName);
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(datasourceName.toLowerCase());
		query.append("\"");
		query.append(",");
		query.append("\"");
		query.append(tempName);
		query.append("\"");
		query.append(")");
		
		try{
			this.executeStatement(query.toString());
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
		try{
		File file = new File(buffer.toString());
		if(file.exists()){
			file.delete();
		}
		}
		catch(Exception ee){
			
		}
	}
	public boolean removeGuidFromTable(String guid){

		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM ");
		query.append(this.templateName.toLowerCase());
		query.append(" WHERE ");
		query.append(EFGImportConstants.GUID);
		query.append("='");
		query.append(guid);
		query.append("'");
		try {
			this.executeStatement(query.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Return all of the keys in the table
	 * @return
	 */
	public Hashtable getAll() {
		
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		query.append(getHeaderQuery());
		query.append(" FROM ");
		query.append(this.templateName.toLowerCase());
		query.append(" ORDER BY ");
		query.append(EFGImportConstants.DISPLAY_NAME);
		Hashtable table = new Hashtable();
		try{
			
			List list =  this.executeQueryForList(query.toString(),5);
			
			for(int i = 0; i < list.size(); i ++){
			
				String key = 
					((EFGQueueObjectInterface) list.get(i)).getObject(0);
				String guid = 
					((EFGQueueObjectInterface) list.get(i)).getObject(1);
				String displayName = 
					((EFGQueueObjectInterface) list.get(i)).getObject(2);
				String dsName = 
					((EFGQueueObjectInterface) list.get(i)).getObject(3);			
				String tempName = 
					((EFGQueueObjectInterface) list.get(i)).getObject(4);
				
				TemplateObject tempObject = new TemplateObject();
				tempObject.setGUID(guid);
				tempObject.setTemplateName(tempName);
				 
				EFGDisplayObject dop = new EFGDisplayObject();
				dop.setDatasourceName(dsName);
				dop.setDisplayName(displayName);
				tempObject.setDisplayObject(dop);
				table.put(key,tempObject);
		
			}
			return table;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Return a list of keys that contains te given key
	 * 
	 * @param key
	 * @return - a list of keys that contains the given key.
	 */
	public List getKeys(String datasourceName) {
		if(!doChecks(datasourceName)){
			return null ;
		}
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		query.append(EFGImportConstants.TEMPLATE_KEY);
		query.append(" FROM ");
		query.append(this.templateName);
		query.append(" WHERE ");
		query.append(EFGImportConstants.DATASOURCE_NAME);
		query.append("=");
		query.append("\"");
		query.append(datasourceName.toLowerCase());
		query.append("\"");
		
		
		try{
			return this.executeQueryForList(query.toString(),1);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
/**
 * Get the TemplateObject associated with this key
 * @param key
 * @return the TemplateObject associated with this key
 */
	public TemplateObject getFromDB(String key) {
		
		if(!doChecks(key)){
			return null ;
		}
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		query.append(EFGImportConstants.GUID);
		query.append(",");
		query.append(EFGImportConstants.DISPLAY_NAME);
		query.append(",");
		query.append(EFGImportConstants.DATASOURCE_NAME);
		query.append(",");
		query.append(EFGImportConstants.TEMPLATE_NAME);
		query.append(" FROM ");
		query.append(this.templateName);
		query.append(" WHERE ");
		query.append(EFGImportConstants.TEMPLATE_KEY);
		query.append("=");
		query.append("\"");
		query.append(key);
		query.append("\"");
		
		
		
		try{
			List list =  this.executeQueryForList(query.toString(),4);
			if(list.size() > 0){
				String guid = ((EFGQueueObjectInterface) list.get(0)).getObject(0);
				String displayName = ((EFGQueueObjectInterface) list.get(1)).getObject(0);
				String dsName = ((EFGQueueObjectInterface) list.get(2)).getObject(0);
				
				String tempName = ((EFGQueueObjectInterface) list.get(3)).getObject(0);
				 TemplateObject tempObject = new TemplateObject();
				 tempObject.setGUID(guid);
				 tempObject.setTemplateName(tempName);
				 
				 EFGDisplayObject dop = new EFGDisplayObject();
				 dop.setDatasourceName(dsName);
				 dop.setDisplayName(displayName);
				 tempObject.setDisplayObject(dop);
				 return tempObject;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public abstract int executeStatement(String query)throws Exception;
	public abstract java.util.List executeQueryForList(String query, 
			int numberOfColumns)throws Exception;
	/**
	 * @return
	 */
	protected static String getHeaderQuery() {
		
		StringBuffer query = new StringBuffer();
		
		query.append(EFGImportConstants.TEMPLATE_KEY);
		query.append(",");
		query.append(EFGImportConstants.GUID);
		query.append(",");
		query.append(EFGImportConstants.DISPLAY_NAME);
		query.append(",");
		query.append(EFGImportConstants.DATASOURCE_NAME);
		query.append(",");
		query.append(EFGImportConstants.TEMPLATE_NAME);
		
		return query.toString();
	}
	/**
	 * @return true if the key is a valid key
	 */
	private boolean doChecks(String key) {
		if((this.templateName == null) || 
				(this.templateName.trim().equals(""))){
			
			return false;
		}
		if((key == null) || (key.trim().equals(""))){ 
			
			return false;
		}
		return true;
	}
	
	
}

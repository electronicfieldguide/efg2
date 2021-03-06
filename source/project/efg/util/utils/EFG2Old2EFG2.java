/**
 * 
 */
package project.efg.util.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Iterator;

import project.efg.util.interfaces.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class EFG2Old2EFG2 {

	/**
	 * Associate this key to this template in the database
	 *@param key  A unique key
	 *@param templateObject - The object associated with this key
	 *@return true if successful
	 */
	public void loadHashTable2DB(String templateHome, Writer writer) {
		String key = null;
		TemplateObject templateObject = null;
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(templateHome);
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
			
			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO efg.efg_template_tables "); 
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
			query.append(datasourceName);
			query.append("\"");
			query.append(",");
			query.append("\"");
			query.append(tempName);
			query.append("\"");
			query.append(")");
			query.append(";");
			query.append("\n");
			try {
				writer.write(query.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
	}
	private String getHeaderQuery() {		
		StringBuilder query = new StringBuilder();		
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
			}
			return null;		
		}
	}
  public static void main(String[] args) throws IOException {
	EFG2Old2EFG2 efg2 = new EFG2Old2EFG2();
	Writer writer = new FileWriter("MapObject.txt");	
	efg2.loadHashTable2DB("C:\\JavaCode\\efg2\\", writer);
	writer.close();
  }
}

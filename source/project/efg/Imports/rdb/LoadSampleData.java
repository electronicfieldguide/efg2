/**
 * 
 */
package project.efg.Imports.rdb;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgImpl.LoginDialog;
import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.efgInterface.ImportBehavior;
import project.efg.Imports.factory.EFGDatasourceObjectFactory;
import project.efg.Imports.factory.EFGDatasourceObjectListFactory;
import project.efg.Imports.factory.ImportBehaviorFactory;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 * 
 */
public class LoadSampleData {
	private JdbcTemplate jdbcTemplate;

	private EFGDatasourceObjectListInterface list;

	private DBObject dbObject;

	private EFGDatasourceObjectInterface datasource;

	private ImportBehavior isUpdate;

	private String searchableField, mediaResourceField,listsField, newDisplayName;

	private String metadataTableName;

	private StringBuffer messageBuffer;
	private boolean isError = false;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(LoadSampleData.class);
		} catch (Exception ee) {
		}
	}
	
	

	/**
	 * 
	 */
	public LoadSampleData(DBObject dbObject) {
		this.messageBuffer = new StringBuffer();
		this.dbObject = this.getClone(dbObject);
		this.init();
	}
	/**
	 * @param dbObject2
	 * @return
	 */
	private DBObject getClone(DBObject dbObject2) {
		String url = EFGImportConstants.EFGProperties
		.getProperty("dburl");
		return dbObject2.clone(url);
	}
	public String getErrorBuffer(){
		return this.messageBuffer.toString();
	}
	public boolean isError(){
		return this.isError;
	}
	private boolean loadFile(){
		try{
		URL url = this.getClass().getResource(
				EFGImportConstants.EFGProperties.getProperty(
						EFGImportConstants.SAMPLE_DATA_LOCATION)
						);
		String fileName = URLDecoder.decode(url.getFile(),"UTF-8");
		File dir = new File(fileName);
		URI file=dir.toURI();
		
		
		
		fileName = EFGUtils.getName(file);
		int index = fileName.lastIndexOf(".");
		String displayName = fileName;
		if (index > -1) {
			displayName = fileName.substring(0, index);
		}

		this.datasource = EFGDatasourceObjectFactory.getEFGDatasourceObject();
		this.datasource.setDisplayName(displayName);
		this.datasource.setDataName(file);
		}
		catch(Exception ee){
			String message = ee.getMessage();
			log.error(message);
			this.messageBuffer.append("\n");
			this.messageBuffer.append(message);
			this.isError = true;
			return false;
		}
		return true;
	
	}
	private void init() {
		if (this.jdbcTemplate == null) {
			this.jdbcTemplate = EFGRDBImportUtils
					.getJDBCTemplate(this.dbObject);
		}
		if(this.loadFile()){
			this.list = EFGDatasourceObjectListFactory
				.getEFGObjectList(this.dbObject);
			String behaviorType =
				EFGImportConstants.EFGProperties.
				getProperty("importOnlyBehavior");
			
			this.isUpdate = ImportBehaviorFactory.getImportBehavior(
				this.list,
				this.datasource, 
				behaviorType
				);
		
			this.readNewDisplayName();
			this.readFields();
		}
	
	}

	/**
	 * 
	 */
	private void readFields() {

		this.mediaResourceField = EFGImportConstants.EFGProperties
				.getProperty(EFGImportConstants.SAMPLE_MEDIA_RESOURCE_FIELD);
		this.searchableField = EFGImportConstants.EFGProperties
				.getProperty(EFGImportConstants.SAMPLE_SEARCHABLE_FIELD);
		this.listsField=EFGImportConstants.EFGProperties
		.getProperty(EFGImportConstants.SAMPLE_LISTS_FIELD); 

	}

	/**
	 * 
	 */
	private void readNewDisplayName() {
		this.newDisplayName = EFGImportConstants.EFGProperties
				.getProperty(EFGImportConstants.SAMPLE_NEW_DISPLAY_NAME);

	}

	public boolean loadData() {
		if(this.list == null){
			this.isError = true;
			return false;
		}
		if (this.list.addEFGDatasourceObject(this.datasource, this.isUpdate)) {
			if (this.editMetadata()) {
				if (this.changeDisplayName()) {
					return true;

				}
			}
		}
		this.isError = true;
		return false;
	}

	private String getMetadataTableName() {

		if ((this.metadataTableName == null)
				|| (this.metadataTableName.trim().equals(""))) {
			this.metadataTableName = this.getTableName();
		}
		return this.metadataTableName;

	}

	private String getTableName() {
		String mutex = "";
		synchronized (mutex) {
			String tableName = null;
			try {
				StringBuffer query = new StringBuffer();
				query.append("SELECT DS_METADATA");
				query.append(" FROM ");
				query.append(EFGImportConstants.EFG_RDB_TABLES);
				query.append(" WHERE DISPLAY_NAME = \"");
				query.append(this.datasource.getDisplayName());
				query.append("\"");
				java.util.List list = EFGRDBImportUtils.executeQueryForList(
						this.jdbcTemplate, query.toString(), 1);

				for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
					EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
							.next();
					tableName = queue.getObject(0);

					break;
				}
			} catch (Exception rr) {
				String message = rr.getMessage();
				log.error(message);
				this.messageBuffer.append("\n");
				this.messageBuffer.append(message);
				this.isError = true;
			}
			return tableName;
		}
	}

	private boolean executeSearchableFieldUpdateQuery() {

		StringBuffer buffer = new StringBuffer("UPDATE ");
		buffer.append(this.getMetadataTableName());
		buffer.append(" SET ");
		buffer.append(EFGImportConstants.SEARCHABLE);
		buffer.append("='true',");
		buffer.append(EFGImportConstants.NARRATIVE);
		buffer.append("='false',");
		buffer.append(EFGImportConstants.CATEGORICAL);
		buffer.append("='true' WHERE ");
		buffer.append(parseField(this.searchableField));
		try {
			this.jdbcTemplate.update(buffer.toString());
			return true;
		} catch (Exception e) {
			String message =e.getMessage();
			log.error(message);
			this.messageBuffer.append("\n");
			this.messageBuffer.append(message);

		}
		this.isError = true;
		return false;

	}

	private boolean executeMediaResourceUpdateQuery() {

		StringBuffer buffer = new StringBuffer("UPDATE ");
		buffer.append(this.getMetadataTableName());
		buffer.append(" SET ");
		buffer.append(EFGImportConstants.NARRATIVE);
		buffer.append("='false',");
		buffer.append(EFGImportConstants.MEDIARESOURCE);
		buffer.append("='true' WHERE ");
		
		buffer.append(parseField(this.mediaResourceField));
		
		try {
			this.jdbcTemplate.update(buffer.toString());
			return true;
		} catch (Exception e) {
			String message = e.getMessage();
			log.error(message);
			this.messageBuffer.append("\n");
			this.messageBuffer.append(message);

		}
		this.isError = true;
		return false;

	}
	private boolean executeListsUpdateQuery() {

		StringBuffer buffer = new StringBuffer("UPDATE ");
		buffer.append(this.getMetadataTableName());
		buffer.append(" SET ");
		buffer.append(EFGImportConstants.NARRATIVE);
		buffer.append("='false',");
		buffer.append(EFGImportConstants.ISLISTS);
		buffer.append("='true' WHERE ");
		
		buffer.append(parseField(this.listsField));
		
		try {
			this.jdbcTemplate.update(buffer.toString());
			return true;
		} catch (Exception e) {
			String message = e.getMessage();
			log.error(message);
			this.messageBuffer.append("\n");
			this.messageBuffer.append(message);

		}
		this.isError = true;
		return false;

	}

	/**
	 * @param string
	 * @return
	 */
	private String parseField(String string) {
		StringBuffer buffer = new StringBuffer();
		String[] resources = string.split(EFGImportConstants.COMMASEP);
		for(int i = 0; i < resources.length;i++){
			if(i != 0){
				buffer.append(" OR ");
			}
			buffer .append(EFGImportConstants.NAME);
			buffer.append("='");
			buffer.append(resources[i].trim());
			buffer.append("'");
		}
		return buffer.toString();
	}

	private boolean editMetadata() {
		this.getMetadataTableName();
		if ((this.metadataTableName != null)
				&& (!this.metadataTableName.trim().equals(""))) {
			if (this.executeSearchableFieldUpdateQuery()) {
				if (this.executeMediaResourceUpdateQuery()) {
					if(this.executeListsUpdateQuery()){
						return true;
					}
				}
			}
		}
		this.isError = true;
		return false;
	}

	private boolean changeDisplayName() {
		return this.list.replaceDisplayName(this.datasource.getDisplayName(),
				this.newDisplayName);

	}
	public static void main(String[] args){
		LoginDialog dialog = new LoginDialog(new JFrame(), "Login", true);
		dialog.setVisible(true);

		if (dialog.isSuccess()) {
			String m_loginName = dialog.getLoginName();
			String m_password = new String(dialog.getPassword());
			String url = EFGImportConstants.EFGProperties
					.getProperty("dburl");

			// factory ?
			DBObject dbObject = new DBObject(url, m_loginName, m_password);
			LoadSampleData sampleData = new LoadSampleData(dbObject);
			sampleData.loadData();
			if (sampleData.isError()) {
				JOptionPane
						.showMessageDialog(
								null,
								"Error in Loading Sample Data. View logs for more details",
								"Error in Loading Sample Data",
								JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			JOptionPane
			.showMessageDialog(
					null,
					"Sample Data Loaded Successfully",
					"Success in Loading Sample Data",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}

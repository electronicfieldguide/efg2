/**
 * 
 */
package project.efg.client.utils.nogui;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.EFGRDBImportUtils;
import project.efg.util.utils.EFGUtils;

/**
 * @author kasiedu
 * 
 */
public abstract class DataChecker {

	protected JdbcTemplate jdbcTemplate;

	protected String metadatasourceName;

	protected String datasourceName;

	protected String efgRDBTable;

	protected String displayName;

	

	protected DBObject dbObject;

	private boolean isReady = false;

	

	static Logger log = null;

	private  URL tempFilesHome;

	protected int numberOfErrors = 0;
	

	
	static {
		try {
			log = Logger.getLogger(DataChecker.class);
		} catch (Exception ee) {
		}
	}

	public DataChecker(DBObject dbObject, String displayName) {
		
		this.dbObject = dbObject;
		
		this.efgRDBTable = EFGUtils.getCurrentTableName();
		this.displayName = displayName;
		this.jdbcTemplate = EFGRDBImportUtils.getJDBCTemplate(this.dbObject);
		this.initDB();
	}
	public abstract String displayErrors();
	
	protected boolean findMetadataTable() {
		if (this.jdbcTemplate == null) {
			return false;
		}
		if ((this.displayName == null) || (this.displayName.trim().equals(""))) {

			log.error("Specified display name: '" + displayName
					+ "' was null or the empty string.");
			return false;
		}
		StringBuffer query = new StringBuffer(
				"SELECT DS_METADATA, DS_DATA FROM ");
		query.append(this.efgRDBTable);
		query.append(" WHERE DISPLAY_NAME=");
		query.append("\"");
		query.append(this.displayName.trim());
		query.append("\"");

		try {
			java.util.List list = EFGRDBImportUtils.executeQueryForList(
					this.jdbcTemplate, query.toString(), 2);
			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				this.metadatasourceName = queue.getObject(0);
				this.datasourceName = queue.getObject(1);
				break;// because only one row is required
			}
			return true;
		} catch (Exception ee) {
			log.error(ee.getMessage());
			return false;
		}

	}

	protected String makeQuery(String fieldName) {
		if (fieldName == null) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("Select ");
		buffer.append(fieldName);
		buffer.append(" From ");
		buffer.append(this.datasourceName);
		return buffer.toString();

	}

	
	

	protected URL getTempFilesHome() {
		if (this.tempFilesHome == null) {
			try {
				this.tempFilesHome = this.getClass().getResource(
						EFGImportConstants.TEMPORARY_ERROR_FILE);
				
			} catch (Exception ee) {
				log.error(ee.getMessage());
				
			}
		}
		return this.tempFilesHome;
	}

	public static String getServerHome() {
		StringBuffer serverHomeBuffer = new StringBuffer(EFGUtils
				.getServerHome());
		serverHomeBuffer.append(File.separator);
		serverHomeBuffer.append(EFGImportConstants.EFG_WEB_APPS);
		serverHomeBuffer.append(File.separator);
		serverHomeBuffer.append(EFGImportConstants.EFG_APPS);
		serverHomeBuffer.append(File.separator);
		return serverHomeBuffer.toString();
	}

	protected int getNumberOfErrors() {
		return this.numberOfErrors;
	}

	/**
	 * 
	 */
	protected void initDB() {
		this.isReady = this.findMetadataTable();
	}

	
	public boolean isReady() {
		return this.isReady;
	}

	
	

}

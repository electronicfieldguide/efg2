/**
 * 
 */
package project.efg.client.rdb.gui;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.client.factory.gui.GUIFactory;
import project.efg.client.factory.nogui.NoGUIFactory;
import project.efg.client.impl.gui.LoginDialog;
import project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface;
import project.efg.client.interfaces.gui.ImportBehavior;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.EFGRDBImportUtils;
import project.efg.util.utils.EFGUtils;

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

	private String searchableField, mediaResourceField, listsField,
			newDisplayName;

	private String metadataTableName;

	private StringBuilder messageBuffer;
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
		this.messageBuffer = new StringBuilder();
		this.dbObject = this.getClone(dbObject);
		this.init();
	}

	/**
	 * @param dbObject2
	 * @return
	 */
	private DBObject getClone(DBObject dbObject2) {
		String url = EFGImportConstants.EFGProperties.getProperty("dburl");
		return dbObject2.clone(url);
	}

	public String getErrorBuffer() {
		return this.messageBuffer.toString();
	}

	public boolean isError() {
		return this.isError;
	}

	private boolean loadFile() {
		try {
			URL url = this
					.getClass()
					.getResource(
							EFGImportConstants.EFGProperties
									.getProperty(EFGImportConstants.SAMPLE_DATA_LOCATION));
			String fileName = URLDecoder.decode(url.getFile(), "UTF-8");
			File dir = new File(fileName);
			URI file = dir.toURI();

			fileName = EFGUtils.getName(file);
			int index = fileName.lastIndexOf(".");
			String displayName = fileName;
			if (index > -1) {
				displayName = fileName.substring(0, index);
			}

			this.datasource = NoGUIFactory.getEFGDatasourceObject();
			this.datasource.setDisplayName(displayName);
			this.datasource.setDataName(file);
		} catch (Exception ee) {
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
		// find out if file already exists and bail out if it does

		if (this.loadFile()) {
			this.readNewDisplayName();
			this.list = NoGUIFactory.getEFGObjectList(this.dbObject);
			String behaviorType = EFGImportConstants.EFGProperties
					.getProperty("importOnlyBehavior");

			this.isUpdate = GUIFactory.getImportBehavior(this.list,
					this.datasource, behaviorType);

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
		this.listsField = EFGImportConstants.EFGProperties
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

		if (this.list == null) {
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
				StringBuilder query = new StringBuilder();
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

		StringBuilder buffer = new StringBuilder("UPDATE ");
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
			String message = e.getMessage();
			log.error(message);
			this.messageBuffer.append("\n");
			this.messageBuffer.append(message);

		}
		this.isError = true;
		return false;

	}

	private boolean executeMediaResourceUpdateQuery() {

		StringBuilder buffer = new StringBuilder("UPDATE ");
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

		StringBuilder buffer = new StringBuilder("UPDATE ");
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
		StringBuilder buffer = new StringBuilder();
		String[] resources = string.split(RegularExpresionConstants.COMMASEP);
		for (int i = 0; i < resources.length; i++) {
			if (i != 0) {
				buffer.append(" OR ");
			}
			buffer.append(EFGImportConstants.NAME);
			buffer.append("=\"");
			buffer.append(resources[i].trim());
			buffer.append("\"");
		}
		return buffer.toString();
	}

	private boolean editMetadata() {
		this.getMetadataTableName();
		if ((this.metadataTableName != null)
				&& (!this.metadataTableName.trim().equals(""))) {
			if (this.executeSearchableFieldUpdateQuery()) {
				if (this.executeMediaResourceUpdateQuery()) {
					if (this.executeListsUpdateQuery()) {
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

	public static void main(String[] args) {
		LoginDialog dialog = new LoginDialog(new JFrame(), "Login", true);
		dialog.setVisible(true);

		if (dialog.isSuccess()) {
			String m_loginName = dialog.getLoginName();
			String m_password = new String(dialog.getPassword());
			String url = EFGImportConstants.EFGProperties.getProperty("dburl");

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
		} else {
			JOptionPane.showMessageDialog(null,
					"Sample Data Loaded Successfully",
					"Success in Loading Sample Data",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}

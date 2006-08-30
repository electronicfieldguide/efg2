/**
 * 
 */
package project.efg.Imports.efgImportsUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.rdb.EFGRDBImportUtils;
import project.efg.servlets.efgServletsUtil.EFGParseObject;
import project.efg.servlets.efgServletsUtil.EFGParseObjectList;
import project.efg.servlets.efgServletsUtil.EFGParseStates;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 * 
 */
public class DataChecker {

	private JdbcTemplate jdbcTemplate;

	private String metadatasourceName;

	private String datasourceName;

	private String efgRDBTable;

	private String displayName;

	private static String imagesHome;

	private DBObject dbObject;

	private boolean isReady = false;

	private String mediaQuery;

	static Logger log = null;

	private  URL tempFilesHome;

	private int numberOfErrors = 0;
	private EFGParseStates efgParseStates;
	private HashMap errorTable;
	static {
		try {
			log = Logger.getLogger(DataChecker.class);
		} catch (Exception ee) {
		}
	}

	public DataChecker(DBObject dbObject, String displayName) {
		this.efgParseStates = new EFGParseStates();
		this.errorTable = new HashMap();
		this.dbObject = dbObject;
		this.efgRDBTable = EFGImportConstants.EFGProperties
				.getProperty("ALL_EFG_RDB_TABLES");
		this.displayName = displayName;
		this.jdbcTemplate = EFGRDBImportUtils.getJDBCTemplate(this.dbObject);
		this.initDB();
	}

	private boolean findMetadataTable() {
		if (this.jdbcTemplate == null) {
			return false;
		}
		if ((this.displayName == null) || (this.displayName.trim().equals(""))) {

			log.error("Specified display name: '" + displayName
					+ "' was null or the empty string.");
			return false;
		} else {

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

	}

	private String makeQuery(String fieldName) {
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

	private String getMediaQuery() {
		if (this.mediaQuery == null) {

			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append("SELECT DISTINCT ");
			queryBuffer.append(EFGImportConstants.LEGALNAME);
			queryBuffer.append(" FROM ");
			queryBuffer.append(this.metadatasourceName);
			queryBuffer.append(" WHERE ");
			queryBuffer.append(EFGImportConstants.MEDIARESOURCE);
			queryBuffer.append(" = 'true'");
			this.mediaQuery = queryBuffer.toString();
		}
		return this.mediaQuery;
	}

	private static String getWebImagesHome() {
		if (imagesHome == null) {
			StringBuffer imagesHomeBuffer = new StringBuffer(getServerHome());
			imagesHomeBuffer.append(EFGImportConstants.EFG_IMAGES_DIR);
			imagesHomeBuffer.append(File.separator);
			imagesHome = imagesHomeBuffer.toString();
		}
		return imagesHome;

	}

	private URL getTempFilesHome() {
		if (this.tempFilesHome == null) {
			try {
				this.tempFilesHome = this.getClass().getResource(
						EFGImportConstants.TEMPORARY_ERROR_FILE);
				
			} catch (Exception ee) {
				System.err.println(ee.getMessage());
			}
		}
		return this.tempFilesHome;
	}

	private static String getServerHome() {
		StringBuffer serverHomeBuffer = new StringBuffer(EFGUtils
				.getCatalinaHome());
		serverHomeBuffer.append(File.separator);
		serverHomeBuffer.append(EFGImportConstants.EFG_APPS);
		serverHomeBuffer.append(File.separator);
		return serverHomeBuffer.toString();
	}

	private int getNumberOfErrors() {
		return this.numberOfErrors;
	}

	/**
	 * 
	 */
	private void initDB() {
		this.isReady = this.findMetadataTable();
	}

	public String displayErrors() {

		StringBuffer errorBuffer = new StringBuffer();
		errorBuffer.append("<html><body>");
		errorBuffer.append("<h1>Application Found ");
		errorBuffer.append(this.getNumberOfErrors() + "");
		errorBuffer.append(" potential errors </h1>");
		errorBuffer.append("<hr></hr>");

		errorBuffer.append("<table border=\"1\">");
		errorBuffer
				.append("<tr><th>Field Name</th> <th>Missing Files</th></tr>");

		Iterator fieldIter = this.errorTable.keySet().iterator();

		while (fieldIter.hasNext()) {
			String fieldName = (String) fieldIter.next();
			errorBuffer.append("<tr><td>");
			errorBuffer.append(fieldName);
			errorBuffer.append("</td> <td></td></tr>");

			Set dataset = (Set) this.errorTable.get(fieldName);

			Iterator datasetIter = dataset.iterator();
			while (datasetIter.hasNext()) {
				String name = (String) datasetIter.next();
				errorBuffer.append("<tr><td>");
				errorBuffer.append("</td>");
				errorBuffer.append("<td>");
				errorBuffer.append(name);
				errorBuffer.append("</td></tr>");
			}

		}
		errorBuffer.append("</table>");
		errorBuffer.append("</body>");
		errorBuffer.append("</html>");
		BufferedWriter out = null;
		try {
			String newFileName = this.datasourceName
					+ EFGImportConstants.IMAGEAPPENDER
					+ EFGImportConstants.HTML_EXT;
			String dir1 = URLDecoder.decode(getTempFilesHome().getFile(),"UTF-8");
			File dir = new File(dir1);
			if(!dir.exists()){
				
				dir.createNewFile();
			}
			File file = new File( dir,newFileName);
			out = new BufferedWriter(new FileWriter(file));
			out.write(errorBuffer.toString());
			out.flush();
			out.close();

			return file.toURI().toString();
		} catch (Exception ee) {
			log.error(ee.getMessage());
			
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

				}
			}
		}
		// write to a file
		return null;
	}

	public boolean isReady() {
		return this.isReady;
	}

	public boolean checkMediaResources() {
		if (!isReady) {
			return true;
		}
		String mediaQuery = getMediaQuery();

		if (mediaQuery != null) {

			try {
				List listOfFields = EFGRDBImportUtils.executeQueryForList(
						this.jdbcTemplate, mediaQuery, 1);
				for (java.util.Iterator iter = listOfFields.iterator(); iter
						.hasNext();) {
					EFGQueueObjectInterface field = (EFGQueueObjectInterface) iter
							.next();
					String fieldName = field.getObject(0);
					String query = makeQuery(fieldName);
					List listOfImages = EFGRDBImportUtils.executeQueryForList(
							this.jdbcTemplate, query, 1);
					Set mSet = new HashSet();
					for (java.util.Iterator iter1 = listOfImages.iterator(); iter1
							.hasNext();) {
						EFGQueueObjectInterface image = (EFGQueueObjectInterface) iter1
								.next();
						String states = image.getObject(0);
						EFGParseObjectList lists =
							this.efgParseStates.parseStates(EFGImportConstants.LISTSEP,states,true); 
						
						for(int i = 0; i < lists.getSize(); i++){
							EFGParseObject obj = lists.getEFGParseObject(i);
							String imageName = obj.getState();
							File file = new File(getWebImagesHome(), imageName);
							if (!file.exists()) {
								mSet.add(imageName);
							}
						}
						
					}
					if (mSet.size() > 0) {
						this.errorTable.put(fieldName, mSet);
					}
					this.numberOfErrors = this.numberOfErrors + mSet.size();

				}
				if (this.numberOfErrors > 0) {
					return false;
				}

			} catch (Exception e) {
				log.error(e.getMessage());
				System.err.println(e.getMessage());
				return true;
			}
		}

		return true;
	}

	public boolean checkLists() {
		return false;

	}

	public boolean checkCategoricals() {
		return false;
	}

	public boolean checkNumerics() {
		return false;
	}

	public boolean checkAll() {
		return false;
	}

}

/**
 * 
 */
package project.efg.client.utils.nogui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.interfaces.EFGRowMapperInterface;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.EFGRDBImportUtils;
import project.efg.util.utils.UnicodeToASCIIFilter;

/**
 * @author kasiedu
 * 
 */
public class IllegalCharactersDataChecker extends DataChecker {

	private EFGRowMapperInterface rowMapper;

	private Hashtable mapTable;

	private StringBuilder errorsBuffer;

	static Logger log = null;

	static {
		try {
			log = Logger.getLogger(IllegalCharactersDataChecker.class);
		} catch (Exception ee) {
		}
	}

	public IllegalCharactersDataChecker(DBObject dbObject, String displayName) {
		super(dbObject, displayName);
		//this.rowMapper = new EFGRowMapperImpl();
		this.rowMapper = SpringFactory.getRowMapper();
		this.mapTable = new Hashtable();
		this.errorsBuffer = new StringBuilder();
	}

	/**
	 * 
	 */
	private boolean loadMap() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(EFGImportConstants.LEGALNAME);
		query.append(",");
		query.append(EFGImportConstants.NAME);
		query.append(" FROM ");
		query.append(this.metadatasourceName);
		try {
			java.util.List list = EFGRDBImportUtils.executeQueryForList(
					this.jdbcTemplate, query.toString(), 2);
			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				String legalName = queue.getObject(0);
				String name = queue.getObject(1);
				this.mapTable.put(legalName.toLowerCase().trim(), name.trim());
			}
			return true;
		} catch (Exception ee) {
			log.error(ee.getMessage());

		}
		return false;
	}

	/**
	 * @return
	 */
	private String getIllegalCharacterQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM " + this.datasourceName);

		return query.toString();
	}

	public String displayErrors() {

		BufferedWriter out = null;
		try {
			String newFileName = this.datasourceName
					+ EFGImportConstants.ILLEGAL_CHAR_APPENDER
					+ EFGImportConstants.HTML_EXT;
			String dir1 = URLDecoder.decode(getTempFilesHome().getFile(),
					"UTF-8");
			File dir = new File(dir1);
			if (!dir.exists()) {
				dir.createNewFile();
			}
			File file = new File(dir, newFileName);
			out = new BufferedWriter(new FileWriter(file));
			out.write(this.errorsBuffer.toString());
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

	public boolean checkIllegalCharacters() {
		try {
			if (!isReady()) {
				return true;
			}
			String illegalQuery = getIllegalCharacterQuery();

			if (illegalQuery != null) {
				if (!loadMap()) {
					return true;
				}
				try {

					SqlRowSet rowset = this.rowMapper.mapRows(
							this.jdbcTemplate, illegalQuery);

					StringBuilder errorsB = null;
					SqlRowSetMetaData metadata = rowset.getMetaData(); // Get
																		// metadata
					String[] columnNames = metadata.getColumnNames(); // on
					// them
					int numcols = metadata.getColumnCount(); // How many
																// columns?

					int cols = numcols + 1;

					// ignore legal names so reduce number of columns by 1

					UnicodeToASCIIFilter filter = new UnicodeToASCIIFilter();
				
					while (rowset.next()) {// for each row

						StringBuilder rowBuffer = new StringBuilder("<tr>");
						boolean isError = false;
						for (int i = 1; i < cols; i++) {
							String colName = metadata.getColumnName(i);
							String str = rowset.getString(colName);

							if ((str == null) || str.trim().equals("")) {
								str = "";
							} else {
								try {
									
									boolean bool = filter
											.filter(new StringReader(str));
									
									if (bool) {
										++this.numberOfErrors;
										rowBuffer
												.append("<td bgcolor=\"red\">");
										rowBuffer.append(str);
										rowBuffer.append("</td>");
										isError = true;
									} else {
										rowBuffer.append("<td>");
										rowBuffer.append(str);
										rowBuffer.append("</td>");
									}
								} catch (Exception ee) {
									rowBuffer.append("<td>");
									rowBuffer.append(str);
									rowBuffer.append("</td>");
								}
							}
						}
						if (isError) {
							rowBuffer.append("</tr>");
							if (errorsB == null) {
								errorsB = this.createTableHeader(columnNames);
							}
							errorsB.append(rowBuffer.toString());
						}
					}
					if (this.getNumberOfErrors() > 0) {
						this.createErrorReport(errorsB);
						return false;
					}

				} catch (Exception e) {
					log.error(e.getMessage());

					return true;
				}
			}

			return true;
		} catch (Exception eex) {
			eex.printStackTrace();
		}
		return true;
	}

	/**
	 * @param errorsB
	 */
	private void createErrorReport(StringBuilder errorsB) {
		this.errorsBuffer.append(this.insertHeader());
		this.errorsBuffer.append(errorsB.toString());
		this.errorsBuffer.append(this.insertFooter());

	}

	private String insertHeader() {
		StringBuilder errorBuffer = new StringBuilder();
		errorBuffer.append("<html><body>");
		errorBuffer.append("<h1>Application Found ");
		errorBuffer.append(this.getNumberOfErrors() + "");
		errorBuffer.append(" potential errors </h1>");
		errorBuffer.append("<hr></hr>");

		errorBuffer.append("<table border=\"1\">");

		return errorBuffer.toString();
	}

	private String insertFooter() {
		StringBuilder errorBuffer = new StringBuilder();
		errorBuffer.append("</table>");
		errorBuffer.append("</body>");
		errorBuffer.append("</html>");

		return errorBuffer.toString();
	}

	private StringBuilder createTableHeader(String[] columnNames) {
		StringBuilder errorBuffer = new StringBuilder();
		for (int i = 0; i < columnNames.length; i++) {
			errorBuffer.append("<th>");

			String name = (String) this.mapTable.get(columnNames[i].toString()
					.toLowerCase());
			errorBuffer.append(name);
			errorBuffer.append("</th>");
		}

		return errorBuffer;
	}

}

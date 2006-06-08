package project.efg.Imports.rdb;

import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.Imports.efgInterface.CSV2DatabaseAbstract;
import project.efg.Imports.efgInterface.EFGDataExtractorInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.factory.ComparatorFactory;
import project.efg.util.EFGImportConstants;

/**
 * CSV2Database.java
 * 
 * $Id$
 * 
 * Created: Tue Feb 28 13:14:19 2006
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 * Imports a csv file into a relational database
 * 
 */
public class CSV2Database extends CSV2DatabaseAbstract {

	private JdbcTemplate jdbcTemplate;

	private String[] dataHeaders;

	private String metadataTableName; // metadata table to use

	private String tableName;

	private Comparator compare;

	// read from an external file
	private String efgRDBTable;

	private DataSourceTransactionManager txManager;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(CSV2Database.class);
		} catch (Exception ee) {
		}
	}

	public CSV2Database(EFGDatasourceObjectInterface datasource,
			EFGDataExtractorInterface dataExtractor, DBObject dbObject,
			boolean isUpdate) {
		super(datasource, dataExtractor, dbObject, isUpdate);
		log.debug("After super");
		this.dataHeaders = this.dataExtractor.getFieldNames();
		log.debug("After get header");
		this.txManager = this.getTransactionManager(this.dbObject);
		this.jdbcTemplate = this.getJDBCTemplate(this.dbObject);
		//READ FROM PROPERTIES FILE
		this.compare = ComparatorFactory
				.getComparator(EFGImportConstants.EFGProperties
						.getProperty("caseinsensitive.compare"));
		this.efgRDBTable = EFGImportConstants.EFGProperties
				.getProperty("ALL_EFG_RDB_TABLES");
	}
	public CSV2Database(EFGDatasourceObjectInterface datasource,
			EFGDataExtractorInterface dataExtractor, DBObject dbObject) {
		this(datasource, dataExtractor, dbObject, false);

	} // CSV2Database constructor

	/**
	 * @return true if import was successful. false otherwise
	 */
	public boolean import2Database() {

		log.debug("File Name : " + this.datasource.getDataName());

		boolean isSuccess = true;
		this.format2efg();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		TransactionStatus status = this.txManager.getTransaction(def);
		try {
			// execute your business logic here

			if (isSuccess) {
				log.debug("About to import: "
						+ this.datasource.getDataName().toString());

				if (this.isUpdate) {// we are doing updates
					log.debug("About to run an update");

					isSuccess = this.createOrUpdateEFGTable();
					
					if (isSuccess) {// replace table
						isSuccess = this.createDataTable();
						if(isSuccess){
						log.debug("Updates run successfully");
						}
					}
				} else {// we are not doing updates
					this.tableName = this.generateTableName(this.datasource
							.getDataName());
					if ((this.tableName == null)
							|| (this.tableName.trim().equals(""))) {// error
						// message
						// table
						// name
						// could not
						// be
						// generated
						log
								.error("Table name must not be null or the empty string!!!");
						
						return false;
					}
					if ((this.getDisplayName() == null)
							|| this.getDisplayName().trim().equals("")) {
						this.datasource.setDisplayName(this.tableName);
						log.debug("A new display name '"
								+ this.getDisplayName()
								+ " was created successfully");
					}
					log.debug("About to create Data table");
					isSuccess = this.createDataTable();

					if (isSuccess) {// data table created successfully
						log
								.debug("A Data table "
										+ this.tableName
										+ " was created successfully out of the file: '"
										+ this.datasource.getDataName()
												.toString() + "'!!");

						this.metadataTableName = this.createMetadataTableName();
						if (this.metadataTableName == null) {
							log
									.error("System could not create a metadata table for : "
											+ this.datasource.getDataName()
													.toString());
							this.txManager.rollback(status);
							return false;
						}
						String[] fieldNames = this.getDataTableHeaders();
						String[] legalNames = this.getLegalNames();

						if ((this.datasource.getTemplateDisplayName() == null)
								|| (this.datasource.getTemplateDisplayName()
										.trim().equals(""))) {// create a new
							// metadata
							// table
							log.debug("About to create new Metadata table");
							isSuccess = this.createMetadataTable(fieldNames,
									legalNames);
						} else {// use an old metadata table as template
							log
									.debug("About to clone an existing Metadata table");
							// find out if this metadata table exists. Quit if
							// it does not
							String metadataTableToClone = this
									.findMetadataTable(this.datasource
											.getTemplateDisplayName());

							if (metadataTableToClone == null) {// specified
								// metadata
								// table does
								// not exists
								log
										.error("System could not find the requested metadata table with display name: '"
												+ this.datasource
														.getTemplateDisplayName()
												+ "' for the table '"
												+ this.datasource.getDataName()
														.toString() + "'");
								isSuccess = false;
							} else {// specified metadata table exists. copy it
								isSuccess = this.cloneMetadataTable(
										metadataTableToClone, fieldNames,
										legalNames);
							}
						}

						if (isSuccess) {
							log.debug("Metadata table: "
									+ this.metadataTableName
									+ " was created successfully");
							log.debug("About to create EFGTable");
							isSuccess = this.createEFGTable();
							log
									.debug("EFGRDBTable was created/updated successfully");
						} else {
							log
									.error("An error occurred while creating Metadata table '"
											+ this.metadataTableName + "'!!!");
							isSuccess = false;
						}
					} else {
						log
								.error("An error occurred while creating data table '"
										+ this.tableName + "'!!!");
						isSuccess = false;
					}
				}
			}

			if (!isSuccess) {
				txManager.rollback(status);
				log.error("The data contained in "
						+ this.datasource.getDataName().toString()
						+ " could not be imported successfully. \n");
				log
						.error("Please view the logs for more verbose error messages!!!");
				return false;
			}
		} catch (Exception ex) {
			txManager.rollback(status);
			return false;
		}
		txManager.commit(status);
		return isSuccess;
	}

	/**
	 * @return the name of the database table created for the current data
	 */
	public String getDataTableName() {
		return this.tableName;
	}

	/**
	 * @return the name metadata table associated with the current imported
	 *         data.
	 */
	public String getMetadataTableName() {
		return this.metadataTableName;
	}

	private DataSourceTransactionManager getTransactionManager(DBObject dbObject){
		return EFGRDBImportUtils.getTransactionManager(dbObject);
	}
	private JdbcTemplate getJDBCTemplate(DBObject dbObject) {
		if (this.jdbcTemplate == null) {

			this.jdbcTemplate = EFGRDBImportUtils
					.getJDBCTemplate(dbObject);
		}
		return this.jdbcTemplate;
	}

	/**
	 * Start processing csv file
	 */
	private void format2efg() {
		try {
			log.debug(this.compare.getClass().getName());
			this.legalNames = this.translateHeaders(this.dataHeaders);
			
		} catch (Exception ee) {

			log.error(" An error occured during importation of : "
					+ this.datasource.getDataName() + "\n");

		}
	}
	private String[] getSortedLegalNames(){
		String[] sortedLegalNames = this.translateHeaders(this.dataHeaders);
		java.util.Arrays.sort(sortedLegalNames, this.compare);
		return sortedLegalNames;
	}
	/**
	 * 
	 * @param name
	 *            the uri to the csv file
	 * @return a name that could be used as a table name in a relational
	 *         database
	 */
	private String generateTableName(URI name) {
		if (name == null) {
			return null;
		}
		return EFGUtils.getName(name) + "_" + EFGUniqueID.getID();
	}

	/**
	 * @return a String array of DataTable header names
	 * 
	 */
	private String[] getDataTableHeaders() {
		return this.dataHeaders;
	}

	/**
	 * Create a table out of the supplied csv File
	 * 
	 * @return true if the table was successfully created false otherwise.
	 */
	private boolean createDataTable() {
		boolean isDone = true;
		// int ret = -1;
		try {
			String[] legalNames = this.getLegalNames();
			if (legalNames == null) {
				log.error("Could not obtain legal names for : "
						+ this.datasource.getDataName().toString());
				return false;
			}
			if (isDone) {
				StringBuffer query = new StringBuffer();
				query.append("uniqueID VARCHAR(255) PRIMARY KEY");
				for (int i = 0; i < legalNames.length; i++) {
					String th = legalNames[i];
					if ((th == null) || (th.trim().equals(""))) {
						StringBuffer errBuffer = new StringBuffer();
						errBuffer.append("The file: ");
						errBuffer.append(this.datasource.getDataName()
								.toString());
						errBuffer.append(" contains a blank column name.\n");
						if (i != 0) {
							errBuffer
									.append(" This column comes after the column named: "
											+ this.dataHeaders[i - 1] + "\n");
						}
						if ((i + 1) < legalNames.length) {
							errBuffer.append(" and before "
									+ this.dataHeaders[i + 1] + "\n");
						}
						errBuffer
								.append("The import is being terminated.Please fix your data and try the import again!!");
						log.error(errBuffer.toString());
						isDone = false;
						break;
					}
					query.append(", `");
					query.append(th);
					query.append("` ");
					//READ FROM PROPERTIES FILE
					query.append("TEXT");
				}
				if (isDone) {
					log.debug("About to drop table : " + this.tableName
							+ " if it exists!!");
					this.executeStatement("DROP TABLE IF EXISTS "
							+ this.tableName);

					String st = "CREATE TABLE " + this.tableName + "( "
							+ query.toString() + ")";
					log.debug("Query: " + st);
					this.executeStatement(st);

					// now get each row of data
					while (this.dataExtractor.nextValue() != null) {
						query = new StringBuffer();
						// Sets up the uniqueID
						query.append("\"");
						query.append(EFGUniqueID.getID() + "");
						query.append("\"");
						boolean exists = false;//means that the line is not blank
						for (int i = 0; i < this.dataHeaders.length; i++) {
							String title = this.dataHeaders[i];

							String dataVal = this.dataExtractor
									.getValueByFieldName(title);
							if ((dataVal == null)
									|| (dataVal.trim().equals(""))) {
								// FIX ME SPRING
								dataVal = EFGImportConstants.EFGProperties
										.getProperty("dbBlank");
								if ((dataVal == null)
										|| (dataVal.trim().equals(""))) {
									dataVal = "";
								}
							}
							else{
								exists = true;//there is at least one variable
							}
							query.append(",");
							query.append("\"");
							query.append(dataVal.trim());
							query.append("\"");
						}
						try {
							if (query.length() > 0) {
								if(exists){
									String stmtQuery = "INSERT INTO "
										+ this.tableName + " VALUES("
										+ query.toString() + ")";
									log.debug("Insert query: " + stmtQuery);
									this.executeStatement(stmtQuery);
								}
								else{
									log.debug("Empty line.Do not insert!!!");
								}
							}
						} catch (Exception eex) {
							eex.printStackTrace();
							isDone = false;
							
							this.logMessage(eex);
							break;
						}
						if (!isDone) {
							break;
						}
					}
				}
			}
		} catch (Exception ee) {
			isDone = false;
			this.logMessage(ee);
		}
		return isDone;
	}

	private int executeStatement(String query) throws Exception {

		return this.jdbcTemplate.update(query);
	}

	private java.util.List executeQueryForList(String query, int numberOfColumns)
			throws Exception {
		return EFGRDBImportUtils.executeQueryForList(this.jdbcTemplate, query,
				numberOfColumns);
	}

	/**
	 * @return true if efgRDBTable exists, false otherwise
	 */
	private boolean checkEFGRDBTable() {
		if ((this.efgRDBTable == null) || (this.efgRDBTable.trim().equals(""))) {
			log.error("The efgRDBTable property is not set!!!");
			return false;
		}
		return true;
	}

	/**
	 * @param displayName -
	 *            Use this displayName to find the metadata table associated
	 *            with it If displayName is null or the empty String, System
	 *            generates a Metadata name to use to create the metadata table ,
	 *            else System finds the metadataTable in the database. If system
	 *            cannot find metadata table in database it returns null;
	 */
	private String findMetadataTable(String displayName) {
		String str = null;

		if ((displayName == null) || (displayName.trim().equals(""))) {
			str = null;
			log.error("Specified display name: '" + displayName
					+ "' was null or the empty string.");
		} else {
			if (checkEFGRDBTable()) {
				StringBuffer query = new StringBuffer(
						"SELECT DS_METADATA FROM ");
				query.append(this.efgRDBTable);
				query.append(" WHERE DISPLAY_NAME=");
				query.append("\"");
				query.append(displayName.trim());
				query.append("\"");
				log.debug("About to retrieve metadata table for : "
						+ displayName);
				try {
					java.util.List list = executeQueryForList(query.toString(),
							1);
					for (java.util.Iterator iter = list.iterator(); iter
							.hasNext();) {
						EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
								.next();
						str = queue.getObject(0);
						break;//because only one row is required
					}
					if (str == null) {
						log
								.error("A metadataTable could not be found with display name:  "
										+ displayName);
						str = null;
					}
				} catch (Exception ee) {
					this.logMessage(ee);
					str = null;
				}
			}
		}
		return str;
	}

	/**
	 * @return true if the table was successfully updated.
	 */
	private boolean createOrUpdateEFGTable() {
		StringBuffer query = null;
		if (!checkEFGRDBTable()) {
			return false;
		}
		try {
			query = new StringBuffer("SELECT DS_METADATA,DS_DATA FROM ");
			query.append(this.efgRDBTable);
			query.append(" WHERE DISPLAY_NAME=");
			query.append("\"");
			query.append(this.getDisplayName());
			query.append("\"");

			log.debug("About to execute query: " + query.toString());
			List list = this.executeQueryForList(query.toString(), 2);

			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				this.metadataTableName = queue.getObject(0);
				log.debug("MetadaTable to use: " + this.metadataTableName);
				this.tableName = queue.getObject(1);
				log.debug("Table to use: " + this.tableName);
				break;
			}
			if (this.metadataTableName == null) {
				log
						.error("A metadata table could not be found for the current data!!");
				return false;
			}

			query = new StringBuffer("SELECT LEGALNAME FROM ");
			query.append(this.metadataTableName);
			list = this.executeQueryForList(query.toString(), 1);

			log.debug("queue size: " + list.size());
			log.debug("legalNames size: " + this.getLegalNames().length);

			if (list.size() != this.getLegalNames().length) {
				log
						.error("The number of columns in the MetadataTable to be used "
								+ "for this update "
								+ "does not match the number of field names in the data being "
								+ "imported!!");
				return false;
			}

			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				String legalname = queue.getObject(0);
				if (Arrays.binarySearch(this.getSortedLegalNames(), legalname.trim(),
						this.compare) < 0) {
					log.error("The column named: '" + legalname
							+ "' does not exists in the table '"
							+ this.metadataTableName + "'");
					return false;
				}
			}
		} catch (Exception ee) {
			this.logMessage(ee);
			return false;

		}
		return true;
	}

	/**
	 * Create EFGRDTable if it does not already exists
	 * 
	 * @return true if query executed successfully.
	 */
	private boolean createEFGRDBTable() {
		try {
			if (checkEFGRDBTable()) {
				StringBuffer query = new StringBuffer();
				log.debug("About to create table: '" + this.efgRDBTable
						+ "' if it does not exists");
				//PUT IN PROPERTIES FILE
				query.append("CREATE TABLE IF NOT EXISTS ");
				query.append(this.efgRDBTable);
				query.append("( DS_DATA VARCHAR(255) not null,"
						+ "ORIGINAL_FILE_NAME TEXT, "
						+ "DS_METADATA VARCHAR(255) not null, "
						+ "DISPLAY_NAME VARCHAR(255) unique not null, "
						+ "XSL_FILENAME_TAXON VARCHAR(255), "
						+ "XSL_FILENAME_SEARCHPAGE_PLATES VARCHAR(255), "
						+ "XSL_FILENAME_SEARCHPAGE_LISTS VARCHAR(255), "
						+ "CSS_FILENAME VARCHAR(255), "
						+ "JAVASCRIPT_FILENAME VARCHAR(255) " + ")");
				log.debug("About to execute query : '" + query.toString());
				this.executeStatement(query.toString());
				log.debug("Query executed successfully!!");
				return true;
			}
		} catch (Exception ee) {
			this.logMessage(ee);
		}
		log.debug("About to return false!!!");
		return false;
	}

	/**
	 * Create helper table for Data and metadata tables
	 * 
	 * @return true if successfully created, false otherwise
	 */
	private boolean createEFGTable() {
		boolean isDone = true;
		try {

			if ((this.getDisplayName() == null)
					|| (this.getDisplayName().trim().equals(""))) {
				this.datasource.setDisplayName(this.tableName);
			}

			isDone = createEFGRDBTable();
			if (isDone) {
				StringBuffer query = new StringBuffer();
				query = new StringBuffer("SELECT count(*) FROM ");
				query.append(this.efgRDBTable);
				query.append(" WHERE DISPLAY_NAME=");
				query.append("\"");
				query.append(this.getDisplayName());
				query.append("\"");

				java.util.List list = this.executeQueryForList(
						query.toString(), 1);
				String counter = ((EFGQueueObjectInterface) list.get(0))
						.getObject(0);
				int count = 0;
				if (counter != null) {
					count = Integer.parseInt(counter);
				}

				if (count > 0) {
					log.debug("Row already exists.Updating row");
					query = new StringBuffer("UPDATE ");
					query.append(this.efgRDBTable);
					query.append(" SET DS_METADATA = ");
					query.append("\"");
					query.append(this.metadataTableName);
					query.append("\"");
					query.append(" WHERE DISPLAY_NAME = ");
					query.append("\"");
					query.append(this.getDisplayName());
					query.append("\"");

				} else {
					log.debug("Row does not already exists.Inserting row");
					query = new StringBuffer("INSERT INTO ");
					query.append(this.efgRDBTable);
					query.append(" VALUES( ");
					query.append("\"");
					query.append(EFGUtils.parse240(this.tableName));
					query.append("\"");
					query.append(",");
					query.append("\"");
					query.append(EFGUtils.parseEFGSEP(this.datasource
							.getDataName().toString()));
					query.append("\"");
					query.append(",");
					query.append("\"");
					query.append(EFGUtils.parse240(this.metadataTableName));
					query.append("\"");
					query.append(",");
					query.append("\"");
					query.append(EFGUtils.parse240(this.getDisplayName()));
					query.append("\"");
					//place holders for xslfile, css file and javascript
					query.append(",");
					query.append("\"");
					query.append("\"");

					query.append(",");
					query.append("\"");
					query.append("\"");

					query.append(",");
					query.append("\"");
					query.append("\"");

					query.append(",");
					query.append("\"");
					query.append("\"");

					query.append(",");
					query.append("\"");
					query.append("\"");

					query.append(")");

				}
				log.debug("About to execute query: " + query.toString());
				this.executeStatement(query.toString());
				log.debug("Query is executed!!!");
			}
		} catch (Exception ee) {
			isDone = false;
			this.logMessage(ee);
		}
		return isDone;
	}

	/**
	 * Create a name for the Metadata table if none is supplied
	 */
	private String createMetadataTableName() {
		String meta = this.tableName + "Info";
		log.debug("About to create Metadata table name: '" + meta + "'");
		return meta;
	}

	/**
	 * @param ee-
	 *            The exception to log
	 */
	private void logMessage(Exception ee) {

		log.error(ee.getMessage());

	}

	private boolean cloneMetadataTable(String metadataTableToClone,
			String[] fieldNames, String[] legalNames) {
		log.debug("Table to clone: " + metadataTableToClone);
		log.debug("Metadata table Name: " + this.metadataTableName);

		boolean isDone = this.createMetadataTableHeader(this.metadataTableName,
				fieldNames, legalNames);
		try {
			if (isDone) {
				StringBuffer query = new StringBuffer();
				query.append("INSERT INTO ");
				query.append(this.metadataTableName);
				query.append("( SELECT * FROM " + metadataTableToClone);
				query.append(")");
				log.debug("Insert query: " + query.toString());

				int ret = this.executeStatement(query.toString());

				if (ret == 0) {
					log.error("'" + this.metadataTableName
							+ "' could not be created from '"
							+ metadataTableToClone + "'");
					isDone = false;
				}
			}
		} catch (Exception ee) {
			isDone = false;
			this.logMessage(ee);
		}
		return isDone;
	}

	private boolean createMetadataTableHeader(String infoTable,
			String[] fieldNames, String[] legalNames) {
		boolean isDone = true;
		// create the query

		if (legalNames == null) {
			log.error("The field names in the file "
					+ this.datasource.getDataName().toString()
					+ " could not be converted to legal names");
			isDone = false;
		}

		if ((legalNames == null) || (fieldNames == null)
				|| (fieldNames.length != legalNames.length)) {
			log.error("Data table and metadata table fields do not match!!");
			isDone = false;
		}
		if (fieldNames == null) {
			log.error("The file " + this.datasource.getDataName().toString()
					+ " does not contain field names");
			isDone = false;
		}
		log.debug("Number of Field Names in current data is : "
				+ fieldNames.length);
		log.debug("Number of Legalnames Names in current data is : "
				+ legalNames.length);

		if (!isDone) {
			log.error("Import terminated");

		} else {
			log.error("Get metadata head");
			String[] metaHead = this.getMetadataTableHeaders();
			if (metaHead == null) {
				log
						.error("System could not find metadata table header file.Import is terminating!!\n");
				isDone = false;
			}
			log.debug("Number of Field Names in system table csv file is : "
					+ metaHead.length);

			if (isDone) {
				// create metadata table headers
				// get this from a utils class perhaps
				log.debug("About to create table : " + infoTable);
				StringBuffer query = new StringBuffer();

				for (int i = 0; i < metaHead.length; i++) {
					String th = metaHead[i];
					if ((th == null) || (th.trim().equals(""))) {
						StringBuffer errBuffer = new StringBuffer();
						errBuffer.append("The table: ");
						errBuffer.append(this.tableName);
						errBuffer.append(" contains a blank column name.\n");
						if (i != 0) {
							errBuffer
									.append(" This column comes after the column named: "
											+ metaHead[i - 1] + "\n");
						}
						if ((i + 1) < metaHead.length) {
							errBuffer.append(" and before " + metaHead[i + 1]
									+ "\n");
						}
						errBuffer
								.append("The import is being terminated.Please fix the metadata table and try the import again!!");
						log.error(errBuffer.toString());
						isDone = false;
						break;
					}

					query.append(" `");
					query.append(th);
					query.append("` ");

					if (EFGImportConstants.ORDER.equalsIgnoreCase(th.trim())) {
						query.append(" INTEGER ");

					} else if ((EFGImportConstants.LEGALNAME
							.equalsIgnoreCase(th))
							|| (EFGImportConstants.NAME.equalsIgnoreCase(th))) {
						query.append(" VARCHAR(255) ");
					} else {
						query.append(" VARCHAR(6) ");
					}
					if ((i + 1) < metaHead.length) {
						query.append(",");
					}
				}
				if (isDone) {
					try {
						log.debug("About to drop the table: '" + infoTable
								+ "' if it exists");
						this.executeStatement("DROP TABLE IF EXISTS "
								+ infoTable);

						String stmtQuery = "CREATE TABLE " + infoTable + "( "
								+ query.toString() + ")";

						log.debug("About to execute query : " + stmtQuery);
						this.executeStatement(stmtQuery);

						log.debug("Metadata table: '" + infoTable
								+ "' was created successfully");
					} catch (Exception ee) {
						isDone = false;
						this.logMessage(ee);
					}
				}
			}
		}
		return isDone;
	}

	/**
	 * 
	 * @return an array of headers to be used for Metadata table REFACTOR USE
	 *         STATIC SINGLETON FACTORY METHOD CONFIGURE WITH SPRING
	 */
	private String[] getMetadataTableHeaders() {
		return EFGUtils.getMetadataTableHeaders();
	}

	/**
	 * @return true if Metadata table was successfully created
	 */
	private boolean createMetadataTable(String[] fieldNames, String[] legalNames) {

		boolean isDone = this.createMetadataTableHeader(this.metadataTableName,
				fieldNames, legalNames);
		try {
			if (isDone) {
				log.debug("About to populate metadataTable");
				String[] metaHead = this.getMetadataTableHeaders();
				for (int j = 0; j < fieldNames.length; j++) {
					StringBuffer query = new StringBuffer();

					for (int i = 0; i < metaHead.length; i++) {
						String th = metaHead[i].trim();
						if (EFGImportConstants.LEGALNAME.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append(legalNames[j].trim());
							query.append("\"");
						} else if (EFGImportConstants.NAME.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append(fieldNames[j].trim());
							query.append("\"");
						} else if (EFGImportConstants.SEARCHABLE
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("false");
							query.append("\"");
						} else if (EFGImportConstants.ISLISTS
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("false");
							query.append("\"");
						} else if (EFGImportConstants.ONTAXONPAGE
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("true");
							query.append("\"");
						} else if (EFGImportConstants.CATEGORICAL
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("false");
							query.append("\"");
						} else if (EFGImportConstants.NARRATIVE
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("true");
							query.append("\"");
						} else if (EFGImportConstants.NUMERIC
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("false");
							query.append("\"");
						} else if (EFGImportConstants.NUMERICRANGE
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("false");
							query.append("\"");
						} else if (EFGImportConstants.MEDIARESOURCE
								.equalsIgnoreCase(th)) {
							query.append("\"");
							query.append("false");
							query.append("\"");
						} else if (EFGImportConstants.ORDER
								.equalsIgnoreCase(th)) {
							query.append(new Integer(j + 1));
						}
						if ((i + 1) < metaHead.length) {
							query.append(",");
						}
					}
					if (query.length() > 0) {
						String stmtQuery = "INSERT INTO "
								+ this.metadataTableName + " VALUES("
								+ query.toString() + ")";
						log.debug("Insert query: " + stmtQuery);
						this.executeStatement(stmtQuery);
					}
				}
			}
		} catch (Exception ee) {
			isDone = false;
			this.logMessage(ee);
		}
		return isDone;
	}

	/**
	 * Main test class. Replace with unit tests
	 */
	public static void main(String args[]) {

	}
} // CSV2Database
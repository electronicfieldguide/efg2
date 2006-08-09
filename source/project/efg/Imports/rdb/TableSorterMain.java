/* $Id$
* $Name$
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
package project.efg.Imports.rdb;

import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgImpl.MyJRadioButton;
import project.efg.Imports.efgImpl.MyJRadioButtonLists;
import project.efg.Imports.efgImpl.TableSorter;
import project.efg.Imports.efgImpl.TableSorterObject;
import project.efg.Imports.efgInterface.TableSorterMainInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.util.EFGImportConstants;

/**
 * TableSorterDemo is like TableDemo, except that it inserts a custom model -- a
 * sorter -- between the table and its data model. It also has column tool tips.
 */
public class TableSorterMain extends TableSorterMainInterface {
	
	
	
	private static final long serialVersionUID = 1100930277167912584L;
	private JdbcTemplate jdbcTemplate;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(TableSorterMain.class);
		} catch (Exception ee) {
		}
	}
	
	public TableSorterMain(DBObject dbObject, 
			EFGDatasourceObjectInterface ds, 
			JFrame frame) {
		super(dbObject, ds, frame);
		
	}
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgImpls.efgImpl.TabelSorterMainInterface#createData(java.lang.String)
	 */
	public TableSorterObject createData(EFGDatasourceObjectInterface ds) {
		String tableName = null;
		//String[] columnNames = null;
		TableSorterObject tableObject = null; 
		if(this.jdbcTemplate == null){
			this.jdbcTemplate = 
				EFGRDBImportUtils.getJDBCTemplate(this.getDBObject());
			}
		MyJRadioButtonLists data = new MyJRadioButtonLists();
		try {
			 tableName = this.getTableName(ds);
			if (tableName == null) {
				return null;
			}
			ArrayList buttonTable = new ArrayList();
			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM " + tableName);

			org.springframework.jdbc.support.rowset.SqlRowSet rowset = 
				this.jdbcTemplate
					.queryForRowSet(query.toString());

			SqlRowSetMetaData metadata = rowset.getMetaData(); // Get metadata
																// on
			// them
			int numcols = metadata.getColumnCount(); // How many columns?

			int cols = numcols + 1;

			// ignore legal names so reduce number of columns by 1
			String[] columnNames = new String[numcols - 1];

			int j = 0;
			while (rowset.next()) {
				// ignore legal names so reduce dataRow[] by 1
				Object[] dataRow = new Object[numcols - 1];
				ButtonGroup button = new ButtonGroup();
				int w = 0;
				for (int i = 1; i < cols; i++) {
					String colName = metadata.getColumnName(i);
					// if the column name is the legal name skip it
					if (colName.equalsIgnoreCase(EFGImportConstants.LEGALNAME)) {
						continue;
					}
					if (j == 0) {// iterating over headers
						columnNames[w] = colName;
					}

					if (colName.equalsIgnoreCase(EFGImportConstants.NAME)) {
						String str = rowset.getString(colName);
						dataRow[w] = str.trim();
					} else if (colName
							.equalsIgnoreCase(EFGImportConstants.ORDER)) {
						int ii = rowset.getInt(colName);

						dataRow[w] = new Integer(ii);
					} else if ((colName
							.equalsIgnoreCase(EFGImportConstants.ONTAXONPAGE))
							|| (colName
									.equalsIgnoreCase(EFGImportConstants.SEARCHABLE))
							|| (colName
									.equalsIgnoreCase(EFGImportConstants.ISLISTS))) {
						String str = rowset.getString(colName);
						dataRow[w] = new Boolean(str);
					} else {
						// add each column to a groupbutton
						String str = rowset.getString(colName);
						MyJRadioButton mj = new MyJRadioButton("", str
								.equalsIgnoreCase("true"));
						dataRow[w] = mj;
						button.add(mj);
					}
					++w;
				}
				data.add(j, dataRow);
				buttonTable.add(button);
				++j;
			}
			tableObject = new TableSorterObject(data,columnNames);
		} catch (Exception rr) {
			rr.printStackTrace();
			log.error(rr.getMessage());
			return null;
		}
		
		return tableObject;
	} 

	private String getTableName(EFGDatasourceObjectInterface ds){
		String tableName = null;
		try {
			StringBuffer query = new StringBuffer();
			query.append("SELECT DS_METADATA");
			query.append(" FROM ");
			query.append(EFGImportConstants.EFGProperties
					.getProperty("ALL_EFG_RDB_TABLES"));
			query.append(" WHERE DISPLAY_NAME = \"");
			query.append(ds.getDisplayName());
			query.append("\"");
			java.util.List list = EFGRDBImportUtils.executeQueryForList(
					this.jdbcTemplate, query.toString(), 1);
			log.debug("After  lists size. " + list.size());
			for (java.util.Iterator iter = list.iterator(); iter.hasNext();) {
				EFGQueueObjectInterface queue = (EFGQueueObjectInterface) iter
						.next();
				tableName = queue.getObject(0);
				log.debug("Table Name: " + tableName);
				break;
			}
		} catch (Exception rr) {
			rr.printStackTrace();
			log.error(rr.getMessage());
		}
		return tableName;
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgImpls.efgImpl.TabelSorterMainInterface#updateMetadataTable()
	 */
	public void updateMetadataTable(TableSorter sorter,EFGDatasourceObjectInterface ds) {
		
		String message = "";
		if(this.jdbcTemplate == null){
			this.jdbcTemplate = 
				EFGRDBImportUtils.getJDBCTemplate(this.getDBObject());
			}
		String tableName = this.getTableName(ds);
		if(tableName == null){
			log.error("Table Name cannot be null");
			return;
		}
		
		StringBuffer startQuery = new StringBuffer("UPDATE ");
		startQuery.append(tableName);
		startQuery.append(" SET ");

		boolean isDone = true;
		try {
			for (int row = 0; row < sorter.getRowCount(); row++) {
				StringBuffer query = new StringBuffer(startQuery.toString());
				StringBuffer nameEndQuery = new StringBuffer(" WHERE NAME=");
			
				for (int col = 0; col < sorter.getColumnCount(); col++) {
					String colName = sorter.getColumnName(col);
					String val = sorter.getValueAt(row, col).toString();
					if (colName.equalsIgnoreCase(EFGImportConstants.NAME)) {
						nameEndQuery.append("\"");
						nameEndQuery.append(val);
						nameEndQuery.append("\"");
					} else {
						query.append(colName);
						query.append(" = ");
						if (colName.equalsIgnoreCase(EFGImportConstants.ORDER)) {
							query.append(val);
						} else {
							query.append("\"");
							query.append(val);
							query.append("\"");
						}
						if ((col + 1) < sorter.getColumnCount()) {
							query.append(",");
						}
					}
				}
				query.append(nameEndQuery.toString());

				log.debug("About to execute update query: ");
				log.debug(query.toString());
				int ret = this.jdbcTemplate.update(query.toString());

				if (ret == 0) {
					isDone = false;
					throw new Exception("Updates could not be done");
				}
			}
		} catch (Exception ee) {
			isDone = false;
			message = ee.getMessage();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error Message",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (isDone) {
			log.debug("All updates successful!!");
			JOptionPane.showMessageDialog(null,
					"The Updates to Metadata table were Successful", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}

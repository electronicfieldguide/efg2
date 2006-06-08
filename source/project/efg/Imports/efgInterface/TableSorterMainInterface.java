/**
 * $Id$
 * $Name$
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
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
 */
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
package project.efg.Imports.efgInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgImpl.EFGTableModel;
import project.efg.Imports.efgImpl.MyJComboBox;
import project.efg.Imports.efgImpl.TableSorter;
import project.efg.Imports.efgImpl.TableSorterObject;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public abstract class TableSorterMainInterface  extends JDialog{
	static final long serialVersionUID = 1;

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(TableSorterMainInterface.class);
		} catch (Exception ee) {
		}
	}
	private TableSorter sorter;
	private EFGDatasourceObjectInterface ds;
	private DBObject dbObject;
	private TableSorterObject sorterObject;
	
	public TableSorterMainInterface(DBObject dbObject, 
			EFGDatasourceObjectInterface ds, 
			JFrame frame) {
		super(frame, ds.getDisplayName(), true);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.ds = ds;
		this.dbObject = dbObject;
		this.sorterObject = 
			this.createData(this.ds);
	
		
		if(this.sorterObject != null){
			init();
		} else {
			log.error("An error has occured");
			this.close();
		}
	}
	private void init(){
		
		EFGTableModel mt = 
			new EFGTableModel(this.sorterObject);
		this.sorter = new TableSorter(mt);
		mt.setSorter(this.sorter);
		JTable table = new JTable(this.sorter);
		sorter.setTableHeader(table.getTableHeader());
		table.setPreferredScrollableViewportSize(new Dimension(750, 550));
		// Set up column sizes.

		this.initColumnSizes(table, mt);
		this.setRenderers(table);

		// Set up tool tips for column headers.
		table
				.getTableHeader()
				.setToolTipText(
						"Click to specify sorting; Control-Click to specify secondary sorting");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		JPanel pan = new JPanel();
		JButton updateBtn = new JButton(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.updatebtn"));
		updateBtn.addActionListener(new UpdateListener(this,this.sorter,this.ds));
		updateBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.updatebtn.tooltip"));

		JButton doneBtn = new JButton(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.cancelbtn"));
		doneBtn.addActionListener(new DoneListener(this));
		doneBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.cancelbtn.tooltip"));

		pan.add(updateBtn);
		pan.add(doneBtn);

		getContentPane().add(pan, BorderLayout.SOUTH);
		setSize(new Dimension(800, 570));
	}
	
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgImpl.TabelSorterMainInterface#getDBObject()
	 */
	public DBObject getDBObject(){
		return this.dbObject;
	}
	public String[] getColumnNames(){
		return this.sorterObject.getColumnNames();
	}

	/*
	 * This method picks good column sizes. If all column heads are wider than
	 * the column's cells' contents, then you can just use
	 * column.sizeWidthToFit().
	 */
	private void initColumnSizes(JTable table, EFGTableModel model) {
		TableColumn column = null;
		for (int i = 0; i < table.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			column.sizeWidthToFit();
		}
	}


	/**
	 * Set the renderers for some of the columns
	 */
	private void setRenderers(JTable table) {

		for (int i = 0; i < this.sorterObject.getColumnNames().length; i++) {
			String colName = this.sorterObject.getColumnNames()[i];

			if ((colName.equalsIgnoreCase(EFGImportConstants.NAME))
					|| (colName
							.equalsIgnoreCase(EFGImportConstants.ONTAXONPAGE))
					|| (colName.equalsIgnoreCase(EFGImportConstants.SEARCHABLE))
					|| (colName.equalsIgnoreCase(EFGImportConstants.ISLISTS))) {
			} else if (colName.equalsIgnoreCase(EFGImportConstants.ORDER)) {
				MyJComboBox myBox = new MyJComboBox();
				for (int ww = 0; ww < table.getRowCount(); ww++) {
					Integer num = new Integer(ww + 1);
					myBox.addItem(num);
				}
				table.getColumn(colName).setCellEditor(
						new DefaultCellEditor(myBox));
				// Set up tool tips for the sport cells.
				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer
						.setToolTipText("The value you select determines the order in which the fields appear on the search page");
				table.getColumn(colName).setCellRenderer(renderer);
			} else {
				table.getColumn(colName).setCellRenderer(
						new project.efg.Imports.efgImpl.RadioButtonRenderer());
				table.getColumn(colName).setCellEditor(
						new project.efg.Imports.efgImpl.RadioButtonEditor(new JCheckBox()));
			}
		}
	}
	public EFGDatasourceObjectInterface getDataSourceObject(){
		return this.ds;
	}
	/**
	 * 
	 * @param sorter
	 * @param ds
	 */
	public abstract void updateMetadataTable(TableSorter sorter,
			EFGDatasourceObjectInterface ds);

	
	/**
	 * Template method to be implemented by sub classes
	 * @param ds
	 * @return
	 */
	public abstract TableSorterObject
	createData(EFGDatasourceObjectInterface ds);
	/* *
	 * @see project.efg.Imports.efgImpl.TabelSorterMainInterface#close()
	 */
	public void close() {
		this.dispose();
	}

	class UpdateListener implements ActionListener {
		private TableSorterMainInterface sorterMain;
		private TableSorter sorter;
		private EFGDatasourceObjectInterface ds;

		public UpdateListener(TableSorterMainInterface sorterMain,
				TableSorter sorter,
				EFGDatasourceObjectInterface ds) {
			this.sorterMain = sorterMain;
			this.sorter = sorter;
			this.ds = ds;
		}

		public void actionPerformed(ActionEvent evt) {
			this.sorterMain.updateMetadataTable(sorter,ds);
		}
	}

	class DoneListener implements ActionListener {
		private TableSorterMainInterface sorterMain;

		public DoneListener(TableSorterMainInterface sorterMain) {
			this.sorterMain = sorterMain;
		}

		public void actionPerformed(ActionEvent evt) {
			this.sorterMain.close();
		}
	}
}
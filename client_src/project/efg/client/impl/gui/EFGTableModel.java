/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
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
package project.efg.client.impl.gui;

import javax.swing.table.AbstractTableModel;

import project.efg.util.interfaces.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class EFGTableModel extends AbstractTableModel {
	static final long serialVersionUID = 1;
	final MyJRadioButton mjj = new MyJRadioButton();
	// read database headers
	private MyJRadioButtonLists data;

	private String[] columnNames;
	private TableSorter sorter;
	public EFGTableModel(TableSorterObject sorterObject) {
		super();
		this.columnNames = sorterObject.getColumnNames();//columnNames;
		this.data = sorterObject.getMyJRadioButtonLists();
		
	}
	public Object getData(){
		return this.data;
	}
	public void addData(int pos,Object mydata){
		if(pos > data.size() - 1){
			data.add(mydata);
			return;
		}
		data.add(pos,mydata);
	}
	public void removeData(Object myData){
		data.remove(myData);
	}
	public int indexOf(Object myData){
		return data.indexOf(myData);
	}
	public void moveData(int oldpos, int newpos){
		if(oldpos > data.size() - 1){
			return;
		}
		Object obj = data.remove(oldpos);
		addData(newpos,obj);
	}
	public void setSorter(TableSorter sorter){
		this.sorter = sorter;
	}
	public int getColumnCount() {
		return this.columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return this.columnNames[col];
	}

	public Object getValueAt(int row, int col) {

		Object[] dataArr = (Object[]) this.data.get(row);
		return dataArr[col];
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column
	 * would contain text (EFGImportConstants.EFG_TRUE/EFGImportConstants.EFG_FALSE), rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		String colName = this.columnNames[col];

		if ((colName.equalsIgnoreCase(EFGImportConstants.NAME_DISPLAY))
				|| (colName.equalsIgnoreCase(EFGImportConstants.LEGALNAME_DISPLAY)) || 
				(colName.equalsIgnoreCase(EFGImportConstants.ORDER_DISPLAY))) {
			return false;
		}
		return true;
	}

	/*
	 * Don't need to implement this method unless your table's data can
	 * change.
	 */
	public void setValueAt(Object value, int row, int col) {
		Class cl = getValueAt(0, col).getClass();

		if(this.sorter != null){
		// The following is a hack to make radiobuttons toggle in unsorted
		// mode
		if ((cl.isInstance(mjj))
				&& (sorter.getSortingStatus(col) == TableSorter.NOT_SORTED)) {
			// get all radio buttons that belong to the same group as this
			// one and
			// set their selected value to false
			// radio buttons in the same row belong to the same button group
			for (int i = 0; i < getColumnCount(); i++) {
				cl = getValueAt(row, i).getClass();
				if (cl.isInstance(mjj)) {
					MyJRadioButton mj = (MyJRadioButton) getValueAt(row, i);
					mj.setSelected(false);
					fireTableCellUpdated(row, i);
				}
			}
		}
		}
		Object[] dataArr = (Object[]) this.data.get(row);
		dataArr[col] = value;
		fireTableCellUpdated(row, col);

	}
	
	/**
	 * @param i
	 * @return
	 */
	public Object getRow(int index) {
		return data.get(index);
	}
	/**
	 * @param rowIndex
	 */
	public void removeData(int rowIndex) {
		Object obj = getRow(rowIndex);
		this.data.remove(obj);
		
	}

}

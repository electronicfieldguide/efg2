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

/**
 * @author kasiedu
 *
 */
public class TableSorterObject {
	private MyJRadioButtonLists data;
	private String[] columnNames;
	
	/**
	 * 
	 */
	public TableSorterObject(MyJRadioButtonLists data, 
			String[] columnNames) {
		this.data = data;
		this.columnNames = columnNames;
	}
	public MyJRadioButtonLists getMyJRadioButtonLists(){
		return this.data;
	}
	public String[] getColumnNames(){
		return this.columnNames;
	}
	

}

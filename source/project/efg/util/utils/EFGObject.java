/**
 * $Id$
 *
 * Copyright (c) 2005  University of Massachusetts Boston
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

package project.efg.util.utils;




/**
 * This class is used to wrap data needed to create a Search page.
 * 
 * @see #SearchPage.jsp
 */
public class EFGObject implements java.lang.Comparable{

	private String name;
	private String databaseName;
	private String dataType;

	

	public EFGObject() {
		
	}

	public String getDatabaseName() {
		return this.databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	

	public String toString() {
		return this.name;
	}

	public boolean equals(Object obj1) {
		EFGObject efg = (EFGObject) obj1;
		return (this.getName().equalsIgnoreCase(efg.getName())
				&& 
				(this.getDataType().equalsIgnoreCase(efg.getDataType())));
			
	}

	public int hashCode() {
		return this.getName().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		EFGObject efg = (EFGObject)o;
		return this.getName().compareTo(efg.getName());
	}
	
}
// $Log: EFGObject.java,v $
// Revision 1.1.1.1  2007/08/01 19:11:27  kasiedu
// efg2.1.1.0 version of efg2
//
// Revision 1.2  2006/12/08 03:51:02  kasiedu
// no message
//
// Revision 1.1.2.3  2006/08/09 18:55:26  kasiedu
// latest code confimrs to what exists on Panda
//
// Revision 1.1.2.2  2006/06/20 13:34:11  kasiedu
// Fixed errors in image uploader
//
// Revision 1.1.2.1  2006/06/08 13:22:47  kasiedu
// New  files
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//
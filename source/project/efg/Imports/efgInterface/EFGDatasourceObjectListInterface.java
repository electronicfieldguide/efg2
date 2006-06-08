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
/**
 * EFGDatasourceObjectInterfaceList.java
 *
 *
 * Created: Sun Feb 19 09:22:49 2006
 *
 * @author <a href="mailto:kasiedu@ccs.umb.edu">Jacob K. Asiedu</a>
 * @version 1.0
 */
package project.efg.Imports.efgInterface;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.factory.StateObjectFactory;

public abstract class EFGDatasourceObjectListInterface {
	protected ArrayList lists;
	protected DBObject dbObject;
	static Logger log = null;

	static {
		try {
			log = Logger.getLogger(EFGDatasourceObjectListInterface.class);
		} catch (Exception ee) {
		}
	}
	protected StateObjectFactory stateFactory;
	
	public EFGDatasourceObjectListInterface(DBObject dbObject) {
		this.dbObject = dbObject;
		this.lists = new ArrayList();
		 this.stateFactory =
				new StateObjectFactory();
	}
	private EFGDatasourceObjectListInterface() {
		this(null);
	}
	/**
	 * @return an iterator over the members of this lists.
	 */
	public Iterator getEFGDatasourceObjectListIterator() {
		return lists.iterator();
	}
	public boolean contains(EFGDatasourceObjectInterface datasource) {
		return this.lists.contains(datasource);
	}
	public boolean contains(String displayName) {
		if (findObjectIndex(displayName) > -1) {
			return true;
		}
		return false;
	}
	/**
	 * @return the number of elements of this list
	 */
	public int getCount() {
		return this.lists.size();
	}
	/**
	 * @param displayName -
	 *            Use this display name to find the index of EFGDatasourceObject
	 *            with that displayName
	 * @return - The index of the element with that display name in the list ,
	 *         -1 if it cannot be found in the list
	 */
	protected int findObjectIndex(String displayName) {
		if ((displayName == null) || (displayName.trim().equals(""))) {
			return -1;
		}
		for (int i = 0; i < lists.size(); ++i) {
			EFGDatasourceObjectInterface datasource = (EFGDatasourceObjectInterface) lists
					.get(i);
			if (datasource.getDisplayName()
					.equalsIgnoreCase(displayName.trim())) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Remove an object from the database
	 * 
	 * @param datasource -
	 *            The datasource object to remove
	 * @return true if the datasource object was part of the list and was
	 *         removed , false otherwise.
	 */
	public abstract boolean removeEFGDatasourceObject(
			EFGDatasourceObjectInterface datasource); 

	/**
	 * Remove an object with the given display name from the database
	 * 
	 * @param datasource -
	 *            The datasource object to remove
	 * @return true if the datasource object was part of the list and was
	 *         removed , false otherwise.
	 */
	public abstract boolean removeEFGDatasourceObject(String displayName);




	/**
	 * @param oldDisplayName -
	 *            the display name to replace.
	 * @param freshDisplayName-
	 *            the display name tio be used to replace the old one.
	 * @return true if the display name for the object is successfully changed.
	 */
	public abstract boolean replaceDisplayName(String oldDisplayName,
			String newDisplayName); 

	/**
	 * Add an object to the database
	 * 
	 * @param datasource -
	 *            The datasource object to add
	 * @param isUpdate -
	 *            true,If there is an update to a data table.
	 * @return true if this datasource was successfully added, false otherwise
	 */
	public abstract boolean addEFGDatasourceObject(
			EFGDatasourceObjectInterface datasource, boolean isUpdate); 
	/**
	 * Add an object to the database
	 * 
	 * @param datasource -
	 *            The datasource object to add
	 * @param isUpdate -
	 *            true,If there is an update to a data table.
	 * @return true if this datasource was successfully added, false otherwise
	 */
	public  boolean addEFGDatasourceObject(
			EFGDatasourceObjectInterface datasource){
		return this.addEFGDatasourceObject(datasource,false);
	}
}// EFGDatasourceObjectInterfaceList


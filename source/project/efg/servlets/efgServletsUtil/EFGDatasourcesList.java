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
package project.efg.servlets.efgServletsUtil;



import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface;
import project.efg.Imports.efgInterface.ImportBehavior;

/**
 * @author kasiedu
 *
 */
public class EFGDatasourcesList extends EFGDatasourceObjectListInterface {

	/**
	 * 
	 */
	public EFGDatasourcesList(DBObject object) {
		super(object);
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface#removeEFGDatasourceObject(project.efg.Imports.efgInterface.EFGDatasourceObjectInterface)
	 */
	public boolean removeEFGDatasourceObject(EFGDatasourceObjectInterface datasource) {
		return this.lists.remove(datasource);
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface#replaceDisplayName(java.lang.String, java.lang.String)
	 */
	public boolean replaceDisplayName(String oldDisplayName, String newDisplayName) {
		try {
			int index = this.findObjectIndex(oldDisplayName);
			if (index > -1) {
			
				EFGDatasourceObjectInterface obj = (EFGDatasourceObjectInterface) this.lists
						.get(index);
				obj.setDisplayName(newDisplayName);
				return true;
			} 
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface#addEFGDatasourceObject(project.efg.Imports.efgInterface.EFGDatasourceObjectInterface, boolean)
	 */
	public boolean addEFGDatasourceObject(EFGDatasourceObjectInterface datasource, ImportBehavior  isUpdate) {
		try {
			int find = this.findObjectIndex(datasource.getDisplayName());
			if(find > -1){
				this.lists.remove(find);
			}
			datasource.setState(this.stateFactory.getSuccessObject());
			return this.lists.add(datasource);	
			
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
		}
		datasource.setState(this.stateFactory.getFailureObject());
		return false;
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectListInterface#removeEFGDatasourceObject(java.lang.String)
	 */
	public boolean removeEFGDatasourceObject(String displayName) {
		try {

			if ((displayName == null) || (displayName.trim().equals(""))) {
				return false;
			}
			int index = this.findObjectIndex(displayName);
			if (index > -1) {
			 this.lists.remove(index);
			 return true;
			}
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
		}
		return false;
	}

	
}

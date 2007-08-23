/* $Id: EFGDatasourceObjectInterface.java,v 1.1.1.1 2007/08/01 19:11:16 kasiedu Exp $
* $Name:  $
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
package project.efg.client.interfaces.nogui;

import java.net.URI;





/**
 * EFGDatasourceObjectInterface.java
 * 
 * 
 * Created: Sun Feb 19 09:22:49 2006
 * 
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * @version 1.0
 */
public interface EFGDatasourceObjectInterface  extends EFGStateInterface{
	
	/**
	 * @param templateName -
	 *            The display name of an existing Table whose MetadataTable
	 *            would be shared by the current data.
	 */
	public void setTemplateDisplayName(String displayName);

	/**
	 * @return The display name of an existing Table which could be used as
	 *         template for the current table.
	 */
	public String getTemplateDisplayName();

	/**
	 * @param dataName -
	 *            The full path to the datasource file to be imported
	 */
	public void setDataName(URI dataName);

	/**
	 * @return the name of the datasource file that is to be imported.
	 */
	public URI getDataName();

	/**
	 * @param displayName -
	 *            A human readable name for this datasource.
	 */
	public void setDisplayName(String displayName);

	/**
	 * @return a human readbale name for this datasource
	 */
	public String getDisplayName();

	/**
	 * 
	 * @return true if this object is equal to the obj
	 */
	public boolean equals(Object obj);

	/**
	 * Override the hashCode mthod.. Return the hashCode for the object
	 */
	public int hashCode();

	/**
	 * A toString for this object
	 * 
	 * @return a String representation of this object
	 */
	public String toString();
}
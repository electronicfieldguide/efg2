/* $Id$
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
package project.efg.util.interfaces;

import java.util.Comparator;

/**
 * @author kasiedu
 *
 */
public interface EFGQueueObjectInterface extends Comparator{

	/**
	 * Maintains the order in which objects are added to it
	 * @param object
	 */
	public abstract void add(String object);

	public abstract String getObject(int position);

	public abstract int getSize();
	public abstract int hashCode();
	public boolean equals(Object obj);
	public String toString();

}
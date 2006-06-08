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
package project.efg.Imports.efgInterface;

/**
 * @author kasiedu
 *
 */
public interface EFGWebAppsDirectoryInterface {

	/**
	 * @param imagesDirectory -
	 *            relative path to the images directory from the server
	 */
	public abstract void setImagesDirectory(String imagesDirectory);
	/**
	 * @return the full path to the images directory
	 */
	public abstract String getImagesDirectory();

	/**
	 * @param pathToServer -
	 *            The full path to server. If this is set after calls to
	 *            setCSSDirectory(String) or setImagesDirectory(String) then
	 *            call those methods again
	 */
	public abstract void setPathToServer(String pathDirectory);

	/**
	 * @return the full path to the server
	 */
	public abstract String getPathToServer();

}
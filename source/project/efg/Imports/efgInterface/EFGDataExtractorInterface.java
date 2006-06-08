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
 * All EFG data importers must implement this interface
 * Used to parse extract data to be imported into 
 * database
 * 
 *  @author <a href="mailto:">Jacob K Asiedu</a>
 *
 */

public interface EFGDataExtractorInterface {

	/**
	 * Get the index of the column having the given label. The first field has
	 * the index 0.
	 * 
	 * @param label-
	 *            The field name.
	 * @return The index of the field name, or -1 if the field name does not
	 *         exist.
	 */
	public int getFieldNameIndex(String label); 

	/**
	 * Given the label for the column, get the column from the current row. If
	 * the column cannot be found in the line, null is returned.
	 * 
	 * @param label -
	 *            The field name.
	 * @return the value from the column or null if there is no such value
	 */
	public String getValueByFieldName(String label);

	/**
	 * A string array of field names to be used to create a database table. The
	 * order of the strings in the array must match the order of the data values
	 * (see getRowValues below)
	 * 
	 * @return a string array of field names
	 */
	public String[] getFieldNames(); 

	/**
	 * Return the values of the next row, much like the next() method in
	 * java.util.Iterator
	 * 
	 * @return a string array of data values
	 * null if there are no more values to return
	 */
	public String[] nextValue(); 

	/**
	 * The maximum number of columns in this object
	 * 
	 * @return the number of coumns in this object
	 */
	public int getNumberOfColumns(); 
}
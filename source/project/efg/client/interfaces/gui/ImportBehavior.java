/* $Id: ImportBehavior.java,v 1.1.1.1 2007/08/01 19:11:16 kasiedu Exp $
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
package project.efg.client.interfaces.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import project.efg.client.factory.gui.SpringGUIFactory;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;

/**
 * @author kasiedu
 *
 */
public abstract class ImportBehavior {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ImportBehavior.class);
		} catch (Exception ee) {
		}
	}
	protected EFGDatasourceObjectListInterface lists;
	protected EFGDatasourceObjectInterface obj; 
	
	protected StringBuffer responseMessage;
	/**
	 * 
	 */
	public ImportBehavior(EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj) {
		this.lists=lists;
		this.obj = obj; 
	
		this.responseMessage = new StringBuffer();
		
	}
	/**
	 * 
	 * @return the state of the EFGDatasourceObjectInterface
	 * that was imported
	 */
	public abstract EFGDatasourceObjectStateInterface importIntoDatabase();
	public String getResponseMessage(){
		return this.responseMessage.toString();
	}
	/**
	 * 
	 * 
	 * @return an alphabetical list of display names
	 */
	protected String[] getAlphabeticallySortedList() {
		Iterator iter = this.lists.getEFGDatasourceObjectListIterator();
		String[] possibleValues = new String[this.lists.getCount()];
		int i = 0;
		while (iter.hasNext()) {
			possibleValues[i] = 
				((EFGDatasourceObjectInterface)iter.next())
					.getDisplayName();
			++i;
		}
		Arrays.sort(possibleValues);
		return possibleValues;
	}
	protected boolean checkLists(){
		if (this.lists.contains(obj)) {
			// create a new display name
			String freshDisplayName = this.generateNewDisplayName(obj
					.getDisplayName());
			this.obj.setDisplayName(freshDisplayName);
			while (lists.contains(obj)) {
				freshDisplayName = this.generateNewDisplayName(obj
						.getDisplayName());
				this.obj.setDisplayName(freshDisplayName);
			}
			
			if (freshDisplayName == null) {
				
				this.responseMessage.append(EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTree.renameErrorMessage") + "\n");
				log.error(this.responseMessage.toString());
				this.obj.setState(SpringGUIFactory.getFailureObject());
				return false;
			}
			this.responseMessage.append(EFGImportConstants.EFGProperties
					.getProperty("SynopticKeyTree.tableAlreadyExists")
					+ ".\n"); 
			
			this.responseMessage.append(EFGImportConstants.EFGProperties
							.getProperty("SynopticKeyTree.renameMessage")
					+ " " + freshDisplayName + "\n");
			log.debug(this.responseMessage.toString());
		}
		this.obj.setState(SpringGUIFactory.getSuccessObject());
		return true;
	}
private String getNumber(String str) {
		String numeric = null;
	
		Matcher matcher = RegularExpresionConstants.matchNumberPattern.matcher(str);
		if (matcher.find()) {
			numeric = matcher.group();
		}
		/*matcher.reset();
		String regEX = "\\d+$";
		Pattern p = Pattern.compile(regEX);
		Matcher matcher = p.matcher(str);
		if (matcher.find()) {
			numeric = matcher.group();
		}*/
		matcher.reset();
		return numeric;
	}
	/**
	 * If there is already a table with the same display name, a new name is
	 * generated as follows: If the oldDisplayName ends with a numeral that
	 * numeral is increased by one and appended to the oldDisplayName parameter.
	 * The system should recursively check that any display name generated is
	 * unique. System should notify user of the generated display name.
	 * 
	 * @param oldDisplayName -
	 *            The name to change
	 * @return the new unique display name
	 */
	private String generateNewDisplayName(String oldDisplayName) {
		String str = this.getNumber(oldDisplayName);
		// Integer ger = null;
		int j = 1;

		if (str != null) {
			int index = oldDisplayName.lastIndexOf(str);
			j = Integer.parseInt(str);
			j = j + 1;
			if (index > -1) {
				oldDisplayName = oldDisplayName.substring(0, index);
			}
		}
		return oldDisplayName + j;
	}
}

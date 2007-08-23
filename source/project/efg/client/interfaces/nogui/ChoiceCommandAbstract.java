/* $Id: ChoiceCommandAbstract.java,v 1.1.1.1 2007/08/01 19:11:16 kasiedu Exp $
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

import org.apache.log4j.Logger;

import project.efg.client.interfaces.gui.EFGDatasourceObjectListInterface;
import project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface;
import project.efg.client.interfaces.gui.ImportBehavior;


/**
 * @author kasiedu
 *
 */
public abstract class ChoiceCommandAbstract {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(ChoiceCommandAbstract.class);
		} catch (Exception ee) {
		}
	}
	private String responseMessage;
	/**
	 * 
	 */
	public ChoiceCommandAbstract() {
		
	}
	/**
	 * 
	 * @return true if execution was successful, 
	 * false otherwise
	 * @throws SetterNotCalledException 
	 */
	private EFGDatasourceObjectStateInterface execute(ImportBehavior behavior){
		return behavior.importIntoDatabase(); 
	}
	public String getResponseMessage(){
		return this.responseMessage;
	}
	/**
	 * 
	 * @return true if execution was successful, 
	 * false otherwise
	 * @throws SetterNotCalledException 
	 */
	public EFGDatasourceObjectStateInterface execute(EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj){
		ImportBehavior behavior = this.createImportBehavior(lists,obj);
		
		EFGDatasourceObjectStateInterface state = this.execute(behavior);
		this.responseMessage = behavior.getResponseMessage();
		return state;
	}
	public abstract ImportBehavior createImportBehavior(
			EFGDatasourceObjectListInterface lists,
			EFGDatasourceObjectInterface obj);
	
}

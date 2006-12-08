/**
 * $Id$
 * $Name$
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
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
package project.efg.Imports.efgInterface;

import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.apache.log4j.Logger;


import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public abstract class DataManipulatorInterface {
	private SynopticKeyTreeInterface tree;
	static Logger log;
	static {
		try {
			log = Logger.getLogger(DataManipulatorInterface.class);
		} catch (Exception ee) {
		}
	}
	public DataManipulatorInterface(SynopticKeyTreeInterface tree){
		this.tree = tree;
	}	
	public abstract boolean processNode();
	public SynopticKeyTreeInterface getSynopticKeyTreeInterface(){
		return this.tree;
	}
	private String getNumber(String str) {
		String numeric = null;
		/*String regEX = "\\d+$";
		Pattern p = Pattern.compile(regEX);
		Matcher matcher = p.matcher(str);*/
		Matcher matcher = EFGImportConstants.matchNumberPattern.matcher(str);
		if (matcher.find()) {
			numeric = matcher.group();
		}
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
	protected String generateNewDisplayName(String oldDisplayName) {
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
	/**
	 * This method returns the previous sibling node if there is no previous
	 * sibling it returns the next sibling if there are no siblings it returns
	 * null
	 * 
	 * @param selNode
	 *            selected node
	 * @return previous or next sibling, or parent if no sibling
	 */
	protected MutableTreeNode getSibling(DefaultMutableTreeNode selNode) {
		// get previous sibling
		MutableTreeNode sibling = selNode
				.getPreviousSibling();
		if (sibling == null) {
			// if previous sibling is null, get the next sibling
			sibling = selNode.getNextSibling();
		}
		return sibling;
	}
	protected void rootNodeUnEditable(String message) {

	
		//log.info(message);
		JOptionPane.showMessageDialog(this.tree.frame, message, "Warning Message",
				JOptionPane.WARNING_MESSAGE);
	}
	protected void selectDatasourceFirst() {
		String message = EFGImportConstants.EFGProperties
				.getProperty("SynopticKeyTree.selectClick");
		log.error(message);
		JOptionPane.showMessageDialog(this.tree.frame, message, "Error Message",
				JOptionPane.ERROR_MESSAGE);
	}
}

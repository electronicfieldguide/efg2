/**
 * $Id: TreeStringComparatorImpl.java,v 1.1.1.1 2007/08/01 19:11:15 kasiedu Exp $
 * $Name:  $
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
package project.efg.client.impl.gui;

import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;

import java.util.Comparator;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeStringComparatorImpl implements Comparator {
	public int compare(Object o1, Object o2) {
		if (!(o1 instanceof DefaultMutableTreeNode && o2 instanceof DefaultMutableTreeNode)) {
			throw new IllegalArgumentException(
					"Can only compare DefaultMutableTreeNode objects");
		}
		EFGDatasourceObjectInterface s1 = (EFGDatasourceObjectInterface) (((DefaultMutableTreeNode) o1)
				.getUserObject());
		EFGDatasourceObjectInterface s2 = (EFGDatasourceObjectInterface) (((DefaultMutableTreeNode) o2)
				.getUserObject());
		return s1.toString().compareToIgnoreCase(s2.toString());
	}
}

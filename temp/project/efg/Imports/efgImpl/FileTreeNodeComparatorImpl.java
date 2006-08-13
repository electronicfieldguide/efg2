package project.efg.Imports.efgImpl;
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
import java.util.Comparator;



public class FileTreeNodeComparatorImpl implements Comparator {

	public boolean equals(Object o) {
		return false;
	}

	public int compare(Object o1, Object o2) {
		FileTreeNode node1 = (FileTreeNode) o1;
		FileTreeNode node2 = (FileTreeNode) o2;

		// Directories come first
		if (node1.isDir() != node2.isDir()) {
			return node1.isDir() ? -1 : +1;
		}

		// Both directories or both files -
		// compare based on pathname
		return node1.getFullName().compareTo(node2.getFullName());
	}
	

}

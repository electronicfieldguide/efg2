package project.efg.server.utils;
/**
 * $Id$
 * $Name:  $
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import project.efg.efgDocument.ItemsType;
import project.efg.server.interfaces.EFGDocumentSorter;

public class EFGTypeSorter implements EFGDocumentSorter{
 private Set sortedSet;
 private ItemsType efgType;
 
 	public EFGTypeSorter(){
	  
 	}
	/* (non-Javadoc)
	 * @see project.efg.util.EFGDocumentSorter#sort(java.lang.Object)
	 */
	public void sort(Comparator compare,Object sorter) {
		this.efgType = (ItemsType)sorter;
		this.sortedSet = Collections.synchronizedSet(new TreeSet(compare));
		Enumeration enums = this.efgType.enumerateItem();
		while(enums.hasMoreElements()){
			this.sortedSet.add(enums.nextElement());
		}
	}
	/* (non-Javadoc)
	 * @see project.efg.util.EFGDocumentSorter#getIterator()
	 */
	public Iterator getIterator(){
		return sortedSet.iterator();
	}
}

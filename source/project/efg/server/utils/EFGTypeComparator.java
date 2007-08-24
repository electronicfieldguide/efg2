package project.efg.server.utils;
/**
 * $Id: EFGTypeComparator.java,v 1.1.1.1 2007/08/01 19:11:25 kasiedu Exp $
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
import java.util.Comparator;

import project.efg.efgDocument.EFGType;

public class EFGTypeComparator implements Comparator {

	/**
	 * Compares the toString() methods of o1 to o2
	 * @param o1 - the first object
	 * @param o2 - the second object
	 * @return o1.getContent().compareTo(o2.getContent())
	 */
	public int compare(Object o1, Object o2) {
		EFGType type1 = (EFGType)o1;
		EFGType type2 = (EFGType)o2;
		return type1.getContent().compareTo(type2.getContent());
	}
}

/**
 * $Id$
 *
 * Copyright (c) 2005  University of Massachusetts Boston
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

package project.efg.client.utils.nogui;
import project.efg.util.utils.EFGObject;

/**
 * This class is used as a comparator for objects to be used on serachable
 * pages. Objects on Serachable pages are sorted by weight.
 * 
 * @see #RDBDataSourceHelper.java
 */
public class EFGObjectComparatorImpl implements java.util.Comparator {

	

	public final int compare(Object object1, Object object2) {
		EFGObject dbean1 = (EFGObject) object1;
		EFGObject dbean2 = (EFGObject) object2;

		
		return dbean1.getName().compareTo(dbean2.getName());
	}
} // EFGObjectComparator
// $Log: EFGObjectComparatorImpl.java,v $
// Revision 1.1.1.1  2007/08/01 19:11:19  kasiedu
// efg2.1.1.0 version of efg2
//
// Revision 1.2  2006/12/08 03:50:58  kasiedu
// no message
//
// Revision 1.1.2.1  2006/06/08 13:27:40  kasiedu
// New files
//
// Revision 1.1.1.1 2006/01/25 21:03:48 kasiedu
// Release for Costa rica
//
/**
 * $Id$
 *
 * Copyright (c) 2005  University of Massachusetts Boston
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

package project.efg.efgInterface;

import java.util.*;

/**
 * This class is used as a comparator for objects to be used on serachable pages.
 * Objects on Serachable pages are sorted by weight.
 * @see #RDBDataSourceHelper.java
 */
public class EFGObjectComparator  implements java.util.Comparator {
      
    public EFGObjectComparator(){
	
    }
    public final int compare (Object object1, Object object2){
	EFGObject dbean1 = (EFGObject)object1;
	EFGObject dbean2 = (EFGObject)object2;
	
	Double d1 = new Double(dbean1.getWeight());
	Double d2 = new Double(dbean2.getWeight());

	int comp = d2.compareTo(d1); 
	if(comp == 0){
	    return dbean1.getName().compareTo(dbean2.getName());
	}
	return comp;
    }
} // EFGObjectComparator
//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//
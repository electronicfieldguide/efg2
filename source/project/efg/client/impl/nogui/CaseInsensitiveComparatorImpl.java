/**
 * $Id: CaseInsensitiveComparatorImpl.java,v 1.1.1.1 2007/08/01 19:11:16 kasiedu Exp $
 *
 * Copyright (c) 2006  University of Massachusetts Boston
 *
 * Authors: Jacob K. Asiedu<kasiedu@cs.umb.edu>
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

package project.efg.client.impl.nogui;

import org.apache.log4j.Logger;

/**
 * Compares two string in a case insensitive way
 */
public class CaseInsensitiveComparatorImpl implements java.util.Comparator {

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(CaseInsensitiveComparatorImpl.class);
		} catch (Exception ee) {
		}
	}

	public int compare(Object o1, Object o2) {
		try {
			String s1 = o1.toString().toUpperCase();
			String s2 = o2.toString().toUpperCase();
			return s1.compareToIgnoreCase(s2);
		} catch (Exception ee) {
			//log.error(ee.getMessage());
		}
		return -1;
	}
}

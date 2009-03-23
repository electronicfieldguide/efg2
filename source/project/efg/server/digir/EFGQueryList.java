package project.efg.server.digir;
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
import java.util.ArrayList;

/**
 * A wrapper around a java.util.ArrayList. To be used later in implementing the
 * IN operator
 * 
 */
public class EFGQueryList extends ArrayList {

	static final long serialVersionUID = 1;

	public EFGQueryList() {
		super();
	}

	public Object clone() {
		EFGQueryList clonedList = new EFGQueryList();
		for (int i = 0; i < this.size(); i++) {
			String child = (String) this.get(i);
			clonedList.add(child);
		}
		return clonedList;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder(this.size() * 2);
		for (int i = 0; i < this.size(); i++) {
			buf.append((String) this.get(i));
			buf.append(" ");
		}
		return buf.toString();
	}

}

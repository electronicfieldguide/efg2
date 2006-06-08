package project.efg.Imports.efgImportsUtil;
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
import java.util.ArrayList;

import project.efg.Imports.efgInterface.EFGQueueObjectInterface;

public class EFGQueueObject implements EFGQueueObjectInterface{
	private ArrayList list = new ArrayList();
	
	/* (non-Javadoc)
	 * @see project.efg.util.EFGQueueObjectInterface#add(java.lang.String)
	 */
	public void add(String object){ 
		this.list.add(object);
	}
	/* (non-Javadoc)
	 * @see project.efg.util.EFGQueueObjectInterface#getObject(int)
	 */
	public String getObject(int position){
		try{
			return (String)this.list.get(position);
		}
		catch(Exception ee){
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see project.efg.util.EFGQueueObjectInterface#getSize()
	 */
	public int getSize(){
		return list.size();
	}
}

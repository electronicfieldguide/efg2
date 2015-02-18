/**
 * $Id$
 *
 * Copyright (c) 2006  University of Massachusetts Boston
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
package project.efg.util.interfaces;

import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;

/**
 * @author kasiedu
 *
 */
public interface EFGDataObject{
	/**
	 * 
	 * @param order in which this object appears in a list
	 */
	public void setOrder(int order);
	/**
	 * 
	 * @return the order in which this object appears in a list 
	 */
	public int getOrder();
	
	/**
	 * 
	 * @return the name for the current object
	 */
	public String getName();
	
	/**
	 * 
	 * @param name - The name for the current object
	 */
	public void setName(String name);
	
	/**
	 * 
	 * @return the legalName for the current object
	 */
	public String getLegalName();
	
	/**
	 * 
	 * @param legalName - The legalName for the current object
	 */
	public void setLegalName(String key);
	/**
	 * 
	 * @return a list of values for the key
	 */
	public ItemsType getStates();
	/**
	 * 
	 * @param list - The list to set for the key
	 */
	public void setStates(ItemsType list);
	/**
	 * 
	 * @param list - The list to set for the key
	 */
	public void setStatisticalMeasures(StatisticalMeasuresType list);
	/**
	 * 
	 * @param list - The list to set for the key
	 */
	public StatisticalMeasuresType getStatisticalMeasures();
	/**
	 * 
	 * @return
	 */
	public EFGListsType getEFGLists();
	/**
	 * 
	 * @param lists
	 */
	public void setEFGListsType(EFGListsType lists);
	/**
	 * 
	 * @return
	 */
	public MediaResourcesType getMediaResources();
	/**
	 * 
	 * @param mediaResources
	 */
	public void setMediaResources(MediaResourcesType mediaResources);
	/**
	 * 
	 * @param object
	 * @return
	 */
	public boolean equals(EFGDataObject object);
	/**
	 * 
	 * @return
	 */
	public int hashCode();

}

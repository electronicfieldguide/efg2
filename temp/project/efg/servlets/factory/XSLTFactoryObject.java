/**
 * $Id$
 * $Name$
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
package project.efg.servlets.factory;



import project.efg.servlets.efgInterface.XSLTFactoryInterface;
import project.efg.servlets.efgServletsUtil.XSLTObjectInterface;
import project.efg.util.EFGImportConstants;


/**
 * @author kasiedu
 *
 */
public class XSLTFactoryObject {
	
	private static XSLTFactoryInterface xsltFactory = null;
	/**
	 * 
	 */
	public XSLTFactoryObject() {}
	/**
	 * 
	 * @param taxonSize - The number of taxa 
	 * @param searchType - The type of search - list,plates,taxonpages
	 * @return an XSLTObjectInterface 
	 */
	public static  synchronized XSLTObjectInterface getInstance(int taxonSize, 
			String searchType){
			
			if(xsltFactory == null){
				xsltFactory = new XSLTFactory();
			}
			if (taxonSize == 1) {
				//log.debug("Returning a Taxon Page");
				return xsltFactory.createTaxonPage();
			}
			
			if (EFGImportConstants.SEARCH_PLATES_TYPE.equalsIgnoreCase(searchType)) {
					//log.debug("Returning a Search Plates Page");
				return  xsltFactory.createSearchPlatesPage();
			} else {
				//log.debug("Returning a Search List Page");
				return xsltFactory.createSearchListsPage();
			}
		
	}
}

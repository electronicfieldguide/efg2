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
import project.efg.servlets.efgServletsUtil.SearchPageHtmlLists;
import project.efg.servlets.efgServletsUtil.SearchPageHtmlPlates;
import project.efg.servlets.efgServletsUtil.TaxonPageHtml;
import project.efg.servlets.efgServletsUtil.XSLTObjectInterface;

/**
 * @author kasiedu
 *
 */
public class XSLTFactory implements XSLTFactoryInterface {

	/**
	 * 
	 */
	public XSLTFactory() {
		super();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.factory.XSLTFactoryInterface#createTaxonPage()
	 */
	public XSLTObjectInterface createTaxonPage(){
		return new TaxonPageHtml();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.factory.XSLTFactoryInterface#createSearchPlatesPage()
	 */
	public XSLTObjectInterface createSearchPlatesPage(){
		return new SearchPageHtmlPlates();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.factory.XSLTFactoryInterface#createSearchListsPage()
	 */
	public XSLTObjectInterface createSearchListsPage(){
		return new SearchPageHtmlLists();
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.XSLTFactoryInterface#createNoSearchCriteriaPage()
	 */
	public XSLTObjectInterface createNoSearchCriteriaPage() {
		return null;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.XSLTFactoryInterface#createErrorPage()
	 */
	public XSLTObjectInterface createErrorPage() {
		
		return null;
	}
}

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
import java.util.Properties;

/**
 * EFGProperties.java
 * 
 * 
 * Created: Mon Nov 29 08:48:25 2004
 * 
 * @author <a href="mailto:"></a>
 * @version 1.0
 */
public class EFGProperties {
	private static final String DEFAULT_SEARCH_PAGE_VIEWER = "Plates";

	private static final String LIST_SEARCH_PAGE_VIEWER = "Lists";

	private static final String NO_GROUP_BY = "nogroupby";

	private static final String GROUP_BY = "groupby";

	public EFGProperties() {

	} // EFGProperties constructor

	public synchronized static void setEFGProperty(String name, String value) {
		System.setProperty(name, value);

	}

	public synchronized static String getEFGProperty(String name) {
		return project.efg.util.EFGImportConstants.EFGProperties.getProperty(name);
	}

	public synchronized static String getDefaultSearchPageViewer() {
		return DEFAULT_SEARCH_PAGE_VIEWER;
	}

	public synchronized static String getSearchPageViewer(String viewer) {
		if (!viewer.equalsIgnoreCase(DEFAULT_SEARCH_PAGE_VIEWER)) {
			return LIST_SEARCH_PAGE_VIEWER;
		}
		return DEFAULT_SEARCH_PAGE_VIEWER;
	}

	public synchronized static String getXSLTFileName(String datasource,
			String viewer, String groupby) {
		// Use the datasource name to get the current datasource from xml
		// configuration file
		// use the groupby name to get the name of the text in the relevant
		// element and use that to get the
		// name of the xsl file associated with it.
		String fileName = datasource + "SearchResult2html_" + viewer + "_"
				+ groupby;
		String xslFileName = project.efg.util.EFGImportConstants.EFGProperties.getProperty(fileName);
		if (xslFileName == null) {
			return ("SearchResult2html_" + viewer + "_" + groupby + ".xsl");
		}
		return xslFileName.trim();
	}

	public synchronized static String getSearchPageGroupBy(String groupby) {
		if ((groupby == null) || (groupby.trim().equals(""))) {
			return NO_GROUP_BY;
		}
		return GROUP_BY;
	}

	public synchronized static void setEFGProperties(Properties p) {
		System.setProperties(p);
	}

	public synchronized static String getDefaultSearchPageImageToDisplay(
			String datasource) {
		return project.efg.util.EFGImportConstants.EFGProperties.getProperty(
				datasource + "_" + "defaultSearchPageImageDisplay").trim();
	}

	public synchronized static String getDefaultSearchPageCaption(
			String datasource) {
		return System
				.getProperty(datasource + "_" + "defaultSearchPageCaption")
				.trim();
	}
} // EFGProperties

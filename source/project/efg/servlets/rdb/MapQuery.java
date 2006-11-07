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
package project.efg.servlets.rdb;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import project.efg.Imports.efgImportsUtil.EFGUtils;
import project.efg.servlets.efgInterface.ServletAbstractFactoryInterface;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.servlets.factory.ServletAbstractFactoryCreator;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 * 
 */
public class MapQuery extends SQLQuery {
	private ServletAbstractFactoryInterface servFactory;

	/**
	 * Constructor.
	 */
	public MapQuery(HttpServletRequest req) {
		super(req);

	}

	/**
	 * Set the datasourceName and the metadataSourceName if they were not passed
	 * in the query request.
	 * 
	 * @throws Exception
	 * 
	 */
	protected boolean initQueryParameters() {
		try {
			boolean bool = super.initQueryParameters();
			if (!bool) {
				if (this.datasourceName == null) {

					if (this.servFactory == null) {
						this.servFactory = ServletAbstractFactoryCreator
								.getInstance();
					}
					List dataSources = toLists(this.servFactory
							.getListOfDatasources());
					if (dataSources == null) {
						return false;
					}
					//do a better job here query all datasources until you find one that can han
					this.datasourceName = (String) dataSources.get(0);
				}
			}
			return true;
		} catch (Exception ee) {
			// log.error(ee.getMessage());
			LoggerUtilsServlet.logErrors(ee);
		}
		return false;
	}

	/**
	 * This method builds a query from the request such as
	 * /efg/servlet/search?Genus=Solanum&Species=chrysotrichum
	 * /efg/servlet/search?searchStr=( Genus=Solanum && Species=chrysotrichum )
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the query string
	 */
	public String buildQuery(HttpServletRequest req) {
		this.datasourceName = req
				.getParameter(EFGImportConstants.DATASOURCE_NAME);
		this.displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);

		if (!super.initQueryParameters()) {
			return null;
		}

		String maxDispStr = req.getParameter(EFGImportConstants.MAX_DISPLAY);
		int maxDisplay = this.getMaxDisplay(maxDispStr);

		StringBuffer querySB = new StringBuffer();
		try {
			querySB.append(this.getCommonQuery());

			Enumeration paramEnum = req.getParameterNames();
			int paramNo = 0;

			while (paramEnum.hasMoreElements()) {
				String legalName = (String) paramEnum.nextElement();
				// log.debug("paramName: " + legalName);
				if (isIgnoreParam(legalName.toLowerCase())) {// ignore this parameter name
					continue;
				}
				
				String[] paramValues = req.getParameterValues(legalName);
				// log.debug("paramaValues length: " + paramValues.length);
				legalName = EFGUtils.encodeToJavaName(legalName);
			
			//find a datasource with that field if is absent
				//if this is a new nae find out if an old name has been found
				//find out if th
				String orBuffer = this.getORQuery(paramValues, legalName.toLowerCase());

				if (!"".equals(orBuffer.trim())) {
					if (paramNo == 0) {
						querySB.append(" where ");
						querySB.append("( ");
						querySB.append(orBuffer);
						querySB.append(" ) ");
					} else {
						querySB.append(" and ( ");
						querySB.append(orBuffer);
						querySB.append(" ) ");
					}
					++paramNo;
				}

			}
			if (maxDisplay > 0) {
				String database = EFGImportConstants.EFGProperties
						.getProperty("database");
				if (EFGImportConstants.MYSQL.equalsIgnoreCase(database)) {
					querySB.append(" limit ");
					querySB.append(maxDisplay + "");
				}
			}
		} catch (Exception e) {
			// log.error(e.getMessage());
			
			LoggerUtilsServlet.logErrors(e);
		}
		// log.debug("Query: " + querySB.toString());
		return querySB.toString();
	}

}

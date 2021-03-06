/**
 * $Id$
 * $Name:  $
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
package project.efg.server.impl;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import project.efg.efgDocument.EFGDocument;
import project.efg.server.interfaces.ResponseObject;
import project.efg.server.utils.XSLTObjectInterface;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.EFGDocTemplate;

public class XSLTResponseObject extends ResponseObject
{

	static Logger log = null;
	static {
		try {
			//log = Logger.getLogger(XSLTResponseObject.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * @param req
	 * @param xslt
	 * @param efgDocument
	 * @param taxonSize
	 */
	public XSLTResponseObject(HttpServletRequest req,
			 EFGDocTemplate efgDocument, String realPath) {
		super(req,  efgDocument, realPath);
		
	}
	private Map copyMap(Map map){
		//log.debug("Inside copyMap");
		Map newmap = new HashMap(map.size());
		    Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        String key = (String)pairs.getKey();
		        String[] val = (String[])pairs.getValue();
		        //log.debug("About to put: " +  key +  "  and   " + val[0] );
		        newmap.put(key,val);
		    }
		return newmap;
	}
	private String getDatasourceName(EFGDocument efgDocument) throws Exception {
		String dsName = null;
		
		try {
			for (int i = 0; i < efgDocument.getDatasources()
			.getDatasourceCount(); i++) {
				dsName = efgDocument.getDatasources().getDatasource(i)
				.getName();
				if ((dsName == null) || (dsName.trim().equals(""))) {
					continue;
				}
				break;
			}
			if (dsName == null) {
				throw new Exception("A datsourceName could not be found");
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
			//LoggerUtilsServlet.logErrors(ee);
			//throw ee;
		}
		return dsName;
		
	}
	private void setRequests(HttpServletRequest req, Properties props) {
		
		for (Enumeration e = props.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = (String) props.get(key);
			req.setAttribute(key, val);
		}
	}
	/**
	 * Determine the servlet to forward to from xslt.
	 * Not a scalable solution at all...Use a Factory instead.
	 * @param xslFileName
	 * @return the servlet to do xslt transformation
	 * 
	 */
	private String createForwardString(String xslFileName){
		if (xslFileName.endsWith(".fo")) {
			return EFGImportConstants.FOPSERVLET; 
		}
		return EFGImportConstants.APPLYXSL;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ResponseDecorator#createForwardPage()
	 */
	protected void createForwardPage() {
		String displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);
		
		if((displayName != null) && (!displayName.trim().equals(""))){
			req.setAttribute(EFGImportConstants.DISPLAY_NAME, displayName);
		}
		
		String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
		boolean noDSName = false;
		
		try {
			if (dsName == null) {
				try {
					dsName = this.getDatasourceName(efgDocument.getEFGDocument());
					//log.debug("DatasourceName obtained from EFGDocument: " + dsName);
					noDSName = true;
				} catch (Exception ee) {
					//log.error(ee.getMessage());
				}
			}
			String guid = req.getParameter(EFGImportConstants.GUID);
			if((guid != null) && (!guid.trim().equals(""))){
				req.setAttribute(EFGImportConstants.GUID,guid);
			}
			Map map = req.getParameterMap();
			Map mapNew = null;
			try{
				//log.debug("About to copy  map. Size is: " + map.size());
				mapNew = copyMap(map);
				//log.debug("Done copying map . Size is: " + mapNew.size());
			}
			catch(Exception iii){
				//log.debug("Exception occured in copying map");
			}
			if(dsName != null){
				if(noDSName){
					String[] aValue = new String[1];
					aValue[0] = dsName;
					//log.debug("About to add to map. Size is: " + mapNew.size());
					mapNew.put(EFGImportConstants.DATASOURCE_NAME,aValue);
					//log.debug("Done adding map . Size is: " + mapNew.size());
				}
				req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
			}
			else{
				//log.debug("DatasourceName is null");
			}
			int taxonSize = this.getTaxonSize();
			
			
			
			req.setAttribute(EFGImportConstants.XML, efgDocument);
			req.setAttribute(EFGImportConstants.TAXONSIZE_STR,taxonSize+"");
		
			
			XSLTObjectInterface xslType = 
				this.getXSLTObject();
			xslType.setMainDataTableName(this.mainTableName);
			
			//log.debug("call xslProps");
			project.efg.server.utils.XSLProperties xslProps = xslType.getXSLProperties(mapNew);
			//log.debug("Done xslProps");
			if (xslProps == null) {
				throw new Exception(
						"Could not find xsl file for: " + 
						dsName
						);
			}
			
			
			Properties props = xslProps.getXSLParameters();
			//log.debug("call props");
			if (props != null) {
				this.setRequests(req, props);
			}
			//log.debug("Done props");
			String xslFileName = xslProps.getXSLFileName();
			//log.debug("call xslFileName: " + xslFileName);
			req.setAttribute(EFGImportConstants.XSL_STRING, xslFileName);
			
			// get xsl file name from the templateConfig file name
			StringBuilder forwardStringBuilder = 
			new StringBuilder(req.getContextPath());
			forwardStringBuilder.append("/");
			forwardStringBuilder
			.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
			forwardStringBuilder.append("/");
			forwardStringBuilder.append(xslFileName);
			
			StringBuilder fwdStrEncodedBuffer = new StringBuilder("/");
			//forward to pdf or html xslt servlet
			fwdStrEncodedBuffer.append(this.createForwardString(xslFileName));
			fwdStrEncodedBuffer.append("?xsl=");
			fwdStrEncodedBuffer.append(
					URLEncoder.encode(forwardStringBuilder.toString(), 
							"UTF-8")
							);
			this.forwardPage = fwdStrEncodedBuffer.toString();
		}
		catch(Exception ee){
			ee.printStackTrace();
			//log.error(ee.getMessage());
			//create fwd page and return its page
		}
	}
}

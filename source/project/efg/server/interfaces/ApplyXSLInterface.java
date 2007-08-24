/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2007  University of Massachusetts Boston
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
package project.efg.server.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;

/**
 * @author jacob.asiedu
 *
 */
public interface ApplyXSLInterface {

	public abstract Source getXMLSource(Object srcDoc);

	/**
	 * Get the xml document as a String or a jdom Document from the
	 * HttpServletRequest object and transform it to a jdom Source.
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the jdom Source transformed from the xml document
	 */
	public abstract Source getXMLSource(HttpServletRequest req);

	/**
	 * Get the xsl filename as a String from the HttpServletRequest object and
	 * transform it to a  Source object.
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the Source transformed from the xml document
	 */
	public abstract Source getXSLSource(HttpServletRequest req);

}

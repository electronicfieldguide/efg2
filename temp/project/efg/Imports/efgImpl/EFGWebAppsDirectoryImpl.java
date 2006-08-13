package project.efg.Imports.efgImpl;
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
/**
 * EFGWebAppsDirectoryObject.java
 *
 *
 * Created: Thu Feb 23 14:45:53 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */

import java.io.File;

import org.apache.log4j.Logger;

import project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface;

public class EFGWebAppsDirectoryImpl implements EFGWebAppsDirectoryInterface {

	private String pathToServer;

	protected String imagesDirectory;

	protected String cssDirectory;
	

	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGWebAppsDirectoryImpl.class);
		} catch (Exception ee) {
		}
	}

	public EFGWebAppsDirectoryImpl(String pathToServer) {
		this.pathToServer = pathToServer;
	} // EFGWebAppsDirectoryObject constructor

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface#setImagesDirectory(java.lang.String)
	 */
	public void setImagesDirectory(String imagesDirectory) {
		if (imagesDirectory == null) {
			return;
		}
		if ((this.getPathToServer() != null)
				&& (!"".equals(this.getPathToServer().trim()))) {
			this.imagesDirectory = this.getPathToServer() + File.separator
					+ imagesDirectory.trim();
		} else {
			String catalina_home = project.efg.Imports.efgImportsUtil.EFGUtils.getCatalinaHome();
			if ((catalina_home != null) && (!"".equals(catalina_home.trim()))) {
				this.setPathToServer(catalina_home + File.separator
						+ project.efg.util.EFGImportConstants.EFG_WEB_APPS
						+ File.separator
						+ project.efg.util.EFGImportConstants.EFG_APPS);
				this.setImagesDirectory(imagesDirectory.trim());
			}
		}
	}



	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface#getImagesDirectory()
	 */
	public String getImagesDirectory() {
		return this.imagesDirectory;
	}

	

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface#setPathToServer(java.lang.String)
	 */
	public void setPathToServer(String pathDirectory) {
		this.pathToServer = pathDirectory;
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGWebAppsDirectoryInterface#getPathToServer()
	 */
	public String getPathToServer() {
		return this.pathToServer;
	}
} // EFGWebAppsDirectoryObject

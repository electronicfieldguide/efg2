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
 * EFGDatasourceObjectInterface.java
 *
 *
 * Created: Sun Feb 19 09:22:49 2006
 *
 * @author <a href="mailto:">Jacob Admin</a>
 * @version 1.0
 */
package project.efg.Imports.efgImpl;

import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface;

import java.net.URI;

public class EFGDatasourceObjectImpl implements EFGDatasourceObjectInterface {
	private String displayName;

	private String templateDisplayName;
	private EFGDatasourceObjectStateInterface state;
	private URI dataName;

	public EFGDatasourceObjectImpl() {
		this(null, "", "");
	}

	/**
	 * @param dataName -
	 *            The full path to the file we are trying to import
	 * @param templateDisplayName -
	 *            The display name whose metadata table will be used by this
	 *            file for importation
	 * @param displayname -
	 *            the name to be used to display this datasource
	 */
	public EFGDatasourceObjectImpl(URI dataName, String templateDisplayName,
			String displayName) {

		this.setDataName(dataName);
		this.setTemplateDisplayName(templateDisplayName);
		this.setDisplayName(displayName);
		this.setState(new NeutralStateObject());
	}

	/**
	 * @param templateName -
	 *            The display name of an existing Table whose metadata table
	 *            could be used by the current importation.
	 */
	public void setTemplateDisplayName(String templateName) {
		this.templateDisplayName = templateName;
	}

	/**
	 * @return The display name of an existing Table which should be used as
	 *         template for the current table.
	 */
	public String getTemplateDisplayName() {
		return this.templateDisplayName;
	}

	/**
	 * @param dataName -
	 *            The full path to the datasource file to be imported
	 */
	public void setDataName(URI dataName) {
		this.dataName = dataName;
	}

	/**
	 * @return the full path to the datasource file that is to be imported.
	 */
	public URI getDataName() {
		return this.dataName;
	}

	/**
	 * @param displayName -
	 *            A human readable name for this datasource.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return a human readbale name for this datasource
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	public boolean equals(Object obj) {
		EFGDatasourceObjectInterface eds = (EFGDatasourceObjectInterface) obj;
		if ((this.getDisplayName() == null) || (eds.getDisplayName() == null)) {
			return false;
		}
		if ((this.getDisplayName().trim().equals(""))
				|| (eds.getDisplayName().trim().equals(""))) {
			return false;
		}
		return this.getDisplayName().equalsIgnoreCase(eds.getDisplayName());

	}

	public int hashCode() {
		if (this.getDisplayName() != null) {
			return this.getDisplayName().hashCode();
		}
		return -1;
	}

	public String toString() {
		return this.getDisplayName();
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGStateInterface#getState()
	 */
	public EFGDatasourceObjectStateInterface getState() {
		return this.state;
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGStateInterface#setState(project.efg.Imports.efgInterface.EFGDatasourceObjectStateInterface)
	 */
	public void setState(EFGDatasourceObjectStateInterface state) {
		this.state = state;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		EFGDatasourceObjectInterface state = 
			(EFGDatasourceObjectInterface)o;
		return this.getDisplayName().compareTo(state.getDisplayName());
	}
}// EFGDatasourceObject

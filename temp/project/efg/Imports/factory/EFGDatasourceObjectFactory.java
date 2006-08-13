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
package project.efg.Imports.factory;

import java.net.URI;

import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.efgImpl.EFGDatasourceObjectImpl;
/**
 * @author kasiedu
 *
 */
public class EFGDatasourceObjectFactory{

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgInterface.EFGDatasourceObjectFactoryInterface#getEFGDatasourceObject()
	 */
	public static synchronized EFGDatasourceObjectInterface getEFGDatasourceObject(URI dataName, String templateDisplayName,
			String displayName) {
		return new EFGDatasourceObjectImpl(dataName,templateDisplayName,
				displayName);
	}
	public static synchronized EFGDatasourceObjectInterface getEFGDatasourceObject() {
		return new EFGDatasourceObjectImpl();
	}
}

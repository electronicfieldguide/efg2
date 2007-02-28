/* $Id$
* $Name$
* Created: Tue Feb 28 13:14:19 2006
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
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
* Imports a csv file into a relational database
* 
*/
package project.efg.Imports.efgInterface;

import java.util.Properties;


public interface EFGToolsUtils {
	Properties properties = project.efg.Imports.rdb.EFGRDBImportUtils.getProperties();

	// The name of the efg logo image
	String ABOUT_ICON = "/icons/efglogo.jpg";

	String HELP_DIR = "help";

	// The EFG Contact information
	String EFG_CONTACT_MESSAGE = "Contact us: " +  properties.getProperty("efg2.url","http://efg.cs.umb.edu");
	String version = properties.getProperty("efg2.version");
	// The application specific copyright Message.
	String COPYRIGHT_MESSAGE = 
		"EFG2 Data Import application Version " +
		version +  properties.getProperty("efg2.copyright",
				" (c) UMASS Boston, 2007");

	// The application specific Message
	String ABOUT_MESSAGE =properties.getProperty("efg2.about.message",
			"About EFG2 Data Import Tool");

	// Java specific information
	String JAVA_MESSAGE = "JVM version " + System.getProperty("java.version")
			+ " by " + System.getProperty("java.vendor");

}

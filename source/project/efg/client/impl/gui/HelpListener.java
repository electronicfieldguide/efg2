package project.efg.client.impl.gui;
/**
 * $Id: HelpListener.java,v 1.1.1.1 2007/08/01 19:11:14 kasiedu Exp $
 * $Name:  $
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
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
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import org.apache.log4j.Logger;

import com.Ostermiller.util.Browser;

/**
 * HelpFileListener.java
 * 
 * 
 * Created: Sat Feb 25 14:12:43 2006
 * 
 * @author <a href="mailto:">Jacob Admin</a>
 * @version 1.0
 */
public class HelpListener implements ActionListener {
	static Logger log = null;
	static {
		try {
			Browser.init();
			log = Logger.getLogger(HelpListener.class);
		} catch (Exception ee) {
		}
	}

	public HelpListener() {

	}

	public void actionPerformed(ActionEvent evt) {

		try {
			URL helpFile = this.getClass().getResource(
					project.efg.util.interfaces.EFGImportConstants.HELP_FILE);
			// Display this page
			Browser.displayURL(helpFile.getFile(), "target");
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
	}
} // HelpListener

package project.efg.client.impl.gui;
/* $Id$
* $Name:  $
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
import java.util.ArrayList;

/**
 * @author kasiedu
 *
 */
public class MyJRadioButtonLists extends ArrayList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2588882141932183041L;

	public boolean add(MyJRadioButton button){
		return this.add(button);
	}
	public boolean add(int i,MyJRadioButton button){
		return this.add(i,button);
	}
}

/* $Id: EFGPageDisplayList.java,v 1.1.1.1 2007/08/01 19:11:25 kasiedu Exp $
* $Name:  $
*
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
* 
 */
package project.efg.server.utils;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;






public class EFGPageDisplayList {

    private List displayList;
    public EFGPageDisplayList(){
    	this.displayList = new ArrayList();
    }
    public void sort(){
		Collections.sort(this.displayList);
	}
    public boolean addDisplay(String ID,String itemName, String imageName, String caption) {
    	if((itemName != null) || (imageName!= null ) || (caption != null)){
    	
    	ImageDisplay display = new ImageDisplay();
    	display.setImageCaption(caption);
    	display.setImageName(imageName);
    	
    	EFGPageDisplay pageDisplay = new EFGPageDisplay();
    	pageDisplay.setDisplay(ID,itemName,display);
    	
    	return this.displayList.add(pageDisplay);
    	}
    	return false;
    }
    public int getSize(){
	    return this.displayList.size();
	}
    public String getItemName(String index) {
    	int position = getPosition(index);
    	if(position < this.displayList.size() && position > -1){
    		EFGPageDisplay display = 
    			(EFGPageDisplay)this.displayList.get(position);
    		return display.getItemName();
    	}
    	return "";
    }
    /**
	 * @param index
	 * @return
	 */
	private int getPosition(String index) {
		try{
		return Integer.parseInt(index);
		}
		catch(Exception ee){
			
		}
		return -1;
	}
	public String getUniqueID(String index) {
		int position = getPosition(index);
    	if(position < this.displayList.size()&& position > -1){
    		EFGPageDisplay display = 
    			(EFGPageDisplay)this.displayList.get(position);
    		return display.getUniqueID();
    	}
    	return "";
    }
	public String getImageName(String index) {
		int position = getPosition(index);
		if(position < this.displayList.size()&& position > -1){
    		EFGPageDisplay display = 
    			(EFGPageDisplay)this.displayList.get(position);
    		return display.getImageName();
    	}
    	return "";
    }
	public String getImageCaption(String index) {
		int position = getPosition(index);
		if(position < this.displayList.size()&& position > -1){
    		EFGPageDisplay display = 
    			(EFGPageDisplay)this.displayList.get(position);
    		return display.getImageCaption();
    		
    	}
    	return "";
    }
}


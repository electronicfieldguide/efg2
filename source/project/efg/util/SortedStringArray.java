/* $Id$
* $Name$
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
package project.efg.util;

import java.util.Arrays;

import org.apache.log4j.Logger;




public class SortedStringArray {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(SortedStringArray.class);
		} catch (Exception ee) {
		}
	}
    private String[] commonNames;
    private int index = 0;

    public SortedStringArray(int size){
      
        commonNames = new String[size];
        this.initArray();
        
    }
    public boolean sort(){
    	try{
    		Arrays.sort(this.commonNames);
    		
    	}
    	catch(Exception ee){
    		log.error(ee.getMessage());
    	}
        return true;
    }
    public boolean addName(String value) {
    		
            commonNames[index]=value;
            ++this.index;
            return true;
    }
  
    public int getArraySize(){
	
	    return commonNames.length;
	}
	public String getName(int position) {
    	String item = null;
    	try{
    		item = commonNames[position]; 
     	}
    	catch(Exception ee){
    		log.error(ee.getMessage());
    		item ="";
    	}
        return item;   
    }
   
    private void initArray(){
		for(int i=0; i < commonNames.length; i++){
			commonNames[i] ="";
		}
	}
	public static void main(String[] args){
        SortedStringArray sortedArray = new SortedStringArray(3);
        sortedArray.addName("cnabc");
        sortedArray.addName ("cnaac");
        sortedArray.addName("cnabb");

        
       
        sortedArray.sort();

        for(int i = 0; i < sortedArray.getArraySize(); ++i){
            log.debug("arr1: " + sortedArray.getName(i));
            
        }
    }
}


/**
 * $Id$
 *
 * Copyright (c) 2005  University of Massachusetts Boston
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

package project.efg.efgInterface;
import project.efg.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.util.*;

/**
 * This class is used to wrap data needed to create a Search page.
 * @see #SearchPage.jsp
 */
public class EFGObject {
    
    private String name;
    private double weight;
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(EFGObject.class); 
	}
	catch(Exception ee){
	}
    }
    public EFGObject(){
	this.name="";
	this.weight=0;
    }
    public double getWeight(){
	return this.weight;
    }
    public String getName(){
	return this.name;
    }
    public void setName(String name){
	this.name = name;
    }
    public void setWeight(String wt){
	try{
	    Double wt1 = new Double(wt);
	    this.weight = wt1.doubleValue();
	}
	catch(Exception ee){
	    LoggerUtilsServlet.logErrors(ee);
	}
    }
    public String toString(){
	return this.name;
    }
    public boolean equals(Object obj1){
	EFGObject efg = (EFGObject)obj1;
	return (this.getName().equals(efg.getName())) && (this.getWeight() == efg.getWeight());
    }
    public int hashCode(){
	if(this.getWeight() <= 0){
	    return this.getName().hashCode();
	}
	return (int)(this.getWeight()*this.getName().hashCode());
    }
}
//$Log$
//Revision 1.1  2006/01/25 21:03:48  kasiedu
//Initial revision
//
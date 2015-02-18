/**
 * $Id$
 *
 * Copyright (c) 2006  University of Massachusetts Boston
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
package project.efg.util.utils;


import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.OperatorInterface;




/**
 * @author kasiedu
 *
 */
public class EFGParseObject implements Comparable  {
	protected String resourceLink;
	protected String state;
	protected  String annotation;
	protected OperatorInterface operator;
	
	public EFGParseObject(String state, 
			String resourceLink,String annotation){
		this(state,resourceLink,annotation,null);
	}
	public EFGParseObject(String state, 
			String resourceLink,
			String annotation,
			OperatorInterface operator){
		this.state=state;
		this.resourceLink=resourceLink;
		this.annotation=annotation;
		if(operator == null){
			this.operator =  SpringFactory.getOperatorInstance("defaultOperator");
		}		
	}
	public EFGParseObject(){

	}
	public void setOperator(OperatorInterface operator){
		this.operator = operator;
	}
	/**
	 * A mathematical operator
	 * one of '+', '-','>','<','>=', '<=', a-b
	 * defaults to the empty string
	 * @return
	 */
	public OperatorInterface getOperator(){
		return this.operator;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.EFGParseObjectInterface#getResourceLink()
	 */
	public String getResourceLink(){
		return this.resourceLink ;
	}
	public void setResourceLink(String resourceLink){
		this.resourceLink = resourceLink;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.EFGParseObjectInterface#getState()
	 */
	public String getState(){
		return this.state;
	}
	public void setState(String state){
		this.state = state;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.EFGParseObjectInterface#getAnnotation()
	 */
	public String getAnnotation(){
		return this.annotation;
	}
	public void setAnnotation(String annotation){
		this.annotation = annotation;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.EFGParseObjectInterface#equals(java.lang.Object)
	 */
	public boolean equals(Object object1){
		EFGParseObject ob1= (EFGParseObject)object1;
		return this.getState().equalsIgnoreCase(ob1.getState());
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.EFGParseObjectInterface#hashCode()
	 */
	public int hashCode(){
		return this.getState().hashCode();
		
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.EFGParseObjectInterface#toString()
	 */
	public String toString(){
		return state;
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgServletsUtil.EFGParseObjectInterface#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		EFGParseObject newState = (EFGParseObject)o;
		return this.getState().compareTo(newState.getState());
	}


}
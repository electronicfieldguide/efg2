package project.efg.Imports.efgImportsUtil;
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
import java.util.ArrayList;

import project.efg.Imports.efgInterface.EFGQueueObjectInterface;

public class EFGQueueObject implements EFGQueueObjectInterface{
	private ArrayList list = new ArrayList();
	
	/* (non-Javadoc)
	 * @see project.efg.util.EFGQueueObjectInterface#add(java.lang.String)
	 */
	public void add(String object){ 
		this.list.add(object);
	}
	/* (non-Javadoc)
	 * @see project.efg.util.EFGQueueObjectInterface#getObject(int)
	 */
	public String getObject(int position){
		try{
			return (String)this.list.get(position);
		}
		catch(Exception ee){
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see project.efg.util.EFGQueueObjectInterface#getSize()
	 */
	public int getSize(){
		return list.size();
	}
	
	/**
	 * Compares its two arguments for order. Returns a negative integer, zero,
	 * or a positive integer as the first argument is less than, equal to, or
	 * greater than the second.
	 * <p>
	 * 
	 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
	 * -sgn(compare(y, x))</tt>
	 * for all <tt>x</tt> and <tt>y</tt>. (This implies that
	 * <tt>compare(x, y)</tt> must throw an exception if and only if
	 * <tt>compare(y, x)</tt> throws an exception.)
	 * <p>
	 * 
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
	 * <tt>compare(x, z)&gt;0</tt>.
	 * <p>
	 * 
	 * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
	 * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
	 * <tt>z</tt>.
	 * <p>
	 * 
	 * It is generally the case, but <i>not</i> strictly required that
	 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>. Generally speaking, any
	 * comparator that violates this condition should clearly indicate this
	 * fact. The recommended language is "Note: this comparator imposes
	 * orderings that are inconsistent with equals."
	 * 
	 * @param o1
	 *            the first object to be compared.
	 * @param o2
	 *            the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 * @throws ClassCastException
	 *             if the arguments' types prevent them from being compared by
	 *             this Comparator.
	 */
	public final int compare(Object object1, Object object2) {
		
		if(object1 == null){
			return 0;
		}
		if(object2 == null){
			return 0;
		}
		
		try {
			EFGQueueObject obj1 = (EFGQueueObject) object1;
			EFGQueueObject obj2 = (EFGQueueObject) object2;
			if(obj1.getSize() == obj2.getSize()){
				
				for(int i=0; i < obj1.getSize(); i++){
					String str1 = obj1.getObject(i);
					String str2 = obj2.getObject(i);
					if(!str2.equalsIgnoreCase(str1)){
						return str1.compareToIgnoreCase(str2);
					}
				}
				return 0;
			}
			else{
				return (obj1.getSize() - obj2.getSize());
			}
		}
		catch(Exception ee){
			
		}
		return 0;
	}
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		int z = this.compare(this,obj);
		if(z == 0){
			return true;
		}
		return false;
	}
	public int hashCode(){
		return this.list.hashCode();
	}
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i < this.list.size(); i++){
			String obj1 = (String)this.getObject(i);
			if((obj1 != null) && (!obj1.trim().equals(""))){
				buffer.append(" " + obj1.toLowerCase());
			}
		}
		return buffer.toString();
	}
}

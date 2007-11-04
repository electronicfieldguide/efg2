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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;





/**
 * @author kasiedu
 *
 */
public class EFGParseObjectList {
	
	
	private List lists;
	private Set stateSet;
	private String name; //the name of the character
	private String databaseName; //the database name of the character
	
	public EFGParseObjectList(){
		this.lists = new ArrayList();
		this.stateSet = new TreeSet();
		
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setDatabaseName(String dbName){
		this.databaseName = dbName;
	}
	public String getDatabaseName(){
		return this.databaseName;
	}
	public int getSize(){
		return this.lists.size();
	}
	public boolean addEFGParseObjectList(EFGParseObjectList list2Add){
		if(list2Add == null){
			return false;
		}
		if(list2Add.getSize() == 0){
			return false;
		}
		Iterator iter = list2Add.iterator();
		while(iter.hasNext()){
			EFGParseObject obj = (EFGParseObject)iter.next();
			this.add(obj);
		}
		return true;
		
	}
	public void sort(Comparator compare){
		Collections.sort(this.lists,compare);
	}
	public void sort(){
		Collections.sort(this.lists);
	}
	/**
	 * 
	 * @param i
	 * @return
	 */
	public EFGParseObject getEFGParseObject(int i){
		return (EFGParseObject)this.lists.get(i);
	}
	
	public boolean add(EFGParseObject obj){
		if(!this.stateSet.contains(obj)){			
			this.stateSet.add(obj);
			return this.lists.add(obj);
		}
		return true;
	}
	public Iterator iterator(){
		return this.lists.iterator();
	}
	public String toString(){
		return lists.toString();
	}
}
package project.efg.server.utils;
/**
 * $Id: CharacterValueTypeSorter.java,v 1.1.1.1 2007/08/01 19:11:24 kasiedu Exp $
 * $Name:  $
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
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import project.efg.templates.taxonPageTemplates.CharacterValue;
import project.efg.templates.taxonPageTemplates.GroupType;
import project.efg.templates.taxonPageTemplates.GroupTypeItem;
import project.efg.templates.taxonPageTemplates.GroupsType;
public class CharacterValueTypeSorter{
	
 private Set sortedSet;

 	private GroupType group;
 	
 	public CharacterValueTypeSorter(){
 		group = new GroupType();
 	}
	/* (non-Javadoc)
	 * @see project.efg.util.EFGDocumentSorter#sort(java.lang.Object)
	 */
	public GroupType sort(CharacterValueComparator compare,GroupType sorter) {
		if(sorter == null){
			//log.debug("GroupType is null");
			return null;
		}
		if(sorter.getContent() != null){
			this.group.setContent(sorter.getContent().trim());
		}
		this.group.setId(sorter.getId());
		if(sorter.getLabel() != null){
			this.group.setLabel(sorter.getLabel().trim());
		}
		this.group.setRank(sorter.getRank());
		if(sorter.getText() != null){
			this.group.setText(sorter.getText().trim());
		}
		this.sortedSet = Collections.synchronizedSet(new TreeSet(compare));
		
	    Enumeration groupTypeItemEnum = sorter.enumerateGroupTypeItem();
		while(groupTypeItemEnum.hasMoreElements()){
			
			GroupTypeItem groupTypeItem = (GroupTypeItem)groupTypeItemEnum.nextElement();
			//if other groups exists
			//get it sort by GroupsType and then sort by the character
			//the groups type should call the charactervalues thing to sort them
			GroupsType groupsType = groupTypeItem.getGroups();
			if(groupsType != null){
				GroupTypeSorter cvt = new GroupTypeSorter();
				GroupsType newGroupsType = cvt.sort(new GroupTypeComparator(), groupsType);
				if(newGroupsType != null){
					GroupTypeItem groupTypeItem1 = new GroupTypeItem();
					groupTypeItem1.setGroups(newGroupsType);
					this.group.addGroupTypeItem(groupTypeItem1);
				}
				
			}
			CharacterValue characterValue = groupTypeItem.getCharacterValue();
			if(characterValue != null){
				this.sortedSet.add(characterValue);
			}
		}
		return sortIt();
	}

	private GroupType sortIt(){
		
		Iterator iter = sortedSet.iterator();
		while(iter.hasNext()){
			CharacterValue characterValue= (CharacterValue)iter.next();
			GroupTypeItem groupTypeItem = new GroupTypeItem();
			groupTypeItem.setCharacterValue(characterValue);
			this.group.addGroupTypeItem(groupTypeItem);
		}
		return this.group;
	}
}

/**
 * $Id$
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
package project.efg.server.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author jacob.asiedu
 *
 */
public class GlossaryMaker {
	private Map table;
	private Set alphabetLists;
	/**
	 * 
	 *
	 */
	public GlossaryMaker(){
		this.table = new TreeMap();
		this.alphabetLists = new TreeSet();
	}
	/**
	 * 
	 * @param glossary
	 */
	public void addGlossaryObject(GlossaryObject glossary){
		String term = glossary.getTerm();
		if(term == null | term.trim().equals("")){
			return;
		}
		this.addToAlphabets(term.trim());
		
		if(this.table.containsKey(term.trim())){
			GlossaryObject glossary1 = 
				(GlossaryObject)this.table.get(term.trim());
			glossary1.addDefinitions(glossary.getDefinitions());
			this.table.put(glossary1.getTerm(),glossary1);
		}
		else{
			this.table.put(term.trim(),glossary);
		}
	}
	/*
	 * Handle Glossary
	 */
	public int getNumberOfTerms(){
		return this.getGlossaryTerms().size();
	}
	public String getTerm(int index){
		List list = this.getGlossaryTerms();
		return (String)list.get(index);
	}
	/*
	 * Handle Definitions
	 */
	public int getNumberOfDefinitions(String term){
		return this.getDefinitions(term).size();
	}
	public String getDefinition(String term,int index){
		List list = this.getDefinitions(term);
		return (String)list.get(index);
	}
/**
	 * Get an alphabet given an index
	 * @param index
	 * @return
	 */
	public String getAlphabet(int index){
		return (String)this.getAlphabetList().get(index);
	}
	public int getNumberOfAlphabets(){
		return this.getAlphabetList().size();
	}
	/**
	 * 
	 * @param term
	 * @return
	 */
	public int getNumberOfMediaResources(String term){
		return this.getMediaResouces(term).size();
	}
	/**
	 * 
	 * @param term
	 * @param index
	 * @return
	 */
	public String getMediaResource(String term,int index){
		List list = this.getMediaResouces(term);
		return (String)list.get(index);
	}
	/**
	 * 
	 * @param term
	 * @return
	 */
	public int getNumberOfAlsoSees(String term){
		return this.getAlsoSee(term).size();
	}
	/**
	 * 
	 * @param term
	 * @param index
	 * @return
	 */
	public String getAlsoSee(String term,int index){
		List list = this.getAlsoSee(term);
		return (String)list.get(index);
	}
	/**
	 * A list of definitions associated with a given term.
	 * @param term
	 * @return
	 */
	private List getDefinitions(String term){
		return ((GlossaryObject)this.table.get(term)).getDefinitions();
	}
	/**
	 * Get the AlsoSee Terms associated with a given Term 
	 * @param term
	 * @return a list of Terms associated with the current Term.
	 */
	private List getAlsoSee(String term){
		return ((GlossaryObject)this.table.get(term)).getAlsoSee();
	}
	/**
	 * A list of terms associated with a data source
	 * @return
	 */
	private List getGlossaryTerms(){
		return new ArrayList(this.table.keySet());
	}
	/**
	 * Get the List of Media resources associated with the given Term
	 * @param term
	 * @return a list of media resources associated with a given term
	 */
	private List getMediaResouces(String term){
		return ((GlossaryObject)this.table.get(term)).getMediaResouces();
	}
	/**
	 * Return a List of alphabets that starts each term in the list of Terms
	 * @return
	 */
	private List getAlphabetList(){
		return new ArrayList(this.alphabetLists);
	}
	/*
	 * Handle Alphabets 
	 */
	/**
	 * 
	 * @param term
	 */
	private void addToAlphabets(String term){
		if(term == null || term.trim().equals("")){
			return;
		}
		String alpha = term.substring(0,1).toUpperCase();
		this.alphabetLists.add(alpha);
	}
}

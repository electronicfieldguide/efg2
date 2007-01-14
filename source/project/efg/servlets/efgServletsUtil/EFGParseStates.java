/**
 * $Id$
 * $Name$
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
package project.efg.servlets.efgServletsUtil;



import java.util.regex.Pattern;

import project.efg.servlets.efgInterface.OperatorInterface;
import project.efg.servlets.factory.OperatorFactory;
import project.efg.util.RegularExpresionConstants;
import project.efg.util.UnicodeToASCIIFilter;

/**
 * @author kasiedu
 *
 */
public class EFGParseStates {

	protected UnicodeToASCIIFilter filter = new UnicodeToASCIIFilter();
	
	private String[] parse(String patternStr,String states) {
		Pattern p = null;
		String[] arr = null;
		boolean isDone = false;
	
		if(patternStr.equalsIgnoreCase(RegularExpresionConstants.NOPATTERN)){
		
			p = RegularExpresionConstants.noPattern;
		}
		else if(patternStr.equalsIgnoreCase(RegularExpresionConstants.LISTSEP)){
			
			p = RegularExpresionConstants.listSepPattern;
		}
		else if(patternStr.equalsIgnoreCase(RegularExpresionConstants.ORCOMMAPATTERN)){
			
			p = RegularExpresionConstants.catPattern;
		}
		else{
			
			arr = states.split(patternStr, -1);
			isDone = true;
		}
		if(!isDone){
			arr =  p.split(states,-1);
		}
		
	
		return arr;
	}

	private  String removeString(String state, String[] str2Remove) {
		for (int i = 0; i < str2Remove.length; i++) {
			String str = str2Remove[i];

			if ((state.indexOf(RegularExpresionConstants.LEFTPAREN) > -1)
					|| (state.indexOf(RegularExpresionConstants.RIGHTPAREN) > -1)) {
				state = state.replaceAll(str, "");
			}
		}

		return state;
	}

	public  EFGParseObject parseUserStats(
			String separator, String states) {
		
		String[] fields = parse(separator,states);
		
		return checkForOperator(createEFGParseObject(fields[0],false));
	}

	private  EFGParseObject checkForOperator(EFGParseObject parseObject){
		if(parseObject == null){
			return null;
		}
			//do ranges
			OperatorInterface operator = OperatorFactory.getInstance(parseObject.getState());
			parseObject.setOperator(operator);
			if(parseObject.getState() != null){
			
				//remove the operator from the string if any
				String[] fields = parseObject.getState().split(operator.toString());
				if(fields.length> 1){
					parseObject.setState(fields[1].trim());
				}
			
			}
			return parseObject;
		
	}
	/**
	 * 
	 * @param fields
	 * @param notRemoveParen - true means do not remove parenthesis
	 * @return
	 */
	private   EFGParseObject createEFGParseObject(String fields, boolean notRemoveParen){
		//log.debug("Fields: " + fields);
		String state = fields.trim();
		
		
		String[] curStatePipe =RegularExpresionConstants.pipePattern.split(state);
		String resourceLink = "";
		String annotation = "";

		if (curStatePipe.length > 1) {
			resourceLink = curStatePipe[0];
			state = curStatePipe[1];
		}
		
		String[] annotations = RegularExpresionConstants.caratPattern.split(state);
		if (annotations.length > 1) {
			annotation = annotations[0];
			state = annotations[1];
		}
	
		if ((state != null) && (!state.trim().equals(""))) {
			if(!notRemoveParen){
				state = removeString(state, RegularExpresionConstants.STR_2_REMOVE);
			}
		
			return new EFGParseObject(state,
					resourceLink, annotation);
		}
	
		return null;
	}

	private String unicode2Ascii(String states){
		/*try{
		
		StringWriter writer = new StringWriter();
		this.filter.filter(new StringReader(states),
				writer);
			states = writer.getBuffer().toString();
		}
		catch(Exception ee){
			
		}*/
			return states;
	}
	public  EFGParseObjectList parseStates(String separator,
			String states,boolean isLists) {
	
	
		states =this.unicode2Ascii(states);
		
		EFGParseObjectList lists = new EFGParseObjectList();
		String[] fields = parse(separator,states);
	

		for (int i = 0; i < fields.length; i++) {
		
			EFGParseObject parseObject = createEFGParseObject(fields[i],isLists);
			if(parseObject != null){
				lists.add(parseObject);
			}
		}
	
		return lists;

	}

}

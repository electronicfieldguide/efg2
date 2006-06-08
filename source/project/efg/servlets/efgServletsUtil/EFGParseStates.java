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

import org.apache.log4j.Logger;

import project.efg.servlets.efgInterface.OperatorInterface;
import project.efg.servlets.factory.OperatorFactory;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class EFGParseStates {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(EFGParseStates.class);
		} catch (Exception ee) {
		}
	}

	public String[] parse(String states, String patternStr) {
		return states.split(patternStr, -1);
	}

	private  String removeString(String state, String[] str2Remove) {
		for (int i = 0; i < str2Remove.length; i++) {
			String str = str2Remove[i];

			if ((state.indexOf(EFGImportConstants.LEFTPAREN) > -1)
					|| (state.indexOf(EFGImportConstants.RIGHTPAREN) > -1)) {
				state = state.replaceAll(str, "");
			}
		}

		return state;
	}

	public  EFGParseObject parseUserStats(
			String separator, String states) {
		log.debug("States: " + states);
		log.debug("Separator: " + separator);
		String[] fields = getFields( separator,states);
		log.debug("Fields length: " + fields.length);
		log.debug("field zero: " + fields[0]);
		return checkForOperator(createEFGParseObject(fields[0]));
	}

	private  EFGParseObject checkForOperator(EFGParseObject parseObject){
		if(parseObject == null){
			return null;
		}
		
			OperatorInterface operator = OperatorFactory.getInstance(parseObject.getState());
			parseObject.setOperator(operator);
			if(parseObject.getState() != null){
				log.debug("State before: " + parseObject.getState());
				//remove the operator from the string if any
				String[] fields = parseObject.getState().split(operator.toString());
				if(fields.length> 1){
					parseObject.setState(fields[1].trim());
				}
				log.debug("State after: " + parseObject.getState());
			}
			return parseObject;
		
	}
	private   EFGParseObject createEFGParseObject(String fields){
		log.debug("Fields: " + fields);
		String state = fields.trim();
		String[] curStatePipe = parse(state, EFGImportConstants.PIPESEP);
		String resourceLink = "";
		String annotation = "";

		if (curStatePipe.length > 1) {
			resourceLink = curStatePipe[0];
			state = curStatePipe[1];
		}
		String[] annotations = parse(state,EFGImportConstants.COLONSEP);
		if (annotations.length > 1) {
			annotation = annotations[0];
			state = annotations[1];
		}
		log.debug("State: " + state);
		if ((state != null) && (!state.trim().equals(""))) {
			state = removeString(state, EFGImportConstants.STR_2_REMOVE);
			log.debug("State2: " + state);
			return new EFGParseObject(state,
					resourceLink, annotation);
		}
		log.debug("returning null");
		return null;
	}
	private  String[] getFields(String separator,
			String states){
		return parse(states, separator);
		
	}
	public  EFGParseObjectList parseStates(String separator,
			String states) {
		log.debug("State to parse: " + states);
		log.debug("Separator: " + separator);
		EFGParseObjectList lists = new EFGParseObjectList();
		String[] fields = getFields(separator,states);
		log.debug("Number of fields: " + fields.length);

		for (int i = 0; i < fields.length; i++) {
			log.debug("About to add Fields[" + i + "]=" + fields[i]);
			EFGParseObject parseObject = createEFGParseObject(fields[i]);
			if(parseObject != null){
				lists.add(parseObject);
			}
		}
		log.debug("Lists: " + lists.toString());
		return lists;

	}

}

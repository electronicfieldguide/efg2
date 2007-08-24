/**
 * $Id: CharacterValueComparator.java,v 1.1.1.1 2007/08/01 19:11:24 kasiedu Exp $
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

package project.efg.server.utils;

import project.efg.templates.taxonPageTemplates.CharacterValue;

/**
 * This class is used as a comparator for objects to be used on serachable
 * pages. Objects on Serachable pages are sorted by weight.
 * 
 * 
 */
public class CharacterValueComparator implements java.util.Comparator {
	

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
		int returnValue = 0;
		try {
			CharacterValue char1 = (CharacterValue) object1;
			CharacterValue char2 = (CharacterValue) object2;
			
			int charRank1 = char1.getRank();
			int charRank2 = char2.getRank();
			
			//log.debug("rank1: " + charRank1);
			//log.debug("rank2: " + charRank2);
			if (charRank1 != charRank2) {//different character ranks
				returnValue =  (charRank1 - charRank2);
			}
			else{//same character ranks
				String characterVal1 = char1.getValue();
				String characterVal2 = char2.getValue();
				if((characterVal1 != null) && 
						(characterVal2 != null) && 
						(!characterVal1.trim().equals(""))&& 
						(!characterVal2.trim().equals(""))
						){//compare values
					returnValue=  characterVal1.compareTo(characterVal2);
				}
				else{
					if((characterVal1 != null) &&
					(!characterVal1.trim().equals(""))){
						returnValue =  1;
					}
					else{
						if((characterVal2 != null) &&
								(!characterVal2.trim().equals(""))){
							returnValue =  -1;
						}
						else{
							characterVal1 = char1.getText();
							characterVal2 = char2.getText();
							if((characterVal1 != null) && 
									(characterVal2 != null) && 
									(!characterVal1.trim().equals(""))&& 
									(!characterVal2.trim().equals(""))
							){//compare text
				
								returnValue= characterVal1.compareTo(characterVal2);
							}
							else{
								if((characterVal1 != null) &&
										(!characterVal1.trim().equals(""))){
									returnValue= 1;
								}
								else{
									if((characterVal2 != null) &&
										(!characterVal2.trim().equals(""))){
										returnValue =  -1;
									}
									else{
										characterVal1 = char1.getLabel();
										characterVal2 = char2.getLabel();
										if((characterVal1 != null) && 
												(characterVal2 != null) && 
												(!characterVal1.trim().equals(""))&& 
												(!characterVal2.trim().equals(""))
											){//compare labels
									
											returnValue = characterVal1.compareTo(characterVal2);
										}
										else{
											if((characterVal1 != null) &&
													(!characterVal1.trim().equals(""))){
												returnValue = 1;
											}
											else{
												if((characterVal2 != null) &&
														(!characterVal2.trim().equals(""))){
													returnValue = -1;
												}	
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ee) {
			//log.error(ee.getMessage());
		}
		//log.debug("return value is from CharacterValueComaparator: " + returnValue);
		return returnValue; //same rank, same id
	}
	
} 
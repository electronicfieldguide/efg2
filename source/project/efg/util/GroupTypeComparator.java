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

package project.efg.util;



import project.efg.templates.taxonPageTemplates.GroupType;

/**
 * This class is used as a comparator for objects to be used on serachable
 * pages. Objects on Serachable pages are sorted by weight.
 * 
 * 
 */
public class GroupTypeComparator implements java.util.Comparator {
	

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

			if ((object1 == null) && (object2 == null)) {
				//log.debug("object1 and object2 are null");
			} else {
				if (object2 == null) {
					//log.debug("object2 is null");
					returnValue = 1;
				} else {

					if (object1 == null) {
						//log.debug("object1 is null");
						returnValue = -1;
					} else {
						GroupType group1 = (GroupType) object1;
						GroupType group2 = (GroupType) object2;
						int groupID1 = Integer.parseInt(group1.getId());
						int groupID2 = Integer.parseInt(group2.getId());

						if (groupID1 != groupID2) {// id's not same
							//log.debug("ID1: " + id1);
							//log.debug("ID2: " + id2);
							returnValue = (groupID1 - groupID2);

						} else {
							int groupRank1 = group1.getRank();
							int groupRank2 = group2.getRank();
							//log.debug("rank1: " + groupRank1);
							//log.debug("rank2: " + groupRank2);

							returnValue = (groupRank1 - groupRank2);

						}
					}
				}
			}

		} catch (Exception ee) {
			ee.printStackTrace();
			//log.error(ee.getMessage());
		}
		//log.debug("Return Value from GroupTypeComparator : " + returnValue);
		return returnValue; // same rank, same id
	}

}
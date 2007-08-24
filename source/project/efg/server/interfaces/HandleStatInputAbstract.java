/*
 * $Id$
 *
 * Copyright (c) 2007  University of Massachusetts Boston
 *
 * Authors: Jacob K. Asiedu
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
package project.efg.server.interfaces;

import project.efg.efgDocument.StatisticalMeasuresType;


/**
 * @author kasiedu
 *
 */
public abstract class HandleStatInputAbstract {

	protected StatisticalMesureComparatorInterface measureComparator;

	/**
	 * 
	 */
	public HandleStatInputAbstract(StatisticalMesureComparatorInterface measureComparator) {
		this.measureComparator = measureComparator;
	}

	public abstract boolean isInRange(StatisticalMeasuresType dbStats, String userValues);
}

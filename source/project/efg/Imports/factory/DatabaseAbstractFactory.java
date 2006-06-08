package project.efg.Imports.factory;
/**
 * $Id$
 * $Name$
 * 
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
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
 * 
 */
import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgInterface.CSV2DatabaseAbstract;
import project.efg.Imports.efgInterface.EFGDataExtractorInterface;
import project.efg.Imports.efgInterface.EFGDatasourceObjectInterface;
import project.efg.Imports.rdb.CSV2Database;

public class DatabaseAbstractFactory{
/**
 * Factory method for making 
 * @param datasource - The datasource
 * @param dataExtractor - The extractor
 * @param dbObject - The dbObject
 * @return CSV2DatabaseAbstract 
 */
	public synchronized static CSV2DatabaseAbstract 
	getDatabaseObject(EFGDatasourceObjectInterface datasource, 
			EFGDataExtractorInterface dataExtractor,
			DBObject dbObject) {
		return getDatabaseObject(datasource,dataExtractor,dbObject,false);
	}
/**
 * 
 * @param datasource
 * @param dataExtractor
 * @param dbObject
 * @param isUpdate
 * @return CSV2DatabaseAbstract 
 */
	public synchronized static CSV2DatabaseAbstract 
	getDatabaseObject(EFGDatasourceObjectInterface datasource,
			EFGDataExtractorInterface dataExtractor,
			DBObject dbObject,
			boolean isUpdate) {
		return new CSV2Database(datasource,dataExtractor,dbObject,isUpdate);
	}

}

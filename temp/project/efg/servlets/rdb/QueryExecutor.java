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
package project.efg.servlets.rdb;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import project.efg.Imports.factory.EFGRowMapperFactory;
import project.efg.Imports.rdb.EFGRowMapperInterface;

/**
 * @author kasiedu
 *
 */
public class QueryExecutor {
	
	private DataSource ds;
	private JdbcTemplate jdbcTemplate;
	/**
	 * 
	 */
	
	public QueryExecutor() {
		this.setDatasource();
	}
	private void setDatasource(){
		this.ds = EFGRDBUtils.getDatasource();
		this.jdbcTemplate = new JdbcTemplate(this.ds);
	}
	public int executeUpdate(String query){
		return this.jdbcTemplate.update(query);
		
	}
	public List executeQueryForList(String query,
			int numberOfColumns) throws Exception {
		if (this.ds == null) {
			this.setDatasource();
		}
		//log.debug("query: " + query);
		EFGRowMapperInterface rowMapper = EFGRowMapperFactory.getRowMapper();
		return rowMapper.mapRows(this.jdbcTemplate, query, numberOfColumns);
	}
	
	public  SqlRowSet executeQueryForRowSet(String query)
	throws Exception {
		if (ds == null) {
			this.setDatasource();
		}
		//log.debug("query: " + query);
		EFGRowMapperInterface rowMapper = EFGRowMapperFactory.getRowMapper();
		return rowMapper.mapRows(this.jdbcTemplate, query);
	}
}

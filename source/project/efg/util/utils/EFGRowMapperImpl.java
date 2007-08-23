package project.efg.util.utils;
/* $Id: EFGRowMapperImpl.java,v 1.1.1.1 2007/08/01 19:11:27 kasiedu Exp $
* $Name:  $
* Created: Tue Feb 28 13:14:19 2006
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
* Imports a csv file into a relational database
* 
*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import project.efg.util.factory.SpringFactory;
import project.efg.util.interfaces.EFGQueueObjectInterface;
import project.efg.util.interfaces.EFGRowMapperInterface;



/**
 * Implements the Spring RowMapper interface
 * @author kasiedu
 *
 */
public class EFGRowMapperImpl implements EFGRowMapperInterface {
	
	private List list; 
	/**
	 * Map the current row to a List
	 */
	public List mapRows(final JdbcTemplate jdbcTemplate,final String query, 
			final int numberOfColumns){
	
		this.list = jdbcTemplate.query(query, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				EFGQueueObjectInterface queue = 
					SpringFactory.getEFGQueueObject();

				try{
					for(int i=0; i < numberOfColumns; i++){					
						String str = rs.getString((i+1));
						queue.add(str);
					}
				}
				catch(Exception ee){
					System.err.println("Message: " + ee.getMessage());
					ee.printStackTrace();
				}
				return queue;
			}
		});
		return this.list;
	}
	public SqlRowSet mapRows(final JdbcTemplate jdbcTemplate, final String query){
	
		SqlRowSet rowset = 
			jdbcTemplate
				.queryForRowSet(query);
		return rowset;
	}
}
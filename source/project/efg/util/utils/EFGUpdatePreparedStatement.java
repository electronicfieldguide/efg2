/**
 * 
 */
package project.efg.util.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementSetter;

import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;

/**
 * @author jacob.asiedu
 *
 */
public class EFGUpdatePreparedStatement implements PreparedStatementSetter {
	
	private String datasourceName;
	
	private int byteLength=-1;
	private boolean isByte = false;
	private ByteArrayInputStream stream;
	/**
	 * @param tableName
	 * @param datasourceName
	 * @param byteArray
	 */
	public EFGUpdatePreparedStatement(
			String datasourceName,
			TaxonPageTemplates tps) {
		
		this.datasourceName = datasourceName;
		this.tps2bytes(tps);
	}
	private void tps2bytes(TaxonPageTemplates tps){
		
		try {
			  ByteArrayOutputStream   byteStream = new ByteArrayOutputStream();
			  ObjectOutputStream objStream;
			objStream = new ObjectOutputStream(byteStream);
			  objStream.writeObject(tps);
			  objStream.flush();
			  byte[] byteArray = (byte[])(byteStream.toByteArray());
			  this.stream = new ByteArrayInputStream(byteArray);
			 isByte = true;
		} catch (IOException e) {
		
			e.printStackTrace();
			isByte = false;
		}
	}
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.PreparedStatementSetter#setValues(java.sql.PreparedStatement)
	 */
	public void setValues(PreparedStatement ps) throws SQLException { 
		if(this.isByte){
			ps.setBinaryStream(1, this.stream, this.byteLength);
		    ps.setString(2, this.datasourceName);
		}
	}
}

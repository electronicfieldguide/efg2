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
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;

/**
 * @author jacob.asiedu
 *
 */
public class EFGBatchUpdatePreparedStatement implements BatchPreparedStatementSetter {
	
	private String[] datasourceName;
	private List byteStreamList;
	private List byteStreamLength;
	private boolean isByte=true;

	/**
	 * @param tableName
	 * @param datasourceName
	 * @param byteArray
	 */
	public EFGBatchUpdatePreparedStatement(
			String[] datasourceName,
			TaxonPageTemplates[] tpsArr) {
		this.byteStreamList = new ArrayList();
		this.byteStreamLength = new ArrayList();
		this.datasourceName = datasourceName;
		
		for (int i = 0; i < tpsArr.length; i++) {
			this.tps2bytes(tpsArr[i]);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.BatchPreparedStatementSetter#getBatchSize()
	 */
	public int getBatchSize() {
		return this.datasourceName.length;
	}
	private void tps2bytes(TaxonPageTemplates tps){
		
		try {
			  ByteArrayOutputStream   byteStream = new ByteArrayOutputStream();
			  ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			  objStream.writeObject(tps);
			  objStream.flush();
			  byte[] byteArray = (byte[])(byteStream.toByteArray());
			  ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
			  this.byteStreamList.add(stream);
			  this.byteStreamLength.add(new Integer(byteArray.length));
			
		} catch (IOException e) {
		
			e.printStackTrace();
			isByte = false;
		}
	}
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.BatchPreparedStatementSetter#setValues(java.sql.PreparedStatement, int)
	 */
	public void setValues(PreparedStatement ps, int index) throws SQLException {
		if(this.isByte){
			ByteArrayInputStream stream = (ByteArrayInputStream)this.byteStreamList.get(index);
			int length = ((Integer)this.byteStreamLength.get(index)).intValue();
			ps.setBinaryStream(1, stream,length);
		    ps.setString(2, datasourceName[index]);
	    
		}
		
	}
}

/**
 * 
 */
package project.efg.tests;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.efg.Imports.efgImpl.DBObject;
import project.efg.Imports.efgInterface.EFGQueueObjectInterface;
import project.efg.Imports.rdb.EFGRDBImportUtils;
import project.efg.efgDocument.EFGDocument;
import project.efg.efgDocument.EFGType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.TaxonEntries;
import project.efg.efgDocument.TaxonEntryType;
import project.efg.efgDocument.TaxonEntryTypeItem;
import project.efg.servlets.rdb.EFGRDBUtils;

/**
 * @author kasiedu
 *
 */
public class DBTests {
	
	
	String query= "Select similar_species from ipotest_1155835915226 where scientific_name='Ipomoea nil'" ;
	public DBTests(){
		EFGRDBImportUtils.init();
	
	}
	
	public  boolean doTests() {
		try{
			DriverManagerDataSource dataSource;
			 String url="jdbc:mysql://localhost:3306/efg";
			 String driver="com.mysql.jdbc.Driver";
			 String user="efg";
			 String pwd="efg";
			 
			dataSource =
				new DriverManagerDataSource();
			
			dataSource.setDriverClassName(driver);
		
			dataSource.setUsername(user);
			
			dataSource.setPassword(pwd);
			
			dataSource.setUrl(url);
			EFGRDBUtils.setDatasource( dataSource);
			DBObject dbObject=new DBObject(url,user,pwd);
			JdbcTemplate jdbcTemplate = EFGRDBImportUtils.getJDBCTemplate(dbObject);
			List list = EFGRDBImportUtils.executeQueryForList(jdbcTemplate,
					query,
					1);
			String state = null;
			for (java.util.Iterator iter = list.iterator(); iter
			.hasNext();) {
				EFGQueueObjectInterface	queue = (EFGQueueObjectInterface) iter.next();
				state = queue.getObject(0);
		
				}
				buildDocument(state);
				return true;
			} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	private void buildDocument(String state)  {
		try{
		System.out.println("State: " + state);
		EFGDocument srdoc = new EFGDocument();
		TaxonEntries taxonEntries = new TaxonEntries();// on
		TaxonEntryType taxonEntry = new TaxonEntryType();
		TaxonEntryTypeItem taxonEntryItem = new TaxonEntryTypeItem();
		ItemsType items = new ItemsType();
		EFGType item = new EFGType();
		item.setContent(state);
		items.addItem(item);
		items.setName("xxx");
		taxonEntryItem.setItems(items);
		taxonEntry.addTaxonEntryTypeItem(taxonEntryItem);
		taxonEntries.addTaxonEntry(taxonEntry);
		srdoc.setTaxonEntries(taxonEntries);
		 
		 Writer out = new BufferedWriter(new OutputStreamWriter(
		            new FileOutputStream("testDB.xml"), "UTF8"));
		       //out.write(aString);
		        //out.close();
		StringWriter swriter = new StringWriter();
		
		//FileWriter writer = new FileWriter("testDB.xml");
		
		srdoc.marshal(out);
		srdoc.marshal(swriter);
		System.out.println(swriter.getBuffer().toString());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		//String query= "Select similar_species from ipotest_1155835915226 where scientific_name='Ipomoea nil'" ;

		DBTests dbTest = new DBTests();
		dbTest.doTests();
	}
}

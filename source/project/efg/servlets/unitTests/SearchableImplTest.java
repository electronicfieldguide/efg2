package project.efg.servlets.unitTests;

/**
 * 
 */

import junit.framework.TestCase;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.efg.servlets.efgInterface.SearchableInterface;
import project.efg.servlets.efgInterface.EFGDataObjectListInterface;
import project.efg.servlets.efgInterface.EFGDataObject;
import project.efg.servlets.rdb.EFGRDBUtils;
import project.efg.servlets.rdb.SearchableImpl;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class SearchableImplTest extends TestCase {
	private SearchableInterface search;
	private DriverManagerDataSource dataSource;
	private String dburl="jdbc:mysql://localhost:3306/efg";
	private String dbDriverName="com.mysql.jdbc.Driver";
	private String dbusername="efg";
	private String dbpassword="efg";
	private String displayName = "IpoTest";
	private String datasourceName="IpoTest_1146671139964";
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SearchableImplTest.class);
	}

	/**
	 * Constructor for SearchableImplTest.
	 * @param name
	 */
	public SearchableImplTest(String name) {
		super(name);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		try{
		
		this.search = new SearchableImpl();
		this.dataSource =
			new DriverManagerDataSource();
		String driver = EFGImportConstants.EFGProperties.getProperty("dbDriverName");
		assertEquals(driver,this.dbDriverName);
		this.dataSource.setDriverClassName(driver);
		String user = EFGImportConstants.EFGProperties.getProperty("dbusername");
		assertEquals("Expected user: " + user + " current user " +
				this.dbusername,user,this.dbusername);
		this.dataSource.setUsername(user);
		String pwd = EFGImportConstants.EFGProperties.getProperty("dbpassword");
		assertEquals(pwd,this.dbpassword);
		this.dataSource.setPassword(pwd);
		String url =EFGImportConstants.EFGProperties.getProperty("dburl");
		assertEquals("Expected url: " + url + " current url " + dburl,
				url,this.dburl);
		this.dataSource.setUrl(url);
		assertNotNull("Datasource must not be null",this.dataSource);
		assertNotNull("Time out must not be null ",new Integer(this.dataSource.getLoginTimeout()));
		EFGRDBUtils.setDatasource( this.dataSource);
		//search.setDatasource(this.dataSource);
		assertNotNull("DisplayName must not be null",this.displayName);
	} catch (Exception e) {
		// 
		e.printStackTrace();
	}
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	

	
	/*
	 * Test method for 'project.efg.servlets.rdb.SearchableImpl.getSearchables(String)'
	 */
	public final void testGetSearchables() {
	
		try {
			EFGDataObjectListInterface list=  
				search.getSearchables(this.displayName,this.datasourceName);
			assertNotNull("List must not be null",list);
			assertEquals("Expected number 3. Number from DB=",3,
					list.getEFGDataObjectCount());
			
			int counter = list.getEFGDataObjectCount();
			for(int i = 0; i < counter; i++){
				//EFGDataObject obj= list.getEFGDataObject(i);
				EFGDataObject obj= null;//list.getEFGDataObject(i);
				if(obj.getStates() != null){
					System.out.println("Name: " + obj.getName() + ". State object position: " + 
							obj.getOrder() + " get postion: " + i);
				}
				if(obj.getStatisticalMeasures() != null){
					System.out.println("Name: " + obj.getName() + ".Stats object position: " + 
							obj.getOrder() + " get postion: " + i);
				}
				if(obj.getMediaResources() != null){
					System.out.println("Name: " + obj.getName() + ".Media object position: " + 
							obj.getOrder() + " get postion: " + i);
				}
				if(obj.getEFGLists() != null){
					System.out.println("Name: " + obj.getName() + ".Lists object position: " + 
							obj.getOrder() + " get postion: " + i);
				}
				//else{
					//System.out.println("Name: " + obj.getName() + ".Unknown object: " + 
					//		obj.getOrder() + " get postion: " + i);
				//}
			
			}
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}

	}

	

	/*
	 * Test method for 'project.efg.servlets.rdb.SearchableImpl.createEFGDataObject()'
	 */
	public final void testCreateEFGDataObject() {
		assertNotNull("Searchable object must not be null",
			this.search.createEFGDataObject());

	}

	/*
	 * Test method for 'project.efg.servlets.rdb.SearchableImpl.createSearchableList()'
	 */
	public final void testCreateSearchableList() {
		
		assertNotNull("SearchableList must not be null",
					this.search.createSearchableList());
			
	}
}


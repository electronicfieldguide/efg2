/**
 * 
 */
package unitTests;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.servlets.efgInterface.HandleStatInputAbstract;

/**
 * @author kasiedu
 *
 */
public class TestHandleStatInput extends TestCase {
	private HandleStatInputAbstract hd;
	/**
	 * @param arg0
	 */
	public TestHandleStatInput(String arg0) {
		super(arg0);
	}
	private void doSpring() {
		
		
		try {
			ApplicationContext    context = 
				new ClassPathXmlApplicationContext("springconfigTestCase.xml");
			this.hd =  (HandleStatInputAbstract)context.getBean("handleStatInput");
			}
			catch(Exception ee) {
				ee.printStackTrace();
			}
			return;
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.doSpring();
		
		
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.HandleStatInput#isInRange(project.efg.efgDocument.StatisticalMeasuresType, java.lang.String)}.
	 */
	public final void testIsInRange() {
		if(this.hd == null) {
			fail("Error in intializing Spring Context");
		}
		StatisticalMeasuresType dbStats = new StatisticalMeasuresType();
		dbStats.setDatabaseName("databaseName");
		dbStats.setMax(16);
		dbStats.setMin(4);
		StatisticalMeasureType dbStat = new StatisticalMeasureType();
		dbStat.setMax(16);
		dbStat.setMin(4);
		dbStats.addStatisticalMeasure(dbStat);
		
		
		String userValues = "GT 5";
		assertTrue(this.hd.isInRange(dbStats, userValues));
		System.out.println("");
		userValues = "LT 3";
		assertFalse(this.hd.isInRange(dbStats, userValues));
		System.out.println("");
		userValues = "LTEQ 10";
		assertTrue(this.hd.isInRange(dbStats, userValues));
		System.out.println("");
		userValues = "GTEQ 20";
		assertFalse(this.hd.isInRange(dbStats, userValues));
		userValues = "5";
		assertTrue(this.hd.isInRange(dbStats, userValues));
		userValues = "4-16";
		assertTrue(this.hd.isInRange(dbStats, userValues));
	}

}

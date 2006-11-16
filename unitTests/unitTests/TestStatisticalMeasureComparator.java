/**
 * 
 */
package unitTests;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.servlets.efgInterface.OperatorInterface;
import project.efg.servlets.efgInterface.StatisticalMesureComparatorInterface;
import project.efg.servlets.factory.OperatorFactory;

/**
 * @author kasiedu
 *
 */
public class TestStatisticalMeasureComparator extends TestCase {
	private StatisticalMesureComparatorInterface stats;
	/**
	 * @param arg0
	 */
	public TestStatisticalMeasureComparator(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.doSpring();
	}
private void doSpring() {
		
		
		try {
			ApplicationContext    context = 
				new ClassPathXmlApplicationContext("springconfigTestCase.xml");
			this.stats =  (StatisticalMesureComparatorInterface)context.getBean("statsComparator");
			}
			catch(Exception ee) {
				ee.printStackTrace();
			}
			return;
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator#StatisticalMeasureComparator()}.
	 */
	public final void testStatisticalMeasureComparator() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator#isInRange(project.efg.servlets.efgInterface.OperatorInterface, project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)}.
	 */
	public final void testIsInRange() {
		StatisticalMeasureType databaseValue = new StatisticalMeasureType();
		databaseValue.setMax(6);
		databaseValue.setMin(4);
		
		StatisticalMeasureType userValue = new StatisticalMeasureType();
		userValue.setMin(3);
		userValue.setMax(3);
		

		try {
			OperatorInterface operator = OperatorFactory.getInstance("GT");
			assertTrue(this.stats.isInRange(operator, userValue, databaseValue));
			operator = OperatorFactory.getInstance("LT");
			assertFalse(this.stats.isInRange(operator, userValue, databaseValue));
			operator = OperatorFactory.getInstance("LTEQ");
			assertFalse(this.stats.isInRange(operator, userValue, databaseValue));
			operator = OperatorFactory.getInstance("GTEQ");
			assertTrue(this.stats.isInRange(operator, userValue, databaseValue));
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator#checkGreaterThanOrEquals(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)}.
	 */
	public final void testCheckGreaterThanOrEquals() {
		StatisticalMeasureType databaseValue = new StatisticalMeasureType();
		databaseValue.setMax(6);
		databaseValue.setMin(4);
		
		StatisticalMeasureType userValue = new StatisticalMeasureType();
		userValue.setMin(7);
		userValue.setMax(7);
	
		try {
			assertFalse(this.stats.checkGreaterThanOrEquals(databaseValue, userValue));
			
			userValue.setMin(4);
			userValue.setMax(4);
			assertTrue(this.stats.checkGreaterThanOrEquals(databaseValue, userValue));
			userValue.setMin(6);
			userValue.setMax(6);
			assertTrue(this.stats.checkGreaterThanOrEquals(databaseValue, userValue));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator#checkGreaterThan(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)}.
	 */
	public final void testCheckGreaterThan() {
		StatisticalMeasureType databaseValue = new StatisticalMeasureType();
		databaseValue.setMax(10);
		databaseValue.setMin(4);
		
		StatisticalMeasureType userValue = new StatisticalMeasureType();
		userValue.setMin(14);
		userValue.setMax(14);
	
		try {
			assertFalse(this.stats.checkGreaterThan(databaseValue, userValue));
			userValue.setMin(5);
			userValue.setMax(5);
			assertTrue(this.stats.checkGreaterThan(databaseValue, userValue));
			userValue.setMin(3);
			userValue.setMax(3);
			assertTrue(this.stats.checkGreaterThan(databaseValue, userValue));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator#checkLessThan(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)}.
	 */
	public final void testCheckLessThan() {
		StatisticalMeasureType databaseValue = new StatisticalMeasureType();
		databaseValue.setMax(10);
		databaseValue.setMin(4);
		
		StatisticalMeasureType userValue = new StatisticalMeasureType();
		userValue.setMin(4);
		userValue.setMax(4);
	
		try {
			assertFalse(this.stats.checkLessThan(databaseValue, userValue));
			userValue.setMin(5);
			userValue.setMax(5);
			assertTrue(this.stats.checkLessThan(databaseValue, userValue));
			userValue.setMin(14);
			userValue.setMax(14);
			assertTrue(this.stats.checkLessThan(databaseValue, userValue));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator#checkLessThanOrEquals(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)}.
	 */
	public final void testCheckLessThanOrEquals() {
		StatisticalMeasureType databaseValue = new StatisticalMeasureType();
		databaseValue.setMax(6);
		databaseValue.setMin(4);
		
		StatisticalMeasureType userValue = new StatisticalMeasureType();
		userValue.setMin(7);
		userValue.setMax(7);
	
		try {
			assertTrue(this.stats.checkLessThanOrEquals(databaseValue, userValue));
			
			userValue.setMin(3);
			userValue.setMax(3);
			assertFalse(this.stats.checkLessThanOrEquals(databaseValue, userValue));
			userValue.setMin(4);
			userValue.setMax(4);
			assertTrue(this.stats.checkLessThanOrEquals(databaseValue, userValue));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link project.efg.servlets.efgServletsUtil.StatisticalMeasureComparator#checkEquals(project.efg.efgDocument.StatisticalMeasureType, project.efg.efgDocument.StatisticalMeasureType)}.
	 */
	public final void testCheckEquals() {
		StatisticalMeasureType databaseValue = new StatisticalMeasureType();
		databaseValue.setMax(6);
		databaseValue.setMin(4);
		
		StatisticalMeasureType userValue = new StatisticalMeasureType();
		userValue.setMin(5);
		userValue.setMax(5);
	
		try {
			assertFalse(this.stats.checkEquals(databaseValue, userValue));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

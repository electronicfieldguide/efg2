/**
 * 
 */
package project.efg.servlets.efgServletsUtil;

import java.util.regex.Matcher;

import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.servlets.efgInterface.HandleStatInputAbstract;
import project.efg.servlets.efgInterface.OperatorInterface;
import project.efg.servlets.efgInterface.StatisticalMesureComparatorInterface;
import project.efg.servlets.factory.OperatorFactory;
import project.efg.util.RegularExpresionConstants;

/**
 * @author kasiedu
 *
 */
public class HandleStatInput extends HandleStatInputAbstract {
	public HandleStatInput(StatisticalMesureComparatorInterface measureComparator){	
		super(measureComparator);
	}
	/**
	 * @param dbStats
	 * @param currentInput
	 * @return
	 */
	private boolean checkRange(StatisticalMeasuresType dbStats, 
			String currentInput) {
		OperatorInterface operator = OperatorFactory.getInstance(currentInput);
		String[] fields = currentInput.split(operator.toString());
		String val2Compare = currentInput.trim();
		if(fields.length > 1) {
			val2Compare = fields[1].trim();
		}
		StatisticalMeasureType stats = this.createStatisticalMeasureType(val2Compare);
		if(stats == null) {
			return true;
		}
		
		
		return compareStats(dbStats, stats, operator);
	}
	private StatisticalMeasureType createStatisticalMeasureType(
			String state
			) {
		//log.debug("Processing: " + state);
		StatisticalMeasureType stats  = new StatisticalMeasureType();
		
		
		stats.setAnnotation("");
		stats.setCaption("");
		stats.setEfgKeyRef(1);
		stats.setUnits("");
		try{
			boolean isError = false;
			try{
				//log.debug("About to process measurese[0]: " +measures[0] );
				stats.setMin(Double.parseDouble(state));
			}
			catch(Exception emin){
				//log.debug("Error processing measurese[0]: " +measures[0] );
				LoggerUtilsServlet.logErrors(emin);
				isError = true;
				System.err.println(emin.getMessage());
			}
			try{
				//log.debug("About to process measurese[1]: " +measures[1] );
				stats.setMax(Double.parseDouble(state));
			}
			catch(Exception emax){
				isError = true;
				//log.debug("Error processing measurese[1]: " +measures[1] );
				LoggerUtilsServlet.logErrors(emax);
			}
			if(!isError){
				return stats;
			}
			
			
			
		}
		catch(Exception ee){
			//log.debug("Error processing : " + state );
			LoggerUtilsServlet.logErrors(ee);
		}
		return null;
	}
	/**
	 * @param dbStats
	 * @param stats
	 * @param operator
	 * @return
	 */
	private boolean compareStats(StatisticalMeasuresType dbStats, StatisticalMeasureType userValue, OperatorInterface operator) {
		//System.out.println("Val: " + val2Compare);
		
		
		for(int j = 0; j < dbStats.getStatisticalMeasureCount();j++){
				StatisticalMeasureType databaseValue = dbStats.getStatisticalMeasure(j);
				//is userValue in the range of the value in the database?
				try{
					
					boolean bool = this.measureComparator.isInRange(operator,userValue,databaseValue);
					if(bool) {
						
						return bool;
					}
				}
				catch(Exception ee){
					ee.printStackTrace();
				}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.HandleStatInputAbstract#isInRange(project.efg.efgDocument.StatisticalMeasuresType, java.lang.String)
	 */
	public boolean isInRange(StatisticalMeasuresType dbStats, String userValues) {
		userValues = userValues.toUpperCase();
		String[] params = null;
		boolean isAnd = false;
		
		//convert to operators
		//System.out.println("userValue before convert: " + userValues);
		userValues = this.convertToOperators(userValues);
		
		if(userValues.indexOf(RegularExpresionConstants.ORSEP) > -1) {
			params = userValues.split(RegularExpresionConstants.ORSEP);
		}
		else if(userValues.indexOf(RegularExpresionConstants.ANDSEP) > -1) {
			params = userValues.split(RegularExpresionConstants.ANDSEP);
			isAnd = true;
			
		}
		else { 
			params = userValues.split(RegularExpresionConstants.ORSEP);
			
		}
		
		boolean bool = false;
		for(int i = 0; i < params.length; i++) {
			String currentInput = params[i];
			
			
			
			bool = checkRange(dbStats,currentInput.trim());
			if(!isAnd && bool) {
				return true;
			}
			if(isAnd && !bool) {
				
				
				return false;
			}
			
		}
		if(isAnd && bool) {
			
			return true;
		}
		return false;
	}
	/**
	 * @param userValues
	 * @return
	 */
	private String convertToOperators(String userValues) {
		
		Matcher matcher = RegularExpresionConstants.matchNumberDashNumberPattern.matcher(userValues);
	   if(matcher.find()) {
		    
		    	 matcher = RegularExpresionConstants.matchNumberDashDashPattern.matcher(userValues);
		    	 
		    	  if(matcher.find()) {
		    		  int index = userValues.lastIndexOf("-");
		    		  if(index > -1) {
		    			  String firstPart = RegularExpresionConstants.GREATERTHANEQUAL_SYMBOL + " "+ userValues.substring(0,index-1);
		    			  String secondPart =RegularExpresionConstants.LESSTHANEQUAL_SYMBOL + " "+  userValues.substring(index,userValues.length());
		    			  return firstPart + " " + RegularExpresionConstants.ANDSEP + " " + secondPart;
		    		  }
		    	  }
		    	 else {
		    		 
		    		 matcher = RegularExpresionConstants.matchNumberDashPattern.matcher(userValues);
		    		 
		    		 if(matcher.find()) {
		    			  int index = userValues.lastIndexOf("-");
		    			  if(index > -1) {
		    				  String firstPart = RegularExpresionConstants.GREATERTHANEQUAL_SYMBOL + " "  + userValues.substring(0,index);
		    				  String secondPart = RegularExpresionConstants.LESSTHANEQUAL_SYMBOL + " " + userValues.substring(index+1,userValues.length());
		    				  return firstPart + " " + RegularExpresionConstants.ANDSEP + " " + secondPart;
		    			  }
		    		  }
		    	  }
		    	  
	   }
	   return userValues;
	}
}

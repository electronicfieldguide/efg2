/**
 * 
 */
package project.efg.servlets.efgServletsUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.EFGType;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourceType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasureType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.servlets.factory.EFGParseObjectFactory;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class EFGParseObjectFactoryImpl implements EFGParseObjectFactory {
	
	
	
	


	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGParseObjectInterafce#createStatisticalMeasures(java.lang.String)
	 */
	public StatisticalMeasuresType createStatisticalMeasures(EFGParseObjectList lists){
		StatisticalMeasuresType stats = null;
		Iterator iter = lists.iterator();
		double min = 0;
		double max = 0;
		boolean unChanged = true;
		String unit = null;
		while(iter.hasNext()){
			EFGParseObject obj = (EFGParseObject)iter.next();
			if(stats == null){
				stats = new StatisticalMeasuresType();
				stats.setDatabaseName(lists.getDatabaseName());
				stats.setName(lists.getName());
			}
			StatisticalMeasureType stat = this.createStatisticalMeasureType(
					obj.getState(), 
					obj.getResourceLink(),
					obj.getAnnotation(),
					obj.getAnnotation()
					);
			
			if(stat != null){
				if((unit == null) || ("".equals(unit.trim()))){
					unit = stat.getUnits();
				}
				double tempMin = stat.getMin();
				double tempMax = stat.getMax();
				if(unChanged){
					min = tempMin;
					max = tempMax;
					unChanged = false;
				}
				else{
					if(min > tempMin){
						min = tempMin;
					}
					if(tempMax > max){
						max = tempMax;
					}
				}
				stats.addStatisticalMeasure(stat);
			}
		}
		if(unit == null){
			unit ="";
		}
		
		
		if(!unChanged){
			//log.debug("Min: " + min);
			//log.debug("Max: " + max);
			stats.setMax(max);
			stats.setMin(min);
			stats.setUnit(unit);
		}
		else{
			//log.debug("no changes to stats");
		}
		return stats;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGParseObjectInterafce#createItems(java.lang.String)
	 */
	public ItemsType createItems(EFGParseObjectList lists){
		ItemsType items = null;
		
		Iterator iter = lists.iterator();
		
		while(iter.hasNext()){
			EFGParseObject obj = (EFGParseObject)iter.next();
			
			if(items == null){
				items = new ItemsType();//IOC
				items.setDatabaseName(lists.getDatabaseName());
				items.setName(lists.getName());
			}
			EFGType item = createEFGType(
					obj.getState(), 
					obj.getResourceLink(),
					obj.getAnnotation(),
					obj.getAnnotation()
					);
			if(item != null){
				items.addItem(item);
			}
		}
		return items;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGParseObjectInterafce#createItems(java.lang.String)
	 */
	public ItemsType createItemsNoParse(String states, String name, String dbName){
		ItemsType items = new ItemsType();//IOC
		items.setDatabaseName(dbName);
		items.setName(name);
		EFGType item = createEFGType(states,"","","");
		if(item != null){
			items.addItem(item);
		}
		return items;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGParseObjectInterafce#createMediaResources(java.lang.String)
	 */
	public MediaResourcesType createMediaResources(EFGParseObjectList objLists){
		MediaResourcesType media = null;
		
		
		Iterator iter = objLists.iterator();
		while(iter.hasNext()){
			EFGParseObject obj = (EFGParseObject)iter.next();
			if(media == null){
				media = new MediaResourcesType();
				media.setDatabaseName(objLists.getDatabaseName());
				media.setName(objLists.getName());
			}
			EFGType efgType = createEFGType(
					obj.getState(), 
					obj.getResourceLink(),
					obj.getAnnotation(),obj.getAnnotation()
					);
			if(efgType != null){
				MediaResourceType mtype = new MediaResourceType();
				mtype.setAnnotation(efgType.getAnnotation().trim());
				mtype.setContent(efgType.getContent().trim());
				mtype.setCaption(efgType.getAnnotation().trim());
				mtype.setEfgKeyRef(efgType.getEfgKeyRef());
				mtype.setResourceLink(efgType.getResourceLink().trim());
				media.addMediaResource(mtype);
			}
		}
		return media;
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.EFGParseObjectInterafce#createEFGLists(java.lang.String)
	 */
	public EFGListsType createEFGLists(EFGParseObjectList objLists){
		EFGListsType lists= null;
	
		
		Iterator iter = objLists.iterator();
		while(iter.hasNext()){
			EFGParseObject obj = (EFGParseObject)iter.next();
			if(lists == null){
				lists= new EFGListsType();//IOC
				lists.setDatabaseName(objLists.getDatabaseName());
				lists.setName(objLists.getName());
			}
			EFGType efgType = createEFGType(
					obj.getState(), 
					obj.getResourceLink(),
					obj.getAnnotation(),
					obj.getAnnotation()
					);
			if(efgType != null){
				lists.addEFGList(efgType);
			}
		}
		return lists;
	}
	private EFGType createEFGType(String state, 
			String resourceLink, 
			String annotation, String caption) {
		EFGType efgType = new EFGType();
		
		efgType.setCaption("");
		efgType.setContent(state.trim());
		if (resourceLink == null) {
			resourceLink = "";
		}
		if(annotation == null){
			annotation = "";
		}
		efgType.setAnnotation(annotation.trim());
		efgType.setResourceLink(resourceLink.trim());
		efgType.setEfgKeyRef(1);
		return efgType;
	}
	private String[] parseStats(String inputStr){
		//log.debug("InputString: " + inputStr);
		
		String[] fieldsR = EFGImportConstants.rightParenPattern.split(inputStr);
		
		ArrayList lists = new ArrayList();
		for(int i = 0; i < fieldsR.length; i++){//right paren removed
			
			String fieldR = fieldsR[i].trim();
			//log.debug("fieldR: " + fieldR);
			if("".equals(fieldR)){//skip over empty strings
				continue;
			}
			if(fieldR.equals(EFGImportConstants.DASHSEP)){//skip over '-' that exists alone
				continue;
			}
			
			String []fieldsL =  EFGImportConstants.leftParenPattern.split(fieldR); 
			
			for(int j = 0; j < fieldsL.length; j++){//left paren removed
				String fieldL = fieldsL[j].trim();
				//log.debug("fieldl: " + fieldL);
				if("".equals(fieldL)){//skip over empty string
					continue;
				}
				if(fieldL.indexOf(EFGImportConstants.DASHSEP) > 0){//dash not the first item in string
					
				//	String[] minVals = fieldL.split(EFGImportConstants.DASHSEP);
					String[] minVals = EFGImportConstants.dashParenPattern.split(fieldL);
					//log.debug("FieLDL After split: " + minVals.length);
					for(int w=0; w < minVals.length; w++){
						String minVal = minVals[w].trim();
						//log.debug("FieLDL After split value: " + minVal);
						if((minVal != null) && (!minVal.trim().equals(""))){
							lists.add(minVal);
						}
					}
				}
				else{
					lists.add(fieldL);
				}
			}
		}
		if(lists.size() > 0){
			return this.list2StringArray(lists);
		}
		return null;
	}
	private String[] list2StringArray(List lists){
		String[] states = new String[lists.size()];
		boolean isPresent = false;
		for(int i =0; i < lists.size();i++){
			String state = (String)lists.get(i);
			if((state != null) && (!state.trim().equals(""))){
				states[i] = state;
				isPresent = true;//a state was successfully added
			}
		}
		if(isPresent){
			return states;
		}
		return null;
	}
	private String[] preProcess(String inputStr){
		//String patternStr = "[A-Z]+";// remove everything that is an
		// alphabet
		//Pattern p = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
		//log.debug("About to parse: " + inputStr);
		//log.debug("With pattern: " + patternStr);
		//return p.split(inputStr);
		return EFGImportConstants.alphaPattern.split(inputStr);
		
	}
	private String[] parseStatsMeasure(String inputStr1) {
		
		
		Double minValD = null;
		Double maxValD = null;
		Double tempD = null;
		Double currentD = null;
		String minVal = "";
		String maxVal = "";
	
		try {
			if((inputStr1 == null) || (inputStr1.equals(""))){
				return null;
			}
		
			String[] fields = preProcess(inputStr1);
			
	
			for (int j = 0; j < fields.length; j++) {
				try {
					String[] vals = parseStats(fields[j].trim());
					//log.debug("Size after split: " +vals.length);
					
					for (int i = 0; i < vals.length; i++) {
						try {
							
							String newStr = vals[i].trim();
							//String patternStr = "^\\d+$|^-\\d+$";
							//log.debug("New Pattern: " + patternStr);
							//log.debug("New State to parse: " + newStr);
							
							//Pattern p = Pattern.compile(patternStr);
							
							//Matcher matcher = p.matcher(newStr);// find strings
																// with only
																// digits
							
							Matcher matcher = EFGImportConstants.matchNumberAtEndPattern.matcher(newStr);
							matcher.find();
							String match = matcher.group();
							if ((match != null) || (!match.trim().equals(""))) {
								if (minValD == null) {
									minValD = new Double(match);
								}
								if (maxValD == null) {
									maxValD = new Double(match);
									if (minValD.compareTo(maxValD) > 0) {
										tempD = minValD;
										minValD = maxValD;
										maxValD = tempD;
									}
								}
								if ((minValD != null) && (maxValD != null)) {
									currentD = new Double(match);
									if (minValD.compareTo(currentD) > 0) {
										minValD = currentD;
									} else if (currentD.compareTo(maxValD) > 0) {
										maxValD = currentD;
									}
								}
							}
						} catch (Exception ex) {
							LoggerUtilsServlet.logErrors(ex);
						}
					}
				} catch (Exception ex) {
					LoggerUtilsServlet.logErrors(ex);
	
				}
			}
	
		} catch (Exception ee) {
			LoggerUtilsServlet.logErrors(ee);
	
		}
		if((minValD == null) || (maxValD == null)){
			return null;
		}
		
		minVal = minValD.toString();
		maxVal = maxValD.toString();
	
		String[] minMax = new String[2];
		minMax[0] = minVal;
		minMax[1] = maxVal;
		return minMax;
	
	}
	private String[] isInNumericRange(String states) {
		if ((states == null) || (states.trim().equals(""))) {
			return null;
		}
		return this.parseStatsMeasure(states);
	}
	private String parseUnits(String inputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			//String patternStr = "[A-Z]+";
			//Pattern p = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
			
			//Matcher matcher = p.matcher(inputStr);
			Matcher matcher = EFGImportConstants.alphaPattern.matcher(inputStr);
			// boolean matchFound = false;
			String match = null;
			while (matcher.find()) {
				try {
					match = matcher.group();
					if (buffer.length() > 0) {
						buffer.append(",");
					}
					buffer.append(match);
				} catch (Exception ee) {
					LoggerUtilsServlet.logErrors(ee);
				}
			}
		} catch (Exception vvv) {
			LoggerUtilsServlet.logErrors(vvv);
		}
		return buffer.toString();
	}
	private StatisticalMeasureType createStatisticalMeasureType(
			String state, 
			String resourceLink, 
			String annotation, String caption) {
		//log.debug("Processing: " + state);
		StatisticalMeasureType stats = null;
		String[] measures = this.isInNumericRange(state);
		if (measures == null) {
			//log.debug("measures is null");
			return stats;
		}
		//log.debug("Size of measures: " + measures.length);
		stats = new StatisticalMeasureType();
		
		String units = this.parseUnits(state.trim());
		if (units == null) {
			units = "";
		}
		stats.setAnnotation(annotation.trim());
		stats.setCaption(caption.trim());
		stats.setEfgKeyRef(1);
		stats.setUnits(units.trim());
		try{
			boolean isError = false;
			try{
				//log.debug("About to process measurese[0]: " +measures[0] );
				stats.setMin(Double.parseDouble(measures[0]));
			}
			catch(Exception emin){
				//log.debug("Error processing measurese[0]: " +measures[0] );
				LoggerUtilsServlet.logErrors(emin);
				isError = true;
			}
			try{
				//log.debug("About to process measurese[1]: " +measures[1] );
				stats.setMax(Double.parseDouble(measures[1]));
			}
			catch(Exception emax){
				isError = true;
				//log.debug("Error processing measurese[1]: " +measures[1] );
				LoggerUtilsServlet.logErrors(emax);
			}
			if(!isError){
				if(stats.getMax() < stats.getMin()){
					swap(stats);
				}
				return stats;
			}
		}
		catch(Exception ee){
			//log.debug("Error processing : " + state );
			LoggerUtilsServlet.logErrors(ee);
		}
		return null;
	}
	private void swap(StatisticalMeasureType stats){
		double temp = stats.getMax();
		stats.setMax(stats.getMin());
		stats.setMin(temp);
	}
	
	
}

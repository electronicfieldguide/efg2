/**
 * 
 */
package project.efg.servlets.rdb;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class QueryMaker {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(QueryMaker.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public QueryMaker() {
		super();
		
	}
	/**
	 * 
	 * @return the EFGDocument object built from the query
	 */
	protected boolean matchNumber(String states) {
		String patternStr = "\\d+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(states);
		try {
			return matcher.find();
		} catch (Exception vvv) {
		}
		return false;
	}
	/**
	 * This method builds a query from the request such as
	 * /efg/servlet/search?Genus=Solanum&Species=chrysotrichum
	 * /efg/servlet/search?searchStr=( Genus=Solanum && Species=chrysotrichum )
	 * 
	 * @param req
	 *            the servlet request object
	 * @return the query string
	 */
	public String buildQuery(HttpServletRequest req) {
		
		
		String maxDispStr = req.getParameter(EFGImportConstants.MAX_DISPLAY);
		int maxDisplay = -1;
		
		if (maxDispStr != null) {
			try {
				if (!EFGImportConstants.MAX_DISPLAY_IGNORE
						.equalsIgnoreCase(maxDispStr.trim())) {
					maxDisplay = Integer.parseInt(maxDispStr);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		StringBuffer querySB = new StringBuffer();
		Hashtable paramValuesTable = new Hashtable();
		try {
			//querySB.append("SELECT * FROM " + this.datasourceName);
			Enumeration paramEnum = req.getParameterNames();
			int paramNo = 0;
			
			while (paramEnum.hasMoreElements()) {
				String paramName = (String) paramEnum.nextElement();
				log.debug("paramName: " + paramName);
				/*if (specialParams.contains(paramName)
						|| paramName.equals(EFGImportConstants.SEARCHSTR)) {
					log.debug("Returning");
					continue;
				}*/
				String legalName = paramName;
				log.debug("LegalName: " + legalName);
				if ((legalName == null) || ("".equals(legalName.trim()))) {
					log.debug("skiping");
					continue;
				}
				if (paramName.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
					paramName = paramName.replaceAll(
							EFGImportConstants.SERVICE_LINK_FILLER, " ");
				}
				String[] paramValues = req.getParameterValues(paramName);
				log.debug("paramaValues length: " + paramValues.length);
				//iterate over it and add ors
				String pVal = null;
				StringBuffer orBuffer = new StringBuffer();
				int orCounter = 0;
				for (int jj = 0; jj < paramValues.length; jj++) {//add or query
					
					pVal = paramValues[jj];
					log.debug("pVal: " + pVal);
					if (pVal == null) {
						log.debug("skiping");
						continue;
					}
					if (pVal.indexOf(EFGImportConstants.SERVICE_LINK_FILLER) > -1) {
						pVal = pVal.replaceAll(
								EFGImportConstants.SERVICE_LINK_FILLER, " ");
					}
					if (orCounter > 0) {
						orBuffer.append(" OR ");
					}
					orBuffer.append(" ( ");
					orBuffer.append(legalName);
					orBuffer.append(" LIKE ");
					orBuffer.append("'%");
					if ((this.matchNumber(pVal)) || ("".equals(pVal.trim()))) {//if there is a number
						orBuffer.append("'");// value
					} else {
						orBuffer.append(pVal + "%'");
					}
					orBuffer.append(" ) ");
					
					if (paramValuesTable.containsKey(legalName.toLowerCase())) {//could have parameter with multiple values
						String oldVal = (String) paramValuesTable.get(legalName
								.toLowerCase());
						if (oldVal.indexOf(pVal) == -1) {//does not exists
							pVal = oldVal + EFGImportConstants.PIPESEP + pVal;
							paramValuesTable.put(legalName.toLowerCase(), pVal);
						}
					} else {
						paramValuesTable.put(legalName.toLowerCase(), pVal);
					}
					++orCounter;
				}
				
				//String legalName = (String)mapping.get(paramName);
				if (!orBuffer.toString().trim().equals("")) {
					if (paramNo == 0) {
						querySB.append(" where ");
						querySB.append("( ");
						querySB.append(orBuffer);
						querySB.append(" ) ");
					} else {
						querySB.append(" and ( ");
						querySB.append(orBuffer);
						querySB.append(" ) ");
					}
					++paramNo;
				}
				
			}
			if (maxDisplay > 0) {
				String database = EFGImportConstants.EFGProperties
				.getProperty("database");
				if (EFGImportConstants.MYSQL.equalsIgnoreCase(database)) {
					querySB.append(" limit ");
					querySB.append(maxDisplay + "");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			LoggerUtilsServlet.logErrors(e);
		}
		log.debug("Query: " + querySB.toString());
		return querySB.toString();
	}
}

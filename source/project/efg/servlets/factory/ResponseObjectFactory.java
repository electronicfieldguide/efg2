/**
 * 
 */
package project.efg.servlets.factory;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgInterface.ResponseObject;
import project.efg.servlets.efgInterface.XSLTResponseObject;
import project.efg.servlets.efgServletsUtil.ErrorResponseObject;
import project.efg.servlets.efgServletsUtil.NoMatchResponseObject;

/**
 * @author kasiedu
 *
 */
public class ResponseObjectFactory {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ResponseObjectFactory.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public ResponseObjectFactory() {
	
	}
	private static int getTaxonSize( EFGDocument efgDocument ){
		int taxonSize = -1;
		
			if(efgDocument != null){
				try{
					taxonSize = efgDocument.getTaxonEntries().getTaxonEntryCount();
				}
				catch(Exception ee){
					log.error(ee.getMessage());
					taxonSize = 0;
				}
			}
		return taxonSize;
	}
	public static synchronized ResponseObject getResponseObject(HttpServletRequest req,
			 EFGDocument efgDocument, String realPath){
		
			int taxonSize = getTaxonSize(efgDocument);
			if(taxonSize== -1){//forward to Error page
				log.debug("Forward to error page");
				return new ErrorResponseObject(req,efgDocument,realPath);
			}
			else if(taxonSize == 0){//forward to no results page
				log.debug("Forward to no match page");
				return new NoMatchResponseObject(req,efgDocument,realPath);
			}
			else{//forward to XSLTResponseObject
				log.debug("Forward to response page");
				return new XSLTResponseObject(req,efgDocument,realPath);
			}
		
	}

}

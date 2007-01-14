/**
 * 
 */
package project.efg.servlets.efgServletsUtil;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;



import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgInterface.ResponseObject;
import project.efg.util.EFGImportConstants;
import project.efg.util.RegularExpresionConstants;

/**
 * @author kasiedu
 *
 */
public class ErrorResponseObject extends ResponseObject {
	
	/**
	 * @param req
	 * @param efgDocument
	 * @param realPath
	 */
	public ErrorResponseObject(HttpServletRequest req, EFGDocument efgDocument,
			String realPath) {
		super(req, efgDocument, realPath);
		
	}
	private void logErrorMessage(){
		Enumeration paramEnum = req.getParameterNames();
		StringBuffer errorBuffer = new StringBuffer();
		errorBuffer.append("An Error ocurred for this request!!!\n");
		errorBuffer.append("*****************************\n");
		while (paramEnum.hasMoreElements()) {
			String legalName = (String) paramEnum.nextElement();
			String[] paramValues = req.getParameterValues(legalName);
			errorBuffer.append(legalName);
			errorBuffer.append("=");
			for(int i = 0; i < paramValues.length; i++){
				errorBuffer.append(paramValues[i]);
				errorBuffer.append(RegularExpresionConstants.NOPATTERN);
			}
			errorBuffer.append("\n");
		}
		errorBuffer.append("Client will be forwarded to error page");
		errorBuffer.append("*****************************\n\n");
		LoggerUtilsServlet.logErrors(new Exception(errorBuffer.toString()));
		//log.error(errorBuffer.toString());
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ResponseObject#createForwardPage()
	 */
	protected void createForwardPage() {
		this.logErrorMessage();
		StringBuffer fwdStrEncodedBuffer = new StringBuffer("/");
		fwdStrEncodedBuffer.append(EFGImportConstants.ERROR_PAGE);
		this.forwardPage = fwdStrEncodedBuffer.toString();
	}

}

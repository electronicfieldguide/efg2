/**
 * 
 */
package project.efg.server.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import project.efg.server.interfaces.ResponseObject;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;
import project.efg.util.utils.EFGDocTemplate;

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
	public ErrorResponseObject(HttpServletRequest req, EFGDocTemplate efgDocument,
			String realPath) {
		super(req, efgDocument, realPath);
		
	}
	private void logErrorMessage(){
		Enumeration paramEnum = req.getParameterNames();
		StringBuilder errorBuffer = new StringBuilder();
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
		StringBuilder fwdStrEncodedBuffer = new StringBuilder("/");
		fwdStrEncodedBuffer.append(EFGImportConstants.ERROR_PAGE);
		this.forwardPage = fwdStrEncodedBuffer.toString();
	}

}

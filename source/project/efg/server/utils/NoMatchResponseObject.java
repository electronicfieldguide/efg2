/**
 * 
 */
package project.efg.server.utils;

import javax.servlet.http.HttpServletRequest;

import project.efg.server.interfaces.ResponseObject;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.EFGDocTemplate;

/**
 * @author kasiedu
 *
 */
public class NoMatchResponseObject extends ResponseObject {
	
	/**
	 * @param req
	 * @param efgDocument
	 * @param realPath
	 */
	public NoMatchResponseObject(HttpServletRequest req, EFGDocTemplate efgDocument,
			String realPath) {
		super(req, efgDocument, realPath);
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ResponseObject#createForwardPage()
	 */
	protected void createForwardPage() {
		StringBuilder fwdStrEncodedBuffer = new StringBuilder("/");
		fwdStrEncodedBuffer.append(EFGImportConstants.NO_MATCH_PAGE);
		this.forwardPage = fwdStrEncodedBuffer.toString();
	}
}

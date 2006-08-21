/**
 * 
 */
package project.efg.servlets.efgServletsUtil;

import javax.servlet.http.HttpServletRequest;

import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgInterface.ResponseObject;
import project.efg.util.EFGImportConstants;

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
	public NoMatchResponseObject(HttpServletRequest req, EFGDocument efgDocument,
			String realPath) {
		super(req, efgDocument, realPath);
	}
	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ResponseObject#createForwardPage()
	 */
	protected void createForwardPage() {
		StringBuffer fwdStrEncodedBuffer = new StringBuffer("/");
		fwdStrEncodedBuffer.append(EFGImportConstants.NO_MATCH_PAGE);
		this.forwardPage = fwdStrEncodedBuffer.toString();
	}
}
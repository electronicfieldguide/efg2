/**
 * 
 */
package project.efg.servlets.efgInterface;

import javax.servlet.http.HttpServletRequest;

import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgServletsUtil.XSLTObjectInterface;
import project.efg.servlets.factory.XSLTFactoryObject;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public abstract class ResponseObject {
	protected HttpServletRequest req;
	protected XSLTObjectInterface xslt;
	protected EFGDocument efgDocument;
	private int taxonSize = -1;
	protected String realPath;
	protected String forwardPage;
	private String searchType;
	protected String mainTableName;
	/**
	 * 
	 */
	public ResponseObject(HttpServletRequest req,EFGDocument efgDocument,String realPath) {
		this.req = req;
		this.mainTableName = this.req.getParameter(EFGImportConstants.ALL_TABLE_NAME);
		this.efgDocument = efgDocument;
		
		this.realPath = realPath;
		this.searchType = req.getParameter(EFGImportConstants.SEARCHTYPE);
		this.createForwardPage();
		
	}
	public XSLTObjectInterface getXSLTObject(){
		XSLTObjectInterface  xslto = XSLTFactoryObject.getInstance(this.taxonSize, this.searchType);
		xslto.setMainDataTableName(this.mainTableName);	
		return xslto;
	 
	}
	protected int getTaxonSize(){
		if(this.efgDocument != null){
			this.taxonSize = efgDocument.getTaxonEntries().getTaxonEntryCount();
		}
		return taxonSize;
	}
	protected abstract void createForwardPage();
	
	public String getForwardPage(){
		return this.forwardPage;
	}

}

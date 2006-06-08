/**
 * 
 */
package project.efg.servlets.efgInterface;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import project.efg.efgDocument.EFGDocument;
import project.efg.servlets.efgServletsUtil.XSLProperties;
import project.efg.servlets.efgServletsUtil.XSLTObjectInterface;
import project.efg.util.EFGImportConstants;

/**
 * @author kasiedu
 *
 */
public class XSLTResponseObject extends ResponseObject {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(XSLTResponseObject.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * @param req
	 * @param xslt
	 * @param efgDocument
	 * @param taxonSize
	 */
	public XSLTResponseObject(HttpServletRequest req,
			 EFGDocument efgDocument, String realPath) {
		super(req,  efgDocument, realPath);
		
	}

	/* (non-Javadoc)
	 * @see project.efg.servlets.efgInterface.ResponseDecorator#createForwardPage()
	 */
	protected void createForwardPage() {
		String displayName = req.getParameter(EFGImportConstants.DISPLAY_NAME);
		req.setAttribute(EFGImportConstants.DISPLAY_NAME, displayName);
		
		String dsName = req.getParameter(EFGImportConstants.DATASOURCE_NAME);
		
		
		try {
			if (dsName == null) {
				try {
					dsName = this.getDatasourceName(efgDocument);
					log.debug("DatasourceName obtained from EFgDocument: " + dsName);
				} catch (Exception ee) {
					log.error(ee.getMessage());
				}
			}
			if(dsName != null){
				req.setAttribute(EFGImportConstants.DATASOURCE_NAME, dsName);
			}
			else{
				log.debug("DatasourceName is null");
			}
			int taxonSize = this.getTaxonSize();
			req.setAttribute(EFGImportConstants.XML, efgDocument);
			req.setAttribute(EFGImportConstants.TAXONSIZE_STR,taxonSize+"");
			
			
			XSLTObjectInterface xslType = 
				this.getXSLTObject();
		
			XSLProperties xslProps = xslType.getXSLProperties(req.getParameterMap(), realPath);
			
			if (xslProps == null) {
				throw new Exception(
						"Could not find xsl file for: " + 
						dsName
						);
			}
			
			
			Properties props = xslProps.getXSLParameters();
			if (props != null) {
				this.setRequests(req, props);
			}
			String xslFileName = xslProps.getXSLFileName();
			// get xsl file name from the templateConfig file name
			StringBuffer forwardStringBuffer = 
			new StringBuffer(req.getContextPath());
			forwardStringBuffer.append("/");
			forwardStringBuffer
			.append(EFGImportConstants.TEMPLATES_FOLDER_NAME);
			forwardStringBuffer.append("/");
			forwardStringBuffer.append(xslFileName);
			
			StringBuffer fwdStrEncodedBuffer = new StringBuffer("/");
			//forward to pdf or html xslt servlet
			fwdStrEncodedBuffer.append(this.createForwardString(xslFileName));
			fwdStrEncodedBuffer.append("?xsl=");
			fwdStrEncodedBuffer.append(
					URLEncoder.encode(forwardStringBuffer.toString(), 
							"UTF-8")
							);
			this.forwardPage = fwdStrEncodedBuffer.toString();
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			//create fwd page and return its page
		}
	}
	private String getDatasourceName(EFGDocument efgDocument) throws Exception {
		String dsName = null;
		
		try {
			for (int i = 0; i < efgDocument.getDatasources()
			.getDatasourceCount(); i++) {
				dsName = efgDocument.getDatasources().getDatasource(i)
				.getName();
				if ((dsName == null) || (dsName.trim().equals(""))) {
					continue;
				}
				break;
			}
			if (dsName == null) {
				throw new Exception("A datsourceName could not be found");
			}
		} catch (Exception ee) {
			log.error(ee.getMessage());
			//LoggerUtilsServlet.logErrors(ee);
			//throw ee;
		}
		return dsName;
		
	}
	private void setRequests(HttpServletRequest req, Properties props) {
		
		for (Enumeration e = props.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = (String) props.get(key);
			req.setAttribute(key, val);
		}
	}
	/**
	 * Determine the servlet to forward to from xslt.
	 * Not a scalable solution at all...Use a Factory instead.
	 * @param xslFileName
	 * @return the servlet to do xslt transformation
	 * 
	 */
	private String createForwardString(String xslFileName){
		if (xslFileName.endsWith(".fo")) {
			return EFGImportConstants.FOPSERVLET; 
		}
		return EFGImportConstants.APPLYXSL;
	}
}

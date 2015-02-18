

package project.efg.client.utils.nogui;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import project.efg.efgDocument.EFGDocument;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplateType;
import project.efg.templates.taxonPageTemplates.TaxonPageTemplates;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.templates.taxonPageTemplates.XslPageType;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.pdf.bookmaker.BookMaker;
import project.efg.util.pdf.bookmaker.ConfigHandler;
import project.efg.util.utils.EFGDocTemplate;



public class BookMakerDriver{
	private EFGDocument efgdoc;
	private XslPage xslpage;
	private URL url;
	private String templateName;
	private URL mediaresourcesURL;
	
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(BookMakerDriver.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 * @param url - url to fetch an EFGDocTemplate object
	 */
	public BookMakerDriver(URL url, String templateName) {
		this.url = url;
		this.templateName = templateName;
		this.mediaresourcesURL = this.getImageServerURL();
	}
	/**
	 * 
	 * @return true if document was successfully created
	 */
	public boolean makePDF(String pdfFileName){
		if(this.templateName == null){
			log.error("Template Name is null");
			return false;
		}
		if(!getConfigandDoc()){
			log.error("Can't find ConfigDoc");
			return false;
		}
		
		ConfigHandler config = new ConfigHandler(this.xslpage);
		BookMaker bookmaker = new BookMaker(this.efgdoc,config,this.mediaresourcesURL);
		try {
			
			bookmaker.writeDocument(new FileOutputStream(pdfFileName), false);
			return true;
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
		return false;
	}
	private boolean getConfigandDoc(){
		EFGDocTemplate docT = getEFGDocument();
		
		if(docT == null){
			log.error("Can't get Document from server");
			return false;
		}
		this.efgdoc = docT.getEFGDocument();
		if(this.efgdoc == null){
			log.error("EFGDocument is null");
			return false;
		}
		
		
		this.xslpage = getXslPage(docT.getTaxonPageTemplates());
	     if(this.xslpage == null){
	    	 log.error("XslPage is null");
	    	 return false;
	     }
	     return true;
	}
	/**
	 * 
	 * @return an EFGDocTemplate
	 */
	private EFGDocTemplate getEFGDocument(){
		
        URLConnection yc;
		try {
			yc = url.openConnection();
			   BufferedReader in = new BufferedReader(
                       new InputStreamReader(
                       yc.getInputStream()));
			   String inputLine = null;
			   StringWriter writer = new StringWriter();
			
			   while ((inputLine = in.readLine()) != null) {
			       writer.write(inputLine);
			   }
			   in.close();
			   StringReader reader = 
				   new StringReader(writer.getBuffer().toString());
			   writer.close();
			   writer.flush();
				return (EFGDocTemplate)EFGDocTemplate.unmarshalEFGDocTemplate(reader);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * @param externalForm
	 * @return
	 */
	private URL getImageServerURL() {
		String externalForm = this.url.toExternalForm();
		int index = externalForm.indexOf(EFGImportConstants.DOC_2_XML);
		if( index > -1){
			externalForm =  externalForm.substring(0,index);
			try {
				return new URL(externalForm + "EFGImages");
			} catch (MalformedURLException e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Get the configuration for the current template
	 * @return
	 */
	private XslPage getXslPage(TaxonPageTemplates taxonPageTemplates){
		if(taxonPageTemplates == null){
			return null;
		}
		TaxonPageTemplateType tp = taxonPageTemplates.getTaxonPageTemplate(0);
		try{
			XslPageType xslPageType =  tp.getXSLFileNames().getXslBookPages();
			
			if(xslPageType != null){
				for(int i = 0; i < xslPageType.getXslPageCount(); i++){
					XslPage page = xslPageType.getXslPage(i);
					if(this.templateName.equals(page.getDisplayName())){
						return page;
					}
				}
			}
		
		}
		catch(Exception ee){
			log.error(ee.getMessage());
		}
		
		return null;
	}
	static String help(){
		StringBuilder buffer = new StringBuilder();
		 buffer.append("usage java BookMakerDriver URL datasourceName templateName glossary(true or false) [outputFileName]\n\n");

		buffer.append("URL = url to server where configuration was done\n");
		buffer.append("datasourceName = must be the datasourceName (identifier name)\n");
		buffer.append("templateName = must be the template name (The one used to save the config)\n");
		buffer.append("glossary = true if the datasource is a glossary- defaults to false\n");
		buffer.append("outputFileName = The name of the output file defaults to PDFBook.pdf\n");
		buffer.append("outputFileName is optional\n");
		return buffer.toString();
	}
	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception{
		
		System.out.println("-> Creates a PDF file");
		System.out.println("-> files generated in /books subdirectory:");
		//args[0] = url to server
		//args[1] = must be the datasourceName (identifier name)
		//args[2] = must be the template name (The one used to save the config)
		//args[3] = true if the datasource is a glossary false otherwise
		//args[4] = The name of the output file defaults to PDFBook.pdf
		String bookDirectory = "books";
		File file = new File(bookDirectory);
		if(!file.exists()){
			file.mkdirs();
		}
		boolean isTest = false;
		 if(args[0].equals("-test")){
			args = new String[3];
			
			args[0] = "http://panda.cs.umb.edu/efg2"; 
			args[1] = "bostonnaturecenterefg_1185631295755";
			args[2] = "Test BNC Book1";
			args[3] = "false";
		
		
			//args[1] = ;
			args[4] = "BNCBook.pdf";
			isTest = true;
		}
		 else if(null != args || args.length < 3 || args.length > 4){
			 help();
			 return;
		}
		String fileName = null;
		if(args.length == 3){
			fileName= bookDirectory + "/PDFBook.pdf";
		}
		else{
			fileName = bookDirectory + "/" + args[3];
		}
		LoggerUtils loggers = new LoggerUtils();
		try {
			String strURL = args[0];
			String templateName = args[2];

			
			strURL = appendDOCURL(args[0], args[1], args[3]);
			checkURL(strURL);
			BookMakerDriver bookmaker = new BookMakerDriver(new URL(strURL),templateName);
			bookmaker.makePDF(fileName);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	/**
	 * @param strURL
	 * @return
	 */
	private static String appendDOCURL(String strURL, String datasourceName, String glossary) {
		StringBuilder url = new StringBuilder();
		url.append(strURL);
		url.append("/doc2xml");
		url.append("?");
		url.append(EFGImportConstants.DATASOURCE_NAME);
		url.append("=");
		url.append(datasourceName);
		url.append("&");
		url.append(EFGImportConstants.ALL_TABLE_NAME);
		url.append("=");
		
		if("false".equalsIgnoreCase(glossary)){
			url.append(EFGImportConstants.EFG_RDB_TABLES);
		}
		else{
			url.append(EFGImportConstants.EFG_GLOSSARY_TABLES);
		}
		return url.toString();
	}
	/**
	 * @param strURL
	 */
	private static void checkURL(String strURL) throws Exception {
		URLConnection conn = null;
		URL url = new URL(strURL);
        conn = url.openConnection();
    	if (conn instanceof HttpURLConnection)
        {
            HttpURLConnection httpcon = (HttpURLConnection)conn;
            int code = httpcon.getResponseCode();
            if(code ==HttpURLConnection.HTTP_OK){
            	return;
            }
            else{
            	throw new Exception("Cannot find url: " + strURL);
            }
        }
    }
}
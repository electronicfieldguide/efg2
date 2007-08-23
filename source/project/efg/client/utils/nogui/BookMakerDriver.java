

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
		
		String bookDirectory = "books";
		File file = new File(bookDirectory);
		if(!file.exists()){
			file.mkdirs();
		}
		 if(args[0].equals("-test")){
			args = new String[3];
			
			args[0] = "http://panda.cs.umb.edu/efg2/doc2xml?dataSourceName=bostonnaturecenterefg_1185631295755&ALL_TABLE_NAME=efg_rdb_tables";
			args[1] = "Test BNC Book1";
			args[2] = "BNCBook.pdf";
		}
		 else if(null != args || args.length < 2 || args.length > 3){
			 System.out.println("usage java BookMakerDriver URL templateName [outputFileName]");
			 return;
		}
		String fileName = null;
		if(args.length == 2){
			fileName= bookDirectory + "/PDFBook.pdf";
		}
		else{
			fileName = bookDirectory + "/" + args[2];
		}
		LoggerUtils loggers = new LoggerUtils();
		try {
			String strURL = args[0];
			String templateName = args[1];
			System.out.println("URL: " + strURL);
			System.out.println("Template Name: " + templateName);
			System.out.println("PDF File Name: " + fileName);
			checkURL(strURL);
			BookMakerDriver bookmaker = new BookMakerDriver(new URL(strURL),templateName);
			bookmaker.makePDF(fileName);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
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
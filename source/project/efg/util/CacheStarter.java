/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import project.efg.Imports.efgImportsUtil.LoggerUtils;
import project.efg.servlets.rdb.QueryExecutor;

/**
 * @author kasiedu
 *
 */
public class CacheStarter {
	
	private QueryExecutor queryExecutor;
	private static String defaultServer = "http://antelope.cs.umb.edu:8080/efg2/";
	private static String redirectString = "Redirect.jsp?displayName=";
	private static String displayFormat = "&displayFormat=HTML";
	private static String XSLNAME = "&xslName=";
	private String serverInfo = defaultServer;
	static Logger log = null;

	private HttpClient client;

	static {
		try {
			log = Logger.getLogger(CacheStarter.class);
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public CacheStarter() {
		this.queryExecutor = new QueryExecutor();
		this.client = new HttpClient();
	}
	public void executeCache(String serverInfo){
		if(serverInfo != null && !"".equals(serverInfo.trim())){
			this.serverInfo = serverInfo;
		}
		this.serverInfo = this.serverInfo + redirectString; 
		try{
		SqlRowSet rowset = 
			this.queryExecutor.executeQueryForRowSet(buildAllDataSourcesQuery());
		
		while (rowset.next()) {
			String displayName = rowset.getString(EFGImportConstants.DISPLAY_NAME_COL);
			String dsName = rowset.getString(EFGImportConstants.DS_DATA_COL);
			String listsTemplate = rowset.getString(EFGImportConstants.SEARCH_PAGE_LISTS_XSL_COL);
			String platesTemplate = rowset.getString(EFGImportConstants.SEARCH_PAGE_PLATES_XSL_COL);
			String taxaTemplate = rowset.getString(EFGImportConstants.TAXON_PAGE_XSL_COL);
			String[] xslFileNames = new String[3];
			xslFileNames[0] = listsTemplate;
			xslFileNames[1] = platesTemplate;
			xslFileNames[2] = taxaTemplate;
			this.executeURL(displayName,xslFileNames);
			this.executeURLWithDSName(displayName,dsName,xslFileNames);
		}	
		}catch(Exception ee){
			log.error(ee.getMessage());
		}
	}
	private void executeURL(String displayName, 
			String[] xslFileNames){
		StringBuffer queryBuffer = new StringBuffer(this.serverInfo);
		queryBuffer.append(displayName);
		queryBuffer.append(displayFormat);
		String urlconst = queryBuffer.toString();
		for(int i = 0; i < xslFileNames.length;i++){
			String xslname = xslFileNames[i];
			String url = urlconst;
			if(xslname != null){
				url= url  + XSLNAME + xslname;
			}
			log.debug("About to execute query: " + url);
			execute(url);
			log.debug("Done executing query: " + url);
		}
	}
	private void executeURLWithDSName(String display, String dsNames,
			String[] xslFileNames){
		
	}
	private void execute(String url){
		 
			    // Create an instance of HttpClient.
			    
			    
			    // Create a method instance.
			    GetMethod method = new GetMethod(url);
			    
			    // Provide custom retry handler is necessary
			    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    		new DefaultHttpMethodRetryHandler(1, false));

			    try {
			      // Execute the method.
			      int statusCode = this.client.executeMethod(method);

			      if (statusCode != HttpStatus.SC_OK) {
			    	  log.debug("Method failed: " + method.getStatusLine());
			      }
			      else{
			    	  log.debug("Method returned OK");
			      }

			      // Read the response body.
			      method.getResponseBody();

			      // Deal with the response.
			      // Use caution: ensure correct character encoding and is not binary data
			    log.debug("Done processing url");
			    method.releaseConnection();
			    log.debug("method released");
			    } catch (Exception e) {
			    	if(method != null){
			    	method.releaseConnection();
			    	}
			    	log.error(e.getMessage());
			    } finally {
			    	if(method != null){
				    	method.releaseConnection();
				    	}
			    }  
			 
		
	}
	private String buildAllDataSourcesQuery(){
		String efgRDBTable = EFGImportConstants.EFGProperties
		.getProperty("ALL_EFG_RDB_TABLES");
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT * FROM ");
		queryBuffer.append(efgRDBTable);
		
		return queryBuffer.toString();
		
	}
	public static String getURLProperty(){
		try{
			File file1 = new File(".");
			log.debug("Abs: " + file1.getAbsolutePath());
			InputStream input = 
				project.efg.util.CacheStarter.class.getResourceAsStream("/properties/cacheURLS.properties");
		
		
		
					Properties properties = new Properties();
					properties.load(input);
					if(input != null){
						input.close();
					}
					return properties.getProperty("urls");
				
		}
		catch(Exception ee){
			ee.printStackTrace();
			log.error(ee.getMessage());
		}
		return null;
	}
	public static void main(String[] args) throws Exception{
		  String[] urls = 
		    {
		       "http://antelope.cs.umb.edu:8080/efg2/SearchPageNantucketInvasives.jsp?displayName=invasives",
		       "http://antelope.cs.umb.edu:8080/efg2/Redirect.jsp?displayName=invasives&displayFormat=HTML&xslName=NantucketListsTemplate_CommonName.xsl",
			   "http://antelope.cs.umb.edu:8080/efg2/Redirect.jsp?displayName=invasives&displayFormat=HTML&xslName=NantucketListsTemplate_ScientificName.xsl"};
		    
		LoggerUtils utils = new LoggerUtils();
		utils.toString();
		/*String url = CacheStarter.getURLProperty();
	    String []urls = null;
	    if(url == null){
	    	urls = defaulturls;
	    }
	    else{
	    	urls = url.split(EFGImportConstants.COMMASEP);
	    }*/
	    
       //		 Create an instance of HttpClient.
	   HttpClient client = new HttpClient();
	
	  
	    // Create a method instance.
	    for(int i = 0; i < urls.length;i++){
	    	GetMethod method = new GetMethod(urls[i]);
	    
	    // Provide custom retry handler is necessary
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler(1, false));

	    try {
	      // Execute the method.
	      int statusCode = client.executeMethod(method);

	      if (statusCode != HttpStatus.SC_OK) {
	    	 log.debug("Method failed: " + method.getStatusLine());
	      }
	      else{
	    	  log.debug("Method returned OK");
	      }

	      // Read the response body.
	      method.getResponseBody();

	      // Deal with the response.
	      // Use caution: ensure correct character encoding and is not binary data
	     log.debug("At end");

	    } catch (Exception e) {
	    	log.debug(e.getMessage());
	    } finally {
	      // Release the connection.
	      method.releaseConnection();
	      log.debug("Done");
	    }  
	    }	
	}
}

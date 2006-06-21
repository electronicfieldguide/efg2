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

import project.efg.servlets.rdb.QueryExecutor;

/**
 * @author kasiedu
 *
 */
public class CacheStarter {
    static Logger log = null;
    static {
	try {
	    log = Logger.getLogger(CacheStarter.class);
	} catch (Exception ee) {
	}
    }
    /**
     * 
     */
    public CacheStarter() {	}
    
    public static void executeCacheQueries(String[] urls){
	HttpClient client = new HttpClient();
	// Create a method instance.
	for(int i = 0; i < urls.length;i++){
	    System.out.println("About to execute: "  + urls[i]);
	    GetMethod method = new GetMethod(urls[i]);
	    
	    // Provide custom retry handler is necessary
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					    new DefaultHttpMethodRetryHandler(1, false));
	    
	    try {
		// Execute the method.
		int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
		    System.out.println("Method failed: " + method.getStatusLine());
		}
		else{
		    System.out.println("Method returned OK");
		}
		
		// Read the response body.
		method.getResponseBody();
		
		// Deal with the response.
		// Use caution: ensure correct character encoding and is not binary data
		System.out.println("At end");
		
	    } catch (Exception e) {
		System.out.println(e.getMessage());
	    } finally {
		// Release the connection.
		method.releaseConnection();
		System.out.println("Done");
	    }  
	}	
    }
    public static String[] getURLs(){
	try{
	    File file1 = new File(".");
	    log.debug("Abs: " + file1.getAbsolutePath());
	    InputStream input = 
		project.efg.util.CacheStarter.class.getResourceAsStream("/properties/cachedURLS.properties");
	    
	    Properties properties = new Properties();
	    properties.load(input);
	    if(input != null){
		input.close();
	    }
	    String urls = properties.getProperty("urls");
	    return urls.split(",");
	    
	}
	catch(Exception ee){
	    ee.printStackTrace();
	    log.error(ee.getMessage());
	}
	return null;
    }
    public static void main(String[] args){
	try{
	    String[] urls = CacheStarter.getURLs();
	    if((urls != null) && (urls.length > 0)){
		CacheStarter.executeCacheQueries(urls);
	    }
	    else{
		System.out.println("Application could not read properties file holding urls");
	    }
	}
	catch(Exception ee){
	    System.err.println(ee.getMessage());
	}
    }
}

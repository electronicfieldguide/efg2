/**
 * 
 */
package project.efg.util;


import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


/**
 * @author kasiedu
 *
 */
public class CacheStarter {
 
    /**
     * 
     */
    public CacheStarter() {	}
    
    public static void executeCacheQueries(String[] urls){
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
		
	    } finally {
		// Release the connection.
		method.releaseConnection();
		
	    }  
	}	
    }
    public static String[] getURLs(){
	try{
	    
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
	   
	}
	return null;
    }
    public static void main(String[] args){
	try{
	    String[] urls = CacheStarter.getURLs();
	    if((urls != null) && (urls.length > 0)){
		CacheStarter.executeCacheQueries(urls);
	    }
	   
	}
	catch(Exception ee){
	    System.err.println(ee.getMessage());
	}
    }
}

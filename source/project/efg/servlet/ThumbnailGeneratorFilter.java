/**
 *$Id$
 * ThumbnailGeneratorFilter.java
 * Copyright (c) 2005  University of Massachusetts Boston
 *
 * Created: Thu Aug 11 13:51:36 2005
 *
 * @author <a href="mailto:">Jacob Kwabena Asiedu</a>
 * @version 1.0
 */
package project.efg.servlet;
import project.efg.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
// Implement Filter interface

// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ThumbnailGeneratorFilter implements javax.servlet.Filter{
    
    // FilterConfig object for class reference
    private FilterConfig config = null;
    protected static org.apache.log4j.Logger log; 
    
    public ThumbnailGeneratorFilter() {
    } // ThumbnailGeneratorFilter constructor

    // Override initialization method in Filter interface
    public void init( FilterConfig config ) {
	this.config = config;
	try{
	    log= Logger.getLogger(project.efg.servlet.ThumbnailGeneratorFilter.class); 
	}
	catch(Exception ee){
	    
	}
    }
    // Override the operational method of the Filter interface
    public void doFilter( ServletRequest req, ServletResponse res,
			  FilterChain chain) throws IOException,
						    ServletException {
	
	// Keep the filter moving
	chain.doFilter( req, res );
    }
    // Clean up on demand
    protected void clean() {
	//get the current servlet context
       	
	String pathToServlet = EFGServletUtils.getPathToServlet();
		
	// Retrieve directory path and get full list of files
	File images = new File(pathToServlet);
	String thmDir = EFGImportConstants.EFGIMAGES_THUMBS;
	File[] list = images.listFiles();
	for( int f = 0; f < list.length; f++ ) {
	    if(list[f].getName().equals(thmDir)){
		System.out.println("Deleting thumbnails directory: " + thmDir);
		this.deleteDir(list[f]);
	    }
	}
    }
    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
	// The directory is now empty so delete it
        return dir.delete();
    }
    // Remove all image thumbnails on undeploy or container shutdown
    public void destroy() {
	clean();
    }
} // ThumbnailGeneratorFilter

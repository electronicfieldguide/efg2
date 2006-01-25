/**
 *$Id$
 * ThumbnailGenerator.java
 * Copyright (c) 2005  University of Massachusetts Boston
 *
 * Created: Thu Aug 11 13:51:36 2005
 *
 * @author <a href="mailto:">Jacob Kwabena Asiedu</a>
 * @version 1.0
 */
package project.efg.util;
import project.efg.servlet.*;
import java.io.*;


// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ThumbNailGeneratorWrapper{
    
    
    protected static org.apache.log4j.Logger log; 
    static {
	try{
	    log= Logger.getLogger(project.efg.util.ThumbNailGeneratorWrapper.class); 
	}
	catch(Exception ee){
	    
	}   
    } 
    public ThumbNailGeneratorWrapper() {
    } 

    /**
     *@param uri - The uri of the current request excluding the context path
     */
    public boolean startGeneration(String srcImagesDir, String destImagesDir, String fileName){
	//get the maximum dimension
	String maxDimension = EFGServletUtils.getImagesMaxDim();//a hack make it per datasource
	if((maxDimension == null) || (maxDimension.trim().equals(""))){
	    maxDimension = EFGImportConstants.DEFAULT_MAX_DIM;
	}
	int maxDim = Integer.parseInt(maxDimension);
	
	return this.generate(srcImagesDir, destImagesDir,fileName,maxDim);
    }
    /**
     * Adapted from Filter code @
     *@param srcImagesDir - The full path to where all images reside
     *@param destImagesDir - Where the transformed images will be placed
     *@param srcFileName - The name of the urrent image file
     *@param maxDim - The maximum dimension for the thumnail image
     */
    private boolean generate(String srcImagesDir, 
			     String destImagesDir,
			     String srcFileName,
			     int maxDim){
	File imagesOut = new File(destImagesDir);
	//make directories if they do not alrady exists
	if(!imagesOut.isDirectory()){
	    imagesOut.mkdirs();
	}
	
	ThumbNailGenerator thm = new ThumbNailGenerator();
	
	// Don't duplicate work if thumbnail version already exists
	
	//find out if the src exists
	//if it does then find out if it has not changed from the last time it was cached
	//if the maxdim value has not changed from the last value
	//if the date on the thumbnail is earlier than the date on the original file
	//then re generate
	//continue

	
	
	if(!check(destImagesDir +  srcFileName)){
	    //if the properties file has not changed since the last cached value
	    //continue
	    // First verify that the original image exists
	    if(check(srcImagesDir + srcFileName)){
		thm.generateThumbNail(
				      srcImagesDir, 
				      destImagesDir, 
				      srcFileName,
				      maxDim
				      );
	    }
	}
	return true;
	
    }
 
    // Check for existance of image
    protected boolean check( String path ) {
	File checker = new File( path );
	return checker.exists();
    }

}

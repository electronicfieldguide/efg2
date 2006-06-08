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

import java.io.File;

import org.apache.log4j.Logger;

//cache on hard disk

public class ThumbNailGeneratorWrapper implements EFGImportConstants{
	
	protected static org.apache.log4j.Logger log;
	static {
		try {
			log = Logger
			.getLogger(ThumbNailGeneratorWrapper.class);
		} catch (Exception ee) {
			
		}
	}
	private ThumbNailGenerator thm;
	public ThumbNailGeneratorWrapper() {
		this.thm = new ThumbNailGenerator();
	}
	
	/**
	 *@param srcImagesDir
	 *@param destImagesDir
	 */
	public boolean startGeneration(String srcImagesDir, String destImagesDir,
			String fileName) {
		//get the maximum dimension
		String maxDimension = EFGProperties.getProperty(IMAGES_MAX_DIM);//a hack make it per datasource
		if ((maxDimension == null) || (maxDimension.trim().equals(""))) {
			maxDimension = DEFAULT_MAX_DIM;
		}
		int maxDim = Integer.parseInt(maxDimension);
		log.debug("SRC: " + srcImagesDir);
		log.debug("DEST: " + destImagesDir);
		log.debug("FILE: " + fileName);
		log.debug("Max Dim: " + maxDim);
		return this.generate(srcImagesDir, destImagesDir, fileName, maxDim);
	}
	
	// Check for existance of image
	protected boolean check(String dir, String path) {
		File checker = new File(dir,path);
		return checker.exists();
	}

	/**
	 * Adapted from Filter code @
	 *@param srcImagesDir - The full path to where all images reside
	 *@param destImagesDir - Where the transformed images will be placed
	 *@param srcFileName - The name of the urrent image file
	 *@param maxDim - The maximum dimension for the thumnail image
	 */
	private boolean generate(String srcImagesDir, String destImagesDir,
			String srcFileName, int maxDim) {
		File imagesOut = new File(destImagesDir);
		//make directories if they do not alrady exists
		if (!imagesOut.isDirectory()) {
			imagesOut.mkdirs();
		}
		
		
		
		// Don't duplicate work if thumbnail version already exists
		
		//find out if the src exists
		//if it does then find out if it has not changed from the last time it was cached
		//if the maxdim value has not changed from the last value
		//if the date on the thumbnail is earlier than the date on the original file
		//then re generate
		//continue
	
			//if the properties file has not changed since the last cached value
			//continue
			// First verify that the original image exists
			if (check(srcImagesDir,srcFileName)) {
				this.thm.generateThumbNail(srcImagesDir, destImagesDir, srcFileName,
						maxDim);
			}
		
		return true;
		
	}
	
}

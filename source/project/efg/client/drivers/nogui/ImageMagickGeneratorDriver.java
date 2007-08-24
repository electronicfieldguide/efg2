/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2007  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */
package project.efg.client.drivers.nogui;

import java.io.File;


import project.efg.client.factory.nogui.SpringNoGUIFactory;
import project.efg.client.interfaces.nogui.ThumbNailGeneratorInterface;



/**
 * @author kasiedu
 *
 */
public class ImageMagickGeneratorDriver {
	private ThumbNailGeneratorInterface thm;
	public ImageMagickGeneratorDriver() {
	
		this.init();
	}
	private void init(){
		this.thm = this.doSpring();
	}
	/**
	 * @return
	 */
	private ThumbNailGeneratorInterface doSpring() {
		return SpringNoGUIFactory.getThumbNailGenerator();
	}

	/**
	 * Adapted from Filter code @
	 * @param srcImagesDir -
	 *            The full path to where all images reside
	 * @param destImagesDir -
	 *            Where the transformed images will be placed
	 * @param srcFileName -
	 *            The name of the current image file
	 * @param maxDim -
	 *            The maximum dimension for the thumnail image
	 */
	private boolean generate(String srcDir, String destDir, String fileName) {
		if (check(srcDir, fileName)) {
			//use a factory here
			return this.thm.generateThumbNail(srcDir, destDir,fileName, 120);
		}
		return false;
	}
	// Check for existence of image
	private  boolean check(String dir, String fileName) {
		File checker = new File(dir,fileName);
		return checker.exists();
	}

private  void generateThumbs(File srcFile, File destFile){
	
	
	try {
		
		
			 if(srcFile.isDirectory()){	
				// destFile.mkdirs();
				 destFile = new File(destFile,srcFile.getName()); 
				 File[] list = srcFile.listFiles();
				
				 for(int i = 0; i < list.length; i++){
					 File file = list[i];
					
					generateThumbs(file,destFile);
				 }
			 }
			 else{
				 String srcDir = srcFile.getParent();
				 String fileName = srcFile.getName();
				 
				 destFile = new File(destFile,srcFile.getName()); 
				 String destDir = destFile.getParent();

				
				
				 try{
					generate(srcDir,destDir,fileName);
					
				 }
				 catch(Exception ee){
					// ee.printStackTrace();
				 }
				
			 }
	}
	catch (Exception e) {
	
	 
		
	} 
}
public static void main(String[] args) {
	String srcDir = "c:\\srcImages";
	
	String outDir = "c:\\outImages";
	try {
		ImageMagickGeneratorDriver driver = new ImageMagickGeneratorDriver();
		driver.generateThumbs(new File(srcDir),new File(outDir));
	}catch (Exception e) {
		
		e.printStackTrace();
	}
}
}

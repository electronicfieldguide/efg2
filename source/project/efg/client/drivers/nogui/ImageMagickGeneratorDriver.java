/**
 * 
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

package project.efg.util;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ThumbNailGenerator {
   
 static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(ThumbNailGenerator.class); 
	}
	catch(Exception ee){
	}
    }
    public ThumbNailGenerator(){}

    /**
     *@param fullPath - The full path to a file
     *@param imageScr - The name of the source file
     *@return the full path to the source file
     */
    private  String getNewFileName(String fullPath, 
					 String imageSrc){
    	File directories = new File(fullPath);
    	if(!directories.exists()){
    	    directories.mkdirs();
    	}
    	return fullPath + File.separator + imageSrc;
    }
  
    /**
     *@param src - The full path to src file .
     *@param dest - The full path to destination file.

     *@param maxDim - The maximum length of the dimensions for the new Image 
    */
    public boolean generateThumbNail(String src,
				     String destSrc,String fileName,
				     int maxDim
				     ){
	BufferedOutputStream out = null;
	boolean done = true;
	
	try{
	  
	    File orig_file = new File(src,fileName);  
	    
	    if(!orig_file.exists()){
	    	return false;
	    }
	  
	    if(orig_file.isDirectory()){
	    	return false;
	    }
	    String newImageName = getNewFileName(destSrc,fileName);
	    String originalImageName = orig_file.getAbsolutePath();
	  
	    int thumbWidth = 0;
		int thumbHeight = 0;
		int quality = 100;
		
		Image image = Toolkit.getDefaultToolkit().getImage(originalImageName);
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);
		mediaTracker.waitForID(0);
		
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		if((imageWidth <= 0) || (imageHeight <=0)){
			log.error(src + File.separator + fileName + " is not a recognized image file");
			return false;
		}
		double aspectRatio = imageWidth/imageHeight;
		boolean scaleByWidth= false;
		if(aspectRatio > 0){
		    scaleByWidth = true;
		}
		
		double imageRatio =0.0;
		
		if(((scaleByWidth) && (imageWidth <= maxDim)) || ((!scaleByWidth) &&(imageHeight <= maxDim))){//maintain size
		    thumbWidth = imageWidth;
		    thumbHeight = imageHeight;
		}
		else if(scaleByWidth){ 
		    imageRatio = (double)maxDim/(double)imageWidth;
		    thumbWidth = (int)(imageRatio * imageWidth);
		    thumbHeight = (int)(imageRatio * imageHeight);
		}
		else{
		    imageRatio = (double)maxDim/(double)imageHeight;
		    thumbHeight = (int)(imageRatio * imageHeight);
		    thumbWidth = (int)(imageRatio * imageWidth);
		}
		
		//draw original image to thumbnail image object and
		// scale it to the new size on-the-fly
		BufferedImage thumbImage = new BufferedImage(thumbWidth, 
							     thumbHeight, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
		// save thumbnail image to OUTFILE
	    
		out = new BufferedOutputStream(new
					       FileOutputStream(newImageName));
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		
		param.setQuality((float)quality/100.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(thumbImage);
		out.flush();
		out.close();
		log.debug("Thumbnail : " + newImageName + "successfully created");
		done = true;
		
		
		graphics2D.dispose();
		thumbImage.flush();
		image.flush();
		image = null;
		thumbImage = null;
		
	    
	}
	catch(Exception ee){
	    done = false;
	    log.error(ee.getMessage());
	  
	}
	finally{
	    try{
		if(out != null){
		    out.close();
		}
	    }catch(Exception e){
	
	    }
	}
	return done;
    }
}
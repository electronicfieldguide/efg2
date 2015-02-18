package project.efg.client.utils.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import project.efg.client.interfaces.nogui.ThumbNailGeneratorInterface;

public class ThumbNailGenerator extends ThumbNailGeneratorInterface {
   
 static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(ThumbNailGenerator.class); 
	}
	catch(Exception ee){
	}
    }
    public ThumbNailGenerator(){
    	
    }

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
  
    /* (non-Javadoc)
	 * @see project.efg.util.ThumbnailGeneratorInterface#generateThumbNail(java.lang.String, java.lang.String, java.lang.String, int)
	 */
    public boolean generateThumbNail(String src,
		     String destSrc,String fileName,
		     int maxDim){
    	
    	return this.useDefault(src, destSrc, fileName, maxDim);
   }
    private boolean useDefault(String src,
		     String destSrc,String fileName,
		     int maxDim) {
    	   BufferedImage image = null;
    	   String newImageName = getNewFileName(destSrc,fileName);
    		try {
    			 int thumbWidth = 0;
    			int thumbHeight = 0;
    			image = ImageIO.read(new File(src,fileName));
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
    	       BufferedImage scaled = new BufferedImage( thumbWidth, thumbHeight,
    	      		 BufferedImage.TYPE_INT_RGB);
    	      		             Graphics g = scaled.getGraphics();
    	      		             g.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
    	      		             g.dispose();
    	      		             ImageIO.write(scaled, "jpg", new File(newImageName));
    	      		            
    	      		             return true;
    		} catch (Exception e) {
    			log.error(e.getMessage());
    		}
    	   	return false;	
    }
}
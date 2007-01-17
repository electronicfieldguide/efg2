/**
 * 
 */
package project.efg.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;


public class EFGJLabel extends JLabel {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGJLabel.class);
		} catch (Exception ee) {
		}
	}
	
	private String path;
	private String text="";

	public EFGJLabel() {
		super();
	}

	public EFGJLabel(String text) {
		super(text,SwingConstants.CENTER);
	}

	public EFGJLabel(Icon image) {
		super(image);
	}

	public EFGJLabel(String text, Icon image, int horizontalAlignment) {
		super(text, image, horizontalAlignment);
	}

	public EFGJLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}
	public void setEFGJLabel(String path) {
		this.path = path;
	}

	protected void paintComponent(Graphics g) {
		if (this.path != null) {
			
			this.createThumbnail(g);
			
		} else {
			super.paintComponent(g);
		}
	}
	
	
	private BufferedImage createThumbnail(Graphics g) {
		BufferedImage outImage = null;
		try {
			if(path == null){//not an image
				return outImage;
			}
			if(new File(path).isDirectory()){//not an image
				return outImage;
			}
			File imageFile = new File(this.path);
			BufferedImage image = javax.imageio.ImageIO.read(imageFile);
			StringBuffer buffer = 
				new StringBuffer();
			ImageInfo imageInfo = new ImageInfo();
			buffer.append(ImageInfo.getFileInfoHtml(imageFile,imageInfo));
			//buffer.append();
			this.setText(buffer.toString());
			// Create an image buffer in 
			//which to paint on.
			outImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			// Set the scale.
			AffineTransform tx = new AffineTransform();
		
			// If the image is smaller than 
			//the desired image size,
			// don't bother scaling.
		
			// Paint image.
			Graphics2D g2d = (Graphics2D)g;
			//g2d.drawImage(image,null, (int)rect.getCenterX()-60,(int)rect.getCenterY());
			g2d.drawImage(image,null, 60,60);
			String tt = this.getText();
			BufferedReader reader = new BufferedReader(new StringReader(tt));
			String s = null;
			int i = 15;
			while( (s = reader.readLine()) != null) {
				g2d.drawString(s,60,60 + image.getHeight(null) + i);
				i = i + 15;
			}
			
			
			g2d.dispose();
			image.flush();
			
		} catch (Exception e) {
			log.error(this.path + " is not an image");
		}
		return outImage;
	}
}

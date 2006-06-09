/**
 * 
 */
package project.efg.Imports.efgImpl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JLabel;

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

	

	int maxDim;

	

	public EFGJLabel() {
		super();
	}

	public EFGJLabel(String text) {
		super(text);
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
	public void setEFGJLabel(String path, int maxDim) {
		this.path = path;
		this.maxDim = maxDim;
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
			BufferedImage image = javax.imageio.ImageIO.read(new File(this.path));
			// Determine the scale.
			double scale = (double) maxDim
					/ (double) image.getHeight(null);
			if (image.getWidth(null) > image.getHeight(null)) {
				scale = (double) this.maxDim / (double)image.getWidth(null);
			}
			// Determine size of new image. 
			//One of them
			// should equal maxDim.
			int scaledW = (int) (scale * image.getWidth(null));
			int scaledH = (int) (scale * image.getHeight(null));
			// Create an image buffer in 
			//which to paint on.
			outImage = new BufferedImage(scaledW, scaledH,
					BufferedImage.TYPE_INT_RGB);
			// Set the scale.
			AffineTransform tx = new AffineTransform();
			// If the image is smaller than 
			//the desired image size,
			// don't bother scaling.
			if (scale < 1.0d) {
				tx.scale(scale, scale);
			}
			// Paint image.
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(image, tx, null);
			g2d.dispose();
		} catch (Exception e) {
			log.error(this.path + " is not an image");
		}
		return outImage;
	}
}

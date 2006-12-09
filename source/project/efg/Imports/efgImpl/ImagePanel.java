/**
 * 
 */
package project.efg.Imports.efgImpl;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import project.efg.util.EFGImportConstants;



/**
 * @author kasiedu
 *
 */
public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image = null;
	private int iWidth2;
	private int iHeight2;

	public ImagePanel()
	{
		super();
		initImage();
		
	}
	private void initImage(){
		URL url = null;
	
		try {
			url = this.getClass().getResource(EFGImportConstants.KEY_DROP_BACKGROUND_IMAGE);
			this.image = new ImageIcon(url).getImage();
			
			this.iWidth2 = image.getWidth(this)/2;
			this.iHeight2 = image.getHeight(this)/2;
			
		} catch (Exception ee) {
		  ee.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (image != null)
		{
			int x = this.getParent().getWidth()/2 - iWidth2;
			int y = this.getParent().getHeight()/2 - iHeight2;
			g.drawImage(image,x,y,this);
		}
	}
}

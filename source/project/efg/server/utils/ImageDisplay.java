/**
 * $Id: ImageDisplay.java,v 1.1.1.1 2007/08/01 19:11:25 kasiedu Exp $
 */
package project.efg.server.utils;

/**
 * @author kasiedu
 * 
 */
public class ImageDisplay implements Comparable {
	private String imageName = "";

	private String imageCaption = "";

	public ImageDisplay() {

	}

	public void setImageName(String imageName) {
		if(imageName != null){
			this.imageName = imageName.trim();
		}
	}

	public void setImageCaption(String imageCaption) {
		if(imageCaption != null){
			this.imageCaption = imageCaption.trim();
		}
	}

	public String getImageCaption() {
		return this.imageCaption;
	}

	public String getImageName() {
		return this.imageName;
	}

	public boolean equals(Object obj) {
		ImageDisplay display = (ImageDisplay) obj;
		return this.getImageCaption().equalsIgnoreCase(
				display.getImageCaption())
				&& this.getImageName().equalsIgnoreCase(display.getImageName());
	}

	public int hashCode() {
		return this.getImageCaption().hashCode()
				* this.getImageName().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj) {
		ImageDisplay obj1 = (ImageDisplay)obj;
		if(this.getImageName().equals(obj1.getImageName())){
			return this.getImageCaption().compareTo(obj1.getImageCaption());
		}
		return this.getImageName().compareTo(obj1.getImageName());
	}

}

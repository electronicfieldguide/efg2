/**
 * $Id: EFGPageDisplay.java,v 1.1.1.1 2007/08/01 19:11:25 kasiedu Exp $
 */
package project.efg.server.utils;


/**
 * @author kasiedu
 * 
 */
public class EFGPageDisplay implements Comparable {
	private String imageName = "";

	private String imageCaption = "";
	private String itemName ="";

	private String uniqueID="";
	
	public EFGPageDisplay() {

	}
	
	public void setDisplay(String uniqueID, String itemName, ImageDisplay imageDispay){
		this.setUniqueID(uniqueID);
		this.setItemName(itemName);
		this.setImageCaption(imageDispay.getImageCaption());
		this.setImageName(imageDispay.getImageName());
	}
	public ImageDisplay getImageDisplay(){
		ImageDisplay display = new ImageDisplay();
		display.setImageCaption(this.getImageCaption());
		display.setImageName(this.getImageName());
		return display;
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
	public void setItemName(String imageCaption) {
		if(imageCaption != null){
			this.itemName = imageCaption.trim();
		}
	}

	public String getImageCaption() {
		return this.imageCaption;
	}

	public String getImageName() {
		return this.imageName;
	}
	public String getItemName() {
		return this.itemName;
	}

	public boolean equals(Object obj) {
		EFGPageDisplay display = (EFGPageDisplay) obj;
		
		return this.getUniqueID().equals(display.getUniqueID());
	}

	public int hashCode() {
		return this.getUniqueID().hashCode();
	}

	/**
	 * If both this and obj have captions compare them.
	 * if this has caption but obj has no cpation but item compare caption to item
	 * and viceversa
	 * if this has caption but obj has no caption or item return this
	 * and vice versa
	 * if this has no caption and obj has no caption compare names
	 * otherwise compare the image names
	 */
	public int compareTo(Object obj) {
		EFGPageDisplay obj1 = (EFGPageDisplay)obj; 
		
		//both have captions
		if((!this.getImageCaption().trim().equalsIgnoreCase("")) && 
				(!obj1.getImageCaption().trim().equalsIgnoreCase(""))){
			return this.getImageCaption().compareTo(obj1.getImageCaption());
		}
		//this has caption
		if((!this.getImageCaption().trim().equalsIgnoreCase("")) && 
				(obj1.getImageCaption().trim().equalsIgnoreCase(""))){
			
			return this.getImageCaption().compareTo(obj1.getItemName());
		}
		//obj1 has caption
		if((!obj1.getImageCaption().trim().equalsIgnoreCase("")) && 
				(this.getImageCaption().trim().equalsIgnoreCase(""))){
			
			return this.getItemName().compareTo(obj1.getImageCaption());
		}
		if((obj1.getImageCaption().trim().equalsIgnoreCase("")) && 
				(this.getImageCaption().trim().equalsIgnoreCase(""))){
			if((obj1.getItemName().trim().equalsIgnoreCase("")) && 
					(this.getItemName().trim().equalsIgnoreCase(""))){
				return this.getImageName().compareTo(obj1.getImageName());
			}
			return this.getItemName().compareTo(obj1.getItemName());
		}
		return this.getImageCaption().compareTo(obj1.getImageCaption());
		
	}
	/**
	 * @param id
	 */
	public void setUniqueID(String id) {
		this.uniqueID = id;
		
	}
	/**
	 * @return
	 */
	public String getUniqueID() {
		return this.uniqueID;
	}
}
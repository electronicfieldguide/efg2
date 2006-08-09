/**
 * 
 */
package project.efg.util;

/**
 * @author kasiedu
 *
 * Holds the name of a searchable field and a mediaresource field normally 
 * used by default search temmplates 
 */
public class EFGMediaResourceSearchableObject {
	private String mediaResourceField="";
	private String searchableField="";
	/**
	 * 
	 */
	public EFGMediaResourceSearchableObject() {
		
		
	}
	/**
	 * @return Returns the mediaResourceField.
	 */
	public String getMediaResourceField() {
		return mediaResourceField;
	}
	/**
	 * @param mediaResourceField The mediaResourceField to set.
	 */
	public void setMediaResourceField(String mediaResourceField) {
		if(mediaResourceField != null){
		this.mediaResourceField = mediaResourceField;
		}
	}
	/**
	 * @return Returns the searchableField.
	 */
	public String getSearchableField() {
		return searchableField;
	}
	/**
	 * @param searchableField The searchableField to set.
	 */
	public void setSearchableField(String searchableField) {
		if(searchableField != null){
			this.searchableField = searchableField;
		}
	}
	public boolean equals(Object obj){
		EFGMediaResourceSearchableObject medO = 
			(EFGMediaResourceSearchableObject)obj;
		return this.getMediaResourceField().equalsIgnoreCase(medO.getMediaResourceField())
		&& this.getSearchableField().equalsIgnoreCase(medO.getSearchableField());
	}
	public int hashCode(){
		return this.getMediaResourceField().hashCode() * 10000 * this.getSearchableField().hashCode();
	}

}

/**
 * 
 */
package project.efg.util;



/**
 * @author kasiedu
 *
 */
public class GuidObject  implements Comparable {
	private String guid,displayName,jspName;

	public GuidObject(String guid,String displayName,String jspName){
		this.guid = guid;
		this.displayName = displayName;
		this.jspName = jspName;
	}
	public String getGuid(){
		return this.guid;
	}
	public String getJSPName(){
		return this.jspName;
	}
	public String getDisplayName(){
		return this.displayName;
	}
	public boolean equals(Object obj){
		GuidObject guid = (GuidObject)obj;
		return this.getGuid().equals(guid.getGuid()) && 
		this.getDisplayName().equalsIgnoreCase(guid.getDisplayName());
	}
	public int hashCode(){
		return this.guid.hashCode();
	}
	public int compareTo(Object obj) {
		GuidObject guid = (GuidObject)obj;
		return this.getDisplayName().compareTo(guid.getDisplayName());
	}
	
}

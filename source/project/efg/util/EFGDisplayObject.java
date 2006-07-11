/**
 * 
 */
package project.efg.util;

/**
 * @author kasiedu
 *
 */
public class EFGDisplayObject implements java.lang.Comparable{
	
	private String displayName;
	private String datasourceName;
	public EFGDisplayObject(){
		
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	public void setDatasourceName(String datasourceName){
		this.datasourceName = datasourceName;
	}
	public String getDisplayName(){
		return this.displayName;
	}
	public String getDatasourceName(){
		return this.datasourceName;
	}
	public boolean equals(Object obj){
		EFGDisplayObject obj1 = (EFGDisplayObject)obj;
		if(this.getDisplayName().equals(obj1.getDisplayName())){
			return this.getDatasourceName().equals(obj1.getDatasourceName());
		}
		return this.getDisplayName().equals(obj1.getDisplayName());
	}
	public int hashCode(){
		return this.getDatasourceName().hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj) {
		EFGDisplayObject obj1 = (EFGDisplayObject)obj;
		if(this.getDisplayName().equals(obj1.getDisplayName())){
			return this.getDatasourceName().compareTo(obj1.getDatasourceName());
		}
		return this.getDisplayName().compareTo(obj1.getDisplayName());
	}
	
}

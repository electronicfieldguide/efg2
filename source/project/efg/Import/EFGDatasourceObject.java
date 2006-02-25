/**
 * EFGDatasourceObjectInterface.java
 *
 *
 * Created: Sun Feb 19 09:22:49 2006
 *
 * @author <a href="mailto:">Jacob Admin</a>
 * @version 1.0
 */
package project.efg.Import;
import project.efg.efgInterface.*;

public class EFGDatasourceObject implements  EFGDatasourceObjectInterface {
    protected String displayName;
    protected String metadataName;
    protected String dataName;
    
    public EFGDatasourceObject(){
	this("","","");
    }
    public EFGDatasourceObject(String dataName, 
			       String metadataName, 
			       String displayName){
	this.setDataName(dataName);
	this.setMetadataName(metadataName);
	this.setDisplayName(displayName);
    }
    /**
     *@param the name of the metadatasource without the file extension
     */
    public void setMetadataName(String metadataName){
	this.metadataName = metadataName.trim();
    }
    /**
     *@return the name of the metadatasource without the file extension
     */
    public String getMetadataName(){
	return this.metadataName;
    }
    /**
     *@param the name of the datasource without the file extension
     */
    public void setDataName(String dataName){
	this.dataName = dataName.trim();
    }
    /**
     *@return the name of the datasource without the file extension
     */
    public String getDataName(){
	return this.dataName;
    }
    /**
     *@param displayName - A human readable name for this datasource.
     */
    public void setDisplayName(String displayName){
	this.displayName = displayName.trim();
    }
    /**
     *@return a human readbale name for this datasource
     */
    public String getDisplayName(){
	if((this.displayName == null) || 
	   ("".equals(this.displayName.trim()))){
	    this.displayName = this.getDataName();
	}
	return this.displayName;
    }
    public boolean equals(Object obj){
	EFGDatasourceObject eds = (EFGDatasourceObject)obj;
	return (this.getMetadataName().equals(eds.getMetadataName())) && 
	    (this.getDataName().equals(eds.getDataName()));
	    
    }
    public int hashCode(){
	return (this.getMetadataName().hashCode() * this.getDataName().hashCode());
    }
    public String toString(){
	return this.getDisplayName();
    }
}// EFGDatasourceObject

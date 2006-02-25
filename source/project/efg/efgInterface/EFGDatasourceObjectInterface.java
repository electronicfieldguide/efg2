package project.efg.efgInterface;
/**
 * EFGDatasourceObjectInterface.java
 *
 *
 * Created: Sun Feb 19 09:22:49 2006
 *
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K Asiedu</a>
 * @version 1.0
 */
public interface EFGDatasourceObjectInterface {
    /**
     *@param the name of the metadatasource without the file extension
     */
    public void setMetadataName(String metadataName);
    /**
     *@return the name of the metadatasource without the file extension
     */
    public String getMetadataName();
    /**
     *@param the name of the datasource without the file extension
     */
    public void setDataName(String dataName);
    /**
     *@return the name of the datasource without the file extension
     */
    public String getDataName();
    /**
     *@param displayName - A human readable name for this datasource.
     */
    public void setDisplayName(String displayName);
    /**
     *@return a human readbale name for this datasource
     */
    public String getDisplayName();
    /**
     *
     *@return true if this object is equal to the obj
     */
   public boolean equals(Object obj);

    /**
     *Override the hashCode mthod.. Return the hashCode for the object
     */
    public int hashCode();
    /**
     * A toString for this object
     *@return a String representation of this object
     */
    public String toString();
}
/**
 * EFGDatasourceObjectInterfaceList.java
 *
 *
 * Created: Sun Feb 19 09:22:49 2006
 *
 * @author <a href="mailto:kasiedu@ccs.umb.edu">Jacob K. Asiedu</a>
 * @version 1.0
 */
package project.efg.efgInterface;

import java.util.Iterator;

public interface EFGDatasourceObjectListInterface {
    
    /**
     *@return an iterator over the members of this list.
     */
    public Iterator getEFGDatasourceObjectListIterator();
    /**
     *Add an object to the database
     *@param datasource - The datasource object to add 
     *@return true if this datasource was succesfully added, false otherwise
     */
    public boolean addEFGDatasourceObject(EFGDatasourceObjectInterface datasource);
    /**
     * Remove an object from the database
     *@param datasource - The datasource object to add 
     *@return true if this datasource was successfully added, false otherwise
     */    
    public boolean removeEFGDatasourceObject(EFGDatasourceObjectInterface datasource);
    
    /**
     *@return true if list contains obj, false otherwise
     */
    public boolean contains(EFGDatasourceObjectInterface obj);
    /**
     *@retur true if the display name for the object is changed in the list
     *false otherwise
     */
    public boolean changeDisplayName(EFGDatasourceObjectInterface obj);
  
}// EFGDatasourceObjectInterfaceList


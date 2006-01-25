/**
 * EFGConstants.java
 *
 *
 * Created: Thu Aug 18 11:41:30 2005
 *
 * @author <a href="mailto:">Jacob Kwabena Asiedu</a>
 * @version 1.0
 */
package project.efg.efgInterface;

public interface EFGConstants {
    //Using ObjectStore
    int OS_CONSTANT = 1;

    //Using relational database
    int RDB_CONSTANT = 2;
    
    //A property name for the maximum dimension property.
    String MAX_DIM_NAME = "EFG_MAX_DIM";
    
    //A property name for the directory holding EFG images
    String EFG_IMAGES_DIR_NAME = "EFG_IMAGES_DIR";

    //A property name for the directory holding EFG images
    String EFG_THUMB_NAILS_DIR_NAME = "EFG_THUMB_NAILS_DIR";

    //The parent directory where all EFGImages can be found
    String EFG_IMAGES_DIR = "EFGImages";

    //The parent directory where all thumbnail generated images are placed
    String EFG_THUMB_NAILS_DIR = "EFG_ThumbNails_EFG";    

    //READ FROM A PROPERTIES FILE
    String MAX_DIM = "200";

    String XSL_PROPERTIES_FILE="XSLProps.properties";
    
    String EFG_KEYWORDS_PROPERTIES= "queryKeyWords.properties";
}// EFGConstants

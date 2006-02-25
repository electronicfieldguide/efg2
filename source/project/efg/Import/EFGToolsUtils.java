/**
 *
 * Created: Tue Aug 31 09:03:10 2004
 *Part of the EFG tools utilities
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K. Asiedu</a>
 * @version 1.0
 */
package project.efg.Import;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public interface EFGToolsUtils {
    
    // The name of the efg logo image
    String ABOUT_ICON = "efglogo.jpg";

    //The directory holding the efg logo
    String ICONS_DIR = "icons";

   String HELP_DIR = "help";
    
    //The EFG Contact information
    String EFG_CONTACT_MESSAGE = "Contact us: www.cs.umb.edu/efg";
    
    //The application specific copyright Message.
   String COPYRIGHT_MESSAGE = "EFG2 Data Import application Version 1.0 (c) UMASS Boston, 2006";

    //The application specific Message
   String ABOUT_MESSAGE = "About EFG2 Data Import Tool";


    //Java specific information
    String JAVA_MESSAGE = "JVM version "+System.getProperty("java.version") + 
	" by "+System.getProperty("java.vendor");
    
   
} 

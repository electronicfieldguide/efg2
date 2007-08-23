package project.efg.client.utils.gui;


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Vector;



/**
 * @version $Revision: 1.1.1.1 $
 * @author  Benoît Mahé (bmahe@w3.org)
 */
public class TransferableFileNode implements Transferable {

    final static int NODE       = 0;
    final static int FILES      = 1;
    final static int STRING     = 2;
    final static int PLAIN_TEXT = 3;
    
    public final static DataFlavor FILENODE_FLAVOR =
	new DataFlavor(FileNode.class, "File Node");

    static DataFlavor flavors[] = { FILENODE_FLAVOR,
				    DataFlavor.javaFileListFlavor,
				    DataFlavor.stringFlavor};

    protected FileNode data = null;

    /**
     * Returns an array of DataFlavor objects indicating the flavors the
     * data can be provided in. The array should be ordered according to 
     * preference for providing the data (from most richly descriptive to 
     * least descriptive).
     * @return an array of data flavors in which this data can be transferred
     */
    public DataFlavor[] getTransferDataFlavors() {
	return flavors;
    }

    /**
     * Returns whether or not the specified data flavor is supported for 
     * this object.
     * @param flavor the requested flavor for the data 
     * @return boolean indicating wether or not the data flavor is supported
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
	boolean returnValue = false;
	for (int i=0, n=flavors.length; i<n; i++) {
	    if (flavor.equals(flavors[i])) {
		returnValue = true;
		break;
	    }
	}
	return returnValue;
    }

    /**
     * Returns an object which represents the data to be transferred. 
     * The class of the object returned is defined by the representation 
     * class of the flavor.
     * @param flavor the requested flavor for the data
     * @return an object which represents the data to be transferred
     * @exception IOException  if the data is no longer available in the 
     * requested flavor.
     * @exception UnsupportedFlavorException if the requested data flavor 
     * is not supported.
     */
    public Object getTransferData(DataFlavor flavor)
	throws UnsupportedFlavorException, IOException 
    {
	Object returnObject;
	
	if (flavor.equals(flavors[NODE])) {
		
	    returnObject = data;
	} else if (flavor.equals(flavors[FILES])) {
		
	    Vector v = new Vector();
	    v.addElement(data.getFile());
	    returnObject = v;
	} else if (flavor.equals(flavors[STRING])) {
	
	    returnObject = data.getFile().getAbsolutePath();
	} 
	else {
		
	    throw new UnsupportedFlavorException(flavor);
	}
	return returnObject;
    }

    public TransferableFileNode(FileNode node) {
	this.data = node;
    }
    
}

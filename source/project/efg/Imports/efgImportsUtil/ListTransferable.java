package project.efg.Imports.efgImportsUtil;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import project.efg.Imports.efgInterface.TableSorterMainInterface;
 
public class ListTransferable implements Transferable {
    private java.util.List data;
 
    public ListTransferable(java.util.List data) {
        this.data = data;
    }
 
    public ListTransferable(Object[] data) {
        this.data = Arrays.asList(data);
    }
 
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
            IOException {
        if (isDataFlavorSupported(flavor)) {
            return data;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
 
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{TableSorterMainInterface.listFlavor};
    }
 
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(flavor)) {
                return true;
            }
        }
        return false;
    }
}

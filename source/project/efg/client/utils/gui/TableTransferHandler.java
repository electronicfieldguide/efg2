/**
 * 
 */
package project.efg.client.utils.gui;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
/**
 * @author kasiedu
 *
 */
public class TableTransferHandler extends TransferHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] rows;
	private int addIndex;
	private int addCount; 
	/**
	 * 
	 */
	public TableTransferHandler() {
		super();
		
	}
	protected Transferable createTransferable(JComponent c) {
	    return null;
	  }

	 public int getSourceActions(JComponent c) {
         return MOVE;
     }
	 protected String exportData(JComponent c) {
         JTable table = (JTable)c;
         rows = table.getSelectedRows();
         int colCount = table.getColumnCount();
         StringBuffer buff = new StringBuffer();
         for (int i = 0; i < rows.length; i++) {
             for (int j = 0; j < colCount; j++) {
                 Object val = table.getValueAt(rows[i], j);
                 buff.append(val == null ? "" : val.toString());
                 if (j != colCount - 1) {
                     buff.append(",");
                 }
             }
             if (i != rows.length - 1) {
                 buff.append("\n");
             }
         }
         return buff.toString();
     }
     protected void importSelectedData(JComponent c, String str) {
         JTable target = (JTable)c;
         DefaultTableModel model = (DefaultTableModel)target.getModel();
         int index = target.getSelectedRow();
         //Prevent the user from dropping data back on itself.
         //For example, if the user is moving rows #4,#5,#6 and #7 and
         //attempts to insert the rows after row #5, this would
         //be problematic when removing the original rows.
         //So this is not allowed.
         if (rows != null && index >= rows[0] - 1 &&
                 index <= rows[rows.length - 1]) {
             rows = null;
             return;
         }
         int max = model.getRowCount();
         if (index < 0) {
             index = max;
         } else {
             index++;
             if (index > max) {
                 index = max;
             }
         }
         addIndex = index;
         String[] values = str.split("\n");
         addCount = values.length;
        // int colCount = target.getColumnCount();
         for (int i = 0; i < values.length ; i++) {
             model.insertRow(index++, values[i].split(","));
         }
         //If we are moving items around in the same table, we
         //need to adjust the rows accordingly, since those
         //after the insertion point have moved.
         if (rows!= null && addCount > 0) {
             for (int i = 0; i < rows.length; i++) {
                 if (rows[i] > addIndex) {
                     rows[i] += addCount;
                 }
             }
         }
     }
     protected void cleanup(JComponent c, boolean remove) {
         JTable source = (JTable)c;
         if (remove && rows != null) {
             DefaultTableModel model =
                     (DefaultTableModel)source.getModel();
             for (int i = rows.length - 1; i >= 0; i--) {
                 model.removeRow(rows[i]);
             }
         }
         rows = null;
         addCount = 0;
         addIndex = -1;
     }
     public boolean importData(JComponent c, Transferable t) {
         if (canImport(c, t.getTransferDataFlavors())) {
             try {
                 String str = (String)t.getTransferData(DataFlavor.stringFlavor);
                 importSelectedData(c, str);
                 return true;
             } catch (UnsupportedFlavorException ufe) {
             } catch (IOException ioe) {
             }
         }
         return false;
     }
     protected void exportDone(JComponent c, Transferable data, int action) {
         cleanup(c, action == MOVE);
     }
     public boolean canImport(JComponent c, DataFlavor[] flavors) {
         for (int i = 0; i < flavors.length; i++) {
             if (DataFlavor.stringFlavor.equals(flavors[i])) {
                 return true;
             }
         }
         return false;
     }
}

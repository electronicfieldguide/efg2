package project.efg.Imports.efgImportsUtil;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import project.efg.Imports.efgImpl.EFGTableModel;
import project.efg.Imports.efgImpl.TableSorter;
import project.efg.Imports.efgInterface.TableSorterMainInterface;

public class DataObjectTransferHandler extends TransferHandler {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Point dragPoint;
	    protected Point dropPoint;
	    protected Component dragComponent;
	    protected Component dropComponent;
	 
	    public Point getDropPoint() {
	        return dropPoint;
	    }
	 
	    public void setDropPoint(Point dropPoint) {
	        this.dropPoint = dropPoint;
	    }
	 
	    public Component getDragComponent() {
	        return dragComponent;
	    }
	 
	    public void setDragComponent(Component dragComponent) {
	        this.dragComponent = dragComponent;
	    }
	 
	    public Point getDragPoint() {
	        return dragPoint;
	    }
	 
	    public void setDragPoint(Point dragPoint) {
	        this.dragPoint = dragPoint;
	    }
	 
	    public Component getDropComponent() {
	        return dropComponent;
	    }
	 
	    public void setDropComponent(Component dropComponent) {
	        this.dropComponent = dropComponent;
	    }
	 
	    public int getSourceActions(JComponent c) {
	        return DnDConstants.ACTION_MOVE;
	    }
	 
	    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
	        if (comp.isEnabled() && (comp instanceof JTable)) {
	        	JTable table = (JTable)comp;
	        	TableSorter sorter = (TableSorter)table.getModel();
	        	if(sorter.isSorting()){
	        		return false;
	        	}
	            for (int i = 0; i < transferFlavors.length; i++) {
	                if (transferFlavors[i].equals(TableSorterMainInterface.listFlavor)) {
	                    return true;
	                }
	            }
	        }
	        return false;
	    }
	 
	    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
	        setDragComponent(comp);
	        setDragPoint(((MouseEvent) e).getPoint());
	        super.exportAsDrag(comp, e, action);
	    }
	 
	    protected Transferable createTransferable(JComponent c) {
	        Transferable t = null;
	        if (c instanceof JTable) {
	            JTable table = (JTable) c;
	            
	            TableSorter sorter = (TableSorter)table.getModel();
	            if(sorter.isSorting()){
	            	//can't drag with sorting on
	            }
	            EFGTableModel efg = (EFGTableModel)sorter.getTableModel();
	            int[] selection = table.getSelectedRows();
	            Object[] selectedRows = new Object[selection.length];
	            
	            for (int j = 0; j < selection.length; j++) {
	                selectedRows[j] = efg.getRow(selection[j]);
	            }
	            t = new ListTransferable(selectedRows);
	        }
	        return t;
	    }
	 
	    public boolean importData(JComponent comp, Transferable t) {
	        if (canImport(comp, t.getTransferDataFlavors())) {
	            try {
	                if (getDragComponent() != comp) {
	                    List list = (List) t.getTransferData(TableSorterMainInterface.listFlavor);
	                    JTable table = (JTable) comp;
	                    TableSorter sorter = (TableSorter)table.getModel();
	    	            EFGTableModel efg = (EFGTableModel)sorter.getTableModel();
	                    int insertRow;
	                    if (getDropPoint() != null) {
	                        insertRow = table.rowAtPoint(getDropPoint());
	                    } else {
	                        insertRow = table.getSelectedRow();
	                    }
	 
	                    for (int i = 0; i < list.size(); i++) {
	                        efg.addData(insertRow + i, (Object[]) list.get(i));
	                    }
	                    efg.fireTableDataChanged();
	                    sorter.fireTableDataChanged();
	                    
	                    table.getSelectionModel().clearSelection();
	                    table.getSelectionModel().setSelectionInterval(insertRow, insertRow + list.size() - 1);
	                    table.requestFocus();
	                }
	                return true;
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }//if
	        return false;
	    }
	 
	    protected void exportDone(JComponent source, Transferable data, int action) {
	        if (action == DnDConstants.ACTION_MOVE) {
	            try {
	                List list = (List) data.getTransferData(TableSorterMainInterface.listFlavor);
	                JTable table = (JTable) source;
	                TableSorter sorter = (TableSorter)table.getModel();
		            EFGTableModel efg = (EFGTableModel)sorter.getTableModel();
	                if (source != getDropComponent()) {
	                    int index;
	                    for (int i = 0; i < list.size(); i++) {
	                        index =efg.indexOf(list.get(i));
	                        efg.removeData(index);
	                    }
	                    efg.fireTableDataChanged();
	                    sorter.fireTableDataChanged();
	                    
	                } else {
	                    int index=-1;
	                    int insertRow = table.rowAtPoint(getDropPoint());
	                    for (int i = 0; i < list.size(); i++) {
	                        index = efg.indexOf(list.get(i));
	                        insertRow = insertRow + i;
	                        efg.moveData(index, insertRow);
	                        efg.setValueAt(new Integer(insertRow+1),insertRow,efg.getColumnCount() -1);
	                    }
	                    if(index < insertRow){
	                    	 for (int i = 0; i < insertRow; i++) {
	                    		
	                    		 efg.setValueAt(new Integer(i+1),i,efg.getColumnCount() -1);
	                    	 }
	                    }
	                    else if (index == insertRow){
	                    	
	                    }
	                    else{
	                    	 for (int i = insertRow; i < efg.getRowCount(); i++) {
	                    		
	                    		 efg.setValueAt(new Integer(i+1),i,efg.getColumnCount() -1);
	                    	 }
	                    }
	                    //if move was up everything after the last insert sould increase by one..
	                    //otherwise decrease everything by one.
	                    
	                    efg.fireTableDataChanged();
	                    sorter.fireTableDataChanged();
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }//if
	    }
	 
	    protected static DropHandler dropHandler = new DropHandler();
	 
	    public static DropHandler getDropHandler() {
	        return dropHandler;
	    }
	 
	    protected static class DropHandler implements DropTargetListener, Serializable {
	 
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private boolean canImport;
	 
	        private boolean actionSupported(int action) {
	            return (action & (DnDConstants.ACTION_MOVE | DnDConstants.ACTION_LINK)) !=
	                   DnDConstants.ACTION_NONE;
	        }
	 
	        // --- DropTargetListener methods -----------------------------------
	 
	        public void dragEnter(DropTargetDragEvent e) {
	            DataFlavor[] flavors = e.getCurrentDataFlavors();
	 
	            JComponent c = (JComponent) e.getDropTargetContext().getComponent();
	            TransferHandler importer = c.getTransferHandler();
	 
	            if (importer != null && importer.canImport(c, flavors)) {
	                canImport = true;
	            } else {
	                canImport = false;
	            }
	 
	            int dropAction = e.getDropAction();
	 
	            if (canImport && actionSupported(dropAction)) {
	                e.acceptDrag(dropAction);
	            } else {
	                e.rejectDrag();
	            }
	        }
	 
	        public void dragOver(DropTargetDragEvent e) {
	            int dropAction = e.getDropAction();
	 
	            if (canImport && actionSupported(dropAction)) {
	                JTable table = (JTable) e.getDropTargetContext().getComponent();
	                int row = table.rowAtPoint(e.getLocation());
	                table.getSelectionModel().setSelectionInterval(row, row);
	                e.acceptDrag(dropAction);
	            } else {
	                e.rejectDrag();
	            }
	        }
	 
	        public void dragExit(DropTargetEvent e) {
	        }
	 
	        public void drop(DropTargetDropEvent e) {
	            int dropAction = e.getDropAction();
	 
	            JComponent c = (JComponent) e.getDropTargetContext().getComponent();
	            DataObjectTransferHandler importer = (DataObjectTransferHandler) c.getTransferHandler();
	 
	            if (canImport && importer != null && actionSupported(dropAction)) {
	                e.acceptDrop(dropAction);
	 
	                try {
	                    Transferable t = e.getTransferable();
	                    importer.setDropPoint(e.getLocation());
	                    importer.setDropComponent(c);
	                    e.dropComplete(importer.importData(c, t));
	                } catch (RuntimeException re) {
	                    e.dropComplete(false);
	                }
	            } else {
	                e.rejectDrop();
	            }
	        }
	 
	        public void dropActionChanged(DropTargetDragEvent e) {
	            int dropAction = e.getDropAction();
	 
	            if (canImport && actionSupported(dropAction)) {
	                e.acceptDrag(dropAction);
	            } else {
	                e.rejectDrag();
	            }
	        }
	    }
	}


package project.efg.Imports.efgImportsUtil;

import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import project.efg.Imports.efgInterface.TableSorterMainInterface;

public class TableDNDRecognizer extends MouseAdapter implements MouseMotionListener {
	
		 
	    private boolean recognized;
	    protected Point pressedPoint;
	 
	    public void mousePressed(MouseEvent e) {
	        pressedPoint = e.getPoint();
	    }
	 
	    public void mouseDragged(MouseEvent e) {
	        Point p = e.getPoint();
	        
	        if (!recognized && e.isAltDown() &&
	            ((Math.abs(pressedPoint.x - p.x) > 5) ||
	             (Math.abs(pressedPoint.y - p.y) > 5))) {
	            TableSorterMainInterface.isDragged = true;
	            recognized = true;
	            JComponent c = (JComponent) e.getSource();
	            TransferHandler th = c.getTransferHandler();
	            if (th != null) {
	              th.exportAsDrag(c, e,DnDConstants.ACTION_MOVE);
	            }//if
	        }
	    }
	 
	    public void mouseReleased(MouseEvent e) {
	        recognized = false;
	        TableSorterMainInterface.isDragged = false;
	        pressedPoint = null;
	    }

		public void mouseMoved(MouseEvent e) {
			
			
		}
	}


/**
 * 
 */
package project.efg.Imports.efgImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * @author jacob.asiedu
 *
 */
public abstract class DatasourceListener implements ActionListener{
	protected String title;
	protected String errorMessage;
	protected JFrame frame;
	protected DBObject dbObject;
	
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(DatasourceListener.class);
			
		} catch (Exception ee) {
		}
	}

	public DatasourceListener(DBObject dbObject, 
			JFrame frame, String title, String errorMessage) {
		this.dbObject = dbObject;
		this.frame = frame;
		this.title = title;
		this.errorMessage = errorMessage;	
	}

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgImpl.DatasourceListener#getDBObject()
	 */
	public final DBObject getDBObject() {
		return this.dbObject;
	}
	/* (non-Javadoc)
	 * @see project.efg.Imports.efgImpl.DatasourceListener#getFrame()
	 */
	public final JFrame getFrame() {
		return this.frame;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		
		if (this.getDBObject() == null) {
			StringBuffer buffer = new StringBuffer(this.errorMessage + "\n");

			JOptionPane.showMessageDialog(this.frame, buffer.toString(),
						"Error Message", JOptionPane.ERROR_MESSAGE);
			log.error(buffer.toString());
			return;
		}
		String buttonString = e.getActionCommand();
		this.handleInput(buttonString);
		
		
	}

	/**
	 * 
	 */
	protected abstract void handleInput(String someString);

	

}
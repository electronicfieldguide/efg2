/**
 * 
 */
package project.efg.Imports.efgImportsUtil;

/**
 * @author kasiedu
 *
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import project.efg.util.EFGImportConstants;


/**
 * 
 * @author jacob.asiedu
 *
 */
public class ResourceWarning  extends JDialog implements ItemListener, ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ResourceWarning.class);
			
		} catch (Exception ee) {
		}
	}
	private String message;
	
	/**
	 * @param frame - The parent frame of this dialog
	 * @param title - Title of this dialog
	 * @param message - The message to display on dialog
	 * @param modal - true if this is a modal view.
	 */
	public ResourceWarning(JFrame frame, 
			String title,
			String message,
			boolean modal
			) {
		super(frame, title, modal);
		
        setSize(new Dimension(400, 220));
        this.message = message;
        this.getContentPane().add(addButtons());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				close();
			}
		});
		setLocationRelativeTo(frame);
    }
	/**
	 * 
	 *
	 */
	protected void close(){
		
		this.dispose();
	}

	private JPanel addButtons() {
		JCheckBox checkBox = 
			new JCheckBox("Warn me every time i change resource directory");
		JPanel btnPanel = new JPanel(new BorderLayout());
			
		JPanel labelPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		labelPane.add(new JLabel(this.message));
		btnPanel.add(labelPane,BorderLayout.PAGE_START);
		
		JPanel btnFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.showchangedirectorymessage.checked",EFGImportConstants.EFG_TRUE);
		if(property.equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
			checkBox.setSelected(true);
		}
		else {
			checkBox.setSelected(false);
		}
		checkBox.addItemListener(this);
		btnFlowPanel.add(checkBox);
		
		JButton doneBtn = new JButton("OK");
		doneBtn.addActionListener(this);
		btnFlowPanel.add(doneBtn);
		btnPanel.add(btnFlowPanel, BorderLayout.SOUTH);
			
		return btnPanel;
	}
	public void itemStateChanged(ItemEvent e) {
	       if (e.getStateChange() == ItemEvent.DESELECTED) {
	    	   EFGImportConstants.EFGProperties.setProperty(
						"efg.showchangedirectorymessage.checked",EFGImportConstants.EFG_FALSE);    
	        }
	       else {
	    	   EFGImportConstants.EFGProperties.setProperty(
						"efg.showchangedirectorymessage.checked",EFGImportConstants.EFG_TRUE);    

	       }
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		this.close();
	}
}
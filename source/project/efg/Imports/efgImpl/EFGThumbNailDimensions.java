/**
 * 
 */
package project.efg.Imports.efgImpl;

/**
 * @author kasiedu
 *
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import project.efg.util.DnDFileBrowserMain;
import project.efg.util.EFGComboBox;
import project.efg.util.EFGImagesConstants;
import project.efg.util.EFGImportConstants;







/*
 * EFGUsersList.java uses these additional files:
 */
public class EFGThumbNailDimensions  extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SerializeDeserializeHandler checkBoxManager;
	
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGThumbNailDimensions.class);
			
		} catch (Exception ee) {
		}
	}
	private String currentSelection;
	private JFrame frame;
	private JCheckBox checkBox;
	private EFGComboBox comboList;
	public EFGThumbNailDimensions(JFrame frame, boolean modal) {
		this(frame, "", modal);
	}
	public EFGThumbNailDimensions(JFrame frame, String title, boolean modal
			) {
		super(frame, title, modal);
    	this.setTitle("Thumbnail Options");
        this.frame = frame;
        this.checkBoxManager = new SerializeDeserializeHandler();
        
        setSize(new Dimension(330, 150));
        add(addButtons());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				close();
			}
		});
		this.setLocationRelativeTo(frame);
    }
	protected void close(){
		this.checkBoxManager.serializeCheckBox(this.checkBox,
		EFGImagesConstants.CHECKBOX_SER_NAME
				);
		this.comboList.serialize(EFGImportConstants.THUMBS_FILE_NAME);
		this.dispose();
	}
	public void setCurrentSelection(String selection) {
		this.currentSelection = selection;
		DnDFileBrowserMain.setCurrentDim(this.currentSelection);
		DnDFileBrowserMain.setCurrentDimLabel(this.currentSelection);
	}
	public String getCurrentSelection() {
		return this.currentSelection;
	}
	private JPanel addButtons() {
		JPanel btnPanel = new JPanel(new BorderLayout());
		this.comboList = new EFGComboBox();
		comboList.setFocusable(true);	
		comboList.deserialize(EFGImportConstants.THUMBS_FILE_NAME);
		
		JPanel labelPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		labelPane.add(new JLabel("Enter or Select max dimension(in pixels): "));
		btnPanel.add(labelPane,BorderLayout.PAGE_START);
		
		JPanel comboBoxPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		comboBoxPane.add(comboList);
		btnPanel.add(comboBoxPane,BorderLayout.CENTER);
		
		
		JPanel btnFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		String buttonLabel = "Don't ask me again..";
		this.checkBox = 
			this.checkBoxManager.getCheckBox(EFGImagesConstants.CHECKBOX_SER_NAME,buttonLabel );
		
		
		btnFlowPanel.add(this.checkBox);
		
		JButton doneBtn = new JButton("OK");
		doneBtn.addActionListener(new ComboBoxListener(this,comboList));
		btnFlowPanel.add(doneBtn);
		btnPanel.add(btnFlowPanel, BorderLayout.PAGE_END);
		
		
		return btnPanel;
	}

    /**
	 * @author kasiedu
	 * 
	 */
	class ComboBoxListener implements ActionListener  {
		
		private EFGThumbNailDimensions dimensions;
		private EFGComboBox cb;
		/**
		 * @param dimensions
		 * @param comboList 
		 */
		public ComboBoxListener(EFGThumbNailDimensions dimensions,
				EFGComboBox comboList) {
			this.dimensions = dimensions;
			this.cb = comboList;
			
		}
		public void actionPerformed(ActionEvent e) {
			//if it is not an integer
			String tempSelection = (String)this.cb.getSelectedItem();
			try{
				
				if(tempSelection == null || tempSelection.trim().equals("")) {
					throw new Exception ("Enter or select a value!!");
				}
				Integer intval = new Integer(tempSelection);
				if(intval == null){
					throw new Exception ("The value entered must not be null or the empty string!!");
				}
				this.dimensions.setCurrentSelection(tempSelection);	
				cb.add(currentSelection);
				this.dimensions.close();
				
			}
			catch(NumberFormatException nee){
				JOptionPane.showMessageDialog(frame, "The value you entered: '" + tempSelection + "' must be a number!!",
						"Number Format Exception", JOptionPane.ERROR_MESSAGE);
				if((this.dimensions.getCurrentSelection() != null) && 
						(!this.dimensions.getCurrentSelection().trim().equals(""))){
					cb.setSelectedItem(this.dimensions.getCurrentSelection());
				}
				//do not dismiss the window
				return;
			}
			catch(Exception ee){
				JOptionPane.showMessageDialog(frame,ee.getMessage(),
						"Exception", JOptionPane.ERROR_MESSAGE);
		
				if((this.dimensions.getCurrentSelection() != null) && 
						(!this.dimensions.getCurrentSelection().trim().equals(""))){
					cb.setSelectedItem(this.dimensions.getCurrentSelection());
				}
				return;
				
			}
		}

	}	
}




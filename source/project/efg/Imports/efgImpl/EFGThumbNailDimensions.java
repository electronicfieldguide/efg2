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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import project.efg.util.EFGComboBox;
import project.efg.util.EFGImportConstants;
import project.efg.util.RegularExpresionConstants;
import project.efg.util.WorkspaceResources;

/*
 * EFGUsersList.java uses these additional files:
 */
public class EFGThumbNailDimensions  extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGThumbNailDimensions.class);
			
		} catch (Exception ee) {
		}
	}
	
	private JFrame frame;
	private JCheckBox checkBox;
	private EFGComboBox comboList;

	public EFGThumbNailDimensions(
			JFrame frame, String title, boolean modal
			) {
		super(frame, title, modal);
    	this.setTitle("Thumbnail Options");
        this.frame = frame;
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
		this.dispose();
	}
	/**
	 * 
	 */
	private void useDefaults(EFGComboBox comboList) {
		String[] dimensions = WorkspaceResources.getDefaultDimensions();
		//DefaultComboBoxModel model =(DefaultComboBoxModel)comboList.getModel();
		StringBuffer buffer = new StringBuffer();
		for(int i= dimensions.length;i > 0;i--) {
			comboList.add(dimensions[i-1]);
			if(i < dimensions.length) {
				buffer.append(",");
			}
			buffer.append(dimensions[i-1]);
		}
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.lists",
				buffer.toString());
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.current",
				comboList.getItemAt(0).toString());
	}
	private JPanel addButtons() {
		JPanel btnPanel = new JPanel(new BorderLayout());
		this.comboList = new EFGComboBox();
		String properties = 
			EFGImportConstants.EFGProperties.getProperty("efg.thumbnails.dimensions.lists");
		if(properties == null || properties.trim().equals("")) {
			this.useDefaults(this.comboList);
		}
		else {
			String[] comboProps = properties.split(RegularExpresionConstants.COMMASEP);
			DefaultComboBoxModel model = new DefaultComboBoxModel(comboProps);
			this.comboList.setModel(model);
		}
		comboList.setFocusable(true);	
		
		
		JPanel labelPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		labelPane.add(new JLabel("Enter or Select max dimension(in pixels): "));
		btnPanel.add(labelPane,BorderLayout.PAGE_START);
		
		JPanel comboBoxPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		comboBoxPane.add(comboList);
		btnPanel.add(comboBoxPane,BorderLayout.CENTER);
		
		
		JPanel btnFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		
		properties = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.thumbnails.dimensions.checked",
					EFGImportConstants.EFG_TRUE);
		String buttonLabel = EFGImportConstants.PROMPT_FOR_DIMENSIONS;
		this.checkBox = new JCheckBox(buttonLabel);
		

		if(properties.trim().equals(EFGImportConstants.EFG_TRUE)) {
			this.checkBox.setSelected(true);
		}
		else {
			this.checkBox.setSelected(false);
		}
	
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


        protected void doHouseKeepingbeforeClosing() {
        	
           	try {
 
        		
        		int numberOfItems =  cb.getItemCount() + 1;
        		String numberOfFilesToShowStr = 
        			EFGImportConstants.EFGProperties.getProperty("numberofserverfilestoshow","5");
        		
    			
    			try {
    				int i  = Integer.parseInt(numberOfFilesToShowStr);
    				numberOfItems = i;
    			}
    			catch(NumberFormatException ee) {
    				numberOfItems = numberOfItems -1;
    			}
        	
        		if(numberOfItems > cb.getItemCount()) {
        			numberOfItems=cb.getItemCount();
        		}
        		
        		
        		StringBuffer buffer = new StringBuffer();
				for(int index =0; index < numberOfItems ;index++) {
            		String currentURL = cb.getItemAt(index).toString();
            		
            		if(index > 0 ) {
            			buffer.append(",");
            		}
            		buffer.append(currentURL);
            	}
				//add to current properties
				EFGImportConstants.EFGProperties.setProperty(
						"efg.thumbnails.dimensions.lists",
						buffer.toString());
				EFGImportConstants.EFGProperties.setProperty(
						"efg.thumbnails.dimensions.current",
						cb.getSelectedItem().toString());
				
				EFGImportConstants.EFGProperties.setProperty(
						"efg.thumbnails.dimensions.checked",
						checkBox.isSelected()+"");
				
				
				this.dimensions.close();
			} catch (Exception e) {
				
				log.error(e.getMessage());
			}
        	
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
				
				cb.add(tempSelection);
				this.doHouseKeepingbeforeClosing();
			}
			catch(NumberFormatException nee){
				JOptionPane.showMessageDialog(frame, "The value you entered: '" + tempSelection + "' must be a number!!",
						"Number Format Exception", JOptionPane.ERROR_MESSAGE);
					cb.setSelectedItem(EFGImportConstants.EFGProperties.getProperty(
							"efg.thumbnails.dimensions.current"));
				
				return;
			}
			catch(Exception ee){
				JOptionPane.showMessageDialog(frame,ee.getMessage(),
						"Exception", JOptionPane.ERROR_MESSAGE);
				cb.setSelectedItem(EFGImportConstants.EFGProperties.getProperty(
				"efg.thumbnails.dimensions.current"));
				return;
				
			}
		}

	}	
}




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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import project.efg.util.DnDFileBrowserMain;
import project.efg.util.EFGImagesConstants;



public class MediaResourceLocationHandler  extends JDialog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	public static final String RADIO_BUTTON_1 = "Copy";
	public static final String RADIO_BUTTON_2 = "NoCopy";
	public static final String CHECK_BOX="CheckBox";
	public static final String DONE = "Done";
	
	
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(MediaResourceLocationHandler.class);
			
		} catch (Exception ee) {
		}
	}
	private JRadioButton remoteServerLocationButton,localServerLocationButton;
	
	private ImportMenu menu;
	
	
	private MediaResourceLocationListener mediaResourceLocatorListener;
	private SerializeDeserializeHandler serializer;
	private JCheckBox checkBox;
	private DnDFileBrowserMain browser;
	private String localPathToImagesDirectory;
	private String pathToImagesServerDirectory;
	
	private boolean remoteServerLocationOriginalButtonState;
	private boolean localServerLocationOriginalButtonState;
	private boolean checkBoxOriginalState;
	public MediaResourceLocationHandler(ImportMenu menu, boolean modal) {
		this(menu, "", modal,null);
	}
	public MediaResourceLocationHandler(
			ImportMenu menu, 
			String title, 
			boolean modal, 
			DnDFileBrowserMain browser
			) {
		super(menu, title, modal);
    	this.setTitle("Set Media Resource Location");
    	if(browser != null) {
    		this.browser = browser;
    	}
    	if(menu != null) {
	        this.menu = menu;
	    	this.localPathToImagesDirectory  = 
				this.menu.getLocalMediaResourceDirectory();
			this.pathToImagesServerDirectory = 
				this.menu.getLocalTomcatServerPath();
    	}
        this.serializer = 
    		new SerializeDeserializeHandler();
        this.mediaResourceLocatorListener = 
        	new MediaResourceLocationListener(this);
        
        setSize(new Dimension(400, 250));
        add(addComponents());
        this.setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				close();
			}
		});
		this.setLocationRelativeTo(this.menu);
    }
	private void serializeRadioButtons() {
		
			this.serializer.serializeButton(
					this.remoteServerLocationButton, 
					EFGImagesConstants.RADIO_BUTTON_2_FNAME );
		
		
		this.serializer.serializeButton(
				this.localServerLocationButton, 
				EFGImagesConstants.RADIO_BUTTON_1_FNAME );
		
	}
	private void serializeCheckBoxes() {
		this.serializer.serializeCheckBox(this.checkBox,
				EFGImagesConstants.CHECK_BOX_1_FNAME);
	}
	/**
	 * Record state change
	 * @return true if the original state of any of the radio
	 * buttons has changed, otherwise return false
	 */
	private boolean isReLoadBrowser() {
		if(((this.remoteServerLocationButton.isSelected()) &&
				(this.remoteServerLocationOriginalButtonState)) || 
				((!this.remoteServerLocationButton.isSelected()) &&
				(!this.remoteServerLocationOriginalButtonState)) ){
			return false;
		}
		if(((this.localServerLocationButton.isSelected()) &&
				(this.localServerLocationOriginalButtonState)) || 
				((!this.localServerLocationButton.isSelected()) &&
				(!this.localServerLocationOriginalButtonState)) ){
			return false;
		}
		return true;
	}
	public void close(){
		
		if(this.menu != null) {
			if(isReLoadBrowser()) {//there is a state change
				String currentImDirectory = null;
				if(this.remoteServerLocationButton.isSelected()) {
					this.menu.setMediaResourcesParentDirectory(
							this.localPathToImagesDirectory);
					currentImDirectory = this.localPathToImagesDirectory 
					+ EFGImagesConstants.EFG_IMAGES_DIR;
				}
				if(this.localServerLocationButton.isSelected()) {
					
					this.menu.setMediaResourcesParentDirectory(
							this.pathToImagesServerDirectory);
					currentImDirectory =this.pathToImagesServerDirectory + 
					EFGImagesConstants.EFG_IMAGES_DIR;
	
				}
				if(this.browser != null) {
					this.browser.setBrowserReload(currentImDirectory);
				}
			}
		}
		this.serializeRadioButtons();
		this.serializeCheckBoxes();
		this.dispose();
	}
	private JPanel addComponents() {
		
		JPanel btnPanel = new JPanel(new BorderLayout());
		
		 String buttonLabel ="<html><p>No.I will copy Media Resources to Server.</p>" +
	 		"<p>Generated Media Resources are Located at <p>" +
	 		"<p>resource/" + EFGImagesConstants.LOCAL_IMAGES_DIR   + "</p></html>"; 

		
		 this.remoteServerLocationButton =  
			 this.serializer.getRadioButton(EFGImagesConstants.RADIO_BUTTON_2_FNAME,buttonLabel);
		 this.remoteServerLocationOriginalButtonState = 
			 this.remoteServerLocationButton.isSelected();
	
		 this.remoteServerLocationButton.setMnemonic(KeyEvent.VK_R);
		 this.remoteServerLocationButton.setActionCommand(RADIO_BUTTON_1);
		 
		 
		 buttonLabel ="<html><p>Yes.Copy Media Resources to Local Server</p></html>";

		this.localServerLocationButton = 
		this.serializer.getRadioButton(EFGImagesConstants.RADIO_BUTTON_1_FNAME,
				buttonLabel);
		 
		this.localServerLocationOriginalButtonState = 
			this.localServerLocationButton.isSelected();
			
		 this.localServerLocationButton.setMnemonic(KeyEvent.VK_P);
		 this.localServerLocationButton.setActionCommand(RADIO_BUTTON_2);
			
		

		    //Group the radio buttons.
		 ButtonGroup group = new ButtonGroup();
		 group.add(this.localServerLocationButton);
		 group.add(this.remoteServerLocationButton);
		
		 
		 JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		 radioPanel.add(this.localServerLocationButton);
	     radioPanel.add(this.remoteServerLocationButton);
	    

	     
		JPanel btnFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonLabel = "Don't ask me again..";
		
		this.checkBox = 
			this.serializer.getCheckBox(EFGImagesConstants.CHECK_BOX_1_FNAME,buttonLabel);

		this.checkBox.setActionCommand(CHECK_BOX);
		//get the state of this checkbox
		//on exit see if it has changed
		this.checkBoxOriginalState = this.checkBox.isSelected();
		btnFlowPanel.add(this.checkBox);
		
		JButton doneBtn = new JButton("OK");

		localServerLocationButton.addActionListener(this.mediaResourceLocatorListener);
	    remoteServerLocationButton.addActionListener(this.mediaResourceLocatorListener);
		checkBox.addItemListener(new MediaResourceItemListener(this));
		
		doneBtn.addActionListener(this.mediaResourceLocatorListener);

		btnFlowPanel.add(doneBtn);
		doneBtn.setActionCommand(DONE);
		String titleLabel = "<html><p>Do you want application to "+ 
		"copy generated media resources</p>" + 
		"<p> to server on local machine?</p></html>";
		JLabel title = new JLabel(titleLabel, JLabel.CENTER);
		btnPanel.add(title,BorderLayout.PAGE_START);
		btnPanel.add(radioPanel, BorderLayout.CENTER);
		btnPanel.add(btnFlowPanel, BorderLayout.PAGE_END);
		
		 TitledBorder titled = 
			 BorderFactory.createTitledBorder("");
	      
		 btnPanel.setBorder(titled);
		return btnPanel;
	}

	class MediaResourceItemListener implements ItemListener{

		private MediaResourceLocationHandler mediaHandler;
		public MediaResourceItemListener(MediaResourceLocationHandler handler) {
			this.mediaHandler = handler;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
	       if (e.getStateChange() == ItemEvent.DESELECTED) {
	          
	        }
	       else {
	    	  
	       }
		}
		
	}
	class MediaResourceLocationListener implements ActionListener  {

		private MediaResourceLocationHandler mediaHandler;
		public MediaResourceLocationListener(MediaResourceLocationHandler handler) {
			this.mediaHandler = handler;
		}

		public void actionPerformed(ActionEvent e) {
			
			 if(e.getActionCommand() == MediaResourceLocationHandler.DONE) {
				
				 this.mediaHandler.close();
			 }
			 else if(e.getActionCommand().equals(MediaResourceLocationHandler.RADIO_BUTTON_1)) {
				// this.mediaHandler.setSelectedRadioButton(MediaResourceLocationHandler.BUTTON_1);
			 }
			 else if(e.getActionCommand().equals(MediaResourceLocationHandler.RADIO_BUTTON_2)) {
				// this.mediaHandler.setSelectedRadioButton(MediaResourceLocationHandler.BUTTON_2);
			 }

		}
	}
 
}




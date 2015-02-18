/**
 * 
 */
package project.efg.client.utils.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import project.efg.client.impl.gui.EFGThumbNailDimensions;
import project.efg.util.interfaces.EFGImportConstants;
/**
 * @author kasiedu
 *
 */
public class PreferencesHandler extends JDialog implements ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @author kasiedu
	 *
	 */
	public class CloseButtonListener implements ActionListener {

		/**
		 * @param frame
		 * @param handler
		 */
		public CloseButtonListener() {
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			close();

		}

	}
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(PreferencesHandler.class);
			
		} catch (Exception ee) {
		}
	}
     JCheckBox thumbnailsButton;
   JCheckBox confirmExitButton;
    boolean showResourceLocator;
    boolean showChangeThumbDimensions;
    JFrame frame;
	/**
	 * @param showResourceLocator 
	 * @param showChangeThumbDimensions 
	 * 
	 */
	public PreferencesHandler(JFrame currentFrame, String title, boolean modal,
			boolean showResourceLocator, boolean showChangeThumbDimensions) {
		super(currentFrame,title,modal);
		this.showResourceLocator = showResourceLocator;
		this.showChangeThumbDimensions = showChangeThumbDimensions;
		this.frame = currentFrame;
		this.setTitle(title);
		this.getContentPane().setLayout(new BorderLayout());
		setSize(new Dimension(400, 200));
		
        //Create the check boxes.
		boolean isSelected = true;
        
        String property =
        	EFGImportConstants.EFGProperties.getProperty(
        			"efg.serverlocation.checked",
        			EFGImportConstants.EFG_TRUE);
        if(!property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        }
         isSelected = true;
        
        thumbnailsButton = new JCheckBox("Prompt For Thumb nail Size");
        thumbnailsButton.setMnemonic(KeyEvent.VK_T);
        property = 
        	EFGImportConstants.EFGProperties.getProperty(
        			"efg.thumbnails.dimensions.checked",EFGImportConstants.EFG_TRUE);
        if(!property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        	
        }
        thumbnailsButton.setSelected(isSelected);
        isSelected = true;
       
       confirmExitButton = new JCheckBox("Confirm Exit Prompt When Closing Window");
        confirmExitButton.setMnemonic(KeyEvent.VK_E);
        property = EFGImportConstants.EFGProperties.getProperty(
        		"efg.showdismiss.checked",
        		EFGImportConstants.EFG_TRUE);
        if(!property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        	
        }
        confirmExitButton.setSelected(isSelected);
        isSelected = true;
       

         property = 
        	EFGImportConstants.EFGProperties.getProperty(
        		"efg.imagemagicklocation.checked",EFGImportConstants.EFG_TRUE
        		);
        if(!property.trim().equals(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        	
        }
          isSelected = true;
        property = 
        	EFGImportConstants.EFGProperties.getProperty(
        		"efg.showchangedirectorymessage.checked",EFGImportConstants.EFG_TRUE
        		);
        if(!property.trim().equals(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        	
        }
        isSelected = true;

        //Register a listener for the check boxes.
         thumbnailsButton.addItemListener(this);
         
        JButton closeBtn = new JButton("OK");
        closeBtn.addActionListener(new CloseButtonListener());

        //Put the check boxes in a column in a panel
        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
         checkPanel.add(thumbnailsButton);
         checkPanel.add(confirmExitButton);
        checkPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.getContentPane().add(checkPanel, BorderLayout.LINE_START);
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeBtn);
        this.getContentPane().add(btnPanel, BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				close();
			}
		});
		this.setLocationRelativeTo(this.frame);

	}
	public void close() {
		this.dispose();
	}
	/**
	 * 
	 */
	public void itemStateChanged(ItemEvent e) {		  
        Object source = e.getItemSelectable();

        if (source == thumbnailsButton) {
        	EFGImportConstants.EFGProperties.setProperty(
        			"efg.thumbnails.dimensions.checked",
        			thumbnailsButton.isSelected()+"");
        	if(thumbnailsButton.isSelected()) {
        		if(showChangeThumbDimensions) {
					EFGThumbNailDimensions thd = 
						new EFGThumbNailDimensions(frame,
								"Enter Thumbnail Dimension",true);
					thd.setVisible(true);
        		}
        	}
        	
        }
	}
	
}

/**
 * 
 */
package project.efg.Imports.efgImportsUtil;

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

import project.efg.Imports.efgImpl.EFGThumbNailDimensions;
import project.efg.util.EFGImportConstants;
import project.efg.util.ServerLocator;
/**
 * @author kasiedu
 *
 */
public class PreferencesHandler extends JDialog implements ItemListener {
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
 	JCheckBox serverRootButton;
    JCheckBox thumbnailsButton;
    JCheckBox confirmExitButton;
    JCheckBox confimChangeDirectoryButton;
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
        serverRootButton = new JCheckBox("Prompt For Server Root");
        serverRootButton.setMnemonic(KeyEvent.VK_S);
        
        String property =
        	EFGImportConstants.EFGProperties.getProperty(
        			"efg.serverlocation.checked",
        			EFGImportConstants.EFG_TRUE);
        if(!property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        }
        serverRootButton.setSelected(isSelected);
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
       

        confimChangeDirectoryButton = new JCheckBox("Prompt When I Change Server Location");
        confimChangeDirectoryButton.setMnemonic(KeyEvent.VK_P);
        property = 
        	EFGImportConstants.EFGProperties.getProperty(
        		"efg.showchangedirectorymessage.checked",EFGImportConstants.EFG_TRUE
        		);
        if(!property.trim().equals(EFGImportConstants.EFG_TRUE)) {
        	isSelected = false;
        	
        }
        confimChangeDirectoryButton.setSelected(isSelected);
       

      

        //Register a listener for the check boxes.
        serverRootButton.addItemListener(this);
        thumbnailsButton.addItemListener(this);
        confirmExitButton.addItemListener(this);
        confimChangeDirectoryButton.addItemListener(this);
        
        JButton closeBtn = new JButton("OK");
        closeBtn.addActionListener(new CloseButtonListener());

        //Put the check boxes in a column in a panel
        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        checkPanel.add(serverRootButton);
        checkPanel.add(thumbnailsButton);
        checkPanel.add(confirmExitButton);
        checkPanel.add(confimChangeDirectoryButton);
        checkPanel.add(closeBtn);
        
        checkPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.getContentPane().add(checkPanel, BorderLayout.LINE_START);
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
        if (source == serverRootButton) {
            EFGImportConstants.EFGProperties.setProperty(
            		"efg.serverlocation.checked",
            		this.serverRootButton.isSelected()+"");
           if(this.serverRootButton.isSelected()) {
        	   if(showResourceLocator) {
	            	 String pathToServer = EFGImportConstants.EFGProperties.getProperty(
	            			 "efg.serverlocations.current");
	     	       String property = 
	    				EFGImportConstants.EFGProperties.getProperty(
	    						"efg.showchangedirectorymessage.checked",
	    						EFGImportConstants.EFG_TRUE);

	     			if(property.equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
	    				StringBuffer buffer = new StringBuffer();
	    				buffer.append("<html>");
	    				buffer.append("<p>If the folder you are about to select is not the root</p>");
	    				buffer.append("<p>of your Tomcat server , then be aware of the following: </p>");
	    				buffer.append("<p>1. Application generated resources ( media resources,</p>");
	    				buffer.append("<p>generated templates etc)</p>");
	    				buffer.append("<p>will be placed in the folder you are about to select. </p>");
	    				buffer.append("<p>2. You will have to physically copy these resources</p>" +
	    						"<p> to an efg2 web application.</p>");
	    				buffer.append("<p> See the docs on how to copy resources to the web application</p>"); 
	    				buffer.append("</html>");
	    				
	    				ResourceWarning rw = 
	    					new ResourceWarning(frame,
	    						"Changing Directory",buffer.toString(),true);
	    				rw.setVisible(true);	
	    			} 
	            	 //bring up the warning
	    			ServerLocator locator = new ServerLocator(frame,pathToServer,true);
	    			locator.setVisible(true);
        	   }
            }
            
        } else if (source == thumbnailsButton) {
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
        	
        } else if (source == confimChangeDirectoryButton) {
        	EFGImportConstants.EFGProperties.setProperty(
        			"efg.showchangedirectorymessage.checked",
        			confimChangeDirectoryButton.isSelected()+"");
        } 
	   else if (source == confirmExitButton) {
      	EFGImportConstants.EFGProperties.setProperty(
      			"efg.showdismiss.checked",
      			confirmExitButton.isSelected()+"");
      	
      }
	}
	
}

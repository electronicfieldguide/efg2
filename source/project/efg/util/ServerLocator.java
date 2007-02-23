/**
 * 
 */
package project.efg.util;

/**
 * @author kasiedu
 *
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import project.efg.Imports.efgImpl.CaseInsensitiveComparatorImpl;



/**
 *
 * This is a Swing application that demonstrates
 * how to use the Jakarta HttpClient multipart POST method
 * for uploading files
 *
 * @author Sean C. Sullivan
 * @author Michael Becke
 *
 */
public class ServerLocator extends JDialog {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(ServerLocator.class);
		} catch (Exception ee) {
	}
	}
    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private File targetFile;
		
        private DefaultComboBoxModel cmbURLModel;
        JComboBox cmbURL;
        JButton okBtn;
       JCheckBox dontAskMeAgainMessage;
      JButton btnSelectFile;
		private boolean isInsert = false;
		private String currentFileLocationProperty;
		private String prompt;
		private String fileLocationProperty;
		private String isCurrentPropertyChecked;
      
		/**
		 * 
		 * @param frame
		 * @param fileLocationProperty - A comma separated list of file names
		 * @param modal
		 * @param currentFileLocationProperty - The property to use to find the current
		 * location
		 * @param prompt - The prompt to use for the 'don't ask me again message'
		 */
		public ServerLocator(JFrame frame,
				String serverLocator,
				boolean modal,
				String fileLocationProperty,
				String currentFileLocationProperty, 
				String isCurrentPropertyChecked,
				String prompt) {
            super(frame,"",modal);
            this.prompt = prompt;
            this.isCurrentPropertyChecked = isCurrentPropertyChecked;
            this.fileLocationProperty=fileLocationProperty;
            this.currentFileLocationProperty= currentFileLocationProperty;
            String[] serverLocations = this.readServerLocations(serverLocator);
            if(serverLocations != null) {
            	cmbURLModel = new DefaultComboBoxModel(serverLocations);
            }
            else {
            	cmbURLModel = new DefaultComboBoxModel();
            }
            this.addComponents(serverLocator);
            this.addGridBags();
     
          
    		addWindowListener(new WindowAdapter() {
    			public void windowClosing(WindowEvent e) {
    				close();
    			}
    		});
    		 this.setLocationRelativeTo(frame);
        }
   
        private String[] readServerLocations(String serverLocator) {
        	
        	String serverLocationProps = 
        		EFGImportConstants.EFGProperties.getProperty(this.fileLocationProperty,null);
        	if((serverLocationProps != null) && 
        			(!serverLocationProps.trim().equals("")) && 
        			((serverLocator != null) && 
        					(!serverLocator.trim().equals("")))) {
        		String[] serverLocations = 
        			serverLocationProps.split(RegularExpresionConstants.COMMASEP);
        		if(serverLocations[0] != null && !serverLocations[0].equals("")) {
        			EFGImportConstants.EFGProperties.setProperty(
						currentFileLocationProperty,serverLocations[0]);
        			 WorkspaceResources.computeMediaResourcesHome();
        			 WorkspaceResources.computeTemplatesHome();
        		}
        		Collections.sort(Arrays.asList(serverLocations));
        		int i = Arrays.binarySearch(serverLocations, serverLocator,
        				new CaseInsensitiveComparatorImpl());
        		if(i < 0) {//not in list so insert it at the beginning
        			this.isInsert = true;
        		}
        		return WorkspaceResources.convertURIToString(serverLocations);
        	}
        	if((serverLocator != null) && 
					(!serverLocator.trim().equals(""))) {
        		this.isInsert = true;//insert the current value
        	}
        	return null;
         }
        private void addGridBags() {
		      this.getContentPane().setLayout(new GridBagLayout());
		        GridBagConstraints c = new GridBagConstraints();
		
		        c.anchor = GridBagConstraints.WEST;
		        c.fill = GridBagConstraints.HORIZONTAL;
		        c.insets = new Insets(5, 5, 5, 10);
		        c.gridwidth = 1;
		        c.gridx = 1;
		        this.getContentPane().add(cmbURL, c);
		        
		        c.anchor = GridBagConstraints.WEST;
		        c.fill = GridBagConstraints.NONE;
		        c.insets = new Insets(5, 5, 5, 10);
		        c.gridwidth = 1;
		        c.gridx = 2;
		        this.getContentPane().add(btnSelectFile, c);
		        
		        c.anchor = GridBagConstraints.WEST;
		        c.fill = GridBagConstraints.NONE;
		        c.insets = new Insets(5, 5, 5, 10);
		        c.gridwidth = 1;
		        c.gridx = 1;
		        this.getContentPane().add(dontAskMeAgainMessage, c);
		        
		        
		        c.anchor = GridBagConstraints.CENTER;
		        c.fill = GridBagConstraints.NONE;
		        c.insets = new Insets(10, 10, 10, 10);
		        c.gridwidth = 3;
		        c.gridx = 0;
		        c.gridy = 3;
		        this.getContentPane().add(okBtn, c);
		}
		private void addComponents(String serverLocator) {
			
		    if(this.isInsert) {
		    	if(serverLocator != null && !serverLocator.trim().equals("")) {
			    	this.cmbURLModel.insertElementAt(serverLocator,0);
			    	
			    	EFGImportConstants.EFGProperties.setProperty(
			    			currentFileLocationProperty,
							WorkspaceResources.convertFileNameToURLString(serverLocator));
			    	 WorkspaceResources.computeMediaResourcesHome();
					 WorkspaceResources.computeTemplatesHome();
		    	}
		    }
		    String fileproperty = 
				EFGImportConstants.EFGProperties.getProperty(
						"ServerLocator.button.message",
						"<html><p>Change Tomcat Home...</p></html>"); 
		    this.cmbURL = new JComboBox(cmbURLModel);
		    cmbURL.setToolTipText(fileproperty);
		    cmbURL.setEditable(true);
		    cmbURL.setSelectedIndex(0);
		    
		   
		    
		   dontAskMeAgainMessage = new JCheckBox(
		            prompt);
		
		   String property = 
			EFGImportConstants.EFGProperties.getProperty(
					isCurrentPropertyChecked,
					EFGImportConstants.EFG_TRUE);
		  boolean isSelected = true;
		   if(!property.trim().equalsIgnoreCase(EFGImportConstants.EFG_TRUE)) {
		   	isSelected = false;
		   }
		   dontAskMeAgainMessage.setSelected(isSelected);
		    
		    
		   this.okBtn = new JButton("OK");
		  
		   this.btnSelectFile = 
		    	new JButton(fileproperty);
		    this.btnSelectFile.addActionListener(
		    		new FileChooserListener(serverLocator,fileproperty)
		    		);		
		    okBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					close();
		          
		        }
		    });
		    setSize(new Dimension(600, 200));
		
		}
		protected void doHouseKeepingbeforeClosing() {
        	
           	try {
            	
        		int numberOfItems = -1;
        		String numberOfFilesToShowStr = 
        			EFGImportConstants.EFGProperties.getProperty("numberofserverfilestoshow","5");
        		
    			try {
    				numberOfItems =Integer.parseInt(numberOfFilesToShowStr);
    			}
    			catch(NumberFormatException ee) {
    				numberOfItems = cmbURL.getItemCount();
    			}
        		
        		if(numberOfItems > cmbURL.getItemCount()) {
        			numberOfItems=cmbURL.getItemCount();
        		}
        		StringBuffer buffer = new StringBuffer();
				for(int index =numberOfItems; index > 0 ;index--) {
					
            		String currentURL = cmbURL.getItemAt(index-1).toString();
            		
            		if(index < numberOfItems ) {
            			buffer.append(",");
            		}
            		buffer.append(WorkspaceResources.convertFileNameToURLString(currentURL));
            	}
				String url = 
					WorkspaceResources.convertFileNameToURLString(cmbURL.getSelectedItem().toString());
				//add to current properties
				
				EFGImportConstants.EFGProperties.setProperty(
						fileLocationProperty,
						
						buffer.toString());
				EFGImportConstants.EFGProperties.setProperty(
						isCurrentPropertyChecked,
						dontAskMeAgainMessage.isSelected()+"");
				EFGImportConstants.EFGProperties.setProperty(
						currentFileLocationProperty,url
						);
				 WorkspaceResources.computeMediaResourcesHome();
				 WorkspaceResources.computeTemplatesHome();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
        	
        }

		public void close() {
        	doHouseKeepingbeforeClosing();
        	this.dispose();
        }
		
		class FileChooserListener implements ActionListener{
			private String serverLocation;
			private String message;
			/**
			 * 
			 */
			public FileChooserListener(String serverLocation, String message) {
				this.serverLocation = serverLocation;
				this.message = message;
			}
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
	            JFileChooser chooser = new JFileChooser();
	            chooser.setFileHidingEnabled(false);
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.setMultiSelectionEnabled(false);
	            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	            chooser.setDialogTitle(this.message);
	            chooser.setCurrentDirectory(new File(this.serverLocation));
	            if (
	                chooser.showOpenDialog(
	                		ServerLocator.this)
	                == JFileChooser.APPROVE_OPTION
	                ) {
	                targetFile = chooser.getSelectedFile();
	                if(targetFile != null && !targetFile.toString().trim().equals("")) {
	
	                    if (!targetFile.toString()
	                            .equals(
	                            cmbURLModel.getElementAt(
	                            cmbURL.getSelectedIndex()))) {
	                        cmbURLModel.insertElementAt(targetFile.toString(),0);
	                        cmbURL.setSelectedIndex(0);//insert at the beginning
	                    }
	                }                        
	            }
				
			}
			
		}
}

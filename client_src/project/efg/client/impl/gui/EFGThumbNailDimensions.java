/**
 * 
 */
package project.efg.client.impl.gui;

/**
 * @author kasiedu
 *
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import project.efg.client.utils.gui.DnDFileBrowserMain;
import project.efg.client.utils.nogui.WorkspaceResources;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;

/*
 * EFGUsersList.java uses these additional files:
 */
public class EFGThumbNailDimensions  extends JDialog{
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
	
	
	List historyList;
	String currentSelection;
    Set comboBoxItemsSet;
    JComboBox comboBoxItems;
    int MAX_SIZE = -1;
    

    public EFGThumbNailDimensions(JFrame frame,String title,boolean isModal) {
    	super(frame,title,isModal);
    	this.frame =frame;
    	this.setComboListSize();
    	this.getContentPane().setLayout(new BorderLayout());
    	
//    	Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                	close();
            }
        });
    	
        this.setSize(300, 200);
        this.setResizable(false);
        

        this.getContentPane().add(getMainPanel(),BorderLayout.CENTER);
        this.getContentPane().add(getBottomPanel(),BorderLayout.SOUTH);
        
        
        this.setLocationRelativeTo(frame);
        try {
        	this.comboBoxItems.setSelectedItem(this.historyList.get(0));
        
        }
        catch(Exception ee) {
        	log.error(ee.getMessage());
        }
    } //constructor
    
    private void setComboListSize() {
    	String numberOfFilesToShowStr = 
			EFGImportConstants.EFGProperties.getProperty(
					"numberofserverfilestoshow","5");
   		try {
   			int intval = Integer.parseInt(numberOfFilesToShowStr);
   			MAX_SIZE = intval;
   		}
   		catch(Exception ee) {
   			MAX_SIZE=5;
   		}
    }
    private JPanel getBottomPanel() {
    	JPanel btnFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		
		String properties = 
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
		JPanel btnCheckPanel = new JPanel();
		btnCheckPanel.add(this.checkBox);
		
		JButton doneBtn = new JButton("OK");
		doneBtn.addActionListener( 
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						  close();
					}
				}//end 
        );
		JButton closeBtn = new JButton("Cancel");
		closeBtn.addActionListener( 
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						  close();
					}
				}//end 
        );

		btnFlowPanel.add(doneBtn);
		btnFlowPanel.add(closeBtn);
		JPanel btnPanel = new JPanel(new BorderLayout());
		btnPanel.add(btnFlowPanel, BorderLayout.CENTER);
		btnPanel.add(btnCheckPanel,BorderLayout.SOUTH);
        return btnPanel;
    }
    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        this.historyList = new ArrayList();
        this.loadComboBox();
   
        //Set up the UI for selecting a pattern.
        String propLabel = 
        	EFGImportConstants.EFGProperties.getProperty(
        			"thumbnails.label1",
        			"Enter the maximum dimension");
        JLabel dimensionsLabel1 = new JLabel(propLabel);
        propLabel = 
        	EFGImportConstants.EFGProperties.getProperty(
        			"thumbnails.label2",
        			"for your thumbnails (in pixels) or");
        JLabel dimensionsLabel2 = new JLabel(propLabel);
        propLabel = 
        	EFGImportConstants.EFGProperties.getProperty(
        			"thumbnails.label3",
        			"select one from the list:");
        JLabel dimensionsLabel3 = new JLabel(propLabel);

        comboBoxItems = new JComboBox(comboBoxItemsSet.toArray());
        comboBoxItems.setEditable(true);
        comboBoxItems.addActionListener(new ComboBoxListener());
      
        //Create the UI for displaying result.
  

        //Lay out everything.
        JPanel dimensionsPanel = new JPanel();
        dimensionsPanel.setLayout(new BoxLayout(dimensionsPanel,
                               BoxLayout.PAGE_AXIS));
        dimensionsPanel.add(dimensionsLabel1);
        dimensionsPanel.add(dimensionsLabel2);
        dimensionsPanel.add(dimensionsLabel3);
        comboBoxItems.setAlignmentX(Component.LEFT_ALIGNMENT);
        dimensionsPanel.add(comboBoxItems);

  

        dimensionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        

        mainPanel.add(dimensionsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
       

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return mainPanel;
    }
	/**
	 * 
	 */
	private void loadComboBox() {
		String properties = 
			EFGImportConstants.EFGProperties.getProperty(
					"efg.thumbnails.dimensions.lists"
					);
		
		if(properties == null || properties.trim().equals("")) {
			this.useDefaults();
			DnDFileBrowserMain.setCurrentDimLabel((String)this.historyList.get(0));
		}
		else {
			this.loadHistoryList(properties.split(
					RegularExpresionConstants.COMMASEP));
			DnDFileBrowserMain.setCurrentDimLabel((String)this.historyList.get(0));
		}
        this.comboBoxItemsSet = new TreeSet(this.historyList);
	}


	public void close() {
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.checked",
				checkBox.isSelected()+"");
		this.dispose();
	}
	/**
	 * 
	 */
	private void useDefaults() {
		this.loadHistoryList(WorkspaceResources.getDefaultDimensions());
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.current",
				(String)this.historyList.get(0));
	}
	/**
	 * @param strings
	 */
	private void loadHistoryList(String[] dimensions) {
		
		StringBuilder buffer = new StringBuilder();
		for(int i=0; i < dimensions.length;i++) {
			this.historyList.add(dimensions[i]);
			if(i > 0) {
				buffer.append(",");
			}
			buffer.append(dimensions[i]);
		}
		
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.lists",
				buffer.toString());
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.current",
				(String)this.historyList.get(0));

		
	}
	private String getCurrentHistoryLists() {
 		StringBuilder buffer = new StringBuilder();
 		
		for (Iterator iter = this.historyList.iterator(); iter.hasNext();) {
			String element = (String)iter.next();
			buffer.append(element);
			if(iter.hasNext()) {
				buffer.append(",");
			}
		}
		
		return buffer.toString();
	}
    public void doHouseKeepingbeforeClosing() {
    	
       	try {
		
		} catch (Exception e) {
			log.error(e.getMessage());
		}
    	
    }

    public void reloadComboBoxes() {
        String newSelection = (String)this.comboBoxItems.getSelectedItem();
       
        if(newSelection == null || "".equals(newSelection.trim())) {
        	return;
        }
        if(!comboBoxItemsSet.contains(newSelection.trim())) {
	         
        	if(historyList.size() > (MAX_SIZE  -1)) {
        		String itemToRemove = (String)historyList.get(historyList.size()-1);
        		comboBoxItems.removeItem(itemToRemove);
        		comboBoxItemsSet.remove(itemToRemove);
        		historyList.remove(itemToRemove);
        	}
        }
        else {
    		comboBoxItems.removeItem(newSelection);
    		comboBoxItemsSet.remove(newSelection);
    		historyList.remove(newSelection);
        	
        }
        comboBoxItemsSet.add(newSelection.trim());
	    comboBoxItems.insertItemAt(newSelection.trim(), 0);
	    
	    historyList.add(0,newSelection.trim());	
       
        comboBoxItems.setSelectedItem(newSelection);
        comboBoxItems.setFocusable(true);
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.lists",
				this.getCurrentHistoryLists());
		EFGImportConstants.EFGProperties.setProperty(
				"efg.thumbnails.dimensions.current",
				(String)this.historyList.get(0));
		DnDFileBrowserMain.setCurrentDimLabel((String)this.historyList.get(0));
    }
    public void doHouseKeeping() {
    	String newSelection = null;
    	try{
	       newSelection = (String)comboBoxItems.getSelectedItem();
	       
	        if(newSelection == null || "".equals(newSelection.trim())) {
	        	throw new Exception ("Enter or select a value!!");
	        }
        	Integer intval = new Integer(newSelection);
			if(intval == null){
				throw new Exception ("The value entered must not be null or the empty string!!");
			}
			
			reloadComboBoxes();
			
    	}
		catch(NumberFormatException nee){
				JOptionPane.showMessageDialog(frame, "The value you entered: \"" +
						newSelection + "\" must be a number!!",
						"Number Format Exception", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch(Exception ee){
			
				JOptionPane.showMessageDialog(frame,ee.getMessage(),
						"Exception", JOptionPane.ERROR_MESSAGE);
				return;
		}
    }
    /**
	 * @author kasiedu
	 * 
	 */
	class ComboBoxListener implements ActionListener  {
		/**
		 * @param dimensions
		 * @param comboList 
		 */
		public ComboBoxListener() {	
		}
	    public void actionPerformed(ActionEvent e) {
	        doHouseKeeping();
	    }
	}
	
}




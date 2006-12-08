/**
 * 
 */
package project.efg.Imports.efgImpl;

/**
 * @author kasiedu
 *
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import project.efg.Imports.rdb.RunSetUp;
import project.efg.util.EFGComboBox;
import project.efg.util.EFGImportConstants;





/*
 * EFGUsersList.java uses these additional files:
 */
public class EFGUsersList  extends JDialog
                          implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGUsersList.class);
			
		} catch (Exception ee) {
		}
	}
	private UserListCombo efgUsers;
	private DBObject dbObject;
	private JFrame frame;
	private String userName;
	private Object[] defaultString= {EFGImportConstants.EFGProperties.getProperty("nousers")};
	public EFGUsersList(JFrame frame, boolean modal, DBObject db) {
		this(frame, "", modal, db);
	}
	protected String getCurrentUser() {
		return this.userName;
	}
	public EFGUsersList(JFrame frame, String title, boolean modal,
			DBObject dbObject) {
		super(frame, title, modal);
		
        this.dbObject = dbObject;
        String newTitle = this.dbObject.getUserName();
    	this.setTitle("Current user is : " + newTitle);
        this.frame = frame;
        setSize(new Dimension(600, 150));
        add(addButtons());
    }
	/**
	 * @return
	 */
	private Object[] getUsers() {
		
		return RunSetUp.getEFGUsers(this.dbObject);
	
	}
    private JPanel addButtons() {
    	JPanel pp = new JPanel(new EFGDialogLayout());
		pp.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED),
				new EmptyBorder(5, 5, 5, 5)));
		pp.add(new JLabel("EFG Users:"));
		add2ComboBox();
		
		
		pp.add(efgUsers);

	

		JPanel p = new JPanel(new EFGDialogLayout());
		p.setBorder(new EmptyBorder(10, 10, 10, 10));
		p.add(pp);

		JButton efgUserBtn =
			new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.efgUserBtn"));
		efgUserBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.efgUserBtn.tooltipText"));
		efgUserBtn.setHorizontalAlignment(SwingConstants.CENTER);
		efgUserBtn.addActionListener(new CreateUserListener(
				this.dbObject, this.frame));
		
		JButton deleteEfgUserBtn =
			new JButton(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deleteEFGUserBtn"));
		deleteEfgUserBtn.setToolTipText(EFGImportConstants.EFGProperties.getProperty("ImportMenu.deleteEFGUserBtn.tooltipText"));
		deleteEfgUserBtn.setHorizontalAlignment(SwingConstants.CENTER);
		deleteEfgUserBtn.addActionListener(new DeleteUserListener(
				this.dbObject, this.frame));
		p.add(efgUserBtn);
		p.add(deleteEfgUserBtn);
		
		JButton closeBtn =
			new JButton("Close");
		closeBtn.addActionListener(new DoneListener(this));
		closeBtn.setToolTipText("Close");
		closeBtn.setHorizontalAlignment(SwingConstants.CENTER);
		p.add(closeBtn);
		
	
		return p;
    }
    /**
	 * 
	 */
	private void add2ComboBox() {
		 Object[] userList = this.getUsers();
		 
	       //Create the combo box, select the item at index 4.
	        //Indices start at 0, so 4 specifies the pig.
		 efgUsers = new UserListCombo();
		 if(userList == null || userList.length == 0) {
			 userList = defaultString;
			 addValues2Combo(userList);
			 //efgUsers = new JComboBox(defaultString);
			efgUsers.setEnabled(false);
		 }
		 else {
			 addValues2Combo(userList);
			//efgUsers = new JComboBox(userList);
			efgUsers.setSelectedIndex(0);
		 }
		 efgUsers.setEditable(false);
	     efgUsers.addActionListener(this);
		
		
	}
	/**
	 * @param userList
	 */
	private void addValues2Combo(Object[] userList) {
		Arrays.sort(userList);
		for(int i=0; i < userList.length; i++) {
			efgUsers.add(userList[i].toString());
		}
	}
	/** Listens to the combo box. */
    public void actionPerformed(ActionEvent e) {
    	UserListCombo cb = (UserListCombo)e.getSource();
       this.userName = (String)cb.getSelectedItem();
       
    }
    /**
	 * 
	 * @author kasiedu Done with changes
	 */
	class DoneListener implements ActionListener {
		private EFGUsersList userList;

		public DoneListener(EFGUsersList userList) {
			this.userList = userList;
			
		}

		public void actionPerformed(ActionEvent evt) {
			this.userList.dispose();
		}
	}
	public class UserListCombo extends EFGComboBox{
		
		public UserListCombo() {
			super();
			this.setToolTipText("");
			
		}
		public void deserialize(String fName) {
			return;
		}
		public void serialize(String fName) {
			return;
		}
		public void add(String item) {
			if(!isEnabled()) {
				removeItemAt(0);
			}
			removeItem(item);
			insertItemAt(item, 0);
			setSelectedItem(item);	
			this.setEnabled(true);
		}
		public void remove(String item){
			removeItem(item);
			if(this.getItemCount() > 0) {
				setSelectedIndex(0);
				efgUsers.setEditable(false);
			}
			else {
				this.add(defaultString[0].toString());
				this.setEnabled(false);
			}
		}
	}
	/**
	 * @author kasiedu
	 *
	 */
	public class CreateUserListener implements ActionListener {
	
		private DBObject dbObject;
		private JFrame frame;

		
		/**
		 * 
		 */
		public CreateUserListener(DBObject dbObject, 
				JFrame frame) {
			this.dbObject = dbObject;
			this.frame = frame;
		}
		private void handleInput(){
			try {
				if (this.dbObject == null) {
					StringBuffer buffer = new StringBuffer(
						EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
						"\n");
					buffer
							.append(EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
							"\n");
					JOptionPane.showMessageDialog(this.frame, buffer.toString(),
							"Error Message", JOptionPane.ERROR_MESSAGE);
					log.error(buffer.toString());
					return;
				}
				
				DBObject superuserInfo = null;
				CreateEFGUserDialog dialog = new CreateEFGUserDialog(this.frame);
				dialog.setVisible(true);
				
				
				if(dialog.isSuccess()){
					superuserInfo = dialog.getDbObject();
				}
				dialog.dispose();
				boolean bool = false;
				if(superuserInfo != null){
					bool = RunSetUp.createSuperUser(dbObject,superuserInfo);
					String newusername = superuserInfo.getUserName();
					efgUsers.setEditable(true);
					efgUsers.add(newusername);
					efgUsers.setEditable(false);
				}
			
			} catch (Exception ee) {
				log.error(ee.getMessage());
				JOptionPane.showMessageDialog(this.frame, ee.getMessage(),
						"Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			 this.handleInput();
			
			
		}
	
	}
	public class DeleteUserListener implements ActionListener {
		
		private DBObject dbObject;
		private JFrame frame;
		

		/**
		 * 
		 */
		public DeleteUserListener(DBObject dbObject, 
				JFrame frame) {
			this.dbObject = dbObject;
			this.frame = frame;
			
		}
		private void handleInput(){
			try {
				if (this.dbObject == null) {
					StringBuffer buffer = new StringBuffer(
						EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
						"\n");
					buffer
							.append(EFGImportConstants.EFGProperties.getProperty("HandleDatasourceListener.buffermessage.1") +	
							"\n");
					JOptionPane.showMessageDialog(this.frame, buffer.toString(),
							"Error Message", JOptionPane.ERROR_MESSAGE);
					log.error(buffer.toString());
					return;
				}
				
				deleteUser();
			
			} catch (Exception ee) {
				log.error(ee.getMessage());
				JOptionPane.showMessageDialog(this.frame, ee.getMessage(),
						"Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
		}
		
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			 this.handleInput();
			
			
		}
		private void deleteUser(){
			String selectedUserName = efgUsers.getCurrentSelection();
			
			if ((selectedUserName!= null) && (selectedUserName.trim().length() > 0)) {
				String currentUser = this.dbObject.getUserName().trim();
				
				if(currentUser.equalsIgnoreCase(selectedUserName.trim())){
					JOptionPane.showMessageDialog(this.frame, "You need to login in as root to delete the current user!!",
							"Information Message", JOptionPane.INFORMATION_MESSAGE);	
					return;
				}
				int result = JOptionPane.showConfirmDialog(null,
						"Do you really want to delete the user : '" + selectedUserName + "' ?", "Delete User?",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					RunSetUp.deleteSuperUser(this.dbObject,selectedUserName);
					//update the users list.
					efgUsers.setEditable(true);
					efgUsers.remove(selectedUserName);
					efgUsers.setEditable(false);
				}
			  
			}
			else {
				JOptionPane.showMessageDialog(this.frame, "Please select a user to delete",
						"Error Message", JOptionPane.ERROR_MESSAGE);
			}
			return ;
		}
		
	}
}




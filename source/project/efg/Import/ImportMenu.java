package project.efg.Import;
import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * ImportMenu.java
 *
 *
 * Created: Sat Feb 18 10:38:07 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
public class ImportMenu extends JDialog {
    private String urlToServer;
    private EFGWebAppsDirectoryObject webappsDirectory;
    private DBObject dbObject;
    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(ImportMenu.class); 
	}
	catch(Exception ee){
	}
    }
    public ImportMenu(JFrame frame, boolean modal) {
	this(frame,"Import Menu",modal,null);
    }
    public ImportMenu() {
	this(null,"Import Menu",false,null);
    }
     public ImportMenu(JFrame frame, 
		      String title,
		      boolean modal, 
		      EFGWebAppsDirectoryObject directory){
	 this(frame,title,modal,directory,null);

     }
    public ImportMenu(JFrame frame, 
		      String title,
		      boolean modal, 
		      EFGWebAppsDirectoryObject directory,
		      DBObject dbObject){
	super(frame,title,modal);
	setSize(new Dimension(220, 150));
	//setLocationRelativeTo(frame);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    close();
		}
	    });
	this.webappsDirectory = directory; 
	this.dbObject = dbObject;
	JComponent newContentPane = this.addPanel();
	setContentPane(newContentPane);
    } // ImportMenu constructor
    
    private JPanel addPanel(){
	JPanel panel = new JPanel();
	panel.setLayout(new GridLayout(5,1));
	
	JButton addNewDatasourceBtn = new JButton("Import");
	addNewDatasourceBtn.setToolTipText("All Database related stuff..Add/Remove a Key");
	addNewDatasourceBtn.setHorizontalAlignment(SwingConstants.LEFT);
	addNewDatasourceBtn.addActionListener(new HandleDatasourceListener(dbObject));
	
	JButton deployImagesBtn = new JButton("MediaResources");
	deployImagesBtn.setToolTipText("Add/Remove Images");
	deployImagesBtn.setHorizontalAlignment(SwingConstants.LEFT);
      	deployImagesBtn.addActionListener(new DeployImagesListener(webappsDirectory.getImagesDirectory()));
	
	JButton helpBtn = new JButton("Help");
	helpBtn.setToolTipText("Help about this application");
	helpBtn.setHorizontalAlignment(SwingConstants.LEFT);
	helpBtn.addActionListener(new HelpListener(project.efg.util.EFGImportConstants.HELP_FILE));
	
	JButton aboutBtn = new JButton("About");
	aboutBtn.setToolTipText("About EFG Project");
	aboutBtn.setHorizontalAlignment(SwingConstants.LEFT);
	aboutBtn.addActionListener(new AboutListener());

	JButton exitBtn = new JButton("Exit");
	exitBtn.setToolTipText("Exit application");
	exitBtn.setHorizontalAlignment(SwingConstants.LEFT);
	exitBtn.addActionListener(new ExitListener(this));

	panel.setBackground(Color.lightGray);
	panel.add(addNewDatasourceBtn);
	panel.add(deployImagesBtn);
	panel.add(helpBtn);
	panel.add(aboutBtn);
	panel.add(exitBtn);
	return panel;
    }
     public void close(){
	 this.dispose();
	System.exit(0);
     }
    class DeployImagesListener implements ActionListener{
	private String imagesDirectory;
	public DeployImagesListener(String imagesDirectory){
	    this.imagesDirectory = imagesDirectory;
	}
	public void actionPerformed(ActionEvent evt) {
	    try{
		FileTreeBrowserMain ftb = new FileTreeBrowserMain(null, 
								  "EFG2 Web Application Images", 
								  true,
								  imagesDirectory);
		ftb.show();
	    }
	    catch(Exception ee){
		log.error(ee.getMessage());
	    }
	}
    }
    class HandleDatasourceListener implements ActionListener{
	private DBObject dbObject;
	public HandleDatasourceListener(DBObject dbObject){
	    this.dbObject = dbObject;
	}
	public void actionPerformed(ActionEvent evt) {
	    try{
		if(this.dbObject == null){
		    StringBuffer buffer = new StringBuffer("Successful Database login is required to Import data.\n");
		    buffer.append("Please consult our documentation via the help menu on how to start this application!!!\n");
		    JOptionPane.showMessageDialog(
						  null,
						  buffer.toString(),
						  "Error Message",
						  JOptionPane.ERROR_MESSAGE
						  ); 
		    log.error(buffer.toString());
		    return;
		}
		SynopticKeyTreeMain ftb = new SynopticKeyTreeMain(null,
								  "List Of EFG Database Tables",
								  true,
								  this.dbObject
								  );
		ftb.show();
	    }
	    catch(Exception ee){
		log.error(ee.getMessage());
		JOptionPane.showMessageDialog(
					      null,
					      ee.getMessage(),
					      "Error Message",
					      JOptionPane.ERROR_MESSAGE
					      ); 
		return;
	    }
	    //open a SynopticTree window with already existting datasources
	    
	}
    }
    class ExitListener implements ActionListener{
	private ImportMenu iMenu;
	public 	ExitListener(ImportMenu iMenu){
	    this.iMenu = iMenu;
	}
	public void actionPerformed(ActionEvent evt) {
	    this.iMenu.close();
	}
    }
  
    public static void main(String[] args){
	EFGWebAppsDirectoryObject obj = new EFGWebAppsDirectoryObject(null);
	obj.setImagesDirectory("EFGImages");

	ImportMenu menu = new ImportMenu(null,"Database Import",true, obj);
	menu.show();
    }
} // ImportMenu


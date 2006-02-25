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
	setLocationRelativeTo(frame);
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
	
	JButton addNewDatasourceBtn = new JButton("Import/Remove Datasources");
	addNewDatasourceBtn.setHorizontalAlignment(SwingConstants.LEFT);
	addNewDatasourceBtn.addActionListener(new HandleDatasourceListener(dbObject));
	
	JButton deployImagesBtn = new JButton("Deploy/Undeploy Images");
	deployImagesBtn.setHorizontalAlignment(SwingConstants.LEFT);
      	deployImagesBtn.addActionListener(new DeployImagesListener(webappsDirectory.getImagesDirectory()));

	JButton helpBtn = new JButton("Help");
	helpBtn.setHorizontalAlignment(SwingConstants.LEFT);
	helpBtn.addActionListener(new HelpListener());

	JButton aboutBtn = new JButton("About");
	aboutBtn.setHorizontalAlignment(SwingConstants.LEFT);
	//aboutBtn.addActionListener(new AboutListener(this));

	JButton exitBtn = new JButton("Exit");
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
     }
    class DeployImagesListener implements ActionListener{
	private String imagesDirectory;
	public DeployImagesListener(String imagesDirectory){
	    this.imagesDirectory = imagesDirectory;
	}
	public void actionPerformed(ActionEvent evt) {
	    try{
		FileTreeBrowserMain ftb = new FileTreeBrowserMain(null, 
								  "Deploy Images", 
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
		    throw new Exception("DBObject is null");
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
    class HelpListener implements ActionListener{
	
	public void actionPerformed(ActionEvent evt) {
	    String url = project.efg.util.EFGImportConstants.HELP_FILE;
	    
	    if (!(url.startsWith("http:") || url.startsWith("file:"))) {
		// If it's not a fully qualified URL, assume it's a file.
		if (url.startsWith("/")) {
		    // Absolute path, so just prepend "file:"
		    url = "file:" + url;
		}
		else {
		    try {
			// Assume it's relative to the starting point.
			File f = new File(url);
			url = f.toURL().toString();
		    }
		    catch (Exception e) {
			url = "http://efg.cs.umb.edu/";
		    }
		}
	    }
	    new HelpFile(url).setVisible(true);
	}
    }
    public static void main(String[] args){
	EFGWebAppsDirectoryObject obj = new EFGWebAppsDirectoryObject(null);
	obj.setImagesDirectory("EFGImages");

	ImportMenu menu = new ImportMenu(null,"Database Import",true, obj);
	menu.show();
    }
} // ImportMenu


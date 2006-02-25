package project.efg.Import;
import project.efg.efgInterface.EFGDatasourceObjectInterface;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * SynopticKeyTreeMain.java
 *
 *
 * Created: Sat Feb 18 16:41:14 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
public class SynopticKeyTreeMain extends JDialog {
  
    SynopticKeyTree tree;
    //FileTreeBrowser target;
    final JButton deleteBtn = new JButton("Delete Datasource");
    final JButton updateBtn = new JButton("Update Display Name");
    final JButton doneBtn = new JButton("Done");

    static Logger log = null;
    static{
	try{
	    log = Logger.getLogger(SynopticKeyTreeMain.class); 
	}
	catch(Exception ee){
	}
    }
    public SynopticKeyTreeMain(JFrame frame, 
			       boolean modal,
			       DBObject db) {
	this(frame,"",modal,db);
    }
    public SynopticKeyTreeMain(DBObject db){
	this(null,"",false,db);
    }
    public SynopticKeyTreeMain(JFrame frame, 
			       String title,
			       boolean modal, 
			       DBObject dbObject) {
	super(frame,title,modal);
	setSize(new Dimension(500, 400));
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    close();
		}
	    });
	String message = "";
	try{
	    this.tree = new SynopticKeyTree(dbObject);
	}
	catch(Exception ee){
	    message = ee.getMessage();
	    log.error(message);
	    JOptionPane.showMessageDialog(
					  null,
					  message,
					  "Error Message",
					  JOptionPane.ERROR_MESSAGE
					  ); 
	}
	JComponent newContentPane = addPanel();
	setContentPane(newContentPane);
    } // SynoptcKeyTreeMain constructor
    public void close(){
	this.dispose();
    }
    private JPanel addPanel(){
	JPanel panel = new JPanel(new BorderLayout());
	
	JPanel btnPanel = new JPanel();
	/*updateBtn.addActionListener(new UpdateListener(this.tree));
	btnPanel.add(updateBtn);*/
	
	deleteBtn.addActionListener(new DeleteListener(this.tree));
	btnPanel.add(deleteBtn);
	
	doneBtn.addActionListener(new DoneListener(this)); 
	btnPanel.add(doneBtn);
	
	
	panel.add(new JScrollPane(this.tree), BorderLayout.CENTER);
	panel.add(btnPanel, BorderLayout.SOUTH);
	return panel;
    }
    class DeleteListener implements ActionListener {
	private SynopticKeyTree tree;
	
	public DeleteListener(SynopticKeyTree tree) {
	    this.tree = tree;
	}
	public void actionPerformed(ActionEvent evt) {
	    this.tree.removeSelectedNode();
	}
    }
    class UpdateListener implements ActionListener {
	private SynopticKeyTree tree;
	
	public UpdateListener(SynopticKeyTree tree) {
	    this.tree = tree;
	}
	public void actionPerformed(ActionEvent evt) {
	    this.tree.updateSelectedNode();
	}
    }
    class DoneListener implements ActionListener{
	private SynopticKeyTreeMain treeBrowser;
	public 	DoneListener(SynopticKeyTreeMain treeBrowser){
	    this.treeBrowser = treeBrowser;
	}
	public void actionPerformed(ActionEvent evt) {
	    this.treeBrowser.close();
	}
    }
} // SynopticKeyTreeMain

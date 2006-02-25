package project.efg.Import;
import java.awt.event.*;
import java.io.File;
/**
 * HelpFileListener.java
 *
 *
 * Created: Sat Feb 25 14:12:43 2006
 *
 * @author <a href="mailto:">Jacob Admin</a>
 * @version 1.0
 */
 public class HelpListener implements ActionListener{
     private String url;
     public HelpListener(String url){
	 this.url = url;
     }
     public void actionPerformed(ActionEvent evt) {
	 
	 if (!(this.url.startsWith("http:") || this.url.startsWith("file:"))) {
	     // If it's not a fully qualified URL, assume it's a file.
	     if (this.url.startsWith("/")) {
		 // Absolute path, so just prepend "file:"
		 this.url = "file:" + this.url;
	     }
	     else {
		 try {
		     // Assume it's relative to the starting point.
		     File f = new File(this.url);
		     this.url = f.toURL().toString();
		 }
		 catch (Exception e) {
		     this.url = "http://efg.cs.umb.edu/";
		 }
	     }
	 }
	 HelpFile help = new HelpFile(null,this.url);
	 help.show();
     }
 } // HelpListener

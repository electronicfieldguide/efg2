package project.efg.Import;
// Borrowed from java Swing second Edition's MiniBrowser.java
// Another implementation of a minimal browser.  This one is tracks mouseover
// events on hyperlinks and shows the destination of the link in a status bar
// at the bottom of the window.
//
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;


/**
 * HelpFile.java
 *
 *
 * Created: Thu Feb 23 17:39:02 2006
 *
 * @author <a href="mailto:">Jacob K Asiedu</a>
 * @version 1.0
 */
public class HelpFile extends JFrame{
 private JEditorPane jep;

  public HelpFile(String startingUrl) {
    // Ok, first just get a screen up and visible, with an appropriate
    // handler in place for the kill window command.
    super(startingUrl);
    setSize(400,300);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Now set up our basic screen components, the editor pane, the
    // text field for URLs, and the label for status and link information.
    JPanel urlPanel = new JPanel();
    urlPanel.setLayout(new BorderLayout());
    JTextField urlField = new JTextField(startingUrl);
    urlPanel.add(new JLabel("Site: "), BorderLayout.WEST);
    urlPanel.add(urlField, BorderLayout.CENTER);
    final JLabel statusBar = new JLabel(" ");

    // Here's the editor pane configuration.  It's important to make
    // the "setEditable(false)" call; otherwise, our hyperlinks won't
    // work.  (If the text is editable, then clicking on a hyperlink
    // simply means that you want to change the text, not follow the
    // link.)
    jep = new JEditorPane();
    jep.setEditable(false);

    try {
      jep.setPage(startingUrl);
    }
    catch(Exception e) {
      statusBar.setText("Could not open starting page.  Using a blank.");
    }
    JScrollPane jsp = new JScrollPane(jep); 

    // Get the GUI components onto our content pane.
    getContentPane().add(jsp, BorderLayout.CENTER);
    getContentPane().add(urlPanel, BorderLayout.NORTH);
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    // Last but not least, hook up our event handlers.
    urlField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        try {
          jep.setPage(ae.getActionCommand());
        }
        catch(Exception e) {
          statusBar.setText("Error: " + e.getMessage());
        }
      }
    });
    jep.addHyperlinkListener(new SimpleLinkListener(jep, urlField, 
                                                    statusBar));
  }

  public static void main(String args[]) {
    String url = "";
    if (args.length == 1) {
      url = args[0];
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
    }
    else {
      url = "http://efg.cs.umb.edu/";
    }
    new HelpFile(url).setVisible(true);
  }
  class SimpleLinkListener implements HyperlinkListener {
      private JEditorPane pane;       // The pane we're using to display HTML
      private JTextField  urlField;   // An optional text field for showing
      // the current URL being displayed
      private JLabel statusBar;       // An optional label for showing where
      // a link would take you
      
      public SimpleLinkListener(JEditorPane jep, JTextField jtf, JLabel jl) {
	  pane = jep;
	  urlField = jtf;
	  statusBar = jl;
      }
      
      public SimpleLinkListener(JEditorPane jep) {
	  this(jep, null, null);
      }
      
      public void hyperlinkUpdate(HyperlinkEvent he) {
	  HyperlinkEvent.EventType type = he.getEventType();
	  if (type == HyperlinkEvent.EventType.ENTERED) {
	      // Enter event.  Fill in the status bar.
	      if (statusBar != null) {
		  statusBar.setText(he.getURL().toString());
	      }
	  }
	  else if (type == HyperlinkEvent.EventType.EXITED) {
	      // Exit event.  Clear the status bar.
	      if (statusBar != null) {
		  statusBar.setText(" "); // Must be a space or it disappears
	      }
	  }
	  else if (type == HyperlinkEvent.EventType.ACTIVATED) {
	      // Jump event.  Get the URL, and, if it's not null, switch to that
	      // page in the main editor pane and update the "site url" label.
	      if (he instanceof HTMLFrameHyperlinkEvent) {
		  // Ahh, frame event; handle this separately.
		  HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)he;
		  HTMLDocument doc = (HTMLDocument)pane.getDocument();
		  doc.processHTMLFrameHyperlinkEvent(evt);
	      } else {
		  try {
		      pane.setPage(he.getURL());
		      if (urlField != null) {
			  urlField.setText(he.getURL().toString());
		      }
		  }
		  catch (FileNotFoundException fnfe) {
		      pane.setText("Could not open file: <tt>" + he.getURL() + 
				   "</tt>.<hr>");
		  }
		  catch (Exception e) {
		      e.printStackTrace();
		  }
	      }
	  }
      }
  }

} // HelpFile

package project.efg.Import;
import java.awt.event.*;
/**
 * HelpFileListener.java
 *
 *
 * Created: Sat Feb 25 14:12:43 2006
 *
 * @author <a href="mailto:">Jacob Admin</a>
 * @version 1.0
 */
public class AboutListener implements ActionListener{
    
    public void actionPerformed(ActionEvent evt) {
	AboutBox about = new AboutBox(null);
	about.show();
    }
}

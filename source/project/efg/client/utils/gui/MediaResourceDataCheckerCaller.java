/**
 * 
 */
package project.efg.client.utils.gui;

import javax.swing.JOptionPane;

import project.efg.client.utils.nogui.DataCheckerCaller;
import project.efg.client.utils.nogui.MediaResourceDataChecker;
import project.efg.util.utils.DBObject;

import com.Ostermiller.util.Browser;
/**
 * @author kasiedu
 * 
 */
public class MediaResourceDataCheckerCaller extends DataCheckerCaller {
	
	
	private MediaResourceDataChecker dataChecker;
	public MediaResourceDataCheckerCaller(DBObject dbObject, String ds) {
		super(dbObject,ds);
		this.dataChecker = new MediaResourceDataChecker(this.dbObject, this.displayName);
	}
	
	public void execute() {
		if (this.dataChecker.isReady()) {
			boolean bool = this.dataChecker.checkMediaResources();
			if (bool) {
				JOptionPane.showMessageDialog(null,
						"No errors found for data in Mediaresources field",
						"Success", JOptionPane.INFORMATION_MESSAGE);
			} else {
				 String fileName = this.dataChecker.displayErrors();
				 if(fileName == null){
						JOptionPane.showMessageDialog(null,
								"Application could not create error file ",
								"Error", JOptionPane.ERROR_MESSAGE);
				 }
				 else{
					 JOptionPane.showMessageDialog(null,
								"Application will open a browser window and display error results.\n " +
								"You can also view errors at \"" + fileName  + "\"",
								"Error", JOptionPane.INFORMATION_MESSAGE); 
					 try{
					 Browser.displayURL(fileName, "target");
					 }
					 catch(Exception ee){
						 JOptionPane.showMessageDialog(null,
									"Application could not find error file \"" + fileName  + "\"",
									"Error", JOptionPane.ERROR_MESSAGE);  
					 }
				 }
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Application could not check data for errors ",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}

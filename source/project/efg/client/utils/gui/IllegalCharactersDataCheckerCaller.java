/**
 * 
 */
package project.efg.client.utils.gui;

import javax.swing.JOptionPane;

import com.Ostermiller.util.Browser;

import project.efg.client.utils.nogui.DataCheckerCaller;
import project.efg.client.utils.nogui.IllegalCharactersDataChecker;
import project.efg.util.utils.DBObject;


/**
 * @author kasiedu
 * 
 */
public class IllegalCharactersDataCheckerCaller extends DataCheckerCaller {
	
	
	
	private IllegalCharactersDataChecker dataChecker;
	
	public IllegalCharactersDataCheckerCaller(DBObject dbObject, String ds) {
		super(dbObject,ds);
		this.dataChecker = new IllegalCharactersDataChecker(this.dbObject, this.displayName);
		
		
	}
	public void execute() {
		try{
			
		if (this.dataChecker.isReady()) {
		
			boolean bool = this.dataChecker.checkIllegalCharacters();
			if (bool) {
				JOptionPane.showMessageDialog(null,
						"No Illegal Characters found in data source",
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
								"You can also view errors at '" + fileName  + "'",
								"Error", JOptionPane.INFORMATION_MESSAGE); 
					 try{
						 Browser.displayURL(fileName, "target");
					 }
					 catch(Exception ee){
						 JOptionPane.showMessageDialog(null,
									"Application could not find error file '" + fileName  + "'",
									"Error", JOptionPane.ERROR_MESSAGE);  
					 }
				 }
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Application could not check data for errors ",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}

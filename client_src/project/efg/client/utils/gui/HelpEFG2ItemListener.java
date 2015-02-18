/**
 * 
 */
package project.efg.client.utils.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import org.apache.log4j.Logger;

import project.efg.util.interfaces.EFGImportConstants;

import com.Ostermiller.util.Browser;

/**
 * @author kasiedu
 * 
 */
public class HelpEFG2ItemListener implements ActionListener {
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(HelpEFG2ItemListener.class);
			Browser.init();
		} catch (Exception ee) {
		}
	}

	private String helpFile;
	/**
	 * 
	 */
	private void handleInput() {
		try {
			URL helpURL = this.getClass().getResource(this.helpFile);
			if (helpURL == null) {
				log.error("Couldn't open help file: "
						+ EFGImportConstants.MAIN_DEPLOY_HELP);
				return;
			}
			Browser.displayURL(helpURL.getFile(), "target");
		} catch (Exception ee) {
			log.error(ee.getMessage());
		}
	}

	/**
	 * 
	 */
	public HelpEFG2ItemListener(String helpFile) {
		this.helpFile = helpFile;
	}

	public void actionPerformed(ActionEvent e) {
		this.handleInput();
	}

}

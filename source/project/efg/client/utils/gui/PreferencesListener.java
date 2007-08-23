/**
 * 
 */
package project.efg.client.utils.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PreferencesListener implements ActionListener {
	private JFrame frame;
	private boolean showResourceLocator;
	private boolean showChangeThumbDimensions;
	/**
	 * @param showResourceLocator
	 * @param showChangeThumbDimensions 
	 * @param menu
	 */
	public PreferencesListener(JFrame frame, boolean showResourceLocator, boolean showChangeThumbDimensions) {
		this.frame = frame;
		this.showResourceLocator = showResourceLocator;
		this.showChangeThumbDimensions= showChangeThumbDimensions;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		PreferencesHandler preferences = 
			new PreferencesHandler(this.frame,"Preferences",
					true, 
					this.showResourceLocator,
					this.showChangeThumbDimensions);
		preferences.setVisible(true);
	}

}
/**
 * 
 */
package project.efg.client.drivers.gui;

import javax.swing.JFrame;

import project.efg.client.impl.gui.AboutBox;

/**
 * @author kasiedu
 *
 */
public class AboutDriver {
	public static void main(String[] args) {
		AboutBox about = new AboutBox(new JFrame());
		about.setVisible(true);
	}
}

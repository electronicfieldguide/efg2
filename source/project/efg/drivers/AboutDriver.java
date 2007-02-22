/**
 * 
 */
package project.efg.drivers;

import javax.swing.JFrame;

import project.efg.Imports.efgImpl.AboutBox;

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

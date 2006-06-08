/**
 * AboutBox.java
 *$Id$
 *
 * Created: Wed Sep 01 08:20:43 2004
 *Part of efg Java application utilities
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob K. Asiedu</a>
 * @version 1.0
 */
package project.efg.Import;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class AboutBox extends JDialog implements EFGToolsUtils {

    public AboutBox(Frame owner) {
	super(owner,  ABOUT_MESSAGE , true);
	
	JLabel lbl = new JLabel(new ImageIcon(ABOUT_ICON));
	JPanel p = new JPanel();
	Border b1 = new BevelBorder(BevelBorder.LOWERED);
	Border b2 = new EmptyBorder(5, 5, 5, 5);
	lbl.setBorder(new CompoundBorder(b1, b2));
	p.add(lbl);
	getContentPane().add(p, BorderLayout.NORTH);
	
	JTextArea txt = new JTextArea(COPYRIGHT_MESSAGE);
	txt.setBorder(new EmptyBorder(5, 10, 5, 10));
	txt.setFont(new Font("Helvetica", Font.BOLD, 12));
	txt.setEditable(false);
	txt.setBackground(getBackground());
	
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(txt);
	

	txt = new JTextArea(JAVA_MESSAGE);
	txt.setBorder(new EmptyBorder(5, 10, 5, 10));
	txt.setFont(new Font("Arial", Font.PLAIN, 12));
	txt.setEditable(false);
	txt.setLineWrap(true);
	txt.setWrapStyleWord(true);
	txt.setBackground(getBackground());
	p.add(txt);
	

	txt = new JTextArea(EFG_CONTACT_MESSAGE);
	txt.setBorder(new EmptyBorder(5, 10, 5, 10));
	txt.setFont(new Font("Arial", Font.PLAIN, 12));
	txt.setEditable(false);
	txt.setLineWrap(true);
	txt.setWrapStyleWord(true);
	txt.setBackground(getBackground());
	p.add(txt);
	
	getContentPane().add(p, BorderLayout.CENTER);		
	p = new JPanel();
		
	final JButton btOK = new JButton("OK");
	ActionListener lst = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    };
	btOK.addActionListener(lst);
	p = new JPanel();
	p.add(btOK,BorderLayout.SOUTH);
	getRootPane().setDefaultButton(btOK);
	getRootPane().registerKeyboardAction(lst,
					     KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
					     JComponent.WHEN_IN_FOCUSED_WINDOW);
	getContentPane().add(p, BorderLayout.SOUTH);
	
	// That will transer focus from first component upon dialog's show
	WindowListener wl = new WindowAdapter() {
		public void windowOpened(WindowEvent e) {
		    btOK.requestFocus();
		}
	    };
	addWindowListener(wl);
	
	pack();
	setResizable(false);
	setLocationRelativeTo(owner);
    }
}
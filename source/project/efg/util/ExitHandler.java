/**
 * 
 */
package project.efg.util;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

/**
 * @author kasiedu
 *
 */
public class ExitHandler {
	/**
	 * 
	 */
	public ExitHandler() {

	}
	/**
	 * 
	 */
	public JCheckBox getCheckbox(
            boolean isSelected) {
		
		JCheckBox checkBox = new JCheckBox("Confirm Exit Prompt When Closing Window");
		checkBox.setSelected(isSelected);
		//find out whether a value is written for it.


		
		checkBox.addItemListener(new ItemStateHandler(checkBox));
		return checkBox;
	}
	class ItemStateHandler implements ItemListener{
		private JCheckBox checkBox;
	
		ItemStateHandler(JCheckBox checkBox){
			this.checkBox = checkBox;
			
		}
		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {

			 EFGImportConstants.EFGProperties.setProperty("efg.showdismiss.checked",
					 this.checkBox.isSelected() + "");
		}		
	}
	public static void main(String[] args) {
		//ExitHandler exit = new ExitHandler();
	}
}

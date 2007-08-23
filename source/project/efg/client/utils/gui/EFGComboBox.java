/**
 * 
 */
package project.efg.client.utils.gui;

import javax.swing.JComboBox;

import org.apache.log4j.Logger;

/**
 * @author kasiedu
 *
 */
public class EFGComboBox extends JComboBox {
	/**
	 * 
	 */
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(EFGComboBox.class);
		} catch (Exception ee) {
		}
	}
	public static final String DELETE_CMD = "delete";
	public String getCurrentSelection(){
		return (String)this.getSelectedItem();
	}
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;
		public static final int MAX_MEM_LEN = 30;

		public EFGComboBox() {
			super();
			setEditable(true);
			
		//this.setPrototypeDisplayValue(new String("xxxx"));
		this.setToolTipText("Enter or select max dimension in pixels or "+ 
		        "select one from the list:");
		
			}

		public void add(String item) {
			removeCurrent(item);
			insertItemAt(item, 0);
			setSelectedItem(item);
			if (getItemCount() > MAX_MEM_LEN){
				removeItemAt(getItemCount()-1);
			}
			
		}
		public void removeCurrent(String item){
			int toremove = -1;
			for(int index = 0; index < this.getItemCount();index++) {
				String current = (String)this.getItemAt(index);
				if(item.equalsIgnoreCase(current)) {
					toremove = index;
				}
			}
			if(toremove > -1) {
				this.remove(toremove);
			}
		}
		public void remove(String item){
		
			removeItem(item);
			setSelectedIndex(0);
		}


	

	
	}
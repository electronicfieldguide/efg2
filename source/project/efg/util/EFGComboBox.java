/**
 * 
 */
package project.efg.util;

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
			removeItem(item);
			insertItemAt(item, 0);
			setSelectedItem(item);
			if (getItemCount() > MAX_MEM_LEN){
				removeItemAt(getItemCount()-1);
			}
			
		}
		public void remove(String item){
			removeItem(item);
			setSelectedIndex(0);
		}


		/**
		 * 
		 */
		private void useDefaults() {
			String[] dimensions = WorkspaceResources.getDefaultDimensions();
			for(int i= dimensions.length;i > 0;i--) {
				this.add(dimensions[i-1]);
			}
		}

	
	}
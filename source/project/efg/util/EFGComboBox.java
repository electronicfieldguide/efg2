/**
 * 
 */
package project.efg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.DefaultComboBoxModel;
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
			
		this.setPrototypeDisplayValue(new String("xxxx"));
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
		public void deserialize(String fName) {
			try {
				if (getItemCount() > 0){
					removeAllItems();
				}
				File f = null;
				
				
				f= new File(fName);
				if (!f.exists()){
					this.serializeFile(fName);
					useDefaults();
					return;
				}
				URL propsURL = 
					this.getClass().getResource("/" + fName);
			
				String dir = URLDecoder.decode(propsURL.getFile(),"UTF-8");
		
				ObjectInputStream in = 
					new ObjectInputStream(new FileInputStream(dir));
				Object obj = in.readObject();
				
				if (obj instanceof DefaultComboBoxModel)
					
					setModel((DefaultComboBoxModel)obj);
				in.close();
				
				this.setSelectedIndex(0);
			}
			catch (Exception e) {
				e.printStackTrace();
				log.error("Serialization error: "+e.toString());
			}
		}

		/**
		 * 
		 */
		private void useDefaults() {
			int[] dimensions = DnDFileBrowserMain.getDefaultDimensions();
			for(int i=0; i < dimensions.length;i++) {
				this.add(dimensions[i] + "");
			}
			if(dimensions.length > 1) {
				System.out.println("Selected set to: :" + dimensions[1] );
				this.setSelectedItem(dimensions[1] + "");
			}
		}
		private void serializeFile(String fname){
			ObjectOutputStream stream = null;
			try {
				

			stream = new ObjectOutputStream(new FileOutputStream(fname));
			if(this.getSelectedIndex() > 0){//last selected item should always show up on top
				String selectedItem = (String)this.getSelectedItem();
				this.add(selectedItem);
			}
			if(this.getItemCount() == 0){
				/*DnDFileBrowserMain.maxDim = 0;
				int maxDim = DnDFileBrowserMain.getMaxDim();
				this.add(maxDim+"");*/
				this.useDefaults();
			}
			DefaultComboBoxModel model = (DefaultComboBoxModel)this.getModel();
		
			stream.writeObject(model);
			stream.close();
			}
		catch (Exception e) {
			e.printStackTrace();
			log.error("Serialization error: "+e.toString());
		}
		finally{
			try{
				if(stream != null){
					stream.close();
				}
			}catch(Exception ee){
				
			}
		}
		}
		public void serialize(String fName) {
			try {
					URL propsURL = 
						this.getClass().getResource("/" + fName);
					String dir = URLDecoder.decode(propsURL.getFile(),"UTF-8");
					
					this.serializeFile(dir);
				
			}
			catch (Exception e) {
				e.printStackTrace();
				log.error("Serialization error: "+e.toString());
			}
		}
	
	}


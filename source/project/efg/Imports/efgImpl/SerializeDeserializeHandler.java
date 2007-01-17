/**
 * 
 */
package project.efg.Imports.efgImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

/**
 * @author kasiedu
 *
 */
public 	class SerializeDeserializeHandler{
	static Logger log = null;
	static {
		try {
			log = Logger.getLogger(SerializeDeserializeHandler.class);
			
		} catch (Exception ee) {
		}
	}
	/**
	 * 
	 */
	public SerializeDeserializeHandler() {
		
	}

	public JCheckBox getCheckBox(String check_box_1_fname, String buttonLabel) {
		File f= new File(check_box_1_fname);
		
		if (!f.exists()){
			return new JCheckBox(buttonLabel);
		}
		URL propsURL = 
			this.getClass().getResource("/" +check_box_1_fname);
	
		try {
			String dir = URLDecoder.decode(propsURL.getFile(),"UTF-8");

			ObjectInputStream in = 
				new ObjectInputStream(new FileInputStream(dir));
			Object obj = in.readObject();
			
			JCheckBox checkBox = (JCheckBox)obj;
			
			in.close();
			return checkBox;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public JRadioButton getRadioButton(String fname, String buttonLabel) {
		File f= new File(fname);
		JRadioButton radioButton = null;
		if(!f.exists()) {
			radioButton =  new JRadioButton(buttonLabel);
			radioButton.setSelected(true);
		}
		else {
		try {
			ObjectInputStream in = 
				new ObjectInputStream(new FileInputStream(f));
			Object obj = in.readObject();
			
			radioButton = (JRadioButton)obj;
			
			in.close();
			return radioButton;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		return radioButton;
	}
	public void serializeCheckBox(JCheckBox checkBox,String fname) {
		ObjectOutputStream stream = null;
		try {
			URL propsURL = null;
			File file = new File(fname);
			if(!file.exists()) {
				file.createNewFile();
			}
			stream = new ObjectOutputStream(new FileOutputStream(file));	
			stream.writeObject(checkBox);
			stream.close();
		}
		catch (Exception ex) {
			log.error("Serialization error: "+ex.toString());
			ex.printStackTrace();
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
	public void serializeButton(JRadioButton radio,String fname) {
		ObjectOutputStream stream = null;
		try {
			URL propsURL = null;
			File file = new File(fname);
			if(!file.exists()) {
				file.createNewFile();
			}
			stream = new ObjectOutputStream(new FileOutputStream(file));	
			stream.writeObject(radio);
			stream.close();
		}
		catch (Exception ex) {
			log.error("Serialization error: "+ex.toString());
			ex.printStackTrace();
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
}
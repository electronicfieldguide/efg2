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

import javax.swing.JCheckBox;

/**
 * @author jacob.asiedu
 *
 */
public class EFGCheckBoxManager {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EFGCheckBoxManager(){
		
	}
	
	public JCheckBox deserialize(String fName) {
		try {

			File f = null;

			f = new File(fName);
			if (!f.exists()) {
				JCheckBox ckBox = new JCheckBox("Don't ask me again..");
				return serialize(fName, ckBox);
			}
			URL propsURL = this.getClass().getResource("/" + fName);

			String dir = URLDecoder.decode(propsURL.getFile(), "UTF-8");

			ObjectInputStream in = 
				new ObjectInputStream(new FileInputStream(
					dir));
			Object obj = in.readObject();

			in.close();
			return (JCheckBox) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private JCheckBox serializeFile(String fname, JCheckBox checkBox) {
		ObjectOutputStream stream = null;
		try {

			stream = new ObjectOutputStream(
					new FileOutputStream(fname));
			stream.writeObject(checkBox);
			stream.close();
			return checkBox;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (Exception ee) {

			}
		}
		return null;
	}

	public JCheckBox serialize(String fName, JCheckBox checkBox) {
		try {
			File f = null;

			f = new File(fName);
			if (!f.exists()) {
				JCheckBox ckBox = new JCheckBox("Don't ask me again..");
				return this.serializeFile(fName, ckBox);
			}

			URL propsURL =this.getClass().getResource("/" + fName);
			String fileName = URLDecoder.decode(propsURL.getFile(), "UTF-8");

			return this.serializeFile(fileName, checkBox);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
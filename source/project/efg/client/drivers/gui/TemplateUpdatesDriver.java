/**
 * 
 */
package project.efg.client.drivers.gui;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import project.efg.client.drivers.nogui.TemplateLoader;
import project.efg.util.factory.SpringFactory;
import project.efg.util.factory.TemplateModelFactory;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;
import project.efg.util.utils.TemplateModelHandler;


/**
 * @author kasiedu
 *
 */
public class TemplateUpdatesDriver {
/**
 * 
 */
protected static void updateDB(DBObject dbObject) {
	StringBuffer  buffer = new StringBuffer("SELECT ");
	buffer.append(EFGImportConstants.QUERY_STR);
	buffer.append(" FROM ");
	buffer.append(EFGImportConstants.TEMPLATE_TABLE);
	try {
		TemplateModelHandler temp = 
			TemplateModelFactory.createImportTemplateHandler(dbObject);
		List list = temp.executeQueryForList(buffer.toString(), 1);
		if(list != null && list.size() > 0){
			
			return;
		}
	} catch (Exception e1) {//HACK //FIXME
		
	}
	
	
		 Properties map= new Properties();
     try {
    	 ClassLoader cl = ClassLoader.getSystemClassLoader();

    	 URL url = cl.getResource("old2newnames.properties");
    	 if (url == null)
    	 {
    		 throw new Exception("Could not find file: old2newnames.properties");
    	 }

    	 InputStream rf = url.openStream();
    	 if (rf == null)
    	 {
    		 throw new Exception("Could not open an input stream");
    	 }
    	 map.load(rf);
	} catch (Exception e) {
		
		e.printStackTrace();
	}
	
	buffer = new StringBuffer();
	buffer.append(EFGImportConstants.EFGProperties.getProperty("efg.templates.home.current"));
	StringBuffer templateBuffer = new StringBuffer(buffer.toString());
	

	TemplateLoader tloader = new TemplateLoader(dbObject);
	
	tloader.loadTemplateFilesIntoDB(new File(templateBuffer.toString()), 
			SpringFactory.getFileNameFilter(), map);
	
}



	public static void main(String[] args) {
		//cause properties to load
		
		
		if(args.length == 2 ){
			Properties properties = EFGImportConstants.EFGProperties;
			String userName = args[0];
			String password = args[1];
			
			String urldb = 
				EFGImportConstants.EFGProperties.getProperty("dburl");
			DBObject dbObject = new DBObject(
					urldb,userName,
					password);
			TemplateUpdatesDriver.updateDB(dbObject);
			
		}
		else{
			JOptionPane.showMessageDialog(null, 
					"Database updates failed. Please re-install application",
					"Database update failure", JOptionPane.ERROR_MESSAGE);
		}
	}
}

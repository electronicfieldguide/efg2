/**
 * 
 */
package project.efg.util.pdf.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.interfaces.RegularExpresionConstants;

import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;

/**
 * @author jacob.asiedu
 *
 */
public class PDFConstants {
	static public Map alignMap;
	static public Map paperSizes;

	static public Map orientationMap;
	
	static {
		loadPaperSizeProperties();
		loadAlignmentProperties();
	}
	public PDFConstants(){
		
	}
	/**
	 * @param property
	 * @param papersizesuffix
	 * @return
	 */
	private static String[] parseEFG(String property, String separator) {
		String[] splits = property.split(separator);
		
		return  splits;
		
	}
	private static void loadPaperSizeProperties(){
		paperSizes = new HashMap();
		Properties props = EFGImportConstants.EFGProperties;
		String papersizesuffix = props.getProperty("papersize_suffix");
		Enumeration enum2 = props.propertyNames();
		
		while(enum2.hasMoreElements()){
			String property = (String)enum2.nextElement();
			if(property.endsWith(papersizesuffix)){
				String[] key = parseEFG(property,papersizesuffix);
				String[] val = parseEFG(props.getProperty(property), 
						RegularExpresionConstants.COMMASEP);
				
				paperSizes.put(key[0], 
						new Rectangle(Integer.parseInt(val[0]),
								Integer.parseInt(val[1])));
				
			}
		}
		
	}
	private static void loadAlignmentProperties(){
		
		orientationMap = new HashMap();
		orientationMap.put("portrait",new Boolean("true"));
		orientationMap.put("landscape",new Boolean("false"));
		alignMap = new HashMap();
		alignMap.put("left", new Integer(Element.ALIGN_LEFT));
		alignMap.put("right",new Integer(Element.ALIGN_RIGHT));
		alignMap.put("center", new Integer(Element.ALIGN_CENTER));
		alignMap.put("top", new Integer(Element.ALIGN_TOP));
		alignMap.put("bottom", new Integer(Element.ALIGN_BOTTOM));	
		alignMap.put("middle", new Integer(Element.ALIGN_MIDDLE));	
		alignMap.put("justified", new Integer(Element.ALIGN_JUSTIFIED));
		
		
	}
	public static Map fontFamilyMap;

	static{//read from fontmetrics
		PDFConstants.fontFamilyMap = FontHandler.LoadFonts();
	}

	public static StringBuffer errorBuffer;

	static{
		String errorProps =EFGImportConstants.EFGProperties.getProperty("pdferrormessage");
		if(errorProps != null && !errorProps.trim().equals("")){
			PDFConstants.errorBuffer= new StringBuffer(); 
	
			 String[] props = errorProps.split(RegularExpresionConstants.PIPESEP);
			 for(int i = 0; i < props.length;i++){
				 String prop = props[i];
				 if(prop.trim().equals("")){
					 PDFConstants.errorBuffer.append("\n");
				 }
				 else{
					 PDFConstants.errorBuffer.append(prop);
				 }
				 PDFConstants.errorBuffer.append("\n");
			 }
		}
		else{
		PDFConstants.errorBuffer= new StringBuffer("Error occured during processing of document\n"); 
		PDFConstants.errorBuffer.append("---------\n");
		PDFConstants.errorBuffer.append("Possible Causes:\n\n");
	
		PDFConstants.errorBuffer.append("- Images are missing. If you intended to print a file with no images,\n");
		PDFConstants.errorBuffer.append("please go back and make sure the box next to \n");
		PDFConstants.errorBuffer.append("'Always display captions' in the Data Display Settings is checked.\n\n");
	
		PDFConstants.errorBuffer.append("- some other error that causes this page to " +
				"get generated that we do not know about, please report this to us\n");
		}
	}

}

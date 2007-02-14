/**
 * 
 */
package project.efg.efgpdf.pdf;

import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Font;

/**
 * @author jacob.asiedu
 *
 */
public class FontHandler {

	/**
	 * Load from font metrics
	 * @return
	 */
	public static Map LoadFonts() {
		Map fontFamilyMap = new HashMap();
		fontFamilyMap.put("times", Font.TIMES_ROMAN +"");
		fontFamilyMap.put("helvetica",Font.HELVETICA+"");
		fontFamilyMap.put("courier",Font.COURIER+"");
	
		return fontFamilyMap;
	}

}

/**
 * 
 */
package project.efg.util.pdf.utils;

import project.efg.util.pdf.interfaces.EFG2PDFConstants;

import com.lowagie.text.Font;

/**
 * @author jacob.asiedu
 *
 */

public class FontObject {
	private int fontName = EFG2PDFConstants.DEFAULT_FONTNAME;

	private int fontsize = EFG2PDFConstants.DEFAULT_FONTSIZE;

	private int fontType = EFG2PDFConstants.DEFAULT_FONTTYPE;

	private boolean isBold = EFG2PDFConstants.DEFAULT_ISBOLD;

	private boolean isItalics = EFG2PDFConstants.DEFAULT_ISITALICS;

	public FontObject() {

	}

	/**
	 * 
	 * @return
	 */
	public int getFontName() {
		return fontName;
	}

	/**
	 * 
	 * @param fontName
	 */
	public void setFontName(int fontName) {
		this.fontName = fontName;
	}

	/**
	 * 
	 * @return
	 */
	public int getFontsize() {
		return fontsize;
	}

	/**
	 * 
	 * @param fontsize
	 */
	public void setFontSize(int fontsize) {
		this.fontsize = fontsize;
	}

	/**
	 * 
	 * @return
	 */
	public int getFontType() {
		return fontType;
	}

	/**
	 * 
	 * @param isBold
	 */
	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}

	/**
	 * 
	 * @param isItalics
	 */
	public void setItalics(boolean isItalics) {
		this.isItalics = isItalics;
	}

	/**
	 * 
	 * @param fontType
	 */
	public void setFontType(int fontType) {
		this.fontType = fontType;
	}

	/**
	 * 
	 * @return
	 */
	public Font createFont() {
		if (this.isBold && this.isItalics) {
			this.setFontType(Font.BOLDITALIC);
		} else if (isBold) {
			this.setFontType(Font.BOLD);
		} else if (isItalics) {
			this.setFontType(Font.ITALIC);
		}

		return new Font(this.getFontName(), this.getFontsize(), this
				.getFontType());
	}
}

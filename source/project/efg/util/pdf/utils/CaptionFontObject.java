/**
 * 
 */
package project.efg.util.pdf.utils;

import java.util.ArrayList;
import java.util.List;

import project.efg.util.pdf.interfaces.EFG2PDFConstants;
import project.efg.util.pdf.interfaces.EFGRankObject;

import com.lowagie.text.Element;
import com.lowagie.text.Font;

/**
 * @author jacob.asiedu
 *
 */
public class CaptionFontObject extends EFGRankObject {

	private String caption;
	private int align;
	private boolean isUnderLine = EFG2PDFConstants.DEFAULT_ISUNDERLINE;

	private List states;

	private FontObject fontObject;


	public String toString(){
		return this.caption;
	}
	public void setAlignment(String align) {
		Integer al =(Integer)PDFConstants.alignMap.get(align.toLowerCase());
		if(al != null){
			this.align = al.intValue();
		}
	}
	public int getAlignment(){
		return this.align;
	}
	/**
	 * 
	 * 
	 */
	public CaptionFontObject() {
		super();
		this.fontObject = new FontObject();
		this.states = new ArrayList();
		this.align=Element.ALIGN_LEFT;
	}
	/**
	 * 
	 * @param font
	 */
	public void setFontObject(FontObject font) {
		this.fontObject = font;

	}

	/**
	 * 
	 * @param fontName
	 */
	public void setFontName(int fontName) {
		this.fontObject.setFontName(fontName);
	}

	/**
	 * 
	 * @param fontsize
	 */
	public void setFontSize(int fontsize) {
		this.fontObject.setFontSize(fontsize);
	}

	public void setFontBold(boolean isBold) {
		this.fontObject.setBold(isBold);
	}

	/**
	 * 
	 * @param isItalics
	 */
	public void setFontItalics(boolean isItalics) {
		this.fontObject.setItalics(isItalics);
	}

	/**
	 * 
	 * @return
	 */
	public int getFontSize() {
		return this.fontObject.getFontsize();
	}

	public int getFontType() {
		return this.fontObject.getFontType();
	}

	/**
	 * 
	 * @param fontType
	 */
	public void setFontType(int fontType) {
		this.fontObject.setFontType(fontType);
	}

	/**
	 * 
	 * @return
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * 
	 * @return
	 */
	public Font getFont() {
		return this.fontObject.createFont();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isUnderLine() {
		return this.isUnderLine;
	}

	/**
	 * 
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 
	 * @param isUnderLine
	 */
	public void setUnderLine(boolean isUnderLine) {
		this.isUnderLine = isUnderLine;
	}

	/**
	 * 
	 */
	public boolean equals(Object o) {
		CaptionFontObject co = (CaptionFontObject) o;
		return this.getCaption().equals(co.getCaption());
	}

	/**
	 * 
	 */
	public int hashCode() {
		return this.getCaption().hashCode();
	}

	/**
	 * 
	 * @param state
	 */
	public void addState(String state) {
		this.states.add(state);
	}

	/**
	 * 
	 * @return
	 */
	public List getStates() {
		return this.states;
	}


}

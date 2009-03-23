/**
 * 
 */
package project.efg.util.pdf.utils;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.pdf.bookmaker.PDFMaker;
import project.efg.util.pdf.interfaces.EFG2PDFConstants;

import com.lowagie.text.Document;

/**
 * @author jacob.asiedu
 *Adds Metadata information to a generated pdf file
 */
public class EFG2PDFDocumentMetadata {
	private static int left_margin = 
		EFG2PDFConstants.DEFAULT_LEFT_MARGIN;

	static int right_margin=
		EFG2PDFConstants.DEFAULT_RIGHT_MARGIN;

	static int top_margin= 
		EFG2PDFConstants.DEFAULT_TOP_MARGIN;

	static int bottom_margin = 
		EFG2PDFConstants.DEFAULT_BOTTOM_MARGIN;
	

	private static String zero="0";

	private static String expires="Expires";
	
	static {
		initProperties();
	}
	static void initProperties(){
		
		String property =
			EFGImportConstants.EFGProperties.getProperty(
			"pdf.left.margin");
		if(property != null && 
				!property.trim().equals("")){
			left_margin = 
				Integer.parseInt(property);
		}
	
		property =
			EFGImportConstants.EFGProperties.getProperty(
			"pdf.right.margin");
		if(property != null && 
				!property.trim().equals("")){
			right_margin = 
				Integer.parseInt(property);
		}

		property =
			EFGImportConstants.EFGProperties.getProperty(
			"pdf.top.margin");
		if(property != null && 
				!property.trim().equals("")){
			top_margin = 
				Integer.parseInt(property);
		}

		property =
			EFGImportConstants.EFGProperties.getProperty(
			"pdf.bottom.margin");
		if(property != null && 
				!property.trim().equals("")){
			 bottom_margin = 
				Integer.parseInt(property);
		}
	}
	public static Document createDocumentMetadata(
			PDFMaker pdfMaker, 
			String creator,
			String authors 
			){
		String mutex = "";
		synchronized (mutex) {
			Document document = new Document(
					pdfMaker.getPaperSize(),
					left_margin,
					right_margin,
					top_margin,
					bottom_margin);
			String title = "";
			
			if(pdfMaker.getMainTitle() != null){
				title = pdfMaker.getMainTitle().getCaption();
				if(title == null){
					title = "";
				}
			}
			if(pdfMaker.getMainTitle() != null){
				title = pdfMaker.getMainTitle().getCaption();
				if(title == null){
					title = "";
				}
			}
			document.addTitle(title);
			document.addSubject(title);
			document.addKeywords(addKeyWords(pdfMaker));
			document.addCreator(creator);
			document.addCreationDate();
			if(authors == null){
				authors = "";
			}
			document.addAuthor(authors);
			document.addHeader(expires, zero);
			return document;
		}
		
	}
	/**
	 * @param pdfMaker
	 * @return
	 */
	private static String addKeyWords(PDFMaker pdfMaker) {
		StringBuilder keywordsBuffer = new StringBuilder();
		Set set = new TreeSet(new EFGRankObjectSortingCriteria());
		
		Set captions = pdfMaker.getCaptionsAbove();
		if(captions != null && captions.size() > 0){
			set.addAll(captions);
			
		}
		captions = pdfMaker.getCaptionsBelow();
		if(captions != null && captions.size() > 0){
			set.addAll(captions);			
		}
		if(set.size() > 0){
			addSet(set,keywordsBuffer);
		}
		return keywordsBuffer.toString();
	}
	/**
	 * @param captions
	 * @param keywordsBuffer
	 */
	private static void addSet(Set captions, StringBuilder keywordsBuffer) {
		for (Iterator iter = captions.iterator(); iter.hasNext();) {
			CaptionFontObject element = (CaptionFontObject) iter.next();
			String keyword = element.getCaption();
			if(keyword != null && !keyword.trim().equals("")){
				keywordsBuffer.append(keyword);
				keywordsBuffer.append(",");
			}
		}
	}

}

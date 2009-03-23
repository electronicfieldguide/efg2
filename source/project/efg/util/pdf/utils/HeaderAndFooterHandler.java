package project.efg.util.pdf.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.pdf.bookmaker.PDFMaker;
import project.efg.util.pdf.interfaces.EFG2PDFConstants;



import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class HeaderAndFooterHandler {
	static Logger log;
	static {
		try {
			log = Logger.getLogger(HeaderAndFooterHandler.class);
		} catch (Exception ee) {
		}
	}
	private PDFMaker pdfMaker;
	
	private boolean isHeader;
	private boolean isFooter;
	private boolean isFooterImage;
	static float isUnderline_header_thickness = 
		EFG2PDFConstants.DEFAULT_UNDERLINE_THICKNESS;
	static float isUnderline_header_y_position= 
		EFG2PDFConstants.DEFAULT_UNDERLINE_Y_POSITION;
	
	static float isUnderline_footer_thickness = 
		EFG2PDFConstants.DEFAULT_UNDERLINE_FOOTER_THICKNESS;
	static float isUnderline_footer_y_position= 
		EFG2PDFConstants.DEFAULT_UNDERLINE_FOOTER_Y_POSITION;
	static {
		initProperties();
	}
	static void initProperties(){
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"pdf.caption.underline_footer_thickness");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_footer_thickness = 
				Float.parseFloat(property);
		}
		property= 
			EFGImportConstants.EFGProperties.getProperty(
					"pdf.caption.underline_footer_y_position");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_footer_y_position = 
				Float.parseFloat(property);
		}
		property= 
			EFGImportConstants.EFGProperties.getProperty(
					"pdf.caption.underline_header_thickness");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_header_thickness = 
				Float.parseFloat(property);
		}
		property= 
			EFGImportConstants.EFGProperties.getProperty(
					"pdf.caption.underline_header_y_position");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_header_y_position = 
				Float.parseFloat(property);
		}
	}
	private File imagesDirectory;
	public HeaderAndFooterHandler(PDFMaker pdfMaker,
			File imagesDirectory){
		this.pdfMaker = pdfMaker;
		this.imagesDirectory = imagesDirectory;
		this.isHeader = true;
		this.isFooter =true;
		this.isFooterImage = false;
		this.computeHeaderFooter();
	}
	public boolean isFooterImage() {
		return isFooterImage;
	}
	private void computeHeaderFooter() {
		String caption = this.pdfMaker.getMainTitle().getCaption();

		
		if(caption == null || caption.trim().equals("")){
			caption = this.pdfMaker.getSubTitle().getCaption();
		}
		CaptionFontObject c1 = this.pdfMaker.getCopyRight1(); 
		CaptionFontObject c2 = this.pdfMaker.getCopyRight2(); 
		if(c1 == null
				&& c2 == null){
			isFooter = false;
			
		}
		else if((
				c1.getCaption() == null || c1.getCaption().trim().equals(""))&& 
				(c2.getCaption() == null || 
						c2.getCaption().trim().equals(""))){
			isFooter = false;
			
		}
		else if(c1.getCaption() == null || c1.getCaption().trim().equals("")){
			isFooter = true;
		
		}
		else if(c2.getCaption() == null || c2.getCaption().trim().equals("")){
			isFooter = true;
		}
		if(this.pdfMaker.getFooterImagesList().size() > 0){
			this.isFooterImage = true;
		}
		if(caption == null || caption.trim().equals("")){
			isHeader = false;
		}
		
		
		
	}
	public boolean isHeader(){
		return this.isHeader;
	}
	public boolean isFooter(){
		return this.isFooter;
	}
	public void addHeaders(PdfPTable datatable,
			EFG2PDFCellCalculations cellCalculus,float availableWidth){	

		if(!isHeader()){		
			return;
		}
		Phrase ph = null;
		if(this.pdfMaker.getMainTitle() != null){	
			String title = this.pdfMaker.getMainTitle().getCaption();
			String titleCaption = 
				cellCalculus.truncateText(
						this.pdfMaker.getMainTitle(),
						title,
						availableWidth);
			// The header starts with a cell that spans 10 columns
			Chunk ck = new Chunk(
					titleCaption, 
					this.pdfMaker.getMainTitle().getFont());
			if(this.pdfMaker.getMainTitle().isUnderLine()){
				ck.setUnderline(isUnderline_header_thickness,
						isUnderline_header_y_position);
			}
			ph = new Phrase(ck);
				
			ph.add(Chunk.NEWLINE);
			ph.add(Chunk.NEWLINE);
		}
		
		if(this.pdfMaker.getSubTitle() != null){
			String subtitle = this.pdfMaker.getSubTitle().getCaption();
			if(subtitle != null && !subtitle.trim().equals("")){
			String titleCaption = 
					cellCalculus.truncateText(
							this.pdfMaker.getSubTitle(),
							subtitle,
							availableWidth);
			Chunk ck = new Chunk(
						titleCaption,
						this.pdfMaker.getSubTitle().getFont());
				if(this.pdfMaker.getSubTitle().isUnderLine()){
					ck.setUnderline(isUnderline_header_thickness,
							isUnderline_header_y_position);
				}
				if(ph == null){
					ph = new Phrase(ck);
				}
				else{
					ph.add(new Phrase(ck));
				}
			}
		}
		PdfPCell headerCell = new PdfPCell(ph);
		headerCell.setBorder(0);
		headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		//set the size of the headerCell
		headerCell.setFixedHeight(cellCalculus.getHeaderTextHeight());
		headerCell.setColspan(this.pdfMaker.getNumberColumns());
		datatable.addCell(headerCell);	
	}

	public void addFooters(PdfPTable datatable,
			EFG2PDFCellCalculations cellCalculus){	
		if(!isFooter()){
			return;
		}
			// footer
		PdfPCell p = this.getFooter(cellCalculus);
		if(p != null){
			datatable.addCell(p);
		}
	}
	public PdfPCell getFooterImages(
			EFG2PDFCellCalculations cellCalculus){
		if(this.pdfMaker.getFooterImagesList().size() <= 0){
			return null;
		}
		PdfPCell footerCell = new PdfPCell();
		footerCell.setBorder(0);
		footerCell.setFixedHeight(cellCalculus.getFooterHeight());
		footerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

		for (Iterator iter = this.pdfMaker.getFooterImagesList().iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			
			try {
				Image image = Image.getInstance(new File(this.imagesDirectory,element).toURI().toURL());
				image.setAlignment(Element.ALIGN_RIGHT | Image.TEXTWRAP);
				footerCell.addElement(image);
			} catch (BadElementException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} catch (MalformedURLException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}				
		}
		return footerCell;
	}
	private PdfPCell getFooter(EFG2PDFCellCalculations cellCalculus){

		Phrase p = null;
    	if((this.pdfMaker.getCopyRight1().getCaption() != null)&&
    			(this.pdfMaker.getCopyRight2().getCaption() != null)){
    		if(p == null){
      			Chunk ck = new Chunk(
    					this.pdfMaker.getCopyRight1().getCaption() + " ", 
    					this.pdfMaker.getCopyRight1().getFont());
    			if(this.pdfMaker.getCopyRight1().isUnderLine()){
    				ck.setUnderline(isUnderline_footer_thickness,
    						isUnderline_footer_y_position);
    			}
    			p = new Phrase(ck);
    		}
    		Chunk ck = new Chunk(
					this.pdfMaker.getCopyRight2().getCaption(), 
					this.pdfMaker.getCopyRight2().getFont());
			if(this.pdfMaker.getCopyRight2().isUnderLine()){
				ck.setUnderline(isUnderline_footer_thickness,
						isUnderline_footer_y_position);
			}
        	Phrase p2 = new Phrase(ck);
        	
    		p.add(p2);
    	}
    	else if(this.pdfMaker.getCopyRight1().getCaption() != null){
    		if(p == null){
    			Chunk ck = new Chunk(
    					this.pdfMaker.getCopyRight1().getCaption() + " ", 
    					this.pdfMaker.getCopyRight1().getFont());
    			if(this.pdfMaker.getCopyRight1().isUnderLine()){
    				ck.setUnderline(isUnderline_footer_thickness,
    						isUnderline_footer_y_position);
    			}
    			p = new Phrase(ck);
    	    }
    	}
    	else if(this.pdfMaker.getCopyRight2().getCaption() != null){
    		if(p == null){
       			Chunk ck = new Chunk(
    					this.pdfMaker.getCopyRight2().getCaption(), 
    					this.pdfMaker.getCopyRight2().getFont());
    			if(this.pdfMaker.getCopyRight2().isUnderLine()){
    				ck.setUnderline(isUnderline_footer_thickness,
    						isUnderline_footer_y_position);
    			}
    			p = new Phrase(ck);
    		}
    	}
    	if(p != null){
    		PdfPCell footerCell = new PdfPCell();
    		footerCell.setBorder(0);
     		footerCell.setFixedHeight(cellCalculus.getFooterHeight());
    		footerCell.setHorizontalAlignment(Element.ALIGN_LEFT);

    		footerCell.addElement(p);
    		return footerCell;
    	}
    	return null;
	}

}

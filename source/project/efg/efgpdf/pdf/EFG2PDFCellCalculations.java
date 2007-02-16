package project.efg.efgpdf.pdf;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import project.efg.efgpdf.pdf.PDFMaker.CaptionFontObject;



import com.lowagie.text.BadElementException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;

public class EFG2PDFCellCalculations {
	private static final float DEFAULT_LEADING = 0.5f;
	static Logger log;
	static {
		try {
			log = Logger.getLogger(EFG2PDFCellCalculations.class);
		} catch (Exception ee) {
		}
	}
	
	//1.5 times the font size if no leading is specified
	public static final float LEADING = 1.5f;
	private float totalTableHeight;
	private PDFMaker pdfMaker;
	private float textHeightBelowImage;
	private float textHeightAboveImage;
	private float fixedCellHeight;
	private float imageHeight;
	private float floatLeading=2f;
	private float cellLeading=0.5f;
	private float maxFontSize=-1f;
	private float maxFontSizeBelow=0f;
	private float maxFontSizeAbove=0f;
	private float TWO=2f;
	private float footerTextWidth = -1;
	private float footerTextHeight=-1;
	private float footerImagesHeight=-1;
	private float footerImagesWidth=-1;

	private float headerTextHeight=-1;

	private float headerTextWidth=-1;
	
	public float getFooterImagesHeight() {
		if(this.footerImagesHeight < 0){
			Rectangle rect = 
				this.computeFooterImageDimensions(
						this.pdfMaker.getFooterImagesList()
						);
			if(rect == null){
				this.footerImagesHeight = 0;
				this.footerImagesWidth = 0;
			}
			else{
				this.footerImagesHeight = rect.height();
				this.footerImagesWidth = rect.width();
			}
		}
		return footerImagesHeight;
	}

	public float getFooterImagesWidth() {
		if(this.footerImagesWidth < 0){
			Rectangle rect = 
				this.computeFooterImageDimensions(this.pdfMaker.getFooterImagesList());
			if(rect == null){
				this.footerImagesHeight = 0;
				this.footerImagesWidth = 0;
			}
			else{
				this.footerImagesHeight = rect.height();
				this.footerImagesWidth = rect.width();
			}
		}
		
		return this.footerImagesWidth;
	}
	public float getHeaderTextHeight() {
		if(this.headerTextHeight < 0){
			Rectangle rect = 
				this.computeHeaderTextDimensions();
			if(rect == null){
				this.headerTextHeight = 0;
				this.headerTextWidth = 0;
			}
			else{
				this.headerTextHeight = rect.height();
				this.headerTextWidth = rect.width();
			}
		}
		return headerTextHeight;
	}

	public float getHeaderTextWidth() {
		if(this.headerTextWidth < 0){
			Rectangle rect = 
				this.computeHeaderTextDimensions();
			if(rect == null){
				this.headerTextHeight = 0;
				this.headerTextWidth = 0;
			}
			else{
				this.headerTextHeight = rect.height();
				this.headerTextWidth = rect.width();
			}
		}
		return this.headerTextWidth;
	}
	public float getFooterTextHeight() {
		if(this.footerTextHeight < 0){
			Rectangle rect = 
				this.computeFooterTextDimensions();
			if(rect == null){
				this.footerTextHeight = 0;
				this.footerTextWidth = 0;
			}
			else{
				this.footerTextHeight = rect.height();
				this.footerTextWidth = rect.width();
			}
		}

		return footerTextHeight;
	}

	public float getFooterTextWidth() {
		if(this.footerTextWidth < 0){
			Rectangle rect = 
				this.computeFooterTextDimensions();
			if(rect == null){
				this.footerTextHeight = 0;
				this.footerTextWidth = 0;
			}
			else{
				this.footerTextHeight = rect.height();
				this.footerTextWidth = rect.width();
			}
		}
		return this.footerTextWidth;
	}
	private HeaderAndFooterHandler headerFootHandler;
	private File footerImagesDirectory;
	public EFG2PDFCellCalculations(PDFMaker pdfMaker, 
			HeaderAndFooterHandler headerFootHandler, File footerImagesDirectory) {
		this.pdfMaker = pdfMaker;
		this.headerFootHandler = headerFootHandler;
		this.footerImagesDirectory = footerImagesDirectory;
		this.doCellComputations();
	
	}


	private Rectangle computeHeaderTextDimensions() {
		CaptionFontObject cfo1 = this.pdfMaker.getMainTitle();
		CaptionFontObject cfo2 =this.pdfMaker.getSubTitle();
		
		if(cfo1 == null && cfo2 == null){
			return null;
		}
		float height = 0;
		float width1 = 0;
		float width2 = 0;
		if(cfo1 != null){
			height = height + computeTextHeight(cfo1);
			width1 = width1 + computeTextWidth(cfo1);
		}
		if(cfo2 != null){
			height = height + computeTextHeight(cfo2);
			width2 = width2 + computeTextWidth(cfo2);
		}
		
		
		if(width1 > width2){
			return new Rectangle(width1,height);
		}
		return new Rectangle(width2,height);
	}
	private Rectangle computeFooterTextDimensions() {
		CaptionFontObject cfo1 = this.pdfMaker.getCopyRight1();
		CaptionFontObject cfo2 =this.pdfMaker.getCopyRight2();
		
		if(cfo1 == null && cfo2 == null){
			return null;
		}
		
		
		float height = 0;
		float width = 0;
		if(cfo1 != null && cfo1.getCaption() != null &&
				!cfo1.getCaption().trim().equals("")){
			height = height + computeTextHeight(cfo1);
			width = width + computeTextWidth(cfo1);
			
			String caption = new String(cfo1.getCaption());			
			cfo1.setCaption(" ");
			width = width + computeTextWidth(cfo1);
			cfo1.setCaption(caption);
		}
		if(cfo2 != null){
			height = height + computeTextHeight(cfo2);
			width = width + computeTextWidth(cfo2);
		}
		
		return new Rectangle(width,height);
	}
	private float computeTextWidth(CaptionFontObject cfo1) {
		if(cfo1 == null){
			return 0;
		}
		String caption = cfo1.getCaption();
		if(caption == null || caption.trim().equals("")){
			return 0;
		}
		Font font = cfo1.getFont();
		BaseFont bf = font.getCalculatedBaseFont(false);
		return bf.getWidthPoint(cfo1.getCaption(), cfo1.getFontSize());
	}
	private Rectangle computeFooterImageDimensions(List list){
		float imageHeight = 0;
		float imageWidth = 0;
		if(list == null || (list.size() <=0) ){
			return null;
		}
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				
				
				try {
					Image image = Image.getInstance(new File(this.footerImagesDirectory,element).toURL());
					float imageHeight1 = image.height();
					if(imageHeight1 > imageHeight){
						imageHeight = imageHeight1;
					}
					float imageWidth1 = image.width();
					imageWidth = imageWidth + imageWidth1;
					
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
		
		return new Rectangle(imageWidth, imageHeight);
	}
	private float computeCellHeight(float totalheight2) {
		float numberOfRows = this.pdfMaker.getNumberRows();
		if(this.headerFootHandler.isFooter() && this.headerFootHandler.isHeader()){
			totalheight2 = totalheight2 - this.getFooterHeight() - this.getHeaderTextHeight();
		}
		else if(this.headerFootHandler.isFooter() ){
			totalheight2 = totalheight2 -
			this.getFooterHeight();
		}
		else if(this.headerFootHandler.isHeader()){
			totalheight2 = totalheight2 -
			this.getHeaderTextHeight();	
		}
		this.fixedCellHeight = 
			(totalheight2/numberOfRows);
		return totalheight2;
		
	}
	private void doCellComputations() {
		log.debug("Paper size Height From pdf Maker: " + this.pdfMaker.getPaperSize().height());
		if(this.pdfMaker.isPortraitOrientation()){
			this.totalTableHeight = 
				this.pdfMaker.getPaperSize().height()-
			(EFG2PDFConstants.DEFAULT_TOP_MARGIN + 
					EFG2PDFConstants.DEFAULT_BOTTOM_MARGIN);
	
		}
		else{
			this.totalTableHeight = 
				this.pdfMaker.getPaperSize().height()-
			(EFG2PDFConstants.DEFAULT_BOTTOM_MARGIN + 
					EFG2PDFConstants.DEFAULT_BOTTOM_MARGIN);

		}
		log.debug("Total Height Before compute: " + this.totalTableHeight);

		//add the space for headers and footers
		this.totalTableHeight = this.computeCellHeight(this.totalTableHeight); 
		log.debug("Total Height After compute: " + this.totalTableHeight);

		this.textHeightBelowImage = this.computeTextHeight(this.textHeightBelowImage,this.pdfMaker.getCaptionsBelow());
		log.debug("Height Below Image: " + this.textHeightBelowImage);

		log.debug("% Text For Column Height: " +((this.textHeightBelowImage/getFixedCellHeight()) * 100));
		log.debug("Fixed Height: " + getFixedCellHeight());
		this.maxFontSizeBelow = this.maxFontSize;
		//this.textHeightBelowImage = this.textHeightBelowImage;// + (this.maxFontSizeBelow * this.cellLeading);
		this.maxFontSize = -1f;
		if(this.textHeightBelowImage < 0){
			this.textHeightBelowImage= 0;
		}
	
		this.textHeightAboveImage = this.computeTextHeight(this.textHeightAboveImage,this.pdfMaker.getCaptionsAbove());
		this.maxFontSizeAbove = this.maxFontSize;
		if(this.textHeightAboveImage < 0){
			this.textHeightAboveImage= 0;
		}
		log.debug("this.textHeightAboveImage: " + this.textHeightAboveImage);
		//compute weight of a cell
		this.computeImageCellHeight();
		this.fixedCellHeight = this.fixedCellHeight - DEFAULT_LEADING;
	}

	public float getFooterHeight() {
		if(this.getFooterTextHeight() > this.getFooterImagesHeight()){
			return this.getFooterTextHeight();
		}
		return this.getFooterImagesHeight();
	}
	public float getFooterWidth() {
		return this.getFooterImagesWidth()+  this.getFooterTextWidth();
	
	}
	private void computeImageCellHeight() {
		this.imageHeight = this.fixedCellHeight-
		this.textHeightBelowImage-
		this.textHeightAboveImage-
		(this.floatLeading)-
		(this.maxFontSizeAbove * this.cellLeading)-
		(this.maxFontSizeBelow * this.cellLeading);
		
		float weight_around_image = 
			TWO * this.pdfMaker.getWEIGHT_FRAME_AROUND_IMAGE();

		float white_space_around_image = 
			TWO * this.pdfMaker.getWEIGHT_WHITE_SPACE_AROUND_IMAGE();
			
		this.imageHeight = this.imageHeight -weight_around_image-white_space_around_image;
	
		if(this.imageHeight < 0){
			this.imageHeight = 0;
		}
		log.debug("Image Height: " + this.imageHeight);
		log.debug("% Image For Column Height: " +((this.imageHeight/getFixedCellHeight()) * 100));

	}
	private float computeTextHeight(CaptionFontObject co ){
		if(co != null){
			float fontSize = co.getFontSize();
			if(fontSize > this.maxFontSize){
				this.maxFontSize = fontSize;
			}
			float currentLeading = (LEADING * fontSize);
			Font font = co.getFont();				
			BaseFont bf = font.getCalculatedBaseFont(false);
			
			float ascent = bf.getFontDescriptor(BaseFont.ASCENT, fontSize);
			
			float descent =bf.getFontDescriptor(BaseFont.DESCENT, fontSize);
			
			float height = (ascent - descent) + currentLeading;

			return height;
		}
		return 0;
	}
	private float computeTextHeight(float textHeight,Collection captions2) {
		if(captions2 == null || captions2.size() == 0){
			return 0;
		}		
		for (Iterator iter = captions2.iterator(); iter.hasNext();) {
			CaptionFontObject co = (CaptionFontObject) iter.next();
			if(co != null){
				float height = computeTextHeight(co);
				textHeight = textHeight + height;
			}
		}		
		return textHeight;
	}
	public float getFixedCellHeight() {
		return fixedCellHeight;
	}
	public void setFixedCellHeight(float fixedCellHeight) {
		this.fixedCellHeight = fixedCellHeight;
	}
	public float getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(float imageHeight) {
		this.imageHeight = imageHeight;
	}
	public PDFMaker getPdfMaker() {
		return pdfMaker;
	}
	public void setPdfMaker(PDFMaker pdfMaker) {
		this.pdfMaker = pdfMaker;
	}
	public float getTextHeightAboveImage() {
		return textHeightAboveImage;
	}
	public void setTextHeightAboveImage(float textHeightAboveImage) {
		this.textHeightAboveImage = textHeightAboveImage;
	}
	public float getTextHeightBelowImage() {
		return textHeightBelowImage;
	}
	public void setTextHeightBelowImage(float textHeightBelowImage) {
		this.textHeightBelowImage = textHeightBelowImage;
	}
	public float getTotalTableHeight() {
		return totalTableHeight;
	}
	public void setTotalTableHeight(float totalTableHeight) {
		this.totalTableHeight = totalTableHeight;
	}
}

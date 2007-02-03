package project.efg.efgpdf.pdf;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import project.efg.efgpdf.pdf.PDFMaker.CaptionFontObject;
import project.efg.util.EFGImportConstants;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class EndPage extends PdfPageEventHelper implements EFG2PDFConstants{
	private PDFMaker pdfMaker;
	private CaptionFontObject mainTitleObject;

	//private CaptionFontObject subTitleObject;

	private CaptionFontObject copyRight1Object;

	private CaptionFontObject copyRight2Object;
	private List headerImages;
	private File imagesDirectory;
	//private float headerHeight = DEFAULT_HEADER_HEIGHT;
	
	/**
	 * 
	 * @param pdfMaker
	 * @param imagesDirectory
	 */
	public EndPage(PDFMaker pdfMaker, File imagesDirectory) {
		super();
		
		this.imagesDirectory = computeImagesDirectory(imagesDirectory);
		this.pdfMaker = pdfMaker;
		this.mainTitleObject = this.pdfMaker.getMainTitle();
		//this.subTitleObject = this.pdfMaker.getSubTitle();
		this.copyRight1Object = this.pdfMaker.getCopyRight1();
		this.copyRight2Object = this.pdfMaker.getCopyRight2();
		this.headerImages = this.pdfMaker.getHeaderImagesList();
		
	}
	/*private void computeHeaderSize(){
		Font font = this.mainTitleObject.getFont();
		float fontsize = font.size();
		if(fontsize > this.headerHeight){
			 this.headerHeight = fontsize + 2;
		}
		
	}*/
	/**
	 * @param imagesDirectory2
	 * @return
	 */
	private File computeImagesDirectory(File imagesDirectory2) {
		if(imagesDirectory2 != null){
			return new File(imagesDirectory2.getParentFile(),
					EFGImportConstants.templateImagesDirectory);
		}
		return null;
	}
	private void addHeader(PdfWriter writer, 
    		Document document) throws BadElementException, MalformedURLException, IOException{
    	
    	PdfPTable pdfTable = new PdfPTable(this.pdfMaker.getNumberColumns());
    	Chunk chunk = null;
    	if(this.mainTitleObject.getCaption() != null && 
    			!this.mainTitleObject.getCaption().trim().equals("")){
    		
			chunk = new Chunk(
					this.mainTitleObject.getCaption(),
					this.mainTitleObject.getFont());
			if(this.mainTitleObject.isUnderLine()){
				chunk.setUnderline(new Color(0,0,0),
						EFG2PDF.isUnderline_thickness,
						(EFG2PDF.isUnderline_thickness*EFG2PDF.isUnderline_thickness),
						EFG2PDF.isUnderline_y_position, 
						(EFG2PDF.isUnderline_y_position*0.3f),
						PdfContentByte.LINE_CAP_PROJECTING_SQUARE
						);			
	
			}
    	}
    	else{
    		chunk = new Chunk("");
    	}
		//float widthPoint = chunk.getWidthPoint();
		
		PdfPCell header = new PdfPCell();
		
		header.setHorizontalAlignment(Element.ALIGN_BOTTOM);
		header.setBorder(PdfPCell.NO_BORDER);
		//header.setFixedHeight(this.headerHeight);
		if(this.pdfMaker.getNumberColumns() > 1){
			if(this.pdfMaker.getImage2DisplayFields().size() > 0){
				header.setColspan(this.pdfMaker.getNumberColumns() -1);
			}
		}
		else if(this.pdfMaker.getNumberColumns() ==1){
			if(this.pdfMaker.getImage2DisplayFields().size() > 0){
				header.setColspan(2);
			}
		}
		header.setPaddingBottom(2);
		/*if(widthPoint > header.width()){
			System.out.println("I am scaling!! by: " + header.width()/widthPoint);
			chunk.setHorizontalScaling(header.width()/widthPoint);
		}
		else{
			System.out.println("I am not scaling!!");
		}*/
		header.addElement(new Paragraph(-1,
				chunk));
		
		pdfTable.addCell(header);
		if(this.pdfMaker.getImage2DisplayFields().size() > 0){
			this.addImageCell(pdfTable);
		}
		
		pdfTable.setTotalWidth(document.getPageSize().width() - document.leftMargin() - document.rightMargin());
		
		pdfTable.writeSelectedRows(0,
    			-1, 
    			document.leftMargin(),
    			document.getPageSize().height() - document.topMargin() + pdfTable.getTotalHeight(),
    			writer.getDirectContent()
    	);
	}	
	/**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    public void onEndPage(PdfWriter writer, 
    		Document document) {
        try {
        	this.addHeader(writer, document);
        	this.addFooter(writer, document);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    /**
	 * @param writer
	 * @param document
	 */
	private void addFooter(PdfWriter writer, Document document) {
    	PdfPTable footer = new PdfPTable(1);
    	PdfPCell cell = null;
    	Paragraph p = null;
    	Chunk chunk = null;
    	if((this.copyRight1Object.getCaption() != null)&&
    			(this.copyRight2Object.getCaption() != null)){
    		p = new Paragraph();
    		
        	chunk = new Chunk(this.copyRight1Object.getCaption(),copyRight1Object.getFont());
        	
   			if(this.copyRight1Object.isUnderLine()){
   				chunk.setUnderline(EFG2PDF.isUnderline_thickness,EFG2PDF.isUnderline_y_position);
			}
   			p.add(chunk);
   			
   			chunk = new Chunk(" ");
   			p.add(chunk);
   			
        	chunk= new Chunk(this.copyRight2Object.getCaption(),this.copyRight2Object.getFont());
			if(this.copyRight2Object.isUnderLine()){
				chunk.setUnderline(EFG2PDF.isUnderline_thickness,EFG2PDF.isUnderline_y_position);
			}	
			p.add(chunk);
    	}
    	else if(this.copyRight1Object.getCaption() != null){
    		p = new Paragraph();
    		chunk =	new Chunk(
					this.copyRight1Object.getCaption(),  
					this.copyRight1Object.getFont() 
    			);
    	
			if(this.copyRight1Object.isUnderLine()){
				chunk.setUnderline(EFG2PDF.isUnderline_thickness,EFG2PDF.isUnderline_y_position);
				
			}
			
    		p.add(chunk);
   		
    	}
    	else if(this.copyRight2Object.getCaption() != null){
    		chunk =	new Chunk(
					this.copyRight2Object.getCaption(),  
					this.copyRight2Object.getFont() 
    			);
    	
    		p = new Paragraph();
			if(this.copyRight2Object.isUnderLine()){
				chunk.setUnderline(EFG2PDF.isUnderline_thickness,EFG2PDF.isUnderline_y_position);
				
			}
			
			p.add(chunk);
    		
    	}
    	if(p != null){
        	cell = new PdfPCell(p);
        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        	cell.setBorder(PdfPCell.NO_BORDER);
        	footer.addCell(cell);
    	}
    	footer.setTotalWidth(document.getPageSize().width() - 
    			document.leftMargin() - 
    			document.rightMargin()
    	);
    	footer.writeSelectedRows(
    			0,
    			-1, 
    			document.leftMargin(), 
    			document.bottomMargin(),
    			writer.getDirectContent()
    	);
		
	}
	private void addImageCell(PdfPTable pdfTable) throws BadElementException, MalformedURLException, IOException{
    	PdfPTable imageTable = null;
    	if(this.headerImages.size() > 0){
			imageTable = new PdfPTable(this.headerImages.size());
			imageTable.setWidthPercentage(100);

			for (Iterator iter = this.headerImages.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				
				PdfPCell image1 = new PdfPCell(
						Image.getInstance(
								new File(
										this.imagesDirectory,element).toURL()),
										true);
				image1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				//image1.setFixedHeight(this.headerHeight);
				image1.setBorder(PdfPCell.NO_BORDER);
				imageTable.addCell(image1);					
			}
		}
		else{
			imageTable = new PdfPTable(1);
			imageTable.setWidthPercentage(100);
			PdfPCell image1 = new PdfPCell();
			image1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			//image1.setFixedHeight(this.headerHeight);
			image1.setBorder(PdfPCell.NO_BORDER);
			imageTable.addCell(image1);	

		}
		PdfPCell pc = new PdfPCell(imageTable);
		pc.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pc.setBorder(PdfPCell.NO_BORDER);
		pc.setPaddingBottom(2);
		pdfTable.addCell(pc);
    }
}

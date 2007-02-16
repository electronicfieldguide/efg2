package project.efg.efgpdf.pdf;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class EndPage extends PdfPageEventHelper implements EFG2PDFConstants{
	static Logger log;
	static {
		try {
			log = Logger.getLogger(EndPage.class);
		} catch (Exception ee) {
		}
	}
	
	private EFG2PDFCellCalculations cellCalculus;
	private HeaderAndFooterHandler headerFooter;
	private PdfPCell imageCell;
	
	public EndPage(EFG2PDFCellCalculations cellCalculus,
			HeaderAndFooterHandler headerFooter) {
		super();	
		this.cellCalculus = cellCalculus;
		this.headerFooter = headerFooter;
		this.imageCell = this.headerFooter.getFooterImages(this.cellCalculus);
	}

	/**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    public void onEndPage(PdfWriter writer, 
    		Document document) {
        try {
        	if(document.isOpen()){
	        	writeHeader(document,writer);
	        	writeFooter(document,writer);
        	}
        	else{
        		throw new Exception("Error in processing document");
        	}

        }
        catch (Exception e) {
        	log.error(e.getMessage());
        	if(document == null){
        		document = new Document(PageSize.LETTER);
        	}
        	if(!document.isOpen()){
        		document.open();
        	}
        	try {
				document.add(EFG2PDF.writeMessage(EFG2PDF.errorBuffer.toString()));
			} catch (DocumentException e1) {
				throw new ExceptionConverter(e);
			}	
        }
    }

	private void writeFooter(Document document,PdfWriter writer) {
		

		if(this.headerFooter.isFooter() || 
				(this.headerFooter.isFooterImage())){
			
			Rectangle page = document.getPageSize();
			PdfPTable footerTable = null;
			int footerTableSize = 0;
			float fIWidth = 0;
			float fTWidth = 0;
			float totalWidth = document.getPageSize().width() - 
			document.leftMargin() - 
			document.rightMargin();

			if(this.headerFooter.isFooterImage()){
				footerTableSize = footerTableSize + 1;
				fIWidth =this.cellCalculus.getFooterImagesWidth();
				if(fIWidth < 1){
					fIWidth = 0;
				}
			}
			if(this.headerFooter.isFooter()){
				footerTableSize = footerTableSize + 1;
				fTWidth = totalWidth- fIWidth;
				if(fTWidth < 1){
					fTWidth = 0;
				}
			}
			float[] tableWidths = new float[footerTableSize];
			if(footerTableSize == 2){
				if(fIWidth > fTWidth){
					tableWidths[0] = 1f;
					tableWidths[1] = fIWidth/fTWidth;
				}
				else if (fIWidth == fTWidth){
					tableWidths[0] = 1f;
					tableWidths[1] = 1f;
				}
				else{
					tableWidths[1] = 1f;
					tableWidths[0] = fTWidth/fIWidth;
					
				}
				footerTable = new PdfPTable(tableWidths);
			}
			else{
				footerTable = new PdfPTable(footerTableSize);				
			}
			footerTable.setWidthPercentage(100);
			footerTable.getDefaultCell().setBorder(0);
			this.headerFooter.addFooters(
					footerTable, 
					this.cellCalculus);
			
			if(this.imageCell != null){
				footerTable.addCell(this.imageCell);
			}
	    	footerTable.setTotalWidth(totalWidth);
	    	float ft =  this.cellCalculus.getHeaderTextHeight();
	    	float imageH = this.cellCalculus.getFooterImagesHeight();
	    	if(imageH > ft){
	    		ft = imageH;
	    	}
	    	
	    	if(!this.headerFooter.isHeader()){	    		
	    		ft = 0;
	    	}
	    	log.debug("Table height: " + this.cellCalculus.getTotalTableHeight());
	    	log.debug("document.leftMargin(): " + document.leftMargin());
	    	log.debug("document.topMargin(): " + document.topMargin());
	    	log.debug("ft: " + ft);

	    	footerTable.writeSelectedRows(
	    			0,
	    			-1, 
	    			document.leftMargin(), 
	    			page.height()-document.topMargin()-this.cellCalculus.getTotalTableHeight() -ft,
	    			writer.getDirectContent()
	    	);
		}	
	}
	private void writeHeader(Document document,PdfWriter writer) {
	
		if(this.headerFooter.isHeader()){	
		Rectangle page = document.getPageSize();
		float totalWidth = document.getPageSize().width() - 
		document.leftMargin() - 
		document.rightMargin();

    	PdfPTable headerTable = new PdfPTable(1);
    	headerTable.setWidthPercentage(100);
    	headerTable.setTotalWidth(totalWidth);
    	this.headerFooter.addHeaders(headerTable, this.cellCalculus);
		headerTable.writeSelectedRows(0,
    			-1, 
    			document.leftMargin(),
    			page.height() - document.topMargin(),
    			writer.getDirectContent()
    	);

		}
	}
}

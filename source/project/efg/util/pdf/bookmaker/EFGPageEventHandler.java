/**
 * 
 */
package project.efg.util.pdf.bookmaker;

import java.util.Iterator;
import java.util.SortedSet;

import project.efg.util.pdf.factory.EFGPDFSpringFactory;
import project.efg.util.pdf.interfaces.EFGRankObject;
import project.efg.util.pdf.utils.EFGLine;
import project.efg.util.pdf.utils.MetricsObject;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class EFGPageEventHandler extends PdfPageEventHelper {
	private boolean isDraw;
	private EFGLine efgLine;
	private MetricsObject headermetrics;
	private MetricsObject footermetrics;

	private ConfigHandler config;
	private boolean hasFirstPage;

	
	
	/**
	 * 
	 */
	public EFGPageEventHandler() {
	
	}
	public boolean isFirstPage(){
		return this.hasFirstPage;
	}
	/**
	 * @param line
	 */
	public void setEFGLine(EFGLine line) {
		this.efgLine = line;
	}
	/**
	 * 
	 * @param metrics
	 */
	public  void setHeaderMetrics(MetricsObject metrics) {
		this.headermetrics = metrics;
	}
	/**
	 * 
	 * @param metrics
	 */
	public  void setFooterMetrics(MetricsObject metrics) {
		this.footermetrics = metrics;
	}

	/**
	 * 
	 * @param isDraw
	 */
	public void setDrawLine(boolean isDraw){
		this.isDraw = isDraw;
	}
	/**
	 * 
	 */
	public void onParagraphEnd(PdfWriter writer,
	        Document document,
	        float paragraphPosition){
		
		if(this.isDraw){
			PdfContentByte cb = writer.getDirectContent();
			cb.setLineWidth(this.efgLine.getLineWidth());	
			float pos = writer.getVerticalPosition(false);
			cb.moveTo(document.leftMargin(),pos - 5);
			cb.lineTo(document.getPageSize().width()- document.leftMargin(),
					pos-5);
			cb.stroke();
		}
	}
	/**
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, 
			Document document) {
	    try {

	    	if(this.hasFirstPage && document.getPageNumber() == 1){
	    		this.writeFirstPage(writer, document);
	    	}
	    	else{
	    		
	    		this.addHeader(document, this.config.getTitle(),writer);
	    		this.addFooter(document, writer);
	    	}
	     }
	    catch (Exception e) {
	        throw new ExceptionConverter(e);
	    }
	
	}
	/**

	/**
	 * FIXME
	 * @param pagesize
	 * @param title
	 */
	private void addHeader(Document document,
			String title,PdfWriter writer){
		Rectangle pagesize = document.getPageSize();
       try {
             PdfPTable head = new PdfPTable(1);
            head.setTotalWidth(pagesize.width() - 
            		document.leftMargin() - document.rightMargin());

            head.getDefaultCell().setBorder(0);
            Paragraph p = new Paragraph(title, 
            		this.headermetrics.getFont());
            
            p.setAlignment(this.headermetrics.getAlignment());
  
            head.addCell(p);
            head.writeSelectedRows(0, -1, 
            		document.leftMargin(), 
            		pagesize.height() - (document.topMargin() - 
            				this.headermetrics.getDistanceFromMargins()),
                writer.getDirectContent());

       }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
	}
	/**
	 * 
	 */
	private void writeFirstPage(PdfWriter writer, Document document) {
    	writeFrontTextHeader(writer, document);
    	writeFrontTextFooter(writer, document);
 	}
	/**
	 * @param set- Retrieve Text from the Set
	 */
	private String getText(SortedSet set) {
		StringBuffer buffer = new StringBuffer();
		if(set == null){
			return buffer.toString();
		}
		Iterator iter = set.iterator();
		while(iter.hasNext()){
			EFGRankObject rank = (EFGRankObject)iter.next();
			String text = rank.getName();
			if(buffer.length() > 0){
				buffer.append(" ");
			}
			buffer.append(text);
		}
		return buffer.toString();
	}
	/**
	 * @param writer
	 * @param document
	 */
	private void writeFrontTextFooter(PdfWriter writer, Document document) {
		int counter = 0;
		Rectangle pageSize = document.getPageSize();
		float totalWidth = document.getPageSize().width() - 
		document.leftMargin() - 
		document.rightMargin();

    	PdfPTable footerTable = new PdfPTable(1);
    	footerTable.setWidthPercentage(100);
    	footerTable.setTotalWidth(totalWidth);
        footerTable.getDefaultCell().setBorder(0);
        footerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

    	
		Font font = null;
		String text = null;
		SortedSet set = this.config.getCredits1();
	
		text = this.getText(set);
		
		if(text != null && text.trim().length() > 0){
			font = EFGPDFSpringFactory.getBold22Font();
			Phrase phrase = new Phrase((font.getCalculatedSize()*1.25f),text,font);
		    
	        PdfPCell pcell = new PdfPCell(phrase);
	        pcell.setBorder(0);
	        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        pcell.setVerticalAlignment(Element.ALIGN_CENTER);
			footerTable.addCell(pcell);
			++counter;
		}
		set = this.config.getCredits2();
		text = this.getText(set);
		if(text != null && text.trim().length() > 0){
			font = EFGPDFSpringFactory.getNormal16Font();
			Phrase phrase = new Phrase((font.getCalculatedSize()*1.25f),text,font);
	    
           PdfPCell pcell = new PdfPCell(phrase);
           pcell.setBorder(0);
	       pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	       pcell.setVerticalAlignment(Element.ALIGN_CENTER);

			footerTable.addCell(pcell);
			++counter;
		}
		if(counter > 0){
	        float y = footerTable.writeSelectedRows(0, counter, 
		      		  document.leftMargin(), 
		      		  (document.getPageSize().height())/3,
		      		  writer.getDirectContent());
		}
	}

	/**
	 * @param font
	 * @param titles
	 * @param b
	 */
	private void writeFrontTextHeader(PdfWriter writer,
	        Document document) {
		
		int counter = 0;
		Rectangle pageSize = document.getPageSize();
		float totalWidth = document.getPageSize().width() - 
		document.leftMargin() - 
		document.rightMargin();

    	PdfPTable headerTable = new PdfPTable(1);
    	headerTable.setWidthPercentage(100);
    	headerTable.setTotalWidth(totalWidth);
    	
		
        headerTable.getDefaultCell().setBorder(0);
		Font font = null;
		SortedSet set = this.config.getFrontPageTitle();
		String text = this.getText(set);
		if(text != null && text.trim().length() > 0){
			font = EFGPDFSpringFactory.getBold36Font();
			Phrase phrase = new Phrase((font.getCalculatedSize()*1.25f),text,font);
            PdfPCell pcell = new PdfPCell(phrase);
            
            pcell.setBorder(0);
	        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        pcell.setVerticalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(pcell);
			++counter;
		}
		if(counter > 0){
			font = EFGPDFSpringFactory.getNormal20Font();
			Phrase phrase = new Phrase("\n",font);
	        headerTable.addCell(phrase);
	        ++counter;
		}
		set = this.config.getFrontSubtitle();
		text = this.getText(set);
		if(text != null && text.trim().length() > 0){
			font = EFGPDFSpringFactory.getNormal20Font();
			Phrase phrase = new Phrase(
					(font.getCalculatedSize()*1.25f),
					text,
					font);
           PdfPCell pcell = new PdfPCell(phrase);
           pcell.setBorder(0);
	       pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	       pcell.setVerticalAlignment(Element.ALIGN_CENTER);
           headerTable.addCell(pcell);
			++counter;
		}
		if(counter > 0){
	       float y = headerTable.writeSelectedRows(
	        		0, 
	        		counter, 
	      		  document.leftMargin(), 
	      		  pageSize.height() - (document.topMargin()*3),
	      		  writer.getDirectContent());
		}
	}
	/**
	 *FIXME
	 * @param document
	 * @param pagesize
	 */
	private void addFooter(Document document,
			PdfWriter writer){
       try {
    	   int pgNumber = document.getPageNumber();
    	   if(this.hasFirstPage){
    		   pgNumber = pgNumber-1;
    	   }
    	   StringBuffer pageNumber = new StringBuffer("- "); 
   		   pageNumber.append(pgNumber);
   		   pageNumber.append(" -");
 
   		   //footer
    	   Rectangle pagesize = document.getPageSize();
            PdfPTable foot = new PdfPTable(1);
            foot.getDefaultCell().setBorder(0);
            Paragraph p = new Paragraph(pageNumber.toString(), 
            		this.footermetrics.getFont());
            
            foot.addCell(p);
            
            foot.setTotalWidth(pagesize.width() - document.leftMargin() - document.rightMargin());
            foot.writeSelectedRows(0, -1, pagesize.width()/2, document.bottomMargin() - 
            		this.footermetrics.getDistanceFromMargins(),
                writer.getDirectContent());
        }
        catch (Exception e) {
        	e.printStackTrace();
            throw new ExceptionConverter(e);
        }
	}
	/**
	 * Write the Front Page if any. We write an empty 
	 * page and use the event handler to write the header
	 * and footer
	 * @param config - ConfigHandler
	 * @param document - The Itext document
	 */
	public void writeFirstPage(ConfigHandler config,Document document) {
		this.config = config;
		if(this.config.getCredits1().size() > 0 || 
				this.config.getCredits2().size() > 0 || 
				this.config.getFrontPageTitle().size() > 0 || 
				this.config.getFrontSubtitle().size() > 0){
					PdfPTable ptable = new PdfPTable(1);
					ptable.getDefaultCell().setBorder(0);
					ptable.addCell(new Paragraph(""));
				try {
					this.hasFirstPage = true;
					document.add(ptable);
					document.newPage();
			
				} catch (DocumentException e) {
					e.printStackTrace();
				}
		}
				
	}


}


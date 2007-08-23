

package project.efg.util.pdf.bookmaker;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;

import project.efg.efgDocument.EFGDocument;
import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.EFGType;
import project.efg.efgDocument.Item;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourceType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.StatisticalMeasuresType;
import project.efg.efgDocument.TaxonEntries;
import project.efg.efgDocument.TaxonEntryType;
import project.efg.efgDocument.TaxonEntryTypeItem;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.pdf.factory.EFGPDFSpringFactory;
import project.efg.util.pdf.interfaces.EFGRankObject;
import project.efg.util.pdf.interfaces.PDFGUIConstants;
import project.efg.util.pdf.utils.EFGLine;
import project.efg.util.pdf.utils.MetricsObject;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.events.IndexEvents;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://itext.ugent.be/itext-in-action/ 
 * http://www.manning.com/lowagie/
 */

public class BookMaker{
	/**
	 * Holds the document to write
	 */
	private Document document;
	
	/**
	 * Metrics for drawing images
	 */
	private MetricsObject imagesMetrics;
	/**
	 * 
	 */
	private ConfigHandler config;
	/**
	 * The path to where images can be found
	 */
	private URL mediaResourceLocation;
	private EFGPageEventHandler eFGPageEventHandler;

	private EFGDocument efgdoc;
	private IndexEvents index;

	private boolean isPreview;
	/**
	 * 
	 * @param efgdoc
	 * @param config
	 * @param mediaResourceLocation
	 */
	public BookMaker(EFGDocument efgdoc, 
			ConfigHandler config, 
			URL mediaResourceLocation) {
		
		this.efgdoc =efgdoc;
		this.document = EFGPDFSpringFactory.getItextBookDocument();
		this.config = config;
		this.mediaResourceLocation= mediaResourceLocation;
		this.imagesMetrics = EFGPDFSpringFactory.getImageMetrics();
	}
	
	
	/* (non-Javadoc)
	 */
	public void writeDocument(OutputStream stream, boolean isPreview) {
		PdfWriter writer= null;
		

		try {
			this.isPreview = isPreview;
			writer = PdfWriter.getInstance(this.document,
					stream);
		this.document.open();
		this.document.setMarginMirroring(true);
		this.eFGPageEventHandler = EFGPDFSpringFactory.getEFGPageEventHandlerInstance();
		this.index = EFGPDFSpringFactory.getIndexEvents();
		if(this.eFGPageEventHandler == null){
			throw new Exception("Can't find Page handler");
		}
		writer.setPageEvent(this.eFGPageEventHandler);
		writer.setPageEvent(this.index);
		this.eFGPageEventHandler.writeFirstPage(this.config,this.document);
		
		TaxonEntries ts = this.efgdoc.getTaxonEntries();
		int ts_count = ts.getTaxonEntryCount();
		for(int index = 0;index < ts_count;++index){
			try {
				Map map = EFGPDFSpringFactory.getMapCollection();
				TaxonEntryType te = ts.getTaxonEntry(index);
				int current_count = te.getTaxonEntryTypeItemCount();
				for(int index1 = 0; index1 < current_count; ++index1){
					boolean isFound = false;
					TaxonEntryTypeItem titem = te.getTaxonEntryTypeItem(index1);
					
					ItemsType its = titem.getItems();
					MediaResourcesType mts = 
						titem.getMediaResources();
					EFGListsType ets = titem.getEFGLists();
					StatisticalMeasuresType st = titem.getStatisticalMeasures();
			
					String character = null;
					if(its != null){
						character = its.getName();
						map.put(character,its);
						
					}
					if(mts != null){
						character = mts.getName();//mediaresource field
						map.put(character,mts);
					}
					if(ets != null){
						character = ets.getName();
						map.put(character,ets);
					}
					if(st != null){
						character = st.getName();
						map.put(character,st);
					}
	
				}
				processTaxonEntry(map);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				if((index == EFGImportConstants.NUMBER_OF_PDF_PAGES_2_DISPLAY) && this.isPreview){
					break;
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
			//if we are showing only one page break here
		}
		writeIndex();
		
		} catch (DocumentException e1) {
			// FIXME Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(this.document != null){
				if(this.document.isOpen()){
				
					this.document.close();
				}
			}
		}
		
	}







	/**
	 * @param file
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws BadElementException 
	 */
	private PdfPCell addImage(String imageName) {
		 Image image = null;
		 PdfPCell imageCell = null;
		
		try {
			URL url = new URL(this.mediaResourceLocation.toExternalForm() + "/" + imageName);
			if(!isMediaresourceExists(url)){
					return null;
			}
			image = Image.getInstance(url);
			if(image == null){
				return null;
			}
			//TODO use spring
			image.scalePercent(PDFGUIConstants.IMAGE_SCALE_PERCENT);
			imageCell = new PdfPCell(image,true);
			imageCell.setVerticalAlignment(this.imagesMetrics.getAlignment());
			imageCell.setPadding(this.imagesMetrics.getPadding());
			imageCell.setBorder(0);
			
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageCell;
	}
	private boolean isMediaresourceExists(URL imageURL){
		
	    URLConnection conn = null;
	   
	    try
	    {
            conn = imageURL.openConnection();
             if (conn instanceof HttpURLConnection){
                HttpURLConnection httpcon = (HttpURLConnection)conn;
                int code = httpcon.getResponseCode();
               if(code != HttpURLConnection.HTTP_OK){
            	   httpcon.disconnect();
            	   return false;
               }
              // httpcon.disconnect();
               return true;
            }
         	File file = new File(imageURL.getFile());
        	if(!file.exists()){
 				file = null;
				return false;
			}
        	return true;
	    }
	    catch(Exception rr){
	    	return false;
	    }
	    finally
	    {
	       if(conn != null){
	    	   conn = null;
	       }
	    }
		
	}
	/**
	 * @param b12
	 * @param string
	 * @param bulletedLists
	 */
	private void addDecscriptionListGroup(Font captionFont, String captionText, List bulletedLists) {
		Paragraph paragraph =EFGPDFSpringFactory.getItextParagraph();
		Chunk chunk = new Chunk(captionText,captionFont);
		paragraph.add(chunk);
		try {
			document.add(paragraph);
			document.add(bulletedLists);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @throws DocumentException 
	 * 
	 *
	 */
	private void addDecscriptionGroup(Font captionFont,
			Font descFont, 
			String captionText, 
			String descText) throws DocumentException{
		
		Paragraph paragraph = EFGPDFSpringFactory.getItextParagraph();
		paragraph.setLeading(PDFGUIConstants.LINE_SPACING_CONSTANT *descFont.getCalculatedSize());
		Chunk chunk = new Chunk(captionText,captionFont);
		paragraph.add(chunk);
		chunk = new Chunk(descText,descFont);
		paragraph.add(chunk);
		document.add(paragraph);
	}
	/**
	 * @throws DocumentException 
	 * 
	 *
	 */
	private void addTaxonomyGroup(Font font,String text){
		
		Chunk chunk = new Chunk(text,font);
		try {
			//TODO use spring
			
			Paragraph p = EFGPDFSpringFactory.getItextParagraph();
			p.add(chunk);
			index.create(chunk, text);
			this.document.add(p);
			this.eFGPageEventHandler.setDrawLine(false);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	private void addTaxonomy(Map taxonEntry){
		//this should come from the configuration
		String mainHeader = this.config.getMainHeader();
		SortedSet set = this.config.getSubHeaders();
	
		
		boolean isFound = false;
		//use spring here
		
		//addheader
		Font font = EFGPDFSpringFactory.getBold14Font();
		ItemsType items = (ItemsType)taxonEntry.get(mainHeader);
		Item item = null;
		if(items != null){
			item = items.getItem(0);
			if(item != null){
				String state = item.getContent();
				
				addTaxonomyGroup(font,state);	
				this.eFGPageEventHandler.setEFGLine(new EFGLine(1f,4));
				this.eFGPageEventHandler.setDrawLine(true);
				addTaxonomyGroup(font,"");
				this.eFGPageEventHandler.setDrawLine(false);

				isFound = true;
			}
		}
		
		//add subheaders
		Iterator iter = set.iterator();
		font = EFGPDFSpringFactory.getNormal12Font();
		while(iter.hasNext()){
			EFGRankObject rank = (EFGRankObject)iter.next();
			String subHeader = rank.getName();
			items = (ItemsType)taxonEntry.get(subHeader);
			if(items == null){
				continue;
			}
			
			for(int i = 0; i < items.getItemCount();i++){
				item = items.getItem(i);
				String state = item.getContent();
				addTaxonomyGroup(font,state);	
				if(i != items.getItemCount() - 1){
					this.eFGPageEventHandler.setEFGLine(new EFGLine(1f,4));
					this.eFGPageEventHandler.setDrawLine(true);
					addTaxonomyGroup(font,"");
					this.eFGPageEventHandler.setDrawLine(false);
				}

				isFound = true;
			}
		}
		this.eFGPageEventHandler.setDrawLine(false);
		if(isFound){
			this.eFGPageEventHandler.setEFGLine(new EFGLine(0.5f,4));
			this.eFGPageEventHandler.setDrawLine(true);
			addTaxonomyGroup(font,"");
			this.eFGPageEventHandler.setDrawLine(false);
			try {
				document.add(Chunk.NEWLINE);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param config 
	 * @param doc 
	 * 
	 */
	private void addDescriptions(Map taxonEntry) {
		boolean isPrinted = false;
		//FIXME use spring
		Font b12 = EFGPDFSpringFactory.getBold12Font();// new Font(Font.HELVETICA, 12, Font.BOLD);
		Font font =EFGPDFSpringFactory.getNormal12Font(); //new Font(Font.HELVETICA, 12, Font.NORMAL);
		try {
			SortedSet set = this.config.getDescriptionFields();
			ItemsType items = null;
			EFGListsType lists = null;
			StatisticalMeasuresType stats = null;
			Item item = null;
			
			Iterator iter = set.iterator();
			//font = new Font(Font.HELVETICA, 12, Font.NORMAL);
			while(iter.hasNext()){
				EFGRankObject rank = (EFGRankObject)iter.next();
				String character = rank.getName();
				Object obj =taxonEntry.get(character);
				StringBuffer characterName = new StringBuffer(character);
				characterName.append(" : ");
				if(obj instanceof ItemsType){
					items = (ItemsType)obj;
					StringBuffer stateBuffer = new StringBuffer();
					for(int i = 0; i < items.getItemCount();i++){
						item = items.getItem(i);
						if(i > 0){
							stateBuffer.append(", ");
						}
						stateBuffer.append(item.getContent());
					}
					if(isPrinted){
						document.add(Chunk.NEWLINE);
					}
					isPrinted = true;

					this.addDecscriptionGroup(b12, font,characterName.toString() ,
							stateBuffer.toString());	

				}
				else if(obj instanceof StatisticalMeasuresType){ 
					stats = (StatisticalMeasuresType)obj;

					StringBuffer stateBuffer = new StringBuffer();
					for(int i = 0; i < stats.getStatisticalMeasureCount();i++){
						double max = stats.getMax();
						double min = stats.getMin();
						if(i > 0){
							stateBuffer.append(", ");
						}
						stateBuffer.append(min);
						stateBuffer.append("-");
						stateBuffer.append(max);
						if(stats.getUnit() != null){
							stateBuffer.append(stats.getUnit());
						}
					}
					if(isPrinted){
						document.add(Chunk.NEWLINE);
					}
					isPrinted = true;
					this.addDecscriptionGroup(b12, font,characterName.toString(),
							stateBuffer.toString());	

				}
				else if(obj instanceof EFGListsType){ 
					//get the list symbol form spring
					lists = (EFGListsType)obj;
					List bulletedLists = new List(List.UNORDERED,10);
					for(int i = 0; i < lists.getEFGListCount();i++){
						EFGType eitem = lists.getEFGList(i);
						String state = eitem.getContent();
						
						bulletedLists.add(new ListItem(1.0f*font.getCalculatedSize(),state,font));
					}
					if(isPrinted){
						document.add(Chunk.NEWLINE);
					}
					isPrinted = true;

					this.addDecscriptionListGroup(b12, characterName.toString(),bulletedLists);				
				}

			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param files
	 * @param metrics
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws DocumentException 
	 */
	private void addMediaResources(Map taxonEntry){
		
		SortedSet set = this.config.getMediaResourceFields();
		if(set != null && set.size()> 0){
			MediaResourcesType items = null;
			MediaResourceType item = null;
			PdfPTable pdfTable = 
				new PdfPTable(this.config.getNumberOFImagesPerColumn());
			int counter = 0;
			pdfTable.setWidthPercentage(100);
			Iterator iter = set.iterator();
			boolean isAdded = false;
			while(iter.hasNext()){
				boolean isFound = false;
				EFGRankObject rank = (EFGRankObject)iter.next();
				String str = rank.getName();
				items = (MediaResourcesType)taxonEntry.get(str);
				if(items == null){
					continue;
				}
				for(int i = 0; i < items.getMediaResourceCount();i++){
					item = items.getMediaResource(i);
					String state = item.getContent();//get an image
					if(state == null || "".equals(state.trim())){
						continue;
					}
					PdfPCell imageCell = this.addImage(state);
					if(imageCell != null){
						pdfTable.addCell(imageCell);
						isFound = true;
						isAdded = true;
						++counter;
					}
					if(!rank.isDisplay() && isFound){//show only one image from this category
						break;
					}
				}
			}
			try {
				if(isAdded){
					counter = counter % this.config.getNumberOFImagesPerColumn();
					
					if( counter > 0){
						
						addEmptyCells(pdfTable,this.config.getNumberOFImagesPerColumn() - counter);
					}
					this.document.add(pdfTable);
				}
				else{
					pdfTable = null;
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Itext will not add cells automatically
	 *  
	 * @param pdfTable
	 * @param counter
	 */
	private void addEmptyCells(PdfPTable pdfTable, int counter) {
		
		for(int i = 0; i <= counter; i++){
			PdfPCell cell = new PdfPCell();
			cell.setVerticalAlignment(this.imagesMetrics.getAlignment());
			cell.setPadding(this.imagesMetrics.getPadding());
			cell.setBorder(0);
			pdfTable.addCell(cell);
		}
		
	}


	/**
	 * @throws DocumentException 
	 * 
	 */
	private void writeIndex() throws DocumentException {
		
		document.newPage();
		
		java.util.List list =index.getSortedEntries();
		MultiColumnText mct = new MultiColumnText();
		mct.addRegularColumns(this.document.left(),this.document.right(), 
				10f, 2);
		for (int i = 0, n = list.size(); i < n; i++) {
			IndexEvents.Entry entry = (IndexEvents.Entry) list.get(i);
			Paragraph in =EFGPDFSpringFactory.getItextParagraph();
			if(entry.getIn1().equals("")){
				continue;
			}
			in.add(new Chunk(entry.getIn1() + " .......... "));
			if (entry.getIn2().length() > 0) {
				in.add(new Chunk("; " + entry.getIn2()));
			}
			if (entry.getIn3().length() > 0) {
				in.add(new Chunk(" (" + entry.getIn3() + ")"));
			}
			java.util.List pages = entry.getPagenumbers();
			
			in.add(" ");
			String oldP = null;
			for (int p = 0, x = pages.size(); p < x; p++) {
				Chunk pagenr= null;
				String newP = pages.get(p)+ "";
				int pageNumber = Integer.parseInt(newP);
				if(this.eFGPageEventHandler.isFirstPage()){
					pageNumber = pageNumber - 1;
				}
				if(p > 0){
					if(newP.equals(oldP)){//don't repeat
						continue;
					}
					pagenr= new Chunk(", "+pageNumber);
				}
				else{
					pagenr= new Chunk(""+pageNumber);
				}
				in.add(pagenr);
				oldP = pages.get(p) + "";
			}
			try {
				mct.addElement(in);
			} catch (DocumentException e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
		}
	try {
		document.add(mct);
	} catch (DocumentException e) {
		// FIXME Auto-generated catch block
		e.printStackTrace();
	}
	}
	/**
	 * @param map
	 */
	private void processTaxonEntry(Map map) {
		addTaxonomy(map);
		addDescriptions(map);
		try {
			document.add(Chunk.NEWLINE);
		} catch (DocumentException e) {
			
		}
		addMediaResources(map);
	}

}
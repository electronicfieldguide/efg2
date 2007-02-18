package project.efg.efgpdf.pdf;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import project.efg.efgDocument.EFGListsType;
import project.efg.efgDocument.EFGType;
import project.efg.efgDocument.Item;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourceType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.TaxonEntries;
import project.efg.efgDocument.TaxonEntryType;
import project.efg.efgDocument.TaxonEntryTypeItem;
import project.efg.efgpdf.pdf.PDFMaker.CaptionFontObject;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.EFGImportConstants;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class EFG2PDF implements EFG2PDFInterface {
	private static final long serialVersionUID = 1L;
	static Logger log;
	static {
		try {
			log = Logger.getLogger(EFG2PDF.class);
		} catch (Exception ee) {
		}
	}
	static Map fontFamilyMap;
	static int counter = 1;
	static{//read from fontmetrics
		fontFamilyMap = FontHandler.LoadFonts();
	}
	
	private SortedSet sortBySet;
	private Map taxonEntryMap;
	private Document document; 
	private PdfPTable pdfTable; 
	private PdfWriter writer;
	
	private float fixedCellHeight;
	
	private PDFMaker pdfMaker;
	private List sortBy;
	private List allCaptions;
	private int numberOfCellsCounter;
	private float textHeightBelowImage;
	private float textHeightAboveImage;
	private float imageHeight;
	private EFG2PDFCellCalculations cellCalculus;
	private HeaderAndFooterHandler hd;
	private File footerImagesDirectory;
	private File mediaResourcesDirectory;
	private float tableCellWidth;
	static StringBuffer errorBuffer;
	static{
	errorBuffer= new StringBuffer("Error occured during processing of document\n"); 
	errorBuffer.append("---------\n");
	errorBuffer.append("Possible Causes:\n\n");

	errorBuffer.append("- Images are missing. If you intended to print a file with no images,\n");
	errorBuffer.append("please go back and make sure the box next to \n");
	errorBuffer.append("'Always display captions' in the Data Display Settings is checked.\n\n");

	errorBuffer.append("- some other error that causes this page to " +
			"get generated that we do not know about, please report this to us\n");
	}

	/**
	 * 
	 * @param output
	 * @param configFile
	 */
	public EFG2PDF(
			) {
		this.allCaptions = new ArrayList();	
		this.numberOfCellsCounter = 0;
		this.taxonEntryMap = new HashMap();
	}
	static float isUnderline_thickness = 
		EFG2PDFConstants.DEFAULT_UNDERLINE_THICKNESS;
	static float isUnderline_y_position= 
		EFG2PDFConstants.DEFAULT_UNDERLINE_Y_POSITION;

	
	static {
		initProperties();
	}
	static void initProperties(){
		String property = 
			EFGImportConstants.EFGProperties.getProperty(
					"pdf.caption.underline_thickness");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_thickness = 
				Float.parseFloat(property);
		}
		property= 
			EFGImportConstants.EFGProperties.getProperty(
					"pdf.caption.underline_y_position");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_y_position = 
				Float.parseFloat(property);
		}	
	}
	public static Phrase writeMessage(String message){
		String mutex ="";
			synchronized (mutex) {
				return new Phrase(message,
						new Font(Font.HELVETICA,15f,Font.BOLD,Color.RED));
	
			}
	}
	/* (non-Javadoc)
	 * @see project.efg.efgpdf.pdf.EFG2PDFInterface#readData(java.io.Reader)
	 */
	public void writePdfToStream(project.efg.efgDocument.EFGDocument efgdoc,
			XslPage xslPage,
			OutputStream output,
			File mediaResourcesDirectory, String authors) {
		
		try{
			/*FileOutputStream foutput =
				new FileOutputStream(new File(mediaResourcesDirectory,"Test.pdf"));
			*/
			this.mediaResourcesDirectory = mediaResourcesDirectory;
			this.footerImagesDirectory = 
				computeFooterImagesDirectory(this.mediaResourcesDirectory);
			this.initDocument(output,xslPage,authors);
			if(efgdoc == null){
				throw new Exception("EFGDocument is null");
			}
			TaxonEntries ts = efgdoc.getTaxonEntries();
			int ts_count = ts.getTaxonEntryCount();
			for(int index = 0;index < ts_count;++index){
				TaxonEntryType te = ts.getTaxonEntry(index);
				int current_count = te.getTaxonEntryTypeItemCount();
				for(int index1 = 0; index1 < current_count; ++index1){
					boolean isFound = false;
					TaxonEntryTypeItem titem = te.getTaxonEntryTypeItem(index1);
					
					ItemsType its = titem.getItems();
					MediaResourcesType mts = 
						titem.getMediaResources();
					EFGListsType ets = titem.getEFGLists();
					
			
					String character = null;
					if(its != null){				
						character = its.getName();
						
						if(this.sortBy.contains(character)){//depends 							
							//on position of the character in the sortby list
							for(int j = 0; j < its.getItemCount(); j++){
								Item item = its.getItem(j);
								String state = item.getContent();
								if(state == null || state.trim().equals("")){
									state = "";
								}
								if(state != null){
									CharacterObject charactero = new CharacterObject();
									charactero.setCharacter(character);
									charactero.addState(state);
									this.sortBySet.add(charactero);
									isFound = addToSet(state,te); 
									break;							
								}
								
								
							}
						}
					}
					if(mts != null){
						character = mts.getName();
						if(this.sortBy.contains(character)){
							
							//depends 
							//on position of the character in the sortby list
							for(int j = 0; j < mts.getMediaResourceCount(); j++){
								MediaResourceType item = mts.getMediaResource(j);
								String state = item.getContent();
								if(state != null && !state.trim().equals("")){
									CharacterObject charactero = new CharacterObject();
									charactero.setCharacter(character);
									charactero.addState(state);
									this.sortBySet.add(charactero);
									isFound = addToSet(state,te); 	
									break;	
								}							
							}
						}
					}
					if(ets != null){
						character = ets.getName();
						if(this.sortBy.contains(character)){						
							//depends 
							//on position of the character in the sortby list
							for(int j = 0; j < ets.getEFGListCount(); j++){
								EFGType item = ets.getEFGList(j);
								String state = item.getContent();
								if(state != null){
									CharacterObject charactero = new CharacterObject();
									charactero.setCharacter(character);
									charactero.addState(state);
									this.sortBySet.add(charactero);
									isFound = addToSet(state,te); 
									break;	
								}								
							}
						}
					}
					
					if(isFound){
						break;
					}
				}
			}
			writeObject(handleTaxonEntries(taxonEntryMap));
			
			int numberOfCellsNotWritten = 
				(this.numberOfCellsCounter % this.pdfMaker.getNumberColumns());
			if(numberOfCellsNotWritten > 0){
				numberOfCellsNotWritten = this.pdfMaker.getNumberColumns() - numberOfCellsNotWritten; 
				this.writeEmptyCells(
						numberOfCellsNotWritten,this.fixedCellHeight 
						);
			}
			else if(this.numberOfCellsCounter == 0){
					this.writeErrorMessages(errorBuffer.toString(), this.fixedCellHeight);
			}
			document.add(pdfTable);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				
				//e.printStackTrace();
				if(this.document == null){
					
					this.document = new Document(
							EFG2PDFConstants.DEFAULT_PAPER_SIZE,
							EFG2PDFConstants.DEFAULT_LEFT_MARGIN,
							EFG2PDFConstants.DEFAULT_RIGHT_MARGIN,
							EFG2PDFConstants.DEFAULT_TOP_MARGIN,
							EFG2PDFConstants.DEFAULT_BOTTOM_MARGIN);
					if(this.writer == null){
						this.writer = PdfWriter.getInstance(this.document,output);
					}
					document.open();
				}
				if(!document.isOpen()){
					document.open();
				}
				if(e == null || e.getMessage().trim().equals("")){
					this.document.add(writeMessage(errorBuffer.toString()));	
					log.error(e.getMessage());
				}
				else{
					log.error(e.getMessage());
					this.document.add(writeMessage(errorBuffer.toString()));	
				}
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
			
		}
	
		this.document.close();
		
	}
	/**
	 * @param mediaResourcesDirectory2
	 * @return
	 */
	private File computeFooterImagesDirectory(File mediaResourcesDirectory2) {
		if(mediaResourcesDirectory2 != null){
			File ParentDirectory = mediaResourcesDirectory2.getParentFile();
			return new File(ParentDirectory,
					EFGImportConstants.templateImagesDirectory);
		}
		return null;
	}
	/**
	 * 
	 * @param character
	 * @param state
	 * @param te
	 * @return
	 */
	private boolean addToSet(String state,TaxonEntryType te){		
		
		Set set = null;
		if(taxonEntryMap.containsKey(state)){
			set = (Set)taxonEntryMap.get(state);
		}
		else{
			set = new HashSet();
		}
		set.add(te);
		taxonEntryMap.put(state,set);
		
		
		return true;

	}
	/**
	 * 
	 * @param taxonEntryMap2
	 * @return
	 */
	private List handleTaxonEntries(Map taxonEntryMap2) {
		List displayList = new ArrayList();
		for (Iterator iter = this.sortBySet.iterator(); iter.hasNext();) {
			Set states = ((CharacterObject)iter.next()).getStates();
			for(Iterator iter1 = states.iterator(); iter1.hasNext();){
				String key = (String)iter1.next();
				if(key == null){
					continue;
				}
				Set set = (Set)taxonEntryMap2.get(key);
				if(set == null){
					continue;
				}
			
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					TaxonEntryType ttype  = (TaxonEntryType) iterator.next();
					List list = writeTaxonEntry(ttype);
					if(list != null){
						if(list.size() > 0){
							displayList.addAll(list);
						}	
					}
				}
			}
		}
		return displayList;
	}
	/**
	 * 
	 *
	 */
	private void readConfig(XslPage xslPage)throws Exception{
		if(xslPage == null){
			throw new Exception("XslPage is null");
		}
		this.pdfMaker = new PDFMaker(xslPage);
		this.sortBy = this.pdfMaker.getSortingFields();
	}
	private void writeErrorMessages(String message,
			float cellHeight){
		if(message == null || message.trim().equals("")){
			message = "Error occured during processing of document";
		}
		PdfPCell cell = new PdfPCell();
		if(this.pdfMaker.getNumberColumns() > 0){
			cell.setColspan(this.pdfMaker.getNumberColumns());
		}
		else{
			cell.setColspan(1);
		}
		if(cellHeight <= 0){
			cellHeight = 1f;
		}
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(cellHeight);
		cell.setBorder(0);
		cell.addElement(writeMessage(message));
		if(this.pdfTable == null){
			this.pdfTable = new PdfPTable(1);
			
		}
		this.pdfTable.addCell(cell);
	}
	/**
	 * 
	 * @param numberToWrite
	 */
	private void writeEmptyCells(int numberToWrite, 
			float cellHeight){
	
		
		for(int i = 0; i < numberToWrite;i++){
			PdfPCell cell = new PdfPCell();
			if(cellHeight <= 0){
				cellHeight = 1f;
			}
			cell.setFixedHeight(cellHeight);
			cell.setBorder(0);
			this.pdfTable.addCell(cell);
		}
	}
	/**
	 * 
	 *
	 */
	private void initDocument(OutputStream output,
			XslPage xslPage, 
			String authors)throws Exception{	
		
		try{
			this.readConfig(xslPage);
		}
		catch(Exception ee){
			throw new Exception("XslPage configuration is null. " +
					"Please consult the EFG team for help");
		}
		this.document = EFG2PDFDocumentMetadata.createDocumentMetadata(
				this.pdfMaker,
				this.getClass().getName(), 
				authors);
		this.tableCellWidth = this.getCellWidth();
		/*this.document = new Document(
				this.pdfMaker.getPaperSize(),
				left_margin,
				right_margin,
				top_margin,
				bottom_margin);*/
		
		try{
			this.writer = 
				PdfWriter.getInstance(this.document,output);
			document.open();
			if(!this.pdfMaker.isCaptionsExists()  && 
					(this.pdfMaker.getImage2DisplayFields() == null ||
							this.pdfMaker.getImage2DisplayFields().size() == 0)){
				
				throw new Exception("No Selections Made for caption/Image");
			}	
			if(this.pdfMaker.isSKIP_CELL_IF_NO_IMAGES() &&
					( this.pdfMaker.getImage2DisplayFields() == null ||
							this.pdfMaker.getImage2DisplayFields().size() == 0)){
				throw new Exception("Please select an image field or check the option" +
						" that allows you to print text only.");
			}
			this.writer.setStrictImageSequence(true);
			this.hd = new HeaderAndFooterHandler(this.pdfMaker,this.footerImagesDirectory);
			this.cellCalculus = 
				new EFG2PDFCellCalculations(this.pdfMaker,hd,this.footerImagesDirectory);
			this.textHeightAboveImage = cellCalculus.getTextHeightAboveImage();
			
			this.textHeightBelowImage = cellCalculus.getTextHeightBelowImage();
			this.fixedCellHeight = cellCalculus.getFixedCellHeight();
			this.imageHeight = cellCalculus.getImageHeight();
			this.writer.setPageEvent(new EndPage(cellCalculus,hd));		
			// step 3: we open the document
			
			// step 4: we create a table and add it to the document
			this.pdfTable = 
				new PdfPTable(this.pdfMaker.getNumberColumns());
			
			this.pdfTable.setWidthPercentage(100);
			//this.computeMaxTableWidth(this.pdfTable.getAbsoluteWidths());
			if(hd.isHeader() || hd.isFooter()){
				this.writeEmptyHeaderFooterCells();
			}
			this.sortBySet = new TreeSet(
					new CharacterCaptionSortingCriteria(
							this.pdfMaker.getSortingFields()
							)
					);
			this.allCaptions.addAll(this.pdfMaker.getCaptionsBelow());
			this.allCaptions.addAll(this.pdfMaker.getCaptionsAbove());
			
			
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			throw ee;
		}
	}


	/**
	 * 
	 * @param numberToWrite
	 */
	private void writeEmptyHeaderFooterCells(){
		int numberOfRows = 0;
		if(hd.isHeader() && this.hd.isFooter()){
			
			numberOfRows = 2;
			this.writeEmptyCells(this.pdfMaker.getNumberColumns(), 
					this.cellCalculus.getHeaderTextHeight());
			this.writeEmptyCells(this.pdfMaker.getNumberColumns(), 
					this.cellCalculus.getFooterHeight());
			this.pdfTable.setHeaderRows(numberOfRows);
			this.pdfTable.setFooterRows(numberOfRows-1);
			
		}
		else if(hd.isHeader()){
			this.writeEmptyCells(this.pdfMaker.getNumberColumns(), 
					this.cellCalculus.getHeaderTextHeight());
			numberOfRows = 1;
			this.pdfTable.setHeaderRows(numberOfRows);
			
		}
		else if (hd.isFooter()){
			numberOfRows = 1;
			this.writeEmptyCells(this.pdfMaker.getNumberColumns(), 
					this.cellCalculus.getFooterHeight());
			this.pdfTable.setHeaderRows(numberOfRows);
			this.pdfTable.setFooterRows(numberOfRows);
			
		}
	}
	/**
	 * 
	 * @param imageName
	 * @return
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private PdfPCell getImageCell(String imageName) 
		throws BadElementException, 
		MalformedURLException, 
			IOException{
		
		
		if(this.imageHeight <= 0){
			log.debug("this.imageHeight <= 0 ");
			return null;
		}
		Image image  = null;
		PdfPCell imageCell = null;
		try{
			if(imageName != null && !imageName.trim().equals("")){
				image = Image.getInstance(new File(this.mediaResourcesDirectory,imageName).toURL());
				Color c = Color.decode(this.pdfMaker.getCOLOR_FOR_FRAME_AROUND_IMAGE());
				image.setBorder(Image.BOX);
				image.setBorderColor(c);
				
				image.setBorderWidth((float)this.pdfMaker.getWEIGHT_FRAME_AROUND_IMAGE());
				
				image.scaleAbsoluteHeight(this.cellCalculus.getImageHeight());
				if(image != null){
					log.debug("DPI Y B: " + image.getDpiY());
					log.debug("DPI X B: " + image.getDpiX());
					log.debug("Image Height: " + image.height());
					log.debug("Image Cell Height: " +
							this.cellCalculus.getImageHeight());
					log.debug("Scaled Height: " + image.scaledHeight());
					log.debug("Scaled Width: " + image.scaledWidth());
					log.debug("");
					
					//default dpi read from a properties file
					image.setDpi(360, 360);				
					log.debug("DPI Y: " + image.getDpiY());
					log.debug("DPI X: " + image.getDpiX());
					log.debug("");
				}
			}
		}
		catch(Exception ee){
			log.error(ee.getMessage());
		}
		if(image == null){
			imageCell = new PdfPCell();
		}
		else{
			imageCell = new PdfPCell(image,true); 
		}		
		imageCell.setPadding(0);
		imageCell.setFixedHeight(this.imageHeight);
		imageCell.setBorder(0);
		if(this.pdfMaker.isTextAboveImage() && !this.pdfMaker.isTextBelowImage()){
			imageCell.setVerticalAlignment(Element.ALIGN_TOP);
		}
		else{
			imageCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		}
		imageCell.setHorizontalAlignment(
				this.pdfMaker.getImageHorizontalAlignment(imageName)
				);
		imageCell.setUseBorderPadding(true);
		imageCell.setPadding(this.pdfMaker.getWEIGHT_WHITE_SPACE_AROUND_IMAGE());
		return imageCell;
	}
	private PdfPCell addTextCellBelowImage(List captionsList) {
		if(this.textHeightBelowImage > 0){
			return this.getTextCell(captionsList,
					this.pdfMaker.getCaptionsBelow(),
					this.textHeightBelowImage);
		}
		return null;
	}
	private PdfPCell addTextCellAboveImage(List captionsList) {
		if(this.textHeightAboveImage > 0){
			return this.getTextCell(captionsList,
					this.pdfMaker.getCaptionsAbove(),
					this.textHeightAboveImage);
		}
		return null;
	}
	private float getCellWidth(){
		if(this.document == null){
			return 0;
		}
	   float cellWidth = this.document.getPageSize().width() - 
		this.document.leftMargin() - 
		this.document.rightMargin(); 
	   cellWidth = cellWidth/this.pdfMaker.getNumberColumns();
	  return cellWidth;
	}
	private Paragraph addCaptionsToParagraph(Set states,
			CaptionFontObject co){
		
		Font font = co.getFont();
		Phrase ph = null;
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for(Iterator iter = states.iterator(); iter.hasNext();){
			String state = (String)iter.next();
			if(i > 0){
				buffer.append(", ");
			}
			buffer.append(state);
			i++;
		}
		String state = this.cellCalculus.truncateText(co, 
				buffer.toString(),
				this.tableCellWidth);	
	
		if(state == null || state.trim().equals("")){
			state="\n";
		}
	
		Chunk chunk = new Chunk(state,font);
		if(co.isUnderLine()){
			chunk.setUnderline(isUnderline_thickness,
					isUnderline_y_position);
		}
		ph = new Phrase(chunk);
	
		Paragraph p = 
			new Paragraph(ph);
		
		p.setAlignment(co.getAlignment());
		return p;
	}
	/**
	 * 
	 * @param captionsList
	 * @param set 
	 * @return
	 */
	private PdfPCell getTextCell(List captionsList, Set captions, float height){
		
		PdfPCell textCell = new PdfPCell();
		textCell.setPadding(0);
		textCell.setFixedHeight(height);
		textCell.setBorder(0);
		textCell.setBorderWidth(0);
		textCell.setUseBorderPadding(true);
		
		Set captionsSet = this.sortCharacters(captionsList,captions);
		for (Iterator iter = captionsSet.iterator(); iter.hasNext();) {
			CharacterObject characterObject = (CharacterObject)iter.next();
			CaptionFontObject co = 
				this.getCaptionObject(captions,
						characterObject.getCharacter()
						);
			if(co != null){	
				Set states = characterObject.getStates();
				textCell.addElement(addCaptionsToParagraph(states,co));
			}
		}	
	
		return textCell;
	}
	/**
	 * @param captionsList
	 * @param captions
	 * @return
	 */
	private Set sortCharacters(List captionsCharacterList, Set captions) {
		
		Set set = new TreeSet(new EFGRankObjectSortingCriteria());
		
		for (Iterator iter = captionsCharacterList.iterator(); iter.hasNext();) {
			CharacterObject characterObject = (CharacterObject)iter.next();
			CaptionFontObject co = 
				this.getCaptionObject(captions,
						characterObject.getCharacter()
						);
			
			if(co != null){
				characterObject.setRank(co.getRank());

				set.add(characterObject);
			}
		}
		return set;
	}
	/* (non-Javadoc)
	 * @see project.efg.efgpdf.pdf.EFG2PDFInterface#writeToPdf(java.lang.String, project.efg.efgpdf.pdf.EFG2PDF.DisplayObject)
	 */
	private void writeToPdf(String imageName,DisplayObject object) {
		PdfPCell imageCell = null;
		
		try{
			
			if(this.imageHeight > 0){
				imageCell = this.getImageCell(imageName);
			}
		}
		catch(Exception ee){
			log.error(ee.getMessage());
		}
		PdfPTable table1 = new PdfPTable(1);
		table1.setSpacingAfter(0);
		table1.setSpacingBefore(0);
		table1.setWidthPercentage(100);
		PdfPCell textCell = null;
		if(this.textHeightAboveImage > 0){
			textCell = addTextCellAboveImage(object.getCaptionsList());
			if(textCell != null){
				table1.addCell(textCell);
			}
		}
		if(imageCell != null){
			table1.addCell(imageCell);
		}
		if(this.textHeightBelowImage > 0){
			textCell = addTextCellBelowImage(object.getCaptionsList());
			if(textCell != null){
				table1.addCell(textCell);
			}
		}
		PdfPCell mainCell = new PdfPCell();
		//difference between border and borderwidth
		if(this.pdfMaker.getWEIGHT_BOUNDING_BOX() > 0){
			mainCell.setBorder(Rectangle.BOX);
		}
		else{
			mainCell.setBorder(0);
		}
		mainCell.setUseBorderPadding(true);
	
		mainCell.setBorderWidth(this.pdfMaker.getWEIGHT_BOUNDING_BOX());
		mainCell.setBorderColor(Color.decode(
				this.pdfMaker.getCOLOR_FOR_BOUNDING_BOX()
				));
		mainCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainCell.setFixedHeight(fixedCellHeight);					
		mainCell.addElement(table1);
		//covers the whole margin
		
		++this.numberOfCellsCounter;
	
		this.pdfTable.addCell(mainCell);
	}
	private boolean writeTableRow(DisplayObject object,
			String imageName,
			int numberOnpage,
			int rowNumber){
		boolean isHead = false;
		boolean isFoot = false;
		int numberN = numberOnpage;
		if(imageName == null || imageName.trim().equals("")){
			//means skip the cell(do not write the cell) if no image is available
			if(this.pdfMaker.isSKIP_CELL_IF_NO_IMAGES()){
				return false;
			}
		}
		else{
			File file = new File(this.mediaResourcesDirectory,imageName);
			if(!file.exists()){
				//means skip the cell(do not write the cell) if no image is available
				if(this.pdfMaker.isSKIP_CELL_IF_NO_IMAGES()){
					return false;
				}	
			}
		}
		if(this.hd.isHeader()){
			isHead = true;
			numberN = numberN + 1;
		}
		if(this.hd.isFooter()){
			isFoot = true;
			numberN = numberN + 1;
		}
		if(isHead && !isFoot){
			/*if(rowNumber % numberN == 0){
			
				try {
					this.document.add(this.pdfTable);
					this.pdfTable.deleteBodyRows();
					this.pdfTable.setSkipFirstHeader(true);
					
				} catch (DocumentException e) {
					
					
					log.error(e.getMessage());
					return false;
				}	
			}*/
		}
		
		writeToPdf(imageName,object);
		return true;
		
	}
	/**
	 * 
	 * @param list
	 */
	private void writeObject(List list) {
		int row = 1;
		
		int numberOnpage = this.pdfMaker.getNumberRows()*this.pdfMaker.getNumberColumns();
		
		//if there is text above image then compute maximum image height
		//reduce the text height 
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			DisplayObject object = (DisplayObject) iter.next();
			Set images = object.getImagesSet();	
			
			if(images.size() == 0){						
				if(writeTableRow(object,null,numberOnpage,row)){					
					++row;
				}
			}
			else{
				for (Iterator iterator = images.iterator(); iterator.hasNext();) {
					String imageName = (String) iterator.next();
					
					if(writeTableRow(object,imageName,numberOnpage,row)){					
						++row;
					}
				}
			}
		}
	}
	private List getAllCaptions(){
		List list = new ArrayList();
		for (int i = 0; i < this.allCaptions.size();i++) {
			CaptionFontObject element = (CaptionFontObject)this.allCaptions.get(i);
			String character = element.getCaption();
			list.add(new String(character));
		}
		return list;
	}
	/**
	 * 
	 * @param ttype
	 * @return
	 */
	private List writeTaxonEntry(TaxonEntryType ttype){
		List displayList = new ArrayList();
		int current_count = ttype.getTaxonEntryTypeItemCount();
		DisplayObject ds = new DisplayObject();
		List captionsL = this.getAllCaptions();
		for(int index1 = 0; index1 < current_count; ++index1){			
			TaxonEntryTypeItem titem = ttype.getTaxonEntryTypeItem(index1);			
			ItemsType its = titem.getItems();
			MediaResourcesType mts= titem.getMediaResources();
			EFGListsType ets = titem.getEFGLists();
			String character = null;

			if(its != null){			
				character = its.getName();
				if(character != null){
					if(isCaption(character)){	
						captionsL.remove(character);
						CharacterObject co = new CharacterObject();
						co.setCharacter(character);
						ds.addCaption(co);
						boolean isAbsent = false;
						for(int j = 0; j < its.getItemCount(); j++){
							Item item = its.getItem(j);
							String state = item.getContent();
							if(state == null){
								state = "";
							}
							co.addState(state);
							isAbsent = true;
						}
						if(!isAbsent ){
							co.addState("");
						}
					}
					
				}
			}
			if(ets != null){
				character = ets.getName();
				if(character != null){
					if(isCaption(character)){	
						captionsL.remove(character);
						CharacterObject co = new CharacterObject();
						co.setCharacter(character);
						ds.addCaption(co);
						boolean isAbsent = false;
						for(int j = 0; j < ets.getEFGListCount(); j++){
							EFGType item = ets.getEFGList(j);
							String state = item.getContent();	
							co.addState(state);
							isAbsent = true;
						}						
						if(!isAbsent ){
							co.addState("");
						}

					}
				}
			}
			if(mts != null){
				character = mts.getName();
				if(character != null){
					
					if(this.pdfMaker.getImage2DisplayFields().contains(character)){
						
						for(int j = 0; j <  mts.getMediaResourceCount(); j++){
							MediaResourceType item =mts.getMediaResource(j);
							String imageName = item.getContent();
							if(imageName != null && !imageName.trim().equals("")){
								
								ds.addImage(imageName);								
								if(isImageExists(imageName) && !this.pdfMaker.isSHOW_ALL_IMAGES()){
									break;
								}
							}
						}
					}
				}
			}
		}
		if(captionsL.size() > 0 ){
			for(int z = 0 ; z < captionsL.size();z++){
				CharacterObject co = new CharacterObject();
				co.setCharacter((String)captionsL.get(z));
				co.addState("");
				ds.addCaption(co);
			}
		}

		if(ds.getCaptionsList().size() > 0 ||
			ds.getImagesSet().size() > 0){		
			displayList.add(ds);
		}
		return displayList;
	}
	/**
	 * @param imageName
	 * @return
	 */
	private boolean isImageExists(String imageName) {
		if(imageName == null || imageName.trim().equals("")){
			return false;
		}
		
		return  new File(this.mediaResourcesDirectory,imageName).exists();
	}
	/**
	 * 
	 * @param character
	 * @return
	 */
	private boolean isCaption(String character) {
		for (int i = 0; i < this.allCaptions.size();i++) {
			CaptionFontObject element = (CaptionFontObject)this.allCaptions.get(i);
			String caption = element.getCaption();
			if(character.equals(caption)){
				return true;
			}
		}
		return false;
	
	}
	/**
	 * 
	 * @param character
	 * @return
	 */
	private CaptionFontObject getCaptionObject(Set set,String character){
		
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			CaptionFontObject element = (CaptionFontObject) iter.next();
		
			String caption = element.getCaption();
			if(character.equals(caption)){
				return element;
			}
		}
		return null;
	}
	class DisplayObject{

		private String colorCode;
		private List captionsList;
		private Set imagesList;
		
		public DisplayObject() {
			this.captionsList = new ArrayList();
			this.imagesList = new TreeSet();
		}
		
		public void setCaptions(List captions){
			this.captionsList.addAll(captions);
		}
		public void setImages(Set imagesList){
			this.imagesList.addAll(imagesList);
		}
		public void addImage(String image) {
			
			this.imagesList.add(image);
		}
		public CharacterObject getCaption(int index) {
			return (CharacterObject)this.captionsList.get(index);
		}
		public void addCaption(CharacterObject caption) {
			
			this.captionsList.add(caption);
		}
		public String getColorCode() {
			return colorCode;
		}
		public void setColorCode(String colorCode) {
			this.colorCode = colorCode;
		}
		public String toString(){
			return this.getCaptionsList().toString();
		}
		public List getCaptionsList() {
			return this.captionsList;
		}
		public Set getImagesSet() {
			return this.imagesList;
		}

	}
	public class CharacterCaptionSortingCriteria implements Comparator{
		private List sortingObjects;
		public CharacterCaptionSortingCriteria(List sortingObjects){
			this.sortingObjects = sortingObjects;
		}
		/**
		 * 
		 */
		public int compare(Object o1, Object o2) {
			CharacterObject sorter1 = 
				(CharacterObject)o1;
			String character1 = sorter1.getCharacter();
			int index1 = this.sortingObjects.indexOf(character1);
			CharacterObject sorter2 = 
				(CharacterObject)o2;
			String character2 = sorter2.getCharacter();

			int index2 = this.sortingObjects.indexOf(character2);
			if(index1 == index2){
				return compareSets(sorter1.getStates(),sorter2.getStates());
			}
			else if(index1 < index2){
				return 1;
			}
			return -1;
		}
	}
	private int compareSets(SortedSet set1, SortedSet set2){
		
		
		if(set1 != null && set2 != null){
			if(set1.size() > 0 &&  set2.size() > 0){
				String set1State = (String)set1.first();
				String set2State = (String)set2.first();
				return set1State.compareToIgnoreCase(set2State);
			}
			else if(set1.size() > 0){
				return 1;
			}
		}
		return 0;
		
	}
	class CharacterObject extends EFGRankObject{
		private String character;
		
		private SortedSet states;
		public CharacterObject() {
			super();
			this.states = new TreeSet();
		}
		public CharacterObject(String character, String state) {
			super();
			this.setCharacter(character);
			this.addState(state);
		}
		public boolean equals(Object object1){
			CharacterObject obj = (CharacterObject)object1;
			String currentState = (String)this.getStates().first();
			String objState = (String)obj.getStates().first();
			
			return currentState.equals(objState);
		}
		public int hashCode(){
			return this.getStates().hashCode();
		}
		public String getCharacter() {
			return character;
		}
		public void setCharacter(String character) {
			this.character = character;
		}
		public SortedSet getStates() {
			return this.states;
		}
		public void addState(String state) {
			this.states.add(state);
		}
		public String toString(){
			return ("Character: " + this.getCharacter() + 
					"  State: " + this.getStates());
		}

	}


}

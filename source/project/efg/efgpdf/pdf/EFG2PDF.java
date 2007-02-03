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
import java.util.TreeSet;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import project.efg.efgDocument.EFGDocument;
import project.efg.efgDocument.Item;
import project.efg.efgDocument.ItemsType;
import project.efg.efgDocument.MediaResourceType;
import project.efg.efgDocument.MediaResourcesType;
import project.efg.efgDocument.TaxonEntries;
import project.efg.efgDocument.TaxonEntryType;
import project.efg.efgDocument.TaxonEntryTypeItem;
import project.efg.efgpdf.pdf.PDFMaker.CaptionFontObject;
import project.efg.servlets.efgServletsUtil.LoggerUtilsServlet;
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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class EFG2PDF{
	/*
	 * use properties file or spring for font
	 * Load font mappings from properties file
	 * 
	 * This file should be used to load the 
	 * html interface
	 * 
	 */

	/*Load from a proeprties file*/
	static Map fontFamilyMap;
	static int counter = 1;
	static{
		fontFamilyMap = new HashMap();
		fontFamilyMap.put("arial",Font.HELVETICA+"");
		fontFamilyMap.put("times", Font.TIMES_ROMAN +"");
	}
	/*static{
		colorCodeMap.put("INVASIVE","#E90B1F");
		colorCodeMap.put("LIKELY INVASIVE","#FB9419");
		colorCodeMap.put("POTENTIAL INVASIVE","#FBFA19");
		colorCodeMap.put("DO NOT LIST AT THIS TIME","#1940FB");
		colorCodeMap.put("NOT EVALUATED","#1940FB");
	}*/
	
	private File imagesDirectory;
	private Set sortBySet;
	private Map taxonEntryMap;
	private Document document; 
	private PdfPTable pdfTable; 
	private PdfWriter writer;
	private float tableHeight;
	private float fixedCellHeight;
	private PDFMaker pdfMaker;
	private List sortBy;
	private List captions;
	private int numberOfCellsCounter;
	private EFGDocument efgDoc;
	private XslPage xslPage;
	private OutputStream out;
	private static int left_margin = 
	EFG2PDFConstants.DEFAULT_LEFT_MARGIN;

	static int right_margin=
		EFG2PDFConstants.DEFAULT_RIGHT_MARGIN;

	static int top_margin= 
		EFG2PDFConstants.DEFAULT_TOP_MARGIN;

	static int bottom_margin = 
		EFG2PDFConstants.DEFAULT_BOTTOM_MARGIN;
	
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
					"underline_thickness");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_thickness = 
				Float.parseFloat(property);
		}
		property= 
			EFGImportConstants.EFGProperties.getProperty(
					"underline_y");
		if(property != null && 
				!property.trim().equals("")){
			isUnderline_y_position = 
				Float.parseFloat(property);
		}
		property =
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
	public EFG2PDF(
			XslPage xslPage, 
			EFGDocument efgDoc,
			File imagesDirectory,OutputStream out){
		this.efgDoc = efgDoc;
		this.xslPage = xslPage;
		
		this.imagesDirectory = imagesDirectory;
		this.out = out;
	
	
	}
	private void initPDFMaker()throws Exception{
		if(this.xslPage == null){
			
			throw new Exception("XslPage configuration is null. " +
					"Please consult the EFG team for help");
		}
		
		this.pdfMaker = new PDFMaker(this.xslPage);
		this.sortBy = this.pdfMaker.getSortingFields();
		
		this.numberOfCellsCounter = 0;
		this.taxonEntryMap = new HashMap();		
		this.sortBySet = new TreeSet(
				new SortingCriteria(
						this.sortBy
						)
				);
		
		this.captions = this.pdfMaker.getCaptions();
		if((this.captions == null || 
				this.captions.size()== 0 ) && 
				(this.pdfMaker.getImage2DisplayFields() == null ||
						this.pdfMaker.getImage2DisplayFields().size() == 0)){
			
			throw new Exception("No Selections Made for caption/Image");
		}
	}
	private boolean addToSet(String character, String state,TaxonEntryType te){
		CharacterObject charactero = new CharacterObject();
		charactero.setCharacter(character);
		charactero.setState(state);
		this.sortBySet.add(charactero);
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
	 * @param reader
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	public void writePdfToStream(){ 
		//if there is an error throw an exception
		try {
			
			this.initPDFMaker();
			this.initDocument();
			if(this.efgDoc == null){
				
				throw new Exception("The EFGDocument is null");
			}
			
			TaxonEntries ts = this.efgDoc.getTaxonEntries();
			int ts_count = ts.getTaxonEntryCount();
			
			for(int index = 0;index < ts_count;++index){
				TaxonEntryType te = ts.getTaxonEntry(index);
				int current_count = te.getTaxonEntryTypeItemCount();
				for(int index1 = 0; index1 < current_count; ++index1){
					boolean isFound = false;
					TaxonEntryTypeItem titem = 
						te.getTaxonEntryTypeItem(index1);
					
					ItemsType its = titem.getItems();
					MediaResourcesType mts = 
						titem.getMediaResources();
					String character = null;
					if(its != null){				
						character = its.getName();
						if(this.sortBy.contains(character)){//depends 
							//on position of the character in the sortby list
							for(int j = 0; j < its.getItemCount(); j++){
								Item item = its.getItem(j);
								isFound = addToSet(character, 
										item.getContent(),te);
								break;
							}
						}
					}
					if(mts != null){
						character = mts.getName();
						if(this.sortBy.contains(character)){//depends 
							//on position of the character in the sortby list
							for(int j = 0; j < mts.getMediaResourceCount(); j++){
								MediaResourceType item = mts.getMediaResource(j);
								isFound = addToSet(character, 
										item.getContent(),te);								
								break;
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
				this.writeEmptyCells(numberOfCellsNotWritten);
			}
			document.add(pdfTable);
		} catch (Exception e) {
			try {
				
				e.printStackTrace();
				if(this.document == null){
					
					this.document = new Document(
							EFG2PDFConstants.DEFAULT_PAPER_SIZE);
					this.writer = PdfWriter.getInstance(this.document,this.out);
					document.open();
				}
				if(e == null || e.getMessage().trim().equals("")){
					
					this.document.add(new Paragraph("An error occured " +
					"during the processing of pdf document."));
					
				}
				else{
				
					this.document.add(new Paragraph(e.getMessage()));
				}
			} catch (DocumentException e1) {
				
				
				//e1.printStackTrace();
			}
			
			LoggerUtilsServlet.logErrors(e);
		}
		document.close();
		
	}

	/**
	 * 
	 * @param taxonEntryMap2
	 * @return
	 */
	private List handleTaxonEntries(Map taxonEntryMap2) throws Exception{
		List displayList = new ArrayList();
		
		for (Iterator iter = this.sortBySet.iterator(); iter.hasNext();) {
			
			String key = ((CharacterObject)iter.next()).getState();
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
					displayList.addAll(list);
				}
			}
		}	
		
		return displayList;
	}

	/**
	 * 
	 * @param numberToWrite
	 */
	private void writeEmptyCells(int numberToWrite)throws Exception{
		for(int i = 0; i < numberToWrite;i++){
			PdfPCell cell = new PdfPCell();
			cell.setBorder(0);
			this.pdfTable.addCell(cell);
			
		}
	}
	/**
	 * @param output 
	 * 
	 *
	 */
	private void initDocument() throws Exception{	
		try{		
			
			this.document = new Document(
					this.pdfMaker.getPaperSize(),
					left_margin,
					right_margin,
					top_margin,
					bottom_margin);
			this.writer = PdfWriter.getInstance(this.document,this.out);
			document.open();

			//read margins from a properties file
			//should map paper size to margins
			
			//this.document = new Document(this.pdfMaker.getPaperSize());
			
			
			writer.setPageEvent(new EndPage(this.pdfMaker,this.imagesDirectory));
		
			// step 3: we open the document
			
			// step 4: we create a table and add it to the document
			this.pdfTable = 
				new PdfPTable(this.pdfMaker.getNumberColumns());
			this.pdfTable.setWidthPercentage(100);
			this.tableHeight = 
				this.pdfMaker.getPaperSize().height()-
			(top_margin+bottom_margin);
			this.fixedCellHeight = 
				this.tableHeight/this.pdfMaker.getNumberRows();
		}
		catch(Exception ee){
			
			throw ee;
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
			IOException, Exception{
		
		File file = new File(this.imagesDirectory,imageName);
		
		if(!file.exists()){
			return null;
		}
		Image image = Image.getInstance(file.toURL());
		
		image.scalePercent(72f/image.getDpiX() * 100);
		PdfPCell imageCell = new PdfPCell(image, true); 
		
		imageCell.setFixedHeight(fixedCellHeight*3/5);
		Color c = Color.decode(this.pdfMaker.getCOLOR_FOR_FRAME_AROUND_IMAGE());
	
		imageCell.setBorderColor(c);
		imageCell.setBorderColorBottom(c);
		imageCell.setBorderColorLeft(c);
		imageCell.setBorderColorRight(c);
		imageCell.setBorderColorTop(c);
		

		imageCell.setUseBorderPadding(true);
		imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		imageCell.setBorderWidth((float)this.pdfMaker.getWEIGHT_FRAME_AROUND_IMAGE());
		imageCell.setPadding(this.pdfMaker.getWEIGHT_WHITE_SPACE_AROUND_IMAGE());
		
		if(this.pdfMaker.isFILL_SPACE_AROUND_DISP_IMAGES()){
			imageCell.setBackgroundColor(
					Color.decode(this.pdfMaker.getCOLOR_SPACE_AROUND_DISPROP_IMAGES()
					)
				);
		}
		return imageCell;
	}

	/**
	 * 
	 * @param captionsList
	 * @return
	 */
	private PdfPCell getTextCell(List captionsList) throws Exception{
		
		PdfPCell textCell = new PdfPCell();
		textCell.setBorderWidth(0);
		textCell.setUseBorderPadding(true);
				
		for (Iterator iter = captionsList.iterator(); iter.hasNext();) {
			CharacterObject characterObject = (CharacterObject)iter.next();
			CaptionFontObject co = this.getCaptionObject(characterObject.getCharacter());
			
			if(co != null){	
				Font font = co.getFont();
				Chunk chunk = new Chunk(characterObject.getState(),font);
				if(co.isUnderLine()){
					chunk.setUnderline(
							isUnderline_thickness, 
							isUnderline_y_position);
				}
				Paragraph p = new Paragraph(chunk);
				p.setAlignment(Element.ALIGN_CENTER);
				textCell.addElement(p);	
			}
		}	
		return textCell;
	}
	/**
	 * 
	 * @param imageName
	 * @param object
	 */
	private void writeToPdf(String imageName,
			DisplayObject object)throws Exception {

		PdfPCell imageCell = null;
		try{
			imageCell = this.getImageCell(imageName);
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		
		PdfPTable table1 = new PdfPTable(1);
	
		PdfPCell textCell = null;
		if(object.getCaptionsList() != null && 
				object.getCaptionsList().size() > 0){
			textCell = this.getTextCell(object.getCaptionsList());
		}
		if(this.pdfMaker.isTextAboveImage()){
			if(textCell != null){
				table1.addCell(textCell);
			}
			if(imageCell != null){
				table1.addCell(imageCell);
			}
		}
		else{
			if(imageCell != null){
				table1.addCell(imageCell);
			}
			if(textCell != null){
				table1.addCell(textCell);
			}
		}
		if(textCell == null && 
				imageCell == null){
			return;
		}
		PdfPCell mainCell = new PdfPCell();

		mainCell.setUseBorderPadding(true);
		mainCell.setBorderWidth(this.pdfMaker.getWEIGHT_BOUNDING_BOX());
		mainCell.setBorderColor(Color.decode(
				this.pdfMaker.getCOLOR_FOR_BOUNDING_BOX()
				));
		mainCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainCell.setFixedHeight(fixedCellHeight);					
		mainCell.addElement(table1);
		//covers the whole margin
		this.pdfTable.setWidthPercentage(100);
		++this.numberOfCellsCounter;
		this.pdfTable.addCell(mainCell);
	}
	/**
	 * 
	 * @param list
	 */
	private void writeObject(List list) throws Exception{
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			DisplayObject object = (DisplayObject) iter.next();
			List images = object.getImagesList();	
			if(images.size() == 0){
				continue;
			}
			for (Iterator iterator = images.iterator(); iterator.hasNext();) {
				String imageName = (String) iterator.next();
				this.writeToPdf(imageName,object);
			}
		}
	}
	/**
	 * 
	 * @param ttype
	 * @return
	 */
	private List writeTaxonEntry(TaxonEntryType ttype)throws Exception{
		List displayList = new ArrayList();
		int current_count = ttype.getTaxonEntryTypeItemCount();
		DisplayObject ds = new DisplayObject();
		for(int index1 = 0; index1 < current_count; ++index1){			
			TaxonEntryTypeItem titem = 
				ttype.getTaxonEntryTypeItem(index1);			
			ItemsType its = titem.getItems();
			MediaResourcesType mts = 
				titem.getMediaResources();
			String character = null;

			if(its != null){				
				character = its.getName();
				if(character != null){
					if(isCaption(character)){	
						for(int j = 0; j < its.getItemCount(); j++){
							Item item = its.getItem(j);
							String state = item.getContent();
							ds.addCaption(new CharacterObject(character,state));
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
							String state = item.getContent();
							ds.addImage(state);
						}
					}
				}
			}
		}	
		if(ds.getCaptionsList().size() > 0 ||
			ds.getImagesList().size() > 0){		
			displayList.add(ds);
		}
		
		return displayList;
	}
	/**
	 * 
	 * @param character
	 * @return
	 */
	private boolean isCaption(String character) throws Exception{
		CaptionFontObject element = getCaptionObject(character);
		if(element == null){
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param character
	 * @return
	 */
	private CaptionFontObject getCaptionObject(String character)throws Exception{
		
		for (int i = 0; i < this.captions.size();i++) {
			CaptionFontObject element = (CaptionFontObject)this.captions.get(i);
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
		private List imagesList;
		
		public DisplayObject() {
			this.captionsList = new ArrayList();
			this.imagesList = new ArrayList();
		}
		
		public void setCaptions(List captions){
			this.captionsList.addAll(captions);
		}
		public void setImages(List imagesList){
			this.imagesList.addAll(imagesList);
		}
		public String getImage(int index) {
			return (String)this.imagesList.get(index);
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
		public List getImagesList() {
			return this.imagesList;
		}

	}
	public class SortingCriteria implements Comparator{
		private List sortingObjects;
		public SortingCriteria(List sortingObjects){
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
				return sorter1.getState().compareTo(sorter2.getState());
			}
			else if(index1 < index2){
				return 1;
			}
			return -1;

		}
	}
	class CharacterObject{
		private String character;
		private String state;
		public CharacterObject() {
		}
		public CharacterObject(String character, String state) {
			this.setCharacter(character);
			this.setState(state);
		}
		public boolean equals(Object object1){
			CharacterObject obj = (CharacterObject)object1;
			return this.getState().equalsIgnoreCase(obj.getState());
		}
		public int hashCode(){
			return this.getState().hashCode();
		}
		public String getCharacter() {
			return character;
		}
		public void setCharacter(String character) {
			this.character = character;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String toString(){
			return ("Character: " + this.getCharacter() + 
					"  State: " + this.getState());
		}
	}
	public static void main(String[] args) {
		
		
	}



}

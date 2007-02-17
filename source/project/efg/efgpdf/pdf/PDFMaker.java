package project.efg.efgpdf.pdf;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;


import project.efg.templates.taxonPageTemplates.CharacterValue;
import project.efg.templates.taxonPageTemplates.GroupType;
import project.efg.templates.taxonPageTemplates.GroupTypeItem;
import project.efg.templates.taxonPageTemplates.GroupsType;
import project.efg.templates.taxonPageTemplates.GroupsTypeItem;
import project.efg.templates.taxonPageTemplates.XslPage;
import project.efg.util.EFGImportConstants;
import project.efg.util.PDFGUIConstants;
import project.efg.util.RegularExpresionConstants;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;


public class PDFMaker implements PDFGUIConstants{
	static Logger log;
	static {
		try {
			log = Logger.getLogger(PDFMaker.class);
		} catch (Exception ee) {
		}
	}
	/*
	 * Variables for handling cell properties
	 */
	private int WEIGHT_WHITE_SPACE_AROUND_IMAGE;

	private float WEIGHT_FRAME_AROUND_IMAGE;

	/**
	 * border weight around image
	 */
	private String COLOR_FOR_FRAME_AROUND_IMAGE;// border color around image

	private int WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS;

	/*
	 * image plus captions maps to outer cell padding
	 * 
	 */
	private int WEIGHT_BOUNDING_BOX;

	/*
	 * frame image plus captions maps to border around outer cell
	 */
	private String COLOR_FOR_BOUNDING_BOX;

	/**
	 * maps to border color around outer cell
	 */

	private boolean FILL_SPACE_AROUND_DISP_IMAGES;
	/**
	 * End variable definitions
	 * 
	 */
	private Rectangle PAPER_SIZE;

	private int NUMBER_OF_ROWS;

	private int NUMBER_OF_COLUMNS;

	
	private List footerImagesList;

	private CaptionFontObject mainTitleObject;

	private CaptionFontObject subTitleObject;

	private CaptionFontObject copyRight1Object;

	private CaptionFontObject copyRight2Object;

	private SortedSet captionsBelowSet;
	private SortedSet captionsAboveSet;
	private List sortingList;
	private boolean SHOW_ALL_IMAGES;
	private boolean SKIP_CELL_IF_NO_IMAGES;
	private List image2DisplayFields;

	private XslPage xslPage;

	private boolean isPortrait;
	static Map alignMap;
	private int imageVerticalAlignment = Element.ALIGN_UNDEFINED;

	private int imageHorizontalAlignment= Element.ALIGN_CENTER; 
	
	static Map paperSizes;

	private static Map orientationMap;
	static {
		loadPaperSizeProperties();
	}
	static {	
		loadAlignmentProperties();	
	}
	/**
	 * @param property
	 * @param papersizesuffix
	 * @return
	 */
	private static String[] parseEFG(String property, String separator) {
		String[] splits = property.split(separator);
		
		return  splits;
		
	}
	private static void loadAlignmentProperties(){
		orientationMap = new HashMap();
		orientationMap.put("portrait",new Boolean("true"));
		orientationMap.put("landscape",new Boolean("false"));
		alignMap = new HashMap();
		alignMap.put("left", new Integer(Element.ALIGN_LEFT));
		alignMap.put("right",new Integer(Element.ALIGN_RIGHT));
		alignMap.put("center", new Integer(Element.ALIGN_CENTER));
		alignMap.put("top", new Integer(Element.ALIGN_TOP));
		alignMap.put("bottom", new Integer(Element.ALIGN_BOTTOM));	
		alignMap.put("middle", new Integer(Element.ALIGN_MIDDLE));	
		alignMap.put("justified", new Integer(Element.ALIGN_JUSTIFIED));
	}
	private static void loadPaperSizeProperties(){
		paperSizes = new HashMap();
		Properties props = EFGImportConstants.EFGProperties;
		String papersizesuffix = props.getProperty("papersize_suffix");
		Enumeration enum2 = props.propertyNames();
		
		while(enum2.hasMoreElements()){
			String property = (String)enum2.nextElement();
			if(property.endsWith(papersizesuffix)){
				String[] key = parseEFG(property,papersizesuffix);
				String[] val = parseEFG(props.getProperty(property), 
						RegularExpresionConstants.COMMASEP);
				
				paperSizes.put(key[0], 
						new Rectangle(Integer.parseInt(val[0]),
								Integer.parseInt(val[1])));
				
			}
		}
		
	}
	public PDFMaker(XslPage xslPage) {
		this.isPortrait = true;
		this.xslPage = xslPage;
		this.setSKIP_CELL_IF_NO_IMAGES(true);
		//show only one image
		this.setSHOW_ALL_IMAGES(false);
		this.initObjects();
		this.readProperties();
		
	}
	private void setPortraitOrientation(boolean isPortrait ){
		this.isPortrait = isPortrait;
	}
	public boolean isPortraitOrientation(){
		return this.isPortrait;
	}	
	public int getVerticalAlignmentForImages(){
		if(this.imageVerticalAlignment > Element.ALIGN_UNDEFINED){
			return this.imageVerticalAlignment;
		}
		if(this.captionsBelowSet.size() > 0){
			this.imageVerticalAlignment = findAlignment(this.captionsBelowSet,true);
		}
		else if(this.captionsAboveSet.size() > 0){
			this.imageVerticalAlignment = findAlignment(this.captionsAboveSet,false);
		}
		else{
			this.imageVerticalAlignment =  ((Integer)alignMap.get("left")).intValue();
		}
		return this.imageVerticalAlignment;
	}

	/**
	 * @param captionsBelowSet2
	 * @param b
	 * @return
	 */
	/**
	 * @param captionsBelowSet2
	 * @param b
	 * @return
	 */
	private int findAlignment(SortedSet captionSet, boolean findLowestRank) {
		if(captionSet.size() > 0){
			if(findLowestRank){
				return ((CaptionFontObject)captionSet.first()).getAlignment();
			}
			else{
				return ((CaptionFontObject)captionSet.last()).getAlignment();
			}
		
		}
		return Element.ALIGN_LEFT;
	}
	public int getImageHorizontalAlignment(String imageName){
		return this.imageHorizontalAlignment;
	}
	private void initObjects(){
		this.captionsBelowSet = new TreeSet(new EFGRankObjectSortingCriteria());
		this.captionsAboveSet= new TreeSet(new EFGRankObjectSortingCriteria());
		this.sortingList = new ArrayList();
		
		this.image2DisplayFields = new ArrayList();
		this.footerImagesList = new ArrayList();

		this.mainTitleObject = new CaptionFontObject();

		this.subTitleObject = new CaptionFontObject();

		this.copyRight1Object = new CaptionFontObject();

		this.copyRight2Object = new CaptionFontObject();
		this.initDefaults();
	}
	// read off a properties file
	private void initDefaults() {

		this.WEIGHT_WHITE_SPACE_AROUND_IMAGE = EFG2PDFConstants.DEFAULT_WEIGHT_WHITE_SPACE_AROUND_IMAGE;

		this.WEIGHT_FRAME_AROUND_IMAGE = EFG2PDFConstants.DEFAULT_WEIGHT_FRAME_AROUND_IMAGE;

		this.COLOR_FOR_FRAME_AROUND_IMAGE = EFG2PDFConstants.DEFAULT_COLOR_FOR_FRAME_AROUND_IMAGE;// border
																									// color
																									// around
																									// image

		this.WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS = EFG2PDFConstants.DEFAULT_WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS;

		this.WEIGHT_BOUNDING_BOX = EFG2PDFConstants.DEFAULT_WEIGHT_BOUNDING_BOX;

		this.COLOR_FOR_BOUNDING_BOX = EFG2PDFConstants.DEFAULT_COLOR_FOR_BOUNDING_BOX;

		this.FILL_SPACE_AROUND_DISP_IMAGES = EFG2PDFConstants.DEFAULT_FILL_SPACE_AROUND_DISP_IMAGES;

		this.PAPER_SIZE = EFG2PDFConstants.DEFAULT_PAPER_SIZE;

		this.NUMBER_OF_ROWS = EFG2PDFConstants.DEFAULT_ROWS;

		this.NUMBER_OF_COLUMNS = EFG2PDFConstants.DEFAULT_COLUMNS;
	}


	public boolean isCaptionsExists(){
		if(this.getCaptionsAbove().size() > 0 ||
				this.getCaptionsBelow().size() > 0){
			return true;
		}
		return false;
	}
	private CaptionFontObject getDefaultSortingField(){
		CaptionFontObject fontObject = null;
		if(this.getCaptionsBelow().size() > 0){
			fontObject = (CaptionFontObject)this.getCaptionsBelow().first();
		}
		if(this.getCaptionsAbove().size() > 0){
			fontObject = (CaptionFontObject)this.getCaptionsAbove().last();
		}
		return fontObject;
	}
	/**
	 * 
	 * @return
	 */
	public List getSortingFields() {
		if(this.sortingList.size() == 0){			
			CaptionFontObject cfo = getDefaultSortingField();			
			if(cfo != null){
				this.sortingList.add(cfo.getCaption());
			}
			else{
				if(this.getImage2DisplayFields().size() > 0){
					String images = 
						(String)this.getImage2DisplayFields().get(0);
					this.sortingList.add(images);
				}
			}
		}
		else{
			if(this.getImage2DisplayFields().size() > 0){
				String images = 
					(String)this.getImage2DisplayFields().get(0);
				this.sortingList.add(images);
			}
		}		
		return this.sortingList;
	}

	/**
	 * 
	 * @return
	 */
	public Rectangle getPaperSize() {
		if(!this.isPortraitOrientation()){
			Rectangle rect = new Rectangle(
					this.PAPER_SIZE.height(),
					this.PAPER_SIZE.width() 
					);
			return rect;
		}
		return this.PAPER_SIZE;
	}

	/**
	 * 
	 * @return
	 */
	public SortedSet getCaptionsBelow() {
		return this.captionsBelowSet;
	}
	/**
	 * 
	 * @return
	 */
	public SortedSet getCaptionsAbove() {
		return this.captionsAboveSet;
	}
	/**
	 * 
	 * @return
	 */
	public String getCOLOR_FOR_BOUNDING_BOX() {
		return this.COLOR_FOR_BOUNDING_BOX;
	}

	/**
	 * 
	 * @return
	 */
	public String getCOLOR_FOR_FRAME_AROUND_IMAGE() {
		return this.COLOR_FOR_FRAME_AROUND_IMAGE;
	}



	/**
	 * 
	 * @return
	 */
	public boolean isFILL_SPACE_AROUND_DISP_IMAGES() {
		return this.FILL_SPACE_AROUND_DISP_IMAGES;
	}

	/**
	 * 
	 * @return
	 */
	public List getFooterImagesList() {
		return this.footerImagesList;
	}

	/**
	 * 
	 * @return
	 */
	public List getImage2DisplayFields() {
		return this.image2DisplayFields;
	}



	/**
	 * 
	 * @return
	 */
	public int getWEIGHT_BOUNDING_BOX() {
		return WEIGHT_BOUNDING_BOX;
	}

	/**
	 * 
	 * @return
	 */
	public float getWEIGHT_FRAME_AROUND_IMAGE() {
		return WEIGHT_FRAME_AROUND_IMAGE;
	}

	/**
	 * 
	 * @return
	 */
	public int getWEIGHT_WHITE_SPACE_AROUND_IMAGE() {
		return WEIGHT_WHITE_SPACE_AROUND_IMAGE;
	}

	/**
	 * 
	 * @return
	 */
	public int getWEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS() {
		return WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS;
	}

	/**
	 * 
	 * @return
	 */
	public boolean fillSpaceAroundDisproportionateImage() {
		return this.FILL_SPACE_AROUND_DISP_IMAGES;
	}

	/**
	 * 
	 * @return
	 */
	public CaptionFontObject getMainTitle() {
		return this.mainTitleObject;
	}

	/**
	 * 
	 * @return
	 */
	public CaptionFontObject getSubTitle() {
		return this.subTitleObject;
	}

	/**
	 * 
	 * @return
	 */
	public CaptionFontObject getCopyRight1() {
		return this.copyRight1Object;
	}

	/**
	 * 
	 * @return
	 */
	public CaptionFontObject getCopyRight2() {
		return this.copyRight2Object;
	}

	public int getNumberColumns() {
		return this.NUMBER_OF_COLUMNS;
	}

	public int getNumberRows() {
		return this.NUMBER_OF_ROWS;
	}

	/**
	 * 
	 * @param group
	 */
	private void createGeneralSettings(GroupType group) {

		GroupTypeItem key = null;
		CharacterValue cv = null;

		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				cv = key.getCharacterValue();
				if (cv != null) {
					String label = cv.getLabel();
					String val = null;
					if (label.equalsIgnoreCase("paperorientation")){
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							log.debug("Orientation: " + val);
							Boolean bool = (Boolean)orientationMap.get(val.toLowerCase());
							if(bool != null){
								log.debug("Bool is not null");
								this.setPortraitOrientation(bool.booleanValue());
							}
						}
					}
					else if (label.equalsIgnoreCase(GENERAL_SETTING_SORT)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							this.sortingList.add(val);
						}
					} else if (label
							.equalsIgnoreCase(GENERAL_SETTING_IMAGE_COLUMN)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							this.image2DisplayFields.add(val.trim());
						}

					} else if (label
							.equalsIgnoreCase(GENERAL_SETTING_ROW_DIMENSION)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							try {
								NUMBER_OF_ROWS = Integer.parseInt(val);
							} catch (Exception ee) {
								NUMBER_OF_ROWS = EFG2PDFConstants.DEFAULT_ROWS;
							}

						}
					} else if (label.equalsIgnoreCase(GENERAL_SETTING_COLUMN_DIMENSION)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							try {
								NUMBER_OF_COLUMNS = Integer.parseInt(val);
							} catch (Exception ee) {
								NUMBER_OF_COLUMNS = EFG2PDFConstants.DEFAULT_COLUMNS;
							}
						}
					} else if (label
							.equalsIgnoreCase(GENERAL_SETTING_PAPER_SIZE)) {// we
						//the size of the paper
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							PAPER_SIZE = (Rectangle) paperSizes.get(val.trim());
						}
				} else if (label
						.equalsIgnoreCase(GENERAL_SETTING_IMAGE_NUMBER)) {
					//Show all images (default is to display the first one only) 
					val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						this.setSHOW_ALL_IMAGES(true);
						
					}
					else{
						this.setSHOW_ALL_IMAGES(false);
						
					}
				} else if (label
						.equalsIgnoreCase(GENERAL_SETTING_SHOW_CAPTIONS)) {
					//Always display captions (default is to skip if no image is available)
					val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						this.setSKIP_CELL_IF_NO_IMAGES(false);
					}
					else{
						this.setSKIP_CELL_IF_NO_IMAGES(true);
					}
				}

				}
			}
		}
	}
	/**
	 * @return true if Show all images
	 * false - display the first image only 
	 */
	public boolean isSHOW_ALL_IMAGES() {
		return this.SHOW_ALL_IMAGES;
	}
	/**
	 * @param true if Show all images
	 * false - display the first image only 
	 */
	public void setSHOW_ALL_IMAGES(boolean show_all_images) {
		this.SHOW_ALL_IMAGES = show_all_images;
	}
	/**
	 * @return true- skip if no image is available
	 * false do not skip cell always display captions.
	 */

	public boolean isSKIP_CELL_IF_NO_IMAGES() {
		return this.SKIP_CELL_IF_NO_IMAGES;
	}
	
	/**
	 * @param true skip if no image is available
	 * false do not skip cell always display captions.
	 */
	public void setSKIP_CELL_IF_NO_IMAGES(boolean skip_cell_if_no_images) {
		this.SKIP_CELL_IF_NO_IMAGES = skip_cell_if_no_images;
	}

	/**
	 * 
	 * @param group
	 * @return
	 */
	private CaptionFontObject createCaptionFont(GroupType group) {

		GroupTypeItem key = null;
		CharacterValue cv = null;
		String caption = null;
		String align = ALIGN_LEFT;
		FontObject font = new FontObject();
		boolean isUnderLine = false;

		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			key = (GroupTypeItem) e.nextElement();
			int tempVal = -1;
			if (key != null) {
				cv = key.getCharacterValue();
				if (cv != null) {
					String label = cv.getLabel();
					String val = null;

					if (label.toLowerCase().endsWith(FONT)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							try {
								font
										.setFontName(Integer
												.parseInt((String) EFG2PDF.fontFamilyMap
														.get(val.toLowerCase())));
							} catch (Exception eee) {

							}
						}

					} 
					else if (label.toLowerCase().endsWith(SIZE)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							try {
								tempVal = Integer.parseInt(val);
								font.setFontSize(tempVal);
							} catch (Exception ee) {

							}
						}
					}
					else if (label.toLowerCase().endsWith(BOLD)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							font.setBold(true);
						}
					}
					else if (label.toLowerCase().endsWith(ITALICS)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							font.setItalics(true);
						}
					}
					else if (label.toLowerCase().endsWith(UNDER_LINE)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							isUnderLine = true;
						}
					}
					else if (label.toLowerCase().endsWith(ALIGN)) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							align = val;
						}
					}
					else if (label.toLowerCase().indexOf(CAPTION_TEXT) > -1) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							caption = val;
						}
					} 
					 else {// we do not know who you are
						// throw exception
					}
				}
			}
		}
		if (caption != null && !caption.trim().equals("")) {
			CaptionFontObject co = new CaptionFontObject();
			co.setRank(group.getRank());
			co.setCaption(caption);
			co.setFontObject(font);
			co.setUnderLine(isUnderLine);
			co.setAlignment(align);
			return co;
		}
		return null;
	}



	/**
	 * 
	 * @param tp
	 */
	private void readProperties() {
		
		
		GroupsType groups = this.xslPage.getGroups();
		GroupsTypeItem[] groupsArray = groups.getGroupsTypeItem();

		for (int i = 0; i < groupsArray.length; i++) {
			GroupsTypeItem gpt = (GroupsTypeItem) groupsArray[i];
			GroupType group = gpt.getGroup();
			String label = group.getLabel();

			if (label.toLowerCase().equals(GENERAL_SETTINGS)) {
				createGeneralSettings(group);
			}else if (label.toLowerCase().equals(TITLES)) {
				createTitles(group);
			}
			else if (label.toLowerCase().equals(IMAGES_HORIZONTAL_ALIGNMENT)) {
				createImagesAlignment(group);
			}
			else if (label.toLowerCase().equals(IMAGES)) {
				createHeaderImages(group);
			} else if (label.toLowerCase().equals(IMAGE_WHITE_SPACES)) {
				createWhiteSpaceAroundImage(group);
			} else if (label.toLowerCase().equals(ELEMENT_WHITE_SPACES)) {
				createElementWhiteSpace(group);
			} else if (label.toLowerCase().equals(IMAGE_FRAME_WEIGHTS)) {
				createImageFrameWeightAroundImage(group);
			} else if (label.toLowerCase().equals(BOUNDING_BOX_WEIGHTS)) {
				createBoundingBoxWeight(group);
			}  else if (label.toLowerCase().indexOf(CAPTIONS_TEXT_ABOVE) > -1) {// returns
																			// captions
				// order
				// of
				// caption
				CaptionFontObject cfo = createCaptionFont(group);
				if (cfo != null) {
					this.captionsAboveSet.add(cfo);
				}
			} else if (label.toLowerCase().indexOf(CAPTIONS_TEXT_BELOW) > -1) {// returns
				// captions
				// order
				// of
				// caption
				CaptionFontObject cfo = createCaptionFont(group);
				if (cfo != null) {
					this.captionsBelowSet.add(cfo);
				}
			}
			

		}
	}



	/**
	 * @param group
	 */
	private void createImagesAlignment(GroupType group) {
		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); 
		e.hasMoreElements();) {
			GroupTypeItem key = (GroupTypeItem) e.nextElement();
			if (key != null) {
				CharacterValue cv = key.getCharacterValue();
				if (cv != null) {
					String val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						Integer al =(Integer)alignMap.get(val.toLowerCase());
						if(al != null){
							this.imageHorizontalAlignment = al.intValue();
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param group
	 */
	private void createBoundingBoxWeight(GroupType group) {
		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			GroupTypeItem key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				CharacterValue cv = key.getCharacterValue();
				if (cv != null) {
					String label = cv.getLabel();
					String val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						if (label.equalsIgnoreCase(BOUNDING_BOX_WEIGHT)) {
							try {
								int wspace = Integer.parseInt(val);
								this.WEIGHT_BOUNDING_BOX = wspace;
							} catch (Exception ee) {

							}

						} else if (label.equalsIgnoreCase(BOUNDING_BOX_COLOR)) {
							this.COLOR_FOR_BOUNDING_BOX = val;
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param group
	 */
	private void createImageFrameWeightAroundImage(GroupType group) {
		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			GroupTypeItem key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				CharacterValue cv = key.getCharacterValue();
				if (cv != null) {
					String label = cv.getLabel();
					String val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						if (label.equalsIgnoreCase(IMAGE_FRAME_WEIGHT)) {
							try {
								int wspace = Integer.parseInt(val);
								this.WEIGHT_FRAME_AROUND_IMAGE = wspace;
							} catch (Exception ee) {

							}

						} else if (label.equalsIgnoreCase(IMAGE_FRAME_COLOR)) {
							this.COLOR_FOR_FRAME_AROUND_IMAGE = val;
						}
					}
				}
			}
		}

	}

	/**
	 * 
	 * @param group
	 */
	private void createElementWhiteSpace(GroupType group) {

		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			GroupTypeItem key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				CharacterValue cv = key.getCharacterValue();
				if (cv != null) {

					String val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						try {
							int wspace = Integer.parseInt(val);
							this.WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS = wspace;
						} catch (Exception ee) {

						}

					}

				}
			}
		}
	}

	/**
	 * 
	 * @param group
	 */
	private void createWhiteSpaceAroundImage(GroupType group) {
		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			GroupTypeItem key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				CharacterValue cv = key.getCharacterValue();
				if (cv != null) {

					String val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						try {
							int wspace = Integer.parseInt(val);

							this.WEIGHT_WHITE_SPACE_AROUND_IMAGE = wspace;
						} catch (Exception ee) {

						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param group
	 */
	private void createHeaderImages(GroupType group) {

		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			GroupTypeItem key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				CharacterValue cv = key.getCharacterValue();
				if (cv != null) {

					String val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						this.footerImagesList.add(val);
					}
				}
			}
		}

	}

	/**
	 * 
	 * @param group
	 */
	private void createTitles(GroupType group) {
		GroupTypeItem key = null;
		CharacterValue cv = null;

		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				cv = key.getCharacterValue();
				if (cv != null) {
					String label = cv.getLabel();
					String val = null;

					if (label.equalsIgnoreCase("maintitle")
							|| label.equalsIgnoreCase("maintitlefont")
							|| label.equalsIgnoreCase("maintitlealign")
							|| label.equalsIgnoreCase("titlesize")
							|| label.equalsIgnoreCase("titleformat")) {

						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							if (label.equalsIgnoreCase("maintitle")) {
								this.mainTitleObject.setCaption(val);
							} else if (label.equalsIgnoreCase("maintitlefont")) {
								try {
									this.mainTitleObject
											.setFontName(Integer
													.parseInt((String) EFG2PDF.fontFamilyMap
															.get(val
																	.toLowerCase())));
								} catch (Exception ee) {

								}
							} else if (label.equalsIgnoreCase("titlesize")) {
								try {
									int tempVal = Integer.parseInt(val);
									this.mainTitleObject.setFontSize(tempVal);
								} catch (Exception ee) {

								}
							} else if (label.equalsIgnoreCase("titleformat")) {
								if (val.equalsIgnoreCase(BOLD)) {
									this.mainTitleObject.setFontBold(true);
								} else if (val.equalsIgnoreCase(ITALICS)) {
									this.mainTitleObject.setFontItalics(true);
								} else if (val.equalsIgnoreCase(UNDER_LINE)) {
									this.mainTitleObject.setUnderLine(true);
								}
							}
							else if (label.equalsIgnoreCase("maintitlealign")) {
								val = cv.getValue();
								if (val != null && !val.trim().equals("")) {
									this.mainTitleObject.setAlignment(val);
								}
							}
						}
					} else if (label.equalsIgnoreCase("subtitle")
							|| label.equalsIgnoreCase("subtitlefont")
							|| label.equalsIgnoreCase("subtitlealign")
							|| label.equalsIgnoreCase("subtitlesize")
							|| label.equalsIgnoreCase("subtitleformat")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {

							if (label.equalsIgnoreCase("subtitle")) {

								this.subTitleObject.setCaption(val);
							} else if (label.equalsIgnoreCase("subtitlefont")) {
								try {
									this.subTitleObject
											.setFontName(Integer
													.parseInt((String) EFG2PDF.fontFamilyMap
															.get(val
																	.toLowerCase())));
								} catch (Exception ee) {

								}
							} else if (label.equalsIgnoreCase("subtitlesize")) {
								try {
									int tempVal = Integer.parseInt(val);
									this.subTitleObject.setFontSize(tempVal);
								} catch (Exception ee) {

								}
							} else if (label.equalsIgnoreCase("subtitleformat")) {
								if (val.equalsIgnoreCase(BOLD)) {
									this.subTitleObject.setFontBold(true);
								}
								if (val.equalsIgnoreCase(ITALICS)) {
									this.subTitleObject.setFontItalics(true);
								}
								if (val.equalsIgnoreCase(UNDER_LINE)) {
									this.subTitleObject.setUnderLine(true);
								}
							}
							else if (label.equalsIgnoreCase("subtitlealign")) {
								val = cv.getValue();
								if (val != null && !val.trim().equals("")) {
									this.subTitleObject.setAlignment(val);
								}
							}

						}
					} else if (label.equalsIgnoreCase("creditstitle1")
							|| label.equalsIgnoreCase("creditstitle1font")
								|| label.equalsIgnoreCase("creditstitle1align")
							|| label.equalsIgnoreCase("creditstitle1size")
							|| label.equalsIgnoreCase("creditstitle1format")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							if (label.equalsIgnoreCase("creditstitle1")) {
								this.copyRight1Object.setCaption(val);
							} else if (label
									.equalsIgnoreCase("creditstitle1font")) {
								try {
									this.copyRight1Object
											.setFontName(Integer
													.parseInt((String) EFG2PDF.fontFamilyMap
															.get(val
																	.toLowerCase())));
								} catch (Exception ee) {
								}
							} else if (label.equalsIgnoreCase("creditstitle1size")) {
								try {
									int tempVal = Integer.parseInt(val);
									this.copyRight1Object.setFontSize(tempVal);
								} catch (Exception ee) {
								}
							} else if (label.equalsIgnoreCase("creditstitle1format")) {
								if (val.equalsIgnoreCase(BOLD)) {
									this.copyRight1Object.setFontBold(true);
								}
								if (val.equalsIgnoreCase(ITALICS)) {
									this.copyRight1Object.setFontItalics(true);
								}
								if (val.equalsIgnoreCase(UNDER_LINE)) {
									this.copyRight1Object.setUnderLine(true);
								}
							}
						}
						else if (label.equalsIgnoreCase("creditstitle1align")) {
							val = cv.getValue();
							if (val != null && !val.trim().equals("")) {
								this.copyRight1Object.setAlignment(val);
							}
						}

					} else if (label.equalsIgnoreCase("creditstitle2")
							|| label.equalsIgnoreCase("creditstitle2font")
							|| label.equalsIgnoreCase("creditstitle2align")
							|| label.equalsIgnoreCase("creditstitle2size")
							|| label.equalsIgnoreCase("creditstitle2format")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							if (label.equalsIgnoreCase("creditstitle2")) {

								this.copyRight2Object.setCaption(val);
							} else if (label
									.equalsIgnoreCase("creditstitle2font")) {
								try {
									this.copyRight2Object
											.setFontName(Integer
													.parseInt((String) EFG2PDF.fontFamilyMap
															.get(val
																	.toLowerCase())));
								} catch (Exception ee) {
								}
							} else if (label.equalsIgnoreCase("creditstitle2size")) {
								try {
									int tempVal = Integer.parseInt(val);
									this.copyRight2Object.setFontSize(tempVal);
								} catch (Exception ee) {
								}
							} else if (label.equalsIgnoreCase("creditstitle2format")) {
								if (val.equalsIgnoreCase(BOLD)) {
									this.copyRight2Object.setFontBold(true);
								}
								if (val.equalsIgnoreCase(ITALICS)) {
									this.copyRight2Object.setFontItalics(true);
								}
								if (val.equalsIgnoreCase(UNDER_LINE)) {
									this.copyRight2Object.setUnderLine(true);
								}
							}
							else if (label.equalsIgnoreCase("creditstitle2align")) {
								val = cv.getValue();
								if (val != null && !val.trim().equals("")) {
									this.copyRight2Object.setAlignment(val);
								}
							}
						}

					} else {// we do not know who you are
					}

				}
			}
		}

	}
	/**
	 * 
	 * @author jacob.asiedu
	 * 
	 */
	class CaptionFontObject extends EFGRankObject{
		private String caption;
		private int align;
		private boolean isUnderLine = EFG2PDFConstants.DEFAULT_ISUNDERLINE;

		private List states;

		private FontObject fontObject;

	
		public String toString(){
			return this.caption;
		}
		public void setAlignment(String align) {
			Integer al =(Integer)alignMap.get(align.toLowerCase());
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

	/**
	 * 
	 * @author jacob.asiedu
	 * 
	 */
	class FontObject {
		private int fontName = EFG2PDFConstants.DEFAULT_FONTNAME;

		private int fontsize = EFG2PDFConstants.DEFAULT_FONTSIZE;

		private int fontType = EFG2PDFConstants.DEFAULT_FONTTYPE;

		private boolean isBold = EFG2PDFConstants.DEFAULT_ISBOLD;

		private boolean isItalics = EFG2PDFConstants.DEFAULT_ISITALICS;

		public FontObject() {

		}

		/**
		 * 
		 * @return
		 */
		public int getFontName() {
			return fontName;
		}

		/**
		 * 
		 * @param fontName
		 */
		public void setFontName(int fontName) {
			this.fontName = fontName;
		}

		/**
		 * 
		 * @return
		 */
		public int getFontsize() {
			return fontsize;
		}

		/**
		 * 
		 * @param fontsize
		 */
		public void setFontSize(int fontsize) {
			this.fontsize = fontsize;
		}

		/**
		 * 
		 * @return
		 */
		public int getFontType() {
			return fontType;
		}

		/**
		 * 
		 * @param isBold
		 */
		public void setBold(boolean isBold) {
			this.isBold = isBold;
		}

		/**
		 * 
		 * @param isItalics
		 */
		public void setItalics(boolean isItalics) {
			this.isItalics = isItalics;
		}

		/**
		 * 
		 * @param fontType
		 */
		public void setFontType(int fontType) {
			this.fontType = fontType;
		}

		/**
		 * 
		 * @return
		 */
		public Font createFont() {
			if (this.isBold && this.isItalics) {
				this.setFontType(Font.BOLDITALIC);
			} else if (isBold) {
				this.setFontType(Font.BOLD);
			} else if (isItalics) {
				this.setFontType(Font.ITALIC);
			}

			return new Font(this.getFontName(), this.getFontsize(), this
					.getFontType());
		}
	}

	/**
	 * @return
	 */
	public boolean isTextAboveImage() {
		return this.getCaptionsAbove().size() > 0;
	}
	/**
	 * @return
	 */
	public boolean isTextBelowImage() {
		return this.getCaptionsBelow().size() > 0;
	}

}

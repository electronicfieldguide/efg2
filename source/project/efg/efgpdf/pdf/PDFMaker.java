package project.efg.efgpdf.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.efg.templates.taxonPageTemplates.CharacterValue;
import project.efg.templates.taxonPageTemplates.GroupType;
import project.efg.templates.taxonPageTemplates.GroupTypeItem;
import project.efg.templates.taxonPageTemplates.GroupsType;
import project.efg.templates.taxonPageTemplates.GroupsTypeItem;
import project.efg.templates.taxonPageTemplates.XslPage;

import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


public class PDFMaker {

	/*
	 * Variables for handling cell properties
	 */
	private int WEIGHT_WHITE_SPACE_AROUND_IMAGE;

	private double WEIGHT_FRAME_AROUND_IMAGE;

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

	private String COLOR_SPACE_AROUND_DISPROP_IMAGES;

	/**
	 * End variable definitions
	 * 
	 */
	private Rectangle PAPER_SIZE;

	private int NUMBER_OF_ROWS;

	private int NUMBER_OF_COLUMNS;

	private boolean isTextAboveImage;

	private List headerImagesList;

	private CaptionFontObject mainTitleObject;

	private CaptionFontObject subTitleObject;

	private CaptionFontObject copyRight1Object;

	private CaptionFontObject copyRight2Object;

	private List captionsList;

	private List sortingList;

	private List image2DisplayFields;

	

	
	private XslPage xslPage;

	static Map paperSizes = new HashMap();
	//FIXME read from properties or Spring file
	static {
		// read from a properties file
		//use same file to populate html table
		paperSizes.put("EFG_LETTER", PageSize.LETTER);
		paperSizes.put("EFG_LEGAL", PageSize.LEGAL);
		paperSizes.put("EFG_792_1224", new Rectangle(792, 1224));
		paperSizes.put("EFG_A4", PageSize.A4);
		paperSizes.put("EFG_216_360", new Rectangle(216, 360));
		paperSizes.put("EFG_288_432", new Rectangle(288, 432));
		paperSizes.put("EFG_360_504", new Rectangle(360, 504));
	}

	public PDFMaker(XslPage xslPage) {
		
		
		this.xslPage = xslPage;
		
		initObjects();
	}

	private void initObjects(){
		this.captionsList = new ArrayList();
		this.sortingList = new ArrayList();
		this.image2DisplayFields = new ArrayList();
		this.headerImagesList = new ArrayList();

		this.mainTitleObject = new CaptionFontObject();

		this.subTitleObject = new CaptionFontObject();

		this.copyRight1Object = new CaptionFontObject();

		this.copyRight2Object = new CaptionFontObject();
		this.initDefaults();	
		this.processPDF();
	}
	// read off a properties file
	private void initDefaults() {

		this.WEIGHT_WHITE_SPACE_AROUND_IMAGE =
			EFG2PDFConstants.DEFAULT_WEIGHT_WHITE_SPACE_AROUND_IMAGE;

		this.WEIGHT_FRAME_AROUND_IMAGE =
			EFG2PDFConstants.DEFAULT_WEIGHT_FRAME_AROUND_IMAGE;

		this.COLOR_FOR_FRAME_AROUND_IMAGE = 
			EFG2PDFConstants.DEFAULT_COLOR_FOR_FRAME_AROUND_IMAGE;// border
																									// color
																									// around
																									// image
		this.WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS = 
			EFG2PDFConstants.DEFAULT_WEIGHT_WHITE_SPACE_BETWEEN_ELEMENTS;

		this.WEIGHT_BOUNDING_BOX = 
			EFG2PDFConstants.DEFAULT_WEIGHT_BOUNDING_BOX;

		this.COLOR_FOR_BOUNDING_BOX = 
			EFG2PDFConstants.DEFAULT_COLOR_FOR_BOUNDING_BOX;

		this.FILL_SPACE_AROUND_DISP_IMAGES = 
			EFG2PDFConstants.DEFAULT_FILL_SPACE_AROUND_DISP_IMAGES;

		this.COLOR_SPACE_AROUND_DISPROP_IMAGES = 
			EFG2PDFConstants.DEFAULT_COLOR_SPACE_AROUND_DISPROP_IMAGES;

		this.PAPER_SIZE = EFG2PDFConstants.DEFAULT_PAPER_SIZE;

		this.NUMBER_OF_ROWS = EFG2PDFConstants.DEFAULT_ROWS;

		this.NUMBER_OF_COLUMNS = EFG2PDFConstants.DEFAULT_COLUMNS;

	}



	/**
	 * 
	 * @return
	 */
	public List getSortingFields() {
		if(this.sortingList.size() == 0){
			if(this.getCaptions().size() > 0){
			CaptionFontObject cfo 	= 
				(CaptionFontObject)this.getCaptions().get(0);
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
		}
		return this.sortingList;
	}

	/**
	 * 
	 * @return
	 */
	public Rectangle getPaperSize() {
		return this.PAPER_SIZE;
	}

	/**
	 * 
	 * @return
	 */
	public List getCaptions() {
		return this.captionsList;
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
	public String getCOLOR_SPACE_AROUND_DISPROP_IMAGES() {
		return this.COLOR_SPACE_AROUND_DISPROP_IMAGES;
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
	public List getHeaderImagesList() {
		return this.headerImagesList;
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
	public boolean isTextAboveImage() {
		return this.isTextAboveImage;
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
	public double getWEIGHT_FRAME_AROUND_IMAGE() {
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
					if (label.equalsIgnoreCase("generalsettingsort")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							this.sortingList.add(val);
						}
					} else if (label
							.equalsIgnoreCase("generalsettingimageColumn")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							this.image2DisplayFields.add(val.trim());
						}

					} else if (label
							.equalsIgnoreCase("generalsettingrowdimension")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							try {
								NUMBER_OF_ROWS = Integer.parseInt(val);
							} catch (Exception ee) {
								NUMBER_OF_ROWS = EFG2PDFConstants.DEFAULT_ROWS;
							}

						}
					} else if (label.equalsIgnoreCase("columndimension")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							try {
								NUMBER_OF_COLUMNS = Integer.parseInt(val);
							} catch (Exception ee) {
								NUMBER_OF_COLUMNS = EFG2PDFConstants.DEFAULT_COLUMNS;
							}
						}
					} else if (label
							.equalsIgnoreCase("generalsettingpapersize")) {
						val = cv.getValue();
						// we
						// do
						// not
						// know
						// who
						// you
						// are
						System.out.println("Papersize: " + val);
						if (val != null && !val.trim().equals("")) {
							this.PAPER_SIZE = (Rectangle) paperSizes.get(val.trim());
							System.out.println("Papersize: " + this.PAPER_SIZE);
						}
					}

				}
			}
		}
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

					if (label.toLowerCase().endsWith("font")) {
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

					} else if (label.toLowerCase().indexOf("captiontext") > -1) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							caption = val;
						}
					} else if (label.equalsIgnoreCase("textsettingsize")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							try {
								tempVal = Integer.parseInt(val);
								font.setFontSize(tempVal);
							} catch (Exception ee) {

							}
						}
					} else if (label.equalsIgnoreCase("textsettingbold")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							font.setBold(true);
						}
					} else if (label.equalsIgnoreCase("textsettingitalics")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							font.setItalics(true);
						}
					} else if (label.equalsIgnoreCase("textsettingunderline")) {
						val = cv.getValue();
						if (val != null && !val.trim().equals("")) {
							isUnderLine = true;
						}
					} else {// we do not know who you are
						// throw exception
					}
				}
			}
		}
		if (caption != null && !caption.trim().equals("")) {
			CaptionFontObject co = new CaptionFontObject();
			co.setCaption(caption);
			co.setFontObject(font);
			co.setUnderLine(isUnderLine);
			return co;
		}
		return null;
	}

	private void createTextAboveImage(GroupType group) {
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
					val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						if (label.equalsIgnoreCase("textsettingimageabove")) {
							this.isTextAboveImage = true;
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param pdfConfigs
	 * @param guid
	 * @param displayName
	 * @return
	 */
	private boolean processPDF() {
		
		if(this.xslPage == null){
			return false;
		}
		GroupsType groups = this.xslPage.getGroups();
		GroupsTypeItem[] groupsArray = groups.getGroupsTypeItem();

		for (int i = 0; i < groupsArray.length; i++) {
			GroupsTypeItem gpt = (GroupsTypeItem) groupsArray[i];
			GroupType group = gpt.getGroup();
			String label = group.getLabel();

			if (label.toLowerCase().equals("generalsettings")) {
				createGeneralSettings(group);
			} else if (label.toLowerCase().equals("textsettings")) {
				createTextAboveImage(group);
			} else if (label.toLowerCase().equals("titles")) {
				createTitles(group);
			} else if (label.toLowerCase().equals("images")) {
				createHeaderImages(group);
			} else if (label.toLowerCase().equals("imagewhitespaces")) {
				createWhiteSpaceAroundImage(group);
			} else if (label.toLowerCase().equals("elementwhitespaces")) {
				createElementWhiteSpace(group);
			} else if (label.toLowerCase().equals("imageframeweights")) {
				createImageFrameWeightAroundImage(group);
			} else if (label.toLowerCase().equals("boundingboxweights")) {
				createBoundingBoxWeight(group);
			} else if (label.toLowerCase().equals("fillbackgrounds")) {
				createColorFillAroundDisPropImage(group);
			} else if (label.toLowerCase().indexOf("captionstext") > -1) {// returns
																			// captions
				// order
				// of
				// caption
				CaptionFontObject cfo = createCaptionFont(group);
				if (cfo != null) {
					this.captionsList.add(cfo);
				}
			}
		}
		return true;	
	}
	/**
	 * 
	 * @param group
	 */
	private void createColorFillAroundDisPropImage(GroupType group) {
		for (java.util.Enumeration e = group.enumerateGroupTypeItem(); e
				.hasMoreElements();) {
			GroupTypeItem key = (GroupTypeItem) e.nextElement();

			if (key != null) {
				CharacterValue cv = key.getCharacterValue();
				if (cv != null) {
					String label = cv.getLabel();
					String val = cv.getValue();
					if (val != null && !val.trim().equals("")) {
						if (label.equalsIgnoreCase("fillbackground")) {
							this.FILL_SPACE_AROUND_DISP_IMAGES = true;
						} else if (label.equalsIgnoreCase("boundingboxcolor")) {
							this.COLOR_SPACE_AROUND_DISPROP_IMAGES = val;
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
						if (label.equalsIgnoreCase("boundingboxweight")) {
							try {
								int wspace = Integer.parseInt(val);
								this.WEIGHT_BOUNDING_BOX = wspace;
							} catch (Exception ee) {

							}

						} else if (label.equalsIgnoreCase("boundingboxcolor")) {
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
						if (label.equalsIgnoreCase("imageframeweight")) {
							try {
								int wspace = Integer.parseInt(val);
								this.WEIGHT_FRAME_AROUND_IMAGE = wspace;
							} catch (Exception ee) {

							}

						} else if (label.equalsIgnoreCase("imageframecolor")) {
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
						this.headerImagesList.add(val);
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
								if (val.equalsIgnoreCase("bold")) {
									this.mainTitleObject.setFontBold(true);
								} else if (val.equalsIgnoreCase("italics")) {
									this.mainTitleObject.setFontItalics(true);
								} else if (val.equalsIgnoreCase("underline")) {
									this.mainTitleObject.setUnderLine(true);
								}
							}
						}
					} else if (label.equalsIgnoreCase("subtitle")
							|| label.equalsIgnoreCase("subtitlefont")
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
								if (val.equalsIgnoreCase("bold")) {
									this.subTitleObject.setFontBold(true);
								}
								if (val.equalsIgnoreCase("italics")) {
									this.subTitleObject.setFontItalics(true);
								}
								if (val.equalsIgnoreCase("underline")) {
									this.subTitleObject.setUnderLine(true);
								}
							}
						}
					} else if (label.equalsIgnoreCase("creditstitle1")
							|| label.equalsIgnoreCase("creditstitle1font")
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
								if (val.equalsIgnoreCase("bold")) {
									this.copyRight1Object.setFontBold(true);
								}
								if (val.equalsIgnoreCase("italics")) {
									this.copyRight1Object.setFontItalics(true);
								}
								if (val.equalsIgnoreCase("underline")) {
									this.copyRight1Object.setUnderLine(true);
								}
							}
						}
					} else if (label.equalsIgnoreCase("creditstitle2")
							|| label.equalsIgnoreCase("creditstitle2font")
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
								if (val.equalsIgnoreCase("bold")) {
									this.copyRight2Object.setFontBold(true);
								}
								if (val.equalsIgnoreCase("italics")) {
									this.copyRight2Object.setFontItalics(true);
								}
								if (val.equalsIgnoreCase("underline")) {
									this.copyRight2Object.setUnderLine(true);
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
	class CaptionFontObject {
		private String caption;

		private boolean isUnderLine = EFG2PDFConstants.DEFAULT_ISUNDERLINE;

		private List states;

		private FontObject fontObject;

		/**
		 * 
		 * 
		 */
		public CaptionFontObject() {
			this.fontObject = new FontObject();
			this.states = new ArrayList();
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

}

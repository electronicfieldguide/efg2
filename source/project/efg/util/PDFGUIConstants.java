/**
 * 
 */
package project.efg.util;

import java.util.Properties;


/**
 * @author jacob.asiedu
 *
 */
public interface PDFGUIConstants {
	boolean isNew = true;
	boolean isOld = false;
	String ALIGN = "align";
	String ALIGN_LEFT="left";
	String ALIGN_RIGHT="right";
	String ALIGN_CENTER="center";

//	constants for general settings
	String IMAGES_HORIZONTAL_ALIGNMENT ="imagesalignment";
	String GENERAL_SETTINGS="generalsettings";
	String GENERAL_SETTING_SORT="generalsettingsort";
	String GENERAL_SETTING_IMAGE_COLUMN="generalsettingimageColumn";
	String GENERAL_SETTING_IMAGE_NUMBER="generalsettingimagenumber";
	String GENERAL_SETTING_SHOW_CAPTIONS="generalsettingshowcaptions";
	String GENERAL_SETTING_ROW_DIMENSION="generalsettingrowdimension";
	String GENERAL_SETTING_PAPER_SIZE="generalsettingpapersize";
	String GENERAL_SETTING_COLUMN_DIMENSION="generalsettingcolumndimension";


//	Query constants
	String IMAGE_NUMBER="imagenumber";
	String SHOW_CAPTIONS="showcaptions";
	String BELOW ="below";
	String ABOVE ="above";

//	Text Settings
	String CAPTIONS_TEXT_BELOW="captionstextbelow";
	String CAPTIONS_TEXT_ABOVE="captionstextabove";

	String QUERIES="queries";
	String QUERY="query";

	String IMAGE_WHITE_SPACES="imagewhitespaces";
	String IMAGE_WHITE_SPACE="imagewhitespace";
	String ELEMENT_WHITE_SPACES="elementwhitespaces";
	String ELEMENT_WHITE_SPACE="elementwhitespace";
	String IMAGE_FRAME_WEIGHTS="imageframeweights";
	String IMAGE_FRAME_WEIGHT="imageframeweight";
	String IMAGE_FRAME_COLOR="imageframecolor";
	String BOUNDING_BOX_WEIGHTS="boundingboxweights";
	String BOUNDING_BOX_WEIGHT="boundingboxweight";
	String BOUNDING_BOX_COLOR="boundingboxcolor";
//	Title settings
	String TITLES="titles";
	String IMAGES="images";
	String IMAGE="image";
	String INDEXCAP="indexcap";
	String ABOVE_BELOW="aboveorbelow";
	String CAPTION_TEXT = "captiontext";
	String CAPTIONS_TEXT = "captionstext";
	String BOLD = "bold";
	String ITALICS = "italics";
	String UNDER_LINE = "underline";
	String FONT="font";
	String SIZE="size";



	String templateMatch ="PDF Plate Maker";
	String numberOfResultsStr = "numberOfResults";
	String RESULTS="queryresults";
	String searchqueryStr = "searchquery";
	String xslFileName = "efgfo.xsl";
	String forwardPage="NoDatasource.jsp";

//	must correspond to data in papersize.properties

//	must correspond to data in papersize.properties

	Properties paperSizeProperties = EFGImportConstants.EFGProperties;


//	Template String Arrays
	String[] NUMBER_OPTIONS = {"0", "1","2","3","4","5"};
	int NUMBER_OF_TITLES = 4; 
	String CREDITS_TEXT = "Credits/Copyright Info Goes Here";
	String[] TITLES_STR = {
			"Main Title Goes Here",
			"Subtitle Goes Here",
			CREDITS_TEXT,
			CREDITS_TEXT
	};
	int[] TITLES_DROP_DOWN_SIZE = {
		54,
		51,
		50,
		50	
	};
	String[] TITLES_LABEL_STR = {
			"Title",
			"Subtitle",
			"Credits 1",
			"Credits 1"
	};
	String[] TITLES_LABEL = {
			"maintitle",
			"subtitle",
			"creditstitle1",
			"creditstitle2"
	};
	String[] TITLE_SIZE = {
			"titlesize",
			"subtitlesize",
			"creditstitle1size",
			"creditstitle2size"
	};
	String[] TITLE_FONT_SIZE = {
			"18",
			"12",
			"10",
			"10"
	};
	String[] TITLE_FORMAT = {
			"titleformat",
			"subtitleformat",
			"creditstitle1format",
			"creditstitle2format"
	};


	String[] FONTS_LABEL = {
			"maintitlefont",
			"subtitlefont",
			"creditstitle1font",
			"creditstitle2font"
	};
//	read from a properties file
	String[] FONTS_NAME = {
		"Helvetica","Times","Courier"
	};
}

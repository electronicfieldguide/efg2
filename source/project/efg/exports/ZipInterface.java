package project.efg.exports;

import project.efg.util.EFGImportConstants;

public interface ZipInterface{
	String  TEMPLATE_CSS_DIR = EFGImportConstants.templateCSSDirectory;
	String  TEMPLATE_JAVASCRIPT_DIR=EFGImportConstants.templateJavascriptDirectory;
	String  TEMPLATE_IMAGES_DIR=EFGImportConstants.templateImagesDirectory;
	String  TEMPLATE_CONFIG_DIR=EFGImportConstants.TEMPLATES_XML_FOLDER_NAME;
	
	String  EFG_IMAGES_DIR=EFGImportConstants.EFGIMAGES;
	String  EFG_THUMS_DIR=EFGImportConstants.EFGIMAGES_THUMBS;
	String  SQL_DIR=EFGImportConstants.SQL_DIR;
	int MAX_NUMBER_OF_CHARS=30;
	String SQL_EXT=".sql";
	int BUF_SIZE = 8 * 1024;
}
	
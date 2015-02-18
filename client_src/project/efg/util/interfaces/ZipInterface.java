package project.efg.util.interfaces;



public interface ZipInterface{
	String  TEMPLATE_CSS_DIR = EFGImportConstants.templateCSSDirectory;
	String  TEMPLATE_JAVASCRIPT_DIR=EFGImportConstants.templateJavascriptDirectory;
	String  TEMPLATE_IMAGES_DIR=EFGImportConstants.templateImagesDirectory;
	String  TEMPLATE_CONFIG_DIR=EFGImportConstants.TEMPLATES_XML_FOLDER_NAME;
	
	String  EFG_IMAGES_DIR=EFGImportConstants.EFGProperties.getProperty(
	"efg.images.home");
	String  EFG_THUMS_DIR=EFGImportConstants.EFGProperties.getProperty(
	"efg.mediaresources.thumbs.home");
	String  SQL_DIR=EFGImportConstants.SQL_DIR;
	int MAX_NUMBER_OF_CHARS=30;
	String SQL_EXT=EFGImportConstants.SQL_EXT;
	int BUF_SIZE = 8 * 1024;
}
	
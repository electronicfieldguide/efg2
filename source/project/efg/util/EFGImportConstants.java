package project.efg.util;


import java.io.File;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * $Id$
 * 
 * Copyright (c) 2005 University of Massachusetts Boston
 *  *
 * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob Kwabena Asiedu</a>
 * 
 * This file is part of the UMB Electronic Field Guide. UMB Electronic Field
 * Guide is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * UMB Electronic Field Guide is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the UMB Electronic Field Guide; see the file COPYING. If not, write to: Free
 * Software Foundation, Inc. 59 Temple Place, Suite 330 Boston, MA 02111-1307
 * USA
 */
public interface EFGImportConstants {
	// The 4 ff constants are used are appended to datasource names to make
	//html file for missing data. MediaResources will  have
	// datasourcename + IMAGEAPPENDER
	//lists will have
	//datasourcename  + EFGLISTAPPENDER and so on.
	
	String IMAGEAPPENDER="_MediaResourceErrors";
	String EFGLISTAPPENDER ="_EFGListErrors";
	String CATAPPENDER="_CategoricalErrors";
	String NUMERICSAPPENDER="_NumericErrors";
	
	//holds the error files 
	String TEMPORARY_ERROR_FILE = "/tempError";
	
	String HTML_EXT = ".html";
	
	String DEFAULT_PLATES_DISPLAY = "Default Thumbnails";
	String DEFAULT_LISTS_DISPLAY = "Default Text List";
	String KEY_METADATA_HELP = "/help/metadataHelp.html";
	String MAIN_DEPLOY_HELP="/help/mainDeployHelp.html";
	String IMAGE_DEPLOY_HELP="/help/imagesDeployHelp.html";
	String KEYTREE_DEPLOY_HELP="/help/keysDeployHelp.html";
	String THUMBS_FILE_NAME = "properties/imagesproperties.dat";
	String JSP_NAME = "jsp";
	String TEMPLATE_UNIQUE_NAME="templateUniqueName";
	String GUID="guid";
	String TEMPLATE_MAP_NAME="templateMap.out";
	String TEMPLATE_SUCCESS_PAGE = "/UploadSuccess.jsp";
	String templateImagesDirectory ="templateImagesDirectory";
	String templateCSSDirectory="templateCSSDirectory";
	String templateJavascriptDirectory="templateJavascriptDirectory";
	String ITEMTYPE ="efgItemtype";
	int NUMBER_OF_TAXON_ON_PAGE=100;
	int TEMPLATE_IMAGES_DEFAULT_SIZE=1000000;
	int TEMPLATE_CSS_DEFAULT_SIZE=1000000;
	int TEMPLATE_JAVASCRIPT_DEFAULT_SIZE=1000000;
	
	String IMGTYPE= "imageType";
	String CSSTYPE ="cssType";
	String JAVASCRIPTYPE = "javascriptType";
	
	String GROUP_TEXT_LABEL = "gtl";

	String GROUP_LABEL = "gl";

	String GROUP = "group";
	String EFG_SEP = "_EFG_SEP_";

	String EFG_COLON = "EFG_COLON";

	String COMMASEP = ",";

	String CAPTION = "caption";

	String ORSEP = "OR";

	//used for server name construction
	String COLONSEP = ":";
	String ESCAPECOLONSEP = "\\\\:";

	String SEMICOLON = ";";

	String PIPESEP = "\\|";
	String ESCAPEPIPESEP = "\\\\\\|";
	
	String PIPESEP_PARSE="|";
	String DASHSEP = "-";
	String DASHSEPREG = "\\-";
	String ESCAPEDASHSEPREG = "\\\\\\-";
	String LEFTPARENSEP = "\\(";
	String ESCAPELEFTPARENSEP = "\\\\\\(";
	String LEFTPAREN="(";
	String RIGHTPAREN=")";
	String matchNumberStr = "\\d+$";
	String RIGHTPARENSEP = "\\)";
	String ESCAPERIGHTPARENSEP = "\\\\\\)";
	String[] STR_2_REMOVE = {"\\(","\\)"};
	String CARAT_SEPARATOR="\\^";
	String ESCAPECARAT_SEPARATOR="\\\\\\^";
	String ORPATTERN = "\\sor\\s";
	String ORCOMMAPATTERN="([,]|([ ]+(or)[ ]))";
	String ESCAPEORCOMMAPATTERN="\\\\([,]|([ ]+(or)[ ]))";
    String LISTSEP="#";
    String ESCAPELISTSEP="\\\\#";
	String COLON_SEPARATOR = COLONSEP;
	String NOPATTERN ="----#----";
	String matchNumbeAtEnd = "^\\d+$|^-\\d+$";
	String patternStr = "[A-Z]+";// remove everything that is an

	//PATTERNS
	Pattern matchNumberPattern = Pattern.compile(matchNumbeAtEnd);
	Pattern matchNumberAtEndPattern = Pattern.compile(matchNumberStr);
	Pattern spacePattern = Pattern.compile("\\s");
	Pattern equalsPattern = Pattern.compile("=");	
	Pattern colonPattern = Pattern.compile(COLON_SEPARATOR);	
	Pattern caratPattern = Pattern.compile(CARAT_SEPARATOR);
	Pattern rightParenPattern = Pattern.compile(EFGImportConstants.RIGHTPARENSEP, 
			Pattern.CASE_INSENSITIVE);
	Pattern leftParenPattern = Pattern.compile(EFGImportConstants.LEFTPARENSEP, 
			Pattern.CASE_INSENSITIVE);
	Pattern dashParenPattern =  Pattern.compile(EFGImportConstants.DASHSEPREG, 
			Pattern.CASE_INSENSITIVE);
	Pattern pipePattern = Pattern.compile(EFGImportConstants.PIPESEP, Pattern.CASE_INSENSITIVE);
	Pattern catPattern = Pattern.compile(EFGImportConstants.ORCOMMAPATTERN, Pattern.CASE_INSENSITIVE);
	Pattern commaPattern = Pattern.compile(EFGImportConstants.COMMASEP, Pattern.CASE_INSENSITIVE);
	Pattern noPattern = Pattern.compile(EFGImportConstants.NOPATTERN, Pattern.CASE_INSENSITIVE);
	Pattern listSepPattern = Pattern.compile(EFGImportConstants.LISTSEP, Pattern.CASE_INSENSITIVE);
	Pattern alphaPattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
	

	//ESCAPE PATTERNS
	Pattern escapecolonPattern = Pattern.compile(ESCAPECOLONSEP);
	Pattern escapepipePattern = Pattern.compile(EFGImportConstants.ESCAPEPIPESEP,
			Pattern.CASE_INSENSITIVE);
	Pattern escapecatPattern = Pattern.compile(EFGImportConstants.ESCAPEORCOMMAPATTERN, 
			Pattern.CASE_INSENSITIVE);
	Pattern escapecommaPattern = Pattern.compile(EFGImportConstants.COMMASEP, 
			Pattern.CASE_INSENSITIVE);
	Pattern escapenoPattern = Pattern.compile(EFGImportConstants.NOPATTERN, 
			Pattern.CASE_INSENSITIVE);
	Pattern escapelistSepPattern = Pattern.compile(EFGImportConstants.ESCAPELISTSEP, 
			Pattern.CASE_INSENSITIVE);
	Pattern escapedashParenPattern =  Pattern.compile(EFGImportConstants.ESCAPEDASHSEPREG, 
			Pattern.CASE_INSENSITIVE);
	Pattern escapeleftParenPattern = Pattern.compile(EFGImportConstants.ESCAPELEFTPARENSEP, 
			Pattern.CASE_INSENSITIVE);
	Pattern escaperightParenPattern = Pattern.compile(EFGImportConstants.ESCAPERIGHTPARENSEP, 
			Pattern.CASE_INSENSITIVE);
	Pattern escapecaratPattern = Pattern.compile(ESCAPECARAT_SEPARATOR);
	
	
	
	String CHARACTER_TEXT_LABEL = "ctl";
	String CHARACTER_LABEL = "cl";
	String XSL_SEARCH_GROUPS = "xslSearchAllGroups";
	String SEARCH_GROUP = "searchPageGroups";
	String IMAGES_MAX_DIM="imagemaxdim";
	String DS_DATA_COL= "DS_DATA";
	String DISPLAY_NAME_COL="DISPLAY_NAME";
	String DS_METADATA_COL = "DS_METADATA";
	String TAXON_PAGE_XSL_COL="XSL_FILENAME_TAXON";
	String SEARCH_PAGE_PLATES_XSL_COL="XSL_FILENAME_SEARCHPAGE_PLATES";
	String SEARCH_PAGE_LISTS_XSL_COL="XSL_FILENAME_SEARCHPAGE_LISTS";
	
	int OS_CONSTANT = 1;

	// Using relational database
	int RDB_CONSTANT = 2;

	// A property name for the maximum dimension property of Images
	String MAX_DIM_NAME = "EFG_MAX_DIM";
	String MAX_DIM_STR = "imagemaxdim";
	// A property name for the directory holding EFG images
	String EFG_IMAGES_DIR_NAME = "EFG_IMAGES_DIR";

	// A property name for the directory holding EFG images
	String EFG_THUMB_NAILS_DIR_NAME = "EFG_THUMB_NAILS_DIR";

	// The parent directory where all EFGImages can be found
	String EFG_IMAGES_DIR = "EFGImages";

	// The parent directory where all thumbnail generated images are placed
	String EFG_THUMB_NAILS_DIR = "EFG_ThumbNails_EFG";

	// READ FROM A PROPERTIES FILE
	String MAX_DIM = "200";
	

	String XSL_PROPERTIES_FILE = "XSLProps.properties";

	String EFG_KEYWORDS_PROPERTIES = "queryKeyWords.properties";
	//no hits page
	String NO_MATCH_PAGE="SearchPage_nohits.jsp";
	String FOPSERVLET= "FopServlet";
	String APPLYXSL="ApplyXSL";
	String ERROR_PAGE="ErrorPage.jsp";
	String UNIQUEID_STR="uniqueID";
	String EFGIMAGES_THUMBS = "efgimagesthumbs";

	String EFGIMAGES = "EFGImages";

	String DEFAULT_MAX_DIM = "200";

	
	String ISDEFAULT_STR="isDefault";
	String XSL_STRING = "xslName";

	String SUBMIT = "submit";

	String DATABASE_NAME = "efg";
    String ANY_STR = "any";
	String CREATE_DATABASE_QUERYBASE = "CREATE DATABASE IF NOT EXISTS ";

	String DEFAULTQUERYBASE = "SELECT * FROM ";

	String FILEMAKERURI = "jdbc:odbc:FileMaker_Files";

	String JDBCODBCCLASS = "sun.jdbc.odbc.JdbcOdbcDriver";

	String XML = "xml";
	String TAXONSIZE_STR="taxonSize";

	String RESULT_SET = "resultSet";

	String SEARCH_RESULTS = "SearchResult";

	String TEXT_HTML = "text/html";

	String DIGIR_TIME = "yyyy-MM-dd'T'hh:mm:ss";

	String DIGIR_NAMESPACE = "http://www.namespaceTBD.org/digir";

	String SEARCHSTR = "searchStr";

	String HTML = "HTML";

	String DIGIR = "digir";
	
	String REQUEST_TYPE="request_type";

	String DATASOURCE_NAME = "dataSourceName";

	String MAX_DISPLAY = "maxDisplay";

	String DEFAULT_MAX_DISPLAY = "defaultMaxDisplay";

	String MAX_DISPLAY_IGNORE = "ignore";

	String DISPLAY_FORMAT = "displayFormat";
	String DISPLAY_NAME="displayName";
	String METAFILESUFFIX = "Info";

	String EXCEL_EXT = ".xls";
	String TEMPLATE_ERROR_PAGE = "/templateJSP/TemplateError.jsp";
	String TEMPLATE_SCHEMA_NAME = "TaxonPageTemplate.xsd";
	String XML_EXT = ".xml";

	String DATAFILESUFFIX = "Data";

	String DATA_XML_EXT = "." + DATAFILESUFFIX + XML_EXT;

	String METADATA_XML_EXT = "." + METAFILESUFFIX + XML_EXT;

	String XML_PROCESSED_DIR = "processedXML";

	String PLATES = "Plates";

	String LISTS = "Lists";

	String AMP = "&";

	String SEARCH_STR = "search?" + EFGImportConstants.DATASOURCE_NAME + "=";

	

	String XSL_EXT = ".xsl";
	String DEFAULT_SEARCH_FILE ="defaultSearchFile.xsl";
	String DEFAULT_TAXON_PAGE_FILE ="defaultTaxonPageFile.xsl";
	

	String SEARCH_PAGE_TITLE = "title";


	String TEMPLATES_FOLDER_NAME = "templateConfigFiles";
	String TEMPLATES_XML_FOLDER_NAME = TEMPLATES_FOLDER_NAME + File.separator + XML;
	String SEARCHTYPE = "searchType";

	String IMAGES_DIR = "EFGImages";

	/**
	 * The standard columnnames for representing field attributes within an ODBC
	 * source.
	 */
	/*Searchable
	List
	Taxon Page
	Numeric Value
	Numeric Range
	Media Resource
	Order*/
	
	String MEDIARESOURCE = "MediaResource";
	String MEDIARESOURCE_DISPLAY = "Media Resource";
	
	String NAME = "Name";
	String NAME_DISPLAY=NAME;

	String LEGALNAME = "LegalName";
	String LEGALNAME_DISPLAY = "LegalName";
	
	String SEARCHABLE = "IsSearchable";
	String SEARCHABLE_DISPLAY = "Searchable";
	
	String ISLISTS = "IsLists";
	String ISLISTS_DISPLAY = "List";
	
	String NUMERICRANGE = "NumericRange";
	String NUMERICRANGE_DISPLAY = "Numeric Range";

	String NUMERIC = "NumericValue";
	String NUMERIC_DISPLAY= "Numeric Value";
	
	String ONTAXONPAGE = "OnTaxonPage";
	String ONTAXONPAGE_DISPLAY = "Taxon Page";

	String CATEGORICAL = "Categorical";
	String CATEGORICAL_DISPLAY = "Categorical";

	String NARRATIVE = "Narrative";
	String NARRATIVE_DISPLAY=NARRATIVE;

	String ORDER = "OrderValue";
	String ORDER_DISPLAY = "Order";

	String ORDERED = ORDER;

	String IMPORT_TITLE = "EFG2 Import Application";

	String WEIGHT = "weight";

	String SPECIESPAGETA = "speciesPageta";

	
	String MEDIARESOURCEFIELD = "mediaResourceField";

	String JAVATYPE = "javaType";

	String DATATYPE = "dataType";

	String PARSERABLE = "parserable";

	String NUMERICYES = "numericYes";

	String NO = "false";

	String YES = "true";

	boolean FALSE = false;

	boolean TRUE = true;

	String EFGTYPE = "type";

	String EFGLISTS = "EFGLists";

	String EFGLIST = "EFGList";

	String STATISTICALMEASURES = "StatisticalMeasures";

	String STATISTICALMEASURE = "StatisticalMeasure";

	String ITEMS = "Items";

	String ITEM = "Item";

	String MEDIARESOURCES = "MediaResources";

	String DATASOURCE = "datasource";

	String DATA = "Data";


	String TAXONENTRY = "TaxonEntry";

	String SIMPLETYPE = "simple";

	String MEDIARESOURCETYPE = "mediaresource";

	String EFGLISTTYPE = "efgList";

	String FORWARD_SLASH = "/";


	String EXCELPATTERN = EXCEL_EXT;

	String SERVICE_LINK = "serviceLink";

	int ANYTYPE = 0;

	int NUMERICTYPE = 1;

	int MEDIARESOURCEINTTYPE = 2;

	int LISTTYPE = 3;
	String EFG_ANY="EFG_ANY_OPTION";
	String TAXONPAGE = "_TaxonPage";
	String TAXONPAGE_XSL = "XSL_FILENAME_TAXON";
	String SEARCHPAGE_PLATES_XSL = "XSL_FILENAME_SEARCHPAGE_PLATES";
	String SEARCHPAGE_LISTS_XSL = "XSL_FILENAME_SEARCHPAGE_LISTS";

	String SEARCHPAGE = "SearchPage";

	String TAXONPAGE_FILLER = TAXONPAGE + XML_EXT;

	String SEARCHPAGE_FILLER = "_" + SEARCHPAGE + XML_EXT;

	String IMAGETYPE = "Image";

	String DATABASENAME = "file:///C:/database.database";

	String MYSQL = "MySQL";

	String SEARCH_PAGE_STR = "search";

	String WILDCARD_STR = "wildcard";
	
	String NO_MATCH_ATTR="nomatch_attr";
	
	String WILDCARD = "*";
	String TEMPLATE_JSP_LOCTION="/templateJSP";
	String TEST_TAXON_CONFIG_PAGE =TEMPLATE_JSP_LOCTION+  "/TestTaxonPage.jsp";

	String TEST_SEARCH_CONFIG_PAGE =TEMPLATE_JSP_LOCTION+  "/TestSearchPage.jsp";

	String SERVICE_LINK_FILLER = "_EFG_";

	String SEARCHPAGE_LISTS_FILLER = "_search_page_lists.xml";

	String SEARCHPAGE_PLATES_FILLER = "_search_page_plates.xml";

	String SEARCH_TYPE_STR = "searchType";
	String TEMPLATE_NAME="templateName";
	String HTML_TEMPLATE_NAME="htmlTemplateName";
	String SEARCH_PLATES_TYPE = "plates";
	String SEARCH_TAXON_TYPE = "taxon";
	String SEARCH_LISTS_TYPE = "lists";
	String CONFIG_TYPE="configType";
	String EFG_APPS = "efg2";

	String EFG_WEB_APPS = "webapps";

	String HELP_FILE = "/help/help.txt";

	String EFG_DATABASE_ROOT_NAME = "EFG Database Root";

	String EFG_DATABASE_DATA_NAME = "EFG";

	String EFG_DATABASE_METADATA_NAME = "EFG";

	// CHANGE ME AFTER TESTS
	String CSV_METADATA_HEADERS = "/metadataHeader/metadataHeaders.csv";

	String LOCK_FILE = "logs" + File.separator +  "temp.dat";
	String SETUP_FILE = "logs" + File.separator +  ".setup";

	String TOMCAT_USERS_FILE = "tomcat-users.xml";

	String TOMCAT_USERS_FILE_LOCATION = File.separator + "conf"
			+ File.separator + TOMCAT_USERS_FILE;

	String TOMCAT_USERS_FILE_RENAME_EXT = "_EFG_old";
	
	/*String TOMCAT_USERS_PROPERTIES = "tomcatusers.properties";
	String RDBPROPERTIES_FILE = "/RDBprops.properties";
	String GUIPROPERTIES_FILE = "/GUI.properties";*/
	String PROPERTIES_DIR="/properties";
	String SQL_KEYWORD_KEY="sqlkeywords";
	Properties EFGProperties =project.efg.Imports.rdb.EFGRDBImportUtils.getProperties();
    Set SQL_KEY_WORDS = project.efg.Imports.rdb.EFGRDBImportUtils.getSQLKeyWords();
	String SORTING_OFF = "Sorting OFF";
	String SORTING_ON = "Sorting ON";
}// EFGImportConstants

package project.efg.util;


import java.io.File;
import java.util.Properties;
import java.util.Set;

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
	String ZIP_EXT = ".zip";
	String ILLEGALCHARACTER_TEXT="efg_illegalcharacter";
	String SAMPLE_MEDIA_RESOURCE_FIELD = "SampleMediaResourceFields";
	String SAMPLE_SEARCHABLE_FIELD = "SampleSearchableFields";
	String SAMPLE_NEW_DISPLAY_NAME = "EFG2SampleDisplayName";
	String SAMPLE_LISTS_FIELD = "SampleListFields";
	String SAMPLE_DATA_LOCATION = "SampleDataLocation";
	String TEMPLATE_KEY = "EFGKey";
	
	
	
	
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
	
	String KEY_METADATA_HELP = "/help/metadataHelp.html";
	String MAIN_DEPLOY_HELP="/help/mainDeployHelp.html";
	String IMAGE_DEPLOY_HELP="/help/imagesDeployHelp.html";
	String KEYTREE_DEPLOY_HELP="/help/keysDeployHelp.html";
	String THUMBS_FILE_NAME = "properties/imagesproperties.dat";
	String JSP_NAME = "jsp";
	String TEMPLATE_UNIQUE_NAME="templateUniqueName";
	String GUID="guid";
	String TEMPLATE_SUCCESS_PAGE = "/UploadSuccess.jsp";
	String templateImagesDirectory ="templateImagesDirectory";
	String templateCSSDirectory="templateCSSDirectory";
	String templateJavascriptDirectory="templateJavascriptDirectory";
	String ITEMTYPE ="efgItemtype";
	
	
	/**
	 * Used by JSP to upload resources
	 */
	int TEMPLATE_IMAGES_DEFAULT_SIZE=1000000;
	int TEMPLATE_CSS_DEFAULT_SIZE=1000000;
	int TEMPLATE_JAVASCRIPT_DEFAULT_SIZE=1000000;
	
	
	/*
	 * 
	 * Template XML File constants
	 * 
	 */
	String GROUP_TEXT_LABEL = "gtl";
	String GROUP_LABEL = "gl";
	String GROUP = "group";
	String EFG_SEP = "_EFG_SEP_";
	String EFG_COLON = "EFG_COLON";
	String CHARACTER_TEXT_LABEL = "ctl";
	String CHARACTER_LABEL = "cl";
	
	

	/*
	 * EFG_RDB Table Column names
	 * 
	 */
	String DS_DATA_COL= "DS_DATA";
	String DISPLAY_NAME_COL="DISPLAY_NAME";
	String DS_METADATA_COL = "DS_METADATA";
	String SEARCH_PAGE_PLATES_XSL_COL="XSL_FILENAME_SEARCHPAGE_PLATES";
	String SEARCH_PAGE_LISTS_XSL_COL="XSL_FILENAME_SEARCHPAGE_LISTS";
	
	
	/**
	 * Servlet cosntants
	 */
	String EFG_KEYWORDS_PROPERTIES = "queryKeyWords.properties";
	//no hits page
	String NO_MATCH_PAGE="SearchPage_nohits.jsp";
	String FOPSERVLET= "FopServlet";
	String APPLYXSL="ApplyXSL";
	String ERROR_PAGE="ErrorPage.jsp";
	String UNIQUEID_STR="uniqueID";
	String ISDEFAULT_STR="isDefault";
	String XSL_STRING = "xslName";
	String DEFAULT_PLATES_DISPLAY = "Default Thumbnails";
	String DEFAULT_LISTS_DISPLAY = "Default Text List";
	String SUBMIT = "submit";

	String DATABASE_NAME = "efg";
    String ANY_STR = "any";
    String TAXONSIZE_STR="taxonSize";
    String RESULT_SET = "resultSet";
    String TEXT_HTML = "text/html";
	String TEXT_XML = "text/xml";
	String HTML = "HTML";
	String XML = "xml";
	String SEARCHSTR = "searchStr";
	String REQUEST_TYPE="request_type";
	String DATASOURCE_NAME = "dataSourceName";

	String MAX_DISPLAY = "maxDisplay";

	String DEFAULT_MAX_DISPLAY = "defaultMaxDisplay";

	String MAX_DISPLAY_IGNORE = "ignore";

	String DISPLAY_FORMAT = "displayFormat";
	String DISPLAY_NAME="displayName";
	String METAFILESUFFIX = "Info";
	
	String TEMPLATE_ERROR_PAGE = "/templateJSP/TemplateError.jsp";
	String TEMPLATE_SCHEMA_NAME = "TaxonPageTemplate.xsd";
	String XML_EXT = ".xml";

	String AMP = "&";

	String SEARCH_STR = "search?" + EFGImportConstants.DATASOURCE_NAME + "=";

	

	String XSL_EXT = ".xsl";
	String DEFAULT_SEARCH_FILE ="defaultSearchFile.xsl";
	String DEFAULT_TAXON_PAGE_FILE ="defaultTaxonPageFile.xsl";



	
	
	
	/**
	 * DIGIR related stuff. To be implemented with Digir like queries
	 */
	String DIGIR_TIME = "yyyy-MM-dd'T'hh:mm:ss";

	String DIGIR_NAMESPACE = "http://www.namespaceTBD.org/digir";

	String DIGIR = "digir";


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
	//IGNORE
	String DATABASENAME = "file:///C:/database.database";

	String MYSQL = "MySQL";

	String SEARCH_PAGE_STR = "search";

	String WILDCARD_STR = "wildcard";
	
	String NO_MATCH_ATTR="nomatch_attr";
	
	String WILDCARD = "*";
	String TEMPLATE_JSP_LOCATION="/templateJSP";
	String TEST_TAXON_CONFIG_PAGE =TEMPLATE_JSP_LOCATION+  "/TestTaxonPage.jsp";

	String TEST_SEARCH_CONFIG_PAGE =TEMPLATE_JSP_LOCATION+  "/TestSearchPage.jsp";

	String SERVICE_LINK_FILLER = "_EFG_";

	String SEARCHPAGE_LISTS_FILLER = "_search_page_lists.xml";

	String SEARCHPAGE_PLATES_FILLER = "_search_page_plates.xml";

	String SEARCH_TYPE_STR = "searchType";
	String TEMPLATE_NAME="templateName";
	String HTML_TEMPLATE_NAME="htmlTemplateName";
	String SEARCH_PLATES_TYPE = "plates";
	String SEARCH_TAXON_TYPE = "taxon";
	String SEARCH_LISTS_TYPE = "lists";
	String SEARCH_PDFS_TYPE = "pdfs";
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
	

	String PROPERTIES_DIR="/properties";
	String SQL_KEYWORD_KEY="sqlkeywords";
	Properties EFGProperties =project.efg.Imports.rdb.EFGRDBImportUtils.getProperties();
    Set SQL_KEY_WORDS = project.efg.Imports.rdb.EFGRDBImportUtils.getSQLKeyWords();
	String SORTING_OFF = "Sorting OFF";
	String SORTING_ON = "Sorting ON";
	String TEMPLATE_TABLE = "EFG_TEMPLATE_TABLES";
	String WEB_APPS = "webapps";
	String ILLEGALCHARACTER_STRING = "illegalCharacters";
	String ILLEGAL_CHAR_APPENDER ="illegalCharacters";
	String KEY_DROP_BACKGROUND_IMAGE = "/icons/draganddropmsg.gif";
	String EFG_MAIN_BKGD_ICON ="/icons/EFG2DataImportScreen_bg.jpg";
	String EFG_TINY_ICON ="/icons/efglogo_tiny.jpg";
	
	String EFG_WILDCARD = "_EFG_WILDCARD";
	String EFG_NUMERIC = "_EFG_NUMERIC";
	String THUMBS_VAR = "thumbsEnvVar";
	String ANDSEP = "AND";
	
	String EQUAL_SYMBOL= "EQ";
	String GREATERTHAN_SYMBOL= "GT";
	String LESSTHAN_SYMBOL= "LT";
	String GREATERTHANEQUAL_SYMBOL= GREATERTHAN_SYMBOL + EQUAL_SYMBOL;
	String LESSTHANEQUAL_SYMBOL= LESSTHAN_SYMBOL + EQUAL_SYMBOL;
	String PLUS_SYMBOL = "+";
	String ALL_TABLE_NAME = "ALL_TABLE_NAME";
	String EFG_RDB_TABLES = "EFG_RDB_TABLES";
	String CHECKBOX_SER_NAME = "checkBox.out";
	String IMAGE_DROP_BACKGROUND_IMAGE  = "/icons/draganddropmsg_medres.gif";
	String ZIP_FILE_EXTENSION =".zip";
	String SQL_DIR = "efgSQL";
	String SAND_BOX_DIRECTORY= "sandBox";
	String EFG_GLOSSARY_TABLES="EFG_GLOSSARY_TABLES";
	String IMPORT_FOLDER = EFGProperties.getProperty("imports_directory");
	String EXPORT_FOLDER = EFGProperties.getProperty("exports_directory");
	String NUMBER_OF_TAXON_ON_PAGE=EFGProperties.getProperty("numberoftaxaperpage");


	String SQL_EXT = ".sql";
	
	
	
	
	
	
}// EFGImportConstants

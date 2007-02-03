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
	Properties EFGProperties =project.efg.Imports.rdb.EFGRDBImportUtils.getProperties();

	String ZIP_EXT = ".zip";
	String ILLEGALCHARACTER_TEXT="efg_illegalcharacter";
	String SAMPLE_MEDIA_RESOURCE_FIELD = "SampleMediaResourceFields";
	String SAMPLE_SEARCHABLE_FIELD = "SampleSearchableFields";
	String SAMPLE_NEW_DISPLAY_NAME = "EFG2SampleDisplayName";
	String SAMPLE_LISTS_FIELD = "SampleListFields";
	String SAMPLE_DATA_LOCATION = "SampleDataLocation";
	String TEMPLATE_KEY = "EFGKey";
	
	
	
	
	// The 4 ff constants are appended to datasource names to make
	//html files for missing data. MediaResources will  have
	// datasourcename + IMAGEAPPENDER
	//lists will have
	//datasourcename  + EFGLISTAPPENDER and so on.
	
	String IMAGEAPPENDER="_MediaResourceErrors";
	String EFGLISTAPPENDER ="_EFGListErrors";
	String CATAPPENDER="_CategoricalErrors";
	String NUMERICSAPPENDER="_NumericErrors";
	
	//holds the above error files 
	String TEMPORARY_ERROR_FILE = "/tempError";
	
	
	/**
	 * File extensions
	 */
	String HTML_EXT = ".html";
	String XML_EXT = ".xml";
	String XSL_EXT = ".xsl";
	String ZIP_FILE_EXTENSION =".zip";
	String SQL_EXT = ".sql";
		
		
	/**
	 * Help file names read from a properties file.
	 */

	String TEMPLATE_SUCCESS_PAGE = "/UploadSuccess.jsp";
	
	
	String KEY_METADATA_HELP = EFGProperties.getProperty("key_metadata_help");
	String MAIN_DEPLOY_HELP=EFGProperties.getProperty("main_deploy_help");
	String IMAGE_DEPLOY_HELP=EFGProperties.getProperty("image_deploy_help");
	String KEYTREE_DEPLOY_HELP=EFGProperties.getProperty("keytree_deploy_help");
	String THUMBS_FILE_NAME = EFGProperties.getProperty("thumbs_file_name");
	String JSP_NAME = EFGProperties.getProperty("jsp_name");
	String TEMPLATE_UNIQUE_NAME=EFGProperties.getProperty("template_unique_name");
	String GUID=EFGProperties.getProperty("template_guid");
	
	String templateImagesDirectory =EFGProperties.getProperty("templateImagesDirectory");
	String templateCSSDirectory=EFGProperties.getProperty("templateCSSDirectory");
	String templateJavascriptDirectory=EFGProperties.getProperty("templateJavascriptDirectory");
	
	
	
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
	
	//Import constants
	String METAFILESUFFIX = "Info";	
	String IMPORT_TITLE = EFGProperties.getProperty("efg2.application.name");

	/**
	 * Servlet constants
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
	
	
	String TEMPLATE_ERROR_PAGE = "/templateJSP/TemplateError.jsp";
	String TEMPLATE_SCHEMA_NAME = "TaxonPageTemplate.xsd";
	String AMP = "&";
	String SEARCH_STR = "search?" + EFGImportConstants.DATASOURCE_NAME + "=";
	String DEFAULT_SEARCH_FILE ="defaultSearchFile" + XSL_EXT;
	String DEFAULT_TAXON_PAGE_FILE ="defaultTaxonPageFile" + XSL_EXT;
	String SEARCHTYPE = "searchType";
	String EFG_ANY="EFG_ANY_OPTION";

	String MYSQL = "MySQL";
	
	
	
	/**
	 * DIGIR related stuff. To be implemented with Digir like queries
	 */
	String DIGIR_TIME = "yyyy-MM-dd'T'hh:mm:ss";

	String DIGIR_NAMESPACE = "http://www.namespaceTBD.org/digir";

	String DIGIR = "digir";

	
	/**
	 * File names
	 */


	/**
	 * Metadata field names
	 * The standard columnnames for representing 
	 * field attributes
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

	
	
	String TAXONPAGE_XSL = "XSL_FILENAME_TAXON";
	String SEARCH_CONFIG_PAGE_XSL = "XSL_FILENAME_SEARCH_CONFIG";
	String SEARCHPAGE_PLATES_XSL = "XSL_FILENAME_SEARCHPAGE_PLATES";
	String SEARCHPAGE_LISTS_XSL = "XSL_FILENAME_SEARCHPAGE_LISTS";
	String SEARCHPAGE_PDF_XSL = "XSL_FILENAME_SEARCHPAGE_PDF";

	


	
	//IGNORE
	String DATABASENAME = "file:///C:/database.database";

	

	String SEARCH_PAGE_STR = "search";

	String WILDCARD_STR = "wildcard";
	
	String NO_MATCH_ATTR="nomatch_attr";
	
	String WILDCARD = "*";
	
	String TEMPLATE_JSP_LOCATION="/templateJSP";
	String TEST_TAXON_CONFIG_PAGE =TEMPLATE_JSP_LOCATION+  "/TestTaxonPage.jsp";

	String TEST_SEARCH_CONFIG_PAGE =TEMPLATE_JSP_LOCATION+  "/TestSearchPage.jsp";
	String TO_PDF_PAGE ="/efg2pdf.pdf";
	String QUERY_RESULTS_XML="searchqueryresultsxml";
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
	String SEARCH_SEARCH_TYPE="searches";
	
	String SEARCH_PAGE_CONFIG_TYPE = "search";
	String CONFIG_TYPE="configType";
	String EFG_APPS = EFGProperties.getProperty("efg.application.name");
	//EFG_WEB_APPS
	String EFG_WEB_APPS = "webapps";

	String HELP_FILE = EFGProperties.getProperty("help_file_name");

	String EFG_DATABASE_ROOT_NAME = "EFG Database Root";

	String EFG_DATABASE_DATA_NAME = EFGProperties.getProperty("efg_db_name");

	String EFG_DATABASE_METADATA_NAME =EFG_DATABASE_DATA_NAME;

	// CHANGE ME AFTER TESTS
	String CSV_METADATA_HEADERS =EFGProperties.getProperty("metadata_header_file");
	//lock_file_name=temp.dat
	//set_up_file_name=.setup
	String LOGS_DIR_NAME = EFGProperties.getProperty("logs_dir_name");
	String LOCK_FILE = LOGS_DIR_NAME + File.separator +
	EFGProperties.getProperty("lock_file_name");
	
	String SETUP_FILE = LOGS_DIR_NAME + File.separator +
	EFGProperties.getProperty("set_up_file_name");
	

	String PROPERTIES_DIR="/properties";
	String SQL_KEYWORD_KEY="sqlkeywords";
    Set SQL_KEY_WORDS = project.efg.Imports.rdb.EFGRDBImportUtils.getSQLKeyWords();
	String SORTING_OFF = "Sorting OFF";
	String SORTING_ON = "Sorting ON";
	String TEMPLATE_TABLE = EFGProperties.getProperty("efg_template_table_name");
	
	String ILLEGALCHARACTER_STRING = "illegalCharacters";
	String ILLEGAL_CHAR_APPENDER ="illegalCharacters";
	String KEY_DROP_BACKGROUND_IMAGE = 
		EFGProperties.getProperty("key_drop_background_image");
	String EFG_MAIN_BKGD_ICON =EFGProperties.getProperty("efg_main_bkgd_icon");
	String EFG_TINY_ICON =EFGProperties.getProperty("efg_tiny_icon");
	
	String EFG_WILDCARD = "_EFG_WILDCARD";
	String EFG_NUMERIC = "_EFG_NUMERIC";
	
	String ALL_TABLE_NAME = "ALL_TABLE_NAME";
	String EFG_RDB_TABLES = EFGProperties.getProperty("efg_rdb_tables_name");
	String MR_CHECKBOX_SER_NAME = "mr_checkBox.out";
	String IMAGE_DROP_BACKGROUND_IMAGE  = 
		EFGProperties.getProperty("image_drop_background_image");
	
	String SQL_DIR = EFGProperties.getProperty("sql_importsexports_dir");
	String EFG_GLOSSARY_TABLES=EFGProperties.getProperty("efg_glossary_table_name");
	String IMPORT_FOLDER = EFGProperties.getProperty("imports_directory");
	String EXPORT_FOLDER = EFGProperties.getProperty("exports_directory");
	String NUMBER_OF_TAXON_ON_PAGE=EFGProperties.getProperty("numberoftaxaperpage");

	String DATABASE_NAME = EFG_DATABASE_DATA_NAME;
	
	
	String TEMPLATES_FOLDER_NAME = "templateConfigFiles";
	String TEMPLATES_XML_FOLDER_NAME = TEMPLATES_FOLDER_NAME + File.separator + XML;
	String PROMPT_FOR_DIMENSIONS = "Prompt Me to Enter Dimensions";
	String RESOURCE_DIRECTORY="/resource";
	String EFG_LOCAL_REPOSITORY = "/EFGLocalRepository";
	String EFG_RESOUCRES_REPOSITORY = RESOURCE_DIRECTORY + EFG_LOCAL_REPOSITORY;
	String EFG_TRUE = "true";
	String EFG_FALSE="false";

	String IS_SAVE_PDF = "savepdf";

	String TEMPLATE_CONFIG_PAGE = "/configTaxonPage";

	String PDF_SUCCESS_PAGE = TEMPLATE_JSP_LOCATION+  "/PDFSuccessPage.html";


	
}// EFGImportConstants

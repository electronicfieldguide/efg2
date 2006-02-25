package project.efg.util;

/**
 * $Id$
 *
 * Copyright (c) 2005  University of Massachusetts Boston
 *
 * * @author <a href="mailto:kasiedu@cs.umb.edu">Jacob Kwabena Asiedu</a>
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2, or 
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */
public interface EFGImportConstants {
    String EFGIMAGES_THUMBS="efgimagesthumbs";
    String EFGIMAGES="EFGImages";
    String DEFAULT_MAX_DIM="200";
    String DATASOURCE_STR = "dataSourceName";
    String XSL_STRING = "xslName";
    String SUBMIT = "submit";

    String DATABASE_NAME="efg";
    String CREATE_DATABASE_QUERYBASE = "CREATE DATABASE IF NOT EXISTS ";
    String DEFAULTQUERYBASE = "SELECT * FROM ";
    String FILEMAKERURI = "jdbc:odbc:FileMaker_Files";
    String JDBCODBCCLASS = "sun.jdbc.odbc.JdbcOdbcDriver";
    String XML="xml";
    String RESULT_SET="resultSet";
    String SEARCH_RESULTS="SearchResult";
    String TEXT_HTML="text/html";
    String DIGIR_TIME="yyyy-MM-dd'T'hh:mm:ss";
    String DIGIR_NAMESPACE="http://www.namespaceTBD.org/digir";
    String SEARCHSTR = "searchStr";
    String HTML="HTML";
    String DIGIR ="digir";
   
    String DATASOURCE_NAME="dataSourceName";
    String MAX_DISPLAY="maxDisplay";
    String DEFAULT_MAX_DISPLAY="defaultMaxDisplay";
    String MAX_DISPLAY_IGNORE="ignore";
    String DISPLAY_FORMAT="displayFormat";
    String METAFILESUFFIX = "Info";
    String EXCEL_EXT =".xls";
    String XML_EXT =".xml";
    String DATAFILESUFFIX = "Data";
    String DATA_XML_EXT="." + DATAFILESUFFIX + XML_EXT;
    String METADATA_XML_EXT="." + METAFILESUFFIX  + XML_EXT;
    String XML_PROCESSED_DIR = "processedXML";
    String PLATES="Plates";
    String LISTS = "Lists";
    String AMP="&";
    String SEARCH_STR = "search?" + EFGImportConstants.DATASOURCE_NAME+"=";
    String DEFAULT_XSL_FILE = "ListPlatesUnsorted.xsl";
    String DEFAULT_SEARCH_TEMPLATE = "PlatesUnsorted";
    String DEFAULT_SEARCH_PAGE_STRING="Unsorted";
    String XSL_EXT = ".xsl";

    String SEARCH_PAGE_TITLE="title";
    String TEMPLATES_XML_SRC="Templates.xml" ;
    String TEMPLATES_CONFIG_SRC="EFGSearchPageConfig.xml";   
    String TEMPLATES_FOLDER_NAME="templateConfigFiles";   
    String SEARCHTYPE="searchType";
    String IMAGES_DIR = "EFGImages";

    /**
     * The standard columnnames for representing field attributes 
     * within an ODBC source.
     */
    String WEIGHT = "weight";
    String NAME = "name"; 
    String LEGALNAME = "legalName";
    String SEARCHABLE = "searchable"; 
    String SPECIESPAGEDATA = "speciesPageData";
    String HOLDSMULTIPLEVALUES = "holdsMultipleValues"; 
    String HOLDSMULTIPLEVALUESYES = "holdsMultipleValuesYes"; 
    String MEDIARESOURCEFIELD = "mediaResourceField";
    String JAVATYPE = "javaType";
    String DATATYPE = "dataType";
    String PARSERABLE="parserable";
    String ORDERED="ordered";
    String NUMERIC="numericValue";

    String NUMERICRANGE="numericRange";
    String NUMERICYES="numericYes";

    String NO = "no";
    String YES = "yes";
    
    boolean FALSE = false;
    boolean TRUE = true;
    String EFGTYPE="type";
    String EFGLISTS = "EFGLists";
    String EFGLIST = "EFGList";
    String STATISTICALMEASURES = "StatisticalMeasures";
    String STATISTICALMEASURE = "StatisticalMeasure";
    String ITEMS = "Items";
    String ITEM = "Item";
    String MEDIARESOURCES = "MediaResources";
    String MEDIARESOURCE = "MediaResource";
    String DATASOURCE="datasource";
    String DATA = "Data";
    String FIELDELEMENT = "EFGField";
    String TAXALIST = "TaxaList";
    String COMMONPATH = "CommonPath";
    String TAXONENTRY = "TaxonEntry";

    String SIMPLETYPE = "simple";
    String MEDIARESOURCETYPE = "mediaresource";
    String EFGLISTTYPE = "efgList";

    String COMMASEP=",";
    String CAPTION="caption";
    String ORSEP="OR";
    String COLONSEP=":";
    String SEMICOLON=";";
    String PIPESEP="|";
    String LEFTPARENSEP="\\(";
    String RIGHTPARENSEP="\\)";
    String ORPATTERN ="\\sor\\s";
    String EXCELPATTERN =EXCEL_EXT;
    String SERVICE_LINK="serviceLink";
    int ANYTYPE = 0;
    int NUMERICTYPE=1;
    int MEDIARESOURCEINTTYPE=2;
    int LISTTYPE=3;
    String TAXONPAGE = "_TaxonPage";
    String SEARCHPAGE = "SearchPage";
    String TAXONPAGE_FILLER = TAXONPAGE + XML_EXT;
    String SEARCHPAGE_FILLER = "_" + SEARCHPAGE + XML_EXT;
    String IMAGETYPE="Image";
    String DATABASENAME = "database";
    String MYSQL = "MySQL";
    String SEARCH_PAGE_STR="search";
    String WILDCARD_STR = "wildcard";
    String WILDCARD = "*";
    String TEST_TAXON_CONFIG_PAGE="/TestTaxonPage.jsp";
    String TEST_SEARCH_CONFIG_PAGE="/TestSearchPage.jsp";
    String SERVICE_LINK_FILLER="_EFG_";
    String SEARCHPAGE_LISTS_FILLER="_search_page_lists.xml";
    String SEARCHPAGE_PLATES_FILLER="_search_page_plates.xml";
    String SEARCH_TYPE_STR="searchType";
    String SEARCH_PLATES_TYPE="plates";
    String SEARCH_LISTS_TYPE="lists";
    
    String EFG_APPS="efg2";
    String EFG_WEB_APPS="webapps";
    String HELP_FILE = "help.html";

    String EFG_DATABASE_ROOT_NAME = "EFG Database Root";
    String EFG_DATABASE_DATA_NAME = "EFG";
    String EFG_DATABASE_METADATA_NAME = "EFG";

}// EFGImportConstants

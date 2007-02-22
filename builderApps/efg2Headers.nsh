**
*$Name$
*$Id$
* Header file for efg2 headers..
*Call all other header files here
*/

/****************************************
* JAVA, TOMCAT, MYSQL RELATED STUFF
*/


;the java classpath 
!define CLASSPATH "log4j-1.2.8.jar;mysqldriver.jar;rdbImport.jar;castor-0.9.5.2.jar;ostermillerutils_1_05_00_for_java_1_4.jar;metadataHeaders.csv;help/efglogo.jpg;."
;
; END TOMCAT, JAVA, MYSQL RELATED DEFS

/***
* SOURCE FILES DIRECTORY
*/
!define BUILDER_APPS_Dir "builderApps"

/**
* TEMP DIRECTORY ON CLIENTS MACHINE
*/
!define EFG2_TEMP_DIR "logs"
!define UPDATE_DIR "${EFG2_TEMP_DIR}"

/**
* HELPER FILES AND DIRECTORIES
*/
;Name of EFGKeys configuration application executable
!define EFG2_ABOUT_EXECUTABLE "${BUILDER_APPS_Dir}\AboutBox.exe"
;The directory where sample files can be found
!define EFG2_IMPORTER_SAMPLES_DIR "samples"
;FIX ME ASK JENN/BOB/ROB FOR APPROPRIATE NAME
!define EFG2_VIEW_SAMPLE_KEY "View a Sample Key"
;An EFG sample file 
!define EFG2_SAMPLE_KEY "IpoTest.csv"
!define EFG2_APPLICATION_NAME "efg2"
; name of log file
!define EFG2_IMPORTER_LOGS_NAME "efg2.log" 

!define EFG2_SOURCE_DIR "efg2"
; Where the current directory is
!define EFG2_PRODUCT_SOURCE_DIR "C:\cvscheckout\${EFG2_SOURCE_DIR}"
;Name of EFGDataImporter application executable
!define EFG2_IMPORTER_EXECUTABLE "${BUILDER_APPS_Dir}\Importer.exe"
;!define EFG2_ABOUT_EXECUTABLE "${BUILDER_APPS_Dir}\About.exe"
!define EFG2_DataImporter_INI "${EFG2_PRODUCT_SOURCE_DIR}\${BUILDER_APPS_Dir}\ini\importer.ini"
;lib directory where jar files can be found
!define EFG2_IMPORTER_LIB "lib"

;The lib directory of the current import
!define EFG2_PRODUCT_SOURCE_LIB "${EFG2_PRODUCT_SOURCE_DIR}\${EFG2_IMPORTER_LIB}"

/**
* WEB APPS RELATED NAMES
*/
!define WEB_APPS_HOME "EFG2_WEB_APPS_HOME" ;name of the configuration file for compiling this application
!define EFG2_WEB_APPLICATION_NAME "efg2"
!define WEB_APPS_NAME ${EFG2_APPLICATION_NAME}
!define WAR_FILE_EXT ".war"




/**
* VARIABLES TO BE USED TO WRITE TO REGISTRY
*/
!define EFG2_IMPORTER_HOME "EFG2HOME"

/**
* VARIABLES FOR SHORTCUT LINKS
*/
;The name of a link to be placed on Start Menu
!define EFG2_IMPORTER_LINK_NAME "EFG Data Importer"


/**
* VERSION INFORMATION
*/
!define EFG2_SHORT_VERSION_NUMBER "0.1"
!define EFG2_CURRENT_VERSION_NUMBER "${EFG2_SHORT_VERSION_NUMBER}.0.0"
/**
* REGISTRY RELATED VARIABLES
*/
;{EFG_APPLICATIONS_NAME} is set in efgHeaders.nsh
;Name of EFGDataImporter application
!define EFG2_PRODUCT_NAME "EFGDataImporter"
!define EFG2_PRODUCT_DIR_REG "SOFTWARE\${EFG_APPLICATIONS_NAME}\${EFG2_PRODUCT_NAME}\${EFG2_SHORT_VERSION_NUMBER}"
!define EFG2_PRODUCT_UNINST "${EFG2_PRODUCT_DIR_REG}\Uninstall"
!define EFG2_PRODUCT_UNINST_KEY "${EFG2_PRODUCT_UNINST}\${EFG2_PRODUCT_NAME}"
!define EFG2_PRODUCT_UNINST_ROOT_KEY "SHCTX"
;Path to the EFG Configuration directory relative to the install directory
!define EFG2_IMPORTER_DIR "$INSTDIR\${EFG2_PRODUCT_NAME}"
;!define EFG2_PRODUCT_REG_DIR "SOFTWARE\${EFG2_APPLICATION_NAME}"
!define WEB_APPS_WAR "${WEB_APPS_NAME}${WAR_FILE_EXT}"
!define EFG2_ABOUT_LINK_NAME "About"

/* relative path from CATALINA_HOME to web application*/
!define EFG2_WEB_APPS "webapps\efg2"


/*****************************
* TOMCAT RELATED STUFF
******************************/
!define EFG2_RENAME_TOMCAT_USERS_BATCH "${EFG2_IMPORTER_DIR}\{EFG2_IMPORTER_LIB}\resetTomcatUsersFile.bat"
!define EFG2_TOMCAT_USERS_OLD_FILE  "conf\tomcat-users.xml_EFG_old"
!define EFG2_TOMCAT_USERS_CURRENT_FILE "conf\tomcat-users.xml"
!define EFG2_TOMCAT_USER_BATCH "tomcatUsers.bat"
!define EFG2_TOMCAT_USER_EXEC "${EFG2_PRODUCT_SOURCE_DIR}\${EFG2_IMPORTER_LIB}\${EFG2_TOMCAT_USER_BATCH}"
; END TOMCAT DEFS
InstallDir $PROGRAMFILES\${EFG2_PRODUCT_NAME}${EFG2_SHORT_VERSION_NUMBER}


/**
*Name of application and version
*/
Name "${EFG2_PRODUCT_NAME}${EFG2_SHORT_VERSION_NUMBER}"
OutFile "${EFG2_PRODUCT_NAME}${EFG2_SHORT_VERSION_NUMBER}Setup.exe"





























  
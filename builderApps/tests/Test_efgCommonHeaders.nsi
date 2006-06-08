/**
*$Name$
*$Id$
* Header file for efg2 headers..
*Call all other header files here
*/
/**
* Contains all the headers needed to create most EFG aplications.
*This file must be called first in any files that intends to include it.
* 
*/
!include "efgHeaders.nsh"
;All EFG applications should be installed relative to this Name
!define EFG_APPLICATIONS_NAME "EFG"

;The name of the product to be built
!define UNINSTALL_STRING "UninstallString"
!define DISPLAY_VERSION "DisplayVersion"
!define CATALINA_HOME_NAME "CATALINA_HOME"
!define SPACE " "
;Language dependency
LangString SELECT_COMPONENT ${LANG_ENGLISH} "Please select at least one Application to uninstall. Click 'OK' and then the 'Back' button to get do so.!!"
;The name of the product to be built
!define EFG_PRODUCT_PUBLISHER "UMass Boston"
!define EFG_PRODUCT_WEB_SITE "http://efg.cs.umb.edu/keys"
;Stuff needed to check the existence of Tomcat
!define TOMCAT5_KEY "SOFTWARE\Apache Software Foundation\Tomcat\5.0" 
!define TOMCAT4_KEY "SOFTWARE\Apache Group\Tomcat\4.1" 
!define TOMCAT4_SERVICES_NAME "Apache Tomcat 4.1"
!define TOMCAT5_SERVICES_NAME "Apache Tomcat"
;REGISTRY KEY OF MYSQL MAKES IT UNSCALABLE..THEY DO NOT HAVE THE 
; vERSION INFORMATION ON THE PARENT NODE OF THE LEAF NODE
!define MYSQL_KEY "SOFTWARE\MYSQL AB\MYSQL SERVER 5.0" 
!define MYSQL_SERVICES_NAME "MySQL"
Name "Test"
OutFile "Test.exe"

Section ""

SectionEnd





























  
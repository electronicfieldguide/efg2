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
;delete me after test


;All EFG applications should be installed relative to this Name
!define EFG_APPLICATIONS_NAME "EFG"

;The name of the product to be built
!define UNINSTALL_STRING "UninstallString"
!define DISPLAY_VERSION "DisplayVersion"
!define CATALINA_HOME_NAME "CATALINA_HOME"
!define JRE_EXEC_NAME "JRE_EXEC"
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
!define JAVA_REGISTRY_KEY "SOFTWARE\JavaSoft\Java Development Kit\1.4"
!define JAVA_INSTALL_MESSAGE "Java development Kit 1.4 is not installed on this system. $\r\
  EFGDataImporter application needs JDK1.4 to run.$\r Do you want to install it now?"
!define EXIT_INSTALL_MESAGE "Exiting installation JDK1.4 is required.."

!define MYSQL_LOCAL_MESSAGE "MySQL5.x was not found in your registry.$\r\
                      However a MYSQL_HOME environment variable is set.$\r\ 
                      Does it point to a working MySQL5.x (EFGDataImporter needs to run under MySQL5.x)?" IDNO InstallMySQL
!define MYSQL_INSTALL_MESSAGE "MySQL is not installed on this system. $\rEFGDataImporter needs to run \
 with MySQL.$\r Do you want to install it now?"





























  
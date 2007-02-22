/**
* Downloads

*We recommend that you have a downloads directory where you put
*all of the stuff that you need to add to the executable
*The DOWNLOADS_DIR must be set by the calling application
*/


!define DOWNLOADS_DIR "C:\downloads"
;MySQL installer
!define MYSQL_SOURCE_DIR "${DOWNLOADS_DIR}"
!define MYSQL_SOURCE_NAME "MySQLSetup.exe"

;JDK installer
!define JDK_SOURCE_DIR "${DOWNLOADS_DIR}"
!define JDK_SOURCE_NAME "j2sdk-1_4_2_10-windows-i586-p.exe"
!define JAVA_REGISTRY_KEY "SOFTWARE\JavaSoft\Java Development Kit\1.4"
!define JAVA_INSTALL_MESSAGE "Java development Kit 1.4 is not installed on this system. $\r\
  EFGDataImporter application needs JDK1.4 to run.$\r Do you want to install it now?"



;Tomcat Installer
!define TOMCAT_SOURCE_DIR "${DOWNLOADS_DIR}"
!define TOMCAT_SOURCE_NAME "jakarta-tomcat-5.0.28.exe"






  
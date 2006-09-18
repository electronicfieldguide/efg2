@echo off
REM $Id$
REM run to copy war file to webapps

if not exist ".\resource\logs\.setup" (
 call chkenv.bat
)
 
 @echo off
 if defined CATALINA_HOME (
	echo Deploying EFG2 Web application and required resources
        xcopy .\resource\efg2.war "%CATALINA_HOME%"\webapps\ /E/Q
	xcopy .\resource\efg2.xml "%CATALINA_HOME%"\conf\Catalina\localhost /Y/Q
	
	xcopy .\resource\mysqldriver.jar "%CATALINA_HOME%"\common\lib /Y/Q  
	echo done
	echo Please restart Tomcat Server..
       
    ) 
@echo off
REM $Log$
REM Revision 1.1.2.2  2006/09/18 18:11:01  kasiedu
REM no message
REM
REM Revision 1.1.2.1  2006/09/01 02:55:58  kasiedu
REM no message
REM

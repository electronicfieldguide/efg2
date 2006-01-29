@echo off
REM $Id$

echo.
echo.Setting up the efg Application





if defined CATALINA_HOME (
   
    echo.
    echo.CATALINA_HOME found...
) else (
    set cata=1
   
    echo.
    echo.Please set environment variable for CATALINA_HOME [Tomcat root directory]
    echo.and add %%CATALINA_HOME%%\common\lib\servlet.jar to your system classpath
    echo. if running Tomcat 4
    echo.and add %%CATALINA_HOME%%\common\lib\servlet-api.jar to your system classpath
    echo. if running Tomcat 5
    goto error

)

if defined JAVA_HOME (
    set java=1
    echo.
    echo.JAVA_HOME found...
) else (
    set err=1
    echo.
    echo.Please set environment variable for JAVA_HOME [JAVA jdk root directory]
    echo.and add %%JAVA_HOME%%\bin to your system path.
    goto error
)

copy tomcat-users.xml "%CATALINA_HOME%"\conf\ /Y 
copy efg.xml "%CATALINA_HOME%"\conf\Catalina\localhost /Y 
copy lib\mysqldriver.jar "%CATALINA_HOME%"\common\lib



goto success



:error
echo.
echo.Some environment variables are missing and will cause efg installation to fail.
echo.Please consult the InstallationGuide.txt that comes with your EFG module and
echo.set up of the environment variables accordingly.
goto end

:success
echo.
echo.Set up completed successfully!!

:end
echo.

REM $Log$
REM Revision 1.2  2006/01/29 03:09:41  kasiedu
REM Updated stuff allows efg2 to run in a web application called efg2
REM
REM Revision 1.1  2006/01/26 04:20:46  kasiedu
REM no message
REM

@echo on



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

xcopy tomcat-users.xml "%CATALINA_HOME%"\conf\ /E
xcopy efg.xml "%CATALINA_HOME%"\conf\Catalina\localhost /E
xcopy efg.war "%CATALINA_HOME%"\webapps


goto success



:error
echo.
echo.Some environment variables are missing and will cause efg installation to fail.
echo.Please consult the InstallationGuide.txt that comes with your EFG module and
echo.set up of the environment variables accordingly.
goto end

:success
echo.
echo.Great! All needed evironment variables are found!

:end
echo.

REM $Log$
REM Revision 1.1  2006/01/26 04:20:46  kasiedu
REM no message
REM

@echo on



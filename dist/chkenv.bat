@echo off
REM $Id$

echo.
echo.Checking environment variables...

set err=0

set cata=0
set java=0



if defined CATALINA_HOME (
    set cata=1
   
    echo.
    echo.CATALINA_HOME found. 
    echo.It points to %CATALINA_HOME%
    echo.
) else (
    set err=1
    echo.
    echo.Please set environment variable for CATALINA_HOME [Tomcat root directory]
 )
if defined JAVA_HOME (
    set java=1
    echo.
    echo.JAVA_HOME found...
    echo.It points to %JAVA_HOME%
    echo.
) else (
    set err=1
    echo.
    echo.Please set environment variable for JAVA_HOME [JAVA jdk root directory]
    echo.and add %%JAVA_HOME%%\bin to your system path.
)
if not exist "C:\Program Files\MySQL\MySQL Server 5.0" (
    echo.
    echo. Application could not find MySQL Server 5.0 at C:\Program Files\MySQL\MySQL Server 5.0
    echo. If you do not have MySQL 5.0 already installed , please do so. The application will not work without it. 
    set err=1	
) else (
    echo.
    echo.MySQL 5.0  found.
)

if defined MAGICK_HOME (
    set cata=1
   
    echo.
    echo.MAGICK_HOME found. 
    echo.It points to %MAGICK_HOME%
    echo.
) else (
   
    echo. For better quality thumbnail image generation, install Image magick and set the MAGICK_HOME environment variable to point to it.
    echo.
 )


if %err% == 1 (
    goto error
)

goto success


:error
echo.
echo.Some environment variables are missing and will cause EFG2 application to fail.
echo Please consult EFG2 documentation for instructions on adding env vars.
goto end

:success
echo.
echo.Great! All needed evironment variables are found!

:end
echo.

REM $Log$
REM Revision 1.2  2006/12/08 03:50:38  kasiedu
REM no message
REM
REM Revision 1.1.2.1  2006/09/01 02:55:58  kasiedu
REM no message
REM


@echo on



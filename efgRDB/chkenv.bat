
@echo off

echo.
echo.Checking environment variables...

set err=0
set war=0
set ant=0
set cata=0
set osroot=0
set java=0

if defined ANT_HOME (
    set ant=1
    echo.
    echo.ANT_HOME found...
) else (
    set err=1
    echo.
    echo.Please set environment variable for ANT_HOME [ANT root directory]
    echo.and add %%ANT_HOME%%\bin to your system path.
)

if defined CATALINA_HOME (
    set cata=1
    echo.
    echo.CATALINA_HOME found...
) else (
    set err=1
    echo.
    echo.Please set environment variable for CATALINA_HOME [Tomcat root directory]
    echo.and add %%CATALINA_HOME%%\common\lib\servlet.jar to your system classpath.
)

if defined OS_ROOTDIR (
    set osroot=1
    echo.
    echo.OS_ROOTDIR found...
) else (
    set err=1
    echo.
    echo.Please set environment variable for OS_ROOTDIR [ObjectStore root directory]
    echo.and add %%OS_ROOTDIR%%\..\osji\tools.jar to your system classpath and
    echo.%%OS_ROOTDIR%%\..\osji\bin to your system path.
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
)

if defined CLASSPATH (
    if %osroot% == 1 (
        echo %CLASSPATH% | find "\osji\tools.jar" > nul
        if errorlevel == 1 (
            set err=1
            echo.
            echo.Please add tools.jar [from ObjectStore osji\] to your classpath.
        ) else (
            echo.
            echo.tools.jar from ObjectStore osji\ found in classpath...
        )
        set errorlevel=0
    )
    if %cata% == 1 (    
        echo %CLASSPATH% | find "%CATALINA_HOME%\common\lib\servlet.jar" > nul
        if errorlevel == 1 (
            set war=1
            echo.
            echo.Warning:
            echo.Can't find %%CATALINA_HOME%%\common\lib\servlet.jar in system classpath.
            echo.Your EFG may not compile with the same servlet classes that Tomcat 
            echo.executes with.
        ) else (
            echo.
            echo.%%CATALINA_HOME%%\common\lib\servlet.jar found in classpath...
        )
        set errorlevel=0
    )
) else (
    set err=1
    echo.
    echo.Please set environment variable for classpath.
)

if defined PATH (
    if %ant% == 1 (
        echo %PATH% | find "%ANT_HOME%\bin" > nul
        if errorlevel == 1 (
            set err=1
            echo.
            echo.Please add %%ANT_HOME%%\bin to your system path.
        ) else (
            echo.
            echo.%%ANT_HOME%%\bin found in system path...
        )
        set errorlevel=0
    )
    if %osroot% == 1 (
        echo %PATH% | find "osji\bin" > nul
        if errorlevel == 1 (
            set err=1
            echo.
            echo.Please add %%OS_ROOTDIR%%\..\ojsi\bin to your system path.
        ) else (
            echo.
            echo.%%OS_ROOTDIR%%\..\ojsi\bin found in system path...
        )
        set errorlevel=0
    )
    if %java% == 1 (
        echo %PATH% | find "%JAVA_HOME%\bin" > nul
        if errorlevel == 1 (
            set war=1
            echo.
            echo.Warning:
            echo.Can't find %%JAVA_HOME%%\bin in your system path.
            echo.You will need this set if running Tomcat LE [the version without 
            echo.an embedded JVM] as a service.
        ) else (
            echo.
            echo.%%JAVA_HOME%%\bin found in system path...
        )
        set errorlevel=0
    )
) else (
    set err=1
    echo.Please set environment variable for system path.
)

if %err% == 1 (
    goto error
)

if %war% == 1 (
    goto warning
)

goto success

:warning
echo.
echo.Some environment variables missing and might cause efg installation to fail.
echo.Please consult the InstallationGuide.txt that comes with your EFG module
echo.for the setup of the environment variables.
goto end

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

@echo on


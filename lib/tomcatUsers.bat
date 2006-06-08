@echo off
set LOG4J=log4j-1.2.8.jar
set IMPORT_JAR=rdbImport.jar
set OSTER_JAR=ostermillerutils_1_05_00_for_java_1_4.jar
set CASTOR=castor-0.9.5.2.jar
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
set POINT=.
set CP=%CP%;%LOG4J%;%IMPORT_JAR%;%CD%;%POINT%;%OSTER_JAR%;%CASTOR%
set CLASSPATH=%CP%




java -classpath "%CLASSPATH%" project/efg/util/TomcatUsers %1

ECHO.

set CLASSPATH=%_CP%

@echo on
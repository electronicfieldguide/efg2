@echo off


set LOG4J=log4j-1.2.8.jar
set MYSQL_DRIVER=mysqldriver.jar
set IMPORT_JAR=rdbImport.jar
set OSTER_JAR=ostermillerutils_1_05_00_for_java_1_4.jar
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
set POINT=.
set CP=%CP%;%LOG4J%;%IMPORT_JAR%;%CD%;%POINT%;%MYSQL_DRIVER%;%OSTER_JAR%
set CLASSPATH=%CP%




java -classpath "%CLASSPATH%" project/efg/Import/CSV2Database

ECHO.

set CLASSPATH=%_CP%

@echo on
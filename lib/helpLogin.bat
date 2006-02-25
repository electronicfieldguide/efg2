@echo off


set LOG4J=log4j-1.2.8.jar
set MYSQL_DRIVER=mysqldriver.jar
set IMPORT_JAR=rdbImport.jar
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
set POINT=.
set CP=%CP%;%LOG4J%;%IMPORT_JAR%;%CD%;%POINT%;%MYSQL_DRIVER%
set CLASSPATH=%CP%




java -classpath "%CLASSPATH%" project/efg/Import/HelpFile

ECHO.

set CLASSPATH=%_CP%

@echo on
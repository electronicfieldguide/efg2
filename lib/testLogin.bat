@echo off



set LOG4J=log4j-1.2.8.jar
set common_logs=commons-logging.jar
set common_codec=commons-codec-1.3.jar
set MYSQL_DRIVER=mysqldriver.jar
set IMPORT_JAR=rdbImport.jar
set SPRING=spring.jar
set ROWSET=rowset.jar
set OSTER_JAR=ostermillerutils_1_05_00_for_java_1_4.jar
set COMMONS=commons-logging.jar
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
set POINT=.
set CP=%CP%;%LOG4J%;%common_logs%;%common_codec%;%IMPORT_JAR%;%CD%;%POINT%;%MYSQL_DRIVER%;%SPRING%;%ROWSET%;%OSTER_JAR%;%COMMONS;.;properties%
set CLASSPATH=%CP%
set cat=%CATALINA_HOME%

echo %cat%


java -classpath "%CLASSPATH%" project/efg/Imports/efgImpl/LoginDialog "%cat%"

ECHO.

set CLASSPATH=%_CP%

@echo on
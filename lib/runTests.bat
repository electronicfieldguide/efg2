@echo off



set LOG4J=log4j-1.2.8.jar
set MYSQL_DRIVER=mysqldriver.jar
set IMPORT_JAR=rdbImport.jar
set SPRING=spring.jar
set ROWSET=rowset.jar
set OSTER_JAR=ostermillerutils_1_05_00_for_java_1_4.jar
set COMMONS=commons-logging.jar
set CASTOR=castor-0.9.5.2.jar
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
REM set SERVLETJAR=C:\Program Files\Apache Software Foundation\Tomcat 5.0\common\lib\servlet-api.jar
set POINT=.
set JUNIT_HOME=junit.jar
set SERVLETAPI="servlet-api.jar"
set CP=%CP%;%SERVLETAPI%;%LOG4J%;%IMPORT_JAR%;%CD%;%POINT%;%MYSQL_DRIVER%;%CASTOR%;%SPRING%;%ROWSET%;%OSTER_JAR%;%COMMONS%;%JUNIT_HOME%

set CLASSPATH=%CP%
echo %CLASSPATH%




java -classpath "%CLASSPATH%" project/efg/servlets/unitTests/SearchableImplTest

ECHO.

set CLASSPATH=%_CP%

@echo on
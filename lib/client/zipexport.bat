@echo off
set XERCES_JAR=xercesImpl.jar
set LOG4J=log4j-1.2.8.jar
set OSCACHE=oscache-2.3.jar
set CASTOR=castor-0.9.5.2.jar
set common_logs=commons-logging.jar
set common_codec=commons-codec-1.3.jar
set MYSQL_DRIVER=mysqldriver.jar
set IMPORT_JAR=rdbImport.jar
set SPRING=spring.jar
set ROWSET=rowset.jar
set SERVLET_JAR=servlet-api.jar
set OSTER_JAR=ostermillerutils_1_05_00_for_java_1_4.jar
set COMMONS=commons-logging.jar
set SPRING_CONFIG_HOME=springConfig
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
set POINT=.
set CP=%XERCES_JAR%;%OSCACHE%;%SERVLET_JAR%;%CASTOR%;%LOG4J%;%common_logs%;%common_codec%;%IMPORT_JAR%;%CD%;%POINT%;%MYSQL_DRIVER%;%SPRING%;%ROWSET%;%OSTER_JAR%;%COMMONS%;properties;%SPRING_CONFIG_HOME%;%CP%
set CLASSPATH=%CP%
set cat=%CATALINA_HOME%




java -cp ".;%CLASSPATH%" project/efg/exports/ZipExport
ECHO.

set CLASSPATH=%_CP%

@echo on
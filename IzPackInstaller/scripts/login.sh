#!/bin/sh
java -classpath ".:xercesImpl.jar:xerces.jar:xalan.jar:xml-apis.jar:commons-io-1.2.jar:log4j-1.2.8.jar:oscache-2.3.jar:castor-0.9.5.2.jar:commons-logging.jar:commons-codec-1.3.jar:mysqldriver.jar:rdbImport.jar:spring.jar:rowset.jar:servlet-api.jar:ostermillerutils_1_05_00_for_java_1_4.jar:commons-logging.jar:springConfig" project/efg/Imports/efgImpl/LoginDialog "%{catalina_home}"

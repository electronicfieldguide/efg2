#!/bin/bash
#$Id$

java -classpath "oscache-2.3.jar:servlet-api.jar:castor-0.9.5.2.jar:log4j-1.2.8.jar:commons-logging.jar:rdbImport.jar:mysqldriver.jar:spring.jar:rowset.jar:ostermillerutils_1_05_00_for_java_1_4.jar:commons-codec-1.3.jar:.:./properties" project/efg/Imports/efgImpl/LoginDialog "$CATALINA_HOME"


#!/bin/sh
if [ $# = 0 ]
then
    echo usage: $0 \<arg\> where arg is \'p\' or \'d\' \(omit quotes\)
    exit
else

#echo in else with $1
  case "$1" in
     'd')
        # echo hello;
        CATALINA_HOME=$CATALINA_BASE;
     'p')
        CATALINA_HOME=$CATALINA_HOME;
     *)
     #adjust this error message if you add cases above
     echo  hey you misspelled an argument! ;
     echo usage: $0 \<arg\> where arg is \'production\' or \'develop\' \(omit quotes\);
     exit;;
  esac
fi
export CATALINA_HOME
JAVA_HOME=/tools/jdk-1.5.0_08/usr/jdk/jdk1.5.0_08 
export JAVA_HOME
MAGICK_HOME=/tools/imagemagick-6.1.8/bin
export MAGICK_HOME


$JAVA_HOME/bin/java -classpath ".:xercesImpl.jar:xerces.jar:xalan.jar:xml-apis.jar:commons-io-1.2.jar:log4j-1.2.8.jar:oscache-2.3.jar:castor-0.9.5.2.jar:commons-logging.jar:commons-codec-1.3.jar:mysqldriver.jar:efg2Client.jar:spring.jar:rowset.jar:efgTemplate.jar:efgDocument.jar:ostermillerutils_1_05_00_for_java_1_4.jar:springConfig" project/efg/client/impl/gui/LoginDialog "$CATALINA_HOME" 


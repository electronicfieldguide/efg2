#!/bin/bash
#$Id$

if [ -e "./resources/logs/.setup" ]
then
    echo ""
else
    sh chkenv.sh
fi

if [ -d "$CATALINA_HOME" ]
then
    echo "Copying application to $CATALINA_HOME/webapps/"
    cp ./resource/efg2.war "$CATALINA_HOME"/webapps
    cp ./resource/efg2.xml "$CATALINA_HOME"/conf/Catalina/localhost
    cp ./resource/tomcat-users.xml "$CATALINA_HOME"/conf
    cp ./resource/mysqldriver.jar "$CATALINA_HOME"/common/lib

    echo "done"
    echo "Please restart Tomcat server"
else
    echo ""
    echo "Please set environment variable for CATALINA_HOME [Tomcat root directory]"
    echo ""
fi
#$Log$
#Revision 1.1.2.1  2006/09/13 17:06:34  kasiedu
#Added new shell scritps fro macs and linux/unix
#

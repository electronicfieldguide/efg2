#!/bin/sh

if [ -d "%{catalina_home}" ] ; then
    echo "Copying application to %{catalina_home}/webapps/"
    cp %{INSTALL_PATH}/resource/efg2.war %{catalina_home}/webapps
    cp %{INSTALL_PATH}/resource/efg2.xml %{catalina_home}/conf/Catalina/localhost
    cp %{INSTALL_PATH}/resource/mysqldriver.jar %{catalina_home}/common/lib
    echo "done"
    echo "Please restart Tomcat server"
else
    echo "Edit this script as follows"
    echo "Replace all of the variables in the %{REPLACE_ME} with hard links"
fi


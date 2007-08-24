#!/bin/bash

if [ -d "$CATALINA_HOME/webapps/efg2" ]
then
    echo "Your web application is located at: $CATALINA_HOME/webapps/efg2"
else
    echo "Please set the CATALINA_HOME environment variable and run deploywebapps.sh"
    echo ""
fi

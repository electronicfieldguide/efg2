#!/bin/bash
# $Id$
err=0
echo " "

if [ -d "$CATALINA_HOME" ]
then
    echo "CATALINA_HOME environment variable is set."
    echo "It points to $CATALINA_HOME"
    echo " "
else
    err=1
    echo "Please set the $CATALINA_HOME environment variable"
    echo "It should point to the root of your Tomcat installation directory"
    echo " "
fi

if [ -d "$JAVA_HOME" ]
then
    echo "JAVA_HOME environment variable is set."
    echo "It points to $JAVA_HOME"
    echo " "
else
    err=1
    echo "Please set the $JAVA_HOME environment variable"
    echo " It should point to the root of your Java installation directory"
    echo " "
fi

if [ -d "$MYSQL_ROOT" ]
then
    echo "MYSQL_ROOT environment variable is set."
    echo "It points to $MYSQL_ROOT"
    echo ""
else
    err=1
    echo "Please set the $MYSQL_ROOT environment variable"
    echo "It should point to the root of your MYSQL installation directory"
    echo " "
fi

if [ -d "$MAGICK_HOME" ]
then
    echo "MAGICK_HOME environment variable is set."
    echo "It points to $MAGICK_HOME"
    echo " "
else
    echo "For better quality thumb nail generation"
    echo "install Image magick and set the $MAGICK_HOME environment variable"
    echo "It should point to the root of your Image magick installation directory"
    echo " "
fi
if [ "$err" = "1" ]
then
    echo "You need to set both the CATALINA_HOME and JAVA_HOME environment variables to use this application."
    echo "CATALINA_HOME must point to the root of your Tomcat installation"
    echo "JAVA_HOME must point to the root of your Java installation"
else
    echo "Great! All needed evironment variables are found!!"
fi



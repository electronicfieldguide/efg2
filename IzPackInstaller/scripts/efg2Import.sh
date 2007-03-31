#!/bin/sh
cd ./resource
sh login.sh

if [ -d "resource" ] ; then
    echo " "
else
    cd ../
fi


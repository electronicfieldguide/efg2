$Id$
You must have the following applications installed
in order to use the EFG2 software. 
Please abort the installation if you do not have them 
already installed and configured on your machine.

Note: INSTALL_PATH is where the application is installed.

Required Applications

Mac Users must read the MacReadMe.txt file
located in mac directory before they continue.

1) Java Runtime 1.5 
2) Jakarta-Tomcat 5.0(from the Apache jakarata project. 
EFG2 requires that Tomcat 5.0 is run with JDK 1.4 .)
 You must have write privileges to the Jakarta-Tomcat 5.0
 folder
3) Image Magick 6.3.2
4) MySQL Server 5.0 (must be at port 3306)



The EFG2 application will fail to work
if you do not have these installed.


MySQL Information
1) You must have root privileges on localhost and % on your
mysql database server to use the EFG2 Application.
You can do that by logging into mysql and issuing the following 
two commands:

GRANT ALL PRIVILEGES ON *.* TO root;

GRANT ALL PRIVILEGES ON *.* To 'root'@'localhost'

Note: root user can be replaced with any username of your choice
but it must be the user that you will use to log into
mysql database via EFG2DataImporter (see manual for more info).


Restart MySQL after making these changes.



Download and Install

1. Download EFG2Installer_1_0_0_2_ For_Linux.zip (linux users)
or EFG2Installer_1_0_0_2_ For_Mac.zip(mac users) 
and unzip it.  
2.You should see a .jar file in there.

3.The Installer requires java 1.5.
To check your default java version open a command window
and type:
java -version

If your OS allows it, you can double click on 
the jar file to launch the installation.
 
Otherwise, as a superuser type:
Open a command window to the unzipped directory and 
enter:
java -jar JAR_FILE.jar 

JAR_FILE is the name of the .jar file in the unzipped 
directory.


2. When prompted for Tomcat home, enter the root of your
Tomcat installation(if you followed our instructions for
mac users it is /etc/jakarta-tomcat-5.0.30.
Note once again that you need Tomcat 5.0 from the 
Apache jakarta project. EFG2 relies on the directory structure from the
distribution made by Apache Jakarata).

3. When prompted for Image Magick home enter the path to
where the Image magick command 'convert' can be found.

(If you followed our instructions for mac users it is:
/usr/local/bin)

For instance if the convert command is found at:
/usr/bin enter /usr/bin



4. After installation, verify that efg2.war file
was copied to your Tomcat application.
You can do that as follows:
Open a command window and cd to where your Tomcat is installed.
cd to webapps folder and verify that efg2.war is there.
If it is not there:
open a command window to the INSTALL_PATH
and execute deploywebapps.sh. 

You may have to change the permissions on this file to execute it.
Files will be copied to your Tomcat_Home so
make sure that the user that executes this script
has write privileges on your Tomcat.



5. Restart Tomcat 

6. Application assumes that JRE1.5 is default java version.
If it is not, then edit INSTALL_PATH/resource/login.sh appropriately
so that JRE 1.5 is used in the script to launch the EFG2 application.

7. To launch the importer, open a command window to INSTALL_PATH
and execute efg2Import.sh (You must have the right privileges to execute this script)

8. For EFG2 manual ( see INSTALL_PATH/efg2doc.html).



Trouble Shooting For Unix/Macs
Q: Clicking on 'View EFGs' shows a 'No datasource uploaded' message

A: This probably means that the user created by the EFG2 DataImporter
does not have enough privileges. So do the following:
a) Open a command window to mysql and enter the following. 

At the mysql prompt type: USE mysql
Then at the next command prompt enter:
UPDATE user SET password=password('efg') WHERE user='efg';

Then enter:
flush privileges;

b) Exit Mysql and restart MySQL , then restart Tomcat.











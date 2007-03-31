Installation Instructions for Mac and Linux users

$Id $

**Note** Do not proceed with the EFG2 installation until you verify that all of the following applications are installed on the server: 

1) Java Runtime Environment 1.5  or later(The installer requires java 1.5 or later. To check your default java version open a command window and type: java -version)
2) Jakarta-Tomcat 5.0 (from the Apache Jakarta project. EFG2 requires that Tomcat 5.0 is run with JDK 1.4. You must also have write privileges to the Jakarta-Tomcat 5.0 folder)
3) MySQL Server 5.0 or later (must be at port 3306)
4) ImageMagick 6.1 or later

***************
Mac Users: Please refer to the special instructions in the ReadMe_MacUsers.txt file if you need assistance!

Linux and Unix Users: The EFG2 software has been successfully tested on Fedora Core ??. If you use a different operating system configuration will likely be tricky. For those of you not using a recent version of Fedora/Red Hat, we recommend that you download and install the applications listed above directly from the product websites rather than using any packaged versions your specific your operating system may provide.
***************

I) MySQL Instructions:

You must have root privileges on localhost and % on your MySQL database server to use the EFG2 Application. To verify this:

   1) Log into MySQL:

   /usr/local/mysql/bin/mysql -u root

   2) At the prompt ( mysql>) type each of these lines (press the Enter key after each line):

   GRANT ALL PRIVILEGES ON *.* TO root;

   GRANT ALL PRIVILEGES ON *.* To 'root'@'localhost';

   3) Note: root user can be replaced with any username of your choice but it must be the username that you will use to log into MySQL via the EFG2 DataImporter (see manual for more info). Do Not create a user called "efg" - that user name is reserved by the EFG2 software.

   4) Quit from MySQL

   5) Restart the MySQL server

II) Download and Install EFG2

   1) If you haven't already, download EFG2Installer_1_0_0_2_ For_Linux.zip (linux users) or EFG2Installer_1_0_0_2_ For_Mac.zip (Mac users) and unzip it.

   2) Start the installation.

   - If your OS allows it, you can double click on the .jar file in the install folder to launch the installation.

   - Otherwise, as a superuser, open a terminal window to the install folder and type:

     java -jar JAR_FILE.jar

     (JAR_FILE is the name of the .jar file in the unzipped directory.)

   - Please note the folder in which the files are installed.

   3) When prompted for Tomcat home, enter the root of your Tomcat installation (if you followed our instructions for mac users it is /usr/local/tomcat).

      Note once again that you need Tomcat 5.0 from the Apache Jakarta project. EFG2 relies on the directory structure from the distribution made by Apache Jakarta. Other install packages that do not have the same directory structure will likely not work with EFG2.

   4) When prompted for the ImageMagick home, enter the path to where the ImageMagick 'convert' command can be found. If you followed our instructions for mac users it is /usr/local/bin.

   5) After installation, verify that efg2.war file was copied to your Tomcat application.

   - Open a terminal window and go to the Tomcat folder named webapps (For the standard Mac install, you can type: cd /usr/local/tomcat/webapps and then type the command "ls" to see a list of files.).

   - If the efg2.war file is missing, open a terminal window to the EFG2 installation folder
and execute the deploywebapps.sh script:

     sh deploywebapps.sh

   - If you get an error trying to run deploywebapps.sh, you may have to change the permissions on that file so that it is executable (i.e. chmod 755 deploywebapps.sh). The script copies files to your Tomcat directory, so be sure to verify that the user that executes this script has write privileges for that directory (you may need to be root/admin).

   6) Restart Tomcat

   7) To launch the EFG2 software:

   - Mac users can double-click on the EFG2Launcher.app in the install folder, or drag that file onto the dock and it will create a shortcut that you can click on to start the application.

   - Else open a command window to the path where the EFG2 software was installed and execute efg2Import.sh (You must have the correct privileges to execute this script):

     sh efg2Import.sh 

   8) For further assistance, see the EFG2 manual (efg2doc.html in the folder where EFG2 was installed).


Troubleshooting

Q: What if JRE 1.5 is not my default java version?

A: The EFG2 application assumes that JRE1.5 is default java version. If it is not, then edit INSTALL_PATH/resource/login.sh appropriately so that JRE 1.5 is used in the script to launch the EFG2 application (INSTALL_PATH is where EFG2 is installed.).

Q: Why does clicking on 'View EFGs' show a 'No datasource uploaded' message?

A: This probably means that the user created by the EFG2 DataImporter does not have enough privileges. If so, do the following:

   - Open a command window to MySQL and type: 

     use mysql

   - At the next command prompt type:

     UPDATE user SET password=password('efg') WHERE user='efg';

   - Then type:

     flush privileges;

   - Exit MySQL and restart it, then restart Tomcat.





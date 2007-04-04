EFG Data Importer Installation for Mac Users: Additional Instructions For Mac OS X 10.4.x
$Id$

Note: Experienced users may find that they can skip these instructions.

Below are instructions for Mac users to get everything up and running prior to installing the EFG Data Importer software. The installation folder for EFG Data Importer contains a directory called "mac" that contains all the files the typical user would need to follow the instructions below.

Applications required prior to EFG Data Importer installation:

1) Java Runtime Environment (JRE) 1.5 
2) Jakarta-Tomcat 5.0, from the Apache Jakarta project. 
**The EFG Data Importer requires that Tomcat 5.0 is run with JDK 1.4 (Not the same as the JRE!). You must also have write privileges to the Tomcat 5.0 folder**
3) ImageMagick 6.1 or later
4) MySQL Server 5.0 or later (must be at port 3306)

*********

I) Check the version of Java Runtime Environment running on your machine

   1) Open a terminal window (In Mac OS 10.4, Finder --> Applications --> Utilities --> Terminal)
   2) Type: java -version
   3) Verify that you have a Java version of 1.5 or higher.
   4) If the version you have is lower than 1.5, please consult your Mac documentation for more information about how to install a new Java version.

II) Install Tomcat

The Tomcat webserver installer and helper installer applications are located in the mac/Tomcat Folder.
	
   1) Double click on Jakarta Tomcat 5.0.30.pkg to install the Tomcat web application server in /usr/local/tomcat

   2) Double click on Jakarta Tomcat 5.0.30StartupItem.pkg to add Tomcat to the startup items list. This will cause Tomcat to startup automatically whenever you reboot your machine.

   3) Double click TomcatMonitor.pkg to install the Tomcat launcher in the Applications folder. The launcher can be used to start, stop and restart Tomcat.
   
   4) To Start/Stop/Restart Tomcat, go to Finder -->  Applications --> TomcatMonitor

   5) Test that Tomcat is running:
   - Open a web browser
   - Visit http://localhost:8080
   - Verify that you get the Apache Tomcat/Jakarta Project front page
 
III) Install MySQL

The MySQL 5.0 database server and its helper applications are in the mac/mysql folder.

   1) Note: Some versions of Mac OS X 10.4 come with an older MySQL server. You will need to turn it off before you install a new one, and if you have any data in there you may want to back it up. Turn off that MySQL server by opening a terminal window and typing:

   /usr/local/mysql/bin/mysqladmin -u root shutdown   (Enter your password if you are prompted for one)
		     
   2) If your Mac is a Powerpc or Intel Core 2 then we have provided the default MySQL 5.0 packages for you. For other Macs please see MySQL's download page (http://dev.mysql.com/downloads/mysql/5.0.html). If you are not sure what processor your Mac uses, go to the apple icon in the top left corner of the screen and select "About this Mac."

   3) Double click on mysql-5.0.37-osx10.4-powerpc.dmg (Power PC) or mysql-5.0.37-osx10.4-i686.dmgfile (x86, Intel Core 2) in the mysql folder. In the window that pops up, there will be four files.

   4) Double click on mysql-5.0.37-osx10.4-powerpc.pkg (mysql-5.0.37-osx10.4-i686.dmg for x86/Intel Core 2 machines) to install MySQL at /usr/local/mysql

   5) Double click on mySQLStartupItem.pkg to add MySQL to the list of startup items list. This will cause MySQL to start automatically whenever you reboot your computer.

   6) Double click on MySQL.prefPane to install the mysql launcher application. The launcher is used to start and stop MySQL. It is installed in System Preferences -> Other

IV) MySQL Post Installation

   1) Change the default password - We strongly recommend you do this to make your MySQL installation secure.

   - Open a terminal window, type the following and press the Enter key:

    /usr/local/mysql/bin/mysql -u root

   - At the prompt ( mysql>) type:

     SET PASSWORD FOR root@localhost=PASSWORD('new_password');
     (Where 'new_password' is a password of your choosing)

   - Then type the following and press Enter:
     
     SET PASSWORD FOR 'root'@'host_name' = PASSWORD('new_password');
     (Where host_name is the name of your machine and 'new_password' is the password you entered above. If you are not sure what your computer's name is, you can find it in any Terminal window preceding the ">" prompt.)

   2) Delete the default user

   - At the prompt, type the following and press Enter:

     delete from mysql.user where not (user="root");

   - Then type the following and press Enter:
		
     flush privileges;
		
   - To exit MySQL, type the word "quit" at the prompt and press Enter.	
	

V) ImageMagick Installation

The EFG Data Importer uses ImageMagick to generate high-quality thumbnails.

   1) Double click on the imagemagick-6.1.7.pkg, located in the
      /mac/Image Magick folder, to install ImageMagick at /usr/local/bin

VI) Proceed with EFG Data Importer installation

Congratulations! You have now successfully installed all of the prerequisites to run EFG Data Importer.
Refer back to the main documentation to install the EFG Data Importer.




Additional Software Installation Instructions For Mac OS X 10.4
$Id$

You can ignore all of these instructions if you have a 
lot of experience in installing applications on Macs.
Just make sure that the applications needed by
EFG2 are installed on your machine.

I)Java Runtime 1.5 or later.

To check the java version on your machine. 
Open a command window and type:

java -version

EFG2 runs with a java version of 1.5 or higher.

See your docs on how to install a new Java version
if the version you have is lower that version 1.5.

II)Tomcat Installation

1.Unzip jakarta-tomcat-5.0.30.zip located into the current directory

2. Move the unzipped folder to /etc
   This can be done by opening a command window 
   to the current directory.
   Then at the command prompt enter:
   sudo cp -r jakarta-tomcat-5.0.30

4. copy the folder named 'Tomcat' in the current directory to
   /Library/StartupItems

   Open a command window to the current directory and type
   sudo cp -r Tomcat /Library/StartupItems
   
5. Start Tomcat as follows:
   open a command window and cd to /Library/StartupItems/Tomcat
   Then enter: Tomcat start
   To stop Tomcat
      cd to the same directory and enter:
      Tomcat stop
   Note that Tomcat will start automatically on reboot of your
   machine so you do not need to issue these commands on startup.
         
III)MySQL Installation:

1. Mac OS X 10.4 comes with an older  MySQL server 
you need to turn it off before you install a new one.
If you have any data in there you may want to back it up.
Turn off the MySQL server by opening a command prompt and typing:

mysqladmin -p -u root shutdown

Enter your password if you are prompted for it.

2. 
a)If your Mac is a Powerpc then

Double click on mysql-5.0.37-osx10.4-powerpc.dmg file located
in the current directory and read the 
ReadMe file inside the folder.
Follow all of the instructions carefully.
Make sure to install all the other packages
according to the instructions provided in the Readme file.
You can ignore all the stuff about adding aliases.


b)Otherwise, go www.mysql.com and find the MySQL 5.0 that
is appropriate for your Mac.



Image Magick Installation
1. EFG2 uses Image Magick to generate thumbnails.
2. Double click on imagemagick-6.1.7.pkg located in the
   current folder and follow the instructions.

3. Note where Image Magick was installed on your machine.




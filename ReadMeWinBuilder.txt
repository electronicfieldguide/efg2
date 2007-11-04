$Id$
$HeadURL$

http://nsis.sourceforge.net/Main_Page

Building efg2 distribution for windows.
=======================================
    A. Required SVN Modules
    =========================
    Check out the following from the svn repository
    and place them at the same level as the efg2 application.
    
    1. efg2_bookmaker
    2. efg2_platemaker
    3. efg2_skins
    4. efg2_website
    
    B. Software Requirements 
    =========================
    1. JDK 1.4
    2. Ant 1.5 or higher
    3. NSIS from http://nsis.sourceforge.net/Main_Page.
       Make sure to set the NSIS_HOME environment variable
       if you did not install it at  'C:\Program Files\NSIS'
       
    C. Required Applications 
      =======================
      The following software are required as part of the distribution.
      1. Create a folder called downloads at c:\.
      (You can create a folder anywhere but you will need to edit
        efg2_installer/winBuilder/headers/downloadHeaders.nsh by editing
        the following variables to point to each application:
        MYSQL_SOURCE, JDK_SOURCE, IMAGE_MAGICK_SOURCE, JRE_SOURCE,
         TOMCAT_SOURCE)
         
      2. Download the following executables and place them into the created directory.
            a. "MySQLSetup.exe" - MySQL 5 executable from 
                http://efg.cs.umb.edu/EFGsoftware/efg2Applications/mysql/MySQLSetup.exe
           
            b. "j2sdk-1_4_2_10-windows-i586-p.exe"  - Suns JDK 1.4 from
                http://efg.cs.umb.edu/EFGsoftware/efg2Applications/java/j2sdk-1_4_2_10-windows-i586-p.exe
           
            c. "jre-1_5_0_08-windows-i586-p.exe"  - Suns JRE 1.5 from
                http://efg.cs.umb.edu/EFGsoftware/efg2Applications/java/jre-1_5_0_08-windows-i586-p.exe
             
            d. "ImageMagick-6.3.2-8-Q16-windows-dll.exe" - Image Magick from
                http://efg.cs.umb.edu/EFGsoftware/efg2Applications/imagemagick/ImageMagick-6.3.2-8-Q16-windows-dll.exe        
    
     D. Building Windows Distribution
        =============================
           a. Make sure NSIS_HOME environment variable is set
           or that NSIS is installed at C:\Program Files\NSIS 
           
           b. Open a Command window to the current directory
              and execute ant as follows:
               ant -buildfile=buildNSIS.xml clean doall
               
              Note that the 'clean' target is optional.
              You could also run this from Eclipse. 
           
           c. EFG2Installer2.0.exe will be created in efg2_installer/winBuilder.
            Copy docs/efg2doc.html(efg2 manual),
            efg2_installer/winBuilder/EFG2Installer2.0.exe and 
            efg2_installer/winBuilder/releaseNotes.txt
            and to the repository where efg2 applications are 
            downloaded. Make links to the copied files.
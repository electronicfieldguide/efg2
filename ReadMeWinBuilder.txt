$Id$
$HeadURL$

Building efg2 distribution for windows.

Requirements 
     Software 
     1.The latest version NSIS plugin for Eclipse.
     (You can also download the latest Version of NSIS and use it on teh command line)
     2. Eclipse must be run with jdk 1.4
     
 2. Create a folder called downloads at the root of your c
 folder. (You can create it anywhere but you will need to edit
 efg2_installer/winBuilder/headers/downloadHeaders.nsh
 )
 
 Put the following executables in it:
 
"MySQLSetup.exe" - MySQL 5 executable
"j2sdk-1_4_2_10-windows-i586-p.exe" 
"ImageMagick-6.3.2-8-Q16-windows-dll.exe"
"jre-1_5_0_08-windows-i586-p.exe"

3. run buildNSIS.xml to create files and folders needed
by NSIS build script.

4. Navigate to efg2_installer/winBuilder
 and execute the following the order stated:
  a. About.nsi
  b. EFG2DataImporter.nsi
  c.  EFG2DataImporterInstaller.nsi
  
  OR
  set the NSIS_HOME environment variable to point to your NSIS home and run
  efg2_installer/winBuilder/compileNSIS.bat
  
5. Copy the EFG2Installer2.0.exe to where your clients can download them.
   Also copy efg2doc.html and efg2_installer/winBuilder/releaseNotes.txt
   and link to them from your downloads page.
 
 Changing the Version Number
 OPen efg2installer/efg2Headers.nsh
 and replace VERSION X.0 and 
 ProductVersion X.Y.Z.K
 
with the version numbers.

  
	
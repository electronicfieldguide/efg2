$Id$
See efg2/docs/DeveloperNotes.txt for notes about code structure.
This module is for building efg2 installers using the IZPack installer.

1. Right now it is set to build efg2.1.1.0.0 on windows.
   Note that it is not backward compatible with old efgs.
	
2. Building the efg2 installer requires JDK1.4 or higher,
   ANT 1.6 or higher and NSIS 2.1 or higher.
   It also requires that the following modules from SVN are at the same
   directory level with the the parent folder of this file. 
  (efg2_skins,efg2_website and efg2).
  The application will fail to build if these folders are not present.
  
  You can use the "checkoutall" and "dist" targets in build.xml to 
  check out all the needed modules from svn and then build an 
  efg2 installer.
  
  NOTE: using the 'checkoutall' option requires requires that you have an svn 
  command line in your classpath. You can download and install an svn command line at 
  http://subversion.tigris.org/ 


 NOTE: That you may have to edit build.properties and winBuilder/compileNSIS.bat
 to suit your environment.
 
3. 	The .xml files and the .properties files in this directory 
	are all needed to build this application. EDIT build.properties to
	suit your environment.
		
   	We use the IzPack installer to build the efg2 application for distribution.
   	 The following build scripts are used
   	  i) build.xml - The main build file. Recursively calls other
   	  files needed to build the distributable jar file.
   	  The name of the jar file is set in build.properties.
       Executing ant on this file with no args builds the installer which is a 
   	  jar file.
   	  
   	  If you chose to use the checkoutall target then you may have to edit build.svn.properties
   	  to point to the svn repository.
   	      	  
   	  ii) buildscripts/build_IzPack.xml  - 	Is the script that starts the building of the 
   	  	  installer. You may edit install.xml located in IzPackInstaller folder. 
   	  	  Right now it only builds efg2 for windows. More will be added later.

	  iii) buildscripts/build_server.xml - Is called by build.xml to build the efg2
	       web application.
	  
	  iv) buildscripts/build_client.xml - Is called by build.xml to build the efg2 
	  	  client application.     
	  
	  v) buildscripts/build_NSIS.xml(only for windows) - Is called to build an NSIS script that will update
	  	 be used during the installation to update efg1.0.0.X to efg1.1.0.0
	  	 on Windows.
	  	 Edit winBuilder/compileNSIS.bat to set your NSIS_HOME environment
	  	 variable.
	  	 
	  vi) 
	   			
		
		
	
	
	
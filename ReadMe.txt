$Id$

See docs/DeveloperNotes.txt for developer notes.

1. This module is for building and deploying efg2 1.1.X.X family of
 efg2's.
 
Requirements
============
	Application requires Tomcat 5.0.x, JDK 1.4 to build(because Tomcat 5.0.X uses JDK 1.4)
	 and JRE 1.5 (or later) to run it (the client app), 
	Image Magick 6.3.2, MySQL 5.0 or later and Ant 1.6.
	It also requires that the following modules from SVN are at the same
	level with the efg2 folder. (efg2_skins,efg2_bookmaker and efg2_website).
	The application will fail to build if these folders are not present.

Build Client and Web applications
================================

	1. The .xml files and the .properties files in this directory 
		are all needed to build this application.
	2.  Rename build.properties.sample to build.properties.(please do not
		check this file into svn).	
	
	3. To build the client application only (The importer app) -
	  	i) Edit the build.properties by setting efg2.mysql.port value
	  	to your MySQL port number(the default on most machines is 3306).
	  	Also edit the efg2.cacheserver.url value to point to the url to your efg2
	  	The default is http://localhost:8080/efg2.
	  	ii)Edit the efg.client.folder.name value if you wish to change
	  		the name of the folder that will be holding the importer
	  		application files.(The default is efg2Client).
	  	iii) edit the value of efg.web.user.password to the password
	  	that will be used by the 'efg' user.Templates cannot be configured until
	  	this is done. The default password is 'efg'.
	  	
	  
	  	Then run ant with the "buildclient" target.
	  	ant buildclient
	  	
	  	Windows users should cd into the created application directory
	  	and run login.bat, linux/unix/mac OS X users should run login.sh.
		efg2 on bdei could run efg2Login.sh
	  	
	4. To build and deploy the server application only(the feg2 web apps)	
		i) Set the value of efg2.mysql.port to your mysql port if it is not 3306 also
		    change the value of efg2.tomcat.home to the full path of your tomcat root.
		    set efg2.tomcat.webapps.home to the full path to your webapps folder.
		 ii) Edit efg2.xml if the webapps folder where efg2 will be deposited is not
		    	directly under the tomcat server.
		iii) edit the value of efg.web.user.password to the password
	  	that will be used by the 'efg' user.Templates cannot be configured until
	  	this is done. The defaults is 'efg' without the single quote.
	  	
		iv) If you have tomcat manager enabled then make sure to set
			 the values of tomcat.manager.username,tomcat.manager.password
			 and tomcat.manager.url properties appropriately.
			 
		v) Then run ant with the "deployserver" target.	 
			ant deployserver
			ant deployservertc if you have tomcat manager enabled
			
		vi) To deploy a war file run ant with the deploywar target.
			ant deploywar	
			
			ant deploywar if you have tomcat manager enabled		   			
	5. To build both web and client applications follow the instructions in
		steps 3 and 4 but run ant with the "doall" target
		ant doall 
		   
		
		
	
	
	








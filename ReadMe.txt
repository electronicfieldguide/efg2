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
	level with the efg2 folder. (efg2_skins,efg2_bookmaker
	efg2_platemaker and efg2_website).
	The application will fail to build if these folders are not present.

Build Client and Web applications
================================
    0. INSURE THAT TOMCAT6 SECURITY MANAGER IS OFF.
    In ubuntu: edit /etc/default/tomcat6 so that the TOMCAT_SECURITY section carries
       TOMCAT6_SECURITY=no
       

	1. The .xml files and the .properties files in this directory 
		are all needed to build this application.
	2.  Rename build.properties.sample to build.properties.(please do not
		check this file into svn).	
	
	3. First build the client application (The importer app) -
	  	i) Edit the build.properties by setting efg2.mysql.port value
	  	to your MySQL port number(the default on most machines is 3306).
	  	Also edit the efg2.cacheserver.url value to point to the url to your efg2
	  	The default is http://localhost:8080/efg2.
	  	ii)Edit the efg.client.folder.name value if you wish to change
	  		the name of the folder that will be holding the importer
	  		application files.(The default is efg2Client).
	
	  
	  	Then run ant with the "buildclient" target.
	  	sudo ant buildclient
	  	
	4. Next build and deploy the server application (the efg2 web apps)	
		i) Set the value of efg2.mysql.port to your mysql port
		if it is not 3306 

		ii)Change the value of efg2.tomcat.home to the full
		path of your tomcat root. 

		iii)Set efg2.tomcat.webapps.home to the full path to
		your webapps folder. This is normally the webapps
		folder under the tomcat home as specified in
		efg2.tomcat.home 

		iv) If the webapps folder where efg2 will be deposited
		is not directly under the tomcat home, then edit the
		file webcontext/efg2.xml. (webcontext/ is a sister
		directory to the one containing this ReadMe.txt). In
		that file, change the value of the docBase attribute
		to the full path of the efg2 webapp, as implied in
		build.properties. For example, if in build.properties
		you had efg2.tomcat.webapps.home=/share/webapps then
		the entry in efg2.xml must be /share/webapps/efg2.
		


		v) Edit the value of efg.web.user.password to the password
	  	that will be used by the 'efg' user. Templates cannot be configured until
	  	this is done. The defaults is 'efg' without the single quote.
	  	
		vi) If you have tomcat manager enabled then make sure
		to set the values of tomcat.manager.username,
		tomcat.manager.password and tomcat.manager.url
		properties appropriately.

		vi) Then run ant with the "deployserver" target.	 
			sudo ant deployserver
			sudo ant deployservertc if you have tomcat manager enabled
			
		vii) Recursively change the tomcat6 webapps/efg2 directory to owner and 
		group tomcat6 to make sure that the user as which tomcat executes can write on webapps/efg2.
		on ubuntu linux, tomcat6 normally runs as user tomcat6, group tomcat6, so this suffices;
		sudo chmod -R  g+wrx /var/lib/tomcat6/webapps/efg2
		sudo chgrp -R tomcat6 /var/lib/tomcat6/webapps/efg2
		
		you must restart tomcat after making the changes to permissions:
		sudo /etc/init.d/tomcat6 restart
			
        viii) In the client directory, locate workspace.configs.properties within the properties directory and edit this file.
        uncomment the efg.serverlocations.lists and efg.serverlocations.current properties to 
        point to the tomcat home directory
        efg.serverlocations.lists=/var/lib/tomcat6
        efg.serverlocations.current=/var/lib/tomcat6/
        
        //ix) is moot by RunSetup.java SVN ver 482. Ignore it
        ix) Log in to mysql via the client as root and run the following sql commands to fix an issue with how the
        client build writes the webapp configuration password into the database (the password value from the build.properties file is written
        to the wrong table:
        mysql> use efg;
        mysql> update efg_users set user_pass = "password" where user_name = "efg";
        mysql> update efg_roles set role_name = "efg" where user_name = "efg";
        
        
	  	
	5. Windows users should cd into the created application directory
	  	and run login.bat, linux/unix/mac OS X users should run login.sh.
		efg2 on bdei could run efg2Login.sh
	  	


	6. As a convenience, if you modify the build.properties and
		webcontext/efg2.xml as described above, you can redeploy
		both client and server with 
			sudo ant doall 
		   
		 
		
	
	
	








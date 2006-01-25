package project.efg.util;
/**
 *$Id$
 * Copyright (c) 2001  University of Massachusetts Boston
 *
 * Authors: Jacob K. Asiedu<kasiedu@cs.umb.edu>
 *
 */

// import java specific packages
import java.io.File;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.sql.*;
// import log4j packages
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * This servlet performs the task of setting up the log4j configuration.
 *@author <a HREF="mailto:kasiedu@cs.umb.edu">Jacob K. Asiedu</a>
 */

public class LoggerUtils{
    private static Logger log = null;
    
    public LoggerUtils(){
    
    }
    static{
	try{
	
	    String user_dir = System.getProperty("user.dir");
	    String props = user_dir + File.separator + "log4j.properties";
	    if(props == null || props.length() == 0 ||
	       !(new File(props)).isFile()){
		System.err.println(
				   "ERROR: Cannot read " + props + ". " + 
				   "Please check the path to the configuration file");
	    }
	    
	    // look up another init parameter that tells whether to watch this
	    // configuration file for changes.
	    String watch = null;
	    
	    // since we have not yet set up our log4j environment,
	    // we will use System.err for some basic information
	    
	    
	    // use the props file to load up configuration parameters for log4j
	    if(watch != null && watch.equalsIgnoreCase("true")){
		PropertyConfigurator.configureAndWatch(props);
	    }
	    else{
		PropertyConfigurator.configure(props);
	    }
	// once configured, we can start using the Logger now
	    log = Logger.getLogger(project.efg.util.LoggerUtils.class);
	    
	}catch(Exception ioex){
	    EFGUtils.log("Logger not initiliazed");
	    EFGUtils.log("If servlet will be initialized from servlet context!!");
	}
    }
    private static void init(){
	try{
	    log = Logger.getLogger(project.efg.util.LoggerUtils.class);
	    EFGUtils.log("Logger initiliazed for servlet in servlet context");
	}
	catch(Exception ee){
	    EFGUtils.log("Logger not initiliazed in servlet context");
	}
    }
    public synchronized static void logErrors(Exception ee){
	if(log == null){
	    init();
	}
	StackTraceElement[] ste = ee.getStackTrace(); 
	StringBuffer buff = new StringBuffer();
	buff.append( ee.getMessage());
	buff.append("\n");
	for(int ii=0; ii < ste.length; ii++){
	    buff.append(ste[ii].toString());
	    buff.append("\n");
	}
	log.error(buff.toString());
    }
   /**
     * Display SQL error messages within a SQLException.
     * @param ex the exception to display
     */
    public synchronized static void printSQLErrors(SQLException ex) {
	if(log == null){
	    init();
	}
	if(ex != null){
	    logErrors(ex);
	}
	while (ex != null) {
	    log.error("SQLState: " + ex.getSQLState());
	    log.error("Message:  " + ex.getMessage());
	    log.error("Vendor:   " + ex.getErrorCode());
	    ex = ex.getNextException();
	    log.error("XXXX");
	}
    }
}

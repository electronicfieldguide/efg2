/**
 *$Id$
 * ThumbnailGenerator.java
 * Copyright (c) 2005  University of Massachusetts Boston
 *
 * Created: Thu Aug 11 13:51:36 2005
 *
 * @author <a href="mailto:">Jacob Kwabena Asiedu</a>
 * @version 1.0
 */
package project.efg.util;

import java.util.Map;

import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

// cache on hard disk

public class ThumbNailGeneratorWrapper implements EFGImportConstants {

	protected static org.apache.log4j.Logger log;
	static {
		try {
			log = Logger.getLogger(ThumbNailGeneratorWrapper.class);
		} catch (Exception ee) {

		}
	}

	



	public ThumbNailGeneratorWrapper(JProgressBar progressBar) {
		
	

	}

	public boolean startGeneration(Map fileNames) {
	 return false;
	}



}

/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2007  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */
package project.efg.client.factory.gui;

import java.util.Comparator;

import javax.swing.TransferHandler;
import javax.swing.event.TreeExpansionListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface;



/**
 * @author jacob.asiedu
 *
 */
public class SpringGUIFactory {
	static Logger log;
	private static ApplicationContext   context;
	static {
		try {
			log = Logger.getLogger(SpringGUIFactory.class);
			doSpring();
		} catch (Exception ee) {
		}
	}
	public SpringGUIFactory(){
		
	}
	
	
	/**
	 * @return
	 */
	private static void doSpring() {
		
	
			try {
			context = 
				new ClassPathXmlApplicationContext("springconfig_gui.xml");
				
			}
			catch(Exception ee) {
				log.error(ee.getMessage());
				
			}
	
		
	}
	
	public static synchronized TransferHandler getTransferHandler(){

		try{
			return (TransferHandler)context.getBean("transferHandler");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	
	}
	public static synchronized TreeExpansionListener  getTreeExpansionListener(){
	
		try{
			return (TreeExpansionListener)context.getBean("tree_expansionlistener");
		}
		catch(Exception ee){
			ee.printStackTrace();
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	}
	public static project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface getFailureObject(){		
	
		try{
			return (EFGDatasourceObjectStateInterface)context.getBean("failure_stateobject");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	
	}
	/**
	 * 
	 * @return
	 */
	public static project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface getSuccessObject(){
	
		try{
			return (EFGDatasourceObjectStateInterface)context.getBean("success_stateObject");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	
	}
	/**
	 * 
	 * @return
	 */
	public static project.efg.client.interfaces.gui.EFGDatasourceObjectStateInterface getNeutralObject(){
	
		try{
			return (EFGDatasourceObjectStateInterface)context.getBean("neutral_stateObject");
		}
		catch(Exception ee){
			log.error(ee.getMessage());
			log.error("Returning default!!");
		}
		return null;
	}


	
	/**
	 * 
	 * @param springID
	 * @return
	 */
		public static Comparator getComparator(String springID) {
			try {		
				return (Comparator)context.getBean(springID);
			}
			catch(Exception ee) {
				
				log.error(ee.getMessage());
			}
			return (Comparator)context.getBean("default_comparator");
		}

}

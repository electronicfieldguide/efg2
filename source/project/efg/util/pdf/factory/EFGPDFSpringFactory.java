/**
 * $Id: EFGSpringFactory.java,v 1.1.1.1 2007/08/01 19:11:21 kasiedu Exp $
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

package project.efg.util.pdf.factory;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.efg.util.pdf.bookmaker.EFGPageEventHandler;
import project.efg.util.pdf.interfaces.EFG2PDFInterface;
import project.efg.util.pdf.interfaces.EFGRankObject;
import project.efg.util.pdf.platemaker.EFG2PDF;
import project.efg.util.pdf.utils.CharacterOrder;
import project.efg.util.pdf.utils.EFGLine;
import project.efg.util.pdf.utils.EFGRankObjectSortingCriteria;
import project.efg.util.pdf.utils.MetricsObject;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.events.IndexEvents;


public class EFGPDFSpringFactory {
	static Logger log;
	private static ApplicationContext appcontext;

	static {
		try {			
			log = Logger.getLogger(EFGPDFSpringFactory.class);
			appcontext = 
				new ClassPathXmlApplicationContext("pdfconfig.xml",EFGPDFSpringFactory.class);

		} catch (Exception ee) {
			//ee.printStackTrace();
		}
	}
	public static Comparator getRankObjectCriteria(){
		try {
			return (Comparator)appcontext.getBean("rankobjectcriteria");	
		}
		catch(Exception ee) {
			//log.error(ee.getMessage());
		}
		return new EFGRankObjectSortingCriteria();

	}
	public static EFG2PDFInterface newEFG2PDFInstance(){
		try {
			return (EFG2PDFInterface)appcontext.getBean("pdfCreator");	
		}
		catch(Exception ee) {
			//log.error(ee.getMessage());
		}
		return new EFG2PDF();
		//return null;
	}
	/**
	 * 
	 * @return
	 */
	public static Font getBold12Font(){
		
		try {
			return (Font)appcontext.getBean("bold12font");	
		}
		catch(Exception ee) {
			//log.error(ee.getMessage());
		}
		//return null;
		return new Font(Font.HELVETICA, 12, Font.BOLD);
	}
	/**
	 * 
	 * @return
	 */
	public static Font getNormal12Font(){
		try {
			return (Font)appcontext.getBean("normal12font");	
		}
		catch(Exception ee) {
			//log.error(ee.getMessage());
		}
		//return null;
		return new Font(Font.HELVETICA, 12, Font.NORMAL);
	}
	/**
	 * 
	 * @return
	 */
	public static EFGPageEventHandler getEFGPageEventHandlerInstance(){
		//try {
		//	return (EFGPageEventHandler)appcontext.getBean("pageeventhandler");	

		//}
		//catch(Exception ee) {
			Font bookheaderfont = new Font(Font.HELVETICA, 8, Font.NORMAL);
			MetricsObject headermetrics = new MetricsObject();
			headermetrics.setAlignment(Element.ALIGN_RIGHT);
			headermetrics.setDistanceFromMargins(54);
			headermetrics.setFont(bookheaderfont);
			
			MetricsObject footermetrics = new MetricsObject();
			footermetrics.setAlignment(Element.ALIGN_RIGHT);
			footermetrics.setDistanceFromMargins(18);
			footermetrics.setFont(bookheaderfont);
			//use spring
			EFGLine efgline = new EFGLine(0.5f,4);
	       

	       EFGPageEventHandler  pageEventHandler = new EFGPageEventHandler();
	       pageEventHandler.setHeaderMetrics(headermetrics); 
	       pageEventHandler.setFooterMetrics(footermetrics); 
	       pageEventHandler.setEFGLine(efgline);
	       return pageEventHandler;
		//}
		
	}
	
	public static MetricsObject getImageMetrics(){
		//try {
			//return (MetricsObject)appcontext.getBean("imagemetrics");	
		//}
		//catch(Exception ee) {
			//ee.printStackTrace();
		//}
		MetricsObject metrics = new MetricsObject();
		metrics.setAlignment(Element.ALIGN_TOP);
		metrics.setPadding(4.5f);
		metrics.setAlignment(4);
		return metrics;/**/
		
	}
	public static EFGRankObject getEFGRankInstance(){
		//try {
		//	return (EFGRankObject)appcontext.getBean("characterorder");	
		//}
		//catch(Exception ee) {
		//}
	   return new CharacterOrder();	
		//return null;
	}
	/**
	 * @return
	 */
	public static SortedSet getEFGPDFSortedTree() {
		return 	new TreeSet(getRankObjectCriteria());
	}
	/**
	 * @return
	 */
	public static Document getItextBookDocument() {
		try {
			//return (Document)appcontext.getBean("book1document");	
		}
		catch(Exception ee) {
			//ee.printStackTrace();
		}
		//return null;
		return new Document(PageSize.LETTER,108, 108, 72, 72);
	}
	/**
	 * 
	 * @return
	 */
	public static IndexEvents getIndexEvents() {
		return new IndexEvents();
	}
	/**
	 * @return
	 */
	public static Map getMapCollection() {
		return   new HashMap();
	}
	/**
	 * @return
	 */
	public static Paragraph getItextParagraph() {
		return  new Paragraph();
	}
	/**
	 * @return
	 */
	public static Font getBold14Font() {
		try {
			//return (Font)appcontext.getBean("bold14font");	
		}
		catch(Exception ee) {
			//log.error(ee.getMessage());
		}
		return new Font(Font.HELVETICA, 14, Font.BOLD);
		//return null;
	}
	/**
	 * @return
	 */
	public static Font getBold36Font() {
		
		return new Font(Font.HELVETICA, 36, Font.BOLD);
	}
	/**
	 * @return
	 */
	public static Font getNormal20Font() {
		
		return new Font(Font.HELVETICA, 20, Font.NORMAL);
	}
	/**
	 * @return
	 */
	public static Font getBold22Font() {
		return new Font(Font.HELVETICA, 22, Font.BOLD);
	}
	/**
	 * @return
	 */
	public static Font getNormal16Font() {
		return new Font(Font.HELVETICA, 16, Font.NORMAL);
	}
}
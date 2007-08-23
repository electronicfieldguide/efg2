/**
 * $Id: UnicodeToASCIIFilter.java,v 1.1.1.1 2007/08/01 19:11:28 kasiedu Exp $
 * $Name:  $
 *
 * Copyright (c) 2006  University of Massachusetts Boston
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
package project.efg.util.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import project.efg.util.interfaces.EFGImportConstants;


/**
 * @author kasiedu
 *
 */
public class UnicodeToASCIIFilter implements EFGImportConstants{
	
	  /**
     * Read characters from the reader, one at a time (using a BufferedReader
     * for efficiency).  Output printable ASCII characters unfiltered.  For
     * other characters, output the \U Unicode encoding.
     **/
	   public static void convertIllegalToUnicode(Reader r, Writer w)
	       throws IOException
	   {
		   
		      BufferedReader in = new BufferedReader(r);
		      PrintWriter out = new PrintWriter(new BufferedWriter(w));
		     
		      int c;
		      while((c = in.read()) != -1) {
		           if(!isValidXMLChar(c)) {
		        	String illegalCharacter = EFGProperties.getProperty(ILLEGALCHARACTER_TEXT);
		            out.print(illegalCharacter);
		        	/*String hex = Integer.toHexString(c);
		               switch (hex.length()) { 
		                 case 1:  out.print("\\u000" + hex); break;
		                 case 2:  out.print("\\u00" + hex); break;
		                 case 3:  out.print("\\u0" + hex); break;
		                 default: out.print("\\u" + hex); break;
		               }*/
		           }
		           else {
		        	   out.write(c);
		           }
		      }
		      out.flush();  // flush the output buffer we create

	   }

	   /**
	    * Section 2.2 of the XML spec describes which Unicode code points
	    * are valid in XML:
	    *
	    * <blockquote><code>#x9 | #xA | #xD | [#x20-#xD7FF] |
	    * [#xE000-#xFFFD] | [#x10000-#x10FFFF]</code></blockquote>
	    *
	    * Code points outside this set must be entity encoded to be
	    * represented in XML.
	    *
	    * @param c The character to inspect.
	    * @return Whether the specified character is valid in XML.
	    */
	   private static final boolean isValidXMLChar(int c)
	   {
		   
		     switch (c)
		      {
		       case 0x9:
		       case 0xa:  // line feed, '\n'
		       case 0xd:  // carriage return, '\r'
		           return true;

		       default:
		         break;
		      }
		   if(c >= 0x20  && c <= 0xd7ff ) {
			   
		   }
		   else if (c > 0xe000  && c <= 0xfffd) {
			   
		   }
		   else if( c > 0x10000  && c <= 0x10ffff) {
			  
		   }
		   else {
			   return false;
		   }
		   return true;
	  
	   }

	/**
	 * @param reader
	 * @return
	 * @throws IOException 
	 */
	public boolean filter(StringReader reader) throws IOException {
	      BufferedReader in = new BufferedReader(reader);
	     
	      boolean bool = false;
	      int c;
	      while((c = in.read()) != -1) {
	           if(!isValidXMLChar(c)) {
	        	   bool = true;
	        	   break;
	           }
	      }

		return bool;
	}

  

}

/**
 * $Id$
 * $Name$
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
package project.efg.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * @author kasiedu
 *
 */
public class UnicodeToASCIIFilter {

	/**
	 * 
	 */
	public UnicodeToASCIIFilter() {
	
	}
	  /**
     * Read characters from the reader, one at a time (using a BufferedReader
     * for efficiency).  Output printable ASCII characters unfiltered.  For
     * other characters, output the \U Unicode encoding.
     **/
    public void filter(Reader r, Writer w) throws IOException {
      BufferedReader in = new BufferedReader(r);
      PrintWriter out = new PrintWriter(new BufferedWriter(w));
      int c;
      while((c = in.read()) != -1) {
        // Just output ASCII characters
        if (((c >= ' ') && (c <= '~')) || (c=='\t') || (c=='\n') || (c=='\r'))
          out.write(c);
        // And encode the others
        else {
          String hex = Integer.toHexString(c);
          switch (hex.length()) { 
            case 1:  out.print("\\u000" + hex); break;
            case 2:  out.print("\\u00" + hex); break;
            case 3:  out.print("\\u0" + hex); break;
            default: out.print("\\u" + hex); break;
          }
        }
      }
      out.flush();  // flush the output buffer we create
    }
}

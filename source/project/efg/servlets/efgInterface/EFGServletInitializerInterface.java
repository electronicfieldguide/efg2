/*
 * $Id$
 *
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K. Asiedu, Kimmy Lin
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

package project.efg.servlets.efgInterface;

/**
 * Intialization parameters are set at Startup. This interface when implemented
 * sets the initialization parameters for a servlet.
 */

public interface EFGServletInitializerInterface 
{
    /**
     * Sets initialization parameters, perhaps read from a servlet Context
     */
    public void init();

    /**
     * This method is used to do clean up when the servlet is being shutdown. 
     * Could be used to return all connections to the pool etc.
     */
    public void contextDestroyed();
}

//$Log$
//Revision 1.1.2.1  2006/06/08 13:27:42  kasiedu
//New files
//
//Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
//no message
//
//Revision 1.4  2003/08/20 18:45:42  kimmylin
//no message
//

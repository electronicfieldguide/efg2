 /**
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
 *$Id$
File installImageMagick.nsh
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/

;Depends on InstallVersions.nsh

!define MAGICK_KEY "SOFTWARE\ImageMagick\6.3.0\${MAGICK_VERSION}"
!define MYSQL_KEY "SOFTWARE\MySQL AB\${MYSQL_VERSION}"
!define TOMCAT_KEY "SOFTWARE\Apache Software Foundation\Tomcat\${TOMCAT_VERSION}"
!define JRE_KEY "SOFTWARE\JavaSoft\Java Runtime Environment"
!define JDK_KEY "SOFTWARE\JavaSoft\Java Development Kit\1.4"

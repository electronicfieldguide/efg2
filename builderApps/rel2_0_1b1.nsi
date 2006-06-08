/**
*This file is part of the UMB Electronic Field Guide.
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
*$Name$
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
* This should be edited to produce all patches for EFGKeysx.xx
* Execute in verbose mode type rel... "-d"
*/
;The order in which header files are called must be followed
;version should be callaed only when the product name is set
;general headers

!include "efgHeaders.nsh"

;product headers Must be implemented by all clients
!include "rel1_0_2Headers.nsh"
;version headers
!include "versionHeaders.nsh"
;include MUI headers
!include "localMUI_HEADERS.nsh"

!include "commonFunctions.nsh"

AutoCloseWindow true
;ShowInstDetails nevershow

;----------------------------------------------------------
; Install Sections
;----------------------------------------------------------
/**
* Copies files to web application
*/



/**
* Copies file to web application etc.
* All clients must implement this function(template function)
*perhaps add a hook in the common functions method? 
*/
Function CopyFiles
 Push "About to copy files  to $CATALINA_HOME${EFG_KEYS_WEB_APPS}\html  $\r$\n"  ;text to write to file 
Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
Call WriteToFile

;add defaultstyle.css
 SetOutPath "$CATALINA_HOME${EFG_KEYS_WEB_APPS}\html"
 SetOverwrite on
 File "${EFG_PRODUCT_SOURCE_DIR}\${UPDATE_DIR}\defaultstyle.css"
; File "${PRODUCT_SOURCE_DIR}\${UPDATE_DIR}\readmeforDownloadedHtmlKeys.html"
 Push "$CATALINA_HOME${EFG_KEYS_WEB_APPS}\WEB-INF\xslt $\r$\n"  ;text to write to file 
Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
 Call WriteToFile
 
 ;add efgHtmlTemplates.xsl
 SetOutPath "$CATALINA_HOME${EFG_KEYS_WEB_APPS}\WEB-INF\xslt"
 SetOverwrite on
 File "${EFG_PRODUCT_SOURCE_DIR}\${UPDATE_DIR}\*.xsl"
 Push "$CATALINA_HOME${EFG_KEYS_WEB_APPS}\WEB-INF\classes\efg\efgPod $\r$\n"  ;text to write to file 
 Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
 Call WriteToFile

;add install.html
 SetOutPath "$CATALINA_HOME${EFG_KEYS_WEB_APPS}\WEB-INF\classes\efg\efgPod" 
 SetOverwrite on 
 File "${EFG_PRODUCT_SOURCE_DIR}\${UPDATE_DIR}\*.html" 
 Call CopyProperties
 
FunctionEnd






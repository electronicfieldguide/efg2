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
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/
;Depends on Installversion,Install URLs and CommonRegKeys.nsh
; Where the current directory is
!define MUI_ICON icons\EFGKeyConfig32x32.ico

!define CLASSPATH "xercesImpl.jar;xerces.jar;xalan.jar;xml-apis.jar;commons-io-1.2.jar;log4j-1.2.8.jar;oscache-2.3.jar;castor-0.9.5.2.jar;commons-logging.jar;commons-codec-1.3.jar;mysqldriver.jar;rdbImport.jar;spring.jar;rowset.jar;servlet-api.jar;ostermillerutils_1_05_00_for_java_1_4.jar;springConfig;."
!define ABOUT_CLASS "project/efg/drivers/AboutDriver"
!define DATA_IMPORTER_CLASS "project/efg/Imports/efgImpl/LoginDialog"



Function FindJRE
  Push $R0
  Push $R1

  ClearErrors
  ReadRegStr $R1 HKLM "${JRE_KEY}" "CurrentVersion"
  ReadRegStr $R0 HKLM "${JRE_KEY}\$R1" "JavaHome"
   IfErrors 0 JreFound
 ;look inside HKCU
  Pop $R0
  Pop $R1
  Push $R0
  Push $R1
  ClearErrors
  ReadRegStr $R1 HKCU "${JRE_KEY}" "CurrentVersion"
  ReadRegStr $R0 HKCU "${JRE_KEY}\$R1" "JavaHome"
  IfErrors error1 JreFound

 JreFound:
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreExec
  StrCpy $R0 "javaw.exe"
  ifErrors error1 JreExec

 JreExec:
  Pop $R1
  Exch $R0
 Goto end
 
 error1:
   MessageBox MB_OK " The java runtime cannot be found on your computer. Please install version 1.5 and try again"
   Abort    
 end:

FunctionEnd

 
 



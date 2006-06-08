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
;Java Launcher
;--------------
!include "WordFunc.nsh"

;include common defines
!addincludedir .
!include "efgHeaders.nsh"


!define CLASS "project.efg.Import.LoginDialog"

!define ARGS "args"



Name "DataImporter"
Caption "Java Launcher"
OutFile "Importer.exe"

SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow


Section ""
  Call ExecuteImporter
SectionEnd
Function GetJRE
  Push $R0
  Push $R1

  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  IfErrors 0 JreFound
 ;look inside HKCU
  Pop $R0
  Pop $R1
  Push $R0
  Push $R1
  ClearErrors
  ReadRegStr $R1 HKCU "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKCU "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
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
   MessageBox MB_OK " The java runtime cannot be found on your computer. Please install version 1.4 and try again"
   Abort    
 end:

FunctionEnd
/**
* Get the CATALINA_HOME variable
*/
Function GetCatalina
 Pop $R1
 Push $R1
 ReadEnvStr $R1 CATALINA_HOME
 StrCmp $R1 "" 0 end
 Pop $R1
 Push $R1
 ClearErrors
 ReadRegStr $R1 HKLM "${PRODUCT_UNINST_KEY}" "CATALINA_HOME"
 StrCmp $R1 "" 0 end
 Pop $R1
 Push $R1
 ClearErrors
 ReadRegStr $R1 HKCU "${PRODUCT_UNINST_KEY}" "CATALINA_HOME"

end:
FunctionEnd
Function ExecuteImporter

 Call GetJRE
 Pop $R0
 Call GetCATALINA
 Pop $R1
 StrCmp $R1 "" 0 foundwebapps 
 StrCpy $0 '"$R0" -classpath "${CLASSPATH}" ${CLASS}'
 Goto execute
 foundwebapps:
    StrCpy $0 '"$R0" -classpath "${CLASSPATH}" ${CLASS} "$R1"'
    Goto execute

execute: 
  SetOutPath $EXEDIR
  Exec $0

FunctionEnd



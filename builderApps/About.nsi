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
 *$Name$
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/
;Java Launcher
;--------------
!include "MUI.nsh"

!define BUILDER_APPS_Dir "builderApps"
!define EFG2_ABOUT_EXECUTABLE "${BUILDER_APPS_Dir}\AboutBox.exe"

; Where the current directory is
!define EFG2_PRODUCT_SOURCE_DIR "C:\cvscheckout\efg2"
!define MUI_ICON "${EFG_PRODUCT_SOURCE_DIR}\EFGKeyConfig32x32.ico"
!define CLASSPATH "serviceRegistry.jar;jdom.jar;castor.jar;log4j.jar;jakarta-oro-2.0.5.jar;keysInstaller.jar;pld.jar;."
!define CLASS "AboutBoxClient"


Name "AboutBox"
Caption "About Box Java Launcher"
OutFile "AboutBox.exe"

SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow

Section ""
  Call GetJRE
  Pop $R0
   StrCpy $0 '"$R0" -classpath "${CLASSPATH}" ${CLASS}'
  SetOutPath $EXEDIR
  Exec $0

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


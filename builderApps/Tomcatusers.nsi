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
*
* ReadMe Launcher
*--------------
*$Id$
*Written by Jacob K. Asiedu for EFG project
*/
!addincludedir .
!include efgHeaders.nsh



Name "TomcatUsers"
Caption "Launch Tomcat users installation"
OutFile "Tomcatusers.exe"

SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow


Section ""
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKLM "${PRODUCT_UNINST_KEY}" "EFGHOME"
  StrCmp $R0 "" 0 openReadMe
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${PRODUCT_UNINST_KEY}" "EFGHOME"
  StrCmp $R0 "" 0 openReadMe
  Goto end
 openReadMe: 
  SetOutPath $EXEDIR
 ExecShell "open" "manual.html"
 Goto end
end: 
  

SectionEnd



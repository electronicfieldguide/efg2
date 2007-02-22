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

!include "headers\GetJRE.nsh"

!define EFG2_ABOUT_EXECUTABLE "AboutBox.exe"

!define CLASS "project/efg/drivers/AboutDriver"


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




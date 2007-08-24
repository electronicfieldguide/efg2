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
 *$Id: About.nsi,v 1.5 2007/02/28 20:47:10 kasiedu Exp $
 *$Name:  $
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/
;Java Launcher
;--------------

!include "JavaClassHeader.nsh"

!define EFG2_ABOUT_EXECUTABLE "UpdateDB.exe"

!define UPDATE_CLASS "project/efg/client/drivers/gui/TemplateUpdatesDriver"
Name "Update Database for EFG2 1.1.0.0"
Caption "Update Database Launcher"
OutFile "UpdateDB.exe"

ShowInstDetails hide
AutoCloseWindow true

var CLASSPATH  


Section ""
    MessageBox MB_OK "About to update database. Please re-install the application if the database update fails"
    ReadRegStr $R1 HKLM "${REGKEY}\Java" "ClassPath"
    Pop $R1
  
    StrCpy $CLASSPATH $R1
  MessageBox MB_OK 'Class Path "$CLASSPATH" UPDATE_CLASS:  "${UPDATE_CLASS}"  '
    Call FindJRE
    Pop $R0 
    StrCpy $0 '"$R0" -classpath "$CLASSPATH" "${UPDATE_CLASS}"' 
      
    ;StrCpy $0 '"$R0" -classpath "$CLASSPATH" '
    SetOutPath $EXEDIR
    Exec $0
SectionEnd




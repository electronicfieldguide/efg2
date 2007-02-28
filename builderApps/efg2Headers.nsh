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
/**
*$Name$
*$Id$
* Header file for efg2 
*/
# Defines
!define VERSION 1.0
!define REGKEY "SOFTWARE\EFG\$(^Name)\${VERSION}"

!define COMPANY "Electronic Field Guide Project UMASS Boston"
!define URL http://efg.cs.umb.edu
!define EFG2_IMPORTER_LOGS_NAME efg2.log
!define EFG2_IMPORTER_EXECUTABLE EFG2DataImporter.exe
!define ABOUT_EXECUTABLE AboutBox.exe
!define EFG_SMALL_ICON EFGKeyConfig32x32.ico
!define EFG_BIG_ICON EFGKeyConfig48x48.ico
!define EFG2_IMPORTER_UNINSTALLER EFG2UnInstaller.exe
!define EFG2_LOCAL_DISTRIBUTION_PATH ..\dist
!define EFG2_RESOURCE_HOME resource
!define EFG2_LOCAL_RESOURCE_PATH "${EFG2_LOCAL_DISTRIBUTION_PATH}\${EFG2_RESOURCE_HOME}"

!define EFG2_HELP_FILE efg2doc.html
!define ProductVersion 1.0.0.0
!define BACK_SLASH  "\"
!define FORWARD_SLASH
# MUI defines
!define MUI_ICON icons\${EFG_SMALL_ICON}
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "EFG2DataImporter"

!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\${EFG2_HELP_FILE}
!define MUI_UNICON icons\${EFG_SMALL_ICON}
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
Name "EFG2DataImporter"

Function GetMySQLMessage
      MessageBox MB_OK "If you are in the middle of a MYSQL installation, $\r$\n\ 
        then make sure to complete it before you install any other application."
FunctionEnd 
























  
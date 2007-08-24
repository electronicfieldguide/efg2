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
 *$Id: efg2Headers.nsh,v 1.8 2007/03/08 19:09:04 kasiedu Exp $
 *$Name:  $
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/
/**
*$Name:  $
*$Id: efg2Headers.nsh,v 1.8 2007/03/08 19:09:04 kasiedu Exp $
* Header file for efg2 
*/
# Defines
!define VERSION 1.0
!define REGKEY "SOFTWARE\EFG\$(^Name)\${VERSION}"

!define EFG2_IMPORTER_EXECUTABLE EFG2DataImporter.exe
!define ABOUT_EXECUTABLE AboutBox.exe
!define EFG_SMALL_ICON EFG2_32x32.ico
!define EFG_BIG_ICON EFG2_48x48.ico


!define EFG2_RESOURCE_HOME resource
!define EFG2_LOCAL_RESOURCE_PATH "${EFG2_RESOURCE_HOME}"
!define EFG2_HELP_FILE efg2doc.html
!define ProductVersion 1.1.0.0
!define BACK_SLASH  "\"
!define FORWARD_SLASH
# MUI defines
!define MUI_ICON icons\${EFG_SMALL_ICON}
;!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "EFG2DataImporter"

!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\${EFG2_HELP_FILE}

!define MUI_UNICON icons\${EFG_SMALL_ICON}
;!define MUI_UNFINISHPAGE_NOAUTOCLOSE
Name "EFG2DataImporter Update 1.1.0.0"
!define INSTALLATION_LOGS "EFG2InstallLog.txt"
























  
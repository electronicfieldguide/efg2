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

!addincludedir "..\headers"
!AddIncludeDir "..\"
!include "efg2Headers.nsh" 

!include "InstallVersions.nsh"
!include "CommonRegKeys.nsh"
!include "InstallJDK.nsh"
Name "DeployWebapps"
Caption "DeployWebapps"

  
OutFile "DeployWebapps.exe"

Function deploywebapps
   ReadRegStr $2 HKLM "${TOMCAT_KEY}" "InstallPath"
   StrLen $0 "$2"
   IntCmp $0 0 NoService NoService 0
    ClearErrors
    IfFileExists "$2\bin\tomcat5.exe" 0 NoService
    ClearErrors
    ExecWait 'cmd /C net stop "Apache Tomcat"'   
    IfErrors  tcnotrunning 
        ;Sleep 500
        Call CopyWebApps
        sleep 200
        ExecWait 'cmd /C net start "Apache Tomcat"'
        Sleep 30000
        Goto End
        
    tcnotrunning:
        Call CopyWebApps
        sleep 200
        ExecWait 'cmd /C net start "Apache Tomcat"'
        Sleep 30000
        ExecWait 'cmd /C net stop "Apache Tomcat"'
        Sleep 200
        Goto End    

 NoService:
        MessageBox MB_OK "Tomcat 5 must be installed as a service"
        Quit

  End:
   ClearErrors
FunctionEnd

;Copy to webapps
Function CopyWebApps
    ReadRegStr $3 HKLM "${TOMCAT_KEY}" "InstallPath"
    
    ;Copy war file to webapps
    SetOutPath "$3\webapps"
    SetOverwrite try
    File ..\${EFG2_LOCAL_RESOURCE_PATH}\efg2.war
 
  
 FunctionEnd

Section ""
 Call deployWebapps
SectionEnd


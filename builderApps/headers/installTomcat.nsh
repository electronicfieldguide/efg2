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
;Call DetectTOMCAT

!define TOMCAT_VERSION "5.0"
!define TOMCAT_URL "http://www.mirrorgeek.com/apache.org/tomcat/tomcat-5/v5.0.28/bin/jakarta-tomcat-5.0.28.exe"

Function GetTOMCAT
        MessageBox MB_OK "$(^Name) uses Tomcat 5.0, it will now \
                         be downloaded and installed.\
                         An internet connection is required."
        StrCpy $2 "$TEMP\TomcatExecutable.exe"
        nsisdl::download /TIMEOUT=30000 ${TOMCAT_URL} $2
        Pop $R0 ;Get the return value
                StrCmp $R0 "success" +3
                MessageBox MB_OK "Download failed: $R0"
                Quit
        ExecWait $2
        Delete $2
FunctionEnd


;
; Deploy webapps
;
Function deploywebapps
   ReadRegStr $2 HKLM "SOFTWARE\Apache Software Foundation\Tomcat\${TOMCAT_VERSION}" "InstallPath"
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
        Sleep 500
        Goto End
        
    tcnotrunning:
        Call CopyWebApps
        sleep 200
        ExecWait 'cmd /C net start "Apache Tomcat"'
        Sleep 500
        ExecWait 'cmd /C net stop "Apache Tomcat"'
        Sleep 500
        Goto End    

 NoService:
        MessageBox MB_OK "Tomcat 5 must be installed as a service"
        Quit

  End:
   ClearErrors
FunctionEnd
Function CopyWebApps
      ReadRegStr $3 HKLM "SOFTWARE\Apache Software Foundation\Tomcat\${TOMCAT_VERSION}" "InstallPath"
    
    SetOutPath "$3\conf\Catalina\localhost" 
    SetOverwrite try
    File ..\dist\resource\efg2.xml
      
    ;copy mysql driver
    SetOutPath "$3\common\lib" 
    SetOverwrite try
    File ..\dist\resource\mysqldriver.jar
    
    ;Copy war file to webapps
    SetOutPath "$3\webapps"
    SetOverwrite try
    File ..\dist\resource\efg2.war
 
  
 FunctionEnd



Function DetectTOMCAT
  ReadRegStr $2 HKLM "SOFTWARE\Apache Software Foundation\Tomcat\${TOMCAT_VERSION}" "InstallPath"
             
  StrLen $0 "$2"
  IntCmp $0 0 tomcat tomcat done
  
  tomcat:
   Call GetTOMCAT
  done:
FunctionEnd
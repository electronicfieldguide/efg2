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
;Depends on InstallURLsHeader and CommonRegKeys.nsh

Var current14home

Var currentJREVersion
Var currentJavaHome
Var currentRuntime

;Var current14JREVersion
Var current14JavaHome
Var current14Runtime

Function addTomcatToInstalls
    !ifdef FullInstall
        SetOutPath $INSTDIR
        File ${TOMCAT_SOURCE} 
    !endif
FunctionEnd
Function GetTOMCAT
  clearErrors
  
  
  
  !ifdef FullInstall  
    MessageBox MB_OK "$(^Name) uses Tomcat 5.0, it will now be installed." 
      StrCmp $isMySQLInstall "true" printMySQLMessage1 continue1   
  printMySQLMessage1:
     Call  GetMYSQLMessage   
  continue1:      
    Call SetJDKForTomcat   
    StrCpy $2 "$INSTDIR\${tomcat_exec}"  
    ExecWait $2
    Delete $2  
    Call ResetJDKForTomcat
     StrCpy $isMySQLInstall "false"
     StrCpy $isTomcatInstalled "true"
  !else
    MessageBox MB_OK "$(^Name) uses Tomcat 5.0, it will now \
                         be downloaded and installed.\
                         An internet connection is required."
   StrCmp $isMySQLInstall "true" printMySQLMessage2 continue2   
  printMySQLMessage2:
     Call  GetMYSQLMessage   
  continue2:      
                         
     Call SetJDKForTomcat
    StrCpy $2 "$TEMP\TomcatExecutable.exe"
    nsisdl::download /TIMEOUT=30000 ${TOMCAT_URL} $2
    Pop $R0 ;Get the return value
    StrCmp $R0 "success" +3
    MessageBox MB_OK "Download failed: $R0"
    Quit
    ExecWait $2
  
    Delete $2  
   Call ResetJDKForTomcat
     StrCpy $isMySQLInstall "false"
      StrCpy $isTomcatInstalled "true"
   !endif
    
FunctionEnd
    ; Set the JDK compatible for the EFG Tonmcat version
Function SetJDKForTomcat

    ClearErrors 
   
     ReadRegStr $2 HKLM "${JDK_KEY}" "JavaHome"
     StrCpy $current14home "$2" 
     
    ReadRegStr $2 HKLM "${JRE_KEY}" "CurrentVersion"
    StrCpy $currentJREVersion "$2"  
    ;replace it with the efg jdk version
    WriteRegStr HKLM "${JRE_KEY}" CurrentVersion "${JDK_VERSION}"
    
    ;read the java home of the jer
    ReadRegStr $2 HKLM "${JRE_KEY}\$currentJREVersion" "JavaHome"
    StrCpy $currentJavaHome "$2" 
    
     ReadRegStr $2 HKLM "${JRE_KEY}\${JDK_VERSION}" "JavaHome"
     StrCpy $current14JavaHome "$2" 
    ;write efg2 java path
    WriteRegStr HKLM "${JRE_KEY}" JavaHome "$current14home"
    WriteRegStr HKLM "${JRE_KEY}\${JDK_VERSION}" JavaHome "$current14Home"
    
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "RuntimeLib"
    StrCpy $currentRuntime "$2" 
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\${JDK_VERSION}" "RuntimeLib"
     StrCpy $current14Runtime "$2" 
     
       StrCpy $2 "$current14home\bin\hotspot\jvm.dll"
      IfFileExists "$2" FoundJvmDll
     StrCpy $2 "$current14home\bin\server\jvm.dll"
     IfFileExists "$2" FoundJvmDll
      StrCpy $2 "$current14home\bin\client\jvm.dll"  
      IfFileExists "$2" FoundJvmDll
     StrCpy $2 "$current14home\bin\classic\jvm.dll"
     
    IfFileExists "$2" FoundJvmDll
   FoundJvmDll:
    WriteRegStr HKLM "${JRE_KEY}" RuntimeLib "$2"
   WriteRegStr HKLM "${JRE_KEY}\${JDK_VERSION}" RuntimeLib "$2"
   
    
FunctionEnd
; Reset the Tomcat installer to point to the original one
Function ResetJDKForTomcat
    ClearErrors
    Strlen $0 "$currentJREVersion"
    IntCmp $0 0  done done writeReg
    pop $0
 
    writeReg:
    WriteRegStr HKLM "${JRE_KEY}" CurrentVersion "$currentJREVersion" 
    WriteRegStr HKLM "${JRE_KEY}" RuntimeLib "$currentRuntime"
    WriteRegStr HKLM "${JRE_KEY}\${JDK_VERSION}" RuntimeLib "$current14Runtime"
    WriteRegStr HKLM "${JRE_KEY}" JavaHome "$currentJavaHome"
    WriteRegStr HKLM "${JRE_KEY}\${JDK_VERSION}" JavaHome "$current14JavaHome"
    done:
FunctionEnd
Function checkTomcatInstalled
    StrCmp  $isTomcatInstalled "true" 0 done
   ReadRegStr $2 HKLM "${TOMCAT_KEY}" "InstallPath"             
    StrLen $0 "$2"   
    IntCmp $0 0 done  done writereg
 
 ;add to components to uninstall
     writereg:
        ReadRegStr $1 HKLM "${TOMCAT_UNINSTALLER_KEY}" "UninstallString"  
        WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" tomcat_uninstaller "$1"
 done:
    
FunctionEnd
Function DetectTOMCAT
  ReadRegStr $2 HKLM "${TOMCAT_KEY}" "InstallPath"
             
  StrLen $0 "$2"
  IntCmp $0 0 tomcat tomcat done
  
  tomcat:
   Call GetTOMCAT
  done:
FunctionEnd
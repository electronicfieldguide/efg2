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
Function addJDKToInstalls
    !ifdef FullInstall
        SetOutPath $INSTDIR
        File ${JDK_SOURCE} 
    !endif
FunctionEnd
Function checkJDKInstalled
    StrCmp  $isJDKInstalled "true" 0 done
    ReadRegStr $2 HKLM "${JDK_KEY}" "JavaHome"        
    StrLen $0 "$2"
    ${If} $0 <= 0
        GoTo done
    ${Else}
        GoTo writereg
    ${EndIf}
    
    ;IntCmp $0 0 done  done writereg 
 
 ;add to components to uninstall
     writereg:
        ReadRegStr $1 HKLM "${JDK_UNINSTALLER_KEY}" "UninstallString"  
        WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" jdk_uninstaller "$1"
 
 
 done:
    
FunctionEnd
;Depends on InstallURLsHeader,CommonRegKey
Function GetJDK
      
       !ifdef FullInstall           
            MessageBox MB_OK "$(^Name) uses J2SDK 1.4, it will now \
                            installed."
            StrCmp $isMySQLInstall "true" printMySQLMessage1 continue1     
            printMySQLMessage1:
                Call  GetMYSQLMessage    
            continue1:    
            StrCpy $2 "$INSTDIR\${jdk_exec}"
            ExecWait $2
            Delete $2
            StrCpy $isJDKInstalled "true"
            StrCpy $isMySQLInstall "false"
        !else
             MessageBox MB_OK "$(^Name) uses J2SDK 1.4, it will now \
                             be downloaded and installed.\
                             An internet connection is required."
             StrCmp $isMySQLInstall "true" printMySQLMessage2 continue2     
            printMySQLMessage2:
                Call  GetMYSQLMessage    
            continue2:       
            StrCpy $2 "$TEMP\Java Development Kit Environment.exe"
            nsisdl::download /TIMEOUT=30000 ${JDK_URL} $2
            Pop $R0 ;Get the return value
             StrCmp $R0 "success" execT
             DetailPrint 'download failed from "${JDK_URL}": $R0'
             MessageBox MB_OK "Download failed: $R0"
            Quit
            execT:           
            ExecWait $2
            Delete $2  
            StrCpy $isJDKInstalled "true" 
            StrCpy $isMySQLInstall "false"         
       !endif  
        
       
FunctionEnd
 
 
Function DetectJDK
 ;IntCmp $0 5 is5 lessthan5 morethan5
  ReadRegStr $2 HKLM "${JDK_KEY}" "JavaHome"        
  StrLen $0 "$2"
  
  ${If} $0 <= 0
    GoTo jdk
  ${Else}
    GoTo done
  ${EndIf}
  
 ;IntCmp $0 0 jdk jdk done
  
   jdk:
       Call GetJDK
  done:
FunctionEnd
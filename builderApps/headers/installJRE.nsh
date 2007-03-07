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
;Depends on InstallURLsHeader,CommonRegKey
;

Function addJREToInstalls
    !ifdef FullInstall
        SetOutPath $INSTDIR
        File ${JRE_SOURCE} 
    !endif
FunctionEnd

Function checkJREInstalled
    ${LogText} 'Checking if JRE${JRE_VERSION} was installed by EFG2Installer'
    StrCmp  $isJREInstalled "true" writereg done
   
    ;add to components to uninstall
     writereg:
        ${LogText} 'JRE ${JRE_VERSION} was installed by EFG2Installer'
        ${LogText} 'Writing JRE ${JRE_VERSION} path in registry'    
        ReadRegStr $1 HKLM "${JRE_UNINSTALLER_KEY}" "UninstallString"  
        WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" jre_uninstaller "$1"
 
 
 done:
    
FunctionEnd
Function GetJRE

    
       !ifdef FullInstall 
        ${LogText}   "$(^Name) uses Java Runtime ${JRE_VERSION}, it will now be installed."       
            MessageBox MB_OK "$(^Name) uses Java Runtime ${JRE_VERSION}, it will now be installed."
              StrCmp $isMySQLInstall "true" printMySQLMessage1 continue1
               
              printMySQLMessage1:
                 Call  GetMYSQLMessage      
              continue1:
            ${LogText} "About to install JRE${JRE_VERSION}"   
            StrCpy $2 "$INSTDIR\${jre_exec}"
            
            ExecWait $2
            StrCpy $isJREInstalled "true"
            StrCpy $isMySQLInstall "false"
             ${LogText} "JRE${JRE_VERSION} Installed"
        !else
            ${LogText} "$(^Name) uses Java Runtime ${JRE_VERSION}, it will now \
                             be downloaded and installed.\
                             An internet connection is required."
            MessageBox MB_OK "$(^Name) uses Java Runtime ${JRE_VERSION}, it will now \
                             be downloaded and installed.\
                             An internet connection is required."
              
              StrCmp $isMySQLInstall "true" printMySQLMessage2 continue2               
              printMySQLMessage2:
                 Call  GetMYSQLMessage      
              continue2:
              ${LogText} "About to install JRE${JRE_VERSION}"         
              StrCpy $2 "$TEMP\Java Runtime Environment.exe"
            nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
            Pop $R0 ;Get the return value
            StrCmp $R0 "success" execT
             DetailPrint 'download failed from "${JRE_URL}": $R0'
             MessageBox MB_OK "Download failed: $R0"
             ${LogText} "download failed from '${JRE_URL}': $R0"
            ${LogText} 'Quitting installation'

            Quit
            execT:
            
            ExecWait $2          
            Delete $2
            StrCpy $isJREInstalled "true"
            StrCpy $isMySQLInstall "false"
            ${LogText} "JRE${JRE_VERSION} Installed"       
        !endif
FunctionEnd
 
 
Function DetectJRE
 ${LogText} 'Checking if JRE${JRE_VERSION} is installed'
  ReadRegStr $2 HKLM "${JRE_KEY}" "CurrentVersion"
 
  StrLen $0 "$2"
  ${If} $0 <= 0
   ${LogText} 'JRE${JRE_VERSION} does not exists.EFG2Installer will install it.' 
    GoTo jre
  ${Else}
    GoTo versioncomp
  ${EndIf}
 
 versioncomp:
     ${LogText} "EFG2 requires at least version: '${JRE_VERSION}'"
     ${LogText} "Version on machine is:'$2' "
    ${VersionCompare} "${JRE_VERSION}" "$2" $1
  
  ${If} $1 = 1 
     ${LogText} "JRE version on machine is not adequate to run EFG2."
       ${LogText} "Installer will install '${JRE_VERSION}'"
    GoTo jre
  ${Else}
  ${LogText} "JRE version on machine is adequate"
    GoTo done
  ${EndIf}    
    jre:
         ${LogText} "Function call GetJRE"
        Call GetJRE
  done:
   
FunctionEnd
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


Function addMySQLToInstalls
    !ifdef FullInstall
        SetOutPath $INSTDIR
        File ${MYSQL_SOURCE} 
    !endif
FunctionEnd
Function GetMYSQL
    StrCpy $isMySQLInstall "true"
    !ifdef FullInstall
         MessageBox MB_OK "$(^Name) uses ${MYSQL_VERSION}, it will now \
                         be installed."
 
        StrCpy $2 "$INSTDIR\${mysqlexec}"
        DetailPrint "MySQL installation is still in progress..Please wait.."
        ExecWait $2       
        Call GetMySQLMessage  
        Sleep 30000
        StrCpy $isMySQLInstalled "true"   
    !else
         MessageBox MB_OK "$(^Name) uses ${MYSQL_VERSION}, it will now \
                         be downloaded and installed.\
                         An internet connection is required."
 
        StrCpy $2 "$TEMP\MYSQLExecutable.exe"
        nsisdl::download /TIMEOUT=30000 ${MYSQL_URL} $2
        Pop $R0 ;Get the return value
                           
              StrCmp $R0 "success" execT
             DetailPrint 'download failed from "${MYSQL_URL}": $R0'
             MessageBox MB_OK "Download failed: $R0"
            Quit
            execT:                   
                    ExecWait $2                   
                    Delete $2
                     StrCpy $isMySQLInstalled "true"
    !endif
FunctionEnd
Function checkMySQLInstalled
    ${LogText} 'Checking if ${MYSQL_VERSION} was installed by EFG2Installer'
    StrCmp  $isMySQLInstalled "true" writereg done
 
 ;add to components to uninstall
     writereg:
        ${LogText} '${MYSQL_VERSION} was installed by EFG2Installer'
        ${LogText} 'Writing ${MYSQL_VERSION} path in registry'    
     
        ReadRegStr $1 HKLM "${MYSQL_UNINSTALLER_KEY}" "UninstallString"  
        WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" mysql_uninstaller "$1"
 
 
 done:
    
FunctionEnd
 
Function DetectMYSQL
    ${LogText} 'Checking for existence of ${MYSQL_VERSION} on machine' 
  ReadRegStr $2 HKLM "${MYSQL_KEY}" "Version"
             
   StrLen $0 "$2"
   ${If} $0 <= 0
   ${LogText} '${MYSQL_VERSION} does not exists on machine and will be installed' 
    GoTo mysql
  ${Else}
     ${LogText} '${MYSQL_VERSION} already exists on machine' 
    GoTo done
  ${EndIf}
  ;IntCmp $0 0 mysql mysql done
  
  mysql: 
   ${LogText} "Function call GetMYSQL"
    Call GetMYSQL
 
  done:
FunctionEnd
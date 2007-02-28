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
    StrCmp  $isJREInstalled "true" 0 done
    ReadRegStr $2 HKLM "${JRE_KEY}" "CurrentVersion"        
    StrLen $0 "$2"
    IntCmp $0 0 done  done writereg
 
 ;add to components to uninstall
     writereg:
        ReadRegStr $1 HKLM "${JRE_UNINSTALLER_KEY}" "UninstallString"  
        WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" jre_uninstaller "$1"
 
 
 done:
    
FunctionEnd
Function GetJRE

    
       !ifdef FullInstall           
            MessageBox MB_OK "$(^Name) uses Java Runtime 1.5, it will now be installed."
              StrCmp $isMySQLInstall "true" printMySQLMessage1 continue1
               
              printMySQLMessage1:
                 Call  GetMYSQLMessage      
              continue1:

            StrCpy $2 "$INSTDIR\${jre_exec}"
            
            ExecWait $2
          
            Delete $2
            StrCpy $isJREInstalled "true"
            StrCpy $isMySQLInstall "false"
        !else
            MessageBox MB_OK "$(^Name) uses Java Runtime 1.5, it will now \
                             be downloaded and installed.\
                             An internet connection is required."
              
              StrCmp $isMySQLInstall "true" printMySQLMessage2 continue2               
              printMySQLMessage2:
                 Call  GetMYSQLMessage      
              continue2:
           StrCpy $2 "$TEMP\Java Runtime Environment.exe"
            nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
            Pop $R0 ;Get the return value
            StrCmp $R0 "success" execT
             DetailPrint 'download failed from "${JRE_URL}": $R0'
             MessageBox MB_OK "Download failed: $R0"
            Quit
            execT:
            
            ExecWait $2          
            Delete $2
            StrCpy $isJREInstalled "true"
            StrCpy $isMySQLInstall "false"
        !endif
FunctionEnd
 
 
Function DetectJRE
  ReadRegStr $2 HKLM "${JRE_KEY}" "CurrentVersion"
 
  StrLen $0 "$2"
  IntCmp $0 0 jre jre versioncomp
    ; "[Version1]"      First version
  ;"[Version2]"       Second version
    ;$var                ; Result:
    ;    $var=0  Versions are equal
    ;    $var=1  Version1 is newer
    ;    $var=2  Version2 is newer
  
 ;val1 val2 jump_if_equal [jump_if_val1_less] [jump_if_val1_more]
 
 
 versioncomp:
    ${VersionCompare} "${JRE_VERSION}" "$2" $1
    IntCmp $1 1  jre done done  
    
    jre:
        Call GetJRE
        Goto done  
  done:
   
FunctionEnd
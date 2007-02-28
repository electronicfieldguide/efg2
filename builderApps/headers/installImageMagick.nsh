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
File installImageMagick.nsh
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/
;Depends on InstallURLsHeader and CommonRegKeys.nsh

Function addMagickToInstalls
    !ifdef FullInstall
        SetOutPath $INSTDIR
        File ${IMAGE_MAGICK_SOURCE} 
    !endif
FunctionEnd
Function GetMAGICK

      !ifdef FullInstall    
        MessageBox MB_OK "$(^Name) uses Image Magic ImageMagick-6.3.2-8-Q16, it will now \
                            installed." 
            StrCmp $isMySQLInstall "true" printMySQLMessage1 continue1     
            printMySQLMessage1:
                Call  GetMYSQLMessage    
            continue1:                                
            StrCpy $2 "$INSTDIR\${magick_exec}"            
            ExecWait $2
            Delete $2 
            StrCpy $isMagickInstalled "true"     
            StrCpy $isMySQLInstall "false"
      !else
         MessageBox MB_OK "$(^Name) uses Image Magic ImageMagick-6.3.2-8-Q16, it will now \
                         be downloaded and installed.\
                         An internet connection is required."
          StrCmp $isMySQLInstall "true" printMySQLMessage2 continue2     
            printMySQLMessage2:
                Call  GetMYSQLMessage    
            continue2:       
    
        StrCpy $2 "$TEMP\ImageMagickExecutable.exe"
        nsisdl::download /TIMEOUT=30000 ${MAGICK_URL} $2
        Pop $R0 ;Get the return value
         StrCmp $R0 "success" execT
         DetailPrint 'download failed from "${MAGICK_URL}": $R0'
         MessageBox MB_OK "Download failed: $R0"
           Quit
        execT:
            ExecWait $2            
            Delete $2
            StrCpy $isMagickInstalled "true"
            StrCpy $isMySQLInstall "false"
      !endif
FunctionEnd
Function checkMagickInstalled
    StrCmp  $isMagickInstalled "true" 0 done
    ReadRegStr $2 HKLM "${MAGICK_KEY}" "Version"              
    StrLen $0 "$2"   
    IntCmp $0 0 done done writereg
 
 ;add to components to uninstall
     writereg:
        ReadRegStr $1 HKLM "${MAGICK_UNINSTALLER_KEY}" "UninstallString"  
        WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" magick_uninstaller "$1"
 
 
 done:
    
FunctionEnd 
 
Function DetectMAGICK
  ReadRegStr $2 HKLM "${MAGICK_KEY}" "Version"              
  StrLen $0 "$2"
  IntCmp $0 0 versioncomp versioncomp done
  
   versioncomp:
    ${VersionCompare} "${MAGICK_VERSION}" "$2" $1
    IntCmp $1 1  magick done done  
 
  magick:
     Call GetMAGICK          
 
  done:
FunctionEnd
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
!define jdk_exec "j2sdk-1_4_2_08-windows-i586-p.exe"

!define JDK_SOURCE "C:\downloads\j2sdk-1_4_2_08-windows-i586-p.exe"

Function addJDKToInstalls
    !ifdef FullInstall
        SetOutPath $INSTDIR
        File ${JDK_SOURCE} 
    !endif
FunctionEnd
;Depends on InstallURLsHeader,CommonRegKey
Function GetJDK

       !ifdef FullInstall           
            MessageBox MB_OK "$(^Name) uses J2SDK 1.4, it will now \
                            installed."
     
            StrCpy $2 "$INSTDIR\${jdk_exec}"
            ExecWait $2
            Delete $2
        !else
             MessageBox MB_OK "$(^Name) uses J2SDK 1.4, it will now \
                             be downloaded and installed.\
                             An internet connection is required."
     
            StrCpy $2 "$TEMP\Java Development Kit Environment.exe"
            nsisdl::download /TIMEOUT=30000 ${JDK_URL} $2
            Pop $R0 ;Get the return value
             StrCmp $R0 "success" +3
            MessageBox MB_OK "Download failed: $R0"
            Quit
       !endif  
        
       
FunctionEnd
 
 
Function DetectJDK
 ;IntCmp $0 5 is5 lessthan5 morethan5
  ReadRegStr $2 HKLM "${JDK_KEY}" "JavaHome"        
  StrLen $0 "$2"
 IntCmp $0 0 jdk jdk done
  
   jdk:
    Call GetJDK
  done:
FunctionEnd
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


!define JDK_VERSION "1.4"
!define JDK_URL "$INSTALL_HOME/java/"

Function GetJDK
         MessageBox MB_OK "$(^Name) uses J2SDK 1.4, it will now \
                         be downloaded and installed.\
                         An internet connection is required."
 
        StrCpy $2 "$TEMP\Java Development Kit Environment.exe"
        nsisdl::download /TIMEOUT=30000 ${JDK_URL} $2
        Pop $R0 ;Get the return value
                StrCmp $R0 "success" +3
                MessageBox MB_OK "Download failed: $R0"
                Quit
        ExecWait $2
        Delete $2
FunctionEnd
 
 
Function DetectJDK
 
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.4" "JavaHome"
            
  StrLen $0 "$2"
  IntCmp $0 0 jdk jdk done
  
  jdk:
    Call GetJDK
  
  
  
  done:
FunctionEnd
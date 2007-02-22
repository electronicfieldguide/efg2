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
;Call DetectJRE

!define JRE_VERSION "1.5"
!define JRE_URL "http://dlc.sun.com/jdk/jre-1_5_0_01-windows-i586-p.exe"




Function GetJRE
        MessageBox MB_OK "$(^Name) uses Java Runtime 1.5, it will now \
                         be downloaded and installed.\
                         An internet connection is required."
 
        StrCpy $2 "$TEMP\Java Runtime Environment.exe"
        nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
        Pop $R0 ;Get the return value
                StrCmp $R0 "success" +3
                MessageBox MB_OK "Download failed: $R0"
                Quit
        ExecWait $2
        Delete $2
FunctionEnd
 
 
Function DetectJRE
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
 
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
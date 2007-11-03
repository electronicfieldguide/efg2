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
 *$Id: installImageMagickExec.nsi 215 2007-02-28 20:51:30Z kasiedu $
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/
;!define FullInstall

Name "InstallImageMagic"
Caption "InstallImageMagic"

OutFile "InstallImageMagic.exe"

 !define Touch `!insertmacro TouchCall`
 
  !macro TouchCall _FILE
    Push `${_FILE}`
        Call Touch
  !macroend
Section ""
 ${Touch} "C:\JavaCode\efg2\dist\resource\efg2.war"
SectionEnd


Function Touch
 
 
  Exch $0 # Get filename
  Push $1       
  Push $2
  Push $3
 
  ;DetailPrint "Touching $0"
  ClearErrors
  FileOpen $1 "$0" a
  IfErrors error
 
  # Big assumption: FileOpen handles are equivalent to
  #                 the handles used in system calls
  # i.e. those used by:
  # BOOL SetFileTime(
  #   HANDLE hFile,
  #   const FILETIME* lpCreationTime,
  #   const FILETIME* lpLastAccessTime,
  #   const FILETIME* lpLastWriteTime
  # );
 
  System::Call '*(&i2,&i2,&i2,&i2,&i2,&i2,&i2,&i2) i .r2'
  System::Call 'kernel32::GetSystemTimeAsFileTime(i)i(r2)'
  System::Call 'kernel32::SetFileTime(i,i,i,i) i(r1,,,r2) .r3'
  System::Free $2
  FileClose $1
  IntCmp $3 -1 error
  goto end
  
  error:
    SetErrors
    MessageBox MB_OK "Failed to touch $0"
 
  end:
  pop $3
  pop $2
  pop $1
  pop $0
 
FunctionEnd


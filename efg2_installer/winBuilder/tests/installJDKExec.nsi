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
!define FullInstall
!addincludedir ..\headers

!include "InstallVersions.nsh"
!include "CommonRegKeys.nsh"
!include "InstallJDK.nsh"
Name "InstallJDK"
Caption "InstallJDK"

!ifdef FullInstall  
    OutFile "InstallJDKFull.exe"
!else
    !Include "InstallURLsHeader.nsh"
    OutFile "InstallJDK.exe"
!endif

Section ""
   !ifdef FullInstall
        Call addJDKToInstalls
   !endif 
    Call DetectJDK
SectionEnd


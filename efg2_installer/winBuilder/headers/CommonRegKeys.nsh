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
 *$Id: CommonRegKeys.nsh 215 2007-02-28 20:51:30Z kasiedu $
File installImageMagick.nsh
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/

;Depends on InstallVersions.nsh

!define MAGICK_KEY "SOFTWARE\ImageMagick\Current"
!define MAGICK_UNINSTALLER_KEY "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\ImageMagick 6.3.2 Q16_is1"

!define TOMCAT_KEY "SOFTWARE\Apache Software Foundation\Tomcat\${TOMCAT_VERSION}"
!define TOMCAT_UNINSTALLER_KEY "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\Apache Tomcat 5.0"


;
;uninstall string
;MsiExec.exe /I{1BEEA3CE-41AB-4E71-85C7-9E90161E522B}
!define MYSQL_KEY "SOFTWARE\MySQL AB\${MYSQL_VERSION}"
!define MYSQL_UNINSTALLER_KEY "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\{1BEEA3CE-41AB-4E71-85C7-9E90161E522B}"


;
;MsiExec.exe /I{32A3A4F4-B792-11D6-A78A-00B0D0150080}
!define JRE_KEY "SOFTWARE\JavaSoft\Java Runtime Environment"
!define JRE_UNINSTALLER_KEY "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\{32A3A4F4-B792-11D6-A78A-00B0D0150080}"



;MsiExec.exe /I{7148F0A8-6813-11D6-A77B-00B0D0142100}
!define JDK_KEY "SOFTWARE\JavaSoft\Java Development Kit\1.4"
!define JDK_UNINSTALLER_KEY "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\{7148F0A8-6813-11D6-A77B-00B0D0142100}"

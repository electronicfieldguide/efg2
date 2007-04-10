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
 *$Name$
*(c) UMASS,Boston, MA
*Written by Jacob K. Asiedu for EFG project
*/





# Included files
!include Sections.nsh
!include "logiclib.nsh"
!include MUI.nsh
!include WordFunc.nsh


Name "EFG2DataImporter"
!define VERSION 1.0
!define REGKEY "SOFTWARE\EFG\$(^Name)\${VERSION}"
!define EFG2_HOME "C:\cvscheckout\EFGsoftware\efg2"

!define COMPANY "Electronic Field Guide Project UMASS Boston"
!define URL http://efg.cs.umb.edu



!define EFG_SMALL_ICON EFG2_32x32.ico
!define EFG_BIG_ICON EFG2_48x48.ico

!define EFG2_LOCAL_DISTRIBUTION_PATH ..\dist
!define EFG2_RESOURCE_HOME resource
!define EFG2_LOCAL_RESOURCE_PATH "${EFG2_LOCAL_DISTRIBUTION_PATH}\${EFG2_RESOURCE_HOME}"
!define PRODUCT_SUB_VERSION ".2"
!define RELEASE_NOTES_1_0_0_2 releaseNotes_1_0_0_2.html
!define EFG2_RELEASE_NOTES ${EFG2_HOME}\${RELEASE_NOTES_1_0_0_2}
!define EFG2_HELP_FILE efg2doc.html
!define ProductVersion 1.0.0${PRODUCT_SUB_VERSION}

# MUI defines
!define MUI_ICON icons\${EFG_SMALL_ICON}
;!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "EFG2DataImporter"
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\${RELEASE_NOTES_1_0_0_2}




!define INSTALLATION_LOGS "EFG2InstallLog.txt"

!addincludedir headers




OutFile "${EFG2_HOME}\EFG2InstallerPatch_1_0_0_2.exe"

   

!include "TextLog.nsh"
!insertmacro VersionCompare

!define TOMCAT_VERSION "5.0"
!define TOMCAT_KEY "SOFTWARE\Apache Software Foundation\Tomcat\${TOMCAT_VERSION}"



# Reserved Files
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Variables
Var StartMenuGroup

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE license\license.txt
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH


  
# Installer languages
!insertmacro MUI_LANGUAGE English



;SpaceTexts "$diskspace" "$availdiskspace"

InstallDir "$PROGRAMFILES\EFG2DataImporter"
CRCCheck on
XPStyle on
ShowInstDetails hide




InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails hide
VIProductVersion "${ProductVersion}"
VIAddVersionKey ProductName "EFG2DataImporter"
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey CompanyName "${COMPANY}"
VIAddVersionKey CompanyWebsite "${URL}"
VIAddVersionKey FileVersion "${ProductVersion}"
VIAddVersionKey  "Comments" "Installer for EFG Data Importer Application"
VIAddVersionKey  "LegalTrademarks" "University of Massachusetts, Boston,MA"
VIAddVersionKey  "LegalCopyright" "© UMASS Boston"
VIAddVersionKey  "FileDescription" "Installer Executable"



;compile with local dependcy files

# Installer sections
Section ""   
    SetOutPath $INSTDIR
    SetOverwrite on
    ${LogText} "OutPutpath set to $INSTDIR" 
    File ${EFG2_HOME}\${EFG2_HELP_FILE}
    ${LogText} "Copied ${EFG2_HOME}\${EFG2_HELP_FILE}" 
    File ${EFG2_RELEASE_NOTES}
    ${LogText} "Copied ${EFG2_RELEASE_NOTES}" 
  
    SetOutPath $INSTDIR\${EFG2_RESOURCE_HOME}
    SetOverwrite on
    ${LogText} "OutPutpath set to $INSTDIR\${EFG2_RESOURCE_HOME}" 
    File /r ${EFG2_LOCAL_DISTRIBUTION_PATH}\${EFG2_RESOURCE_HOME}\rdbImport.jar
    ${LogText} "Copied ${EFG2_LOCAL_DISTRIBUTION_PATH}\${EFG2_RESOURCE_HOME}\rdbImport.jar" 
    Call doHouseKeeping 
SectionEnd


Section -post ""
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "${REGKEY}" Version "${ProductVersion}"

   
    Call CleanUp
   ; Call startDataImporter
SectionEnd

Function writeProductVersion
    ${LogText} "Writing variables in '$INSTDIR\${EFG2_RESOURCE_HOME}\properties\version.properties'"

   ;on update also update this file
    FileOpen $9 "$INSTDIR\${EFG2_RESOURCE_HOME}\properties\version.properties" w  ;Opens a Empty File an fills it
    FileWrite $9 "efg2.version=${ProductVersion}$\r$\n"   
    FileClose $9   
 ${LogText} "Done writing variables in '$INSTDIR\${EFG2_RESOURCE_HOME}\properties\version.properties'"

FunctionEnd
# Installer functions
Function .onInit
  System::Call "kernel32::CreateMutexA(i 0, i 0, t '$(^Name)') i .r0 ?e"
 Pop $0
 StrCmp $0 0 launch
  StrLen $0 "$(^Name)"
  IntOp $0 $0 + 1
 loop:
   FindWindow $1 '#32770' '' 0 $1
   IntCmp $1 0 +4
   System::Call "user32::GetWindowText(i r1, t .r2, i r0) i."
   StrCmp $2 "$(^Name)" 0 loop
   System::Call "user32::SetForegroundWindow(i r1) i."
   Abort
   Goto end
 launch:
    InitPluginsDir
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp icons\EFG2_32x32.bmp
    advsplash::show 1000 1000 1000 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
   end: 
   IfFileExists "$INSTDIR\${INSTALLATION_LOGS}" nocreate createD
    
    createD:
    CreateDirectory "$INSTDIR"
     
    nocreate:   
     ${LogSetFileName} "$INSTDIR\${INSTALLATION_LOGS}"
    ${LogSetOn}
    Call writeTime
   Call CheckVersion

FunctionEnd

 
Function CleanUp
    ${LogText} "Closing log file $INSTDIR\${INSTALLATION_LOGS}"
    ${LogSetOff}
FunctionEnd


Function updatewebapps
${LogText} "Inside updatewebapps"
   ReadRegStr $2 HKLM "${TOMCAT_KEY}" "InstallPath"
   StrLen $0 "$2"
    ${If} $0 <= 0
   ${LogText} 'Tomcat cannot be found on Machine' 
   MessageBox MB_OK "Tomcat 5 cannot be found on machine. Aborting installation"
    Quit
   ${EndIf}
    ${LogText} 'Tomcat found Machine' 
   
   ; IntCmp $0 0 NoService NoService 0
    ClearErrors
    IfFileExists "$2\bin\tomcat5.exe" 0 NoService
    ClearErrors
    ${LogText} "About to stop Tomcat"
    ExecWait 'cmd /C net stop "Apache Tomcat"'   
    IfErrors  tcnotrunning 
        Sleep 500
         ${LogText} "Tomcat stopped"
        ${LogText} "About to Copy web application updates to Server"
        Call Copy2WebApps
        sleep 200
        ${LogText} "About to start Tomcat"
        ExecWait 'cmd /C net start "Apache Tomcat"'
        DetailPrint "Starting Tomcat.Please wait"
        
        sleep 200
        ${LogText} "Tomcat started"
        Goto End
        
    tcnotrunning:
         ${LogText} "About to Copy web application updates to Server"
        Call Copy2WebApps
        sleep 200
        Goto End    

 NoService:
    ${LogText} "Tomcat 5 must be installed as a service"
        MessageBox MB_OK "Tomcat 5 must be installed as a service"
        Quit

  End:
   ClearErrors
FunctionEnd

;Copy to webapps
Function Copy2WebApps
    ${LogText} "Inside Copy2WebApps"
   ReadRegStr $3 HKLM "${TOMCAT_KEY}" "InstallPath"
    

    DetailPrint 'About to update EFG2 Web application'
    ${LogText} "About to update EFG2 Web application"
    MessageBox MB_OK "About to update EFG2 Web application"
    
    clearErrors
    SetOutPath "$3\webapps\efg2\WEB-INF\classes\project\efg\exports\"
   
    SetOverwrite on
    File /r ${EFG2_LOCAL_RESOURCE_PATH}\exports\*
    ${LogText} "Copied efg2 updates to 'Web application'"
 
     Push $0 
 FunctionEnd



; Check the installed version to the new version
; and prompt user with appropriate message
Function CheckVersion
     ${LogText} "Inside Checkversion"
;read version from unistalled location
    ReadRegStr $2 HKLM "${REGKEY}" "Version"
    
    ${If} $0 <= 0
   ${LogText} 'EFG2DataImporter Cannot be found on Machine' 
   MessageBox MB_OK "EFG2DataImporter Cannot be found on Machine. Aborting upgrade"
    Quit
   ${EndIf}
    

    ${LogText} "Comparing System Importer version:  '$2' to this version: '${ProductVersion}'"
    ${VersionCompare} "${ProductVersion}" "$2" $1
     ${If} $1 = 1
     Goto install
     ${ElseIf} $1 = 0
        Goto sameVersion  
     ${Else}
        Goto installedVersionisNewer
     ${EndIf}
  
  
  installedVersionisNewer:
    ${LogText} "The installed version is newer"
    MessageBox MB_YESNO|MB_ICONQUESTION  "The current version of the EFGDataImporter $\r$\n \
                                installed on your machine $\r$\n \
                             is newer than the version you want to install. $\r$\n \
                             You need to uninstall the current first"
    Goto quitinstall
                             
    
  sameVersion:
  ${LogText} "Versions are the same"
     MessageBox MB_YESNO|MB_ICONQUESTION  "The current version of the EFGDataImporter $\r$\n \
                              installed on your machine $\r$\n \
                             is the same version as the one you are about to install. $\r$\n \
                             Would you still like to overwrite it? " IDYES install IDNO quitinstall
                             
  quitinstall:
  ${LogText} "User chose to quit installation"
   Quit
   
  install:
     ${LogText} "Getting ready to install DataImporter"
FunctionEnd


# Write scripts, deployweb application
Function doHouseKeeping
 ${LogText} "Inside doHousekeeping"
  Call writeProductVersion
  Call updatewebapps
  ${LogText} "doHousekeeping done"
FunctionEnd
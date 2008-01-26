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
# Auto-generated by EclipseNSIS Script Wizard
# Feb 20, 2007 8:09:10 PM


!include "headers\FullInstaller.nsh" 

;If MySQL install is in progress
Var isMySQLInstall

;checks if EFGDataImporter installed the following applications
Var isMagickInstalled
Var isJDKInstalled
Var isJREInstalled
Var isTomcatInstalled
Var isMySQLInstalled

# Included files
!include Sections.nsh
!include "logiclib.nsh"
!include MUI.nsh
!include WordFunc.nsh
!include "efg2Headers.nsh" 
!addincludedir headers

!include "downloadHeaders.nsh"

# Installer attributes
OutFile "EFG2Installer2.0.exe"
!include "TextLog.nsh"
!insertmacro VersionCompare



;order of these inclusions matters

!include InstallVersions.nsh
!include CommonRegKeys.nsh


!include installJDK.nsh
!include installJRE.nsh
!include installImageMagick.nsh
!include InstallMySQL.nsh
!include installTomcat.nsh
!include JavaClasses.nsh

# Reserved Files
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Variables
Var StartMenuGroup
Var /Global UnselCount 
# counts unselected components in the uninstaller.
Var /Global BoxCount
# Counts the components available in the uninstaller
;Var /GLOBAL diskspace
;Var /GLOBAL availdiskspace 

# Installer pages
!define MUI_COMPONENTSPAGE_TEXT_TOP " "



!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE license\license.txt
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_COMPONENTS
!insertmacro MUI_UNPAGE_INSTFILES

  
# Installer languages
!insertmacro MUI_LANGUAGE English


;!insertmacro MUI_COMPONENTSPAGE_TEXT_COMPLIST text
;Text to display on next to the components list.

;!insertmacro MUI_COMPONENTSPAGE_TEXT_INSTTYPE text
;Text to display on next to the installation type combo box.

;Text to display on the of the top of the description box.


;Text to display inside the description box when no section is selected.




;SpaceTexts "$diskspace" "$availdiskspace"

InstallDir "$PROGRAMFILES\EFG2DataImporter"
CRCCheck on
XPStyle on
ShowInstDetails hide

!include "efg2VersionHeaders.nsh"


InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails hide


;compile with local dependcy files

# Installer sections
Section -Main SEC0000
   ; CHECK FOR JRE AFTER JDK INSTALL BEFORE TOMCAT
   ; IM INSTALLED TWICE
    ;TOMCAT NOT INSTALLED AT ALL
    
    IfFileExists "$INSTDIR\${INSTALLATION_LOGS}" nocreate createD
    
    createD:
    CreateDirectory "$INSTDIR"
    
    
    nocreate:
    CreateDirectory "$INSTDIR\${EFG2_SAMPLES_HOME}"
     ${LogSetFileName} "$INSTDIR\${INSTALLATION_LOGS}"
    ${LogSetOn}
    Call writeTime
    ${LogText} "Inside -Main . Initializing global variables" 
    StrCpy $isMySQLInstall "false"
    StrCpy $isMagickInstalled "false"
    StrCpy $isJDKInstalled "false"
    StrCpy $isJREInstalled "false"
    StrCpy $isTomcatInstalled "false"
    StrCpy $isMySQLInstalled "false"
  
    ${LogText} "Calling CheckVersion"
    Call CheckVersion
    Call InstallDependencies
    SetOutPath $INSTDIR
    SetOverwrite on
    ${LogText} "OutPutpath set to $INSTDIR" 
    File ${EFG2_HELP_FILE_LOC}
    ${LogText} "Copied ${EFG2_HELP_FILE}" 
    ;remove me
    File icons\${EFG_SMALL_ICON}
    ${LogText} "Copied icons\${EFG_SMALL_ICON}" 
    File icons\${EFG_BIG_ICON}
    ${LogText} "Copied icons\${EFG_BIG_ICON}" 
    SetOutPath $INSTDIR\${EFG2_SAMPLES_HOME}
     ${LogText} "OutPutpath set to $INSTDIR\${EFG2_SAMPLES_HOME}" 
     File /r ..\dist\samples\*
    SetOutPath $INSTDIR\${EFG2_RESOURCE_HOME}
    ${LogText} "OutPutpath set to $INSTDIR\${EFG2_RESOURCE_HOME}" 
    File /r ${EFG2_LOCAL_DISTRIBUTION_PATH}\* 
    ${LogText} "Copied ${EFG2_LOCAL_DISTRIBUTION_PATH}" 
    File ${ABOUT_EXECUTABLE}
    ${LogText} "Copied ${ABOUT_EXECUTABLE}" 
    File ${EFG2_IMPORTER_EXECUTABLE}
    ${LogText} "Copied ${EFG2_IMPORTER_EXECUTABLE}" 
    ;call the headers
    ${LogText} "${EFG2_IMPORTER_EXECUTABLE}"
    
      Call doHouseKeeping
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
 
SectionEnd


Section -post SEC0001
    ;remove the web application
    RMDir /r "$INSTDIR\${EFG2_RESOURCE_HOME}\efg2"
    
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    ;WriteIniStr "$INSTDIR\EFGKeys${PRODUCT_VERSION}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
    WriteIniStr "$INSTDIR\${EFG2_WEB_HOME_LINK}" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\${EFG2_IMPORTER_UNINSTALLER}
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\$(^Name).lnk" "$INSTDIR\${EFG2_RESOURCE_HOME}\${EFG2_IMPORTER_EXECUTABLE}" "" "$INSTDIR\${EFG_SMALL_ICON}"
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\About.lnk" "$INSTDIR\${EFG2_RESOURCE_HOME}\${ABOUT_EXECUTABLE}" "" "$INSTDIR\${EFG_SMALL_ICON}"
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\EFG2 Samples.lnk" "$INSTDIR\${EFG2_SAMPLES_HOME}"
    
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\manual.lnk" "$INSTDIR\${EFG2_HELP_FILE}"
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\Website.lnk" "$INSTDIR\${EFG2_WEB_HOME_LINK}"
    
   
    FileOpen $4 "$INSTDIR\${EFG2_RESOURCE_HOME}\logs\${EFG2_IMPORTER_LOGS_NAME}" w
    FileClose $4
    sleep 50
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\EFG2 DataImporter Logs.lnk" "$INSTDIR\${EFG2_RESOURCE_HOME}\logs\${EFG2_IMPORTER_LOGS_NAME}" 
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\View EFG2 Installation Logs.lnk" "$INSTDIR\${INSTALLATION_LOGS}" 
    
     CreateShortcut "$SMPROGRAMS\$StartMenuGroup\Uninstall $(^Name).lnk" $INSTDIR\${EFG2_IMPORTER_UNINSTALLER}
     !insertmacro MUI_STARTMENU_WRITE_END
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\${EFG2_IMPORTER_UNINSTALLER}
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\${EFG2_IMPORTER_UNINSTALLER}
    ;installed products
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
    
    WriteRegStr HKLM "${REGKEY}" Version "${ProductVersion}"
    Call WriteJavaClassPath
    CreateShortCut "$DESKTOP\$(^Name).lnk" "$INSTDIR\${EFG2_RESOURCE_HOME}\${EFG2_IMPORTER_EXECUTABLE}" "" "$INSTDIR\${EFG_BIG_ICON}"
    Call checkTomcatInstalled
    Call checkMySQLInstalled
    Call checkMagickInstalled
    Call checkJREInstalled
    Call checkJDKInstalled
    Call removeInstalledApps
    Call CleanUp
   ; Call startDataImporter
SectionEnd
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
  
  ; strcpy  $diskspace "Required Disk Space: "
  
   ;strcpy $availdiskspace "Available Disk Space: "
  end: 


FunctionEnd
/*Function ShowInstallLog
    ExecShell "open" "$INSTDIR\${INSTALLATION_LOGS}"
FunctionEnd*/
 
Function CleanUp
    ${LogText} "Closing log file $INSTDIR\${INSTALLATION_LOGS}"
    ${LogSetOff}
FunctionEnd
Function WriteJavaClassPath
    WriteRegStr HKLM "${REGKEY}\Java" ClassPath "${CLASSPATH}"
    WriteRegStr HKLM "${REGKEY}\Java" AboutClass "${ABOUT_CLASS}"
    WriteRegStr HKLM "${REGKEY}\Java" ImporterClass "${DATA_IMPORTER_CLASS}"
FunctionEnd
Function deploywebapps
${LogText} "Inside deploywebapps"
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
        ${LogText} "About to Copy web application to Server"
        Call CopyWebApps
        sleep 200
        ${LogText} "About to start Tomcat"
        ExecWait 'cmd /C net start "Apache Tomcat"'
        DetailPrint "Starting Tomcat.Please wait"
        
        sleep 200
        ${LogText} "Tomcat started"
        Goto End
        
    tcnotrunning:
        ${LogText} "About to Copy web application to Server"
        Call CopyWebApps
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
Function CopyWebApps
    ${LogText} "Inside CopyWebApps"
   ReadRegStr $3 HKLM "${TOMCAT_KEY}" "InstallPath"
    
    SetOutPath "$3\conf\Catalina\localhost" 
    SetOverwrite on
    File ..\..\efg2_xml\efg2.xml
    ${LogText} "Copied efg2.xml to  '$3\conf\Catalina\localhost'"
    ;copy mysql driver
    SetOutPath "$3\common\lib" 
    SetOverwrite on
    File ${EFG2_LOCAL_RESOURCE_PATH}\mysqldriver.jar
   ${LogText} "Copied mysqldriver.jar to Copied '$3\common\lib'"
    ;Copy war file to webapps
    clearErrors
    DetailPrint 'About to install EFG2 Web application'
    ${LogText} "About to install EFG2 Web application"
    MessageBox MB_OK "About to install EFG2 Web application. This may take a while."
    
    clearErrors
    ;IfErrors warnUser
    CreateDirectory "$3\webapps\efg2"
    ${LogText} "Created directory '$3\webapps\efg2'"
    SetOutPath "$3\webapps"
   
    SetOverwrite on
   ; File /r ${EFG2_LOCAL_RESOURCE_PATH}\efg2.war 
     ;copy all files to efg2
     File /r ..\..\efg2
     ${LogText} "Copied efg2 to '$3\webapps\efg2'"
 
     Push $0
 FunctionEnd
/*Function startDataImporter
   ClearErrors
    MessageBox MB_OK "About to Import Sample Data.$\r$\n\
    Your user name is root and your password is$\r$\n \
    the one you chose while installing MySQL.$\r$\n \
    Plese close the application after the sample data \
    is successfully imported."
    StrCpy $2 "$INSTDIR\${EFG2_RESOURCE_HOME}\${EFG2_IMPORTER_EXECUTABLE}"
    ExecWait $2
FunctionEnd*/
Function removeInstalledApps
!ifdef FullInstall 
    ${LogText} "Removing executables. If they cannot be removed a reboot will remove them" 
    Delete  /REBOOTOK "$INSTDIR\${jdk_exec}"
     ${LogText} "Removed '$INSTDIR\${jdk_exec}'"
    Delete /REBOOTOK "$INSTDIR\${jre_exec}"
     ${LogText} "Removed '$INSTDIR\${jre_exec}'"
    Delete /REBOOTOK "$INSTDIR\${mysqlexec}"
     ${LogText} "Removed '$INSTDIR\${mysqlexec}'"
    Delete /REBOOTOK "$INSTDIR\${tomcat_exec}"
     ${LogText} "Removed '$INSTDIR\${tomcat_exec}'"
    Delete /REBOOTOK "$INSTDIR\${magick_exec}"
     ${LogText} "Removed '$INSTDIR\${magick_exec}'"
!endif

FunctionEnd


; Check the installed version to the new version
; and prompt user with appropriate message
Function CheckVersion
     ${LogText} "Inside Checkversion"
;read version from unistalled location
    ReadRegStr $2 HKLM "${REGKEY}" "Version"
    StrLen $0 "$2"
    ${LogText} "Current Importer version is:  '$2'"
    IntCmp $0 0 install install versioncomp
    
    versioncomp:
    ${LogText} "Comparing System Importer version:  '$2' to this version: '${ProductVersion}'"
    ${VersionCompare} "${ProductVersion}" "$2" $1
    IntCmp $1 1  install sameVersion installedVersionisNewer  
   
  
  installedVersionisNewer:
    ${LogText} "The installed version is newer"
    MessageBox MB_YESNO|MB_ICONQUESTION  "The current version of the EFGDataImporter $\r$\n \
                                installed on your machine $\r$\n \
                             is newer than the version you are about to install. $\r$\n \
                             Would you still like to install it? " IDYES install IDNO quitinstall
                             
    
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

Function InstallDependencies
 
    !ifdef FullInstall
        ${LogText} "This is a full installation"
        Call addJDKToInstalls
        Call addJREToInstalls
        Call addTomcatToInstalls
        Call addMySQLToInstalls
        Call addMagickToInstalls
   !endif 
    Call DetectMYSQL
   ; StrCmp $isMySQLInstall "true" printMySQLMessage continue
   
  ; printMySQLMessage:
   ;Call GetMySQLMessage  
   ;DetailPrint "MySQL installation is still in progress..Please wait.."
   
   ;Sleep 30000
    

    Call DetectJDK
    Call DetectJRE
    Call DetectTOMCAT
    Call DetectMAGICK
FunctionEnd
# Write scripts, deployweb application
Function doHouseKeeping
 ${LogText} "Inside doHousekeeping"
  Call WriteApplicationVariables
  Call deploywebapps
  ${LogText} "doHousekeeping done"
FunctionEnd




Function WriteApplicationVariables
    ${LogText} "Inside WriteApplicationVariables"
    ${LogText} "Writing variable in '$INSTDIR\${EFG2_RESOURCE_HOME}\properties\envvars.properties'"
    FileOpen $9 "$INSTDIR\${EFG2_RESOURCE_HOME}\properties\envvars.properties" a  ; Opens a Empty File an fills it
    FileSeek $9 0 END #go to end
   
    ;write java home
    ReadRegStr $2 HKLM "${JDK_KEY}" "JavaHome"     
    Push $2 
    Push "${BACK_SLASH}"
    Call StrSlash
    Pop $R0
    FileWrite $9 "$\r$\n" 
    FileWrite $9 "JAVAHOME=/$R0/$\r$\n" 
    
 
    ;write mysql home
    ReadRegStr $2 HKLM "${MYSQL_KEY}" "Location"
    Push $2 
    Push "${BACK_SLASH}"
    Call StrSlash
    Pop $R0
    FileWrite $9 "MYSQL_HOME=/$R0$\r$\n"  ;text to write to file 
    FileClose $9  
    
    FileOpen $9 "$INSTDIR\${EFG2_RESOURCE_HOME}\properties\workspace.configs.properties" a  ;Opens a Empty File an fills it
    FileSeek $9 0 END #go to end
    ;write Magick home
    ReadRegStr $2 HKLM "${MAGICK_KEY}" "BinPath"  
    Push $2 
    Push "${BACK_SLASH}"
    Call StrSlash
    Pop $R0
    
    FileWrite $9 "efg.imagemagicklocation.current=/$R0$\r$\n"  ;text to write to file 
    FileWrite $9 "efg.imagemagicklocation.lists=/$R0$\r$\n"  ;text to write to file 
    
    ;write catalina home
    ReadRegStr $2 HKLM "SOFTWARE\Apache Software Foundation\Tomcat\${TOMCAT_VERSION}" "InstallPath"
    Push $2 
     
    Push "${BACK_SLASH}"
    Call StrSlash
    Pop $R0
   
    ; to url
    FileWrite $9 "efg.serverlocations.current=/$R0$\r$\n"  ;text to write to file 
    FileWrite $9 "efg.serverlocations.lists=/$R0$\r$\n"  ;text to write to file    

    ;mediaresources home

 FileWrite $9 "efg.mediaresources.home.current=/$R0/webapps/efg2/EFGImages$\r$\n"
 FileWrite $9 "efg.templates.home.current=/$R0/webapps/efg2/templateConfigFiles/xml$\r$\n"
 FileWrite $9 "efg.thumbnails.dimensions.lists=175,200,125,150$\r$\n"
 FileWrite $9 "efg.sampledata.loaded=true$\r$\n"
 FileWrite $9 "efg.thumbnails.dimensions.current=100$\r$\n"
 FileWrite $9 "efg.serverlocation.checked=false$\r$\n"
 FileWrite $9 "efg.thumbnails.dimensions.checked=true$\r$\n"
 FileWrite $9 "efg.showdismiss.checked=false$\r$\n"
 FileWrite $9 "efg.imagemagicklocation.checked=true$\r$\n"
 FileWrite $9 "efg.showchangedirectorymessage.checked=false$\r$\n"
 FileWrite $9 "cacheserverurl=http://localhost:8080/efg2$\r$\n"


    FileClose $9  
   ${LogText} "Done variable in '$INSTDIR\${EFG2_RESOURCE_HOME}\properties\envvars.properties'"
   
    Call writeProductVersion
     ${LogText} "WriteApplicationVariables done"
FunctionEnd



; Push $filenamestring (e.g. 'c:\this\and\that\filename.htm')
; Push "\"
; Call StrSlash
; Pop $R0
; ;Now $R0 contains 'c:/this/and/that/filename.htm'
Function StrSlash
  Exch $R3 ; $R3 = needle ("\" or "/")
  Exch
  Exch $R1 ; $R1 = String to replacement in (haystack)
  Push $R2 ; Replaced haystack
  Push $R4 ; $R4 = not $R3 ("/" or "\")
  Push $R6
  Push $R7 ; Scratch reg
  StrCpy $R2 ""
  StrLen $R6 $R1
  StrCpy $R4 "\"
  StrCmp $R3 "/" loop
  StrCpy $R4 "/"  
loop:
  StrCpy $R7 $R1 1
  StrCpy $R1 $R1 $R6 1
  StrCmp $R7 $R3 found
  StrCpy $R2 "$R2$R7"
  StrCmp $R1 "" done loop
found:
  StrCpy $R2 "$R2$R4"
  StrCmp $R1 "" done loop
done:
  StrCpy $R3 $R2
  Pop $R7
  Pop $R6
  Pop $R4
  Pop $R2
  Pop $R1
  Exch $R3
FunctionEnd





# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o -un.Main UNSEC0000
   
    
SectionEnd

Function un.removeTomcat


     ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "tomcat_uninstaller"  
     ExecWait $1
 
FunctionEnd
Function un.removeMagick

   ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "magick_uninstaller" 
    ExecWait $1
FunctionEnd
Function un.removeMySQL

   ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "mysql_uninstaller"    
   ExecWait $1
FunctionEnd
Function un.removeJRE
;${LogText} "Removing JRE"
   ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "jre_uninstaller"     
   ExecWait $1
FunctionEnd
Function un.removeJDK

   ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "jdk_uninstaller"   
   ExecWait $1
FunctionEnd

Section un.Tomcat un_Tomcat
SectionGetFlags "${un_Tomcat}" $R0

    ${If} $R0 == 1
       ; Call un.removeTomcat
   ${EndIf}
Sectionend
Section un.Magick un_Magick
    SectionGetFlags "${un_Magick}" $R0
  ${If} $R0 == 1
   ;Call un.removeMagick
  ${EndIf}

Sectionend
Section un.MySQL un_MySQL
   ${If} $R0 == 1
  ; Call un.removeMySQL
  ${EndIf}

Sectionend

Section un.JRE un_JRE
   SectionGetFlags "${un_JRE}" $R0
   ${If} $R0 == 1  
  ; Call un.removeJRE
   ${EndIf}

Sectionend

Section un.JDK un_JDK
   SectionGetFlags "${un_JDK}" $R0
   ${If} $R0 == 1  
   ;Call un.removeJDK
   ${EndIf}
Sectionend

Section un.EFGWebapps un_EFGWebapps 
  SectionGetFlags "${un_EFGWebapps}" $R0
 ${If} $R0 == 1 
 ; Call un.removewebapps 
 ${EndIf}

Sectionend

;Uninstaller Section for complete removal

Section "un.All" unAll
 SectionGetFlags "${un_EFGWebapps}" $R0
 ${If} $R0 == 1 
  Call un.removewebapps 
  Sleep 3000
 ${EndIf}
   
   
  SectionGetFlags "${un_Tomcat}" $R0
  ${If} $R0 == 1
   Call un.removeTomcat
   Sleep 3000
  ${EndIf}
   
  SectionGetFlags "${un_MySQL}" $R0
  ${If} $R0 == 1
   Call un.removeMySQL
   Sleep 3000
  ${EndIf}
   
  SectionGetFlags "${un_Magick}" $R0
  ${If} $R0 == 1
   Call un.removeMagick
   Sleep 3000
  ${EndIf}
  
  
   SectionGetFlags "${un_JRE}" $R0
   ${If} $R0 == 1  
   Call un.removeJRE
   Sleep 3000
   ${EndIf}
   
   SectionGetFlags "${un_JDK}" $R0
   ${If} $R0 == 1  
   Call un.removeJDK
   Sleep 3000
   ${EndIf}
SectionEnd

Section -un.post UNSEC0001   
    Call un.deleteRegistry
    Call un.deleteFiles
    Call un.clean
SectionEnd

  
!insertmacro MUI_UNFUNCTION_DESCRIPTION_BEGIN
!insertmacro MUI_DESCRIPTION_TEXT ${un_efgwebapps} "EFG2 Web Application"
  !insertmacro MUI_DESCRIPTION_TEXT ${un_tomcat} "Apache Tomcat ${TOMCAT_VERSION}"
  !insertmacro MUI_DESCRIPTION_TEXT ${un_mysql} "${MYSQL_VERSION}"
  !insertmacro MUI_DESCRIPTION_TEXT ${un_magick} "Image Editing software"
   !insertmacro MUI_DESCRIPTION_TEXT ${un_jre} "Java Runtime Environment"
    !insertmacro MUI_DESCRIPTION_TEXT ${un_jdk} "Java Ddevelopment Kit"
  !insertmacro MUI_UNFUNCTION_DESCRIPTION_END
Function un.onSelChange

  StrCpy $UnselCount $BoxCount
 
  SectionGetFlags "${un_EFGWebapps}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0
 
 SectionGetFlags "${un_Magick}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0


  SectionGetFlags "${un_Tomcat}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0

  SectionGetFlags "${un_MySQL}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0

  SectionGetFlags "${un_JRE}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0 
  
  SectionGetFlags "${un_JDK}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0 
FunctionEnd
;delete values from registry
Function un.deleteRegistry
  ;  ${LogText} "Deleting registry values"
    DeleteRegValue HKLM "${REGKEY}\Components" Main
    
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
 
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegValue HKLM "${REGKEY}" Version
  
    DeleteRegValue HKLM "${REGKEY}\Java" ClassPath
    DeleteRegValue HKLM "${REGKEY}\Java" AboutClass
    DeleteRegValue HKLM "${REGKEY}\Java" ImporterClass
    DeleteRegKey HKLM "${REGKEY}\java"
  
    DeleteRegKey HKLM "${REGKEY}\Components"
    DeleteRegKey HKLM "${REGKEY}"
 FunctionEnd
; delete all files and folders
Function un.deleteFiles  
    ;${LogText} "Deleting files"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\manual.lnk"
     ;${LogText} "$SMPROGRAMS\$StartMenuGroup\manual.lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\Website.lnk"
    ;${LogText} "$SMPROGRAMS\$StartMenuGroup\Website.lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\EFG2 DataImporter Logs.lnk" 
    
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\View EFG2 Installation Logs.lnk" 
    
    ;${LogText} "$SMPROGRAMS\$StartMenuGroup\Logs.lnk" 
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\About.lnk"  
  ;${LogText} "$SMPROGRAMS\$StartMenuGroup\About.lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^Name).lnk"  
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\EFG2 Samples.lnk" 
   ;${LogText} "$SMPROGRAMS\$StartMenuGroup\$(^Name).lnk"  
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\Uninstall $(^Name).lnk"
   
   
   ;${LogText} "$SMPROGRAMS\$StartMenuGroup\Uninstall $(^Name).lnk"
    
    
     RmDir /r /REBOOTOK  "$INSTDIR\${EFG2_SAMPLES_HOME}"
      RmDir /r /REBOOTOK  "$INSTDIR\${INSTALLATION_LOGS}"
   ;${LogText} "$INSTDIR\${EFG2_IMPORTER_UNINSTALLER}"
    Delete /REBOOTOK "$INSTDIR\${EFG_SMALL_ICON}"
   ;${LogText} "$INSTDIR\${EFG_SMALL_ICON}"
    Delete /REBOOTOK "$INSTDIR\${EFG_BIG_ICON}"
   ;${LogText} "$INSTDIR\${EFG_BIG_ICON}"
    Delete $INSTDIR\$(^Name).url
   ;${LogText} "$INSTDIR\$(^Name).url"
    Delete /REBOOTOK "$DESKTOP\$(^Name).lnk"
   ;${LogText} "$DESKTOP\$(^Name).lnk"
    Delete /REBOOTOK "$INSTDIR\${EFG2_HELP_FILE}"
    ;${LogText} "$INSTDIR\${EFG2_HELP_FILE}"
    RmDir /r /REBOOTOK "$INSTDIR\${EFG2_RESOURCE_HOME}"
    ;${LogText} "$INSTDIR\${EFG2_RESOURCE_HOME}"
    RmDir /REBOOTOK "$SMPROGRAMS\$StartMenuGroup"
    ;${LogText} "$SMPROGRAMS\$StartMenuGroup"
    Delete /REBOOTOK "$INSTDIR\${EFG2_IMPORTER_UNINSTALLER}"
    RmDir /r /REBOOTOK $INSTDIR
    
FunctionEnd

Function un.clean
    ;${LogText} "In Section -CleanUp"
   ; ${LogSetOff}
FunctionEnd

Function un.removewebapps
    ;${LogText} "Remove webapps"
    ClearErrors
    ReadRegStr $2 HKLM "${TOMCAT_KEY}" "InstallPath"
    StrLen $0 "$2"
    
    
    IntCmp $0 0 End End 0
    ExecWait 'cmd /C net stop "Apache Tomcat"'   
    IfErrors  tcnotrunning 0
    Sleep 30000
    RmDir /r /REBOOTOK "$2/webapps/efg2"   
    ;${LogText} "'$2/webapps/efg2' removed"
     Sleep 3000
    ExecWait 'cmd /C net start "Apache Tomcat"'
    Goto End
        
    tcnotrunning:
       RmDir /r /REBOOTOK "$2/webapps/efg2"
       ;${LogText} "'$2/webapps/efg2' removed"
       Goto End    
  End:
   ClearErrors
FunctionEnd







# Uninstaller functions
Function un.onInit
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
    MessageBox MB_OK|MB_ICONEXCLAMATION|MB_TOPMOST "The uninstaller is already running."
   Abort
   Goto efg_installed
   
 launch: 
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2|MB_TOPMOST "Are you sure you want to completely remove $(^Name) and all of its components?" IDYES +2
  Quit
    ; StrCpy $diskspace "'none'"
    ;StrCpy $availdiskspace 'none'
  
   ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuGroup
    !insertmacro SELECT_UNSECTION Main ${UNSEC0000}
     SectionSetText ${unAll} ""
      SectionSetText ${un_EFGWebapps} "EFGWebApps"
      StrCpy $UnselCount "0"
    StrCpy $BoxCount "0"
    
    ;SpaceTexts 'none' " "
  ;  ${LogSetFileName} "$INSTDIR\uninstalllogs.txt"
   ; ${LogSetOn}
    Call un.checkSelectedSections
 efg_installed:
 
FunctionEnd
Function un.checkSelectedSections

;hide section unAll
 
 
  ;${LogText} "Inside checkSelectedSections"
  ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "tomcat_uninstaller"  
  StrLen $0 "$1"
  IntCmp $0 0 noTomcat noTomcat isTomcat
  isTomcat:
  SectionSetText ${un_tomcat} "Tomcat ${TOMCAT_VERSION}"
    ;IntOp $BoxCount $BoxCount + 1
    SectionSetFlags ${un_Tomcat} 0
    Goto checkMagick
  noTomcat:
    SectionSetText ${un_Tomcat} ""
    SectionSetFlags ${un_Tomcat} 0
  
 checkMagick:
  ;${LogText} "Check Magic"
 
   ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "magick_uninstaller"  
   StrLen $0 "$1"
   IntCmp $0 0 noMagick noMagick isMagick
  isMagick:
    SectionSetText ${un_magick} "ImageMagick ${MAGICK_VERSION}"
    SectionSetFlags ${un_magick} 0
    ;IntOp $BoxCount $BoxCount + 1
    Goto checkMySQL
  noMagick:
    SectionSetText ${un_magick} ""
    SectionSetFlags ${un_magick} 0

  checkMySQL:
     ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "mysql_uninstaller"    
     StrLen $0 "$1"
   IntCmp $0 0 noMySQL noMySQL isMySQL
   isMySQL:
    SectionSetText ${un_mysql} "${MYSQL_VERSION}"
    ;IntOp $BoxCount $BoxCount + 1
    SectionSetFlags ${un_mysql} 0
    Goto checkJRE
  noMySQL:
    SectionSetText ${un_mysql} ""
    SectionSetFlags ${un_mysql} 0
  
  checkJRE:
    ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "jre_uninstaller"     
    StrLen $0 "$1"
   IntCmp $0 0 noJRE noJRE isJRE
  isJRE:
    SectionSetText ${un_jre} "Java Runtime Environment ${JRE_VERSION}"
    ;IntOp $BoxCount $BoxCount + 1
    SectionSetFlags ${un_jre} 0
    Goto checkJDK
  noJRE:
    SectionSetText ${un_jre} ""
    SectionSetFlags ${un_jre} 0
  
  checkJDK:
     ReadRegStr $1 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "jdk_uninstaller"   
    StrLen $0 "$1"
   IntCmp $0 0 noJDK noJDK isJDK
   isJDK:
    SectionSetText ${un_jdk} "Java Development Kit ${JDK_VERSION}"
    ;IntOp $BoxCount $BoxCount + 1
    SectionSetFlags ${un_jdk} 0
    
    Goto end
  noJDK:
    SectionSetText ${un_jdk} ""
    SectionSetFlags ${un_jdk} 0

  end:
FunctionEnd

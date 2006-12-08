/**
*$Name$
*$Id$
*/
/**
*do preinstallation checks
*/

Function checkPreInstallationRequirements
 Call findJDK
 Call setJRE
 Call findTomcat
 Call findMySQL
FunctionEnd

/*
* Figure out how to call reinstall from checkVersion
*/
Function  startInstallation
    Call setUpLogFile
    Call checkVersion ; make sure version if any, is newer before you install
    Call  checkPreInstallationRequirements 
   ;Find out if Tomcat exists and if so whether it is running or not
    Push "Found $CURRENT_TOMCAT $\r$\n"  ;text to write to file 
    Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
    Call WriteToFile
    
    Push $CURRENT_TOMCAT
    Push "bin" 
    Push "<"
    Call StrLoc
    pop $R0
    StrCmp $R0 "" foundReg notFoundReg
    notFoundReg:
        clearErrors
        Push "About to stop Tomcat $\r$\n"  ;text to write to file 
        Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
        Call WriteToFile
        ExecWait '"$CURRENT_TOMCAT\shutdown.bat"'
        IfErrors tcnotrunning1 tcrunning1
        
        tcnotrunning1:
            StrCpy $isTomcatRunning "false"
            Goto sl1
       tcrunning1:
            Sleep 5000
            StrCpy $isTomcatRunning "true"
            Goto sl1
        sl1: 
            Push "About to copy files to server $\r$\n"  ;text to write to file 
            Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
            Call WriteToFile
        
            Call CopyFiles
            ;implemented by subclasses
            
            Push "About to start Tomcat $\r$\n"  ;text to write to file 
            Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
            Call WriteToFile
            Strcmp $isTomcatRunning "true" 0 pre-end
            ExecWait '"$CURRENT_TOMCAT\startup.bat"'
            Goto pre-end
        
    foundReg: 
        clearErrors
        Push "About to stop Tomcat $\r$\n"  ;text to write to file 
        Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
        Call WriteToFile
        ExecWait 'cmd /C net stop "$CURRENT_TOMCAT"'
        IfErrors tcnotrunning tcrunning 

    tcnotrunning:
        StrCpy $isTomcatRunning "false"
        Goto sl
    tcrunning:
        Sleep 5000
        StrCpy $isTomcatRunning "true"
        Goto sl

    sl:
        Push "About to copy files to server $\r$\n"  ;text to write to file 
        Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
        Call WriteToFile
 
        ; copy and write all files to appropriate place on clients machine
        Call CopyFiles
    
       
        Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
        Call WriteToFile
        clearErrors
        Strcmp $isTomcatRunning "true" 0 pre-end
         Push "About to start Tomcat $\r$\n"  ;text to write to file 
        ExecWait 'cmd /C net start "$CURRENT_TOMCAT"'
        Sleep 5000
        clearErrors
    pre-end:
        IfErrors 0 success
        Push "Unsuccessful in applying UPDATE $\r$\n"  ;text to write to file 
        Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
        Call WriteToFile
        MessageBox MB_OK "Unsuccessful in applying UPDATE"
        Goto end

    success:
        ;replace display version in registry
        Push "UPDATE successfully applied $\r$\n"  ;text to write to file 
        Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
        Call WriteToFile
        ;WriteRegStr SHCTX "${EFG2_PRODUCT_UNINST_KEY}" "${DISPLAY_VERSION}" "${EFG2_SHORT_VERSION_NUMBER}"
        MessageBox MB_OK "UPDATE successfully applied"
 
    end:
FunctionEnd

/**
* Checks for the existence of MySQL
*/
!macro initMySQLFind UN
Function ${UN}initMySQLFind
 Pop $R0
 Pop $R1
 Push $R0
 ClearErrors
 ReadRegStr $R0 HKLM "${MYSQL_KEY}" "Location"
 StrCmp $R0 "" checkHKCU finishMySQL
  
 finishMySQL:  
  StrCpy $MYSQL_LOCATION "$R0"
  StrCpy $CURRENT_MYSQL "${MYSQL_SERVICES_NAME}"
  StrCpy $CURRENT_MYSQL_KEY "${MYSQL_KEY}" 
  StrCpy $R0 "1"
  Goto Continue

 checkHKCU:
  
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${MYSQL_KEY}" "Location"
  StrCmp $R0 "" CheckMySQLHome 0
  StrCpy $MYSQL_LOCATION "$R0"
  StrCpy $CURRENT_MYSQL "${MYSQL_SERVICES_NAME}"
  StrCpy $CURRENT_MYSQL_KEY "${MYSQL_KEY}" 
   StrCpy $R0 "1"
  Goto Continue


  CheckMySQLHome:
   
    Pop $R0
    Push $R0
    ClearErrors
    
    ;check environment variables
    ReadEnvStr $R0 MYSQL_HOME
    IfErrors 0 mysql
    Pop $R0
    Push $R0
    ClearErrors
    StrCpy $R0 ""
    StrCpy $R1 ""
    Goto Continue

  mysql:
    Pop $R1
    Push $R1
    ClearErrors
    StrCpy $R1 "1"
 Continue:
FunctionEnd
!macroend
!insertmacro initMySQLFind ""
!insertmacro initMySQLFind "un."
/**
* Check for the existence of MySQL on client machine
*/
Function findMySQL
 ;Find the MySQL Installation in the Registry
 Call initMySQLFind 
 StrCmp $R0 "" InstallMySQL 
 StrCmp $R0 "1" Noinstall
 StrCmp $R1 "1" localMySQL 

 Pop $R0
 Goto Continue

 localMySQL:
  MessageBox MB_YESNO|MB_TOPMOST "${MYSQL_LOCAL_MESSAGE}"
  StrCpy $MYSQL_LOCATION "$R0"
  StrCpy $CURRENT_MySQL "$MySQL_LOCATION\bin"
  Goto Continue

InstallMySQL:
 MessageBox MB_YESNO|MB_TOPMOST "${MYSQL_INSTALL_MESSAGE}" IDNO Noinstall
 SetOutPath "$TEMP"
 SetOverwrite try
 File "${MySQL_SOURCE_DIR}\${MySQL_SOURCE_NAME}" 
 ExecWait "$TEMP\${MySQL_SOURCE_NAME}"
 sleep 5000
 Push $R0
 ReadRegStr $R0 HKLM "${MySQL_KEY}" "Location"
 StrCmp $R0 "" checkHKCU finishMySQLInstall
 
 finishMySQLInstall:
  StrCmp $R0 "" Noinstall 0
  StrCpy $MySQL_LOCATION "$R0"
  ;may cause problems if it is not installed in HKLM
  Pop $R0
  StrCpy $CURRENT_MYSQL "${MySQL_SERVICES_NAME}"
  StrCpy $CURRENT_MySQL_KEY "${MySQL_KEY}" 
  Delete "$TEMP\${MySQL_SOURCE_NAME}"
  Goto Continue

 checkHKCU:
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${MySQL_KEY}" "Location"
  StrCmp $R0 "" Noinstall 

  StrCpy $MySQL_LOCATION "$R0"
  StrCpy $CURRENT_MySQL "${MySQL_SERVICES_NAME}"
  StrCpy $CURRENT_MySQL_KEY "${MySQL_KEY}" 
  Pop $R0
  Delete "$TEMP\${MySQL_SOURCE_NAME}"
  Goto Continue
 
 Noinstall:
  Pop $R0
  Pop $R1
 Continue:
FunctionEnd
Function setJRE
  Push $R0
  Push $R1

  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  IfErrors 0 JreFound
 ;look inside HKCU
  Pop $R0
  Pop $R1
  Push $R0
  Push $R1
  ClearErrors
  ReadRegStr $R1 HKCU "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKCU "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  IfErrors end JreFound

 JreFound:
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreSet
  StrCpy $R0 "javaw.exe"
  ifErrors end JreSet

 JreSet:
   StrCpy "$JRE_EXEC" $R0 

  
 end:

FunctionEnd
/**
* Check for the existence of JDK on client machine
*/
Function findJDK
  Pop $R0
  Push $R0
 
  ClearErrors
  ReadRegStr $R0 HKLM "${JAVA_REGISTRY_KEY}" "JavaHome"
  IfErrors 0 Continue
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${JAVA_REGISTRY_KEY}" "JavaHome"
  IfErrors 0 Continue
  MessageBox MB_YESNO "${JAVA_INSTALL_MESSAGE}" IDNO Noinstall
  SetOutPath "$TEMP"
  SetOverwrite try
  File "${JDK_SOURCE_DIR}\${JDK_SOURCE_NAME}"
  ExecWait "$TEMP\${JDK_SOURCE_NAME}" $0

  sleep 5000    
 StrCmp $0 "0" 0 Noinstall
  SetRebootFlag true
  Goto Continue
 
  Noinstall:
   Pop $R0
   Pop $0
   MessageBox MB_OK|MB_TOPMOST "${EXIT_INSTALL_MESAGE}"
   Quit
 Continue:
   Pop $R0
   Pop $0
FunctionEnd
/*
 * Initialize install page
 */
Function setUpLogFile
CreateDirectory "$TEMP\${EFG2_TEMP_DIR}"
SetOutPath "$TEMP\${EFG2_TEMP_DIR}"
File "${EFG2_PRODUCT_SOURCE_DIR}\${UPDATE_DIR}\log.txt"


Push "Logs for current installation $\r$\n"  ;text to write to file 
Push "$TEMP\${EFG2_TEMP_DIR}\log.txt" ;file to write to 
Call WriteToFile

Push "********************************$\r$\n$\r$\n"  ;text to write to file 
Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
Call WriteToFile

${GetTime} "" "LS" $0 $1 $2 $3 $4 $5 $6
    ; $0="01"      day
    ; $1="04"      month
    ; $2="2005"    year
    ; $3="Friday"  day of week name
    ; $4="11"      hour
    ; $5="05"      minute
    ; $6="50"      seconds

Push "Date=$0/$1/$2 ($3)$\nTime=$4:$5:$6 $\r$\n"  ;text to write to file 
Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
Call WriteToFile
FunctionEnd

/**
* Checks for the existence of a Tomcat4.XXX ot Tomcat5.0.XXX.
* If one does not exists UPDATE cannot be installed.
*/
Function findTomcat

 ;Find the Tomcat Installation in the Registry
 Call initTomcatFind 
 StrCmp $R0 "" InstallTomcat 
 StrCmp $R0 "1" Continue
 StrCmp $R1 "1" localTomcat 

 Pop $R0
 Goto Continue

 localTomcat:
   Push "Tomcat installed locally.Finding it with CATALINA_HOME:  $CATALINA_HOME  $\r$\n"  ;text to write to file 
Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
 
Call WriteToFile
  StrCpy $CURRENT_TOMCAT "$CATALINA_HOME\bin"
  Goto Continue

InstallTomcat:
 Push "Tomcat is not installed on your system. UPDATE cannot be applied.  $\r$\n"  ;text to write to file 
Push "$TEMP\${UPDATE_DIR}\log.txt" ;file to write to 
 Call WriteToFile
 ;install Tomcat here
  MessageBox MB_YESNO|MB_TOPMOST "Tomcat is not installed on this system. $\rEFGDataImporter needs to run \
 under Tomcat.$\r Do you want to install it now?" IDNO Noinstall
 SetOutPath "$TEMP"
 SetOverwrite try
 File "${TOMCAT_SOURCE_DIR}\${TOMCAT_SOURCE_NAME}" 
 ExecWait "$TEMP\${TOMCAT_SOURCE_NAME}"
 sleep 5000
 Push $R0
 ReadRegStr $R0 HKLM "${TOMCAT5_KEY}" "InstallPath"
 StrCmp $R0 "" checkHKCU51 finishTomcatInstall
 
 finishTomcatInstall:
  StrCmp $R0 "" Noinstall 0
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $WEB_APPS_LOCATION "$R0\webapps"
  ;may cause problems if it is not installed in HKLM
  Pop $R0
  StrCpy $CURRENT_TOMCAT "${TOMCAT5_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT5_KEY}" 
  Delete "$TEMP\${TOMCAT_SOURCE_NAME}"
  Goto Continue

 checkHKCU51:
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${TOMCAT5_KEY}" "InstallPath"
  StrCmp $R0 "" Noinstall 
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $WEB_APPS_LOCATION "$R0\webapps"
  StrCpy $CURRENT_TOMCAT "${TOMCAT5_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT5_KEY}" 
  Pop $R0
  Delete "$TEMP\${TOMCAT_SOURCE_NAME}"
  Goto Continue
 
 Noinstall:
  Pop $R0
  Pop $R1
  Abort

 Continue:
 
FunctionEnd
/**
* Find Tomcat installation
*/
Function initTomcatFind

 Pop $R0
 Pop $R1
 Push $R0
 ClearErrors
 ReadRegStr $R0 HKLM "${TOMCAT5_KEY}" "InstallPath"
 StrCmp $R0 "" checkHKCU5 finishTomcat5
  
 finishTomcat5:
  StrCpy $CURRENT_TOMCAT "${TOMCAT5_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT5_KEY}" 
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $R0 "1"
  Goto Continue

 checkHKCU5:
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${TOMCAT5_KEY}" "InstallPath"
  StrCmp $R0 "" checkTomcat4 0
  StrCpy $CURRENT_TOMCAT "${TOMCAT5_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT5_KEY}" 
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $R0 "1"
  Goto Continue

 checkTomcat4:
  Pop $R0
  Push $R0
  clearErrors
  ReadRegStr $R0 HKLM "${TOMCAT4_KEY}" ""   
  StrCmp $R0 "" checkHKCU4 finishTomcat4
  
 finishTomcat4:
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $R0 "1"
  StrCpy $CURRENT_TOMCAT "${TOMCAT4_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT4_KEY}" 
  Goto Continue
  
 checkHKCU4:
   Pop $R0
   Push $R0
   ClearErrors
   ReadRegStr $R0 HKCU "${TOMCAT4_KEY}" ""
   StrCmp $R0 "" CheckCatalinaHome 0
   StrCpy $CATALINA_HOME "$R0"
   StrCpy $CURRENT_TOMCAT "${TOMCAT4_SERVICES_NAME}"
   StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT4_KEY}" 
   StrCpy $R0 "1"
   Goto Continue
 
  CheckCatalinaHome:
    Pop $R0
    Push $R0
    ClearErrors
    ;check environment variables
    ReadEnvStr $R0 CATALINA_HOME
    IfErrors 0 catalina
    Pop $R0
    Push $R0
    ClearErrors
    StrCpy $R0 ""
    StrCpy $R1 ""
    Goto Continue

  catalina:
    StrCpy $CATALINA_HOME "$R0"
    Pop $R1
    Push $R1
    ClearErrors
    StrCpy $R1 "1"
 Continue:
FunctionEnd

Function noInstall
 
FunctionEnd

Function checkVersion


;get the display version if any
 Pop $R0
 Push $R0
 ReadRegStr $R0 HKLM "${EFG2_PRODUCT_UNINST_KEY}" "${DISPLAY_VERSION}"
 ;if it exists goto set shctx to all. if not check hkcu
 StrCmp $R0 "" checkHKCU 
 SetShellVarContext all
 ;compare the current version to the installers version
 Goto setUpInstall
 
 ;read hkcu registry
  checkHKCU:
    Pop $R0
    Push $R0
    ReadRegStr $R0 HKCU "${EFG2_PRODUCT_UNINST_KEY}" "${DISPLAY_VERSION}"
    StrCmp $R0 ""  installMessage  ;This application is not installed on the 
                                    ;current computer so go ahead and install it
   SetShellVarContext current
   Goto setUpInstall
   
   installMessage:
   ;means it does not exists on your system install it
    Goto install
   ; 0       Versions are equal
;   1       Version 1 is newer
;   2       Version 2 is newer
    setUpInstall:
     var /GLOBAL cur_version
     ;StrCpy "$R9" "$R0"
     StrCpy "$cur_version" "$R0"
     ${VersionCheckNew} "$cur_version" "${EFG2_SHORT_VERSION_NUMBER}" "$R0"
     IntCmp $R0 "1" versionRoNewer versionEqual versionRoOlder
     
     versionRoNewer:
      MessageBox MB_YESNO|MB_TOPMOST "Your version of the EFGKeys Configurator ($R9) $\r\
                      is newer than the version you are trying to install (${EFG2_SHORT_VERSION_NUMBER})$\r\ 
                      .Would you still like to continue with the installation?" IDYES install 
                      Goto noinstall
     versionEqual:
    MessageBox MB_YESNO|MB_TOPMOST "Your version of the EFGKeys Configurator ($R9)$\r\
                      is the same as the version you are trying to install (${EFG2_SHORT_VERSION_NUMBER}).$\r\ 
                      Would you still like to continue with the installation?" IDYES install 
                      Goto noinstall
     versionRoOlder:
        Goto install
   
     noinstall:
          MessageBox MB_OK|MB_TOPMOST "Installation is Terminating...$\r"
           Quit
          
     install:
  
FunctionEnd

/**
* Installs the EFGDataImporter Configurator Application
*/


Function copyImporterFiles
;copy schema,icons efg2.xml,docs
  CreateDirectory "${EFG2_IMPORTER_DIR}"
  
  SetOutPath "${EFG2_IMPORTER_DIR}"
  ;File "${EFG2_PRODUCT_SOURCE_DIR}\schema\*.*"
  File "${EFG_INSTALLER_SMALL_ICON}"
  File "${EFG_INSTALLER_BIG_ICON}"
  ;File "${EFG2_PRODUCT_SOURCE_DIR}\efg2.xml"
  
  SetOutPath "${EFG2_IMPORTER_DIR}\docs"
 ; File "${EFG2_PRODUCT_SOURCE_DIR}\docs\*.html"
  ;File "${EFG2_PRODUCT_SOURCE_DIR}\docs\*.jpg"
  
  ;copy to lib
  SetOutPath "${EFG2_IMPORTER_DIR}\${EFG2_IMPORTER_LIB}"
  File "${EFG2_PRODUCT_SOURCE_DIR}\${EFG2_IMPORTER_EXECUTABLE}"
  File "${EFG2_PRODUCT_SOURCE_DIR}\${EFG2_ABOUT_EXECUTABLE}"
  File "${EFG2_PRODUCT_SOURCE_LIB}\*.jar"
   ;RE-VISIT THIS NOT SURE ABOUT THIS FOR NOW
  File "${EFG2_PRODUCT_SOURCE_LIB}\tomcatusers.bat"
  
  SetOutPath "${EFG2_IMPORTER_DIR}\properties"
  File "${EFG2_PRODUCT_SOURCE_LIB}\properties\*.properties"
 
 
  ;copy icons
  SetOutPath "${EFG2_IMPORTER_DIR}\${EFG2_IMPORTER_LIB}\icons"
  File "${EFG2_PRODUCT_SOURCE_LIB}\icons\*.*"

  ;copy help files
  SetOutPath "${EFG2_IMPORTER_DIR}\${EFG2_IMPORTER_LIB}\help"
  File "${EFG2_PRODUCT_SOURCE_LIB}\help\*.*"
  
  ;copy smaples
  SetOutPath "${EFG2_IMPORTER_DIR}\${EFG2_IMPORTER_LIB}\samples"
  File "${EFG2_PRODUCT_SOURCE_LIB}\data\IpoTest.csv"


FunctionEnd


Function CopyFiles
        Call copyImporterFiles
        Call copyToWebApps
        clearErrors
        
        ;invoke batch file to copy stuff to web application
        ;needs enhancement
        ;FIX LATER
        /*SetOutPath "$TEMP"
        SetOverwrite try
        File "${EFG2_TOMCAT_USER_EXEC}"
        File "${EFG2_PRODUCT_SOURCE_DIR}\${EFG2_IMPORTER_LIB}\*.jar"
        File "${EFG2_PRODUCT_SOURCE_DIR}\${EFG2_IMPORTER_LIB}\properties\*.properties"
        ExecWait '${EFG2_TOMCAT_USER_EXEC} "$CATALINA_HOME"'
        sleep 500
        Delete "$TEMP\${EFG2_TOMCAT_USER_BATCH}"
        Delete "$TEMP\*.jar"
        Delete "$TEMP\*.properties"
        Strcmp $isTomcatRunning "true" 0 end
        ExecWait 'cmd /C net start "$CURRENT_TOMCAT"'
        Sleep 5000
        clearErrors
        end:*/
FunctionEnd

Function copyToWebApps
    SetOutPath "$CATALINA_HOME\conf\Catalina\localhost" 
    SetOverwrite try
    File "${EFG2_PRODUCT_SOURCE_DIR}\efg2.xml"
        
    ;copy mysql driver
    SetOutPath "$CATALINA_HOME\common\lib" 
    SetOverwrite try
    File "${EFG2_PRODUCT_SOURCE_DIR}\${EFG2_IMPORTER_LIB}\mysqldriver.jar"
    
    ;Copy war file to webapps
    
    ;FIX FOR NOW..NEEDS ENCHANCEMENT.
    SetOutPath "$WEB_APPS_LOCATION\efg2"
    SetOverwrite try
  ;  File "${EFG2_PRODUCT_SOURCE_DIR}\efg2\index.html"
  
    SetOutPath "$WEB_APPS_LOCATION\efg2\EFGImages"
    SetOverwrite try
    ;File "${EFG2_PRODUCT_SOURCE_DIR}\efg2\index.html"
    
    SetOutPath "$WEB_APPS_LOCATION\efg2\WEB-INF"
    SetOverwrite try
    File "${EFG2_PRODUCT_SOURCE_DIR}\efgRDB\etc\web.xml"
    
    ;FIX ME use program to install this
    /*SetOutPath "$CATALINA_HOME\conf"
    SetOverwrite try
    File "${EFG2_PRODUCT_SOURCE_DIR}\tomcat-users.xml"*/
    ;add datahandling capability
    
    ;execute java to write/edit tomcat-users file
FunctionEnd

/**
* Prompts user to indicate how(for current user or for all users of this system) they will want the application installed 
*/
Function PageInitInstall
   Call setUpLogFile
   !insertmacro MUI_INSTALLOPTIONS_WRITE "${EFG2_DataImporter_INI}" "Field 1" "Text" "Indicate whether you want to install the application for all users or for the current user only. Select the operation you want to perform and click Next to continue."
   !insertmacro MUI_INSTALLOPTIONS_WRITE "${EFG2_DataImporter_INI}" "Field 2" "Text" "For All Users"
   !insertmacro MUI_INSTALLOPTIONS_WRITE "${EFG2_DataImporter_INI}" "Field 3" "Text" "For Current User Only"
   !insertmacro MUI_HEADER_TEXT "Installation Option" "Choose an option to install"
   StrCpy $R0 "2"
   
   !insertmacro MUI_INSTALLOPTIONS_DISPLAY "${EFG2_DataImporter_INI}"
FunctionEnd
/**
* This function is automatically called by System after exiting the PageInitInstall function
* Does the necessary house keeping after users choice in the above function
*/
Function PageLeaveInitInstall
 !insertmacro MUI_INSTALLOPTIONS_READ $R1 "${EFG2_DataImporter_INI}" "Field 2" "State"
 StrCmp $R0 "2" 0 Next
 StrCmp $R1 "1" all current

all:
;StrCpy $WriteEnvStr_RegKey "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"
 SetShellVarContext all
Goto end

current:
 SetShellVarContext current
 ;StrCpy $WriteEnvStr_RegKey "Environment"
Goto end

 Next:
 MessageBox MB_OK|MB_TOPMOST "You need to make a selection.Exiting installation"
 Quit

end:
FunctionEnd






/**
*Checks to see if application is already installed.
* If it is, the version is checked to see if it is the same as the current version
* The appropriate prompts are issued to user on what to do next.
*/ 
Function PageReinstall
 ;get the display version if any
 /*Pop $R0
 Push $R0
 ReadRegStr $R0 HKLM "${EFG2_PRODUCT_UNINST_KEY}" "${DISPLAY_VERSION}"
 ;if it exists goto set shctx to all. if not check hkcu
 StrCmp $R0 "" checkHKCU 
 SetShellVarContext all
 ;compare the current version to the installers version
 Goto setUpInstall
 
 ;read hkcu registry
  checkHKCU:
    Pop $R0
    Push $R0
    ReadRegStr $R0 HKCU "${EFG2_PRODUCT_UNINST_KEY}" "${DISPLAY_VERSION}"
    StrCmp $R0 ""  installMessage  ;This application is not installed on the 
                                    ;current computer so go ahead and install it
   SetShellVarContext current
   Goto setUpInstall
   
   installMessage:
   ;means it does not exists on your system install it
    Goto install
   ; 0       Versions are equal
;   1       Version 1 is newer
;   2       Version 2 is newer
    setUpInstall:
     StrCpy "$R9" "$R0"
     ${VersionCheckNew} "$R9" "${EFG2_SHORT_VERSION_NUMBER}" "$R0"
     IntCmp $R0 "1" versionRoNewer versionEqual versionRoOlder

     ; Installed version is older than the current version
     versionRoNewer:
        StrCpy $R0 "1"
        Goto reinst_start

    ; Installed version is newer than current version
    versionRoOlder:
        StrCpy $R0 "1"
        Goto reinst_start
 
 ;installed version is same as current version
  versionEqual:
    !insertmacro MUI_INSTALLOPTIONS_WRITE "${EFG2_DataImporter_INI}" "Field 1" "Text" "$(^Name) is already installed. Select the operation you want to perform and click Next to continue."
    !insertmacro MUI_INSTALLOPTIONS_WRITE "${EFG2_DataImporter_INI}" "Field 2" "Text" "Add/Reinstall components"
    !insertmacro MUI_INSTALLOPTIONS_WRITE "${EFG2_DataImporter_INI}" "Field 3" "Text" "Uninstall EFGDataImporter"
    !insertmacro MUI_HEADER_TEXT "Already Installed" "Choose the maintenance option to perform."
    StrCpy $R0 "2"
  
  reinst_start:
   !insertmacro MUI_INSTALLOPTIONS_DISPLAY "${EFG2_DataImporter_INI}"

Continue:*/
FunctionEnd

/**
* System calls this function after it leaves the reinstall page. 
*Function does necessary work required to exit PageReinstall function
*/
Function PageLeaveReinstall
 !insertmacro MUI_INSTALLOPTIONS_READ $R1 "${EFG2_DataImporter_INI}" "Field 2" "State"
 StrCmp $R0 "1" 0 Next1
 StrCmp $R1 "1" reinst_done

 Next1:
  StrCmp $R0 "2" 0 hideWindow
  StrCmp $R1 "1" reinst_done reinst_uninstall

 reinst_uninstall:
  ReadRegStr $R1 ${EFG2_PRODUCT_UNINST_ROOT_KEY} "${EFG2_PRODUCT_UNINST_KEY}" "${UNINSTALL_STRING}"

 ;Run uninstaller
 hideWindow:
  ;HideWindow
  ClearErrors
  MessageBox MB_YESNO|MB_TOPMOST "Uninstalling this application will cause you to lose all$\r\
                      of the keys/configuration that you have deployed to the web application.$\r\
                      Do you still want to remove it?" IDNO reinst_done
 Call removeApplications
 SetAutoClose true

 IfErrors no_remove_uninstaller
  MessageBox MB_OK|MB_TOPMOST "An error occured while trying to uninstall  $(^Name)" 
 SetAutoClose true

  no_remove_uninstaller:
  MessageBox MB_OK|MB_TOPMOST "The $(^Name) has been uninstalled successfully" 
  Quit

  reinst_done:
FunctionEnd



;----------------------------------------
; Install and Uninstall Macros
;
;----------------------------------------

/**
* Function calls to delete all files associated with installation
*/
!macro removeApplications UN
Function ${UN}removeApplications
Call ${UN}removeEFGDataImporter
Call ${UN}removeEFGDataImporterWebapps 
Call ${UN}efgDeletes
FunctionEnd
!macroend
!insertmacro removeApplications ""
!insertmacro removeApplications "un."
/**
*Called to gracefully delete installed files and registry information
*/
!macro efgDeletes UN
Function ${UN}efgDeletes
 DeleteRegKey ${EFG2_PRODUCT_UNINST_ROOT_KEY} "${EFG2_PRODUCT_UNINST_KEY}"
 DeleteRegKey ${EFG2_PRODUCT_UNINST_ROOT_KEY} "${EFG2_PRODUCT_UNINST}"
 DeleteRegKey ${EFG2_PRODUCT_UNINST_ROOT_KEY} "${EFG2_PRODUCT_DIR_REG}"
FunctionEnd
!macroend
!insertmacro efgDeletes ""
!insertmacro efgDeletes "un."

/**
* Called for the removal of EFGDataImporter application
*/
!macro startEFGDataImporterRemoval UN
Function ${UN}startEFGDataImporterRemoval
;read global variables
 Pop $R0
 Push $R0
 ClearErrors
 
 ReadRegStr $R0 HKLM "${EFG2_PRODUCT_UNINST_KEY}" "EFG2HOME" 
 StrCmp $R0 "" EFGHKCU finishEFGHKLM 

 finishEFGHKLM:
  StrCpy $INSTDIR $R0
  SetShellVarContext all 
  
  Goto removeRO
;see if it was installed in HKCU with Tomcat 4
 EFGHKCU:
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${EFG2_PRODUCT_UNINST_KEY}" "EFG2HOME" 
  StrCmp $R0 "" removeRO finishEFGHKCU
  
 finishEFGHKCU:
  StrCpy $INSTDIR $R0
  SetShellVarContext current
  ;StrCpy $WriteEnvStr_RegKey "Environment"
  Goto removeRO  
  
 removeRO:
  Pop $R0 
  Goto Finish
Finish:
FunctionEnd
!macroend
!insertmacro startEFGDataImporterRemoval ""
!insertmacro startEFGDataImporterRemoval "un."

/**
* Initiliazes Tomcat
*/
!macro initTomcatCheck UN
Function ${UN}initTomcatCheck
 Pop $R0
 Pop $R1
 Push $R0
 ClearErrors
 ReadRegStr $R0 HKLM "${TOMCAT5_KEY}" "InstallPath"
 StrCmp $R0 "" checkHKCU5 finishTomcat5
  
 finishTomcat5:
  ;MessageBox MB_OK "Finish HKLM tomcat5"
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $WEB_APPS_LOCATION "$R0\webapps"
  StrCpy $CURRENT_TOMCAT "${TOMCAT5_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT5_KEY}" 
  StrCpy $R0 "1"
  Goto Continue

 checkHKCU5:
 ;MessageBox MB_OK "check HKCU4"
  Pop $R0
  Push $R0
  ClearErrors
  ReadRegStr $R0 HKCU "${TOMCAT5_KEY}" "InstallPath"
  StrCmp $R0 "" checkTomcat4 0
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $WEB_APPS_LOCATION "$R0\webapps"
  StrCpy $CURRENT_TOMCAT "${TOMCAT5_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT5_KEY}" 
   StrCpy $R0 "1"
  Goto Continue

 checkTomcat4:
  Pop $R0
  Push $R0
  clearErrors
  ReadRegStr $R0 HKLM "${TOMCAT4_KEY}" ""   
  StrCmp $R0 "" checkHKCU4 finishTomcat4
  
finishTomcat4:
  StrCpy $R0 "1"
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $WEB_APPS_LOCATION "$R0\webapps"
  StrCpy $CURRENT_TOMCAT "${TOMCAT4_SERVICES_NAME}"
  StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT4_KEY}" 
  Goto Continue
  
 checkHKCU4:
 
   Pop $R0
   Push $R0
   ClearErrors
   ReadRegStr $R0 HKCU "${TOMCAT4_KEY}" ""
   StrCmp $R0 "" CheckCatalinaHome 0
   StrCpy $CATALINA_HOME "$R0"
   StrCpy $WEB_APPS_LOCATION "$R0\webapps"
   StrCpy $CURRENT_TOMCAT "${TOMCAT4_SERVICES_NAME}"
   StrCpy $CURRENT_TOMCAT_KEY "${TOMCAT4_KEY}" 
   StrCpy $R0 "1"
   Goto Continue
 
  CheckCatalinaHome:
    Pop $R0
    Push $R0
    ClearErrors
    
    ;check environment variables
    ReadEnvStr $R0 CATALINA_HOME
    IfErrors 0 catalina
    Pop $R0
    Push $R0
    ClearErrors
    StrCpy $R0 ""
    StrCpy $R1 ""
    Goto Continue

  catalina:
    Pop $R1
    Push $R1
    ClearErrors
    StrCpy $R1 "1"
 Continue:
FunctionEnd
!macroend
!insertmacro initTomcatCheck ""
!insertmacro initTomcatCheck "un."

/**
* Initiliazes Tomcat
*/
/*
*/
/**
*Start the removal of EFGDataImporter web application
*/
!macro removeEFGDataStuff UN
Function ${UN}removeEFGDataStuff


  RMDIR /r /REBOOTOK "$SMPROGRAMS\EFGDataImporter${EFG2_SHORT_VERSION_NUMBER}"
  Delete "$DESKTOP\${EFG2_IMPORTER_LINK_NAME}.lnk"
  RMDIR /r /REBOOTOK $INSTDIR
  clearErrors

  Delete "$CATALINA_HOME\conf\Catalina\localhost\efg2.xml" 
 
  ClearErrors
  Delete "$CATALINA_HOME\common\lib\mysqldriver.jar" 
 
  ClearErrors
  ExecWait '"${EFG2_RENAME_TOMCAT_USERS_BATCH}" "$CATALINA_HOME\${EFG2_TOMCAT_USERS_OLD_FILE}" "$CATALINA_HOME\${EFG2_TOMCAT_USERS_CURRENT_FILE}"'
  sleep 5000
  ;Rename "$CATALINA_HOME\conf\tomcat-users.xml_EFG_old" "$CATALINA_HOME\conf\tomcat-users.xml"
  ClearErrors
  
   ; RMDIR /r /REBOOTOK "$WEB_APPS_LOCATION\keys" 

    ;execute java to write/edit tomcat-users file
    ;Restore Tomcat users file
  ;Delete "$WEB_APPS_LOCATION\${WEB_APPS_WAR}"
 
 FunctionEnd
!macroend
!insertmacro removeEFGDataStuff ""
!insertmacro removeEFGDataStuff "un."

/**
*Start the removal of EFGDataImporter web application
*/
!macro removeEFGDataImporterWebapps UN
Function ${UN}removeEFGDataImporterWebapps

  Call ${UN}initTomcatCheck 
  StrCmp $R0 "" end ; no tomcat on the system
  StrCmp $R1 "1" localTomcat ; tomcat installed locally and not in registry
  StrCmp $R0 "1" foundReg  ;Tomcat is information is in registry 
 
 localTomcat:
  StrCpy $CATALINA_HOME "$R0"
  StrCpy $WEB_APPS_LOCATION "$R0\webapps"
  StrCpy $CURRENT_TOMCAT "$CATALINA_HOME\bin"
  Push $CURRENT_TOMCAT
  Push "bin" 
  Push "<"
  Call ${UN}StrLoc
  Pop $R0
  StrCmp $R0 "" end 0
  
  ExecWait '"$CURRENT_TOMCAT\shutdown.bat"'
  sleep 5000
  Call ${UN}removeEFGDataStuff
  ExecWait '"$CURRENT_TOMCAT\startup.bat"'
  sleep 5000
  Goto end
  
  foundReg: 
   ExecWait 'cmd /C net stop "$CURRENT_TOMCAT"'
   sleep 5000
   Call ${UN}removeEFGDataStuff
  
   ExecWait 'cmd /C net start "$CURRENT_TOMCAT"'  
   sleep 5000
  end:
 FunctionEnd
!macroend
!insertmacro removeEFGDataImporterWebapps ""
!insertmacro removeEFGDataImporterWebapps "un."


/**
*Start the removal of EFGDataImporter application
*/
!macro removeEFGDataImporter UN
Function ${UN}removeEFGDataImporter
 Call ${UN}startEFGDataImporterRemoval
FunctionEnd
!macroend
!insertmacro removeEFGDataImporter ""
!insertmacro removeEFGDataImporter "un."



  
# TextLog.nsh
# Written by Mike Schinkel [http://www.mikeschinkel.com/blog/]
# 12/23/2005
#
# 9/15/2006 Added seek to end of file so it's not overwritten on each call
#           Added LogMsg so file is not locked between writes

!AddIncludeDir ..\headers
!include "TextLog.nsh"
;!include "FileFunc.nsh" 

OutFile "test.exe"
 
# This so the log will be written to same dir as the test file. 
# NOT recommended for actual installation files.
InstallDir "$EXEDIR"  
 
Page custom OnCustom AfterCustom
Page components
Page instfiles
Page custom ShowLog

Function .onInit
    ${LogSetFileName} "$INSTDIR\MyInstallLog.txt"
    ${LogSetOn}
    Call writeTime
    ${LogText} "In .onInit"
FunctionEnd
 
Function OnCustom
    ${LogText} "In Function OnCustom"
FunctionEnd
 
Function AfterCustom
    ${LogText} "In Function AfterCustom"
FunctionEnd



Section one
    ${LogText} "In Section One"
SectionEnd
 
Section two
    ${LogText} "In Section Two"
SectionEnd
 
Section three
    ${LogText} "In Section Three"
SectionEnd
 
Function ShowLog
    ${LogText} "In Function ShowLog"
    MessageBox MB_OK $INSTDIR;
    ExecShell "open" "$INSTDIR\MyInstallLog.txt"
FunctionEnd
 
Section "-CleanUp"
    ${LogText} "In Section -CleanUp"
    ${LogSetOff}
SectionEnd
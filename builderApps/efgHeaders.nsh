!include "Sections.nsh"
!include "LogicLib.nsh"
!include "WordFunc.nsh"

;Global header variables needed by most EFG applications
!include "efgCommonHeaders.nsh"

;icons needed to run application and where they can be found
!include "iconHeaders.nsh"

;Contains external files to be run by client
; and their location
!include "downloadHeaders.nsh"

;Contains header information for EFG2
!include "efg2Headers.nsh"
;Contains version information for the 
;current installation
!include "efg2VersionHeaders.nsh"
;Contains some global variables needed to run
;most EFG applications
!include "efg2CommonVariables.nsh"

!include "local_MUI_Headers.nsh"
!addincludedir .
; a word replace macro
!insertmacro WordReplace
;general functions 
!include "commonFunctions.nsh"

;specific efg2 functions
!include "efg2Functions.nsh"





  
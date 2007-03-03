;--------------------------------
;Includes

  !include "MUI.nsh"
  !include Sections.nsh

;--------------------------------
;General

  Name "partial uninstaller"
  OutFile "partial_uninstall_example.exe"
  
  Var /Global UnselCount /* counts unselected components in the uninstaller.*/
  Var /Global BoxCount /* Counts the components available in the uninstaller.*/

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !define MUI_FINISHPAGE_RUN
  !define MUI_FINISHPAGE_RUN_FUNCTION "openINSTDIR"
  !define MUI_FINISHPAGE_RUN_TEXT "open the installation directory now"
  !define MUI_FINISHPAGE_TEXT "The partial uninstaller example is now installed on your computer. Open the installation directory to test it."
  !insertmacro MUI_PAGE_FINISH

  !insertmacro MUI_UNPAGE_COMPONENTS
  !insertmacro MUI_UNPAGE_INSTFILES
  
  
;--------------------------------
;Languages
 
 !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "" sec_vikings
  SetOutPath "$INSTDIR"
  FileOpen $0 $INSTDIR\vikings.txt w
  FileWrite $0 "Spam spam spam spam. Lovely spam! Wonderful spam! Spam spa-a-a-a-a-am spam spa-a-a-a-a-am spam. Lovely spam! Lovely spam! Lovely spam! Lovely spam! Lovely spam! Spam spam spam spam!"
  FileClose $0
  ;Create uninstaller
  SetOutPath "$INSTDIR"
  WriteUninstaller "$INSTDIR\partial-uninstall.exe"
Sectionend

Section man sec_man
  SetOutPath "$INSTDIR"
  FileOpen $0 $INSTDIR\man.txt w
  FileWrite $0 "You sit here, dear"
  FileClose $0
Sectionend

Section wife sec_wife
  SetOutPath "$INSTDIR"
  FileOpen $0 $INSTDIR\wife.txt w
  FileWrite $0 "All right"
  FileClose $0
Sectionend

Section waitress sec_waitress
  SetOutPath "$INSTDIR"
  FileOpen $0 $INSTDIR\waitress.txt w
  FileWrite $0 "Morning!"
  FileClose $0
Sectionend

;--------------------------------
;Uninstaller Sections

Section un.man un_man
  Delete $INSTDIR\man.txt
Sectionend

Section un.wife un_wife
  Delete $INSTDIR\wife.txt
Sectionend

Section un.waitress un_waitress
  Delete $INSTDIR\waitress.txt
Sectionend

;Uninstaller Section for complete removal

Section "un.All" unAll
  StrCmp $UnselCount "0" 0 end
    Delete $INSTDIR\vikings.txt
    Delete "$INSTDIR\partial-uninstall.exe"
  end:
SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecHurdyGurdy ${LANG_ENGLISH} "The HurdyGurdy VST instrument will run in Demo mode until a license code is purchased."
  LangString DESC_SecHurdyLE ${LANG_ENGLISH} "The HurdyGurdyLE is a feature reduced free version of our HurdyGurdy VST instrument."

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${sec_man} "install man"
  !insertmacro MUI_DESCRIPTION_TEXT ${sec_wife} "install wife"
  !insertmacro MUI_DESCRIPTION_TEXT ${sec_waitress} "install waitress"
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

  !insertmacro MUI_UNFUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${un_man} "remove man"
  !insertmacro MUI_DESCRIPTION_TEXT ${un_wife} "remove wife"
  !insertmacro MUI_DESCRIPTION_TEXT ${un_waitress} "remove waitress"
  !insertmacro MUI_UNFUNCTION_DESCRIPTION_END

;--------------------------------
; the deinstaller components page shows only sections that are present on the users system.
Function un.onInit

;hide section unAll
  SectionSetText ${unAll} ""
;
  StrCpy $UnselCount "0"
  StrCpy $BoxCount "0"
  
  IfFileExists $INSTDIR\man.txt isMan noMan
  isMan:
    IntOp $BoxCount $BoxCount + 1
    Goto checkWife
  noMan:
    SectionSetText ${un_man} ""
    SectionSetFlags ${un_man} 0
  
  checkWife:
  IfFileExists $INSTDIR\wife.txt isWife noWife
  isWife:
    IntOp $BoxCount $BoxCount + 1
    Goto checkWaitress
  noWife:
    SectionSetText ${un_wife} ""
    SectionSetFlags ${un_wife} 0

  checkWaitress:
  IfFileExists $INSTDIR\waitress.txt isWaitress noWaitress
  isWaitress:
    IntOp $BoxCount $BoxCount + 1
    Goto end
  noWaitress:
    SectionSetText ${un_waitress} ""
    SectionSetFlags ${un_waitress} 0

  end:
FunctionEnd

;--------------------------------*/
; alternative implementation that shows the absent sections greyed out and deselected.
/*
Function un.onInit

;hide section unAll
  SectionSetText ${unAll} ""
;
  StrCpy $UnselCount "0"
  StrCpy $BoxCount "0"

  IfFileExists $INSTDIR\man.txt isMan noMan
  isMan:
    IntOp $BoxCount $BoxCount + 1
    Goto checkWife
  noMan:
    SectionSetFlags ${un_man} ${SF_RO}

  checkWife:
  IfFileExists $INSTDIR\wife.txt isWife noWife
  isWife:
    IntOp $BoxCount $BoxCount + 1
    Goto checkWaitress
  noWife:
    SectionSetFlags ${un_wife} ${SF_RO}

  checkWaitress:
  IfFileExists $INSTDIR\waitress.txt isWaitress noWaitress
  isWaitress:
    IntOp $BoxCount $BoxCount + 1
    Goto end
  noWaitress:
    SectionSetFlags ${un_waitress} ${SF_RO}
  end:
FunctionEnd
*/
;--------------------------------
Function un.onSelChange

  StrCpy $UnselCount $BoxCount

  SectionGetFlags "${un_man}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0

  SectionGetFlags "${un_wife}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0

  SectionGetFlags "${un_waitress}" $R0
  IntOp $R0 $R0 & ${SF_SELECTED}
  IntOp $UnselCount $UnselCount - $R0
  
FunctionEnd
;--------------------------------
Function openINSTDIR
  ExecShell "open" "$INSTDIR"
FunctionEnd

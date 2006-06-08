; HM NIS Edit Wizard helper defines
; MUI Headers------
!include "efgHeaders.nsh"
!include "MUI.nsh"


; MUI Settings
;Include after local definitions
;Must be called after iconsHeaders.nsh
!define MUI_ICON "${EFG_INSTALLER_SMALL_ICON}"

!define MUI_UNICON "${EFG_INSTALLER_SMALL_ICON}"
;Title to display on the top of the page.
!define MUI_WELCOMEPAGE_TITLE "Installation of  ${EFG2_PRODUCT_NAME}"

;Extra space for the title area.
;!define MUI_WELCOMEPAGE_TITLE_3LINES

;Text to display on welcome page
/*!define MUI_WELCOMEPAGE_TEXT "This application installs updates for ${PREVIOUS_PRODUCT_NAME}\r\n\r\n\
                             If you do not already have ${PREVIOUS_PRODUCT_NAME} installed on\r\n\
                             your system then, kindly exit this installation.\r\n\r\n\
                             If you have not already read the release notes that\r\n\
                             come with this installation then kindly do so.\r\n\
                             Release notes (${RELEASE_NOTES_FILE}) can be found \
                             in the same directory as this executable or at our \r\n\
                             web site:\r\n\
                             http://efg.cs.umb.edu/EFGsoftware/\r${RELEASE_NOTES_FILE}\r\n"
*/
; macro for welcome page
!insertmacro MUI_PAGE_WELCOME 

; License page
!insertmacro MUI_PAGE_LICENSE "${EFG2_PRODUCT_SOURCE_DIR}\builderApps\license\license.txt"

; abort warning message
!define MUI_ABORTWARNING

; bitmap for header of page
!define MUI_HEADERIMAGE_BITMAP "${EFG2_INSTALLER_SMALL_ICON}"

;small description page
!define MUI_COMPONENTSPAGE_SMALLDESC


;Check to see if this application is already installed
Page custom PageReinstall PageLeaveReinstall
Page custom PageInitInstall PageLeaveInitInstall


!insertmacro MUI_PAGE_DIRECTORY
;macro needed for custom page calls to work
!insertmacro MUI_PAGE_INSTFILES
;"Select to View Read Me file" 
; Finish page
;"Select to View Read Me file" 
!define MUI_FINISHPAGE_SHOWREADME ${IMPORTER_DIR}\docs\manual.html

;read me message
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Select to View Read Me file(recommended)" 
;Don't auto close after installation
!define MUI_FINISHPAGE_NOAUTOCLOSE
!insertmacro MUI_PAGE_FINISH

;Uninstall instructions
!insertmacro MUI_UNPAGE_WELCOME

!insertmacro MUI_UNPAGE_CONFIRM

!insertmacro MUI_UNPAGE_INSTFILES

;Don't close the page automatically after installation
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
; Language files
!insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Reserve Files
;These files should be inserted before other files in the data block
 ReserveFile "${EFG2_DataImporter_INI}"
 !insertmacro MUI_RESERVEFILE_INSTALLOPTIONS


Section ""

SectionEnd








 
/**
*$Name$
*$Id$
* Header file for all images and icons to be used by the installer.
* Make sure to set the ICON_SOURCE_DIR variable to point to where
*all your icons can be found.
*/
/* Icons needed to install appplication*/
!define "efgHeaders.nsi"
!define EFG_SMALL_ICON "EFGKeyConfig32x32.ico"
!define EFG_BIG_ICON "EFGKeyConfig48x48.ico"
!define ICONS_SOURCE_DIR "c:\downloads\icons"

!define EFG_INSTALLER_SMALL_ICON "${ICONS_SOURCE_DIR}\${EFG_SMALL_ICON}"
!define EFG_INSTALLER_BIG_ICON "${ICONS_SOURCE_DIR}\${EFG_BIG_ICON}"

Name "Test"
OutFile "Test.exe"

Section ""

SectionEnd

























  
; HM NIS Edit Wizard helper defines
;$Id: efg2VersionHeaders.nsh,v 1.4 2007/03/07 19:56:19 kasiedu Exp $
;$Name:  $
;include local headers
;Version information on properties tab
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

Function writeProductVersion
    ${LogText} "Writing variables in '$INSTDIR\${EFG2_RESOURCE_HOME}\properties\version.properties'"

   ;on update also update this file
    FileOpen $9 "$INSTDIR\${EFG2_RESOURCE_HOME}\properties\version.properties" w  ;Opens a Empty File an fills it
    FileWrite $9 "efg2.version=${ProductVersion} $\r$\n"   
    FileClose $9   
 ${LogText} "Done writing variables in '$INSTDIR\${EFG2_RESOURCE_HOME}\properties\version.properties'"

FunctionEnd
  
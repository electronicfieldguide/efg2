; HM NIS Edit Wizard helper defines
;$Id$
;$Name$
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

   ;on update also update this file
    FileOpen $9 "$INSTDIR\${EFG2_RESOURCE_HOME}\properties\version.properties" w  ;Opens a Empty File an fills it
    FileWrite $9 "efg2.version=${ProductVersion} $\r$\n"   
    FileClose $9   

FunctionEnd

  
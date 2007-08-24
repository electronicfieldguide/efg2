@echo off
REM $Id$
REM You will have to set the NSIS_HOME environment variable to point to where NSIS was installed
REM or change the NSIS_HOME variable set  in this file
REM echo. "Setting the classpath"

if "%NSIS_HOME%" == "" goto next
goto execute

:next
echo setting NSIS_HOME to C:\Program Files\NSIS
SET NSIS_HOME=C:\Program Files\NSIS
goto execute

:execute
echo Making "About.exe"
"%NSIS_HOME%\makensis.exe" About.nsi

echo "EFG2DataImporter.exe"
"%NSIS_HOME%\makensis.exe" EFG2DataImporter.nsi



echo "making EFG2 Installer"
"%NSIS_HOME%\makensis.exe" EFG2DataImporterInstaller.nsi
SET NSIS_HOME=""
echo "Done!!!"




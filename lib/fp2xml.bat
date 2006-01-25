@echo off

set EFGHOME=%CD%
set EFG_DATA=%CD%\data
echo %EFG_DATA%
set LOG4J=%CD%\log4j-1.2.8.jar
set IMPORT_JAR=%CD%\rdbImport.jar
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
set CP=%CP%;%LOG4J%;%IMPORT_JAR%;%CD%
set CLASSPATH=%CP%

SET EFG_TEMPLATE=%CD%\BaseMetaFile.fp5
copy /Y RDBprops.properties %EFG_DATA%
copy /Y log4j_rdb.properties %EFG_DATA%
cd %EFG_DATA%
IF EXIST log4j.properties DEL /Q log4j.properties
rename log4j_rdb.properties log4j.properties
SET DATAFILE_LIST=_precopy.data
set FILE_EXT=.fp5



FOR %%F IN (*%FILE_EXT%) DO @ECHO %%~nF,Data,Info>> %DATAFILE_LIST%
FOR /F "tokens=1,2,3 delims=," %%i IN (%DATAFILE_LIST%) DO REN %%i%FILE_EXT% %%i%%j%FILE_EXT% > NUL

FOR /F "tokens=1,2,3 delims=," %%i in (%DATAFILE_LIST%) DO COPY /Y %EFG_TEMPLATE% %%i%%k%FILE_EXT% > NUL

FOR %%i IN (*%FILE_EXT%) DO START "C:\Program Files\FileMaker\FileMaker Pro 6\FileMaker Pro.exe" "%%i"

ping -n 6 127.0.0.1>NUL

FOR /F "tokens=1,2,3 delims=," %%i in (%DATAFILE_LIST%) DO java project/efg/Import/EFGDataPrepareTool %%i%%j %%i%%k

:EXIT
set CLASSPATH=%_CP%
cd %EFGHOME%
@echo off

REM $Log$
REM Revision 1.1  2006/01/25 21:03:44  kasiedu
REM Initial revision
REM

REM
@echo on

@echo off

set EFGHOME=%CD%
set EFG_DATA=%CD%\data
set LOG4J=%CD%\log4j-1.2.8.jar
set MYSQL_DRIVER=%CD%\mysqldriver.jar
set IMPORT_JAR=%CD%\rdbImport.jar
set _CP=%CLASSPATH%
set CP=%CLASSPATH%
set POINT=.
set CP=%CP%;%LOG4J%;%IMPORT_JAR%;%CD%;%POINT%;%MYSQL_DRIVER%
set CLASSPATH=%CP%

copy /Y RDBprops.properties %EFG_DATA%
copy /Y log4j_rdb.properties %EFG_DATA%
cd %EFG_DATA%

IF EXIST log4j.properties DEL /Q log4j.properties

rename log4j_rdb.properties log4j.properties
SET FILE_EXT=.xml
SET DATAFILE_LIST=xmlmysql.txt

IF EXIST %DATAFILE_LIST% DEL /Q %DATAFILE_LIST% > NUL

ECHO.@ECHO OFF>%TEMP%\EFGTEMP.BAT
ECHO.SET X=%%1>>%TEMP%\EFGTEMP.BAT
ECHO.SET Y=%%X:~0,-4%%>>%TEMP%\EFGTEMP.BAT
ECHO.ECHO.%%Y%%,Data,Info^>^>%%2>>%TEMP%\EFGTEMP.BAT

FOR %%F in (*Data.xml) DO call %TEMP%\EFGTEMP.BAT %%~nF %CD%\%DATAFILE_LIST%

DEL /Q %TEMP%\EFGTEMP.BAT > NUL



FOR /F "tokens=1,2,3 delims=," %%i IN (%DATAFILE_LIST%) DO java -classpath "%CLASSPATH%" project/efg/Import/EFGImportTool 2 %%i%%j%FILE_EXT% %%i%%k%FILE_EXT%

DEL %DATAFILE_LIST%
ECHO.
ECHO. Done importing data. See log file (data/efg.log) for any errors!!!
set CLASSPATH=%_CP%
cd %EFGHOME%
@echo on
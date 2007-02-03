Net Stop "Apache Tomcat"
if errorlevel==1 goto clearLogs1
@ping 127.0.0.1 -n 2 -w 1000 > nul
@ping 127.0.0.1 -n %1% -w 1000> nul
goto clearLogs2




:clearLogs1
echo y | DEL "%CATALINA_HOME%"\logs\*.*
goto end

:clearLogs2
echo y | DEL "%CATALINA_HOME%"\logs\*.*
@ping 127.0.0.1 -n 2 -w 1000 > nul
@ping 127.0.0.1 -n %1% -w 1000> nul

:end
@ping 127.0.0.1 -n 2 -w 1000 > nul
@ping 127.0.0.1 -n %1% -w 1000> nul
Net Start "Apache Tomcat"

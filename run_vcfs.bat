@echo off
echo ===================================================
echo  Virtual Career Fair System (VCFS) - Group 9
echo  Project Manager ^& Lead Developer: Zaid
echo ===================================================
echo.
echo Compiling project...
if not exist bin mkdir bin
cd src\main\java
dir /s /b *.java > sources_raw.txt
type nul > sources.txt
for /f "delims=" %%i in (sources_raw.txt) do echo "%%i" >> sources.txt
javac -d ..\..\..\bin @sources.txt
set JAVAC_ERROR=%ERRORLEVEL%
cd ..\..\..
if %JAVAC_ERROR% NEQ 0 (
    echo Compilation failed! Please check the code.
    pause
    exit /b %ERRORLEVEL%
)
echo Compilation successful!
echo.
echo Starting application...
java -cp bin vcfs.App
pause
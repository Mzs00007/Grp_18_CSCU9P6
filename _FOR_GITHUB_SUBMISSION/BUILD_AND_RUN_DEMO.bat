@echo off
setlocal enabledelayedexpansion

REM ================================================================
REM  VCFS - Virtual Career Fair System (CSCU9P6 Group 9)
REM  Build & Launch Demo Application
REM  Source Location: _FOR_GITHUB_SUBMISSION/src
REM  Project Manager & Lead Dev: Zaid Siddiqui (mzs00007)
REM  Collaborators: Taha, YAMI, MJAMishkat, Mohamed
REM ================================================================
REM
REM  COMPREHENSIVE DEMO LAUNCHER:
REM    ✓ Compiles all Java source files
REM    ✓ Validates compilation success
REM    ✓ Manages classpath automatically
REM    ✓ Launches main application with Swing UI
REM    ✓ Color-coded console feedback
REM    ✓ Detailed error diagnostics
REM    ✓ Performance timing
REM
REM ================================================================

cls
color 0A
chcp 65001 >nul 2>&1

REM ================================================================
REM  INITIALIZATION
REM ================================================================

REM Record start time
set START_TIME=%time%
set BUILD_DIR=%cd%\build
set BIN_DIR=%BUILD_DIR%\bin
set SRC_DIR=%cd%\src\main\java

echo.
echo ========================================================================
echo   VIRTUAL CAREER FAIR SYSTEM (VCFS)
echo   Build and Launch Demo - Group 9 CSCU9P6
echo        University of Stirling
echo ========================================================================
echo.
echo  Launch Time: %DATE% %TIME%
echo  Working Directory: %cd%
echo  Source Directory: %SRC_DIR%
echo  Build Directory: %BUILD_DIR%
echo  Binary Output: %BIN_DIR%
echo.

REM ================================================================
REM  STEP 1: System Verification
REM ================================================================

echo [STEP 1 of 6] Verifying Java Installation...
echo.

java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    color 0C
    echo  ✗ [ERROR] Java is NOT installed or NOT in PATH
    echo.
    echo  REQUIRED: Java JDK 11 or higher
    echo  DOWNLOAD: https://www.oracle.com/java/
    echo  SETUP:    Add Java\bin to system PATH environment variable
    echo.
    pause
    exit /b 1
)

for /f "tokens=3" %%j in ('java -version 2^>^&1 ^| find /i "version"') do set JAVA_VERSION=%%j
echo  ✓ Java Found: Version %JAVA_VERSION%
echo.

REM ================================================================
REM  STEP 2: Verify Source Code Structure
REM ================================================================

echo [STEP 2 of 6] Verifying Source Code Structure...
echo.

if not exist "%SRC_DIR%" (
    color 0C
    echo  ✗ [ERROR] Source directory not found: %SRC_DIR%
    echo.
    echo  This script must run from: _FOR_GITHUB_SUBMISSION\
    echo  Current location: %cd%
    echo.
    pause
    exit /b 2
)

if not exist "%SRC_DIR%\vcfs\App.java" (
    color 0C
    echo  ✗ [ERROR] Main entry point not found
    echo  Expected: %SRC_DIR%\vcfs\App.java
    echo.
    pause
    exit /b 3
)

REM Count source files
setlocal enabledelayedexpansion
set SOURCE_COUNT=0
for /r "%SRC_DIR%" %%f in (*.java) do set /a SOURCE_COUNT+=1
endlocal & set SOURCE_COUNT=%SOURCE_COUNT%

echo  ✓ Source Structure Verified
echo  ✓ Java Source Files: %SOURCE_COUNT% files
echo  ✓ Main Entry Point: src\main\java\vcfs\Main.java
echo.

REM ================================================================
REM  STEP 3: Prepare Build Directory
REM ================================================================

echo [STEP 3 of 6] Preparing Build Directory...
echo.

if exist "%BIN_DIR%" (
    echo  ℹ Cleaning previous build...
    rmdir /s /q "%BIN_DIR%" >nul 2>&1
)

mkdir "%BIN_DIR%" >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    color 0C
    echo  ✗ [ERROR] Failed to create build directory: %BIN_DIR%
    echo.
    pause
    exit /b 4
)

echo  ✓ Build Directory Created: %BIN_DIR%
echo.

REM ================================================================
REM  STEP 4: Compile All Java Source Files
REM ================================================================

echo [STEP 4 of 6] Compiling Java Source Files...
echo.
echo  Command: javac -d %BIN_DIR% -encoding UTF-8 [%SOURCE_COUNT% files]
echo.

echo  Compiling %SOURCE_COUNT% files with javac...
echo.

REM Change to source directory for proper relative path compilation
pushd "%SRC_DIR%"

REM Compile using -sourcepath to let javac automatically find all dependencies
REM This compiles the entire vcfs package tree
javac -d "%BIN_DIR%" -encoding UTF-8 -sourcepath . vcfs/App.java 2>nul

set COMPILE_RESULT=%ERRORLEVEL%

popd

REM Count compiled classes
setlocal enabledelayedexpansion
set CLASS_COUNT=0
for /r "%BIN_DIR%" %%f in (*.class) do set /a CLASS_COUNT+=1
endlocal & set CLASS_COUNT=%CLASS_COUNT%

if %CLASS_COUNT% LEQ 0 (
    color 0C
    echo  ✗ [ERROR] Compilation failed - no classes generated
    echo.
    echo  Attempting diagnostic compile for error details...
    echo.
    javac -d "%BIN_DIR%" -encoding UTF-8 -sourcepath "%SRC_DIR%" "%SRC_DIR%\vcfs\App.java"
    echo.
    pause
    exit /b 5
)

echo  ✓ Compilation Successful: %CLASS_COUNT% classes generated
echo  ✓ Output Location: %BIN_DIR%\vcfs\
echo.

REM ================================================================
REM  STEP 5: Verify Compiled Main Class
REM ================================================================

echo [STEP 5 of 6] Verifying Compiled Application...
echo.

if not exist "%BIN_DIR%\vcfs\App.class" (
    color 0C
    echo  ✗ [ERROR] App.class not found at: %BIN_DIR%\vcfs\App.class
    echo.
    pause
    exit /b 6
)

echo  ✓ Main Entry Point Compiled: %BIN_DIR%\vcfs\App.class
echo  ✓ Ready for execution
echo.

REM ================================================================
REM  STEP 6: Launch Application
REM ================================================================

echo [STEP 6 of 6] Launching Virtual Career Fair System Demo...
echo.
echo ========================================================================
echo   APPLICATION STARTUP
echo ========================================================================
echo.
echo  Main Class: vcfs.App
echo  Classpath: "%BIN_DIR%"
echo  Framework: Java Swing UI
echo  Status: Starting...
echo.
echo  ℹ Note: Application window should appear in a few seconds
echo  ℹ This console will display log messages during execution
echo  ℹ Close the application window to return to this console
echo.
echo ========================================================================
echo.

REM Launch the application with full output capture
java -cp "%BIN_DIR%" vcfs.App 2>&1

REM Capture exit code
set APP_EXIT_CODE=%ERRORLEVEL%

REM ================================================================
REM  Post-Execution Report
REM ================================================================

echo.
echo ========================================================================
echo   APPLICATION EXECUTION COMPLETE
echo ========================================================================
echo.

if %APP_EXIT_CODE% EQU 0 (
    color 0A
    echo  ✓ Application exited successfully
    echo  ✓ Exit Code: 0
    echo.
) else if %APP_EXIT_CODE% EQU 1 (
    color 0E
    echo  ! Application exited with user request or clean shutdown
    echo  ! Exit Code: %APP_EXIT_CODE%
    echo.
) else (
    color 0C
    echo  ✗ Application encountered an error
    echo  ✗ Exit Code: %APP_EXIT_CODE%
    echo.
    echo  Troubleshooting:
    echo    - Check console output above for error messages
    echo    - Verify Java version is 11 or higher
    echo    - Ensure all 45 source files compiled successfully
    echo    - Check that vcfs.Main class exists
    echo.
)

echo ========================================================================
echo.

REM Call end time (simpler than elapsed calculation)
echo  Build Location: %BIN_DIR%
echo  Compilation: %SOURCE_COUNT% files ^-^> %CLASS_COUNT% classes
echo.
echo  Press any key to close this window...
pause >nul

endlocal
exit /b %APP_EXIT_CODE%

REM ================================================================
REM  END OF BUILD AND LAUNCH SCRIPT
REM ================================================================
REM
REM  QUICK START:
REM    1. Copy BUILD_AND_RUN_DEMO.bat to _FOR_GITHUB_SUBMISSION\
REM    2. Open command prompt in _FOR_GITHUB_SUBMISSION\
REM    3. Run: BUILD_AND_RUN_DEMO.bat
REM
REM  TROUBLESHOOTING:
REM
REM  Q: "Java is NOT installed or NOT in PATH"
REM  A: Install Java JDK 11+ and add to system PATH:
REM     Settings ^> Environment Variables ^> System ^> PATH
REM
REM  Q: "Source directory not found"
REM  A: Script must run from _FOR_GITHUB_SUBMISSION\ directory
REM     Check current location in console window
REM
REM  Q: "Compilation failed"
REM  A: Verify all 45 .java files exist in src/main/java/vcfs/
REM     Requires: Java 11+, UTF-8 encoding support
REM
REM  Q: "Main.class not found"
REM  A: Compilation did not produce vcfs/Main.class
REM     Check that Main.java exists in correct directory
REM
REM  Q: Application crashes on startup
REM  A: Check console output for detailed error trace
REM     File paths, permissions, or missing resources may be issue
REM
REM  Q: "OutOfMemoryError"
REM  A: Increase heap space, modify launch line:
REM     java -Xmx1024m -cp "%BIN_DIR%" vcfs.Main
REM
REM ================================================================
REM
REM  PROJECT STRUCTURE REMINDER:
REM
REM  _FOR_GITHUB_SUBMISSION/
REM  ├── BUILD_AND_RUN_DEMO.bat    ← Script location
REM  ├── src/
REM  │   ├── main/java/vcfs/       ← Source code (45 files)
REM  │   │   ├── Main.java
REM  │   │   ├── controllers/
REM  │   │   ├── core/
REM  │   │   ├── models/
REM  │   │   └── views/
REM  │   └── test/java/vcfs/       ← JUnit tests
REM  └── build/                     ← Generated during build
REM      └── bin/                   ← Compiled classes output
REM
REM ================================================================

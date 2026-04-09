@ECHO OFF
REM ===================================================================
REM VIRTUAL CAREER FAIR SYSTEM (VCFS) - FULLY AUTOMATED BUILD LAUNCHER
REM ===================================================================
REM Group 9 - CSCU9P6 Project
REM Project Manager & Lead Developer: Zaid Siddiqui (mzs00007)
REM Collaborators: Taha, YAMI, MJAMishkat, Mohamed
REM University of Stirling - Semester 6, 2026
REM ===================================================================
REM COMPLETELY AUTOMATED - NO MANUAL STEPS REQUIRED
REM ===================================================================

SETLOCAL ENABLEDELAYEDEXPANSION
CLS
COLOR 0A
CHCP 65001 >NUL 2>&1

REM Configuration
SET "SCRIPT_DIR=%~dp0"
SET "SRC_MAIN=%SCRIPT_DIR%src\main\java"
SET "SRC_TEST=%SCRIPT_DIR%src\test\java"
SET "BIN_PROD=%SCRIPT_DIR%bin"
SET "BIN_TEST=%SCRIPT_DIR%bin-test"
SET "LIB_DIR=%SCRIPT_DIR%lib"
SET "LOGS_DIR=%SCRIPT_DIR%logs"
SET "LOG_FILE=%LOGS_DIR%\build_%DATE:~-4%-%DATE:~-10,2%-%DATE:~-7,2%_%TIME:~0,2%-%TIME:~5,2%.log"
SET "MAIN_CLASS=vcfs.App"
SET "RETRY_COUNT=0"
SET "MAX_RETRIES=2"

REM Create logs directory
IF NOT EXIST "%LOGS_DIR%" MKDIR "%LOGS_DIR%" >NUL 2>&1

REM Startup Banner
ECHO. >> "%LOG_FILE%"
ECHO ================================================================ >> "%LOG_FILE%"
ECHO VCFS BUILD AUTOMATION - %DATE% %TIME% >> "%LOG_FILE%"
ECHO ================================================================ >> "%LOG_FILE%"

ECHO.
ECHO ===============================================================
ECHO VCFS - VIRTUAL CAREER FAIR SYSTEM
ECHO AUTOMATED BUILD AND LAUNCH SYSTEM
ECHO ===============================================================
ECHO.
ECHO Project Root: %SCRIPT_DIR%
ECHO Build Target: %BIN_PROD%
ECHO Status: INITIALIZING...
ECHO.

REM ===================================================================
REM AUTO-DETECTION AND VALIDATION
REM ===================================================================

REM Check Java availability
WHERE java >NUL 2>NUL
IF %ERRORLEVEL% NEQ 0 (
    ECHO.
    ECHO [ERROR] Java not found in PATH!
    ECHO Please install Java JDK 11+ and add to system PATH
    ECHO.
    PAUSE
    EXIT /B 1
)

REM Get Java version
FOR /F "tokens=3" %%V IN ('java -version 2^>^&1 ^| findstr "version"') DO SET "JAVA_VER=%%V"
ECHO [OK] Java Detected: %JAVA_VER%

REM Auto-detect source files
SETLOCAL ENABLEDELAYEDEXPANSION
SET "PROD_FILES=0"
SET "TEST_FILES=0"
FOR /R "%SRC_MAIN%" %%F IN (*.java) DO SET /A PROD_FILES+=1
FOR /R "%SRC_TEST%" %%F IN (*.java) DO SET /A TEST_FILES+=1
ENDLOCAL & SET "PROD_FILES=%PROD_FILES%" & SET "TEST_FILES=%TEST_FILES%"
ECHO [OK] Detected %PROD_FILES% production files, %TEST_FILES% test files

REM Auto-detect libraries
SETLOCAL ENABLEDELAYEDEXPANSION
SET "JAR_COUNT=0"
FOR %%J IN ("%LIB_DIR%\*.jar") DO SET /A JAR_COUNT+=1
ENDLOCAL & SET "JAR_COUNT=%JAR_COUNT%"
ECHO [OK] Detected %JAR_COUNT% library JARs

REM Create output directories
IF NOT EXIST "%BIN_PROD%" MKDIR "%BIN_PROD%" >NUL 2>&1
IF NOT EXIST "%BIN_TEST%" MKDIR "%BIN_TEST%" >NUL 2>&1

REM Clean old build artifacts silently
DEL /Q /S "%BIN_PROD%\*.class" >NUL 2>&1
DEL /Q /S "%BIN_TEST%\*.class" >NUL 2>&1
DEL /Q /S "%BIN_PROD%\vcfs\*" >NUL 2>&1
DEL /Q /S "%BIN_TEST%\vcfs\*" >NUL 2>&1

REM ===================================================================
REM AUTO-COMPILATION WITH RECURSIVE FILE DISCOVERY
REM ===================================================================

:COMPILE_LOOP
SET /A RETRY_COUNT+=1

ECHO.
ECHO [Attempt %RETRY_COUNT%] Compiling production code...
ECHO Command: javac -d "%BIN_PROD%" -encoding UTF-8 -cp "%LIB_DIR%\*" with sourcepath enabled

REM Smart compilation using sourcepath - javac automatically finds all dependencies
REM This is more reliable than manually listing directories
javac -d "%BIN_PROD%" -encoding UTF-8 -cp "%LIB_DIR%\*" -sourcepath "%SRC_MAIN%" "%SRC_MAIN%\vcfs\App.java" >> "%LOG_FILE%" 2>&1

REM Count compiled classes
SET "CLASS_COUNT=0"
FOR /R "%BIN_PROD%" %%C IN (*.class) DO SET /A CLASS_COUNT+=1

REM Verify compilation success
IF %CLASS_COUNT% LSS 10 (
    IF %RETRY_COUNT% LSS %MAX_RETRIES% (
        ECHO [RETRY] Only %CLASS_COUNT% classes generated, retrying...
        GOTO COMPILE_LOOP
    ) ELSE (
        ECHO.
        ECHO [ERROR] Compilation failed after %MAX_RETRIES% attempts
        ECHO Classes generated: %CLASS_COUNT% (expected 90+)
        ECHO Check %LOG_FILE% for details
        TYPE "%LOG_FILE%"
        ECHO.
        PAUSE
        EXIT /B 2
    )
)

ECHO [OK] Production compilation successful - %CLASS_COUNT% classes

REM Compile tests (non-critical)
ECHO.
ECHO [Auto] Compiling test code...
javac -d "%BIN_TEST%" -encoding UTF-8 -cp "%LIB_DIR%\*;%BIN_PROD%" -sourcepath "%SRC_TEST%" "%SRC_TEST%\vcfs\integration\PortalIntegrationTest.java" >> "%LOG_FILE%" 2>&1

SET "TEST_CLASS_COUNT=0"
FOR /R "%BIN_TEST%" %%C IN (*.class) DO SET /A TEST_CLASS_COUNT+=1
IF %TEST_CLASS_COUNT% GTR 0 (
    ECHO [OK] Test compilation successful - %TEST_CLASS_COUNT% classes
) ELSE (
    ECHO [INFO] No test classes compiled (optional)
)

REM ===================================================================
REM AUTO-CLASSPATH GENERATION AND VALIDATION
REM ===================================================================

ECHO.
ECHO [Auto] Generating classpath...

SET "CLASSPATH=%LIB_DIR%\*;%BIN_PROD%;%BIN_TEST%"
ECHO [OK] Classpath: %CLASSPATH%

REM Verify main class exists
IF NOT EXIST "%BIN_PROD%\vcfs\App.class" (
    REM Try alternative main class names
    IF EXIST "%BIN_PROD%\vcfs\Main.class" (
        SET "MAIN_CLASS=vcfs.Main"
        ECHO [AUTO] Detected main class: vcfs.Main
    ) ELSE (
        ECHO [ERROR] No main class found (App.class or Main.class)
        ECHO Check that source files compiled correctly
        PAUSE
        EXIT /B 3
    )
) ELSE (
    SET "MAIN_CLASS=vcfs.App"
    ECHO [AUTO] Detected main class: vcfs.App
)

REM ===================================================================
REM AUTO-LAUNCH APPLICATION
REM ===================================================================

ECHO.
ECHO ===============================================================
ECHO BUILD COMPLETE - LAUNCHING APPLICATION
ECHO ===============================================================
ECHO.
ECHO Compilation Summary:
ECHO   Production Classes: %CLASS_COUNT%
ECHO   Test Classes: %TEST_CLASS_COUNT%
ECHO   Total: %CLASS_COUNT% + %TEST_CLASS_COUNT% classes
ECHO   Java Version: %JAVA_VER%
ECHO.
ECHO Application Starting: %MAIN_CLASS%
ECHO UI Framework: Java Swing
ECHO.
ECHO Three Portals Available:
ECHO   [1] Administrator   - Configure fairs, organizations, recruiters
ECHO   [2] Recruiter       - Publish offers, manage meetings
ECHO   [3] Candidate       - Browse offers, request interviews
ECHO.
ECHO ===============================================================
ECHO.

ECHO [LAUNCHING...] >> "%LOG_FILE%"
ECHO Executing: java -cp "%CLASSPATH%" "%MAIN_CLASS%" >> "%LOG_FILE%"

REM Launch application with automatic output capture
java -cp "%CLASSPATH%" "%MAIN_CLASS%" >> "%LOG_FILE%" 2>&1

SET "APP_EXIT_CODE=!ERRORLEVEL!"

REM ===================================================================
REM AUTO POST-LAUNCH REPORTING
REM ===================================================================

ECHO.
ECHO ===============================================================
ECHO APPLICATION EXECUTION COMPLETE
ECHO ===============================================================
ECHO.
ECHO Build Artifacts:
ECHO   Production: %CLASS_COUNT% classes in %BIN_PROD%
ECHO   Tests:      %TEST_CLASS_COUNT% classes in %BIN_TEST%
ECHO   Logs:       %LOG_FILE%
ECHO.

IF %APP_EXIT_CODE% EQU 0 (
    ECHO Status: [SUCCESS] Application exited cleanly
    COLOR 0A
) ELSE (
    ECHO Status: [INFO] Exit code %APP_EXIT_CODE%
)

ECHO.
ECHO Quick Commands:
ECHO   To rebuild: BUILD_AND_RUN_DEMO.bat
ECHO   View logs:  type "%LOG_FILE%"
ECHO   Run only:   java -cp "%CLASSPATH%" "%MAIN_CLASS%"
ECHO.
ECHO ===============================================================
ECHO.

ENDLOCAL
EXIT /B %APP_EXIT_CODE%

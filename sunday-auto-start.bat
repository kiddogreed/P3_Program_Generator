@echo off
REM ============================================
REM   Sunday Auto-Start Script
REM   Church Program Generator - Pasay 3rd Ward
REM ============================================

echo ============================================
echo    Church Program Generator
echo    Sunday Auto-Start
echo ============================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo [%time%] Checking if application is already running...

REM Check if application is already running
tasklist /FI "IMAGENAME eq java.exe" /FI "WINDOWTITLE eq *ProgramGenerator*" 2>nul | find /I "java.exe" >nul
if %ERRORLEVEL% EQU 0 (
    echo [%time%] Application is already running!
    echo Opening browser to Sacrament program form...
    start http://localhost:8080/sacrament
    timeout /t 3 /nobreak >nul
    exit /b 0
)

echo [%time%] Starting Church Program Generator...
echo.

REM Start the application in background
start /B java -jar target\program-generator-0.0.1-SNAPSHOT.jar

echo [%time%] Waiting for application to start (30 seconds)...
timeout /t 30 /nobreak

echo [%time%] Opening browser to Sacrament Meeting form...
start http://localhost:8080/sacrament

echo.
echo ============================================
echo   Application Started Successfully!
echo ============================================
echo.
echo The application is now running at:
echo http://localhost:8080
echo.
echo Sacrament Meeting form is open in your browser.
echo.
echo Please complete today's program:
echo  1. Enter meeting date (today)
echo  2. Add presiding and conducting names
echo  3. Enter hymn numbers
echo  4. Add speakers with topics
echo  5. Preview and export to Word/PDF
echo.
echo To stop the application, close the Java window.
echo ============================================
echo.

REM Optional: Create a log entry
echo [%date% %time%] Sunday auto-start completed >> sunday-automation.log

pause
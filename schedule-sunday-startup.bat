@echo off
REM ============================================
REM   Setup Windows Task Scheduler
REM   Auto-start Church Program Generator every Sunday
REM ============================================

echo ============================================
echo   Windows Task Scheduler Setup
echo   Church Program Generator
echo ============================================
echo.
echo This script will create a scheduled task to automatically
echo start the Church Program Generator every Sunday morning.
echo.
echo Task Details:
echo   Name: Church Program Generator - Sunday
echo   Trigger: Every Sunday at 7:00 AM
echo   Action: Start application and open browser
echo.
pause

REM Get current directory
set "SCRIPT_DIR=%~dp0"
set "SCRIPT_PATH=%SCRIPT_DIR%sunday-auto-start.bat"

echo.
echo Creating scheduled task...
echo.

REM Create the scheduled task
schtasks /Create ^
    /TN "Church Program Generator - Sunday" ^
    /TR "\"%SCRIPT_PATH%\"" ^
    /SC WEEKLY ^
    /D SUN ^
    /ST 07:00 ^
    /F

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo   SUCCESS! Task Created Successfully
    echo ============================================
    echo.
    echo The Church Program Generator will now automatically
    echo start every Sunday at 7:00 AM.
    echo.
    echo To modify the schedule:
    echo   1. Open Task Scheduler (taskschd.msc)
    echo   2. Find "Church Program Generator - Sunday"
    echo   3. Right-click and select Properties
    echo   4. Modify trigger time or frequency
    echo.
    echo To test the task now:
    echo   schtasks /Run /TN "Church Program Generator - Sunday"
    echo.
    echo To delete the task:
    echo   schtasks /Delete /TN "Church Program Generator - Sunday" /F
    echo.
) else (
    echo.
    echo ============================================
    echo   ERROR! Task Creation Failed
    echo ============================================
    echo.
    echo Please run this script as Administrator.
    echo Right-click and select "Run as administrator"
    echo.
)

echo ============================================
pause
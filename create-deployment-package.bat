@echo off
echo Creating deployment package...

REM Create deployment folder
if not exist "deployment" mkdir deployment

REM Copy JAR file
copy "target\program-generator-0.0.1-SNAPSHOT.jar" "deployment\"

REM Copy startup scripts
copy "run-church-program.bat" "deployment\"
copy "run-church-program.sh" "deployment\"

REM Copy documentation
copy "DEPLOYMENT_GUIDE.md" "deployment\"
copy "README.md" "deployment\"

REM Create a simple README for the deployment folder
echo # Church Program Generator - Ready to Deploy > deployment\README.txt
echo. >> deployment\README.txt
echo This folder contains everything needed to run the Church Program Generator. >> deployment\README.txt
echo. >> deployment\README.txt
echo For Windows: >> deployment\README.txt
echo 1. Double-click "run-church-program.bat" >> deployment\README.txt
echo 2. Open browser to http://localhost:8080 >> deployment\README.txt
echo. >> deployment\README.txt
echo For Mac/Linux: >> deployment\README.txt
echo 1. Run "bash run-church-program.sh" >> deployment\README.txt
echo 2. Open browser to http://localhost:8080 >> deployment\README.txt
echo. >> deployment\README.txt
echo Requirements: Java 17 or higher >> deployment\README.txt

echo.
echo Deployment package created in 'deployment' folder!
echo.
echo Contents:
dir deployment
echo.
echo You can now copy the 'deployment' folder to any computer
echo and run the application using the provided scripts.
echo.
pause
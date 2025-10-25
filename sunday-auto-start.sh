#!/bin/bash

# ============================================
#   Sunday Auto-Start Script
#   Church Program Generator - Pasay 3rd Ward
# ============================================

echo "============================================"
echo "   Church Program Generator"
echo "   Sunday Auto-Start"
echo "============================================"
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    read -p "Press Enter to exit..."
    exit 1
fi

echo "[$(date '+%H:%M:%S')] Checking if application is already running..."

# Check if application is already running
if pgrep -f "program-generator.*jar" > /dev/null; then
    echo "[$(date '+%H:%M:%S')] Application is already running!"
    echo "Opening browser to Sacrament program form..."
    
    # Open browser based on OS
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        open http://localhost:8080/sacrament
    else
        # Linux
        xdg-open http://localhost:8080/sacrament 2>/dev/null || sensible-browser http://localhost:8080/sacrament
    fi
    
    sleep 3
    exit 0
fi

echo "[$(date '+%H:%M:%S')] Starting Church Program Generator..."
echo

# Start the application in background
cd "$(dirname "$0")"
nohup java -jar target/program-generator-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# Store the process ID
APP_PID=$!
echo $APP_PID > app.pid

echo "[$(date '+%H:%M:%S')] Waiting for application to start (30 seconds)..."
sleep 30

echo "[$(date '+%H:%M:%S')] Opening browser to Sacrament Meeting form..."

# Open browser based on OS
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    open http://localhost:8080/sacrament
else
    # Linux
    xdg-open http://localhost:8080/sacrament 2>/dev/null || sensible-browser http://localhost:8080/sacrament
fi

echo
echo "============================================"
echo "   Application Started Successfully!"
echo "============================================"
echo
echo "The application is now running at:"
echo "http://localhost:8080"
echo
echo "Process ID: $APP_PID"
echo "Sacrament Meeting form is open in your browser."
echo
echo "Please complete today's program:"
echo " 1. Enter meeting date (today)"
echo " 2. Add presiding and conducting names"
echo " 3. Enter hymn numbers"
echo " 4. Add speakers with topics"
echo " 5. Preview and export to Word/PDF"
echo
echo "To stop the application, run:"
echo "kill $APP_PID"
echo "============================================"
echo

# Optional: Create a log entry
echo "[$(date)] Sunday auto-start completed" >> sunday-automation.log

# Make script wait (optional)
read -p "Press Enter to continue..."
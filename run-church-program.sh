#!/bin/bash

echo "==============================================="
echo "   Church Program Generator"
echo "   Pasay 3rd Ward"
echo "==============================================="
echo

echo "Checking Java installation..."

if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo
    echo "Please install Java 17 or higher from:"
    echo "https://adoptium.net/"
    echo
    read -p "Press Enter to exit..."
    exit 1
fi

echo "Java found! Starting application..."
echo
echo "The application will be available at:"
echo "http://localhost:8080"
echo
echo "Press Ctrl+C to stop the application"
echo "==============================================="

java -jar target/program-generator-0.0.1-SNAPSHOT.jar
# Church Program Generator - Deployment Guide

## Option 1: JAR File (Requires Java 17+ on target device)

### Files needed:
- `program-generator-0.0.1-SNAPSHOT.jar` (located in `target/` folder)

### To run:
1. Install Java 17 or higher on the target device
2. Copy the JAR file to the target device
3. Open command prompt/terminal in the folder containing the JAR
4. Run: `java -jar program-generator-0.0.1-SNAPSHOT.jar`
5. Open browser and go to: http://localhost:8080

### Startup script for Windows (`run-church-program.bat`):
```batch
@echo off
echo Starting Church Program Generator...
java -jar program-generator-0.0.1-SNAPSHOT.jar
pause
```

### Startup script for Mac/Linux (`run-church-program.sh`):
```bash
#!/bin/bash
echo "Starting Church Program Generator..."
java -jar program-generator-0.0.1-SNAPSHOT.jar
```

## Option 2: Docker Container (No Java required on target device)

### Requirements:
- Docker installed on target device

### Steps:
1. Copy the entire project folder
2. Run: `docker build -t church-program-generator .`
3. Run: `docker run -p 8080:8080 church-program-generator`
4. Open browser and go to: http://localhost:8080

## Option 3: Native Executable (No Java required - Advanced)

### Requirements:
- GraalVM Native Image (for building)

### Benefits:
- No Java required on target device
- Faster startup time
- Smaller memory footprint

## Option 4: Cloud Deployment (Access from anywhere)

### Heroku:
- Free tier available
- Accessible from any device with internet
- No installation required

### Railway/Render:
- Modern alternatives to Heroku
- Easy deployment from GitHub

### Azure/AWS:
- Enterprise-grade hosting
- More configuration options

## Recommended Approach

For church use, I recommend **Option 2 (Docker)** or **Option 4 (Cloud)** because:

1. **Docker**: No Java installation needed, works consistently across all devices
2. **Cloud**: Accessible from any device, no local installation needed

## Network Access

If you want others to access the application from different computers:

1. **Local Network**: Change `server.address=0.0.0.0` in `application.properties`
2. **Find your IP**: Run `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
3. **Access via**: `http://YOUR-IP:8080`

## Port Configuration

To change the default port (8080), add to `application.properties`:
```properties
server.port=9000
```

Then access via: http://localhost:9000
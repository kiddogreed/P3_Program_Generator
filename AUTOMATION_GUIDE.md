# 🤖 Church Program Generator - Automation Guide

## Overview

This guide provides multiple approaches to automate your Church Program Generator to run every Sunday, reducing manual work and ensuring programs are ready on time.

## 🎯 Automation Options

### **Option 1: Auto-Start Application Every Sunday**
Automatically start the application every Sunday morning so it's ready when you need it.

### **Option 2: Windows Task Scheduler**
Schedule the application to launch automatically at a specific time each Sunday.

### **Option 3: Linux/Mac Cron Jobs**
Use cron to automatically start the application on Sundays.

### **Option 4: Windows Service**
Run the application as a Windows service that starts automatically.

### **Option 5: Email Reminder System**
Receive email reminders every Sunday to create programs.

---

## 📅 Option 1: Windows Task Scheduler (Recommended for Windows)

### **Setup Instructions:**

1. **Open Task Scheduler**
   - Press `Win + R`, type `taskschd.msc`, press Enter

2. **Create Basic Task**
   - Click "Create Basic Task" in the right panel
   - Name: "Church Program Generator - Sunday"
   - Description: "Auto-start program generator every Sunday"

3. **Set Trigger**
   - When: **Weekly**
   - Start date: Next Sunday
   - Recur every: **1 week**
   - Days: Check **Sunday only**
   - Time: **7:00 AM** (or your preferred time)

4. **Set Action**
   - Action: **Start a program**
   - Program/script: Browse to `run-church-program.bat`
   - Start in: `C:\projects\Church\ProgramGenerator\deployment`

5. **Finish**
   - Check "Open Properties dialog" if you want to configure advanced options
   - Click Finish

### **Advanced Options:**
- **Run whether user is logged on or not**: Requires password
- **Run with highest privileges**: If needed for permissions
- **Stop task if runs longer than**: 1 hour (adjust as needed)

### **Script for Task Scheduler:**
Use the `schedule-sunday-startup.bat` script provided in this project.

---

## 🐧 Option 2: Linux/Mac Cron Job

### **Setup Cron Job:**

1. **Edit Crontab**
   ```bash
   crontab -e
   ```

2. **Add Sunday Schedule**
   ```bash
   # Start Church Program Generator every Sunday at 7:00 AM
   0 7 * * 0 cd /path/to/ProgramGenerator && bash run-church-program.sh
   
   # Alternative: Start at 6:30 AM
   30 6 * * 0 cd /path/to/ProgramGenerator && bash run-church-program.sh
   ```

3. **Save and Exit**
   - In vi/vim: Press `Esc`, type `:wq`, press Enter
   - In nano: Press `Ctrl+X`, then `Y`, then Enter

### **Cron Syntax Explanation:**
```
*    *    *    *    *
│    │    │    │    │
│    │    │    │    └─── Day of week (0-7, 0 and 7 = Sunday)
│    │    │    └──────── Month (1-12)
│    │    └───────────── Day of month (1-31)
│    └────────────────── Hour (0-23)
└─────────────────────── Minute (0-59)
```

### **Verify Cron Job:**
```bash
# List all cron jobs
crontab -l

# Check cron logs (Linux)
grep CRON /var/log/syslog
```

---

## 🪟 Option 3: Windows Service Installation

### **Create Windows Service:**

1. **Using NSSM (Non-Sucking Service Manager)**
   ```bash
   # Download NSSM from: https://nssm.cc/download
   
   # Install service
   nssm install ChurchProgramGenerator "C:\Program Files\Java\jdk-17\bin\java.exe"
   
   # Set arguments
   nssm set ChurchProgramGenerator AppParameters "-jar C:\projects\Church\ProgramGenerator\target\program-generator-0.0.1-SNAPSHOT.jar"
   
   # Set working directory
   nssm set ChurchProgramGenerator AppDirectory "C:\projects\Church\ProgramGenerator"
   
   # Set service to start automatically
   nssm set ChurchProgramGenerator Start SERVICE_AUTO_START
   
   # Start the service
   nssm start ChurchProgramGenerator
   ```

2. **Manual Service Creation (Advanced)**
   - Use `sc.exe` to create service
   - Requires administrative privileges
   - Service will start automatically with Windows

### **Service Management:**
```bash
# Start service
nssm start ChurchProgramGenerator

# Stop service
nssm stop ChurchProgramGenerator

# Restart service
nssm restart ChurchProgramGenerator

# Remove service
nssm remove ChurchProgramGenerator confirm
```

---

## 📧 Option 4: Email Reminder System

### **Setup Email Reminders:**

Create a scheduled task that sends an email reminder every Sunday morning.

**PowerShell Email Script** (Windows):
```powershell
# save as: send-program-reminder.ps1

$EmailFrom = "churchprogram@yourdomain.com"
$EmailTo = "clerk@yourdomain.com"
$Subject = "Reminder: Create Sunday Sacrament Program"
$Body = @"
Good morning!

This is your automated reminder to create today's Sacrament Meeting program.

Please visit: http://localhost:8080/sacrament

Have a blessed Sunday!
"@

$SMTPServer = "smtp.gmail.com"
$SMTPPort = 587
$Username = "your-email@gmail.com"
$Password = ConvertTo-SecureString "your-app-password" -AsPlainText -Force
$Credential = New-Object System.Management.Automation.PSCredential ($Username, $Password)

Send-MailMessage -From $EmailFrom -To $EmailTo -Subject $Subject -Body $Body `
                 -SmtpServer $SMTPServer -Port $SMTPPort -UseSsl -Credential $Credential
```

**Schedule Email with Task Scheduler:**
- Action: Start a program
- Program: `powershell.exe`
- Arguments: `-File "C:\path\to\send-program-reminder.ps1"`
- Trigger: Weekly on Sunday at 6:00 AM

---

## 🔔 Option 5: Browser Auto-Open

### **Auto-open Browser to Program Generator:**

**Windows Batch Script:**
```batch
@echo off
REM Auto-start program generator and open browser

echo Starting Church Program Generator...
start /B java -jar "C:\projects\Church\ProgramGenerator\target\program-generator-0.0.1-SNAPSHOT.jar"

REM Wait for application to start (30 seconds)
timeout /t 30 /nobreak

REM Open browser to sacrament program page
start http://localhost:8080/sacrament

echo Application started and browser opened!
```

**Mac/Linux Bash Script:**
```bash
#!/bin/bash
# Auto-start and open browser

echo "Starting Church Program Generator..."
java -jar /path/to/program-generator-0.0.1-SNAPSHOT.jar &

# Wait for application to start
sleep 30

# Open browser (Mac)
open http://localhost:8080/sacrament

# Open browser (Linux)
# xdg-open http://localhost:8080/sacrament

echo "Application started and browser opened!"
```

---

## 🎯 Complete Sunday Automation Workflow

### **Recommended Setup:**

1. **Saturday Evening (11:00 PM)** - Start application as service/background
2. **Sunday Morning (6:00 AM)** - Send email reminder
3. **Sunday Morning (6:30 AM)** - Auto-open browser to program form
4. **Sunday Morning (7:00 AM - 9:00 AM)** - Clerk creates program
5. **Sunday Morning (9:30 AM)** - Meeting starts with program ready

### **Complete Automation Script:**
```batch
@echo off
REM Complete Sunday Automation Script

echo ============================================
echo   Sunday Program Generator Automation
echo   Pasay 3rd Ward
echo ============================================

REM 1. Check if application is already running
tasklist /FI "IMAGENAME eq java.exe" | find /I "java.exe" >nul
if %ERRORLEVEL% EQU 0 (
    echo Application is already running
) else (
    echo Starting application...
    start /B java -jar target\program-generator-0.0.1-SNAPSHOT.jar
    timeout /t 30 /nobreak
)

REM 2. Open browser to sacrament form
echo Opening browser...
start http://localhost:8080/sacrament

REM 3. Display reminder message
echo.
echo ============================================
echo   REMINDER: Create Today's Program
echo ============================================
echo.
echo Please complete the following:
echo 1. Fill in today's date
echo 2. Add presiding and conducting names
echo 3. Enter hymn numbers
echo 4. Add speakers and topics
echo 5. Preview and export program
echo.
echo The application will remain running.
echo Close this window when done.
echo ============================================

pause
```

---

## 📊 Pre-populate Recurring Data

### **Template System for Recurring Information:**

Many details remain constant week-to-week. Consider creating:

1. **Default Ward Name**: Always "Pasay 3rd Ward"
2. **Recurring Leaders**: Bishop and counselors usually don't change weekly
3. **Standard Hymns**: Common hymns that repeat

### **Future Enhancement: Database Storage**
In future versions, the application could:
- Remember recent speakers
- Suggest rotation of speakers
- Auto-fill based on calendar
- Store recurring meeting patterns

---

## 🔧 Troubleshooting Automation

### **Application Doesn't Start:**
- Check Java is installed and in PATH: `java -version`
- Verify file paths in scheduled task
- Check Windows Event Viewer for errors
- Ensure user has permissions to run the application

### **Task Scheduler Issues:**
- Run task manually to test
- Check "Task History" for errors
- Ensure "Run whether user is logged on or not" if needed
- Verify account has necessary permissions

### **Cron Job Not Working:**
- Check cron service is running: `systemctl status cron`
- Verify cron syntax: `crontab -l`
- Check system logs: `grep CRON /var/log/syslog`
- Use absolute paths in cron commands

### **Service Doesn't Start:**
- Check Windows Services: `services.msc`
- Verify JAVA_HOME is set correctly
- Check service logs in Event Viewer
- Ensure service account has permissions

---

## 🎯 Best Practices

### **For Church Clerk:**
1. Set up automation on Saturday evening
2. Receive reminder email Sunday morning
3. Create program 1-2 hours before meeting
4. Keep backup of previous programs

### **For Ward Technology Specialist:**
1. Set up as Windows Service for reliability
2. Configure automatic start on system boot
3. Set up email notifications for errors
4. Regular backup of generated programs

### **For Multi-Ward Setup:**
1. Run on shared server accessible to all wards
2. Each ward accesses via network (http://server-ip:8080)
3. Different ports for different wards if needed
4. Central backup and monitoring

---

## 📧 Support & Maintenance

### **Monitoring:**
- Check application logs weekly
- Verify programs are being created
- Test automation monthly
- Update Java/dependencies as needed

### **Backup Strategy:**
```batch
REM Backup generated programs weekly
xcopy "src\reports\*" "backup\reports_%date:~-4,4%%date:~-10,2%%date:~-7,2%\" /E /I /Y
```

---

## 🚀 Future Automation Features (Roadmap)

### **Planned Enhancements:**
- [ ] Web-based scheduling interface
- [ ] Email distribution of completed programs
- [ ] SMS reminders for program creation
- [ ] Auto-populate dates and recurring information
- [ ] Integration with church calendar systems
- [ ] Mobile app notifications
- [ ] Voice assistant integration
- [ ] AI-powered content suggestions

---

**🤖 Automation makes program creation effortless and ensures consistency!**
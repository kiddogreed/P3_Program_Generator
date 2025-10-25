# Church Program Generator - Sunday Automation

## Quick Setup Guide

### For Windows Users

1. **Automatic Setup (Recommended)**
   ```batch
   # Right-click and "Run as Administrator"
   schedule-sunday-startup.bat
   ```
   
   This will:
   - Create a Windows scheduled task
   - Auto-start every Sunday at 7:00 AM
   - Open browser to Sacrament Meeting form

2. **Manual Testing**
   ```batch
   # Test the auto-start script
   sunday-auto-start.bat
   ```

3. **Verify Schedule**
   - Open Task Scheduler (`Win + R`, type `taskschd.msc`)
   - Look for "Church Program Generator - Sunday"
   - Verify it's scheduled for Sunday 7:00 AM

### For Mac/Linux Users

1. **Automatic Setup**
   ```bash
   # Make scripts executable
   chmod +x setup-sunday-cron.sh
   chmod +x sunday-auto-start.sh
   
   # Run setup
   ./setup-sunday-cron.sh
   ```

2. **Manual Cron Setup**
   ```bash
   # Edit crontab
   crontab -e
   
   # Add this line (adjust path):
   0 7 * * 0 cd /path/to/ProgramGenerator && bash sunday-auto-start.sh >> cron.log 2>&1
   ```

3. **Verify Cron Job**
   ```bash
   # List cron jobs
   crontab -l
   ```

### Manual Start

If you want to start manually instead of automatically:

```batch
# Windows
run-church-program.bat

# Mac/Linux
bash run-church-program.sh
```

---

## Customization

### Change Start Time

**Windows Task Scheduler:**
1. Open Task Scheduler
2. Find "Church Program Generator - Sunday"
3. Right-click → Properties
4. Go to Triggers tab
5. Edit trigger and change time

**Mac/Linux Cron:**
```bash
# Edit crontab
crontab -e

# Change the hour (7 = 7:00 AM, 6 = 6:00 AM, etc.)
0 6 * * 0 cd /path/to/ProgramGenerator && bash sunday-auto-start.sh
```

### Change Day (Not Just Sunday)

**Every Saturday:**
```
0 7 * * 6 ...
```

**Every Saturday AND Sunday:**
```
0 7 * * 0,6 ...
```

---

## Troubleshooting

### Application Doesn't Start

1. **Check Java Installation**
   ```bash
   java -version
   ```

2. **Check File Paths**
   - Verify script paths are correct
   - Ensure JAR file exists in target/

3. **Check Logs**
   - Windows: Check Task Scheduler history
   - Linux/Mac: Check `cron.log` file

### Browser Doesn't Open

1. Manually open: http://localhost:8080/sacrament
2. Check if port 8080 is available
3. Verify application started successfully

---

## Advanced Options

### Run as Windows Service

For 24/7 availability:

```batch
# Install NSSM (Non-Sucking Service Manager)
# Download from: https://nssm.cc/download

# Install service
nssm install ChurchProgramGenerator "C:\Program Files\Java\jdk-17\bin\java.exe" "-jar" "C:\projects\Church\ProgramGenerator\target\program-generator-0.0.1-SNAPSHOT.jar"

# Start service
nssm start ChurchProgramGenerator
```

### Email Notifications

Create a reminder email script (requires email server setup):

```powershell
# send-reminder.ps1
Send-MailMessage -To "clerk@ward.com" -From "reminder@ward.com" `
  -Subject "Create Sunday Program" `
  -Body "Don't forget to create today's sacrament program!" `
  -SmtpServer "smtp.gmail.com" -Port 587 -UseSsl
```

Schedule this to run Saturday evening or Sunday morning.

---

## What Gets Automated

✅ **Application starts automatically**  
✅ **Browser opens to Sacrament form**  
✅ **Current date ready to select**  
✅ **All forms accessible**  

❌ **Not Automated (Manual entry required):**
- Presiding/Conducting names
- Hymn numbers
- Speaker names and topics
- Preview and export

---

## Future Automation Ideas

- Auto-fill recurring leaders
- Remember common hymns
- Speaker rotation suggestions
- Template-based programs
- Email distribution of completed programs

---

For complete automation documentation, see:
- `AUTOMATION_GUIDE.md` - Comprehensive automation guide
- `DEPLOYMENT_GUIDE.md` - Deployment options

**Happy Automating! 🤖**
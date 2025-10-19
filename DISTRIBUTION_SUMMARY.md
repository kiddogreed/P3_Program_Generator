# 🏛️ Church Program Generator - Distribution Methods

## 📦 Available Distribution Options

### ✅ **Option 1: Simple JAR Distribution (RECOMMENDED for most users)**

**What's included in the `deployment` folder:**
- `program-generator-0.0.1-SNAPSHOT.jar` - The complete application
- `run-church-program.bat` - Windows startup script
- `run-church-program.sh` - Mac/Linux startup script
- `README.txt` - Quick start instructions

**Requirements:** Java 17+ on target device

**How to distribute:**
1. Copy the entire `deployment` folder to target device
2. Double-click the appropriate script for the operating system
3. Access at http://localhost:8080

### 🐳 **Option 2: Docker Container (NO Java required)**

**Commands to run:**
```bash
# Build the container
docker build -t church-program-generator .

# Run the container
docker run -p 8080:8080 church-program-generator

# Or use Docker Compose
docker-compose up -d
```

**Benefits:**
- No Java installation required
- Consistent across all operating systems
- Isolated environment

### ☁️ **Option 3: Cloud Deployment (Access from anywhere)**

**Free hosting options:**
- **Railway**: Connect GitHub repo for automatic deployment
- **Render**: Free tier with easy deployment
- **Heroku**: Classic option (limited free tier)

**Benefits:**
- No local installation needed
- Accessible from any device with internet
- Professional deployment

### 🌐 **Option 4: Network Access (Local network sharing)**

**Current configuration allows network access:**
- Other devices on same WiFi can access the application
- Find your computer's IP address: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
- Others access via: `http://YOUR-IP-ADDRESS:8080`

## 🔧 **Technical Details**

### Built Application Size:
- JAR file: ~44 MB (includes all dependencies)
- Contains embedded Tomcat server
- Self-contained Spring Boot application

### System Requirements:
- **With JAR**: Java 17+ (any operating system)
- **With Docker**: Docker Desktop (any operating system)
- **Cloud**: Just a web browser

### Network Configuration:
- Default port: 8080
- Network access: Enabled (0.0.0.0)
- Can be changed in `application.properties`

## 📋 **Distribution Checklist**

### For Church Members/Leaders:
- [ ] Copy `deployment` folder to their computer
- [ ] Ensure Java 17+ is installed
- [ ] Run the startup script
- [ ] Bookmark http://localhost:8080

### For IT-Savvy Users:
- [ ] Provide Docker files for containerized deployment
- [ ] Share GitHub repository for cloud deployment
- [ ] Configure custom domain if needed

### For Remote Access:
- [ ] Deploy to cloud platform
- [ ] Share public URL with authorized users
- [ ] Consider authentication if needed

## 🚀 **Quick Start for Recipients**

1. **Download** the `deployment` folder
2. **Install Java 17+** from https://adoptium.net/ (if not already installed)
3. **Run** the appropriate script:
   - Windows: Double-click `run-church-program.bat`
   - Mac/Linux: Run `bash run-church-program.sh`
4. **Open browser** to http://localhost:8080
5. **Create programs** for your church meetings!

## 📞 **Support Information**

**If users encounter issues:**
- Check Java installation: `java -version`
- Ensure port 8080 is not in use
- Check firewall settings for network access
- Review `DEPLOYMENT_GUIDE.md` for detailed instructions

**Contact:** [Your contact information here]
# 🏛️ Church Program Generator

A professional Spring Boot web application for creating and managing church meeting programs with elegant document generation capabilities.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## ✨ Features

### 🎵 Sacrament Meeting Programs
- **Hymn Management**: Opening, Sacrament, and Closing hymns with numbers
- **Speaker Details**: Multiple speakers with topics and auxiliary assignments
- **Leadership Roles**: Presiding and conducting assignments
- **Professional Layout**: Elegant bordered design with church branding
- **Export Options**: Word (.docx) and PDF document generation

### 📋 Ward Council Meetings
- **Beautiful Table Layout**: Professional agenda format with Christ background
- **Meeting Structure**: Opening/Closing prayers, Handbook readings, Auxiliary reports
- **Business Items**: Agenda, Welfare, and Administrative matters
- **Document Export**: Professional Word and PDF documents with table formatting

### 👔 Bishopric Meetings
- **Executive Format**: Leadership meeting structure with professional styling
- **Agenda Management**: Handbook spiritual thoughts, business items, callings & releases
- **Table Layout**: Elegant red-bordered table matching ward council design
- **Document Generation**: Professional Word and PDF exports with proper formatting

## 🚀 Quick Start

### Prerequisites
- **Java 17+** (Required for Spring Boot 3.x)
- **Maven 3.6+** (Build tool)
- **Git** (Version control)

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/kiddogreed/P3_Program_Generator.git
   cd P3_Program_Generator
   ```

2. **Build the Application**
   ```bash
   mvn clean compile
   ```

3. **Run the Application**
   ```bash
   # Option 1: Using Maven (Recommended for development)
   mvn spring-boot:run
   
   # Option 2: Using custom port
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
   
   # Option 3: Build and run JAR
   mvn clean package
   java -jar target/program-generator-0.0.1-SNAPSHOT.jar
   ```

4. **Access the Application**
   - Open your browser and navigate to: `http://localhost:8080`
   - For custom port: `http://localhost:8081`

## 📖 How to Use the Application

### 🎵 Creating Sacrament Meeting Programs

1. **Navigate to Sacrament Programs**
   - Go to `http://localhost:8080/sacrament`
   - Click "Sacrament Meeting" in the navigation menu

2. **Fill Out the Program Details**
   - **Meeting Date**: Select the meeting date
   - **Presiding**: Enter the presiding authority (e.g., "Bishop Smith")
   - **Conducting**: Enter who is conducting (e.g., "President Johnson")
   - **Hymns**: Enter hymn numbers for Opening, Sacrament, and Closing
   - **Speakers**: Add speaker names and topics
   - **Speakers Auxiliary**: Select which auxiliary the speakers are from:
     - Elders Quorum
     - Relief Society
     - Family History and Missionary Work
     - Youth Organization
     - Primary Organization
     - Stake Organization

3. **Preview Your Program**
   - Click "Preview Program" to see the formatted layout
   - Review all information for accuracy

4. **Export Documents**
   - **Word Document**: Click "Export to Word" for .docx file
   - **PDF Document**: Click "Export to PDF" for .pdf file
   - Files are automatically saved to `src/reports/` with date stamps

### 📋 Creating Ward Council Meetings

1. **Navigate to Ward Council**
   - Go to `http://localhost:8080/ward-council`
   - Click "Ward Council Meeting" in the navigation menu

2. **Enter Meeting Information**
   - **Ward Name**: Enter your ward name (e.g., "Pasay 3rd Ward")
   - **Meeting Date**: Select the meeting date
   - **Opening Prayer**: Assign opening prayer
   - **Handbook Reading**: Enter handbook spiritual thought assignment
   - **Auxiliary Reports**: List auxiliary organizations reporting
   - **Agenda Items**: Enter business items (one per line)
   - **Welfare**: Enter welfare-related items
   - **Closing Prayer**: Assign closing prayer

3. **Preview and Export**
   - Click "Preview Program" to see the elegant table layout
   - Export to Word or PDF formats
   - Documents saved with naming: `wardCouncilMeeting{date}.docx/pdf`

### 👔 Creating Bishopric Meetings

1. **Navigate to Bishopric Meetings**
   - Go to `http://localhost:8080/bishopric`
   - Click "Bishopric Meeting" in the navigation menu

2. **Fill Meeting Details**
   - **Ward Name**: Enter ward name (defaults to "Pasay 3rd Ward")
   - **Meeting Date**: Select meeting date
   - **Presiding**: Enter bishop's name
   - **Conducting**: Enter conducting authority
   - **Opening Prayer**: Assign opening prayer
   - **Handbook Spiritual**: Enter handbook thought or reference
   - **Agenda Items**: Enter agenda items (one per line)
   - **Callings and Releases**: Enter administrative items
   - **Closing Prayer**: Assign closing prayer

3. **Preview and Export**
   - Preview shows professional table layout with red borders
   - Export to Word/PDF formats
   - Files saved as: `bishopricMeeting{date}.docx/pdf`

## 📁 Document Export System

### 🗂️ Organized File Storage Structure
```
src/reports/
├── sacrament/                          # Sacrament Meeting Programs
│   ├── sacramentProgram2025-10-12.docx
│   ├── sacramentProgram2025-10-12.pdf
│   ├── sacramentProgram2025-10-19.docx
│   └── sacramentProgram2025-10-19.pdf
├── wardcouncil/                        # Ward Council Meetings  
│   ├── wardCouncilMeeting2025-10-12.docx
│   ├── wardCouncilMeeting2025-10-12.pdf
│   ├── wardCouncilMeeting2025-11-02.docx
│   └── wardCouncilMeeting2025-11-02.pdf
└── bishopric/                          # Bishopric Meetings
    ├── bishopricMeeting2025-10-12.docx
    ├── bishopricMeeting2025-10-12.pdf
    ├── bishopricMeeting2025-10-15.docx
    └── bishopricMeeting2025-10-15.pdf
```

#### **📋 Organized Benefits**
- **Easy Navigation**: Find documents by meeting type quickly
- **Better Organization**: Separate folders prevent file mixing
- **Scalable Structure**: Handles large numbers of documents efficiently
- **Clear Categorization**: Professional file management system

### 📄 Document Features

#### **Word Documents (.docx)**
- **Professional Formatting**: Bold headers, proper spacing, clean layout
- **Church Branding**: "The Church of Jesus Christ of Latter-day Saints" header
- **Table Layout**: Structured table format for Ward Council and Bishopric meetings
- **Font Styling**: Consistent typography with 10-12pt fonts
- **Borders and Spacing**: Professional appearance with proper margins

#### **PDF Documents (.pdf)**
- **High Quality**: Vector-based text and graphics
- **Color Coding**: Red borders and headers for professional appearance
- **Table Structure**: Bordered tables matching web preview
- **Print Ready**: Optimized for printing and digital distribution
- **Consistent Formatting**: Matches Word document layout

### 💾 How to Download Documents

1. **Automatic Download**: Documents automatically download when "Export" is clicked
2. **File Location**: Documents are also saved to `src/reports/` directory
3. **File Naming**: Files include meeting type and date (YYYY-MM-DD format)
4. **Both Formats**: Generate both Word and PDF versions as needed

## 🏗️ Application Architecture

### 📂 Project Structure
```
src/
├── main/
│   ├── java/com/church/programgenerator/
│   │   ├── controller/                    # Web Controllers
│   │   │   ├── HomeController.java        # Root path handler
│   │   │   ├── SacramentController.java   # Sacrament meeting logic
│   │   │   ├── WardCouncilController.java # Ward council logic  
│   │   │   └── BishopricController.java   # Bishopric meeting logic
│   │   ├── model/                         # Data Models
│   │   │   ├── SacramentProgram.java      # Sacrament meeting model
│   │   │   ├── WardCouncilProgram.java    # Ward council model
│   │   │   └── BishopricProgram.java      # Bishopric meeting model
│   │   ├── service/                       # Business Logic Services
│   │   │   ├── SacramentProgramDocumentService.java    # Word generation
│   │   │   ├── SacramentProgramPreviewService.java     # HTML preview
│   │   │   ├── WardCouncilDocumentService.java         # Word generation
│   │   │   ├── WardCouncilPdfService.java              # PDF generation
│   │   │   ├── BishopricProgramDocumentService.java    # Word generation
│   │   │   ├── BishopricProgramPdfService.java         # PDF generation
│   │   │   └── FileStorageService.java                 # File management
│   │   └── ProgramGeneratorApplication.java            # Main application
│   └── resources/
│       ├── static/css/
│       │   └── styles.css                 # Application styling
│       ├── templates/                     # Thymeleaf Templates
│       │   ├── layout.html               # Base layout with navigation
│       │   ├── index.html                # Home page
│       │   ├── sacrament.html            # Sacrament meeting form
│       │   ├── sacrament-preview.html    # Sacrament preview
│       │   ├── ward-council.html         # Ward council form
│       │   ├── ward-council-preview.html # Ward council preview
│       │   ├── bishopric.html            # Bishopric meeting form
│       │   └── bishopric-preview.html    # Bishopric preview
│       └── application.properties         # Configuration
└── reports/                              # Generated documents storage
```

### 🔧 Technology Stack

#### **Backend Framework**
- **Spring Boot 3.1.5**: Modern Java framework for web applications
- **Spring Web MVC**: RESTful web services and controllers  
- **Spring DevTools**: Development hot-reloading support
- **Java 17+**: Latest LTS version with modern features

#### **Frontend Technologies**
- **Thymeleaf**: Server-side template engine for dynamic HTML
- **HTML5**: Modern semantic markup
- **CSS3**: Advanced styling with Grid and Flexbox
- **Responsive Design**: Mobile-first approach with media queries
- **JavaScript**: Form validation and interactive elements

#### **Document Generation**
- **Apache POI 5.2.4**: Microsoft Office document manipulation
  - XWPFDocument: Word document creation
  - XWPFTable: Professional table layouts
  - XWPFParagraph: Text formatting and styling
- **iText 8.0.2**: PDF generation library
  - PdfDocument: PDF creation and manipulation  
  - Table/Cell: Structured table layouts
  - Font and Color: Professional typography

#### **Build & Development**
- **Maven 3.6+**: Dependency management and build automation
- **Spring Boot Starter**: Auto-configuration and embedded server
- **Tomcat Embedded**: Built-in web server (port 8080 default)

## 🎨 Design System

### 🎨 Visual Design
- **Color Scheme**: 
  - Primary Blue: #2c5282
  - Accent Teal: #4a90a4  
  - Alert Red: #e74c3c
  - Neutral Grays: #f7fafc, #e2e8f0
- **Typography**: System fonts with proper hierarchy
- **Layout**: Clean, professional church program aesthetic
- **Responsive**: Mobile-friendly design for all devices

### 📋 Layout Patterns
- **Card-Based Forms**: Elegant input forms with rounded corners
- **Table Layouts**: Professional meeting agendas with borders
- **Navigation**: Consistent header navigation across all pages
- **Preview System**: Live preview before document generation

## ⚙️ Configuration

### 🔧 Application Properties
```properties
# Server Configuration  
server.port=8080

# Thymeleaf Configuration (Development)
spring.thymeleaf.cache=false

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 🚀 Environment Setup

#### **Development Mode**
```bash
# Run in development mode (hot reload enabled)
mvn spring-boot:run

# Custom port for development
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

#### **Production Deployment**
```bash
# Build production JAR
mvn clean package -DskipTests

# Run production build
java -jar target/program-generator-0.0.1-SNAPSHOT.jar

# With custom configuration
java -jar -Dserver.port=8080 target/program-generator-0.0.1-SNAPSHOT.jar
```

## 📦 Runnable JAR Export

### ✅ **Yes! Your Spring Boot application can be exported as a standalone runnable JAR file.**

Your Church Program Generator is already configured to create a **fully executable JAR** that includes:
- ✅ **Embedded Tomcat Server** - No external server needed
- ✅ **All Dependencies** - Self-contained with all libraries
- ✅ **Static Resources** - CSS, templates, and assets included
- ✅ **Configuration** - Application properties bundled
- ✅ **Cross-Platform** - Runs on any system with Java 17+

### 🚀 **Creating the Runnable JAR**

#### **Step 1: Build the JAR**
```bash
# Clean and build the executable JAR
mvn clean package

# Skip tests for faster build (optional)
mvn clean package -DskipTests
```

#### **Step 2: Locate the JAR File**
```bash
# The JAR will be created at:
target/program-generator-0.0.1-SNAPSHOT.jar

# File size: ~35MB (includes all dependencies)
```

#### **Step 3: Run the JAR**
```bash
# Basic execution (default port 8080)
java -jar target/program-generator-0.0.1-SNAPSHOT.jar

# Custom port
java -jar target/program-generator-0.0.1-SNAPSHOT.jar --server.port=8082

# Background execution
nohup java -jar target/program-generator-0.0.1-SNAPSHOT.jar &

# Windows background (using start)
start java -jar target/program-generator-0.0.1-SNAPSHOT.jar
```

### 🖥️ **Distribution Options**

#### **1. Standalone Distribution**
```bash
# Create a distribution folder
mkdir church-program-generator
cp target/program-generator-0.0.1-SNAPSHOT.jar church-program-generator/
cd church-program-generator

# Create startup scripts
# Windows (run.bat)
echo java -jar program-generator-0.0.1-SNAPSHOT.jar > run.bat

# Linux/Mac (run.sh)
echo "#!/bin/bash" > run.sh
echo "java -jar program-generator-0.0.1-SNAPSHOT.jar" >> run.sh
chmod +x run.sh
```

#### **2. Service Installation (Linux)**
```bash
# Create systemd service file
sudo tee /etc/systemd/system/church-program-generator.service > /dev/null <<EOF
[Unit]
Description=Church Program Generator
After=syslog.target

[Service]
User=churchapp
ExecStart=/usr/bin/java -jar /opt/church-program-generator/program-generator-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF

# Enable and start service
sudo systemctl enable church-program-generator
sudo systemctl start church-program-generator
```

#### **3. Docker Containerization**
```dockerfile
# Create Dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app
COPY target/program-generator-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
# Build and run Docker container
docker build -t church-program-generator .
docker run -p 8080:8080 church-program-generator
```

### ⚙️ **Runtime Configuration**

#### **System Requirements for JAR Deployment**
- **Java Runtime**: JRE 17 or higher
- **Memory**: Minimum 256MB RAM (recommended 512MB)
- **Storage**: 50MB for JAR + space for generated documents
- **Network**: Any available port (default 8080)

#### **Configuration Options**
```bash
# Memory settings
java -Xmx512m -jar program-generator-0.0.1-SNAPSHOT.jar

# Custom application properties
java -jar program-generator-0.0.1-SNAPSHOT.jar \
  --server.port=8081 \
  --spring.thymeleaf.cache=true

# External configuration file
java -jar program-generator-0.0.1-SNAPSHOT.jar \
  --spring.config.location=classpath:/application.properties,./config/
```

### 🌍 **Network Access & Deployment**

#### **Local Network Access**
```bash
# Allow access from other computers on network
java -jar program-generator-0.0.1-SNAPSHOT.jar \
  --server.address=0.0.0.0 \
  --server.port=8080

# Access from other computers: http://YOUR-IP:8080
```

#### **Cloud Deployment Ready**
- ✅ **AWS**: Deploy to EC2, Elastic Beanstalk, or ECS
- ✅ **Google Cloud**: Deploy to Compute Engine or Cloud Run
- ✅ **Azure**: Deploy to Virtual Machines or App Service
- ✅ **Heroku**: Direct JAR deployment supported
- ✅ **VPS**: Any Linux/Windows VPS with Java 17+

### 📋 **JAR Deployment Checklist**

#### **Before Distribution:**
- [ ] Build JAR with `mvn clean package`
- [ ] Test JAR locally: `java -jar target/program-generator-0.0.1-SNAPSHOT.jar`
- [ ] Verify all features work (forms, previews, document generation)
- [ ] Check generated documents in `src/reports/` directory
- [ ] Test on target deployment environment

#### **For Production:**
- [ ] Configure appropriate server port
- [ ] Set up proper file permissions for `reports/` directory
- [ ] Configure firewall rules for chosen port
- [ ] Set up monitoring/logging if needed
- [ ] Create backup strategy for generated documents
- [ ] Document access URLs for users

### 🎯 **Distribution Examples**

#### **Church Office Distribution**
```bash
# Package for church office computers
mkdir ChurchProgramGenerator-v1.0
cp target/program-generator-0.0.1-SNAPSHOT.jar ChurchProgramGenerator-v1.0/
echo "java -jar program-generator-0.0.1-SNAPSHOT.jar" > ChurchProgramGenerator-v1.0/START.bat
zip -r ChurchProgramGenerator-v1.0.zip ChurchProgramGenerator-v1.0/
```

#### **USB Portable Version**
```bash
# Create portable version for USB drive
mkdir PortableChurchPrograms
cp target/program-generator-0.0.1-SNAPSHOT.jar PortableChurchPrograms/
echo "@echo off" > PortableChurchPrograms/run-portable.bat
echo "java -jar program-generator-0.0.1-SNAPSHOT.jar --server.port=8080" >> PortableChurchPrograms/run-portable.bat
echo "pause" >> PortableChurchPrograms/run-portable.bat
```

**Your Church Program Generator is fully ready for JAR deployment! 🎉**

## 🧪 Testing & Quality

### 🔍 Code Quality
- **Spring Boot Best Practices**: Following Spring conventions
- **Separation of Concerns**: Clear MVC architecture
- **Error Handling**: Graceful error management
- **Responsive Design**: Cross-browser compatibility

### 📱 Browser Support
- **Chrome/Chromium**: Full support
- **Firefox**: Full support  
- **Safari**: Full support
- **Edge**: Full support
- **Mobile Browsers**: Responsive design optimized

## 🤝 Contributing

### 🔄 Development Workflow
1. **Fork the Repository**: Create your own fork
2. **Create Feature Branch**: `git checkout -b feature/new-feature`
3. **Make Changes**: Implement your improvements
4. **Test Locally**: Ensure application runs properly
5. **Commit Changes**: `git commit -m "Add new feature"`
6. **Push Branch**: `git push origin feature/new-feature`
7. **Create Pull Request**: Submit for review

### 📋 Development Guidelines
- Follow Spring Boot conventions
- Maintain consistent code formatting
- Add comments for complex logic
- Test all new features thoroughly
- Update documentation as needed

## 🐛 Troubleshooting

### Common Issues

#### **Port Already in Use**
```bash
# Check what's using port 8080
netstat -ano | findstr :8080

# Run on different port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

#### **Java Version Issues**
```bash
# Check Java version
java -version

# Ensure Java 17+ is installed
# Download from: https://adoptium.net/
```

#### **Maven Build Errors**
```bash
# Clean and rebuild
mvn clean compile

# Update dependencies
mvn clean compile -U
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**kiddogreed**
- GitHub: [@kiddogreed](https://github.com/kiddogreed)
- Repository: [P3_Program_Generator](https://github.com/kiddogreed/P3_Program_Generator)

---

## � Future Features & Roadmap

### 🗄️ **Database Integration**
- **Program History**: Save and retrieve historical meeting programs
- **Template Management**: Create and save custom program templates
- **Speaker Database**: Maintain speaker information and topics library
- **Calling Management**: Track ward leadership and calling assignments
- **Statistics**: Generate reports on meeting frequency and participation

### 🔐 **Authentication & Authorization**
- **User Roles**: Differentiated access for Bishop, Bishopric, Clerks, and Members
- **Ward Integration**: Multi-ward support for stake-level administration
- **Login System**: Secure authentication with LDS Account integration
- **Permission Levels**: Role-based access to different meeting types
- **Audit Trail**: Track who created and modified programs

### 📱 **Mobile & Responsive Enhancements**
- **Progressive Web App (PWA)**: Install as mobile app
- **Offline Mode**: Create programs without internet connection
- **Mobile-First Forms**: Touch-optimized input fields
- **Push Notifications**: Meeting reminders and program updates
- **QR Code Generation**: Quick sharing of digital programs

### 🎨 **Advanced Document Features**
- **Custom Branding**: Ward-specific logos and styling
- **Multiple Templates**: Various program layout options
- **Bulk Export**: Generate multiple programs simultaneously
- **Email Integration**: Direct email distribution of programs
- **Print Optimization**: Enhanced print layouts with margins and scaling
- **Digital Signatures**: Bishop approval workflow for programs

### 🔄 **Automation & Integration**
- **Calendar Integration**: Sync with LCR (Leader and Clerk Resources)
- **Email Automation**: Automatic program distribution
- **Recurring Events**: Template-based recurring meeting setup
- **API Integration**: Connect with church systems and databases
- **Backup & Sync**: Cloud storage integration (Google Drive, OneDrive)

### 📊 **Analytics & Reporting**
- **Usage Analytics**: Track program creation and downloads
- **Attendance Tracking**: Integration with attendance systems
- **Performance Metrics**: Meeting efficiency and participation reports
- **Trend Analysis**: Historical data visualization and insights
- **Export Analytics**: Document generation statistics

### 🌐 **Multi-Language Support**
- **Internationalization**: Support for multiple languages
- **RTL Support**: Right-to-left language compatibility
- **Cultural Adaptations**: Region-specific meeting formats
- **Translation Management**: Dynamic language switching
- **Local Customizations**: Country and stake-specific variations

### 🎵 **Enhanced Meeting Features**
- **Music Integration**: Hymn lyrics and sheet music display
- **Video Conferencing**: Integration with Zoom/Teams for hybrid meetings
- **Digital Announcements**: Dynamic announcement management
- **Photo Integration**: Add photos to programs and announcements
- **Multimedia Support**: Audio/video content embedding

### 🛠️ **Administrative Tools**
- **Batch Operations**: Bulk program creation and management
- **Data Import/Export**: CSV import for speaker lists and schedules
- **Backup Management**: Automated backup and restore functionality
- **System Monitoring**: Health checks and performance monitoring
- **Configuration Management**: Advanced settings and customization options

### 📈 **Advanced Analytics Dashboard**
- **Real-time Statistics**: Live usage and generation metrics
- **Trend Visualization**: Charts and graphs for program trends
- **Performance Insights**: System performance and optimization suggestions
- **User Engagement**: Track feature usage and user satisfaction
- **Predictive Analytics**: Suggest optimal meeting scheduling and content

### 🔧 **Developer & Integration Features**
- **REST API**: Full API for external system integration
- **Webhook Support**: Event-driven integrations with external services
- **Plugin Architecture**: Extensible plugin system for custom features
- **Theme Framework**: Custom theme development capabilities
- **SDK Development**: Software development kit for third-party integrations

### 🎯 **Specialized Meeting Types**
- **Stake Conferences**: Multi-ward conference program generation
- **Youth Programs**: Specialized youth meeting formats
- **Primary Programs**: Children's program templates with activities
- **Relief Society/Elders Quorum**: Auxiliary-specific program formats
- **Temple Sessions**: Temple-related program management
- **Special Events**: Holiday and special occasion program templates

### 💡 **Innovation Features**
- **AI-Powered Suggestions**: Smart content recommendations
- **Voice Input**: Speech-to-text for program creation
- **Smart Templates**: Context-aware template suggestions
- **Automated Scheduling**: AI-driven optimal meeting scheduling
- **Content Library**: Shared repository of talks, lessons, and resources

### 🏗️ **Technical Improvements**
- **Microservices Architecture**: Scalable service-oriented design
- **Cloud Native**: Kubernetes and Docker containerization
- **Performance Optimization**: Caching and database optimization
- **Real-time Collaboration**: Multiple users editing simultaneously
- **Version Control**: Program versioning and change tracking

---

## 📋 Implementation Priority

### **Phase 1** (Short-term - 3-6 months)
1. Database integration for program storage
2. Basic user authentication
3. Mobile responsive improvements
4. Enhanced document templates

### **Phase 2** (Medium-term - 6-12 months)
1. Multi-language support
2. Calendar integration
3. Advanced reporting features
4. API development

### **Phase 3** (Long-term - 1+ years)
1. AI-powered features
2. Microservices architecture
3. Advanced analytics dashboard
4. Plugin ecosystem

---

## �🙏 Acknowledgments

- **Spring Boot Team**: For the excellent framework
- **Apache POI**: For Word document generation capabilities  
- **iText**: For professional PDF generation
- **The Church of Jesus Christ of Latter-day Saints**: For inspiration and use case

---

**Built with ❤️ for church program management and organization**
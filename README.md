# 🏛️ Pasay 3rd Ward Program Generator

A modern, professional Spring Boot web application for creating and managing church meeting programs with elegant document generation capabilities and MSN-style navigation interface.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)

## 🆕 **Latest Updates (April 2026)**

### **📱 Full Mobile Browser Compatibility (April 2026)**
- **Hamburger Navigation**: Navigation bar collapses into a hamburger menu (☰) on screens ≤768px — tap to expand/collapse; menu auto-closes on link tap or outside click
- **Responsive Forms**: Single-column layout with full-width buttons on mobile; all form rows stack vertically
- **Speaker Rows Stack**: Dynamically added speaker rows wrap and stack on screens ≤520px
- **iOS Auto-Zoom Fix**: All form inputs/textareas/selects set to `font-size: 16px` on ≤480px screens — prevents iOS Safari from zooming in on focus
- **History Table**: Low-priority columns hidden on small screens; action buttons stack vertically
- **Preview & Export**: Single-column layout for preview containers and export button groups on mobile
- **Bishopric Forms**: Reduced padding/margin; form rows forced to single-column grid on mobile
- **Cross-Browser Tested**: Chrome Mobile, Safari iOS, Samsung Internet, Firefox Mobile

### **☁️ Cloud Deployment Ready (April 2026)**
- **Environment Variable Support**: `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` now override `application.properties` — no code change needed for cloud deployment
- **Local Fallback**: Defaults to `localhost:5432/church_programs` when env vars are absent — local dev unchanged
- **Railway / Render / Fly.io Ready**: Connect GitHub repo → add PostgreSQL plugin → set 3 env vars → live URL

### **🎨 Bishopric Meeting PDF Redesign (April 2026)**
- **Elegant Single-Page Layout**: Full lavender `#e8e4f0` background with double-border frame
- **C-Scroll Corner Ornaments**: Decorative PdfCanvas-drawn ornaments at each corner
- **Serif Typography**: Times New Roman / Times fonts for a formal, traditional look
- **Centered Layout**: All content centered — ward name, date, logo, meeting details
- **Auto-Scale**: Font size auto-adjusts (scale 0.72–1.0) to fill the page without overflow
- **PDF Only**: DOCX export removed from Bishopric preview — PDF is the sole export format

### **🎯 Document Output Overhaul — DOCX & PDF Match Preview (April 2026)**
- **Blue Section Header**: Program header now renders in church blue (`#2c5282`) at 12pt bold in both DOCX and PDF — matching the HTML preview
- **Consistent Font Sizing**: All body text uniformly set to 11pt (DOCX) / 10pt (PDF); previously inconsistent across sections
- **Centered Music Row**: Chorister/Pianist row is center-aligned with pipe separator in both export formats
- **Announcements in PDF**: Announcements section was previously missing from PDF exports — now included
- **Blank Lines for Handwriting**: Empty lines added after Acknowledgement, Announcements, and Ward Business for pen annotations on printed copies

### **📐 Adaptive Font Scaling**
- **Auto Scaling on Overflow**: When total field content exceeds threshold, font size automatically scales down to prevent text overflow:
  - ≤450 chars → 11pt/10pt · ≤700 → 10pt/9pt · ≤1000 → 9pt/8.5pt · >1000 → 8pt/7.5pt
- **Per-Document Calculation**: `computeDocxAdaptiveFontSize()` and `computePdfAdaptiveFontSize()` count characters across variable fields (Acknowledgement, Announcements, Ward Business, Stake Business, Speakers)

### **🏷️ Speakers Auxiliary Display Fix**
- **Bold Value Display**: Speakers auxiliary (e.g. "Relief Society", "Sunday School") now shown in bold on the same line as the label — `Speakers: Relief Society`
- **Removed Parentheses & Italic**: Previously displayed as italic `(Relief Society)` — updated in HTML preview, DOCX, and PDF services
- **Consistent Across All Outputs**: Fix applied to `SacramentProgramPreviewService`, `SacramentProgramDocumentService`, and `FileStorageService`

### **🧪 Test Preview Endpoint**
- **`GET /sacrament/test-preview`**: New endpoint pre-fills all form fields with real Pasay 3rd Ward data for quick full-program testing
- **"🧪 Test Full Preview" Button**: Added to the sacrament form's action buttons for one-click access
- **Announcements Fix**: Fixed `param.announcements[0]` → `announcementsText` model attribute in `sacrament-preview.html` so announcements survive the round-trip through preview → export

### **🗄️ PostgreSQL Migration**
- **PostgreSQL 17**: Switched from H2 to PostgreSQL — database `church_programs` on `localhost:5432`
- **Auto DDL**: `ddl-auto=update` creates/updates the `saved_programs` table on startup
- **Driver Updated**: `pom.xml` replaced H2 dependency with `org.postgresql:postgresql` runtime driver

### **🗄️ Database Integration for Program Storage** *(earlier April 2026)*
- **Auto-Save on Export**: Every DOCX or PDF export silently saves the program to the database
- **Saved Programs History**: Browse, reload, and delete past programs from a dedicated History page
- **Type Filtering**: Filter history by Sacrament, Ward Council, or Bishopric meeting type
- **One-Click Reload**: Load any saved program back into its form, pre-filled and ready to edit

### **🛡️ Enhanced Error Handling & User Feedback**
- **Global Exception Handler**: Centralized `@ControllerAdvice` catches validation errors, type mismatches, and unexpected exceptions — no more raw stack traces
- **User-Friendly Error Page**: Clean error card with title, message, Go Back, and Home buttons
- **Bean Validation**: `@NotBlank` and `@NotNull` enforce required fields (ward name, meeting date) on all three program models
- **Flash Alert Banners**: Success and error messages displayed inline on form pages (e.g. "Program loaded from history")
- **Structured Error Responses**: Export endpoints return descriptive error messages instead of empty 500 responses

### **⚡ Performance Optimization & Caching**
- **Caffeine Cache**: In-memory cache (max 200 entries, 30-minute TTL) via `spring.cache.type=caffeine`
- **`@Cacheable` on History Queries**: `getAllPrograms()` and `getProgramsByType()` served from cache
- **`@CacheEvict` on Mutations**: Cache invalidated automatically on every save or delete
- **GZIP Compression**: Server-side compression enabled for HTML, CSS, JS, and JSON responses (min 1 KB)
- **Thymeleaf Production Cache**: Easily toggled for production deployments

### **🎨 Additional Document Templates & Formatting**
- **New UI Components**: Alert banners, meeting-type badges, filter buttons, and small action buttons added to the design system
- **Responsive History Table**: Sortable columns, type-color badges, and mobile-friendly layout
- **CSS Version Bump**: All new styles appended; backward-compatible with existing forms

### **✨ MSN-Style Navigation Interface** *(October 2025)*
- **Dark Gradient Navigation**: Professional MSN-inspired navigation bar with deep blue gradient
- **Rounded Tab Design**: Modern rounded tabs with smooth hover animations
- **Active Tab Highlighting**: Visual feedback with accent colors and subtle effects
- **Responsive Design**: Mobile-optimized navigation that adapts to all screen sizes
- **Backdrop Blur Effects**: Modern glass-morphism styling for enhanced visual appeal

### **📦 Deployment Ready** *(October 2025)*
- **Standalone JAR Distribution**: Complete deployment package for easy distribution
- **Docker Containerization**: Containerized deployment with no Java installation required
- **Network Access**: Configured for local network sharing across devices
- **Production Scripts**: Ready-to-use startup scripts for Windows, Mac, and Linux
- **Cloud Deployment Ready**: Optimized for Heroku, Railway, Render, and other cloud platforms

## ✨ Core Features

### 🎵 Sacrament Meeting Programs
- **Complete Meeting Structure**: Opening/Sacrament/Closing hymns with speaker management
- **Speaker Assignment System**: Multiple speakers with auxiliary organization label displayed bold inline (e.g. `Speakers: Relief Society`)
- **Leadership Integration**: Presiding and conducting assignments with professional formatting
- **Adaptive Font Scaling**: Automatically reduces font size when content is long to prevent overflow
- **Blank Annotation Lines**: Empty lines after Acknowledgement, Announcements, and Ward Business for pen annotations
- **Elegant Document Generation**: Blue-header layout matching the HTML preview in both DOCX and PDF
- **Multi-Format Export**: Professional Word (.docx) and PDF document generation

### 📋 Ward Council Meetings
- **Professional Table Layouts**: Elegant agenda format with structured business items
- **Comprehensive Meeting Structure**: Prayer assignments, handbook readings, auxiliary reports
- **Business Item Management**: Organized agenda, welfare, and administrative matters
- **Christ-Centered Design**: Background imagery and professional styling
- **Automatic Document Export**: Seamless Word and PDF generation with date stamping

### 👔 Bishopric Meetings
- **Executive Meeting Format**: Leadership-focused meeting structure and agenda management
- **Administrative Tools**: Handbook spiritual thoughts, callings & releases, business items
- **Professional Styling**: Red-bordered tables matching organizational standards
- **Leadership Integration**: Bishop and counselor assignment tracking
- **Document Generation**: High-quality Word and PDF exports with proper formatting

### 🌐 Modern Navigation System
- **MSN-Style Interface**: Dark gradient navigation with professional appearance
- **Tabbed Navigation**: Intuitive tab system for different meeting types
- **History Tab**: Browse and reload saved programs from the navigation bar
- **Hamburger Menu**: Collapses to a ☰ toggle button on mobile — animated, accessible
- **Coming Soon Features**: Visual indicators for future functionality (Speaker Invites)
- **Mobile Responsive**: Full hamburger nav on ≤768px; iOS/Android tested
- **Visual Feedback**: Active states, hover effects, and smooth transitions

### 🗄️ Program History & Database
- **Auto-Save**: Programs are saved to the database every time a document is exported
- **History Browser**: Dedicated `/history` page lists all saved programs sorted by date
- **Reload Saved Programs**: Load any historical program back into an editable form
- **Type Filtering**: Filter history by meeting type (Sacrament, Ward Council, Bishopric)
- **Delete Records**: Remove outdated entries directly from the history table
- **Persistent Storage**: PostgreSQL database keeps data between restarts

### 🛡️ Error Handling & Validation
- **Global Exception Handler**: All errors route to a clean, user-friendly error page
- **Form Validation**: Required fields enforced with descriptive inline messages
- **Flash Notifications**: Success and error banners on every form page
- **Safe Exports**: Export failures return a human-readable message, not a crash

## 🚀 Quick Start Guide

### Prerequisites
- **Java 17+** (Required for Spring Boot 3.x)
- **Maven 3.6+** (Build automation tool)
- **PostgreSQL 17** (Database server — create database `church_programs` before first run)
- **Git** (Version control - optional for ZIP download)

### 💻 Development Setup

1. **Clone or Download Repository**
   ```bash
   git clone https://github.com/kiddogreed/P3_Program_Generator.git
   cd P3_Program_Generator
   ```

2. **Build the Application**
   ```bash
   # Clean build with all dependencies
   mvn clean compile
   
   # Full package build (creates JAR)
   mvn clean package
   ```

3. **Run the Application**
   ```bash
   # Option 1: Development mode (with hot reload)
   mvn spring-boot:run
   
   # Option 2: Custom port
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
   
   # Option 3: Run the built JAR
   java -jar target/program-generator-0.0.1-SNAPSHOT.jar
   ```

4. **Access the Application**
   - **Local Access**: http://localhost:8080
   - **Network Access**: http://YOUR-IP-ADDRESS:8080 (enabled by default)
   - **Custom Port**: http://localhost:8081 (if using custom port)

## 📦 Distribution & Deployment

### 🎯 **Ready-to-Deploy Package**

Your application includes a complete deployment solution:

```bash
# Create deployment package
./create-deployment-package.bat    # Windows
bash create-deployment-package.sh  # Mac/Linux
```

#### **Deployment Package Contents:**
```
deployment/
├── program-generator-0.0.1-SNAPSHOT.jar    # Complete application (~44MB)
├── run-church-program.bat                  # Windows startup script
├── run-church-program.sh                   # Mac/Linux startup script
├── README.txt                              # Quick start instructions
├── DEPLOYMENT_GUIDE.md                     # Detailed deployment guide
└── DISTRIBUTION_SUMMARY.md                 # Distribution overview
```

### 🖥️ **Distribution Methods**

#### **Method 1: Standalone JAR (Recommended)**
**Requirements:** Java 17+ on target device

1. Copy the `deployment` folder to target computer
2. Double-click the appropriate startup script:
   - **Windows**: `run-church-program.bat`
   - **Mac/Linux**: `bash run-church-program.sh`
3. Access at http://localhost:8080

#### **Method 2: Docker Container (No Java Required)**
**Requirements:** Docker Desktop

```bash
# Build the container
docker build -t church-program-generator .

# Run the application
docker run -p 8080:8080 church-program-generator

# Or use Docker Compose
docker-compose up -d
```

#### **Method 3: Cloud Deployment (Access Anywhere)**
**Requirements:** Cloud hosting account

- **Railway** *(Recommended)*: Connect GitHub repo → add PostgreSQL plugin → set `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` → auto-deploys on every push
- **Render**: Free tier with direct JAR deployment (sleeps after 15 min on free tier)
- **Fly.io**: Uses existing Dockerfile directly via `flyctl` CLI
- **AWS/Azure/GCP**: Enterprise-grade hosting options

#### **Method 4: Network Sharing (Local Network)**
**Requirements:** Same WiFi network

1. Start application on one computer
2. Find IP address: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
3. Others access via: `http://YOUR-IP:8080`

### 🛠️ **Startup Scripts Features**

#### **Windows Script (`run-church-program.bat`)**
- Automatic Java version detection
- Professional startup messages
- Error handling with user-friendly messages
- Automatic browser guidance
- Graceful shutdown instructions

#### **Mac/Linux Script (`run-church-program.sh`)**
- Cross-platform compatibility
- Java installation verification
- Clean console output
- Professional church branding
- User-friendly error messages

## 📖 Complete User Guide

### 🗄️ **Saved Programs History**

1. **Navigate to History**: Click the **History** tab in the top navigation
2. **Browse All Programs**: All exported programs are listed, newest first
3. **Filter by Type**: Click Sacrament, Ward Council, or Bishopric to narrow the list
4. **Reload a Program**: Click **Load** to open a saved program pre-filled in its form
5. **Delete a Record**: Click **Delete** and confirm to remove the entry from the database
6. **Database**: Programs stored in PostgreSQL — connect via `psql -U postgres -d church_programs` for direct inspection

### 🏠 **Home Page Navigation**
1. **Access Main Interface**: Navigate to http://localhost:8080
2. **Professional Interface**: Experience the MSN-style dark navigation
3. **Program Selection**: Choose from three meeting types via card interface:
   - **Sacrament Program**: Sunday worship service programs
   - **Ward Council**: Administrative meeting agendas
   - **Bishopric Meeting**: Leadership meeting planning

### 🎵 **Creating Sacrament Meeting Programs**

#### **Step 1: Access Sacrament Form**
- Click "Sacrament program" tab in navigation or "Create Sacrament Program" card
- Navigate to structured form interface

#### **Step 2: Complete Program Details**
- **Meeting Date**: Select Sunday meeting date
- **Leadership Assignments**:
  - **Presiding**: Enter presiding authority (e.g., "Bishop John Smith")
  - **Conducting**: Enter conducting authority (e.g., "President David Johnson")
- **Musical Elements**:
  - **Opening Hymn**: Enter hymn number (e.g., "2")
  - **Sacrament Hymn**: Enter sacrament hymn number (e.g., "169")
  - **Closing Hymn**: Enter closing hymn number (e.g., "166")
- **Speaker Management**:
  - **Speaker Names**: Add multiple speakers with full names
  - **Speaker Topics**: Assign speaking topics or themes
  - **Auxiliary Assignment**: Select speaker's organization:
    - Elders Quorum
    - Relief Society
    - Family History and Missionary Work
    - Youth Organization
    - Primary Organization
    - Stake Organization

#### **Step 3: Preview and Export**
- **Preview**: Click "Preview Program" to see formatted layout
- **Review**: Verify all information for accuracy
- **Export Options**:
  - **Word Document**: Click "Export to Word" for .docx file
  - **PDF Document**: Click "Export to PDF" for .pdf file
- **Auto-Save**: Documents automatically saved to `src/reports/sacrament/`

### 📋 **Creating Ward Council Meetings**

#### **Step 1: Access Ward Council Form**
- Click "Ward council" tab or corresponding card from home page
- Enter comprehensive meeting planning interface

#### **Step 2: Meeting Information Entry**
- **Ward Identification**: Enter ward name (default: "Pasay 3rd Ward")
- **Meeting Date**: Select council meeting date
- **Prayer Assignments**:
  - **Opening Prayer**: Assign member for opening prayer
  - **Closing Prayer**: Assign member for closing prayer
- **Spiritual Component**:
  - **Handbook Reading**: Enter handbook spiritual thought assignment
- **Administrative Elements**:
  - **Auxiliary Reports**: List reporting organizations (one per line)
  - **Agenda Items**: Enter business discussion items (one per line)
  - **Welfare Items**: Enter welfare and humanitarian concerns
- **Meeting Structure**: Form automatically includes standard ward council format

#### **Step 3: Preview and Document Generation**
- **Professional Preview**: View elegant table layout with professional styling
- **Export Documents**:
  - **Word Export**: Generate structured table format in .docx
  - **PDF Export**: Create print-ready PDF with borders and professional layout
- **File Management**: Documents saved as `wardCouncilMeeting{date}.docx/pdf`

### 👔 **Creating Bishopric Meetings**

#### **Step 1: Access Bishopric Interface**
- Click "Bishopric meeting" tab or home page card
- Enter executive meeting planning interface

#### **Step 2: Leadership Meeting Details**
- **Ward Information**: Enter ward name (defaults to "Pasay 3rd Ward")
- **Meeting Date**: Select bishopric meeting date
- **Leadership Assignments**:
  - **Presiding**: Enter bishop's name
  - **Conducting**: Enter conducting authority (bishop or counselor)
- **Prayer Assignments**:
  - **Opening Prayer**: Assign opening prayer
  - **Closing Prayer**: Assign closing prayer
- **Meeting Content**:
  - **Handbook Spiritual**: Enter handbook thought or reference
  - **Agenda Items**: Enter discussion items (one per line)
  - **Callings and Releases**: Enter administrative actions
- **Executive Format**: Professional layout for leadership meetings

#### **Step 3: Professional Export**
- **Preview**: View red-bordered table layout matching bishopric standards
- **Document Export**:
  - **Word Format**: Professional table layout in .docx format
  - **PDF Format**: High-quality print-ready PDF with proper formatting
- **File Organization**: Saved as `bishopricMeeting{date}.docx/pdf`

## 📁 Document Management System

### 🗂️ **Organized File Structure**
```
src/reports/
├── sacrament/                          # Sacrament Meeting Programs
│   ├── sacramentProgram2025-10-19.docx
│   ├── sacramentProgram2025-10-19.pdf
│   ├── sacramentProgram2025-10-26.docx
│   └── sacramentProgram2025-10-26.pdf
├── wardcouncil/                        # Ward Council Meetings  
│   ├── wardCouncilMeeting2025-11-02.docx
│   ├── wardCouncilMeeting2025-11-02.pdf
│   ├── wardCouncilMeeting2025-11-16.docx
│   └── wardCouncilMeeting2025-11-16.pdf
└── bishopric/                          # Bishopric Meetings
    ├── bishopricMeeting2025-10-15.docx
    ├── bishopricMeeting2025-10-15.pdf
    ├── bishopricMeeting2025-10-22.docx
    └── bishopricMeeting2025-10-22.pdf
```

### 📄 **Document Features**

#### **Professional Word Documents (.docx)**
- **Church Branding**: "The Church of Jesus Christ of Latter-day Saints" headers
- **Consistent Typography**: Professional fonts with proper hierarchy (10-12pt)
- **Table Layouts**: Structured formats for Ward Council and Bishopric meetings
- **Border Styling**: Clean borders and professional spacing
- **Print Optimization**: Proper margins and page formatting

#### **High-Quality PDF Documents (.pdf)**
- **Vector Graphics**: Crisp text and graphics for all zoom levels
- **Color Coding**: Professional red borders and accent colors
- **Table Structure**: Matching table layouts from Word documents
- **Print Ready**: Optimized for both digital viewing and physical printing
- **Consistent Formatting**: Identical layout to Word versions

### 💾 **Automatic Document Management**
- **Instant Download**: Documents automatically download when "Export" is clicked
- **Local Storage**: Files simultaneously saved to `src/reports/` directory
- **Date-Based Naming**: Files include meeting type and date (YYYY-MM-DD format)
- **Dual Format Generation**: Both Word and PDF versions created
- **Organized Categories**: Separate folders prevent file mixing

## 🏗️ Technical Architecture

### 📂 **Enhanced Project Structure**
```
src/
├── main/
│   ├── java/com/church/programgenerator/
│   │   ├── controller/                        # MVC Controllers
│   │   │   ├── HomeController.java            # Main navigation handler
│   │   │   ├── SacramentController.java       # Sacrament meeting logic
│   │   │   ├── WardCouncilController.java     # Ward council management
│   │   │   ├── BishopricController.java       # Bishopric meeting handling
│   │   │   ├── HistoryController.java         # Saved programs history
│   │   │   └── GlobalExceptionHandler.java    # Centralized error handling
│   │   ├── model/                             # Data Models
│   │   │   ├── SacramentProgram.java          # Sacrament meeting data model
│   │   │   ├── WardCouncilProgram.java        # Ward council data model
│   │   │   ├── BishopricProgram.java          # Bishopric meeting model
│   │   │   ├── Speaker.java                   # Speaker entity model
│   │   │   └── SavedProgram.java              # JPA entity for DB storage
│   │   ├── repository/                        # Spring Data Repositories
│   │   │   └── SavedProgramRepository.java    # JPA repository for saved programs
│   │   ├── service/                           # Business Logic Services
│   │   │   ├── SacramentProgramDocumentService.java      # Word generation
│   │   │   ├── SacramentProgramPreviewService.java       # HTML preview
│   │   │   ├── SacramentProgramPdfService.java           # PDF generation
│   │   │   ├── WardCouncilDocumentService.java           # Word generation
│   │   │   ├── WardCouncilPdfService.java                # PDF generation
│   │   │   ├── BishopricProgramDocumentService.java      # Word generation
│   │   │   ├── BishopricProgramPdfService.java           # PDF generation
│   │   │   ├── ProgramStorageService.java                # DB save/load with caching
│   │   │   └── FileStorageService.java                   # File management
│   │   └── ProgramGeneratorApplication.java              # Spring Boot main class
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   │   └── style.css                  # MSN-style navigation & theming
│       │   ├── js/
│       │   │   └── navigation.js              # Interactive navigation logic
│       │   └── images/                        # Static assets and icons
│       ├── templates/                         # Thymeleaf Templates
│       │   ├── layout.html                    # Base layout with MSN navigation
│       │   ├── index.html                     # Home page with card interface
│       │   ├── sacrament.html                 # Sacrament meeting form
│       │   ├── sacrament-preview.html         # Sacrament preview display
│       │   ├── ward-council.html              # Ward council form
│       │   ├── ward-council-preview.html      # Ward council preview
│       │   ├── bishopric.html                 # Bishopric meeting form
│       │   ├── bishopric-preview.html         # Bishopric preview
│       │   ├── history.html                   # Saved programs browser
│       │   └── error.html                     # Global error page
│       └── application.properties             # Application configuration
├── reports/                                   # Generated documents storage
│   ├── sacrament/                            # Sacrament program exports
│   ├── wardcouncil/                          # Ward council exports
│   └── bishopric/                            # Bishopric meeting exports
data/
│   └── church-programs.mv.db                # H2 persistent database
├── deployment/                               # Distribution package
│   ├── program-generator-0.0.1-SNAPSHOT.jar # Standalone executable
│   ├── run-church-program.bat               # Windows startup script
│   ├── run-church-program.sh                # Unix startup script
│   └── README.txt                           # Quick start guide
├── Dockerfile                               # Docker containerization
├── docker-compose.yml                       # Docker Compose configuration
├── create-deployment-package.bat            # Package creation script
├── DEPLOYMENT_GUIDE.md                      # Comprehensive deployment guide
└── DISTRIBUTION_SUMMARY.md                  # Distribution overview
```

### 🔧 **Technology Stack**

#### **Backend Framework**
- **Spring Boot 3.1.5**: Latest Spring framework with Java 17 support
- **Spring Web MVC**: RESTful controllers and web service handling
- **Spring Data JPA**: Repository pattern for database access
- **Spring Cache (Caffeine)**: In-memory caching with TTL and size limits
- **Spring Validation**: Bean Validation (`jakarta.validation`) for form input
- **Spring DevTools**: Development hot-reload and debugging support
- **Embedded Tomcat**: Built-in web server with GZIP compression enabled

#### **Frontend Technologies**
- **Thymeleaf 3.1**: Server-side template engine with fragment support
- **HTML5**: Semantic markup with accessibility considerations
- **CSS3**: Advanced styling with Grid, Flexbox, and CSS Variables
- **MSN-Style Navigation**: Dark gradient themes with modern aesthetics
- **Responsive Design**: Mobile-first approach with breakpoint optimization
- **JavaScript ES6**: Modern interactive elements and form validation

#### **Document Generation Libraries**
- **Apache POI 5.2.4**: Microsoft Office document manipulation
  - **XWPFDocument**: Word document creation and formatting
  - **XWPFTable**: Professional table layouts with borders and styling
  - **XWPFParagraph**: Text formatting, fonts, and paragraph management
  - **XWPFRun**: Character-level formatting and styling
- **iText 8.0.2**: Professional PDF generation library
  - **PdfDocument**: PDF creation with metadata and structure
  - **Table/Cell**: Advanced table layouts with borders and styling
  - **Font Management**: Professional typography and character encoding
  - **Color/Layout**: Advanced styling and page layout management

#### **Build & Deployment Tools**
- **Maven 3.6+**: Dependency management and build automation
- **Spring Boot Maven Plugin**: JAR packaging and executable generation
- **Docker Support**: Containerization with multi-stage builds
- **Production Scripts**: Cross-platform startup and deployment scripts

#### **Data & Caching**
- **PostgreSQL 17**: Production-grade relational database (`localhost:5432`, database `church_programs`)
- **Environment Variable Override**: `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` for cloud deployment
- **Caffeine Cache**: High-performance in-memory caching library
- **Jackson + JavaTimeModule**: JSON serialization of program data including `LocalDate`/`LocalDateTime`

## 🎨 Design System & User Interface

### 🌟 **MSN-Style Navigation System**
- **Dark Gradient Background**: Professional deep blue gradient (from `#1a1a2e` to `#0f3460`)
- **Rounded Tab Design**: 20px border-radius for modern appearance
- **Hover Effects**: Subtle backdrop blur and lift animations
- **Active State Styling**: White overlay with blue accent indicators
- **Church Icon Integration**: Emoji-based church icon (🏛️) for branding
- **Hamburger Menu**: On mobile (≤768px), tabs collapse behind a ☰ button — animated X on open

### 🎨 **Visual Design System**
- **Color Palette**:
  - **Primary Blue**: #2c5282 (navigation accents)
  - **Secondary Teal**: #4a90a4 (interactive elements)
  - **Alert Red**: #e74c3c (important notifications)
  - **Neutral Grays**: #f7fafc, #e2e8f0 (backgrounds and borders)
  - **Navigation Gradient**: Deep blues for professional appearance
- **Typography**:
  - **Primary Font**: System font stack (-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto)
  - **Font Weights**: 400 (normal), 500 (medium), 600 (semibold), 700 (bold)
  - **Font Hierarchy**: Proper sizing for headers, body text, and navigation

### 📱 **Responsive Design Features**
- **Hamburger Navigation**: Collapses nav tabs into a toggle menu on mobile (≤768px)
- **Card Interface**: Grid-based layout that adapts to screen size
- **Touch Optimization**: Larger touch targets and improved spacing; menu closes on outside tap
- **iOS Auto-Zoom Prevention**: `font-size: 16px` on all inputs at ≤480px
- **Speaker Row Stacking**: JS-built flex rows wrap to full width on ≤520px screens
- **Cross-Browser Support**: Tested on Chrome, Firefox, Safari (iOS), Samsung Internet, and Edge
- **Print Optimization**: Clean print styles for generated documents

## ⚙️ Configuration & Environment

### 🔧 **Application Configuration (`application.properties`)**
```properties
# Server Configuration
server.port=8080
server.address=0.0.0.0                    # Network access enabled

# Application Identification
spring.application.name=ProgramGenerator

# Thymeleaf Configuration
spring.thymeleaf.cache=false              # Set true in production

# PostgreSQL Database (supports environment variable overrides for cloud deployment)
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/church_programs}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${DATABASE_USERNAME:postgres}
spring.datasource.password=${DATABASE_PASSWORD:<your-password>}
spring.jpa.hibernate.ddl-auto=update     # Auto-create/update tables
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Caffeine Cache
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=200,expireAfterWrite=30m

# GZIP Compression
server.compression.enabled=true
server.compression.mime-types=text/html,text/css,application/javascript,application/json
server.compression.min-response-size=1024

# Logging Configuration
logging.level.root=INFO
logging.level.com.church=DEBUG
```

### 🚀 **Environment-Specific Configuration**

#### **Development Mode**
```bash
# Hot reload development
mvn spring-boot:run

# Custom port development
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Debug mode
mvn spring-boot:run -Dspring-boot.run.arguments=--debug
```

#### **Production Deployment**
```bash
# Build production JAR
mvn clean package -DskipTests

# Run production with optimized settings
java -jar -Xmx512m -Xms256m target/program-generator-0.0.1-SNAPSHOT.jar

# Background execution (Linux/Mac)
nohup java -jar target/program-generator-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# Windows service execution
start /B java -jar target/program-generator-0.0.1-SNAPSHOT.jar
```

#### **Docker Configuration**
```bash
# Build Docker image
docker build -t church-program-generator .

# Run with resource limits
docker run -p 8080:8080 --memory=512m church-program-generator

# Docker Compose with environment variables
docker-compose up -d
```

### 🌐 **Network & Security Configuration**

#### **Network Access Setup**
```properties
# Enable network access (already configured)
server.address=0.0.0.0

# Custom port configuration
server.port=9000

# SSL configuration (optional)
server.ssl.enabled=false
```

#### **Firewall Configuration**
```bash
# Windows Firewall (if needed)
netsh advfirewall firewall add rule name="Church Program Generator" dir=in action=allow protocol=TCP localport=8080

# Linux UFW (if needed)
sudo ufw allow 8080/tcp

# macOS (if needed)
sudo pfctl -f /etc/pf.conf
```

## 🧪 Testing & Quality Assurance

### 🔍 **Quality Assurance Features**
- **Spring Boot Best Practices**: Following official Spring conventions
- **MVC Architecture**: Clear separation of concerns between layers
- **Error Handling**: Graceful error management with user-friendly messages
- **Template Validation**: Thymeleaf fragment parameter validation
- **Responsive Testing**: Cross-device compatibility verification

### 📱 **Browser & Platform Support**
- **Desktop Browsers**:
  - ✅ Chrome/Chromium 90+ (Full support)
  - ✅ Firefox 85+ (Full support)
  - ✅ Safari 14+ (Full support)
  - ✅ Microsoft Edge 90+ (Full support)
- **Mobile Browsers**:
  - ✅ Mobile Safari (iOS 14+)
  - ✅ Chrome Mobile (Android 10+)
  - ✅ Samsung Internet
  - ✅ Firefox Mobile
- **Operating Systems**:
  - ✅ Windows 10/11
  - ✅ macOS 10.15+
  - ✅ Linux (Ubuntu, CentOS, Alpine)
  - ✅ Docker containers

### 🔧 **Development Testing**
```bash
# Run application tests
mvn test

# Integration testing
mvn verify

# Build and test full package
mvn clean package

# Test JAR execution
java -jar target/program-generator-0.0.1-SNAPSHOT.jar

# Test Docker build
docker build -t test-church-app .
docker run -p 8080:8080 test-church-app
```

## 🐛 Troubleshooting Guide

### **Common Issues & Solutions**

#### **🚫 Port Already in Use Error**
```bash
# Check what's using port 8080
netstat -ano | findstr :8080          # Windows
lsof -i :8080                         # Mac/Linux

# Kill existing process (if safe)
taskkill /PID <PID> /F                # Windows
kill -9 <PID>                        # Mac/Linux

# Run on different port
java -jar target/program-generator-0.0.1-SNAPSHOT.jar --server.port=8081
```

#### **☕ Java Version Compatibility**
```bash
# Check Java version
java -version

# Required: Java 17 or higher
# Download from: https://adoptium.net/

# Set JAVA_HOME (if needed)
export JAVA_HOME=/path/to/java17      # Mac/Linux
set JAVA_HOME=C:\Program Files\Java\jdk-17  # Windows
```

#### **🔧 Maven Build Issues**
```bash
# Clean and rebuild
mvn clean compile

# Force dependency updates
mvn clean compile -U

# Skip tests if failing
mvn clean package -DskipTests

# Verbose output for debugging
mvn clean package -X
```

#### **🌐 Network Access Issues**
```bash
# Check if server.address is configured
grep "server.address" src/main/resources/application.properties

# Test local access first
curl http://localhost:8080

# Find your IP address
ipconfig                              # Windows
ifconfig                              # Mac/Linux

# Test network access
curl http://YOUR-IP:8080
```

#### **📄 Document Generation Issues**
```bash
# Check reports directory permissions
ls -la src/reports/                   # Mac/Linux
dir src\reports\                      # Windows

# Create directories if missing
mkdir -p src/reports/{sacrament,wardcouncil,bishopric}    # Mac/Linux
mkdir src\reports\sacrament src\reports\wardcouncil src\reports\bishopric    # Windows

# Check file system space
df -h                                 # Mac/Linux
dir /-s                               # Windows
```

#### **🐳 Docker Issues**
```bash
# Build with no cache
docker build --no-cache -t church-program-generator .

# Check Docker logs
docker logs <container-id>

# Remove and rebuild
docker system prune -f
docker build -t church-program-generator .
```

## 🤝 Contributing & Development

### 🔄 **Development Workflow**
1. **Fork Repository**: Create personal fork on GitHub
2. **Clone Locally**: `git clone https://github.com/YOUR-USERNAME/P3_Program_Generator.git`
3. **Create Feature Branch**: `git checkout -b feature/navigation-improvement`
4. **Develop Features**: Implement improvements with proper testing
5. **Test Thoroughly**: Ensure all functionality works correctly
6. **Commit Changes**: `git commit -m "Add MSN-style navigation improvements"`
7. **Push Branch**: `git push origin feature/navigation-improvement`
8. **Create Pull Request**: Submit for review and integration

### 📋 **Development Guidelines**
- **Spring Boot Conventions**: Follow official Spring Boot best practices
- **Code Organization**: Maintain clear MVC separation of concerns
- **Responsive Design**: Ensure mobile-first development approach
- **Documentation**: Update README.md for significant changes
- **Testing**: Test all features across multiple browsers and devices
- **Git Commit Messages**: Use clear, descriptive commit messages

### 🛠️ **Development Environment Setup**
```bash
# Install development tools
# Java 17+, Maven 3.6+, Git, Docker (optional)

# Clone and setup
git clone https://github.com/kiddogreed/P3_Program_Generator.git
cd P3_Program_Generator

# Install dependencies and run
mvn clean compile
mvn spring-boot:run

# Access development server
open http://localhost:8080
```

## 📄 License & Legal

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for complete details.

### **MIT License Summary**
- ✅ Commercial use allowed
- ✅ Modification allowed
- ✅ Distribution allowed
- ✅ Private use allowed
- ❗ License and copyright notice required
- ❗ No warranty provided

## 👨‍💻 Author & Contact

**Developer**: kiddogreed  
**GitHub**: [@kiddogreed](https://github.com/kiddogreed)  
**Repository**: [P3_Program_Generator](https://github.com/kiddogreed/P3_Program_Generator)  
**Organization**: Pasay 3rd Ward, The Church of Jesus Christ of Latter-day Saints

### **Support & Feedback**
- **Issues**: [GitHub Issues](https://github.com/kiddogreed/P3_Program_Generator/issues)
- **Discussions**: [GitHub Discussions](https://github.com/kiddogreed/P3_Program_Generator/discussions)
- **Email**: [Contact via GitHub](https://github.com/kiddogreed)

---

## 🚀 Future Roadmap & Enhancement Plans

### 🗄️ **Phase 1: Database Integration (Q1 2026)** ✅ *Completed*
- **Program History**: Save and retrieve historical meeting programs ✅
- **PostgreSQL Migration**: Switched from H2 to PostgreSQL 17 ✅
- **Template Management**: Create and save custom program templates  
- **Speaker Database**: Maintain speaker information and topic libraries
- **Search Functionality**: Find previous programs by date, speaker, or topic

### 🔐 **Phase 2: Authentication & Multi-Ward Support (Q2 2026)**
- **User Role Management**: Bishop, Bishopric, Clerk, and Member access levels
- **Ward Integration**: Multi-ward support for stake-level administration
- **LDS Account Integration**: Secure authentication with church systems
- **Permission Levels**: Role-based access to different meeting types

### 📱 **Phase 3: Mobile App & PWA (Q3 2026)**
- **Progressive Web App**: Install as mobile application
- **Offline Mode**: Create programs without internet connection
- **Push Notifications**: Meeting reminders and program updates
- **Mobile-Optimized Forms**: Touch-friendly input interfaces

### 🌐 **Phase 4: Advanced Features (Q4 2026)**
- **Calendar Integration**: Sync with LCR (Leader and Clerk Resources)
- **Email Distribution**: Automatic program distribution to members
- **Multi-Language Support**: Spanish, Tagalog, and other languages
- **API Integration**: Connect with external church systems

### 🎯 **Phase 5: Analytics & Reporting (2027)**
- **Usage Analytics**: Track program creation and utilization
- **Meeting Statistics**: Attendance and participation reporting
- **Trend Analysis**: Historical data visualization and insights
- **Performance Metrics**: System optimization and user experience improvements

### 💡 **Innovation Features (Future)**
- **AI-Powered Suggestions**: Smart content recommendations
- **Voice Input**: Speech-to-text program creation
- **Real-time Collaboration**: Multiple users editing simultaneously
- **Advanced Document Templates**: Custom layouts and branding options

---

## 🎯 **Implementation Priority Timeline**

### **Immediate (Next 3 months)**
- [x] Enhanced error handling and user feedback
- [x] Additional document templates and formatting options
- [x] Performance optimization and caching improvements
- [x] Database integration for program storage

### **Short-term (3-6 months)**
- [ ] Basic user authentication system
- [ ] Mobile app development initiation
- [ ] Advanced document formatting features
- [ ] Extended browser compatibility testing

### **Medium-term (6-12 months)**
- [ ] Multi-language internationalization
- [ ] Calendar and scheduling integration
- [ ] Advanced reporting and analytics
- [ ] API development for external integrations

### **Long-term (1+ years)**
- [ ] AI-powered feature development
- [ ] Microservices architecture migration
- [ ] Advanced analytics dashboard
- [ ] Plugin ecosystem development

---

## 🙏 Acknowledgments & Credits

- **Spring Boot Team**: For the excellent framework and documentation
- **Apache POI Project**: For powerful Word document generation capabilities
- **iText Software**: For professional PDF generation and formatting
- **Thymeleaf Team**: For elegant server-side templating
- **The Church of Jesus Christ of Latter-day Saints**: For inspiration and organizational structure
- **Pasay 3rd Ward**: For real-world testing and feedback
- **Open Source Community**: For tools, libraries, and development resources

---

## 📊 **Project Statistics**

- **Total Lines of Code**: ~3,500+ (Java, HTML, CSS, JavaScript)
- **Dependencies**: 20+ Spring Boot and utility libraries
- **Supported Browsers**: 4+ major browsers with mobile support
- **Document Formats**: 2 (Word .docx, PDF)
- **Meeting Types**: 3 (Sacrament, Ward Council, Bishopric)
- **Deployment Options**: 4+ (JAR, Docker, Cloud, Network)
- **Supported Platforms**: Windows, macOS, Linux, Docker containers
- **Database**: H2 file-based, auto-schema, persistent across restarts
- **Cache**: Caffeine in-memory, 200 entries, 30-min TTL

---

**🏛️ Built with ❤️ and faith for church program management and organization**

*"And let all things be done unto edifying." - 1 Corinthians 14:26*

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
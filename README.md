# Church Program Generator

A Spring Boot web application for creating and managing church meeting programs.

## Features
- **Sacrament Meeting Programs**: Create programs with hymns, speakers, and conducting information
- **Ward Council Meetings**: Generate agendas with business items and discussion topics
- **Bishopric Meetings**: Plan leadership meetings with ward business and member concerns

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Running the Application
```bash
# Clone the repository
git clone <repository-url>
cd ProgramGenerator

# Run with Maven
./mvnw spring-boot:run

# Or build and run the JAR
./mvnw clean package
java -jar target/program-generator-0.0.1-SNAPSHOT.jar
```

The application will be available at `http://localhost:8080`

## Project Structure
```
src/
├── main/
│   ├── java/com/church/programgenerator/
│   │   ├── controller/          # Web controllers
│   │   └── ProgramGeneratorApplication.java
│   └── resources/
│       ├── static/css/          # Stylesheets
│       ├── templates/           # Thymeleaf templates
│       └── application.properties
```

## Technology Stack
- **Backend**: Spring Boot 3.1.5, Spring Web MVC
- **Frontend**: Thymeleaf templating, HTML5, CSS3
- **Build Tool**: Maven
- **Java Version**: 17

## Development
- Thymeleaf cache is disabled for development (`spring.thymeleaf.cache=false`)
- Spring Boot DevTools included for hot reloading
- Responsive design with mobile-first approach

---
Built with Spring Boot for church program management.
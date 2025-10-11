# Copilot Instructions for AI Coding Agents

## Project Overview
**ProgramGenerator** is a Spring Boot web application for generating church meeting programs. It provides a simple web UI for creating programs for three types of meetings:
- Sacrament Meeting Programs (hymns, speakers, conducting info)
- Ward Council Meeting agendas (business items, prayers)
- Bishopric Meeting agendas (ward business, member concerns)

## Architecture & Structure

### Spring Boot MVC Pattern
- **Controllers**: Located in `src/main/java/com/church/programgenerator/controller/`
  - `HomeController.java` - Root path handler
  - `SacramentController.java` - `/sacrament` endpoint
  - `WardCouncilController.java` - `/ward-council` endpoint  
  - `BishopricController.java` - `/bishopric` endpoint
- **Templates**: Thymeleaf templates in `src/main/resources/templates/`
  - `layout.html` - Shared navigation and footer fragments
  - Page-specific templates for each meeting type
- **Static Resources**: CSS and assets in `src/main/resources/static/`

### Key Conventions
- **URL Pattern**: Kebab-case URLs (`/ward-council`, `/bishopric`)
- **Template Fragments**: Use `th:replace="layout :: navigation"` for consistent navigation
- **CSS Variables**: Defined in `:root` for consistent theming (primary: #2c5282)
- **Responsive Design**: Mobile-first with CSS Grid for card layouts

### Development Workflow
```bash
# Run the application
./mvnw spring-boot:run
# Access at http://localhost:8080

# Build
./mvnw clean package
```

### Configuration
- **Port**: 8080 (configurable in `application.properties`)
- **Thymeleaf**: Cache disabled for development (`spring.thymeleaf.cache=false`)

## Integration Points
Currently standalone with form-based UI. Future extensions might include:
- Database integration for saving programs
- PDF generation for printing programs
- Authentication for different ward roles

---
_Last updated: 2025-10-11_

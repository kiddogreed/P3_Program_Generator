# Final Working Features Reference

**Pasay 3rd Ward Program Generator — Spring Boot, April 2026**

This document is the authoritative reference for all working features, design decisions, and workflows. It is intended to guide the full transition to a **Flutter Android application** with identical functionality for local mobile use.

---

## Table of Contents

1. [Application Overview](#1-application-overview)
2. [Sacrament Meeting Program](#2-sacrament-meeting-program)
3. [Bishopric Meeting](#3-bishopric-meeting)
4. [Ward Council Meeting](#4-ward-council-meeting)
5. [Preview, Export & Auto-Save](#5-preview-export--auto-save)
6. [History Browser](#6-history-browser)
7. [Manage Tab](#7-manage-tab)
8. [Rules Tab — Scheduling Automation](#8-rules-tab--scheduling-automation)
9. [Database Design (PostgreSQL)](#9-database-design-postgresql)
10. [Backend Architecture (Spring Boot)](#10-backend-architecture-spring-boot)
11. [UI & Mobile Compatibility](#11-ui--mobile-compatibility)
12. [Flutter Android Migration Guide](#12-flutter-android-migration-guide)

---

## 1. Application Overview

- **Stack**: Java 17, Spring Boot 3.1.5, Thymeleaf (server-side HTML), PostgreSQL 17
- **Port**: 8080 (binds to `0.0.0.0` — accessible from any device on the local network)
- **URL root**: `http://localhost:8080`
- **Navigation tabs**: Home, Sacrament, Bishopric, Ward Council, History, Manage, Rules
- **Logo**: `LDS_LOGO.png` embedded in every document header and preview. Falls back to text if file is missing.
- **Performance**: Caffeine in-memory cache (200 entries, 30-min TTL), GZIP compression for HTML/CSS/JS/JSON responses ≥ 1 KB, Thymeleaf template cache disabled in dev.
- **Error handling**: Global `GlobalExceptionHandler` catches exceptions and renders a user-friendly `error.html` page. Inline flash messages (success/error banners) on all forms.

---

## 2. Sacrament Meeting Program

**URL**: `GET /sacrament`

### Form Fields (all on one page)

| Field | Type | Source |
|---|---|---|
| Stake Name | Text | Auto-filled from `ward_config.stake_name` |
| Ward Name | Text | Auto-filled from `ward_config.ward_name` |
| Meeting Date | Date picker | Auto-calculated: nearest upcoming Sunday |
| Presiding | Text | Manual entry (not locked) |
| Conducting | Dropdown | Auto-suggested via round-robin from **sacrament conductors** list |
| Chorister | Dropdown | From **choristers** list (Manage tab) |
| Pianist | Dropdown | From **pianists** list (Manage tab) |
| Opening Hymn | Text | Manual entry |
| Sacrament Hymn | Text | Manual entry |
| Closing Hymn | Text | Manual entry |
| Invocation | Text | Manual entry |
| Benediction | Text | Manual entry |
| Ward Business | Textarea (max 400 chars) | Manual entry |
| Stake Business | Textarea (max 400 chars) | Manual entry |
| Speakers | Dynamic rows | Add/remove rows; each row has Name (text) + Title (text) |
| Speakers Auxiliary | Dropdown | From **auxiliaries** list (Manage tab) |
| Announcements | Textarea | Each line becomes a separate announcement item |
| Acknowledgement | Textarea (max 600 chars) | Auto-filled from `ward_config.acknowledgement_template` |

### Speaker Cycle Badge
A badge displays above the Speakers section showing the expected speaker type for the meeting date:
- **1st Sunday** → "Fast & Testimony"
- **2nd Sunday** → cycles: Cycle 1 = "Relief Society", Cycle 2 = "Elders Quorum", Cycle 3 = "Ward Mission & Family History"
- **3rd Sunday** → "Stake Assignment"
- **4th Sunday** → cycles: Cycle 1 = "Sunday School", Cycle 2 = "Primary", Cycle 3 = "Youth"
- **5th Sunday** → "Bishopric Special"

### Acknowledgement Template Substitutions
The template stored in `ward_config` supports two placeholders:
- `{OTHER_CONDUCTORS}` — replaced with all sacrament conductors except the one conducting
- `{BISHOPRIC_OTHERS}` — replaced with all bishopric conductors except the Bishop and the conducting counselor

### Export Options
- **Word (.docx)**: `POST /sacrament/export/docx` — downloads a formatted `.docx` file
- **PDF**: `POST /sacrament/export/pdf` — downloads a formatted `.pdf` file
- Both exports auto-save the program to the `saved_programs` table and write the file to `src/reports/sacrament/`

### Test Preview
`GET /sacrament/test-preview` — loads a fully pre-filled form with dummy data for instant testing.

---

## 3. Bishopric Meeting

**URL**: `GET /bishopric`

### Form Fields (all on one page)

| Field | Type | Source |
|---|---|---|
| Ward Name | Text | Auto-filled from `ward_config.ward_name` |
| Meeting Date | Date picker | Auto-calculated: next Thursday (or Sunday if configured) |
| Presiding | Read-only display | Always the **first bishopric conductor** whose name starts with "Bishop". Hidden input carries value on submit. Blue-tinted locked field. |
| Conducting | Dropdown | Auto-suggested via round-robin from **bishopric conductors** list |
| Opening Prayer | Dropdown | Auto-assigned from bishopric conductors — round-robin, no duplicates with other prayer fields |
| Handbook Spiritual Thought | Dropdown | Auto-assigned from bishopric conductors — round-robin |
| Agenda Items | Dynamic rows (JSON) | Add/remove rows via JS; each row has Title + Notes |
| Callings & Releases | Textarea (max 500 chars) | Manual entry |
| Closing Prayer | Dropdown | Auto-assigned from bishopric conductors — round-robin |

### Presiding Lock Logic
The Bishop is identified as the first entry in the bishopric conductors list whose name starts with "Bishop" (case-insensitive). The visible field is styled as locked (read-only, blue border). A hidden `<input>` carries the value through form submission.

### Prayer & Spiritual Thought Auto-Assignment
On every new form load, three consecutive different people from the bishopric conductors list are assigned to the three fields (Opening Prayer, Handbook Spiritual Thought, Closing Prayer). The rotation base index (`bp_handbook_idx`) advances after each assignment and persists to `ward_config`.

### Export Options
- **Word (.docx)**: `POST /bishopric/export/docx`
- **PDF**: `POST /bishopric/export/pdf`
- Both exports auto-save to `saved_programs` and write the file to `src/reports/bishopric/`

---

## 4. Ward Council Meeting

**URL**: `GET /ward-council`

### Form Fields (all on one page)

| Field | Type | Source |
|---|---|---|
| Ward Name | Text | Auto-filled from `ward_config.ward_name` |
| Meeting Date | Date picker | Auto-calculated: next 1st or 3rd Sunday (configurable) |
| Presiding | Read-only display | Always the first bishopric conductor starting with "Bishop" |
| Conducting | Dropdown | Auto-suggested via round-robin from **bishopric conductors** list |
| Opening Prayer | Dropdown | Auto-assigned from **auxiliaries** list — round-robin, no duplicates |
| Handbook Reading | Dropdown | Auto-assigned from **auxiliaries** list — round-robin |
| Closing Prayer | Dropdown | Auto-assigned from **auxiliaries** list — round-robin |
| Agenda Items | Dynamic rows (JSON) | Add/remove via JS; each row has Title + Notes |
| Welfare | Textarea | Manual entry |

### Prayer & Handbook Auto-Assignment
On every new form load, three consecutive different people from the auxiliaries list are assigned to: Opening Prayer, Handbook Reading, Closing Prayer. The rotation base index (`wc_handbook_idx`) advances and persists to `ward_config`.

### Export Option
- **PNG Image**: `POST /ward-council/export/png` — downloads a `.png` image of the agenda
- Auto-saves to `saved_programs` and writes to `src/reports/wardcouncil/`

---

## 5. Preview, Export & Auto-Save

### Preview Flow (all three meeting types)
1. User fills out form and clicks **Preview**
2. Form submits via `POST` to `/sacrament/preview`, `/bishopric/preview`, or `/ward-council/preview`
3. Dedicated preview template renders the program exactly as the exported document looks
4. User can click **Edit** to return to the form with all values pre-filled, or **Export** to download

### Export Flow
1. From the preview page, user clicks **Export PDF** or **Export Word** (or **Export PNG** for Ward Council)
2. Form submits to the appropriate `/export/pdf` or `/export/docx` endpoint
3. Server generates the file using Apache POI (DOCX) or iText/PDFBox (PDF) or BufferedImage (PNG)
4. File is returned as a download attachment
5. Program is **auto-saved** to the `saved_programs` table in the same request (errors silently ignored)
6. File copy is written to the local `src/reports/{type}/` directory

### Single-Page Design with Dynamic Content
- All header/footer content (ward name, stake name, date, logo) is data-driven — changes in Manage/Rules automatically appear in the next form and all exports
- All text (agenda items, speakers, announcements) auto-scales in PDF/DOCX via font-size calculation based on content length
- Logo (`LDS_LOGO.png`) is embedded at 120×120 in DOCX, 80×80 pt in PDF, and displayed in HTML with a text fallback

---

## 6. History Browser

**URL**: `GET /history`

### Features
- **Paginated list**: 15 records per page. Supports filtering by meeting type (ALL / SACRAMENT / BISHOPRIC / WARD_COUNCIL)
- **Columns**: Meeting Type, Description (e.g. "Sacrament – Pasay 3rd Ward – 2026-04-13"), Meeting Date, Created At
- **Actions per row**:
  - **Load** → opens the saved program back into the form for editing or re-export
  - **Delete** → removes the record from the database

### Load Behavior
- Sacrament: `GET /history/load/sacrament/{id}` — deserializes JSON, rebuilds Speaker list with correct order, restores announcements as multi-line text, re-populates all dropdowns
- Bishopric: `GET /history/load/bishopric/{id}` — deserializes JSON, re-populates agenda items
- Ward Council: `GET /history/load/ward-council/{id}` — deserializes JSON, re-populates agenda items

### Storage Format
Programs are stored in `saved_programs` as a JSON string in the `program_data` (TEXT) column. Each record has `meeting_type`, `description`, `meeting_date`, `created_at`.

---

## 7. Manage Tab

**URL**: `GET /manage` (also accessible at `/conductors`)

### Sections

#### Sacrament Conductors
- List of names used in the **Conducting** dropdown for Sacrament Meeting
- Type = `"sacrament"` in the `conductors` table
- CRUD: Add (name), Edit (name inline), Delete

#### Bishopric Conductors
- List of names used for Conducting, Presiding detection, and prayer auto-assignment in Bishopric and Ward Council meetings
- Type = `"bishopric"` in the `conductors` table
- The **first entry whose name starts with "Bishop"** is automatically used as the presiding officer
- CRUD: Add (name), Edit (name inline), Delete

#### Auxiliaries
- List of auxiliary organization names used in the **Speakers Auxiliary** dropdown (Sacrament) and **prayer/handbook dropdowns** (Ward Council)
- Stored in the `auxiliaries` table (unique names)
- CRUD: Add (name), Delete

#### Choristers
- List of chorister names for the **Chorister** dropdown in Sacrament Meeting
- Type = `"chorister"` in the `musicians` table
- CRUD: Add (name), Edit, Delete

#### Pianists
- List of pianist names for the **Pianist** dropdown in Sacrament Meeting
- Type = `"pianist"` in the `musicians` table
- CRUD: Add (name), Edit, Delete

All add/edit/delete actions redirect back to `/manage` with a flash success or error message.

---

## 8. Rules Tab — Scheduling Automation

**URL**: `GET /rules`

### What the Rules Page Shows
- **Next meeting dates**: computed next Sacrament, Bishopric, and Ward Council dates
- **Upcoming 6 Sundays**: table showing each Sunday's date, occurrence number (1st/2nd/…), and speaker type label
- **Next speaker type**: label for the next Sacrament Sunday
- **Suggested conductors**: who rounds-robin suggests for Sacrament and Bishopric

### Configurable Rules (stored in `ward_config`, id = 1)

| Field | Default | Description |
|---|---|---|
| `stake_name` | "Pasay Philippine Stake" | Displayed in Sacrament header and acknowledgement |
| `ward_name` | "Pasay 3rd Ward" | Displayed in all meeting headers |
| `acknowledgement_template` | (long default) | Template text with `{OTHER_CONDUCTORS}` and `{BISHOPRIC_OTHERS}` placeholders |
| `sacrament_time` | "09:00" | Meeting time (display only) |
| `bishopric_preferred_day` | "Thursday" | "Thursday" or "Sunday" — which day Bishopric meeting falls on |
| `bishopric_thursday_time` | "19:00" | Bishopric meeting time on Thursdays |
| `bishopric_sunday_time` | "12:00" | Bishopric meeting time on Sundays |
| `ward_council_occurrences` | "1,3" | Comma-separated occurrence numbers (1=1st Sunday, 3=3rd Sunday) |
| `ward_council_time` | "11:00" | Ward Council meeting time |
| `speaker_cycle_base_month` | "2026-01" | Base month (yyyy-MM) for the 3-month speaker cycle rotation |

### How Auto-Date Calculation Works
- **Sacrament**: nearest upcoming Sunday from today (today counts if today is Sunday)
- **Bishopric**: next occurrence of `bishopric_preferred_day` that is strictly in the future (never today even if it matches)
- **Ward Council**: scans up to 8 consecutive Sundays from the next Sunday, returns the first one whose occurrence number (1st/2nd/…5th of month) is in `ward_council_occurrences`

### Conductor Round-Robin (Sacrament)
- On Sacrament form load, the service looks up `last_sacrament_conductor_id` in `ward_config`
- Picks the next conductor in the ordered sacrament conductors list after the last-used one
- On export, `last_sacrament_conductor_id` is updated to the used conductor's ID

### Conductor Round-Robin (Bishopric & Ward Council)
- On Bishopric or Ward Council form load, the service picks the next conductor from the bishopric conductors list after `last_bishopric_conductor_id`
- After picking one for Conducting, it immediately advances the index and saves to DB so the next form load gets the next person

### Prayer & Handbook Round-Robin (Ward Council)
- Three indices advance together from `wc_handbook_idx` 
- `nextThreeIndices(list, baseIdx)` returns 3 consecutive, different indices in the list
- Returns index `baseIdx+1`, `baseIdx+2`, `baseIdx+3` (with wrapping)
- Assigned to: Opening Prayer, Handbook Reading, Closing Prayer
- Final index (`idxs[2]`) is saved back as the new `wc_handbook_idx`

### Prayer & Handbook Round-Robin (Bishopric)
- Same logic using `bp_handbook_idx` and the bishopric conductors list
- Assigned to: Opening Prayer, Handbook Spiritual Thought, Closing Prayer

### Speaker Type Cycle
- The cycle number (1, 2, or 3) is computed from months elapsed since `speaker_cycle_base_month`
- Formula: `cycle = ((monthsElapsed % 3) + 3) % 3 + 1`
- Labels are determined by both the occurrence-in-month and the cycle number

---

## 9. Database Design (PostgreSQL)

**Database name**: `church_programs` (configurable via env var `DATABASE_URL`)

### Tables

#### `ward_config` (singleton, id = 1)
Stores all scheduling rules and rotation state. Only one row ever exists.

| Column | Type | Purpose |
|---|---|---|
| id | bigint PK | Always 1 |
| stake_name | varchar(200) | |
| ward_name | varchar(200) | |
| acknowledgement_template | text | With placeholders |
| sacrament_time | varchar(10) | |
| bishopric_preferred_day | varchar(20) | "Thursday" or "Sunday" |
| bishopric_thursday_time | varchar(10) | |
| bishopric_sunday_time | varchar(10) | |
| ward_council_occurrences | varchar(20) | e.g. "1,3" |
| ward_council_time | varchar(10) | |
| speaker_cycle_base_month | varchar(10) | e.g. "2026-01" |
| last_sacrament_conductor_id | bigint | FK reference (logical) |
| last_bishopric_conductor_id | bigint | FK reference (logical) |
| wc_opening_prayer_idx | int | |
| wc_closing_prayer_idx | int | |
| wc_handbook_idx | int | Rolling base for WC assignments |
| bp_opening_prayer_idx | int | |
| bp_closing_prayer_idx | int | |
| bp_handbook_idx | int | Rolling base for BP assignments |

#### `conductors`
| Column | Type | Purpose |
|---|---|---|
| id | bigint PK auto | |
| name | varchar(200) NOT NULL | |
| display_order | int | Sort order in dropdowns |
| program_type | varchar(20) | `"sacrament"` or `"bishopric"` |

#### `auxiliaries`
| Column | Type | Purpose |
|---|---|---|
| id | bigint PK auto | |
| name | varchar UNIQUE NOT NULL | |

#### `musicians`
| Column | Type | Purpose |
|---|---|---|
| id | bigint PK auto | |
| name | varchar(200) NOT NULL | |
| musician_type | varchar(20) | `"chorister"` or `"pianist"` |
| display_order | int | |

#### `saved_programs`
| Column | Type | Purpose |
|---|---|---|
| id | bigint PK auto | |
| meeting_type | varchar(30) NOT NULL | `SACRAMENT`, `BISHOPRIC`, or `WARD_COUNCIL` |
| description | varchar(200) NOT NULL | Human-readable label |
| meeting_date | date NOT NULL | |
| program_data | TEXT NOT NULL | Full JSON of the program object |
| created_at | timestamp NOT NULL | Set on insert via `@PrePersist` |

### Hibernate DDL
`spring.jpa.hibernate.ddl-auto=update` — Hibernate auto-creates or alters tables on startup.  
Flyway migration `V1_1__insert_auxiliaries.sql` seeds initial auxiliary data.

### Connection
Configured via environment variables with fallback defaults:
```
DATABASE_URL       → jdbc:postgresql://localhost:5432/church_programs
DATABASE_USERNAME  → postgres
DATABASE_PASSWORD  → (set locally)
```

---

## 10. Backend Architecture (Spring Boot)

### Package Structure
```
com.church.programgenerator
├── ProgramGeneratorApplication.java       (main)
├── controller/
│   ├── HomeController.java                GET /
│   ├── SacramentController.java           GET/POST /sacrament/*
│   ├── BishopricController.java           GET/POST /bishopric/*
│   ├── WardCouncilController.java         GET/POST /ward-council/*
│   ├── HistoryController.java             GET/POST /history/*
│   ├── ScheduleRulesController.java       GET/POST /rules/*
│   ├── ConductorController.java           GET/POST /manage, /conductors/*
│   ├── AuxiliaryAdminController.java      POST /auxiliaries/*
│   ├── MusicianController.java            POST /musicians/*
│   └── GlobalExceptionHandler.java        @ControllerAdvice
├── model/
│   ├── WardConfig.java                    @Entity — singleton config
│   ├── Conductor.java                     @Entity
│   ├── Auxiliary.java                     @Entity
│   ├── Musician.java                      @Entity
│   ├── SavedProgram.java                  @Entity
│   ├── SacramentProgram.java              POJO (not entity)
│   ├── BishopricProgram.java              POJO (not entity)
│   ├── WardCouncilProgram.java            POJO (not entity)
│   ├── Speaker.java                       Embedded in SacramentProgram
│   └── AgendaItem.java                    Embedded in Bishopric/WardCouncil
├── repository/
│   ├── WardConfigRepository.java          JpaRepository<WardConfig, Long>
│   ├── ConductorRepository.java           + findByProgramTypeOrderByDisplayOrder
│   ├── AuxiliaryRepository.java           + findByName
│   ├── MusicianRepository.java            + findByMusicianType
│   └── SavedProgramRepository.java        + findByMeetingType (pageable)
└── service/
    ├── WardConfigService.java             All scheduling logic, round-robin, date calc
    ├── ConductorService.java              CRUD + getByType
    ├── AuxiliaryService.java              CRUD + existsByName
    ├── MusicianService.java               CRUD + getChoristers/getPianists
    ├── ProgramStorageService.java         JSON serialize/deserialize + save/load/delete
    ├── FileStorageService.java            Write files to reports folder, filename generation
    ├── SacramentProgramDocumentService.java   Generate DOCX
    ├── SacramentProgramPreviewService.java    Build preview model
    ├── BishopricProgramDocumentService.java   Generate DOCX
    ├── BishopricProgramPdfService.java        Generate PDF
    ├── WardCouncilDocumentService.java        Generate DOCX
    ├── WardCouncilPdfService.java             Generate PDF
    └── WardCouncilPngService.java             Generate PNG
```

### Key Design Patterns
- **MVC**: Controllers handle HTTP, pass models to Thymeleaf templates
- **Service Layer**: All business logic in services, never in controllers
- **Repository Layer**: Spring Data JPA — no SQL written by hand (Hibernate generates)
- **Singleton Config**: `WardConfig` always has id=1; `getConfig()` creates it on first use
- **JSON Serialization**: `ProgramStorageService` uses Jackson `ObjectMapper` to serialize/deserialize program POJOs to/from `saved_programs.program_data`
- **Agenda Items via JSON**: Dynamic agenda items are passed between form and server as a JSON string (`agendaItemsJson` request param), parsed on the server with Jackson

---

## 11. UI & Mobile Compatibility

- **Templates**: Thymeleaf with a shared `layout.html` providing the navigation bar and footer fragment used by all pages (`th:replace="layout :: navigation"`)
- **Responsive CSS**: Mobile-first using CSS Grid and media queries in `style.css`
- **Breakpoints**:
  - ≤768px: hamburger navigation (☰), single-column forms, stacked table rows
  - ≤520px: speaker rows stack vertically
  - ≤480px: all `input`, `textarea`, `select` set to `font-size: 16px` to prevent iOS Safari auto-zoom
- **Hamburger Menu**: JavaScript in `navigation.js` — tap ☰ to expand/collapse, auto-closes on link tap or outside click
- **Dynamic Speaker Rows**: Add/remove speaker rows via JavaScript (`addSpeaker()`, `removeSpeaker()`)
- **Dynamic Agenda Rows**: Add/remove agenda item rows via JavaScript, serialized to JSON on form submit
- **Flash Messages**: Thymeleaf reads `successMessage` and `errorMessage` from flash attributes and renders inline banners
- **History Table**: Low-priority columns hidden on small screens; action buttons stack vertically

---

## 12. Flutter Android Migration Guide

This section maps every working feature to what must be implemented in the Flutter Android app.

### Architecture Recommendations
- **Local database**: SQLite via `sqflite` or `drift` — replaces PostgreSQL
- **State management**: Provider, Riverpod, or BLoC
- **PDF generation**: `pdf` package (dart) — build identical layouts
- **DOCX generation**: No native Flutter support — use server-side endpoint or `archive` + OOXML templates
- **PNG/image export**: `flutter_screenshot` or render a widget to image using `RenderRepaintBoundary`
- **File save/share**: `path_provider` + `share_plus` for saving and sharing exports

### Feature-by-Feature Mapping

| Web Feature | Flutter Equivalent |
|---|---|
| Sacrament form (single screen) | `SacramentFormScreen` — single scrollable form |
| Speaker rows (add/remove) | Dynamic `ListView` with add/remove buttons |
| Bishopric form | `BishopricFormScreen` — with locked Presiding widget |
| Ward Council form | `WardCouncilFormScreen` |
| Preview screen | Dedicated read-only preview widget, full-screen |
| Export PDF | Generate PDF in-app, save to local storage, share |
| Export DOCX | Generate from template or share via other means |
| Export PNG | Render preview widget to PNG, save/share |
| History (paginated) | `HistoryScreen` with `ListView` + pagination |
| Load/Edit from history | Deep-link back to form screen with pre-filled data |
| Delete history entry | Swipe-to-delete or long-press menu |
| Manage tab | `ManageScreen` with `TabBar` for each list type |
| Add/Edit/Delete conductors | CRUD widget per list (conductor-sacrament, conductor-bishopric) |
| Add/Delete auxiliaries | CRUD list widget |
| Add/Edit/Delete choristers | CRUD list widget |
| Add/Edit/Delete pianists | CRUD list widget |
| Rules tab | `RulesScreen` — form fields matching `ward_config` columns |
| Auto-date calculation | Dart `DateTime` logic matching the Java algorithms |
| Conductor round-robin | Same algorithm in Dart using local DB for last-used index |
| Prayer/handbook round-robin | `nextThreeIndices` logic in Dart |
| Speaker type badge | Dart implementation of `getSpeakerTypeLabel` |
| Acknowledgement template substitution | String replace logic in Dart |
| Flash messages | `SnackBar` or banner widget |
| Logo in documents | Embed `LDS_LOGO.png` asset in PDF/image exports |
| Hamburger nav | `Drawer` or `BottomNavigationBar` |

### Data Model for SQLite

Replicate the exact same tables:
- `ward_config` (singleton, id=1)
- `conductors` (with `program_type`: "sacrament" / "bishopric")
- `auxiliaries`
- `musicians` (with `musician_type`: "chorister" / "pianist")
- `saved_programs` (store program as JSON string)

### Scheduling Logic to Port (Dart)

```
nextSacramentDate()     → next Sunday from today (today counts)
nextBishopricDate()     → next Thursday or Sunday (strictly future)
nextWardCouncilDate()   → next Sunday matching ward_council_occurrences list
getSundayOccurrence()   → (dayOfMonth - 1) ~/ 7 + 1
getSpeakerCycleNumber() → ((monthsElapsed % 3) + 3) % 3 + 1
getSpeakerTypeLabel()   → map occurrence + cycle to label string
getSuggestedConductor() → round-robin after last_used_id
nextThreeIndices()      → [base+1, base+2, base+3] mod list.length
```

### Important Implementation Notes
1. The Bishop is always identified as the first bishopric conductor whose name starts with "Bishop" — this detection must be preserved in Flutter.
2. Prayer auto-assignment always selects 3 different people — no two fields get the same person in the same meeting.
3. Rotation indices persist to the local DB immediately after each form load so the next load advances correctly.
4. History records store the full serialized program as JSON — design Flutter data classes to match the POJO fields exactly.
5. All dropdown data (conductors, auxiliaries, musicians) comes from local DB — no hardcoded lists.
6. Acknowledgement template is editable by the user and stored in `ward_config`, not hardcoded.

---

*Last updated: April 14, 2026*

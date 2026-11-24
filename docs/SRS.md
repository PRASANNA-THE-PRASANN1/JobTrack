# JobTrack — Software Requirements Specification (SRS)

## 1. Introduction

### 1.1 Purpose
JobTrack is a full-stack web application designed for job seekers to efficiently track, manage, and optimize their job applications throughout the recruitment lifecycle.

### 1.2 System Scope
The application is structured into multi-phase delivery:
- **Phase 1 (Completed & Verified)**: Core Job Application Management (Full CRUD, Status Lifecycle, Filtering, Search, KPIs, and Persistence).
- **Phase 2 (Active Implementation)**: Interview Scheduling, Timeline & Audit Logs, Upcoming Schedule Aggregation, and Document/Artifact Tracking.
- **Phase 3 (Future Scope)**: Application analytics, export/import (CSV/JSON), and email notification integrations.

---

## 2. Technology Stack

- **Frontend**: React 18 / 19, Vite, Tailwind CSS, Lucide React, Axios.
- **Backend**: Java 21, Spring Boot 3.3+, Spring Data JPA, Jakarta Validation, SpringDoc OpenAPI (Swagger).
- **Persistence**: PostgreSQL 16 (with H2 in-memory profile for seamless development / test isolation).
- **API Protocol**: RESTful JSON over HTTP, CORS-enabled for modern decoupled frontends.

---

## 3. Phase 1 Functional Requirements (Completed)

### FR-01: Create Job Application
- Allows creating job applications with company, title, location, work mode, employment type, status, salary range, dates, and notes.

### FR-02: Read & Filter Applications
- Paginated and sortable list with full-text search, status filtering, and work mode selection.

### FR-03: Update Job Application
- Editable fields with audit timestamp updates.

### FR-04: Delete Job Application
- Safe deletion with confirmation.

### FR-05: Application Metrics & KPIs
- Aggregate summary returning total, active pipeline, applied, interviewing, offered, and rejected counts.

---

## 4. Phase 2 Functional Requirements (Current Scope)

### FR-06: Interview Round Scheduling & Management
- The system shall allow users to schedule and manage multi-round interviews for any job application:
  - `applicationId` (FK to `job_applications.id`, Required)
  - `roundName` (String, Required, e.g. "Screening Call", "Coding Assessment", "System Design Loop", "Hiring Manager")
  - `roundType` (Enum: `PHONE_SCREEN`, `TECHNICAL`, `BEHAVIORAL`, `SYSTEM_DESIGN`, `TAKE_HOME`, `ONSITE`, `FINAL_ROUND`, Required)
  - `interviewDate` (Instant / LocalDateTime, Required)
  - `durationMinutes` (Integer, Optional, default 45)
  - `interviewerNames` (String, Optional)
  - `meetingLink` (String URL, Optional)
  - `status` (Enum: `SCHEDULED`, `COMPLETED`, `CANCELLED`, `RESCHEDULED`, Required, default `SCHEDULED`)
  - `feedbackNotes` (Text, Optional)
  - `rating` (Integer 1-5, Optional)
- Status transitions: Completing an interview can automatically log feedback and update the application timeline.

### FR-07: Application Activity Timeline & Audit Logs
- The system shall automatically record timeline events when:
  - An application is created.
  - An application status changes (e.g. `APPLIED` -> `INTERVIEWING`).
  - An interview is scheduled or completed.
  - A manual note/event is submitted by the user (`NOTE_ADDED`, `FOLLOW_UP_SENT`, `SALARY_NEGOTIATION`).
- The system shall return a chronological activity feed for any application.

### FR-08: Upcoming Interviews Aggregation
- The system shall provide an endpoint returning all future/upcoming interviews sorted chronologically across all active applications to power the dashboard schedule widget.

### FR-09: Document & Artifact Tracking
- The system shall allow tracking resume versions, cover letters, and portfolio links associated with an application:
  - `documentName` (String, Required)
  - `documentType` (Enum: `RESUME`, `COVER_LETTER`, `PORTFOLIO`, `OFFER_LETTER`, `OTHER`, Required)
  - `documentUrl` (String, Optional)
  - `notes` (String, Optional)

---

## 5. REST API Specification

### Phase 1 Endpoints
| Method | Endpoint | Description | Response Code |
|---|---|---|---|
| `GET` | `/api/v1/health` | Service health status | 200 OK |
| `GET` | `/api/v1/applications` | List applications (with search, filter, sort, page) | 200 OK |
| `GET` | `/api/v1/applications/{id}` | Get application by ID | 200 OK / 404 |
| `POST` | `/api/v1/applications` | Create a new application | 201 Created / 400 |
| `PUT` | `/api/v1/applications/{id}` | Update existing application | 200 OK / 400 / 404 |
| `DELETE` | `/api/v1/applications/{id}` | Delete application | 200 OK / 404 |
| `GET` | `/api/v1/applications/stats` | Aggregate counts and KPI metrics | 200 OK |

### Phase 2 Endpoints
| Method | Endpoint | Description | Response Code |
|---|---|---|---|
| `GET` | `/api/v1/applications/{appId}/interviews` | List interview rounds for an application | 200 OK / 404 |
| `POST` | `/api/v1/applications/{appId}/interviews` | Schedule new interview round | 201 Created / 400 |
| `PUT` | `/api/v1/interviews/{id}` | Update interview details, status, or feedback | 200 OK / 400 / 404 |
| `DELETE` | `/api/v1/interviews/{id}` | Remove an interview round | 200 OK / 404 |
| `GET` | `/api/v1/interviews/upcoming` | Retrieve all upcoming interviews across applications | 200 OK |
| `GET` | `/api/v1/applications/{appId}/timeline` | Retrieve chronological activity feed | 200 OK / 404 |
| `POST` | `/api/v1/applications/{appId}/timeline` | Add manual timeline event / note | 201 Created / 400 |
| `GET` | `/api/v1/applications/{appId}/documents` | List attached documents / links | 200 OK / 404 |
| `POST` | `/api/v1/applications/{appId}/documents` | Add document reference | 201 Created / 400 |
| `DELETE` | `/api/v1/documents/{id}` | Remove document reference | 200 OK / 404 |

---

## 6. Acceptance Criteria

- **AC-01 through AC-10**: Phase 1 verified and passing.
- **AC-11**: User can schedule multi-round interviews with date, duration, interviewers, and meeting link.
- **AC-12**: Scheduled interviews appear chronologically in the application detail and in the upcoming dashboard widget.
- **AC-13**: Updating an interview status or logging feedback updates the record and appends a timeline event.
- **AC-14**: System automatically creates timeline entries upon application creation, status transitions, and interview scheduling.
- **AC-15**: Users can log documents (resume variant used, portfolio link, offer letter) attached to an application.
- **AC-16**: Automated tests for interview scheduling, timeline generation, and document management pass 100%.

# JobTrack — Job Application & Interview Pipeline Tracker

JobTrack is a full-stack, enterprise-grade job application and interview tracking system designed for job seekers to organize their applications, manage multi-round interviews, inspect full activity audit trails, and archive application artifacts.

---

## Key Features

### Phase 1 — Core Application Lifecycle Management
* **Comprehensive CRUD**: Create, view, update, and delete applications with salary ranges, work modes (Remote, Hybrid, Onsite), employment types, and recruiter contacts.
* **Status Pipeline**: Transitions across `SAVED`, `APPLIED`, `SCREENING`, `INTERVIEWING`, `OFFERED`, `REJECTED`, and `WITHDRAWN`.
* **Dynamic Search & Filtering**: Instant search across company name and job title, status filters, work mode filters, and column sorting.
* **KPI Metrics Dashboard**: Real-time aggregated counters for Total Applied, Active Pipeline, Interviews, Offers, and Rejections.

### Phase 2 — Interview Rounds, Activity Timeline & Artifact Tracking
* **Interview Scheduling & Round Tracking**: Record multi-round interviews with round types (`PHONE_SCREEN`, `TECHNICAL`, `SYSTEM_DESIGN`, `BEHAVIORAL`, `TAKE_HOME`, `ONSITE`, `FINAL_ROUND`), interviewer names, date/time, duration, and direct meeting links (Zoom/Google Meet/Teams).
* **Self-Assessment Feedback & Star Ratings**: Complete interview rounds with 1–5 star ratings, feedback notes, and questions asked.
* **Upcoming Interviews Banner Widget**: Prominently highlights upcoming scheduled rounds with one-click access to meeting rooms and applications.
* **Chronological Activity Timeline**: Automated audit trail logging application creation, status changes, scheduled interviews, completed reviews, and attached documents, plus support for manual log notes.
* **Document & Artifact Tracker**: Attach and organize tailored resume versions, cover letters, portfolio links, and offer letters with external URLs and notes.

---

## Tech Stack

* **Frontend**: React 18, Vite 5, Tailwind CSS, Lucide React, Axios
* **Backend**: Java 21, Spring Boot 3.3.5, Spring Data JPA, Jakarta Validation, SpringDoc OpenAPI (Swagger UI)
* **Database**: PostgreSQL 16 (with zero-configuration H2 development fallback profile)
* **API Architecture**: RESTful JSON API with global CORS support and standardized `ApiResponse<T>` envelope

---

## Directory Structure

```
JobTrack/
├── backend/                         # Java Spring Boot Backend
│   ├── src/main/java/com/jobtrack/
│   │   ├── config/                  # CORS, JPA Auditing, and OpenAPI Configuration
│   │   ├── controller/              # REST Controllers (Applications, Interviews, Timeline, Documents, Health)
│   │   ├── dto/                     # Request/Response DTOs & Validation
│   │   ├── entity/                  # JPA Entities (JobApplication, InterviewRound, TimelineEvent, ApplicationDocument)
│   │   ├── exception/               # GlobalExceptionHandler (@RestControllerAdvice)
│   │   ├── repository/              # Spring Data JPA Repositories
│   │   └── service/                 # Business Logic & Automated Audit Logging
│   ├── src/main/resources/
│   │   ├── application.properties   # General settings & active profiles
│   │   ├── application-dev.properties # H2 PostgreSQL-compatible in-memory profile
│   │   └── application-postgres.properties # Production/Local PostgreSQL profile
│   ├── src/test/java/com/jobtrack/  # JUnit 5 & MockMvc Unit & Integration Tests (20 tests)
│   ├── pom.xml                      # Maven Dependencies & Build Configuration
│   └── mvnw.cmd / mvnw              # Maven Wrapper
├── frontend/                        # React Frontend
│   ├── src/
│   │   ├── api/                     # Axios Client & Typed Application Service
│   │   ├── components/              # Common UI, Modals, Timeline, Filters, Tables, KPI Cards
│   │   ├── utils/                   # Currency & Date Formatters
│   │   ├── App.jsx                  # Main Dashboard Container & Modals Coordination
│   │   └── main.jsx                 # Application Entrypoint
│   ├── package.json                 # Node Dependencies & Scripts
│   ├── vite.config.js               # Vite Configuration & Backend Proxy
│   └── tailwind.config.js           # Tailwind CSS Styling
└── docs/
    └── SRS.md                       # Software Requirements Specification (Phase 1 & Phase 2)
```

---

## Prerequisites

1. **Java JDK 21**: Microsoft OpenJDK 21 or Eclipse Temurin JDK 21
2. **Node.js**: v18+ (Node v20 LTS recommended) and npm
3. **Maven**: Maven 3.9+ (or use the included `./mvnw` / `mvnw.cmd` wrapper)
4. **PostgreSQL** *(Optional)*: If running with real PostgreSQL, PostgreSQL 14+ on port 5432. Otherwise, the included development profile (`dev`) runs out-of-the-box with H2 in PostgreSQL compatibility mode.

---

## Quick Start (Running Both Tiers)

### 1. Start the Spring Boot Backend

Open a terminal in the `backend/` directory:

```powershell
cd backend

# Run with the default dev profile (H2 in PostgreSQL mode, zero setup):
./mvnw spring-boot:run

# OR run with a local PostgreSQL service:
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

The backend server starts on **`http://localhost:8080`**.
* **Health Check**: `http://localhost:8080/api/v1/health`
* **Swagger UI (Interactive API Docs)**: `http://localhost:8080/swagger-ui.html`
* **H2 Database Console** *(dev mode)*: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:jobtrackdb`, User: `sa`, Password: empty)

---

### 2. Start the React Frontend

Open a second terminal in the `frontend/` directory:

```powershell
cd frontend

# Install dependencies:
npm install

# Start Vite development server:
npm run dev
```

The frontend will start at **`http://localhost:5173`**.

Open your browser at **`http://localhost:5173`** to interact with the full dashboard.

---

## Running Automated Tests

### Backend Unit & Integration Tests (20 Tests)

```powershell
cd backend
./mvnw test
```

Runs comprehensive unit and MockMvc integration tests:
* `JobApplicationServiceTest`: CRUD validation, DTO mapping, status transitions, KPI calculations.
* `JobApplicationControllerTest`: MockMvc integration testing for routes, validation constraints, pagination, and error responses.
* `InterviewRoundServiceTest`: Interview scheduling, upcoming round filters, and feedback recording.
* `InterviewRoundControllerTest`: MockMvc tests for interview endpoints.
* `TimelineServiceTest`: Chronological timeline retrieval and manual note auditing.

### Frontend Production Build Verification

```powershell
cd frontend
npm run build
```

Compiles the React application with Vite and validates syntax, imports, and Tailwind CSS styles.

---

## REST API Specification

### Applications
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/health` | Service health status |
| `GET` | `/api/v1/applications` | Paginated and filtered applications list (`search`, `status`, `workMode`, `page`, `size`, `sortBy`, `direction`) |
| `GET` | `/api/v1/applications/{id}` | Retrieve specific application details |
| `POST` | `/api/v1/applications` | Create a new job application |
| `PUT` | `/api/v1/applications/{id}` | Update existing application |
| `DELETE` | `/api/v1/applications/{id}` | Delete application |
| `GET` | `/api/v1/applications/stats` | Aggregate counters (Total, Active, Applied, Interviewing, Offered, Rejected) |

### Interviews & Rounds
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/applications/{appId}/interviews` | List all interview rounds for an application |
| `GET` | `/api/v1/interviews/upcoming` | Retrieve all scheduled upcoming rounds ordered by date |
| `POST` | `/api/v1/applications/{appId}/interviews` | Schedule a new interview round |
| `PUT` | `/api/v1/interviews/{id}` | Update round details, completion status, rating, or feedback |
| `DELETE` | `/api/v1/interviews/{id}` | Delete an interview round |

### Timeline & Audit Trail
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/applications/{appId}/timeline` | Retrieve chronological activity log for an application |
| `POST` | `/api/v1/applications/{appId}/timeline` | Add a manual activity note / follow-up entry |

### Documents & Artifacts
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/applications/{appId}/documents` | List attached documents and URLs for an application |
| `POST` | `/api/v1/applications/{appId}/documents` | Attach a document or external link (Resume, Cover Letter, Portfolio, Offer) |
| `DELETE` | `/api/v1/documents/{id}` | Delete a document attachment |

---

## Sample cURL Commands

### 1. Create Application
```bash
curl -X POST http://localhost:8080/api/v1/applications \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Google",
    "jobTitle": "Staff Software Engineer",
    "jobLocation": "Mountain View, CA",
    "workMode": "HYBRID",
    "employmentType": "FULL_TIME",
    "applicationDate": "2026-11-20",
    "status": "APPLIED",
    "salaryMin": 220000,
    "salaryMax": 290000,
    "salaryCurrency": "USD",
    "jobUrl": "https://careers.google.com/jobs/123",
    "contactPerson": "Alex Rivera",
    "contactEmail": "arivera@google.com"
  }'
```

### 2. Schedule Interview Round
```bash
curl -X POST http://localhost:8080/api/v1/applications/1/interviews \
  -H "Content-Type: application/json" \
  -d '{
    "roundName": "Technical Architecture Screen",
    "roundType": "SYSTEM_DESIGN",
    "interviewDate": "2026-12-05T15:00:00",
    "durationMinutes": 60,
    "interviewerNames": "Jane Doe (Principal Eng)",
    "meetingLink": "https://meet.google.com/abc-xyz"
  }'
```

### 3. Record Interview Feedback & Rating
```bash
curl -X PUT http://localhost:8080/api/v1/interviews/1 \
  -H "Content-Type: application/json" \
  -d '{
    "roundName": "Technical Architecture Screen",
    "roundType": "SYSTEM_DESIGN",
    "interviewDate": "2026-12-05T15:00:00",
    "durationMinutes": 60,
    "status": "COMPLETED",
    "rating": 5,
    "feedbackNotes": "Deep dive into distributed cache invalidation and Raft consensus."
  }'
```

### 4. Attach Resume Document
```bash
curl -X POST http://localhost:8080/api/v1/applications/1/documents \
  -H "Content-Type: application/json" \
  -d '{
    "documentName": "Resume_Staff_Distributed_v2.pdf",
    "documentType": "RESUME",
    "documentUrl": "https://storage.googleapis.com/resumes/staff_v2.pdf",
    "notes": "Customized for cloud infrastructure"
  }'
```

### 5. Log Timeline Follow-up Note
```bash
curl -X POST http://localhost:8080/api/v1/applications/1/timeline \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "NOTE_ADDED",
    "title": "Recruiter debrief call",
    "description": "Recruiter confirmed positive review; advancing to offer committee."
  }'
```

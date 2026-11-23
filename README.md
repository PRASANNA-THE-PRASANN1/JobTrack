# JobTrack — Job Application Tracker (Phase 1)

JobTrack is a full-stack, enterprise-grade job application tracking system designed for job seekers to track, organize, and monitor their job applications, status transitions, interview pipeline, and compensation details.

---

## Tech Stack

* **Frontend**: React 18, Vite, Tailwind CSS, Lucide React, Axios
* **Backend**: Java 21, Spring Boot 3.3.5, Spring Data JPA, Jakarta Validation, SpringDoc OpenAPI (Swagger UI)
* **Database**: PostgreSQL 16 (with zero-configuration H2 development fallback profile)
* **API Architecture**: RESTful JSON API with global CORS support and RFC 7807 error formatting

---

## Directory Structure

```
JobTrack/
├── backend/                         # Java Spring Boot Backend
│   ├── src/main/java/com/jobtrack/
│   │   ├── config/                  # CORS and OpenAPI Configuration
│   │   ├── controller/              # JobApplicationController & HealthController
│   │   ├── dto/                     # Request/Response DTOs & API Payloads
│   │   ├── entity/                  # JPA Entities (JobApplication, Enums)
│   │   ├── exception/               # GlobalExceptionHandler (@RestControllerAdvice)
│   │   ├── repository/              # Spring Data JPA Repositories
│   │   └── service/                 # Business Logic & Transactions
│   ├── src/main/resources/
│   │   ├── application.properties   # General settings & active profiles
│   │   ├── application-dev.properties # H2 PostgreSQL-compatible in-memory profile
│   │   └── application-postgres.properties # Production/Local PostgreSQL profile
│   ├── src/test/java/com/jobtrack/  # JUnit 5 & MockMvc Integration Tests
│   ├── pom.xml                      # Maven Dependencies & Build Configuration
│   └── mvnw.cmd / mvnw              # Maven Wrapper
├── frontend/                        # React Frontend
│   ├── src/
│   │   ├── api/                     # Axios Client & Typed Application Service
│   │   ├── components/              # Modals, Filters, Lists, Badges, KPI Cards
│   │   ├── utils/                   # Currency & Date Formatters
│   │   ├── App.jsx                  # Main Dashboard Container
│   │   └── main.jsx                 # Application Entrypoint
│   ├── package.json                 # Node Dependencies & Scripts
│   ├── vite.config.js               # Vite Configuration & Backend Proxy
│   └── tailwind.config.js           # Tailwind CSS Styling
└── docs/
    └── SRS.md                       # Software Requirements Specification (Phase 1)
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

Open your browser at `http://localhost:5173` to interact with the full application.

---

## Running Automated Tests

### Backend Unit & Integration Tests

```powershell
cd backend
./mvnw test
```

Runs comprehensive tests covering:
* `JobApplicationServiceTest`: Business validation, mapping, status calculation, and KPI tallying.
* `JobApplicationControllerTest`: MockMvc HTTP integration testing for all CRUD routes, validation constraints, pagination, and error responses.

### Frontend Production Build Test

```powershell
cd frontend
npm run build
```

Compiles the React application with Vite and validates syntax, imports, and Tailwind CSS styles.

---

## REST API Specification

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/health` | Service health status |
| `GET` | `/api/v1/applications` | Paginated and filtered applications list (`search`, `status`, `workMode`, `page`, `size`, `sortBy`, `direction`) |
| `GET` | `/api/v1/applications/{id}` | Retrieve specific application details |
| `POST` | `/api/v1/applications` | Create a new job application |
| `PUT` | `/api/v1/applications/{id}` | Update existing application |
| `DELETE` | `/api/v1/applications/{id}` | Delete application |
| `GET` | `/api/v1/applications/stats` | Aggregate counters (Total, Active, Applied, Interviewing, Offered, Rejected) |

### Sample cURL Commands

#### Create Application
```bash
curl -X POST http://localhost:8080/api/v1/applications \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Google",
    "jobTitle": "Senior Software Engineer",
    "jobLocation": "Mountain View, CA",
    "workMode": "HYBRID",
    "employmentType": "FULL_TIME",
    "status": "APPLIED",
    "salaryCurrency": "USD",
    "salaryMin": 160000,
    "salaryMax": 210000,
    "jobUrl": "https://careers.google.com/jobs/123",
    "notes": "Referred by senior engineer; tech screen in 2 weeks."
  }'
```

#### Fetch Applications
```bash
curl http://localhost:8080/api/v1/applications?search=Google&status=APPLIED
```

#### Get Pipeline Stats
```bash
curl http://localhost:8080/api/v1/applications/stats
```

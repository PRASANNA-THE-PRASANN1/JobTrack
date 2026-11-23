# JobTrack — Software Requirements Specification (SRS)

## 1. Introduction

### 1.1 Purpose
JobTrack is a full-stack web application designed for job seekers to efficiently track, manage, and optimize their job applications throughout the recruitment lifecycle.

### 1.2 System Scope
The application is structured into multi-phase delivery:
- **Phase 1 (Current Scope)**: Core Job Application Management (Full CRUD, Status Lifecycle, Filtering, Search, KPIs, and Persistence).
- **Phase 2 (Future Scope)**: Interview scheduling, timeline logs, contact management, and document attachment (resumes/cover letters).
- **Phase 3 (Future Scope)**: Application analytics, export/import (CSV/JSON), and email notification integrations.

---

## 2. Technology Stack

- **Frontend**: React 18 / 19, Vite, Tailwind CSS, Lucide React, Axios.
- **Backend**: Java 17 / 21, Spring Boot 3.3+, Spring Data JPA, Jakarta Validation, SpringDoc OpenAPI (Swagger).
- **Persistence**: PostgreSQL 16 (with H2 in-memory profile for seamless development / test isolation).
- **API Protocol**: RESTful JSON over HTTP, CORS-enabled for modern decoupled frontends.

---

## 3. Phase 1 Functional Requirements

### FR-01: Create Job Application
- The system shall allow users to record a new job application with the following fields:
  - `companyName` (String, Required, 2-150 chars)
  - `jobTitle` (String, Required, 2-150 chars)
  - `jobLocation` (String, Optional, max 100 chars, e.g. "San Francisco, CA" or "Remote")
  - `workMode` (Enum: `REMOTE`, `HYBRID`, `ONSITE`, Required, default `REMOTE`)
  - `employmentType` (Enum: `FULL_TIME`, `PART_TIME`, `CONTRACT`, `INTERNSHIP`, Required, default `FULL_TIME`)
  - `status` (Enum: `SAVED`, `APPLIED`, `SCREENING`, `INTERVIEWING`, `OFFERED`, `REJECTED`, `WITHDRAWN`, Required, default `APPLIED`)
  - `salaryMin` (Decimal, Optional, non-negative)
  - `salaryMax` (Decimal, Optional, >= salaryMin)
  - `salaryCurrency` (String, Optional, default `USD`)
  - `jobUrl` (String URL, Optional, max 1024 chars)
  - `applicationDate` (Date, Required, default today's date)
  - `jobDescription` (Text, Optional)
  - `notes` (Text, Optional)
  - `contactPerson` (String, Optional, max 100 chars)
  - `contactEmail` (Email, Optional, valid email format)

### FR-02: Read & Filter Applications
- The system shall return a paginated and sortable list of job applications.
- The system shall support filtering by:
  - Text search (matching `companyName` or `jobTitle` case-insensitively).
  - Status (e.g. `APPLIED`, `INTERVIEWING`, `OFFERED`, etc.).
  - Work mode (`REMOTE`, `HYBRID`, `ONSITE`).
- The system shall allow retrieving full details of an application by its unique ID.

### FR-03: Update Job Application
- The system shall allow users to update any editable field of an existing application.
- Status changes shall automatically update the `updatedAt` audit timestamp.

### FR-04: Delete Job Application
- The system shall allow users to delete an application with confirmation.
- The system shall return a 204 No Content or success confirmation on successful deletion.

### FR-05: Application Metrics & KPIs
- The system shall provide an aggregated summary endpoint returning:
  - Total number of applications.
  - Count by status (`APPLIED`, `INTERVIEWING`, `OFFERED`, `REJECTED`, etc.).
  - Active applications count (`APPLIED + SCREENING + INTERVIEWING`).

---

## 4. REST API Specification

| Method | Endpoint | Description | Response Code |
|---|---|---|---|
| `GET` | `/api/v1/health` | Service health status | 200 OK |
| `GET` | `/api/v1/applications` | List applications (with search, filter, sort, page) | 200 OK |
| `GET` | `/api/v1/applications/{id}` | Get application by ID | 200 OK / 404 |
| `POST` | `/api/v1/applications` | Create a new application | 201 Created / 400 |
| `PUT` | `/api/v1/applications/{id}` | Update existing application | 200 OK / 400 / 404 |
| `DELETE` | `/api/v1/applications/{id}` | Delete application | 204 No Content / 404 |
| `GET` | `/api/v1/applications/stats` | Aggregate counts and KPI metrics | 200 OK |

---

## 5. Acceptance Criteria

- **AC-01**: Backend starts successfully and connects to the database.
- **AC-02**: Creating an application with valid inputs persists to the database and returns 201 Created.
- **AC-03**: Submitting invalid data (e.g. empty company name or title) triggers validation errors (400 Bad Request) with informative messages.
- **AC-04**: Fetching `/api/v1/applications` returns correct paginated application records.
- **AC-05**: Updating an application's status (e.g. from `APPLIED` to `INTERVIEWING`) persists and reflects immediately.
- **AC-06**: Deleting an application removes it and subsequent fetches return 404.
- **AC-07**: Metrics endpoint accurately tallies total, applied, interviewing, offered, and rejected counts.
- **AC-08**: Frontend connects to the live backend API for all operations without mock data fallbacks.
- **AC-09**: Frontend UI provides instant visual feedback, responsive layouts, clean status badges, and intuitive modal dialogs.
- **AC-10**: Backend automated test suite (JUnit + MockMvc) passes 100%.

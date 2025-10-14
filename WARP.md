# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Overview

This is a Spring Boot application for student registration with comprehensive testing using Testcontainers. The application manages student (Aluno) entities with email validation requiring "@senac" domain, and includes full CRUD operations via REST API.

## Architecture

**Technology Stack:**
- Spring Boot 3.3.1 with Java 17
- Spring Data JPA with PostgreSQL
- Maven build system
- JUnit 5 + Testcontainers for testing

**Core Components:**
- `StudentRegistrationApplication` - Main Spring Boot application
- `Aluno` - JPA entity representing students
- `AlunoRepository` - Spring Data repository
- `AlunoService` - Business logic with email validation (@senac domain required)
- `AlunoController` - REST endpoints at `/api/v1/alunos`

**Database Schema:**
- Single `aluno` table with fields: id (BIGSERIAL), nome (VARCHAR), idade (INTEGER), email (VARCHAR)
- Email validation enforces "@senac" domain requirement

## Development Commands

### Build and Run
```bash
# Clean build and run tests
mvn clean install

# Run the application
mvn spring-boot:run

# Application runs on http://localhost:8080
```

### Testing Commands
```bash
# Run all tests (unit + integration)
mvn test

# Run only integration tests with Testcontainers
mvn test -Dtest=AlunoIntegrationTest

# Run tests with detailed output
mvn test -Dtest=AlunoIntegrationTest -X

# View test reports
# Reports are in target/surefire-reports/
```

## API Endpoints

Base URL: `http://localhost:8080/api/v1/alunos`

- **POST** `/api/v1/alunos` - Create student (email must contain "@senac")
- **GET** `/api/v1/alunos` - List all students  
- **PUT** `/api/v1/alunos/{id}` - Update student
- **DELETE** `/api/v1/alunos/{id}` - Delete student

## Testing Strategy

**Test Structure:**
- **Unit Tests**: `AlunoTest`, `AlunoServiceTest`, `AlunoRepositoryTest` (H2 in-memory)
- **Integration Tests**: `AlunoControllerTest` (MockMvc), `AlunoIntegrationTest` (Testcontainers)
- **Integration Test Features**: Real PostgreSQL containers, ordered test execution, full CRUD testing

**Testcontainers Setup:**
- Uses `postgres:15-alpine` image
- Database: `testdb`, User/Pass: `test/test`
- Dynamic port configuration via `@DynamicPropertySource`
- Automatic container lifecycle management

**Email Validation Rules:**
- Must contain "@senac" (case-insensitive)
- Cannot be empty or null
- Validated in service layer

## Manual Testing

PowerShell examples for API testing:

```powershell
# Create valid student
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method POST -ContentType "application/json" -Body '{"nome":"João Silva", "idade":25, "email":"joao@senac.com"}'

# List all students
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method GET

# Create invalid student (will fail)
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method POST -ContentType "application/json" -Body '{"nome":"Maria", "idade":22, "email":"maria@email.com"}'
```

## Database Configuration

**Development Database:**
- PostgreSQL configured in `application.properties`
- Property `spring.jpa.hibernate.ddl-auto=update` auto-creates tables

**Manual Table Creation (if needed):**
```sql
CREATE TABLE aluno (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    idade INTEGER,
    email VARCHAR(255)
);
```

## Key Business Rules

- Email addresses must contain "@senac" domain
- Student names cannot be empty
- Age must be positive integer
- All CRUD operations validate email requirements
- Update operations require different email from current one
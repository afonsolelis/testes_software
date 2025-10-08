# Student Registration Application

This is a simple Spring Boot application for student registration.

## Database Setup

The application is configured to connect to a PostgreSQL database. The connection properties are in `src/main/resources/application.properties`.

The property `spring.jpa.hibernate.ddl-auto=update` should automatically create the `aluno` table. If the table is not created automatically, you can use the following SQL statement to create it manually:

```sql
CREATE TABLE aluno (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    idade INTEGER,
    email VARCHAR(255)
);
```

## How to Run

You will need Java 17 (or higher) and Maven installed.

1.  Navigate to the `student-registration` directory.
2.  Run the application using the following command:

    ```bash
    mvn spring-boot:run
    ```

## API Endpoints

Refer to the `rest.md` file for documentation on the available API endpoints.

### Testing API with PowerShell

You can test the API endpoints using PowerShell's `Invoke-RestMethod`:

#### Create a new student (success):
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method POST -ContentType "application/json" -Body '{"nome":"Fulano de Tal", "idade":25, "email":"fulano@senac.com"}'
```

#### Create a new student (error - invalid email):
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method POST -ContentType "application/json" -Body '{"nome":"Fulano de Tal", "idade":25, "email":"fulano@email.com"}'
```

#### List all students:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method GET
```

#### Update a student:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos/1" -Method PUT -ContentType "application/json" -Body '{"nome":"Fulano Atualizado", "idade":26, "email":"fulano.atualizado@senac.com"}'
```

#### Delete a student:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos/1" -Method DELETE
```

## Running Tests

### Run all tests:
```bash
mvn test
```

### Run integration tests with Testcontainers:
```bash
mvn test -Dtest=AlunoIntegrationTest
```

### Run tests with detailed output:
```bash
mvn test -Dtest=AlunoIntegrationTest -X
```

## Testcontainers Integration

This project includes integration tests using Testcontainers for realistic database testing. See `20251014.md` for detailed documentation about the Testcontainers implementation.

### Test Features:
- ✅ Real PostgreSQL database in Docker containers
- ✅ Complete CRUD operation tests
- ✅ Email validation tests
- ✅ Error handling tests
- ✅ Ordered test execution
- ✅ Automatic container cleanup

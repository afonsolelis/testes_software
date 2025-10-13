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

## VCR (Video Cassette Recorder) for API Testing

This project implements a VCR (Video Cassette Recorder) functionality to record and replay HTTP interactions. This is especially useful for teaching API testing concepts to students.

### How VCR Works
- **Record Mode**: Makes real HTTP requests to external APIs and saves the interactions to cassette files
- **Playback Mode**: Returns recorded responses without making actual HTTP requests
- **Educational Value**: Students can see real API interactions and understand how HTTP requests/responses work

### VCR Features:
- Records interactions with external APIs (e.g., ViaCEP API for address lookup)
- Saves interactions to JSON cassette files in `src/test/resources/vcr_cassettes/`
- Supports both recording and playback modes
- Provides a clear demonstration of HTTP interactions for learning purposes

### API Endpoint for VCR
```
GET /api/v1/vcr/cep/{cep}?recordMode={boolean}&cassette={cassetteName}
```

### Example Usage:
- Record: `GET /api/v1/vcr/cep/01001000?recordMode=true&cassette=saopaulo_center`
- Playback: `GET /api/v1/vcr/cep/01001000?recordMode=false&cassette=saopaulo_center`

### Additional API Endpoints
- `GET /api/v1/cep/{cep}` - Direct lookup on ViaCEP API without VCR

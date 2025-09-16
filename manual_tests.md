## Manual Tests (PowerShell)

To run the manual tests, you need to have the application running.

### Start the Application

To start the application, run the following command in the root directory of the project:

```bash
mvn spring-boot:run
```

### Test Cases

Open a PowerShell terminal to run the following commands.

#### Successful Cases

1.  **Create a valid student:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Peter Parker", "idade": 20, "email": "peter.parker@senac.com"}'
    ```

2.  **Create another valid student:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Mary Jane", "idade": 19, "email": "mary.jane@senac.com"}'
    ```

3.  **Create a student with a different valid email:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Harry Osborn", "idade": 21, "email": "harry.osborn@senac.com"}'
    ```

4.  **Create a student with a valid email with numbers:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Gwen Stacy", "idade": 20, "email": "gwen.stacy123@senac.com"}'
    ```

5.  **Get all students:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Get
    ```

#### Failing Cases

1.  **Create a student with an invalid email (not from @senac):**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Norman Osborn", "idade": 50, "email": "norman.osborn@oscorp.com"}'
    ```

2.  **Create a student with an email without the domain:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Otto Octavius", "idade": 45, "email": "otto.octavius"}'
    ```

3.  **Create a student with an empty email:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Curt Connors", "idade": 40, "email": ""}'
    ```

4.  **Create a student with an empty name:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "", "idade": 35, "email": "max.dillon@senac.com"}'
    ```

5.  **Create a student with a negative age:**

    ```powershell
    Invoke-RestMethod -Uri http://localhost:8080/api/v1/alunos -Method Post -ContentType "application/json" -Body '{"nome": "Flint Marko", "idade": -30, "email": "flint.marko@senac.com"}'
    ```
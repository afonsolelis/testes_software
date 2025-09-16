# API Endpoints

Este arquivo documenta os endpoints da API para a aplicação de cadastro de estudantes.

## Criar um novo Aluno

Cria um novo aluno no banco de dados.

**URL:** `/api/v1/alunos`

**Método:** `POST`

**Corpo da Requisição (JSON):**

```json
{
  "nome": "string",
  "idade": integer,
  "email": "string"
}
```

**Resposta de Sucesso (200 OK):**

```json
{
  "id": 1,
  "nome": "Fulano de Tal",
  "idade": 25,
  "email": "fulano@senac.com"
}
```

**Resposta de Erro (400 Bad Request):**

Se o e-mail não for válido (não contiver "@senac"), a API retornará uma mensagem de erro.

```
Email inválido. O email deve conter '@senac'.
```

**Exemplo de comando `curl` (sucesso):**

```bash
curl -X POST -H "Content-Type: application/json" -d '{"nome":"Fulano de Tal", "idade":25, "email":"fulano@senac.com"}' http://localhost:8080/api/v1/alunos
```

**Exemplo de comando `PowerShell` (sucesso):**

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method POST -ContentType "application/json" -Body '{"nome":"Fulano de Tal", "idade":25, "email":"fulano@senac.com"}'
```

**Exemplo de comando `curl` (erro):**

```bash
curl -X POST -H "Content-Type: application/json" -d '{"nome":"Fulano de Tal", "idade":25, "email":"fulano@email.com"}' http://localhost:8080/api/v1/alunos
```

**Exemplo de comando `PowerShell` (erro):**

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method POST -ContentType "application/json" -Body '{"nome":"Fulano de Tal", "idade":25, "email":"fulano@email.com"}'
```

## Listar todos os Alunos

Retorna uma lista com todos os alunos cadastrados no banco de dados.

**URL:** `/api/v1/alunos`

**Método:** `GET`

**Resposta de Sucesso (200 OK):**

```json
[
  {
    "id": 1,
    "nome": "Fulano de Tal",
    "idade": 25,
    "email": "fulano@senac.com"
  },
  {
    "id": 2,
    "nome": "Ciclana Silva",
    "idade": 22,
    "email": "ciclana@senac.com"
  }
]
```

**Exemplo de comando `curl`:**

```bash
curl -X GET http://localhost:8080/api/v1/alunos
```

**Exemplo de comando `PowerShell`:**

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos" -Method GET
```
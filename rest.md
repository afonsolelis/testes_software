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

## Atualizar um Aluno

Atualiza os dados de um aluno existente no banco de dados.

**URL:** `/api/v1/alunos/{id}`

**Método:** `PUT`

**Parâmetros da URL:**
- `id` (Long): ID do aluno a ser atualizado

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
  "nome": "Fulano de Tal Atualizado",
  "idade": 26,
  "email": "fulano.atualizado@senac.com"
}
```

**Resposta de Erro (400 Bad Request):**

Se o aluno não for encontrado ou o email for inválido.

```
Aluno não encontrado.
```

**Exemplo de comando `curl`:**

```bash
curl -X PUT -H "Content-Type: application/json" -d '{"nome":"Fulano Atualizado", "idade":26, "email":"fulano.atualizado@senac.com"}' http://localhost:8080/api/v1/alunos/1
```

**Exemplo de comando `PowerShell`:**

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos/1" -Method PUT -ContentType "application/json" -Body '{"nome":"Fulano Atualizado", "idade":26, "email":"fulano.atualizado@senac.com"}'
```

## Deletar um Aluno

Remove um aluno do banco de dados.

**URL:** `/api/v1/alunos/{id}`

**Método:** `DELETE`

**Parâmetros da URL:**
- `id` (Long): ID do aluno a ser deletado

**Resposta de Sucesso (200 OK):**

```
Aluno deletado com sucesso
```

**Resposta de Erro (400 Bad Request):**

Se o aluno não for encontrado.

```
Aluno não encontrado.
```

**Exemplo de comando `curl`:**

```bash
curl -X DELETE http://localhost:8080/api/v1/alunos/1
```

**Exemplo de comando `PowerShell`:**

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/alunos/1" -Method DELETE
```
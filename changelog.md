# Changelog

Data: 2025-10-14

## Resumo

- **Implementação completa de Testcontainers** para testes de integração com PostgreSQL real em containers Docker.
- **Substituição de H2 por PostgreSQL** nos testes, garantindo ambiente idêntico à produção.
- **Cobertura completa de testes CRUD** com 10 cenários ordenados cobrindo todos os endpoints da API.
- **Documentação técnica detalhada** sobre Testcontainers e suas vantagens.

## Mudanças

### Dependências (pom.xml)
- Adicionadas dependências do Testcontainers:
  - `org.testcontainers:junit-jupiter` (scope: test)
  - `org.testcontainers:postgresql` (scope: test) 
  - `org.springframework.boot:spring-boot-testcontainers` (scope: test)

### Testes de Integração
- **Criado**: `src/test/java/com/example/studentregistration/AlunoIntegrationTest.java`
  - Container PostgreSQL real (`postgres:15-alpine`)
  - 10 testes ordenados com `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)`
  - Configuração dinâmica de propriedades com `@DynamicPropertySource`
  - Testes de CRUD completos:
    - `testCreateAluno()` - Criação com email válido
    - `testCreateAlunoWithInvalidEmail()` - Validação de email inválido
    - `testGetAllAlunos()` - Listagem de alunos
    - `testUpdateAluno()` - Atualização com dados válidos
    - `testUpdateAlunoWithInvalidEmail()` - Atualização com email inválido
    - `testUpdateNonExistentAluno()` - Atualização de aluno inexistente
    - `testDeleteAluno()` - Exclusão de aluno
    - `testDeleteNonExistentAluno()` - Exclusão de aluno inexistente
    - `testGetAllAlunosAfterDeletion()` - Verificação após exclusão
    - `testCreateMultipleAlunos()` - Criação de múltiplos alunos

### Configuração de Teste
- **Criado**: `src/test/resources/application-test.properties`
  - Configuração específica para Testcontainers
  - Logging detalhado para debugging
  - Hibernate DDL auto-create-drop

### Documentação
- **Criado**: `20251014.md` - Documentação completa sobre Testcontainers
  - Explicação dos benefícios (ambiente real, isolamento, reprodutibilidade)
  - Exemplos de código para todos os cenários
  - Instruções de execução e configuração
  - Referências técnicas

### Controller Aprimorado
- **Adicionados endpoints**:
  - `PUT /api/v1/alunos/{id}` - Atualização de aluno
  - `DELETE /api/v1/alunos/{id}` - Exclusão de aluno
- **Melhorada validação** com método `validateEmail()` centralizado
- **Tratamento de erros** aprimorado para todos os endpoints

### Documentação REST Atualizada
- **Atualizado**: `rest.md` com novos endpoints (PUT/DELETE)
- **Adicionados comandos PowerShell** para todos os endpoints
- **Exemplos de resposta** para sucesso e erro

### README Atualizado
- **Seção de testes** com instruções específicas para Testcontainers
- **Comandos PowerShell** para todos os endpoints
- **Instruções de execução** de testes

## Vantagens da Implementação

### Testcontainers vs H2
- **Ambiente Real**: PostgreSQL real vs H2 em memória
- **Isolamento**: Container isolado por execução
- **Reprodutibilidade**: Funciona em qualquer ambiente
- **Limpeza Automática**: Containers removidos automaticamente
- **Configuração Dinâmica**: URLs configuradas automaticamente

### Cobertura de Testes
- **10 cenários de teste** cobrindo todos os endpoints
- **Validação de email** com casos válidos e inválidos
- **Tratamento de erros** para casos não encontrados
- **Testes ordenados** garantindo sequência lógica
- **Assertions robustas** com verificações detalhadas

## Como validar

### Executar testes com Testcontainers:
```bash
# Todos os testes
mvn test

# Apenas testes de integração
mvn test -Dtest=AlunoIntegrationTest

# Com output detalhado
mvn test -Dtest=AlunoIntegrationTest -X
```

### Verificar containers:
```bash
# Listar containers em execução
docker ps

# Ver logs do container de teste
docker logs <container_id>
```

### Relatórios:
- **Surefire**: `target/surefire-reports/`
- **Logs**: Console output com detalhes do Testcontainers
- **Container logs**: Logs do PostgreSQL em tempo real

## Requisitos Técnicos

- **Docker** instalado e rodando
- **Java 17+** (compatível com Testcontainers)
- **Maven 3.6+** para execução dos testes
- **PostgreSQL 15** (baixado automaticamente via Docker)

# Changelog

Data: 2025-10-13

## Resumo

- **Implementação completa de Testcontainers** para testes de integração com PostgreSQL real em containers Docker.
- **Instalação do VCR educativo para ViaCEP** permitindo gravar e reproduzir interações HTTP em “cassetes” reutilizáveis.
- **Guia de instalação do Apache Maven no PowerShell** com Java 21 e instalação via Scoop para ambientes Windows.
- **Substituição de H2 por PostgreSQL** nos testes, garantindo ambiente idêntico à produção.
- **Cobertura completa de testes CRUD** com 10 cenários ordenados cobrindo todos os endpoints da API.
- **Documentação técnica detalhada** sobre Testcontainers e suas vantagens.

## Guia Rápido: Instalação do Maven com Scoop (Windows)

1. **Validar pré-requisitos**
   ```powershell
   java -version
   echo $env:JAVA_HOME
   ```
2. **Permitir execução de scripts no PowerShell**
   ```powershell
   Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
   ```
3. **Instalar o Scoop**
   ```powershell
   irm get.scoop.sh | iex
   ```
4. **(Opcional) Garantir que o bucket principal esteja disponível**
   ```powershell
   scoop bucket add main
   ```
5. **Instalar o Apache Maven e validar**
   ```powershell
   scoop install maven
   mvn -v
   ```

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

### VCR para Testes de API
- **Criado**: pacote `com.example.studentregistration.vcr` com `VCRService`, `VCRInterceptor`, `VCRViaCEPService` e `VCRController` para orquestrar gravação e reprodução de interações HTTP.
- **Endpoint educativo**: `GET /api/v1/vcr/cep/{cep}?recordMode={boolean}&cassette={nome}` que grava chamadas reais ao ViaCEP quando `recordMode=true` e reproduz respostas salvas quando `recordMode=false`.
- **Armazenamento dos cassetes**: interações persistidas em JSON no diretório `src/test/resources/vcr_cassettes/`, permitindo reuso determinístico nos testes.
- **Testes automatizados**: `src/test/java/com/example/studentregistration/vcr/VCRViaCEPServiceTest.java` garante o fluxo completo de gravação e reprodução.

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
- **Criado**: `vcr_teaching_guide.md` com guia prático sobre instalação, gravação, reprodução e localização dos cassetes VCR.
- **Adicionado**: guia “Instalação do Apache Maven no PowerShell (Windows)” detalhando pré-requisitos com Java 21, configuração do `JAVA_HOME` e instalação do Maven via Scoop (`Set-ExecutionPolicy`, `irm get.scoop.sh | iex`, `scoop install maven`, `mvn -v`).

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

### Por que migrar do H2 para Testcontainers?

#### **Problemas do H2 em Testes de Integração**
- **Diferenças de Comportamento**: H2 é um banco em memória que pode ter comportamentos diferentes do PostgreSQL em produção
- **SQL Incompatível**: Algumas funcionalidades SQL específicas do PostgreSQL não funcionam no H2
- **Limitações de Recursos**: H2 não simula restrições de memória, conexões e performance de um banco real
- **Falsos Positivos**: Testes podem passar no H2 mas falhar em produção com PostgreSQL
- **Configurações Diferentes**: Hibernate pode se comportar diferente entre H2 e PostgreSQL

#### **Vantagens dos Testcontainers**
- **Ambiente Real**: PostgreSQL real vs H2 em memória
- **Isolamento**: Container isolado por execução
- **Reprodutibilidade**: Funciona em qualquer ambiente
- **Limpeza Automática**: Containers removidos automaticamente
- **Configuração Dinâmica**: URLs configuradas automaticamente
- **Compatibilidade Total**: Mesmo banco usado em produção
- **Testes Mais Confiáveis**: Reduz falsos positivos e negativos

### Por que não usar Mocks em Testes de Integração?

#### **Limitações dos Mocks**
- **Não Testam Integração Real**: Mocks simulam comportamento, não testam a integração real entre componentes
- **Manutenção Complexa**: Mocks precisam ser atualizados sempre que a API muda
- **Falsos Positivos**: Testes com mocks podem passar mesmo com bugs reais
- **Não Detectam Problemas de Performance**: Mocks não simulam latência, timeouts, ou problemas de conexão
- **Não Testam Transações**: Mocks não testam rollbacks, commits, ou isolamento de transações
- **Não Validam SQL**: Mocks não executam queries reais, não detectam problemas de SQL
- **Não Testam Constraints**: Mocks não validam foreign keys, unique constraints, ou triggers

#### **Vantagens dos Testcontainers sobre Mocks**
- **Testes Reais**: Executam operações reais no banco de dados
- **Detecção de Problemas**: Identificam problemas de SQL, performance e integração
- **Validação Completa**: Testam constraints, transações e relacionamentos
- **Menos Manutenção**: Não precisam ser atualizados quando a API muda
- **Confiança Maior**: Testes que passam com Testcontainers têm alta probabilidade de funcionar em produção

### Evolução das Práticas de Teste

#### **Era dos Mocks (2000-2015)**
- **Foco**: Testes unitários rápidos com mocks
- **Problema**: Falsos positivos, não testavam integração real
- **Resultado**: Bugs em produção mesmo com testes "passando"

#### **Era do H2 (2010-2020)**
- **Foco**: Testes de integração com banco em memória
- **Problema**: Diferenças de comportamento entre H2 e PostgreSQL
- **Resultado**: Incompatibilidades e bugs específicos de banco

#### **Era dos Testcontainers (2015-presente)**
- **Foco**: Testes de integração com ambiente real
- **Vantagem**: Mesmo ambiente de produção, containers isolados
- **Resultado**: Testes mais confiáveis, menos bugs em produção

### Por que Testcontainers é o Padrão Moderno?

#### **Tendências Atuais**
- **DevOps e CI/CD**: Containers são padrão em pipelines
- **Microserviços**: Cada serviço tem seu próprio banco
- **Cloud Native**: Aplicações rodam em containers
- **Infrastructure as Code**: Infraestrutura versionada e reproduzível

#### **Benefícios para Equipes**
- **Desenvolvedores**: Testes funcionam em qualquer máquina
- **DevOps**: Mesma infraestrutura em dev, test e prod
- **QA**: Testes mais realistas e confiáveis
- **Produto**: Menos bugs em produção, maior qualidade

### Cobertura de Testes
- **10 cenários de teste** cobrindo todos os endpoints
- **Validação de email** com casos válidos e inválidos
- **Tratamento de erros** para casos não encontrados
- **Testes ordenados** garantindo sequência lógica
- **Assertions robustas** com verificações detalhadas

### Exemplos Práticos de Problemas Resolvidos

#### **Problema 1: SQL Específico do PostgreSQL**
```sql
-- Funciona no PostgreSQL, falha no H2
SELECT * FROM aluno WHERE email ILIKE '%senac%';
```
- **H2**: Não suporta `ILIKE`
- **Testcontainers**: Testa com PostgreSQL real

#### **Problema 2: Transações e Constraints**
```java
// Teste que falha com mocks, funciona com Testcontainers
@Test
void testConstraintViolation() {
    // Tenta inserir email duplicado
    // Mock: Sempre passa
    // Testcontainers: Falha corretamente com constraint violation
}
```

#### **Problema 3: Performance e Timeouts**
```java
// Teste de timeout que só funciona com banco real
@Test
void testSlowQuery() {
    // Mock: Retorna instantaneamente
    // Testcontainers: Simula latência real
}
```

#### **Problema 4: Configurações de Hibernate**
```properties
# Configurações que se comportam diferente entre H2 e PostgreSQL
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

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

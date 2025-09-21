# Changelog — branch `tests/novos-testes-parametrizados`

Data: 2025-09-21

## Resumo

- 1 commit à frente de `main` (9df4074).
- 4 arquivos alterados, 71 inserções e 1 deleção.

## Mudanças

- Serviço: adiciona `AlunoService.updateAluno(Long id, Aluno alunoDetails)` com validações:
  - exige e-mail diferente do atual;
  - valida que o e-mail contém `@senac`;
  - lança `IllegalArgumentException` em casos inválidos.
- Testes: adiciona teste parametrizado em `AlunoServiceTest` para atualização de e-mail (válido, igual ao atual e domínio inválido) usando `@ParameterizedTest` + `@CsvSource`.
- Build: altera `pom.xml` para Java 17 e configura o `maven-compiler-plugin` (source/target/testSource/testTarget = 17). Remove o uso do `maven.compiler.release` herdado.
- Outros: adiciona `effective-pom.xml` (arquivo vazio) ao repositório.

## Arquivos afetados

- A `effective-pom.xml`
- M `pom.xml`
- M `src/main/java/com/example/studentregistration/service/AlunoService.java`
- M `src/test/java/com/example/studentregistration/service/AlunoServiceTest.java`

## Commits

- 9df4074: test: novo test parametizado

# aula

Objetivo da aula: entender o que é teste parametrizado no JUnit 5 e como ele está sendo usado neste projeto para validar a atualização de e‑mail de Aluno.

O que é teste parametrizado?
- É uma técnica para executar o mesmo método de teste várias vezes com combinações diferentes de entradas e expectativas.
- Benefícios: reduz duplicação de código, aumenta cobertura de cenários e deixa explícito o que varia entre casos.
- No JUnit 5 usamos `@ParameterizedTest` e uma fonte de dados, como `@CsvSource`, `@MethodSource`, etc.

Como funciona no projeto
- Biblioteca: JUnit 5 com `@ParameterizedTest` e `@CsvSource` em `AlunoServiceTest`.
- Foco do teste: método `AlunoService.updateAluno(Long id, Aluno alunoDetails)` que valida e atualiza e‑mail.
- Casos cobertos via CSV (colunas → parâmetros do teste):
  1) `newEmail` (entrada)
  2) `expectedEmail` (resultado quando é atualização válida)
  3) `expectedExceptionMessage` (mensagem esperada quando deve falhar)
- Exemplos usados:
  - `jane.doe.updated@senac.com, jane.doe.updated@senac.com,` → atualização válida (sem exceção).
  - `jane.doe@senac.com, , O novo e-mail deve ser diferente do e-mail atual.` → rejeita e‑mail igual ao atual.
  - `john.doe@gmail.com, , Email inválido. O email deve conter '@senac'.` → rejeita e‑mail sem domínio `@senac`.

Fluxo do teste parametrizado
- Arrange: cria um `Aluno` existente com e‑mail atual e configura o `AlunoRepository` mockado para:
  - `findById(id)` retornar o aluno existente;
  - `save(...)` retornar o mesmo aluno.
- Act & Assert:
  - Se `expectedExceptionMessage` estiver preenchida, usa `assertThrows` e compara a mensagem.
  - Caso contrário, chama `updateAluno(...)` e valida com `assertEquals` que o e‑mail foi atualizado para `expectedEmail`.

Outros testes no arquivo
- `testCreateAluno`: verifica criação simples de aluno com mock do `save` e checagem de atributos básicos.

Ferramentas e anotações importantes
- `@InjectMocks` injeta o `AlunoService` com dependências mockadas.
- `@Mock` cria o mock de `AlunoRepository`.
- `@BeforeEach` com `MockitoAnnotations.openMocks(this)` inicializa as anotações do Mockito a cada teste.
- `assertThrows`/`assertEquals`: asserções do JUnit 5.

Como adicionar novos cenários rapidamente
- Acrescente uma nova linha em `@CsvSource`, por exemplo:
  - `"novo.email@senac.com.br, novo.email@senac.com.br,"` para outro caso válido.
  - `"invalido@outrodominio.com, , Email inválido. O email deve conter '@senac'."` para outro caso inválido.

Como executar os testes
- Requisitos de build: Java 17 (configurado no `pom.xml`).
- Comando: `mvn test` na raiz do projeto.

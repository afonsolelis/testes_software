# Changelog

Data: 2025-09-22

## Resumo

- Adicionados testes de integração (MockMvc) em caixa‑preta para `POST /api/v1/alunos`, cobrindo Partição de Equivalência e Análise de Valor Limite do campo `email`.
- Configuração de banco H2 exclusiva para testes, isolando o Postgres definido no ambiente principal.
- Todos os testes passam na suíte (`mvn test`).

## Mudanças

- Testes (Controller): criado `src/test/java/com/example/studentregistration/controller/AlunoControllerTest.java` com cenários:
  - Válidos: "@senac" (caso mínimo), "x@SeNaC.com" (variação de caixa).
  - Inválidos: "john@gmail.com", "senac@x.com" (ordem invertida), `null`, `""`.
  - Verificação de respostas 200/400 e mensagem: "Email inválido. O email deve conter '@senac'.".
- Configuração (Test): adicionado `src/test/resources/application.properties` com H2 em memória (`create-drop`), garantindo que os testes não utilizem o Postgres externo.

## Como validar

- Executar: `mvn test`
- Relatórios: `target/surefire-reports/` (arquivos `*.txt` e `*.xml`).

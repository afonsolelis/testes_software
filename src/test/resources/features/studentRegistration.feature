Feature: Student Registration
  As a system administrator
  I want to register new students in the system
  So that I can manage student information

  Background:
    Given the student registration system is available
    And the database is ready to accept new registrations

  # Cenário de sucesso: validação de registro completo
  # Padrão AAA (Arrange-Act-Assert):
  # - Arrange: Prepara dados válidos do estudante
  # - Act: Submete o formulário de registro
  # - Assert: Verifica sucesso e retorno dos dados
  Scenario: Successfully register a new student with valid institutional email
    Given I have a student with the following details:
      | name  | João Silva           |
      | email | joao.silva@senac.br  |
      | age   | 20                   |
    And the email contains the required "@senac" domain
    When I submit the student registration form to the API endpoint "/api/v1/alunos"
    Then the registration should be successful with HTTP status code 201
    And the system should return the registered student information
    And the response should contain a generated student ID
    And the response should include the student name "João Silva"
    And the response should include the student email "joao.silva@senac.br"
    And the response should include the student age 20

  # Cenário de falha: validação de domínio de email
  # Padrão AAA:
  # - Arrange: Prepara dados com email inválido (sem @senac)
  # - Act: Tenta submeter o formulário
  # - Assert: Verifica falha e mensagem de erro específica
  Scenario: Reject registration when email does not contain institutional domain
    Given I have a student with the following details:
      | name  | Maria Santos        |
      | email | maria@gmail.com     |
      | age   | 22                  |
    And the email does not contain the required "@senac" domain
    When I submit the student registration form to the API endpoint "/api/v1/alunos"
    Then the registration should fail with HTTP status code 400
    And the system should return an error message "Email inválido. O email deve conter '@senac'."
    And no student record should be created in the database

  # Cenário de falha: validação de campo obrigatório
  # Padrão AAA:
  # - Arrange: Prepara dados com email vazio
  # - Act: Tenta submeter o formulário
  # - Assert: Verifica falha e mensagem de validação
  Scenario: Reject registration when email field is empty
    Given I have a student with the following details:
      | name  | Pedro Costa |
      | email |             |
      | age   | 19          |
    And the email field is empty or null
    When I submit the student registration form to the API endpoint "/api/v1/alunos"
    Then the registration should fail with HTTP status code 400
    And the system should return an error message "Email não pode ser vazio."
    And no student record should be created in the database

  # Cenário adicional: validação de múltiplos campos
  Scenario: Reject registration when student name is empty
    Given I have a student with the following details:
      | name  |                      |
      | email | teste@senac.br       |
      | age   | 21                   |
    And the name field is empty or null
    When I submit the student registration form to the API endpoint "/api/v1/alunos"
    Then the registration should fail with HTTP status code 400
    And the system should return a validation error for the name field
    And no student record should be created in the database

  # Cenário de validação de idade
  Scenario: Successfully register a student with minimum valid age
    Given I have a student with the following details:
      | name  | Ana Paula            |
      | email | ana.paula@senac.br   |
      | age   | 16                   |
    And the age is within the valid range for enrollment
    When I submit the student registration form to the API endpoint "/api/v1/alunos"
    Then the registration should be successful with HTTP status code 201
    And the system should return the registered student information

  # Cenário de borda: idade negativa
  Scenario: Reject registration when age is negative
    Given I have a student with the following details:
      | name  | Carlos Silva         |
      | email | carlos@senac.br      |
      | age   | -5                   |
    And the age is invalid (negative value)
    When I submit the student registration form to the API endpoint "/api/v1/alunos"
    Then the registration should fail with HTTP status code 400
    And the system should return a validation error for the age field
    And no student record should be created in the database

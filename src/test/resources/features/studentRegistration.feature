Feature: Student Registration
  As a system administrator
  I want to register new students in the system
  So that I can manage student information

  Scenario: Successfully register a new student
    Given I have student details with valid information
    When I submit the student registration form
    Then the student should be successfully registered
    And the system should return the registered student information

  Scenario: Register a student with invalid email
    Given I have student details with email not containing "@senac"
    When I submit the student registration form
    Then the registration should fail
    And the system should return an error message "Email inválido. O email deve conter '@senac'."

  Scenario: Register a student with empty email
    Given I have student details with empty email
    When I submit the student registration form
    Then the registration should fail
    And the system should return an error message "Email não pode ser vazio."
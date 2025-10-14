package com.example.studentregistration.stepdefinitions;

import com.example.studentregistration.model.Aluno;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=true",
    "spring.datasource.driver-class-name=org.postgresql.Driver"
})
public class StudentRegistrationStepDefinitions {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private Aluno aluno;
    private ResponseEntity<String> response;
    private String baseUrl;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Given("I have student details with valid information")
    public void i_have_student_details_with_valid_information() {
        aluno = new Aluno("Maria Oliveira", 22, "maria@senac.com");
        baseUrl = "http://localhost:" + port + "/api/v1";
    }

    @Given("I have student details with email not containing \"@senac\"")
    public void i_have_student_details_with_email_not_containing_senac() {
        aluno = new Aluno("Pedro Santos", 25, "pedro@gmail.com");
        baseUrl = "http://localhost:" + port + "/api/v1";
    }

    @Given("I have student details with empty email")
    public void i_have_student_details_with_empty_email() {
        aluno = new Aluno("Ana Costa", 20, "");
        baseUrl = "http://localhost:" + port + "/api/v1";
    }

    @When("I submit the student registration form")
    public void i_submit_the_student_registration_form() {
        response = restTemplate.postForEntity(
            baseUrl + "/alunos", 
            aluno, 
            String.class
        );
    }

    @Then("the student should be successfully registered")
    public void the_student_should_be_successfully_registered() {
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Then("the system should return the registered student information")
    public void the_system_should_return_the_registered_student_information() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("maria@senac.com"));
    }

    @Then("the registration should fail")
    public void the_registration_should_fail() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Then("the system should return an error message {string}")
    public void the_system_should_return_an_error_message(String expectedMessage) {
        assertTrue(response.getBody().contains(expectedMessage));
    }
}
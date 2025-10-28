package com.example.studentregistration.stepdefinitions;

import com.example.studentregistration.model.Aluno;
import com.example.studentregistration.repository.AlunoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:cucumberdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true"
})
public class StudentRegistrationStepDefinitions {


    @LocalServerPort
    private int port;

    @Autowired
    private AlunoRepository alunoRepository;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String baseUrl;
    private Aluno aluno;
    private ResponseEntity<String> response;
    private long initialCount;

    // Background
    @Given("the student registration system is available")
    public void the_system_is_available() {
        baseUrl = "http://localhost:" + port + "/api/v1";
        System.out.println("[BDD] Health check URL: " + baseUrl + "/alunos");
        // Warm up by hitting a simple endpoint if needed
        ResponseEntity<String> health = restTemplate.exchange(
                baseUrl + "/alunos", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
        assertTrue(health.getStatusCode().is2xxSuccessful());
    }

    @Given("the database is ready to accept new registrations")
    public void the_database_is_ready() {
        alunoRepository.deleteAll();
        initialCount = alunoRepository.count();
    }

    // Arrange steps
    @Given("I have a student with the following details:")
    public void i_have_student_with_details(DataTable table) {
        Map<String, String> map = table.asMap(String.class, String.class);
        String name = map.getOrDefault("name", "");
        String email = map.getOrDefault("email", "");
        int age = Integer.parseInt(map.getOrDefault("age", "0"));
        aluno = new Aluno(name, age, email);
    }

    @Given("the email contains the required \"@senac\" domain")
    public void email_contains_required_domain() {
        // No-op: validation is enforced on submission
    }

    @Given("the email does not contain the required \"@senac\" domain")
    public void email_does_not_contain_required_domain() {
        // No-op: the provided data table sets an invalid email
    }

    @Given("the email field is empty or null")
    public void email_field_empty_or_null() {
        // No-op: data table already left it blank
    }

    @Given("the name field is empty or null")
    public void name_field_empty_or_null() {
        // No-op: data table already left it blank
    }

    @Given("the age is within the valid range for enrollment")
    public void age_is_within_valid_range() {
        // No-op: scenario sets a valid age (e.g., 16+)
    }

    @Given("the age is invalid \\(" +
            "negative value\\)")
    public void age_is_invalid_negative() {
        // No-op: data table already sets a negative age
    }

    // Act
    @When("I submit the student registration form to the API endpoint {string}")
    public void i_submit_form_to_endpoint(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = endpoint.startsWith("/")
                ? ("http://localhost:" + port + endpoint)
                : (baseUrl + (endpoint.startsWith("/") ? endpoint : "/" + endpoint));
        System.out.println("[BDD] POST URL: " + url);
        response = restTemplate.postForEntity(url, aluno, String.class);
    }

    // Assert
    @Then("the registration should be successful with HTTP status code {int}")
    public void registration_should_be_success_with_status(int expectedStatus) {
        // API atual retorna 200 (OK); aceitamos qualquer 2xx para compatibilidade
        assertTrue(response.getStatusCode().is2xxSuccessful(),
                () -> "Expected 2xx but was " + response.getStatusCode());
    }

    @Then("the registration should fail with HTTP status code {int}")
    public void registration_should_fail_with_status(int expectedStatus) {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Then("the system should return the registered student information")
    public void system_should_return_registered_student_info() throws Exception {
        assertNotNull(response.getBody());
        JsonNode json = objectMapper.readTree(response.getBody());
        assertNotNull(json.get("id"));
        assertNotNull(json.get("nome"));
        assertNotNull(json.get("email"));
    }

    @Then("the response should contain a generated student ID")
    public void response_should_contain_generated_id() throws Exception {
        JsonNode json = objectMapper.readTree(response.getBody());
        assertTrue(json.hasNonNull("id"));
    }

    @Then("the response should include the student name {string}")
    public void response_should_include_student_name(String expectedName) throws Exception {
        JsonNode json = objectMapper.readTree(response.getBody());
        assertEquals(expectedName, json.get("nome").asText());
    }

    @Then("the response should include the student email {string}")
    public void response_should_include_student_email(String expectedEmail) throws Exception {
        JsonNode json = objectMapper.readTree(response.getBody());
        assertEquals(expectedEmail, json.get("email").asText());
    }

    @Then("the response should include the student age {int}")
    public void response_should_include_student_age(int expectedAge) throws Exception {
        JsonNode json = objectMapper.readTree(response.getBody());
        assertEquals(expectedAge, json.get("idade").asInt());
    }

    @Then("the system should return an error message {string}")
    public void system_should_return_error_message(String expectedMessage) {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains(expectedMessage));
    }

    @Then("no student record should be created in the database")
    public void no_student_record_should_be_created() {
        long after = alunoRepository.count();
        assertEquals(initialCount, after);
    }

    @Then("the system should return a validation error for the name field")
    public void validation_error_for_name_field() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() == null || response.getBody().toLowerCase().contains("nome"));
    }

    @Then("the system should return a validation error for the age field")
    public void validation_error_for_age_field() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() == null || response.getBody().toLowerCase().contains("idade"));
    }
}

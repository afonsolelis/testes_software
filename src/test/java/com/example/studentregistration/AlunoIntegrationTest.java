package com.example.studentregistration;

import com.example.studentregistration.model.Aluno;
import org.junit.jupiter.api.*;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=true"
})
class AlunoIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    @Order(1)
    void testCreateAluno() {
        // Given
        Aluno aluno = new Aluno("João Silva", 25, "joao@senac.com");
        
        // When
        ResponseEntity<Aluno> response = restTemplate.postForEntity(
            getBaseUrl() + "/alunos", aluno, Aluno.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Aluno createdAluno = response.getBody();
        assertThat(createdAluno.getNome()).isEqualTo("João Silva");
        assertThat(createdAluno.getEmail()).isEqualTo("joao@senac.com");
        assertThat(createdAluno.getId()).isNotNull();
    }

    @Test
    @Order(2)
    void testCreateAlunoWithInvalidEmail() {
        // Given
        Aluno aluno = new Aluno("Maria Silva", 22, "maria@email.com");
        
        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl() + "/alunos", aluno, String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email inválido");
    }

    @Test
    @Order(3)
    void testGetAllAlunos() {
        // When
        ResponseEntity<Aluno[]> response = restTemplate.getForEntity(
            getBaseUrl() + "/alunos", Aluno[].class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Aluno[] alunos = response.getBody();
        assertThat(alunos).hasSize(1);
        assertThat(alunos[0].getNome()).isEqualTo("João Silva");
    }

    @Test
    @Order(4)
    void testUpdateAluno() {
        // Given
        Aluno alunoAtualizado = new Aluno("João Silva Atualizado", 26, "joao.atualizado@senac.com");
        
        // When
        ResponseEntity<Aluno> response = restTemplate.exchange(
            getBaseUrl() + "/alunos/1", HttpMethod.PUT, 
            new HttpEntity<>(alunoAtualizado), Aluno.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Aluno updatedAluno = response.getBody();
        assertThat(updatedAluno.getNome()).isEqualTo("João Silva Atualizado");
        assertThat(updatedAluno.getIdade()).isEqualTo(26);
    }

    @Test
    @Order(5)
    void testUpdateAlunoWithInvalidEmail() {
        // Given
        Aluno alunoInvalido = new Aluno("João Silva", 26, "joao@email.com");
        
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            getBaseUrl() + "/alunos/1", HttpMethod.PUT, 
            new HttpEntity<>(alunoInvalido), String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email inválido");
    }

    @Test
    @Order(6)
    void testUpdateNonExistentAluno() {
        // Given
        Aluno aluno = new Aluno("Aluno Inexistente", 30, "inexistente@senac.com");
        
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            getBaseUrl() + "/alunos/999", HttpMethod.PUT, 
            new HttpEntity<>(aluno), String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Aluno não encontrado");
    }

    @Test
    @Order(7)
    void testDeleteAluno() {
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            getBaseUrl() + "/alunos/1", HttpMethod.DELETE, 
            new HttpEntity<>(new Object()), String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Aluno deletado com sucesso");
    }

    @Test
    @Order(8)
    void testDeleteNonExistentAluno() {
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            getBaseUrl() + "/alunos/999", HttpMethod.DELETE, 
            new HttpEntity<>(new Object()), String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Aluno não encontrado");
    }

    @Test
    @Order(9)
    void testGetAllAlunosAfterDeletion() {
        // When
        ResponseEntity<Aluno[]> response = restTemplate.getForEntity(
            getBaseUrl() + "/alunos", Aluno[].class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(0);
    }

    @Test
    @Order(10)
    void testCreateMultipleAlunos() {
        // Given
        Aluno aluno1 = new Aluno("Ana Silva", 23, "ana@senac.com");
        Aluno aluno2 = new Aluno("Carlos Santos", 28, "carlos@senac.com");
        
        // When
        ResponseEntity<Aluno> response1 = restTemplate.postForEntity(
            getBaseUrl() + "/alunos", aluno1, Aluno.class);
        ResponseEntity<Aluno> response2 = restTemplate.postForEntity(
            getBaseUrl() + "/alunos", aluno2, Aluno.class);
        
        // Then
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Verify both students were created
        ResponseEntity<Aluno[]> allResponse = restTemplate.getForEntity(
            getBaseUrl() + "/alunos", Aluno[].class);
        assertThat(allResponse.getBody()).isNotNull();
        assertThat(allResponse.getBody()).hasSize(2);
    }
}

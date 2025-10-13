package com.example.studentregistration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String alunoJson(String nome, int idade, String emailLiteral) {
        String emailField = emailLiteral == null ? "null" : ("\"" + emailLiteral + "\"");
        return "{"
                + "\"nome\":\"" + nome + "\"," 
                + "\"idade\":" + idade + ","
                + "\"email\":" + emailField
                + "}";
    }

    @Test
    @DisplayName("POST /alunos - sucesso com '@senac' mínimo (AVL)")
    void postAluno_sucesso_minimoSenac() throws Exception {
        String body = alunoJson("Fulano", 20, "@senac");

        mockMvc.perform(post("/api/v1/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("@senac"));
    }

    @Test
    @DisplayName("POST /alunos - sucesso com variação de caixa (equivalência válida)")
    void postAluno_sucesso_variaçãoCaixa() throws Exception {
        String body = alunoJson("Fulano", 20, "x@SeNaC.com");

        mockMvc.perform(post("/api/v1/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("x@SeNaC.com"));
    }

    @Test
    @DisplayName("POST /alunos - erro: email comum (equivalência inválida)")
    void postAluno_erro_emailComum() throws Exception {
        String body = alunoJson("Fulano", 20, "john@gmail.com");

        mockMvc.perform(post("/api/v1/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email inválido. O email deve conter '@senac'."));
    }

    @Test
    @DisplayName("POST /alunos - erro: ordem invertida 'senac@' (AVL inválido)")
    void postAluno_erro_ordemInvertida() throws Exception {
        String body = alunoJson("Fulano", 20, "senac@x.com");

        mockMvc.perform(post("/api/v1/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email inválido. O email deve conter '@senac'."));
    }

    @Test
    @DisplayName("POST /alunos - erro: email null (equivalência inválida)")
    void postAluno_erro_emailNull() throws Exception {
        String body = alunoJson("Fulano", 20, null);

        mockMvc.perform(post("/api/v1/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email não pode ser vazio."));
    }

    @Test
    @DisplayName("POST /alunos - erro: email vazio (equivalência inválida)")
    void postAluno_erro_emailVazio() throws Exception {
        String body = alunoJson("Fulano", 20, "");

        mockMvc.perform(post("/api/v1/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email não pode ser vazio."));
    }
}

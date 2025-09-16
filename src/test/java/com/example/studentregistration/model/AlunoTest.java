package com.example.studentregistration.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlunoTest {

    @Test
    public void testNewAluno() {
        Aluno aluno = new Aluno("John Doe", 25, "john.doe@example.com");
        assertEquals("John Doe", aluno.getNome());
        assertEquals(25, aluno.getIdade());
        assertEquals("john.doe@example.com", aluno.getEmail());
    }
}

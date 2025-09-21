package com.example.studentregistration.service;

import com.example.studentregistration.model.Aluno;
import com.example.studentregistration.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AlunoServiceTest {

    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAluno() {
        Aluno aluno = new Aluno("John Doe", 25, "john.doe@senac.com");
        when(alunoRepository.save(aluno)).thenReturn(aluno);

        Aluno created = alunoService.createAluno(aluno);

        assertEquals(aluno.getNome(), created.getNome());
    }

    @ParameterizedTest
    @CsvSource({
        "jane.doe.updated@senac.com, jane.doe.updated@senac.com,",
        "jane.doe@senac.com, , O novo e-mail deve ser diferente do e-mail atual.",
        "john.doe@gmail.com, , Email inválido. O email deve conter '@senac'."
    })
    public void testUpdateAlunoEmail(String newEmail, String expectedEmail, String expectedExceptionMessage) {
        Long alunoId = 1L;
        Aluno existingAluno = new Aluno("John Doe", 25, "jane.doe@senac.com");
        existingAluno.setId(alunoId);

        Aluno alunoDetails = new Aluno("John Doe", 25, newEmail);

        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(existingAluno));
        when(alunoRepository.save(existingAluno)).thenReturn(existingAluno);

        if (expectedExceptionMessage != null && !expectedExceptionMessage.isEmpty()) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                alunoService.updateAluno(alunoId, alunoDetails);
            });
            assertEquals(expectedExceptionMessage, exception.getMessage());
        } else {
            Aluno updatedAluno = alunoService.updateAluno(alunoId, alunoDetails);
            assertEquals(expectedEmail, updatedAluno.getEmail());
        }
    }
}

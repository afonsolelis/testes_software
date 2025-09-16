package com.example.studentregistration.service;

import com.example.studentregistration.model.Aluno;
import com.example.studentregistration.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}

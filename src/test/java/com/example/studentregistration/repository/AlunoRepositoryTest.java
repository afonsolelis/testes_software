package com.example.studentregistration.repository;

import com.example.studentregistration.model.Aluno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AlunoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    public void testFindByEmail() {
        Aluno aluno = new Aluno("John Doe", 25, "john.doe@example.com");
        entityManager.persist(aluno);
        entityManager.flush();

        Aluno found = alunoRepository.findByEmail(aluno.getEmail());

        assertEquals(aluno.getNome(), found.getNome());
    }
}

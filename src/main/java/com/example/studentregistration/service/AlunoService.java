package com.example.studentregistration.service;

import com.example.studentregistration.model.Aluno;
import com.example.studentregistration.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Aluno createAluno(Aluno aluno) {
        if (aluno.getEmail() == null || !aluno.getEmail().toLowerCase().contains("@senac")) {
            throw new IllegalArgumentException("Email inválido. O email deve conter '@senac'.");
        }
        return alunoRepository.save(aluno);
    }

    public List<Aluno> getAllAlunos() {
        return alunoRepository.findAll();
    }
}

package com.example.studentregistration.service;

import com.example.studentregistration.model.Aluno;
import com.example.studentregistration.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Aluno createAluno(Aluno aluno) {
        validateEmail(aluno.getEmail());
        return alunoRepository.save(aluno);
    }

    public List<Aluno> getAllAlunos() {
        return alunoRepository.findAll();
    }

    public Aluno updateAluno(Long id, Aluno alunoDetails) {
        Optional<Aluno> optionalAluno = alunoRepository.findById(id);
        if (!optionalAluno.isPresent()) {
            throw new IllegalArgumentException("Aluno não encontrado.");
        }

        Aluno aluno = optionalAluno.get();

        if (alunoDetails.getEmail().equals(aluno.getEmail())) {
            throw new IllegalArgumentException("O novo e-mail deve ser diferente do e-mail atual.");
        }

        validateEmail(alunoDetails.getEmail());

        aluno.setNome(alunoDetails.getNome());
        aluno.setIdade(alunoDetails.getIdade());
        aluno.setEmail(alunoDetails.getEmail());

        return alunoRepository.save(aluno);
    }

    public void deleteAluno(Long id) {
        Optional<Aluno> optionalAluno = alunoRepository.findById(id);
        if (!optionalAluno.isPresent()) {
            throw new IllegalArgumentException("Aluno não encontrado.");
        }
        alunoRepository.deleteById(id);
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio.");
        }
        if (!email.toLowerCase().contains("@senac")) {
            throw new IllegalArgumentException("Email inválido. O email deve conter '@senac'.");
        }
    }
}

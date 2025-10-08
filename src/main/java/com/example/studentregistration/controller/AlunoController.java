package com.example.studentregistration.controller;

import com.example.studentregistration.model.Aluno;
import com.example.studentregistration.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping("/alunos")
    public ResponseEntity<Object> createAluno(@RequestBody Aluno aluno) {
        try {
            Aluno novoAluno = alunoService.createAluno(aluno);
            return ResponseEntity.ok(novoAluno);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/alunos")
    public List<Aluno> getAllAlunos() {
        return alunoService.getAllAlunos();
    }

    @PutMapping("/alunos/{id}")
    public ResponseEntity<Object> updateAluno(@PathVariable Long id, @RequestBody Aluno aluno) {
        try {
            Aluno alunoAtualizado = alunoService.updateAluno(id, aluno);
            return ResponseEntity.ok(alunoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/alunos/{id}")
    public ResponseEntity<Object> deleteAluno(@PathVariable Long id) {
        try {
            alunoService.deleteAluno(id);
            return ResponseEntity.ok("Aluno deletado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
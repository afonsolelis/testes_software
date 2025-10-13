package com.example.studentregistration.controller;

import com.example.studentregistration.model.ViaCEPResponse;
import com.example.studentregistration.service.ViaCEPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cep")
public class CepController {

    @Autowired
    private ViaCEPService viaCEPService;

    @GetMapping("/{cep}")
    public ResponseEntity<ViaCEPResponse> buscarEnderecoPorCEP(@PathVariable String cep) {
        try {
            ViaCEPResponse response = viaCEPService.buscarEnderecoPorCEP(cep);
            if (response != null && response.getLogradouro() != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
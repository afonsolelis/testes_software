package com.example.studentregistration.controller;

import com.example.studentregistration.model.ViaCEPResponse;
import com.example.studentregistration.vcr.VCRViaCEPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vcr")
public class VCRController {

    @Autowired
    private VCRViaCEPService vcrViaCEPService;

    @GetMapping("/cep/{cep}")
    public ResponseEntity<ViaCEPResponse> buscarEnderecoPorCEPComVCR(
            @PathVariable String cep,
            @RequestParam(defaultValue = "false") boolean recordMode,
            @RequestParam(defaultValue = "viacep_default") String cassette) {
        
        try {
            ViaCEPResponse response = vcrViaCEPService.buscarEnderecoPorCEP(cep, cassette, recordMode);
            
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
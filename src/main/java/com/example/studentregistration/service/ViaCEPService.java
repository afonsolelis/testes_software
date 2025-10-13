package com.example.studentregistration.service;

import com.example.studentregistration.model.ViaCEPResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCEPService {

    private final RestTemplate restTemplate;

    public ViaCEPService() {
        this.restTemplate = new RestTemplate();
    }

    public ViaCEPResponse buscarEnderecoPorCEP(String cep) {
        // Remove any non-numeric characters from CEP
        String cepFormatado = cep.replaceAll("[^0-9]", "");
        
        if (cepFormatado.length() != 8) {
            throw new IllegalArgumentException("CEP inválido: deve conter 8 dígitos");
        }

        String url = "https://viacep.com.br/ws/" + cepFormatado + "/json/";
        
        try {
            ViaCEPResponse response = restTemplate.getForObject(url, ViaCEPResponse.class);
            
            // Ensure the cep field in the response contains only numbers
            if (response != null) {
                response.setCep(cepFormatado);
            }
            
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar CEP na API ViaCEP: " + e.getMessage(), e);
        }
    }
}
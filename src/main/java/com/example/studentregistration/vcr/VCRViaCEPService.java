package com.example.studentregistration.vcr;

import com.example.studentregistration.model.ViaCEPResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VCRViaCEPService {
    
    private final VCRService vcrService;
    private RestTemplate restTemplate;

    public VCRViaCEPService(VCRService vcrService) {
        this.vcrService = vcrService;
        this.restTemplate = new RestTemplate();
    }
    
    public ViaCEPResponse buscarEnderecoPorCEP(String cep, String cassetteName, boolean recordMode) {
        // Remove any non-numeric characters from CEP
        String cepFormatado = cep.replaceAll("[^0-9]", "");
        
        if (cepFormatado.length() != 8) {
            throw new IllegalArgumentException("CEP inválido: deve conter 8 dígitos");
        }

        String url = "https://viacep.com.br/ws/" + cepFormatado + "/json/";
        
        if (recordMode) {
            // Add the VCR interceptor in record mode
            VCRInterceptor interceptor = new VCRInterceptor(vcrService, cassetteName, true);
            restTemplate = new RestTemplate();
            restTemplate.getInterceptors().add(interceptor);
        } else {
            // Check if cassette exists, if not, we can't replay
            if (!vcrService.cassetteExists(cassetteName)) {
                throw new RuntimeException("Cassette " + cassetteName + " does not exist. Run in record mode first.");
            }
            
            // In replay mode, we'll just return the recorded response
            // For this implementation, we're creating a mock service for demonstration
            // In a real scenario, you'd want to have a more sophisticated approach
            try {
                VCRRecording recording = vcrService.loadCassette(cassetteName);
                // This is a simplified approach - in real implementation you'd match the request
                if (!recording.getInteractions().isEmpty()) {
                    String recordedResponse = recording.getInteractions().get(0).getResponseBody();
                    // Parse the JSON response to get ViaCEPResponse object
                    // Note: This is simplified - in practice you'd use an ObjectMapper
                    return parseViaCEPResponse(recordedResponse);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error loading recorded response", e);
            }
        }
        
        try {
            if (recordMode) {
                ResponseEntity<ViaCEPResponse> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    HttpEntity.EMPTY, 
                    ViaCEPResponse.class
                );
                
                ViaCEPResponse result = response.getBody();
                if (result != null) {
                    result.setCep(cepFormatado); // Ensure the cep field contains only numbers
                }
                
                return result;
            } else {
                // In replay mode, return from cassette
                return null; // Will be handled by the VCRReplayResponse
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar CEP na API ViaCEP: " + e.getMessage(), e);
        }
    }
    
    private ViaCEPResponse parseViaCEPResponse(String jsonResponse) {
        // Simplified parsing - in practice you'd use Jackson ObjectMapper
        ViaCEPResponse response = new ViaCEPResponse();
        
        // Extract data using simple string operations (not ideal, but works for demo)
        if (jsonResponse.contains("\"cep\"")) {
            int start = jsonResponse.indexOf("\"cep\":\"") + 7;
            int end = jsonResponse.indexOf("\"", start);
            if (start > 6 && end > start) {
                response.setCep(jsonResponse.substring(start, end));
            }
        }
        
        if (jsonResponse.contains("\"logradouro\"")) {
            int start = jsonResponse.indexOf("\"logradouro\":\"") + 13;
            int end = jsonResponse.indexOf("\"", start);
            if (start > 12 && end > start) {
                response.setLogradouro(jsonResponse.substring(start, end));
            }
        }
        
        if (jsonResponse.contains("\"bairro\"")) {
            int start = jsonResponse.indexOf("\"bairro\":\"") + 10;
            int end = jsonResponse.indexOf("\"", start);
            if (start > 9 && end > start) {
                response.setBairro(jsonResponse.substring(start, end));
            }
        }
        
        if (jsonResponse.contains("\"localidade\"")) {
            int start = jsonResponse.indexOf("\"localidade\":\"") + 14;
            int end = jsonResponse.indexOf("\"", start);
            if (start > 13 && end > start) {
                response.setLocalidade(jsonResponse.substring(start, end));
            }
        }
        
        if (jsonResponse.contains("\"uf\"")) {
            int start = jsonResponse.indexOf("\"uf\":\"") + 6;
            int end = jsonResponse.indexOf("\"", start);
            if (start > 5 && end > start) {
                response.setUf(jsonResponse.substring(start, end));
            }
        }
        
        return response;
    }
}
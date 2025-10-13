package com.example.studentregistration.vcr;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VCRInterceptor implements ClientHttpRequestInterceptor {
    
    private final VCRService vcrService;
    private final String cassetteName;
    private final boolean recordMode;
    
    public VCRInterceptor(VCRService vcrService, String cassetteName, boolean recordMode) {
        this.vcrService = vcrService;
        this.cassetteName = cassetteName;
        this.recordMode = recordMode;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, 
            byte[] body, 
            ClientHttpRequestExecution execution) throws IOException {
        
        String url = request.getURI().toString();
        String method = request.getMethod().name();
        String requestBody = new String(body, StandardCharsets.UTF_8);
        
        if (recordMode) {
            // In record mode, execute the real request and record the interaction
            ClientHttpResponse response = execution.execute(request, body);
            
            // Read the response body
            String responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            int statusCode = response.getStatusCode().value();
            
            // Create and save the interaction
            VCRInteraction interaction = new VCRInteraction(
                method, 
                url, 
                requestBody, 
                statusCode, 
                responseBody
            );
            
            try {
                VCRRecording recording = vcrService.loadCassette(cassetteName);
                recording.addInteraction(interaction);
                vcrService.saveCassette(cassetteName, recording.getInteractions());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save VCR cassette", e);
            }
            
            // Create a new response to return since we consumed the body
            return new VCRClientHttpResponse(response, responseBody);
        } else {
            // In replay mode, return the recorded response
            try {
                List<VCRInteraction> interactions = vcrService.getInteractionsForUrl(cassetteName, url, method);
                
                if (interactions.isEmpty()) {
                    throw new RuntimeException("No recorded interaction found for URL: " + url);
                }
                
                // Use the first matching interaction (simplified approach)
                VCRInteraction interaction = interactions.get(0);
                
                return new VCRReplayResponse(interaction);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load VCR cassette", e);
            }
        }
    }
}
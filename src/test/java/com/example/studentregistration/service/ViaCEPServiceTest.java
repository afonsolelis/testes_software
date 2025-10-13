package com.example.studentregistration.service;

import com.example.studentregistration.model.ViaCEPResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ViaCEPServiceTest {

    private ViaCEPService viaCEPService = new ViaCEPService();

    @Test
    void testBuscarEnderecoPorCEP() {
        // Given
        String cepValido = "01001000"; // CEP do centro de SP

        // When
        ViaCEPResponse response = viaCEPService.buscarEnderecoPorCEP(cepValido);

        // Then
        assertNotNull(response);
        assertEquals(cepValido, response.getCep());
        assertNotNull(response.getLogradouro());
        assertNotNull(response.getLocalidade());
        assertNotNull(response.getUf());
    }

    @Test
    void testBuscarEnderecoPorCEPInvalido() {
        // Given
        String cepInvalido = "1234567"; // Apenas 7 dígitos

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            viaCEPService.buscarEnderecoPorCEP(cepInvalido);
        });
    }

    @Test
    void testBuscarEnderecoPorCEPComMascara() {
        // Given
        String cepComMascara = "01001-000"; // CEP do centro de SP com máscara

        // When
        ViaCEPResponse response = viaCEPService.buscarEnderecoPorCEP(cepComMascara);

        // Then
        assertNotNull(response);
        assertEquals("01001000", response.getCep()); // Deve retornar o CEP sem máscara
        assertNotNull(response.getLogradouro());
        assertNotNull(response.getLocalidade());
        assertNotNull(response.getUf());
    }
}
# VCR (Video Cassette Recorder) para Ensino de Testes de API

## Introdução

O VCR (Video Cassette Recorder) é um conceito de testes de API onde você pode "gravar" interações reais com APIs externas e depois "reproduzir" essas interações em testes subsequentes, sem fazer chamadas reais à API externa.

## Componentes do VCR no Projeto

### 1. Classes Principais
- `VCRService`: Gerencia o armazenamento e recuperação de interações gravadas
- `VCRInterceptor`: Intercepta chamadas HTTP para gravar ou reproduzir interações
- `VCRController`: Fornece endpoints para demonstrar o funcionamento do VCR
- `VCRAnnotation`: Anotação para marcar testes que devem usar funcionalidade VCR

### 2. Funcionalidades
- **Modo Gravação**: Faz chamadas reais à API e salva as interações
- **Modo Reprodução**: Retorna respostas gravadas sem fazer chamadas reais
- **Armazenamento**: As interações são salvas em arquivos JSON no diretório `src/test/resources/vcr_cassettes/`

## Demonstração Prática

### Endpoint VCR para CEP
O projeto inclui um endpoint VCR que demonstra o conceito com a API do ViaCEP:

```
GET /api/v1/vcr/cep/{cep}?recordMode={boolean}&cassette={nome_do_cassete}
```

### Exemplos de Uso

1. **Gravar uma interação**:
   ```
   GET /api/v1/vcr/cep/01001000?recordMode=true&cassette=saopaulo_center
   ```

2. **Reproduzir uma interação**:
   ```
   GET /api/v1/vcr/cep/01001000?recordMode=false&cassette=saopaulo_center
   ```

### Benefícios Educacionais

1. **Compreensão de Chamadas HTTP**: Os alunos podem ver exatamente como são as requisições e respostas
2. **Testes Determinísticos**: Os testes não dependem da disponibilidade de APIs externas
3. **Desenvolvimento Offline**: É possível rodar testes sem conexão com a API externa
4. **Performance**: Testes mais rápidos, pois não fazem chamadas de rede reais
5. **Controle de Ambiente**: Consistência total entre execuções de testes

## Estrutura dos Cassetes

Os cassetes são armazenados em `src/test/resources/vcr_cassettes/` no formato JSON:
```json
{
  "interactions": [
    {
      "method": "GET",
      "url": "https://viacep.com.br/ws/01001000/json/",
      "requestBody": "",
      "responseStatus": 200,
      "responseBody": "{...}",
      "timestamp": "2025-10-13T17:30:00"
    }
  ]
}
```

## Exemplo de Teste com VCR

```java
@Test
void testVCRRecordAndPlayback() throws IOException {
    String cassetteName = "viacep_test";
    
    // Grava uma interação real
    ViaCEPResponse recordedResponse = vcrViaCEPService.buscarEnderecoPorCEP("01001000", cassetteName, true);
    
    // Verifica que foi gravada
    assertTrue(vcrService.cassetteExists(cassetteName));
    
    // Carrega a gravação
    VCRRecording recording = vcrService.loadCassette(cassetteName);
    assertFalse(recording.getInteractions().isEmpty());
}
```

## Conclusão

O VCR é uma ferramenta poderosa para o ensino de testes de API porque:
- Demonstra claramente como funcionam as interações HTTP
- Ensina boas práticas de testes determinísticos
- Permite testar com dados reais de APIs externas
- Proporciona um ambiente de aprendizado controlado
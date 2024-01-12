package br.com.gabriel.gestao_vagas.utils;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    
    // criando um método para transformar objeto em string
    public static String objectToJSON(Object obj) {
        try {
            // ObjectMapper converte um objeto
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(objectMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // criando um token 
    public static String generateToken(UUID idCompany, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        var expiresIn = Instant.now().plus(java.time.Duration.ofHours(2));

        var token = JWT.create().withIssuer("Javagas")
           .withExpiresAt(expiresIn) // duração máx de 2 horas do token
           .withSubject(idCompany.toString())
           .withClaim("roles", Arrays.asList("COMPANY"))
           .sign(algorithm);

        return token;
    }
}

package br.com.gabriel.gestao_vagas.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTProvider {

    @Value("${security.token.secret}")
    private String secretKey;
    
    public DecodedJWT validateToken(String token) {
        // replace vai substituir o primeiro parâmetro pelo segundo
        // replace do prefixo "Bearer" no token para obter apenas o token em si
        token = token.replace("Bearer ", "");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        // algoritmo de validação do JWT para verificar se o token é válido
        try {
            var tokenDecoded = JWT.require(algorithm)
                             .build()
                             .verify(token);
            return tokenDecoded;
        // Caso ocorra algum erro na validação, uma exceção é capturada e um campo vazio é retornado
        } catch (JWTVerificationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

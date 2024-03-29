package br.com.gabriel.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.gabriel.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.gabriel.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.gabriel.gestao_vagas.modules.candidate.repositories.CandidateRepository;

@Service
public class AuthCandidateUseCase {

    // criando o hash da senha criptografada
    @Value("${security.token.secret_candidate}")
    private String secretKey;
    
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
        // verificando se usuário já existe, caso não -> erro
        var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
            .orElseThrow(() -> {
                throw new UsernameNotFoundException("Username/password incorrect");
            });

        // validando a senha 
        var passwordMatches = this.passwordEncoder
        .matches(authCandidateRequestDTO.password(), candidate.getPassword());

        if(!passwordMatches) {
            throw new AuthenticationException();
        }

        // criando token do candidate
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofHours(2));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(candidate.getId().toString())
                .withClaim("roles", Arrays.asList("CANDIDATE"))
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        // retornando a resposta do token do candidate
        var authCandidateResponse = AuthCandidateResponseDTO.builder()
            .access_token(token)
            .expires_in(expiresIn.toEpochMilli()) 
            .build();
        // dentro de builder cria um objeto de retorno de resposta do controller

        return authCandidateResponse;
    }
}

package br.com.gabriel.gestao_vagas.modules.company.useCases;

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

import br.com.gabriel.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.gabriel.gestao_vagas.modules.company.dto.AuthCompanyDTO;

import br.com.gabriel.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {

    // passando o valor do application properties
    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException{
        // verificando se username existe no repositório, caso não exista jogue um erro
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
            () -> {
                throw new UsernameNotFoundException("User not found");
            });

        // caso exista, verificar a senha 
        // sempre o primeiro parâmetro é a senha que não esta criptografada e a segunda já criptografada
        var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        // Se não for igual -> Erro
        if(!passwordMatches){
            throw new AuthenticationException();
        }

        // Se for igual -> Gerar o token
        /* Utilizamos a função CREATE para criar um token JWT. 
        Definimos o emissor (ISSUER) como o nome da empresa e o assunto (SUBJECT) como o ID do usuário. 
        Também definimos o algoritmo de criptografia como HS256 e uma chave secreta para assinar o token */
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiresIn = Instant.now().plus(java.time.Duration.ofHours(2));

        var token = JWT.create().withIssuer("Javagas")
           .withExpiresAt(expiresIn) // duração máx de 2 horas do token
           .withSubject(company.getId().toString())
           .withClaim("roles", Arrays.asList("COMPANY"))
           .sign(algorithm);

        var authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
           .access_token(token)
           .expires_in(expiresIn.toEpochMilli())
           .build();

        return authCompanyResponseDTO;
    }
}

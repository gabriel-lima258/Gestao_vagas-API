package br.com.gabriel.gestao_vagas.modules.candidate.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/* Ao criar um objeto em que devemos criar sets e gets para as variáveis, podemos usar
dependecies do mavem chamada lombok para automatizar isso. Sendo DATA usado para criar
amobos, mas podemos utilizar somente SETTER e GETTER.*/
@Data
@Entity(name = "candidate")
public class CandidateEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // UUID - id único de candidate

    @Schema(example = "Gabriel Lima", requiredMode = RequiredMode.REQUIRED, description = "Nome do Candidato")
    private String name;
    
    @Schema(example = "glds12345", minLength = 10, maxLength = 100, requiredMode = RequiredMode.REQUIRED, description = "Senha do Candidato")
    @Length(min = 10, max = 100, message = "A senha deve conter no mínimo 10 caracteres")
    private String password;
    
    @Schema(example = "gabriel_lima@gmail.com", requiredMode = RequiredMode.REQUIRED, description = "Email do Candidato")
    @Email(message = "O campo [email] deve conter um e-mail válido.")
    private String email;
    
    @Schema(example = "gabriel_lima123", requiredMode = RequiredMode.REQUIRED, description = "Username do Candidato")
    @Pattern(regexp = "\\S+", message = "O campo [username] não deve conter espaço.")
    private String username;

    @Schema(example = "desenvolvedor java junior", requiredMode = RequiredMode.REQUIRED, description = "Descrição do Candidato")
    private String description;
    private String curriculum;

    @CreationTimestamp
    private LocalDateTime createdAt;

}

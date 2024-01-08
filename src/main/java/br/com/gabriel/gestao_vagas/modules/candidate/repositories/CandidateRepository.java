package br.com.gabriel.gestao_vagas.modules.candidate.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabriel.gestao_vagas.modules.candidate.entities.CandidateEntity;

public interface CandidateRepository extends JpaRepository<CandidateEntity, UUID> {
    /* o método findByUsernameOrEmail serVE para fazer essa busca de forma automatizada, passando os parâmetros username e email.
    Também a classe Optional para tratar o retorno da busca, retornando um valor nulo caso não encontre o usuário ou email. */
    Optional<CandidateEntity> findByUsernameOrEmail(String username, String email);
    Optional<CandidateEntity> findByUsername(String username);
}

package br.com.gabriel.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabriel.gestao_vagas.exceptions.JobNotFoundException;
import br.com.gabriel.gestao_vagas.exceptions.UserNotFoundException;
import br.com.gabriel.gestao_vagas.modules.candidate.entities.ApplyJobEntity;
import br.com.gabriel.gestao_vagas.modules.candidate.repositories.ApplyJobRepository;
import br.com.gabriel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.gabriel.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ApplyJobCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired 
    private JobRepository jobRepository;

    @Autowired 
    private ApplyJobRepository applyJobRepository;
    
    // Id do Candidato
    // Id da Vaga
    public ApplyJobEntity execute(UUID idCandidate, UUID idJob) {
        // Validar se candidato existe
        this.candidateRepository.findById(idCandidate)
        .orElseThrow(() -> {
            throw new UserNotFoundException();
        });

        // Validar se a vaga existe 
        this.jobRepository.findById(idJob)
        .orElseThrow(() -> {
            throw new JobNotFoundException();
        });

        // Candidato se inscrever na vaga
        var applyJob = ApplyJobEntity.builder()
        .candidadeId(idCandidate)
        .jobId(idJob)
        .build();

        applyJob = applyJobRepository.save(applyJob);
        return applyJob;
    }
}

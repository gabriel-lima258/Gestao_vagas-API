package br.com.gabriel.gestao_vagas.modules.candidate.useCases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import br.com.gabriel.gestao_vagas.exceptions.JobNotFoundException;
import br.com.gabriel.gestao_vagas.exceptions.UserNotFoundException;
import br.com.gabriel.gestao_vagas.modules.candidate.entities.ApplyJobEntity;
import br.com.gabriel.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.com.gabriel.gestao_vagas.modules.candidate.repositories.ApplyJobRepository;
import br.com.gabriel.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.gabriel.gestao_vagas.modules.company.entities.JobEntity;
import br.com.gabriel.gestao_vagas.modules.company.repositories.JobRepository;

// antes de iniciar um mock devemos extender sua classe para dentro do teste
@ExtendWith(MockitoExtension.class)
public class ApplyJobCandidateUseCaseTest {

    /* Utilizamos a biblioteca Mockito para fazer mocks das dependências 
    e verificamos se a exceção UserNotFoundException é lançada corretamente. */

    // quando quero testar uma camada de serviço usamos @InjectMock
    @InjectMocks
    private ApplyJobCandidateUseCase applyJobCandidateUseCase;

    // já o mock é uma dependência que está dentro da classe ApplyJobCandidateUseCase
    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ApplyJobRepository applyJobRepository;
    
    @Test
    @DisplayName("Should not be able to apply job with candidate not found")
    public void should_not_be_able_to_apply_job_with_candidate_not_found(){
        try {
            this.applyJobCandidateUseCase.execute(null, null);
        } catch (Exception e) {
            // estou assegurando que o erro retornado seja o mesmo da classe ApplyJobCandidateUseCase
            assertThat(e).isInstanceOf(UserNotFoundException.class);
        }
    }

    @Test
    @DisplayName("Should not be able to apply job with job not found")
    public void should_not_be_able_to_apply_job_with_job_not_found(){
        
        var candidateId = UUID.randomUUID();

        var candidate = new CandidateEntity();
        candidate.setId(candidateId);

        // passando o ID do candidato existente e verificamos que o teste passou
        // utilizamos o método optionalOf para retornar um Optional do nosso candidato
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        try {
            this.applyJobCandidateUseCase.execute(candidateId, null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(JobNotFoundException.class);
        }
    }

    @Test
    @DisplayName("Should be able to create a new apply job")
    public void should_be_able_to_create_a_new_apply_job(){
        var candidateId = UUID.randomUUID();
        var jobId = UUID.randomUUID();

        var applyJob = ApplyJobEntity.builder()
        .candidadeId(candidateId)
        .jobId(jobId)
        .build();

        // criando um id para Apply job
        var applyJobCreated = ApplyJobEntity.builder()
        .id(UUID.randomUUID())
        .build();

        // verificando a existência do candidate e job
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new CandidateEntity()));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(new JobEntity()));

        when(applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

        var result = applyJobCandidateUseCase.execute(candidateId, jobId);
        
        // verificando se as informações de id estão corretas
        assertThat(result).hasFieldOrProperty("id");
        // recebendo valor de applyJob não nulo
        assertNotNull(result.getId());
    }
}

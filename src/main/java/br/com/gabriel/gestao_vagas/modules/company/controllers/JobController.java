package br.com.gabriel.gestao_vagas.modules.company.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.gabriel.gestao_vagas.modules.company.entities.JobEntity;
import br.com.gabriel.gestao_vagas.modules.company.useCases.CreateJobUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/job")
public class JobController {
    
    @Autowired
    private CreateJobUseCase createJobUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    public JobEntity create(@Valid @RequestBody CreateJobDTO createJobDTO, HttpServletRequest request){
        // adicionando o request para atribuir o company_id
        var companyId = request.getAttribute("company_id");

        // utilizando o builder para criar uma instância de job entity dos parâmetros CreateJobDTO
        var jobEntity = JobEntity.builder()
                    .benefits(createJobDTO.getBenefits())
                    .companyId(UUID.fromString(companyId.toString())) // convertendo o tipo Objeto para String
                    .description(createJobDTO.getDescription())
                    .level(createJobDTO.getLevel())
                    .build();

        return this.createJobUseCase.execute(jobEntity);
    }
}

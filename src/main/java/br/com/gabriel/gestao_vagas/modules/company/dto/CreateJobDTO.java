package br.com.gabriel.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobDTO {

    // criando exemplos de valores para o swagger
    
    @Schema(example = "Vaga para dev Java", requiredMode = RequiredMode.REQUIRED)
    private String description;

    @Schema(example = "Gym pass, Plano de saúde", requiredMode = RequiredMode.REQUIRED)
    private String benefits;

    @Schema(example = "SENIOR", requiredMode = RequiredMode.REQUIRED)
    private String level;
}

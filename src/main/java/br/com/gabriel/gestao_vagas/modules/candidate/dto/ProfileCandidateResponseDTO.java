package br.com.gabriel.gestao_vagas.modules.candidate.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCandidateResponseDTO {
    
    @Schema(example = "Desenvolvedor Java")
    private String description;

    @Schema(example = "Gabriel_Lima")
    private String username;

    @Schema(example = "Gabriel Lima da Silva")
    private String name;

    @Schema(example = "gabriel58221@gmail.com")
    private String email;
    private UUID id;
}

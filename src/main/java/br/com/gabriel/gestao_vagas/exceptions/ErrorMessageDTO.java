package br.com.gabriel.gestao_vagas.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // cria um construtor com argumentos
public class ErrorMessageDTO {
    
    private String message;
    private String field;
}

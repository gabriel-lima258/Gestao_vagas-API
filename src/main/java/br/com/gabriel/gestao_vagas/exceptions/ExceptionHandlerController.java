package br.com.gabriel.gestao_vagas.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// @ControllerAdvice serve para tratar as exceções da nossa validação
@ControllerAdvice
public class ExceptionHandlerController {

    // MessageSource serve para fazer o tratamento das mensagens
    private MessageSource messageSource;
    
    // esse método serve para instanciar o MessageSource, e não voltar como nulo
    public ExceptionHandlerController(MessageSource message) {
        this.messageSource = message;
    }
    
    // Quando cair em uma exceção o @ExceptionHandler vai direcionar para essa classe
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        // array do objeto erro 
        List<ErrorMessageDTO> dto = new ArrayList<>();

        /* getBindingResult vai ter acesso a todos os erros e getFieldErros vai criar uma list
        junto com forEach para percorre cada um e message vai tratar o erro*/
        e.getBindingResult().getFieldErrors().forEach(err -> {
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());

            // chamando o construtor do ErrorMessageDTO e adicionando no array criado
            ErrorMessageDTO error = new ErrorMessageDTO(message, err.getField());
            dto.add(error);
        });

        // retornando o erro para o usuário final 
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}

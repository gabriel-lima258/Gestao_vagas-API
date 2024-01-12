package br.com.gabriel.gestao_vagas.exceptions;

public class JobNotFoundException extends RuntimeException{
    public JobNotFoundException() {
        super("Trabalho não encontrado!");
    }
}

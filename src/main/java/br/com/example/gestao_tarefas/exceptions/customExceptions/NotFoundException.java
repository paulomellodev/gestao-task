package br.com.example.gestao_tarefas.exceptions.customExceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}

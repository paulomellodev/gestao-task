package br.com.example.gestao_tarefas.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.example.gestao_tarefas.exceptions.customExceptions.NotFoundException;
import br.com.example.gestao_tarefas.exceptions.dtos.ErrorMessageDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
    private MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource message) {
        this.messageSource = message;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<ErrorMessageDTO> dto = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(err -> {
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
            ErrorMessageDTO error = new ErrorMessageDTO(message, err.getField());
            dto.add(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        ErrorMessageDTO error = new ErrorMessageDTO(e.getMessage(), "id");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
            List<ErrorMessageDTO> dto = new ArrayList<>();
            invalidFormatException.getPath().forEach(reference -> {
                String fieldName = reference.getFieldName();
                String message = String.format("Valor '%s' é inválido para o campo '%s'", invalidFormatException.getValue(), fieldName);
                ErrorMessageDTO error = new ErrorMessageDTO(message, fieldName);
                dto.add(error);
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

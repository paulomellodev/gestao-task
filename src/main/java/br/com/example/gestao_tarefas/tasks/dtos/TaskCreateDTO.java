package br.com.example.gestao_tarefas.tasks.dtos;

import java.util.UUID;

import br.com.example.gestao_tarefas.tasks.enums.TasksStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TaskCreateDTO {
    private UUID id;
    
    @NotBlank(message = "Campo não pode ser vazio.")
    @NotEmpty(message = "Campo obrigatório.")
    private String title;
    
    private TasksStatusEnum status;

    @NotBlank(message = "Campo não pode ser vazio.")
    @NotEmpty(message = "Campo obrigatório")
    private String description;
}

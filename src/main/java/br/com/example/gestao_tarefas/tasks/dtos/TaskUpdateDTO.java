package br.com.example.gestao_tarefas.tasks.dtos;

import br.com.example.gestao_tarefas.tasks.enums.TasksStatusEnum;
import lombok.Data;

@Data
public class TaskUpdateDTO {
    private String title;
    private TasksStatusEnum status;
    private String description;
}

package br.com.example.gestao_tarefas.tasks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.example.gestao_tarefas.exceptions.customExceptions.NotFoundException;
import br.com.example.gestao_tarefas.tasks.dtos.TaskCreateDTO;
import br.com.example.gestao_tarefas.tasks.enums.TasksStatusEnum;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskEntity create(TaskCreateDTO taskData){
        if(taskData.getStatus() == null){
            taskData.setStatus(TasksStatusEnum.DRAFT);
        }
        final TaskEntity newTask = new TaskEntity(taskData.getTitle(), taskData.getStatus(), taskData.getDescription());
        taskRepository.save(newTask);
        return newTask;
    }

    public List<TaskEntity> listAll(Optional<TasksStatusEnum> taskStatus){
        if(taskStatus.isPresent()){
            return taskRepository.findByStatus(taskStatus);
        }
        return taskRepository.findAll();
    }

    public TaskEntity findById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    public TaskEntity updateTask(UUID taskId, TaskCreateDTO taskData){
        
    }

    public void deleteTaskById(UUID taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException();
        }
        taskRepository.deleteById(taskId);
    }

}

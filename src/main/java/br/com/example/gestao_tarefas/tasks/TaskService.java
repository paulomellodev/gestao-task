package br.com.example.gestao_tarefas.tasks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.example.gestao_tarefas.exceptions.customExceptions.NotFoundException;
import br.com.example.gestao_tarefas.tasks.dtos.TaskCreateDTO;
import br.com.example.gestao_tarefas.tasks.dtos.TaskUpdateDTO;
import br.com.example.gestao_tarefas.tasks.enums.TasksStatusEnum;
import br.com.example.gestao_tarefas.users.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional
    public TaskEntity create(TaskCreateDTO taskData, UserEntity user){
        if(taskData.getStatus() == null){
            taskData.setStatus(TasksStatusEnum.DRAFT);
        }

        final TaskEntity newTask = new TaskEntity(taskData.getTitle(), taskData.getStatus(), taskData.getDescription(), user);
        taskRepository.save(newTask);
        return newTask;
    }

    public List<TaskEntity> listAll(Optional<String> status, Optional<String> title){
        return taskRepository.findByTitleStatus(status.orElse(null), title.orElse(null));
    }

    public TaskEntity findById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @Transactional
    public TaskEntity updateTask(UUID taskId, TaskUpdateDTO taskData){
        TaskEntity task = findById(taskId);
        
        if(taskData.getTitle() != null){
            task.setTitle(taskData.getTitle());
        }
        if(taskData.getStatus() != null){
            task.setStatus(taskData.getStatus());
        }
        if(taskData.getDescription() != null){
            task.setDescription(taskData.getDescription());
        }

        return taskRepository.save(task);
    }

    public void deleteTaskById(UUID taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException();
        }
        taskRepository.deleteById(taskId);
    }

}

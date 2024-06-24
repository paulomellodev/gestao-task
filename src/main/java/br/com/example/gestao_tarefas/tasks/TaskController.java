package br.com.example.gestao_tarefas.tasks;

import br.com.example.gestao_tarefas.tasks.dtos.TaskCreateDTO;
import br.com.example.gestao_tarefas.tasks.dtos.TaskUpdateDTO;
import br.com.example.gestao_tarefas.users.UserEntity;
import br.com.example.gestao_tarefas.users.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<TaskEntity> createTask(@Valid @RequestBody TaskCreateDTO taskData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails);
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> {
            String error = String.format("Username %s not found", userDetails.getUsername());
            return new UsernameNotFoundException(error);
        });
                
        TaskEntity newTask = taskService.create(taskData, user);
        return new ResponseEntity<TaskEntity>(newTask, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<TaskEntity>> listAllTasks(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> status) {
        final List<TaskEntity> allTasks = taskService.listAll(status, title);
        return new ResponseEntity<List<TaskEntity>>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskEntity> listTaskById(@PathVariable UUID taskId) {
        return new ResponseEntity<TaskEntity>(taskService.findById(taskId), HttpStatus.OK);
    }
    
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskEntity> updateTask(
        @PathVariable UUID taskId,
        @Valid @RequestBody TaskUpdateDTO taskUpdateDto) {
        return new ResponseEntity<>(taskService.updateTask(taskId, taskUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable UUID taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.noContent().build();
    }
}

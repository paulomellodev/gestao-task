package br.com.example.gestao_tarefas.tasks;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import br.com.example.gestao_tarefas.tasks.enums.TasksStatusEnum;


public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findByStatus(Optional<TasksStatusEnum> taskStatus);
    List<TaskEntity> findByTitle(String title);

    @Query(value = "SELECT * FROM tasks WHERE 1=1 AND title LIKE %:title% AND status = :taskStatus", nativeQuery=true)
    List<TaskEntity> filterTasks(
        @Param("taskStatus") Optional<TasksStatusEnum> taskStatus,
        @Param("title") Optional<String> title);
}

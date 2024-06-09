package br.com.example.gestao_tarefas.tasks;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    @Query("SELECT t FROM tasks t WHERE " +
           "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:status IS NULL OR UPPER(:status) = t.status)")
    List<TaskEntity> findByTitleStatus(
        @Param("status") String status,
        @Param("title") String title);
}

package br.com.example.gestao_tarefas.tasks;

import java.util.UUID;

import br.com.example.gestao_tarefas.tasks.enums.TasksStatusEnum;
import br.com.example.gestao_tarefas.users.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity(name="tasks")
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter private UUID id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TasksStatusEnum status;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_task_id")
    private TaskEntity parentTask;

    public TaskEntity(String title, TasksStatusEnum status, String description, UserEntity user){
        this.title = title;
        this.status = status;
        this.description = description;
        this.user = user;
    }
}

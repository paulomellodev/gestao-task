package br.com.example.gestao_tarefas.users;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Setter;

@Data
@Entity(name = "users")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ Setter private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}

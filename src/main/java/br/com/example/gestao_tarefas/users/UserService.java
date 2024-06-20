package br.com.example.gestao_tarefas.users;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserEntity> read(){
        return userRepository.findAll();
    }

    public UserEntity findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }
}

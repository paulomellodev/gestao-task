package br.com.example.gestao_tarefas.session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.example.gestao_tarefas.security.JwtTokenProvider;
import br.com.example.gestao_tarefas.users.UserEntity;
import br.com.example.gestao_tarefas.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class SessionController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity payload) {
        Optional<UserEntity> foundUser = userRepository.findByUsername(payload.getUsername());

        if(foundUser.isPresent()){
            HashMap<String, String> msg = new HashMap<String, String>();
            msg.put("error", "Username already exists");
            return ResponseEntity.status(409).body(msg);
        }

        var userBuilder = UserEntity.builder()
                                    .username(payload.getUsername())
                                    .name(payload.getName())
                                    .email(payload.getEmail())
                                    .password(encoder.encode(payload.getPassword()));
        
        if(payload.getAdmin()){
            List<String> adminRole = Arrays.asList("ADMIN");
            UserEntity admin = userBuilder.roles(adminRole).admin(payload.getAdmin()).build();

            UserEntity user = userRepository.save(admin);
            return ResponseEntity.status(201).body(user);
        }
        
        UserEntity commom = userBuilder.roles(Arrays.asList("COMMON")).build();

        UserEntity user = userRepository.save(commom);
        return ResponseEntity.status(201).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity payload) {
        var authToken = new UsernamePasswordAuthenticationToken(
            payload.getUsername(),
            payload.getPassword()
            );
        
        Authentication authentication = authenticationManager.authenticate(authToken);
        String token = jwtTokenProvider.createToken(authentication);

        HashMap<String, String> msg = new HashMap<String, String>();
        msg.put("token", token);

        return ResponseEntity.status(200).body(msg);
    }
}

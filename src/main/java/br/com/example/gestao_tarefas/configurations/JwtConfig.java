package br.com.example.gestao_tarefas.configurations;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class JwtConfig {
    private final String secretKey = genValidSecretKey("chave_muito_secreta");
    private final Long expiresInMiliSeconds = hoursToMs(2);

    private Long hoursToMs (long hour){
        return hour * 60 * 60 * 1000;
    }

    private String genValidSecretKey(String secretKey){
        int repeats = 1;
        while (secretKey.length() < 22){
            secretKey = secretKey.repeat(repeats);
            repeats++;
        }

        return secretKey;
    }
}

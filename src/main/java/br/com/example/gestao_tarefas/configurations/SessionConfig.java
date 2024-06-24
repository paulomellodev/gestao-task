package br.com.example.gestao_tarefas.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.example.gestao_tarefas.users.UserRepository;

@Configuration
public class SessionConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository){
        return (username) -> userRepository
                                .findByUsername(username)
                                .orElseThrow(()-> {
                                    var error = String.format("Username %s not found", username);
                                    return new UsernameNotFoundException(error);
                                });
    }

    @Bean
    AuthenticationManager authenticationManager(
                                        UserDetailsService userDetailsService,
                                        PasswordEncoder encoder) {
        return authentication -> {
            String username = authentication.getPrincipal().toString();
            String password = authentication.getCredentials().toString();

            try {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                Boolean hasSamePassword = encoder.matches(password, user.getPassword());

                if(!hasSamePassword){
                    throw new BadCredentialsException("Invalid username/password supplied");
                }
                if(!user.isEnabled()){
                    throw new DisabledException("User account is not active");
                }

                return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
            } catch (UsernameNotFoundException e) {
                    throw new BadCredentialsException("Invalid username/password supplied");
            }
        };
    }   
}

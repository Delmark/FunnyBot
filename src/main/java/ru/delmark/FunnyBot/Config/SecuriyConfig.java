package ru.delmark.FunnyBot.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecuriyConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.GET, "/actuator/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/jokes/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.POST, "/jokes").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/jokes/{id}").hasAnyAuthority("MODERATOR", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/jokes/{id}").hasAnyAuthority("MODERATOR", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority("ADMIN")

                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

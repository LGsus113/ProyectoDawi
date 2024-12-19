package com.cibertec.edu.Proyecto_DAWI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    CustomHandlerSuccess customHandlerSuccess;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/start/login").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/start/products-all").hasAnyRole("Admin")
                        .requestMatchers("/start/home").hasAnyRole("Usuario")
                        .requestMatchers("/start/car-to-shop").hasAnyRole("Usuario")
                        .requestMatchers("/start/procesar-carrito").hasAnyRole("Usuario")
                        .requestMatchers("/start/add").hasAnyRole("Admin")
                        .requestMatchers("/start/update").hasAnyRole("Admin")
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((req, rsp, e) -> {
                            rsp.sendRedirect("/start/restricted");
                        })
                )

                .formLogin(form -> form
                        .loginPage("/start/login")
                        .successHandler(customHandlerSuccess)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/start/logout")
                        .logoutSuccessUrl("/start/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.example.twspring2.security.config;

import com.example.twspring2.service.users.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthenticationSuccesHandler customAuthenticationSuccesHandler;
    private final UserService userService;

    public SecurityConfig(CustomAuthenticationSuccesHandler customAuthenticationSuccesHandler, UserService userService) {
        this.customAuthenticationSuccesHandler = customAuthenticationSuccesHandler;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/*").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccesHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(userService))
                        .successHandler(customAuthenticationSuccesHandler)
                        .defaultSuccessUrl("/home", true)
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}

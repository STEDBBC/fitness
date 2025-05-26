package com.example.demo.config;

import com.example.demo.security.CustomUrlAuthenticationSuccessHandler;
import com.example.demo.security.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    public SecurityConfig(CustomUserDetailsService uds) {
        this.userDetailsService = uds;
    }

    // 1) PasswordEncoder (остается как раньше)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2) SuccessHandler
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomUrlAuthenticationSuccessHandler();
    }

    // 3) AuthenticationManager — нужен, чтобы Spring знал про наш UserDetailsService
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 4) Основные правила безопасности
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF отключить, если у вас чистый SPA или вы сами обрабатываете токены
            .csrf(csrf -> csrf.disable())

            // права доступа
            .authorizeHttpRequests(auth -> auth
                // Админ
                .requestMatchers("/admin.html", "/manage-**").hasRole("ADMIN")
                // Клиент
                .requestMatchers("/client.html", "/client/**").hasRole("CLIENT")
                // Тренер
                .requestMatchers("/trainer.html", "/trainer/**").hasRole("TRAINER")
                // Статика и регистрация — открыто
                .requestMatchers("/register.html", "/api/register", "/css/**", "/js/**").permitAll()
                // Всё остальное — только для аутентифицированных
                .anyRequest().authenticated()
            )

            // форма логина
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .successHandler(successHandler())
                .permitAll()
            )

            // logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout")
                .permitAll()
            );

        return http.build();
    }
}

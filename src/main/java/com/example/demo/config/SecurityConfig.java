package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import com.example.demo.security.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public CustomUserDetailsService userDetailsService(UserRepository userRepo) {
        return new CustomUserDetailsService(userRepo);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(
        CustomUserDetailsService uds,
        BCryptPasswordEncoder encoder
    ) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(encoder);
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           RoleBasedAuthSuccessHandler successHandler) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authProvider(userDetailsService(null), passwordEncoder()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login.html", "/register.html", "/api/auth/register").permitAll()
                .requestMatchers("/css/**","/js/**").permitAll()
                // админские страницы
                .requestMatchers("/admin.html", "/manage-**.html", "/api/admin/**")
                .hasRole("ADMIN")
                // клиентская часть
                .requestMatchers("/client.html", "/api/user/**")
                .hasRole("CLIENT")
                // тренерская часть (потом)
                .requestMatchers("/trainer.html", "/api/trainer/**")
                .hasRole("TRAINER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public RoleBasedAuthSuccessHandler roleSuccessHandler() {
        return new RoleBasedAuthSuccessHandler();
    }
}

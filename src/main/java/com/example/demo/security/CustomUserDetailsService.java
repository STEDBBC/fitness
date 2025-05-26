package com.example.demo.security;

import com.example.demo.model.data.UserData;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;  // ваш JPA-репозиторий

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserData user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + email));

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities
        );
    }
}
package com.example.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;

public class CustomUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {
        // Проходим по ролям
        String targetUrl = "/";  // по умолчанию на корень
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                targetUrl = "/admin.html";
                break;
            }
            if ("ROLE_CLIENT".equals(role)) {
                targetUrl = "/client.html";
                break;
            }
            if ("ROLE_TRAINER".equals(role)) {
                targetUrl = "/trainer.html";
                break;
            }
        }
        response.sendRedirect(targetUrl);
    }
}
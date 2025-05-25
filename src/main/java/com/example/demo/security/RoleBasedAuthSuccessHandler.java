// com/example/demo/security/RoleBasedAuthSuccessHandler.java
package com.example.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;

public class RoleBasedAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication auth
    ) throws IOException, ServletException {
        String role = auth.getAuthorities().iterator().next().getAuthority(); // e.g. "ROLE_CLIENT"
        switch (role) {
            case "ROLE_ADMIN":
                response.sendRedirect("/admin.html");
                break;
            case "ROLE_CLIENT":
                response.sendRedirect("/client.html");
                break;
            case "ROLE_TRAINER":
                response.sendRedirect("/trainer.html");
                break;
            default:
                response.sendRedirect("/login.html?error");
        }
    }
}

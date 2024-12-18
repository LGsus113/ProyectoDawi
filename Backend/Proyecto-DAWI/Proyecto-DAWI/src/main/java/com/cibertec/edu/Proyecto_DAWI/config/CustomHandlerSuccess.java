package com.cibertec.edu.Proyecto_DAWI.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomHandlerSuccess implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities().toString();

        if (role.contains("ROLE_Admin")) {
            response.sendRedirect("/start/products-all");
        } else if (role.contains("ROLE_Usuario")) {
            response.sendRedirect("/start/home");
        } else {
            response.sendRedirect("/start/products-all");
        }
    }
}

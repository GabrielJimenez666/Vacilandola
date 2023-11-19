package com.example.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ControladorLogout {

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidar la sesión
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Redirigir a la página de inicio de sesión o inicio
        return "redirect:/";
    }
}
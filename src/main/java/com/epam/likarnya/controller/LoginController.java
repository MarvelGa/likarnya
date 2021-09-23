package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import com.epam.likarnya.model.User;
import com.epam.likarnya.security.CustomUserDetailsService;
import com.epam.likarnya.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@Data
@Controller
@RequestMapping("/likarnya")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping(value = "/login")
    public String loginForm(Model model) {
        return "loginPage";
    }

    @GetMapping(value = "/login-error")
    public String errorLoginForm(Principal principal, Model model) {
        if (principal == null) {
            String errorMessage = "Wrong login or password";
            model.addAttribute("errorMessage", errorMessage);
        }
        return "loginPage";
    }

    @GetMapping("/success")
    public void loginPageRedirect(Principal principal, HttpServletRequest request, HttpServletResponse response, Authentication authResult, HttpSession session, Model model) throws IOException {
        var currentUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(principal.getName());
        String role = authResult.getAuthorities().toString();
        if (role.contains("ROLE_ADMIN")) {
            session.setAttribute("user", user);
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/likarnya/admin"));
        } else if (role.contains("ROLE_DOCTOR")) {
            session.setAttribute("doctor", user);
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/likarnya/doctor-cabinet"));
        } else if (role.contains("ROLE_NURSE")) {
            session.setAttribute("nurse", user);
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/likarnya/nurse-cabinet"));
        }
    }

    @PostMapping("/perform-logout")
    public String doLogout(Model theModel, final HttpSession session, @ModelAttribute("loginUser") LoginRequestDto loginRequestDto) {
        session.invalidate();
        theModel.addAttribute("loginUser", new LoginRequestDto());
        return "redirect:/likarnya/login";
    }
}

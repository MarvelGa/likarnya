package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import com.epam.likarnya.exception.EncryptException;
import com.epam.likarnya.model.User;
import com.epam.likarnya.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Slf4j
@Data
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping(value = "/login")
    public String loginForm(Model model) {
        model.addAttribute("loginUser", new LoginRequestDto());
        return "loginPage";
    }

    @PostMapping(value = "/login")
    public String handleLogin(Model model, HttpSession session, @Valid @ModelAttribute("loginUser") LoginRequestDto loginRequestDto, BindingResult bindingResult) {
        String errorMessage;
        if (bindingResult.hasErrors()) {
            return "loginPage";
        }

        User user = userService.findByEmail(loginRequestDto.getEmail());
        log.trace("Found in DB: user --> " + user);

        if (user == null || !(encryptPassword(loginRequestDto.getPassword()).equals(user.getPassword()))) {
            errorMessage = "Wrong login or password";
            model.addAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            log.debug(String.format("forward --> %s", "/login"));
            return "login";
        } else {
            log.trace("Found in DB: user --> " + user);
            if (user.getRole() == User.Role.DOCTOR) {
                session.setAttribute("doctor", user);
                log.trace("Set the session attribute: user --> " + user);
                log.debug(String.format("redirect --> %s", "/doctorPage"));
                return "redirect:/doctor-cabinet";
            }
            if (user.getRole() == User.Role.NURSE) {
                session.setAttribute("nurse", user);
                log.trace("Set the session attribute: user --> " + user);
                log.debug(String.format("redirect --> %s", "/doctorPage"));
                return "nursePage";
            }
            session.setAttribute("user", user);
            log.trace("Set the session attribute: user --> " + user);
            log.debug(String.format("redirect --> %s", "/adminPage"));
            return "redirect:/admin";
        }
    }

    @PostMapping("/perform-logout")
    public String doLogout(Model theModel, final HttpSession session, @ModelAttribute("loginUser") LoginRequestDto loginRequestDto) {
        session.invalidate();
        theModel.addAttribute("loginUser", new LoginRequestDto());
        return "loginPage";
    }


    private String encryptPassword(final String password) throws EncryptException {
        if (Objects.isNull(password) || password.isEmpty()) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes(), 0, password.length());
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            throw new EncryptException(e.getMessage(), e);
        }
    }
}

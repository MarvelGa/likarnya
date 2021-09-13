package com.epam.likarnya.controller;

import com.epam.likarnya.dto.RegistrationRequestDto;
import com.epam.likarnya.model.User;
import com.epam.likarnya.service.CategoryService;
import com.epam.likarnya.service.UserService;
import com.epam.likarnya.validator.DataValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Controller
@RequiredArgsConstructor
public class RegistrationUserController {
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/admin/medical-registration")
    public String registrationForm(Model model) {
        model.addAttribute("registrationUser", new RegistrationRequestDto());
        return "medicalRegistrationPage";
    }

    @PostMapping("/admin/medical-registration")
    public String handleRegistration(Model model, @Valid @ModelAttribute("registrationUser") RegistrationRequestDto requestDto,
                                     BindingResult bindingResult) {
        log.debug("Registration medical personal starts");
        List<String> errorMessages = new ArrayList<>();
        String errorMessage;

        if (bindingResult.hasErrors()) {
            return "registrationPage";
        }
        if (!DataValidator.isEmailValid(requestDto.getEmail())) {
            errorMessage = "Email is not valid";
            log.error(errorMessage);
            errorMessages.add(errorMessage);
        }
        if (!DataValidator.isPasswordValid(requestDto.getPassword())) {
            errorMessage = "Password is not valid";
            log.error(errorMessage);
            errorMessages.add(errorMessage);
        }

        if (!DataValidator.isNameValid(requestDto.getFirstName())) {
            errorMessage = "First Name is not valid";
            log.error(errorMessage);
            errorMessages.add(errorMessage);
        }

        if (!DataValidator.isSurnameValid(requestDto.getLastName())) {
            errorMessage = "Last Name is not valid";
            log.error(errorMessage);
            errorMessages.add(errorMessage);
        }

        if (userService.findByEmail(requestDto.getEmail()) != null) {
            errorMessage = "This email is already exist";
            log.error(errorMessage);
            errorMessages.add(errorMessage);
        }

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages);
            log.debug(String.format("forward --> %s", "/registration"));
            return "medicalRegistrationPage";
        } else {
            User newUser;
            if (requestDto.getCategory() != 0) {
                newUser = User.builder()
                        .firstName(requestDto.getFirstName())
                        .lastName(requestDto.getLastName())
                        .email(requestDto.getEmail())
                        .password(encryptPassword(requestDto.getPassword()))
                        .role(requestDto.getRole())
                        .category(categoryService.findById(requestDto.getCategory()))
                        .build();

            } else {
                newUser = User.builder()
                        .firstName(requestDto.getFirstName())
                        .lastName(requestDto.getLastName())
                        .email(requestDto.getEmail())
                        .password(encryptPassword(requestDto.getPassword()))
                        .role(requestDto.getRole())
                        .build();
            }

            if (newUser.getRole()== User.Role.NURSE){
                userService.createOrUpdate(newUser);
                log.debug(String.format("redirect --> %s", "/admin/nurses"));
                model.addAttribute("registrationSuccess", "The nurse added successfully");
                return "redirect:/admin/nurses";
            }

            log.trace("Saving new user: " + newUser);
            userService.createOrUpdate(newUser);
            log.debug(String.format("redirect --> %s", "/admin/doctors"));
            model.addAttribute("registrationSuccess", "The doctor added successfully");
            return "redirect:/admin/doctors";
        }
    }

    private String encryptPassword(final String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes(), 0, password.length());
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            return password.hashCode() + password.hashCode() + "";
        }
    }
}

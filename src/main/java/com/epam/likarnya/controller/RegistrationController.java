package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import com.epam.likarnya.dto.RegistrationRequestDto;
import com.epam.likarnya.validator.DataValidator;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@Controller
public class RegistrationController {
    @GetMapping( "/registration")
    public String registrationForm(Model model){
        model.addAttribute("registrationUser", new RegistrationRequestDto());
        return "registrationPage";
    }

    @PostMapping( "/registration")
    public String handleRegistration(Model model, @Valid @ModelAttribute("registrationUser") RegistrationRequestDto requestDto,
                                     BindingResult bindingResult){
        List<String> errorMessages = new ArrayList<>();

        if(bindingResult.hasErrors()){
            return "registrationPage";
        }
        if (!DataValidator.isEmailValid(requestDto.getEmail())){
            errorMessages.add("Email is not valid");
//            model.addAttribute("validateEmail", "Email is not valid");
//            return "registrationPage";
        }
        if (!DataValidator.isPasswordValid(requestDto.getPassword())){
            errorMessages.add("Password is not valid");
//            model.addAttribute("validatePassword", "Password is not valid");

        }
        if (!errorMessages.isEmpty()){
            model.addAttribute("errorMessages", errorMessages);
            return "registrationPage";
        }

        return "ssPage";
    }
}

package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Data
@Controller
public class LoginController {
    @GetMapping( value ="/login")
    public String loginForm(Model model){
        model.addAttribute("loginUser", new LoginRequestDto());
        return "loginPage";
    }

    @PostMapping(value="/login")
    public String handleLogin(Model model, @Valid @ModelAttribute ("loginUser") LoginRequestDto theLoginUser, BindingResult bindingResult ){

        if (bindingResult.hasErrors()){
            return "loginPage";
        }

        return "adminPage";
    }
}

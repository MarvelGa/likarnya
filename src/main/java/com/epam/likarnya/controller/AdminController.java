package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import com.epam.likarnya.model.User;
import com.epam.likarnya.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Data
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping(value = "/admin")
    public String adminCabinet(Model model) {
        model.addAttribute("loginUser", new LoginRequestDto());
        return "adminPage";
    }

    @GetMapping(value = "/admin/doctors")
    public String listDoctors(Model model) {
        List<User> doctors = userService.findUsersByRole(User.Role.DOCTOR);
        model.addAttribute("doctors", doctors);
        return "listDoctors";
    }

    @GetMapping(value = "/admin/patient")
    public String listPatients(Model model) {
        model.addAttribute("loginUser", new LoginRequestDto());
        return "listPatient";
    }



}

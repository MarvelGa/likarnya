package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.model.Category;
import com.epam.likarnya.model.User;
import com.epam.likarnya.repository.UserRepository;
import com.epam.likarnya.service.CategoryService;
import com.epam.likarnya.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Data
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping(value = "/admin")
    public String adminCabinet(Model model) {
        model.addAttribute("loginUser", new LoginRequestDto());
        return "adminPage";
    }

    @GetMapping(value = "/admin/doctors")
    public String listDoctors(Model model) {
        List<Category> categories = categoryService.getAll();
//        List<User> doctors = userService.findUsersByRole(User.Role.DOCTOR);
//        model.addAttribute("doctors", doctors);
        model.addAttribute("categories", categories);
        return "listDoctors";
    }

    @GetMapping(value = "/admin/doctors/{cat}")
    public String listDoctorskk(Model model, @PathVariable(value = "cat", required = false) String catjjj) {
        String hhh=catjjj;
        List<Category> categories = categoryService.getAll();
      var doctors=  userRepository.getDoctorsByCategory(catjjj);
//      var doctors=  userRepository.getDoctorsByCategory(1L);


        //List<User> doctors = userService.findUsersByRole(User.Role.DOCTOR);
        model.addAttribute("categ", catjjj);
        model.addAttribute("doctors", doctors);
        model.addAttribute("categories", categories);
        return "listDoctors";
    }


    @GetMapping(value = "/admin/nurses")
    public String listNurses(Model model) {
        List<NurseDTO> nurses = userService.getNurses();
        model.addAttribute("nurses", nurses);
        return "listNurses";
    }

}

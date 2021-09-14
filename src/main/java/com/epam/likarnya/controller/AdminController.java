package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.dto.UserDTO;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

//    @GetMapping(value = "/admin/doctors")
//    public String listDoctors(Model model) {
//        List<Category> categories = categoryService.getAll();
////        List<User> doctors = userService.findUsersByRole(User.Role.DOCTOR);
////        model.addAttribute("doctors", doctors);
//        model.addAttribute("categories", categories);
//        return "listDoctors";
//    }
//
//    @GetMapping(value = "/admin/doctors/{cat}")
//    public String listDoctorskk(Model model, @PathVariable(value = "cat", required = false) String catjjj) {
//        String hhh=catjjj;
//        List<Category> categories = categoryService.getAll();
//      var doctors=  userRepository.getDoctorsByCategory(catjjj);
////      var doctors=  userRepository.getDoctorsByCategory(1L);
//
//
//        //List<User> doctors = userService.findUsersByRole(User.Role.DOCTOR);
//        model.addAttribute("categ", catjjj);
//        model.addAttribute("doctors", doctors);
//        model.addAttribute("categories", categories);
//        return "listDoctors";
//    }


    @GetMapping(value = "/admin/nurses")
    public String listNurses(Model model) {
        List<NurseDTO> nurses = userService.getNurses();
        model.addAttribute("nurses", nurses);
        return "listNurses";
    }

    @GetMapping(value = "/admin/doctors")
    public String listDoctors(@RequestParam(value = "category", required = false) String category, @RequestParam(value = "sorting", required = false) String sort, String cat, Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("cat", new String());
        model.addAttribute("categories", categories);
        List<UserDTO> doctorsList = new ArrayList<>();

        if (category == null) {
            doctorsList = userService.findDoctorsWithCountOfPatients();
        } else {
            if (category.isEmpty()) {
                doctorsList = userService.findDoctorsWithCountOfPatients();
                return getString(model, sort, doctorsList);
            }

            Long categoryId = Long.valueOf(category);
            doctorsList = userService.findDoctorsWithCountOfPatientsByCategoryId(categoryId);
            model.addAttribute("catValue", categoryId);
        }
        return getString(model, sort, doctorsList);
    }

    private String getString(Model model, String sort, List<UserDTO> doctorsList) {
        if (sort != null && doctorsList.size() != 0) {
            if (!sort.isEmpty()) {
                if (sort.equals("ASC-NAME")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(UserDTO::getFirstName))
                            .collect(Collectors.toList());
                }
                if (sort.equals("DESC-NAME")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(UserDTO::getFirstName).reversed())
                            .collect(Collectors.toList());
                }
                if (sort.equals("ASC")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(UserDTO::getLastName))
                            .collect(Collectors.toList());
                }
                if (sort.equals("DESC")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(UserDTO::getLastName).reversed())
                            .collect(Collectors.toList());
                }
                if (sort.equals("DECREASE")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c.getCountOfPatients()!=null)
                            .sorted(Comparator.comparing(UserDTO::getCountOfPatients).reversed())
                            .collect(Collectors.toList());
                }
                if (sort.equals("INCREASE")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c.getCountOfPatients()!=null)
                            .sorted(Comparator.comparing(UserDTO::getCountOfPatients))
                            .collect(Collectors.toList());
                }
                if (sort.equals("CAT-ASC")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(UserDTO::getCategory))
                            .collect(Collectors.toList());
                }
                if (sort.equals("CAT-DESC")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(UserDTO::getCategory).reversed())
                            .collect(Collectors.toList());
                }
            }
        }

        model.addAttribute("sort", sort);
        model.addAttribute("doctorsList", doctorsList);
        return "listDoctors";
    }

}

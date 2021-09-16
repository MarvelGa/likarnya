package com.epam.likarnya.controller;

import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.dto.UserDTO;
import com.epam.likarnya.model.Category;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.repository.UserRepository;
import com.epam.likarnya.service.CategoryService;
import com.epam.likarnya.service.PatientService;
import com.epam.likarnya.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private final PatientService patientService;

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
                            .filter(c -> c.getCountOfPatients() != null)
                            .sorted(Comparator.comparing(UserDTO::getCountOfPatients).reversed())
                            .collect(Collectors.toList());
                }
                if (sort.equals("INCREASE")) {
                    doctorsList = doctorsList.stream()
                            .filter(c -> c.getCountOfPatients() != null)
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

    @GetMapping(value = "/admin/patients/history")
    public String handleHistory(@PageableDefault(size = 8) Pageable pageable, Model model) {
        Page<TreatmentPatientDto> page = patientService.getHistory(pageable);
        Long totalElements = page.getTotalElements();
        model.addAttribute("page", page);
        model.addAttribute("totalElements", totalElements);
        return "patientsHistory";
    }

    @GetMapping(value = "/admin")
    public String listPatients(@RequestParam(value = "sorting", required = false) String sort, Model model) {
        List<Patient> patients = patientService.patientsWithOutMedicCard();
        if (sort != null && patients.size() != 0) {
            if (!sort.isEmpty()) {
                if (sort.equals("ASC-NAME")) {
                    patients = patients.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(Patient::getFirstName))
                            .collect(Collectors.toList());
                }
                if (sort.equals("DESC-NAME")) {
                    patients = patients.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(Patient::getFirstName).reversed())
                            .collect(Collectors.toList());
                }
                if (sort.equals("ASC")) {
                    patients = patients.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(Patient::getLastName))
                            .collect(Collectors.toList());
                }
                if (sort.equals("DESC")) {
                    patients = patients.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(Patient::getLastName).reversed())
                            .collect(Collectors.toList());
                }
                if (sort.equals("DECREASE")) {
                    patients = patients.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(Patient::getDateOfBirth).reversed())
                            .collect(Collectors.toList());
                }
                if (sort.equals("INCREASE")) {
                    patients = patients.stream()
                            .filter(c -> c != null)
                            .sorted(Comparator.comparing(Patient::getDateOfBirth))
                            .collect(Collectors.toList());
                }
                model.addAttribute("sort", sort);
            }
        }
        model.addAttribute("listPatients", patients);
        return "listPatient";
    }

}

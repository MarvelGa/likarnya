package com.epam.likarnya.controller;

import com.epam.likarnya.dto.LoginRequestDto;
import com.epam.likarnya.dto.PatientDto;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.service.PatientService;
import com.epam.likarnya.validator.DataValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Controller
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

//    @GetMapping(value = "/admin/patients")
//    public String patientsList(Model model) {
////        model.addAttribute("dateOfToday", String.valueOf(LocalDate.now()));
////        model.addAttribute("registrationPatient", new PatientDto());
//        return "listPatient";
//
//    }

    @GetMapping(value = "/admin/patient-registration")
    public String patientRegistration(Model model) {
        model.addAttribute("dateOfToday", String.valueOf(LocalDate.now()));
        model.addAttribute("registrationPatient", new PatientDto());
        return "patientRegistration";
    }

    @PostMapping(value = "/admin/patient-registration")
    public String patientRegistration(Model model, @Valid @ModelAttribute("registrationPatient") PatientDto patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "patientRegistration";
        }

        log.debug("Registration patient starts");
        List<String> errorMessages = new ArrayList<>();
        String errorMessage;

        if (!DataValidator.isNameValid(patient.getFirstName())) {
            errorMessage = "First Name is not valid";
            log.error(errorMessage);
            errorMessages.add(errorMessage);
        }

        if (!DataValidator.isSurnameValid(patient.getLastName())) {
            errorMessage = "Last Name is not valid";
            log.error(errorMessage);
            errorMessages.add(errorMessage);
        }
        if (!errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages);
            log.debug(String.format("forward --> %s", "/admin/patient-registration"));
            return "patientRegistration";
        } else {
            Patient newPatient = new Patient();
            newPatient.setFirstName(patient.getFirstName());
            newPatient.setLastName(patient.getLastName());
            newPatient.setDateOfBirth(LocalDate.parse(patient.getDateOfBirth()));
            newPatient.setGender(patient.getGender());
            patientService.createOrUpdate(newPatient);
            return "redirect:/admin/patients";
        }

    }

//    @GetMapping(value = "/admin/patients")
//    public String listPatients(Model model) {
//        List<Patient> patients = patientService.getPatients();
//        model.addAttribute("patients", patients);
//        return "listPatient";
//    }

    @GetMapping(value = "/admin/patients")
    public String listPatients(@PageableDefault(size = 8) Pageable pageable, Model model) {
        Page<Patient> page = patientService.getPatients(pageable);
        Long totalElements = page.getTotalElements();
        model.addAttribute("page", page);
        model.addAttribute("totalElements", totalElements);
        return "listPatient";
    }
}

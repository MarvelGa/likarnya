package com.epam.likarnya.controller;

import com.epam.likarnya.dto.PatientDto;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.repository.PatientRepository;
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
    private final PatientRepository patientRepository;


    @GetMapping(value = "/admin/patient-registration")
    public String patientRegistration(Model model) {
        model.addAttribute("dateOfToday", String.valueOf(LocalDate.now()));
        model.addAttribute("registrationPatient", new PatientDto());
        return "/admin/patientRegistration";
    }

    @PostMapping(value = "/admin/patient-registration")
    public String patientRegistration(Model model, @Valid @ModelAttribute("registrationPatient") PatientDto patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/patientRegistration";
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
            return "redirect:/admin";
        }

    }
}

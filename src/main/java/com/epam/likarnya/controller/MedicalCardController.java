package com.epam.likarnya.controller;

import com.epam.likarnya.model.*;
import com.epam.likarnya.repository.UserRepository;
import com.epam.likarnya.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.RequiresQualifier;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityListeners;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Controller
@RequiredArgsConstructor
public class MedicalCardController {
    private final MedicalCardService medicalCardService;
    private final StatementService statementService;
    private final PatientService patientService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @GetMapping(value = "/admin/create-medical-card/{patient_id}")
    public String openMedicalCard(@PathVariable("patient_id") long patientId, Model model) {
        List<Category> categories = categoryService.getAll();
        Patient patient = patientService.findById(patientId);
        model.addAttribute("medicalCard", new MedicalCard());
        model.addAttribute("patient", patient);
        model.addAttribute("categories", categories);
        return "medicalCard";
    }


    @GetMapping(value = "/admin/create-medical-card/{patient_id}/{cat}")
    public String openMedicalCard5(@PathVariable("patient_id") long patientId, @PathVariable(value = "cat", required = true) String doctorCategory, Model model) {
        Patient patient2 = (Patient) model.getAttribute("patient");
//        Long patientId1 = Long.parseLong(patientId);
        model.addAttribute("categ", doctorCategory);
        List<Category> categories = categoryService.getAll();
        Patient patient = patientService.findById(Long.valueOf(patientId));
        var doctors = userRepository.getDoctorsByCategory(doctorCategory);
        model.addAttribute("medicalCard", new MedicalCard());
        model.addAttribute("patient", patient);
        model.addAttribute("categories", categories);
        model.addAttribute("doctors", doctors);
        return "medicalCard";
    }

    @PostMapping(value = "/admin/create-medical-card/add/{patient_id}")
    public String openMedicalCard2(@PathVariable("patient_id") long patientId, MedicalCard card, Model model) {
        Long chosenDoctorId = card.getUser().getId();
        String complaints = card.getComplaints();
        Patient patient = patientService.findById(patientId);
        User selectedDoctor = userService.findById(chosenDoctorId);

        Statement st = Statement.builder()
                .patientStatus(Statement.PatientStatus.NEW)
                .createdAt(LocalDateTime.now())
                .patient(patient)
                .build();

        Statement statement = statementService.createOrUpdate(st);

        MedicalCard medicalCard = MedicalCard.builder()
                .statement(statement)
                .user(selectedDoctor)
                .complaints(complaints)
                .build();

        medicalCardService.createOrUpdate(medicalCard);
        return "nursePage";
    }

}

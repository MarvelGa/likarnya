package com.epam.likarnya.controller;

import com.epam.likarnya.model.Patient;
import com.epam.likarnya.model.User;
import com.epam.likarnya.service.PatientService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Data
@RequiredArgsConstructor
@Slf4j
@Controller
public class DoctorController {
    private final PatientService patientService;

    @GetMapping(value = "/doctor-cabinet")
    public String handleDoctorPage(HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("doctor");
        List<Patient> patients = patientService.getPatientForDiagnosis(doctor.getId());
        model.addAttribute("patients",patients);
        return "doctorPage";
    }


}

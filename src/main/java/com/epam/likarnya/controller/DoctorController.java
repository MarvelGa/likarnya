package com.epam.likarnya.controller;

import com.epam.likarnya.model.*;
import com.epam.likarnya.repository.MedicalCardRepository;
import com.epam.likarnya.service.MedicalCardService;
import com.epam.likarnya.service.PatientService;
import com.epam.likarnya.service.TreatmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Data
@RequiredArgsConstructor
@Slf4j
@Controller
public class DoctorController {
    private final TreatmentService treatmentService;
    private final PatientService patientService;
    private final MedicalCardService medicalCardService;
    private final MedicalCardRepository repository;

    @GetMapping(value = "/doctor-cabinet")
    public String handleDoctorPage(HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("doctor");
        List<Patient> patients = patientService.getPatientForDiagnosis(doctor.getId());
        model.addAttribute("patients",patients);
        return "doctorPage";
    }


    @GetMapping(value =  "/doctor-cabinet/add-treatment/{patient_id}")
    public String addingTreatment(@PathVariable("patient_id") long patientId,  HttpSession session, Model model){
         Patient patient = patientService.findById(patientId);
         MedicalCard medicalCard = medicalCardService.getMedicalCardForDiagnosis(patientId);
         model.addAttribute("patient", patient);
         model.addAttribute("medicalCard", medicalCard);
        return "addTreatment";
    }

    @PostMapping(value =  "/doctor-cabinet/add-treatment/{patient_id}")
    public String addingTreatment(@PathVariable("patient_id") long patientId, @ModelAttribute MedicalCard medicalCard,
                                  @ModelAttribute  Patient patient,  HttpSession session, Model model){
        Treatment treatment = treatmentService.createOrUpdate(medicalCard.getTreatment());
        MedicalCard patientMedicalCard = medicalCardService.getMedicalCardForDiagnosis(patientId);
        patientMedicalCard.setDiagnosis(medicalCard.getDiagnosis());
        patientMedicalCard.getStatement().setPatientStatus(Statement.PatientStatus.DIAGNOSED);
        patientMedicalCard.setTreatment(treatment);
        medicalCardService.createOrUpdate(patientMedicalCard);
        return "redirect:/doctor-cabinet";
    }

}

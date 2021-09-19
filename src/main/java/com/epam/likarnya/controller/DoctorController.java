package com.epam.likarnya.controller;

import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.model.*;
import com.epam.likarnya.repository.MedicalCardRepository;
import com.epam.likarnya.repository.PatientRepository;
import com.epam.likarnya.service.MedicalCardService;
import com.epam.likarnya.service.PatientService;
import com.epam.likarnya.service.StatementService;
import com.epam.likarnya.service.TreatmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Slf4j
@Controller
public class DoctorController {
    private final StatementService statementService;
    private final PatientRepository patientRepository;
    private final TreatmentService treatmentService;
    private final PatientService patientService;
    private final MedicalCardService medicalCardService;
    private final MedicalCardRepository repository;

    @GetMapping(value = "/doctor-cabinet")
    public String handleDoctorPage(HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("doctor");
        var patients = patientService.getPatientsForDiagnosis(doctor);
        model.addAttribute("patients", patients);
        return "/doctor/doctorPage";
    }


    @GetMapping(value = "/doctor-cabinet/add-treatment/{patient_id}")
    public String addingTreatment(@PathVariable("patient_id") long patientId, HttpSession session, Model model) {
        Patient patient = patientService.findById(patientId);
        MedicalCard medicalCard = medicalCardService.getMedicalCardForDiagnosis(patientId);
        model.addAttribute("patient", patient);
        model.addAttribute("medicalCard", medicalCard);
        model.addAttribute("treatment", new Treatment());
        return "/doctor/addTreatment";
    }

    @PostMapping(value = "/doctor-cabinet/add-treatment/{patient_id}")
    public String addingTreatment(@PathVariable("patient_id") long patientId, @ModelAttribute MedicalCard medicalCard,
                                  @ModelAttribute Patient patient, @ModelAttribute Treatment treatment, HttpSession session, Model model) {


        MedicalCard patientMedicalCard = medicalCardService.getMedicalCardForDiagnosis(patientId);
        patientMedicalCard.setDiagnosis(medicalCard.getDiagnosis());
        patientMedicalCard.getStatement().setPatientStatus(Statement.PatientStatus.DIAGNOSED);
        patientMedicalCard.getStatement().setChanged(LocalDateTime.now());

        MedicalCard updatedMedicalCard = medicalCardService.createOrUpdate(patientMedicalCard);

        Treatment createTreatment = new Treatment();
        createTreatment.setAppointment(treatment.getAppointment());
        createTreatment.setAppointmentStatus(Treatment.AppointmentStatus.NOT_EXECUTED);
        createTreatment.setCreatedAt(LocalDateTime.now());
        createTreatment.setMedicalCard(updatedMedicalCard);

        treatmentService.createOrUpdate(createTreatment);
        return "redirect:/doctor-cabinet";
    }

    @GetMapping(value = "/doctor-cabinet/treatment-patients")
    public String handleTreatment(HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("doctor");
        var patients = patientService.getPatientsForTreatment(doctor.getId());
        model.addAttribute("patients", patients);
        return "/doctor/treatmentPatients";
    }

    @GetMapping(value = "/doctor-cabinet/execute-treatment/{patient_id}")
    public String executeTreatment(@PathVariable("patient_id") long patientId, HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("doctor");
        var patient = patientService.getPatientForTreatment(doctor.getId(), patientId);
        model.addAttribute("patientForTreatment", patient);
        return "/doctor/executeTreatment";
    }


    @PostMapping(value = "/doctor-cabinet/execute-treatment")
    public String handleExecuteTreatment(@RequestParam long treatmentId, @RequestParam long statementId, HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("doctor");
        Treatment treatment = treatmentService.getById(treatmentId);
        treatment.setAppointmentStatus(Treatment.AppointmentStatus.EXECUTED);
        treatment.setExecutorId(doctor.getId());
        treatmentService.createOrUpdate(treatment);
        Statement statement = statementService.getById(statementId);
        statement.setPatientStatus(Statement.PatientStatus.DISCHARGED);
        statementService.createOrUpdate(statement);
        return "redirect:/doctor-cabinet/treatment-patients";
    }

//    @GetMapping(value = "/doctor-cabinet/history")
//    public String handlePatientsHistory(HttpSession session, Model model) {
//        User doctor = (User) session.getAttribute("doctor");
//        var patient = patientService.getHistoryByDoctorId(doctor.getId(), );
//        model.addAttribute("patientsHistory", patient);
//        return "/doctor/doctorHistory";
//    }

    @GetMapping(value = "/doctor-cabinet/history")
    public String handlePatientsHistory(@PageableDefault(size = 3) Pageable pageable,HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("doctor");
        var patient = patientService.getHistoryByDoctorId(doctor.getId(), pageable);
        model.addAttribute("page", patient);
        model.addAttribute("totalElements", patient.getTotalElements());
        model.addAttribute("patientsHistory", patient.getContent());
        return "/doctor/doctorHistory";
    }

//    @GetMapping(value = "/admin/patients/history")
//    public String handleHistory(@PageableDefault(size = 8) Pageable pageable, Model model) {
//        Page<TreatmentPatientDto> page = patientService.getHistory(pageable);
//        Long totalElements = page.getTotalElements();
//        model.addAttribute("page", page);
//        model.addAttribute("totalElements", totalElements);
//        return "/admin/patientsHistory";
//    }
}

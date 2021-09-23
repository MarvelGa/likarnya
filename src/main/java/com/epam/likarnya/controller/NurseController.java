package com.epam.likarnya.controller;

import com.epam.likarnya.model.Statement;
import com.epam.likarnya.model.Treatment;
import com.epam.likarnya.model.User;
import com.epam.likarnya.repository.MedicalCardRepository;
import com.epam.likarnya.repository.PatientRepository;
import com.epam.likarnya.service.MedicalCardService;
import com.epam.likarnya.service.PatientService;
import com.epam.likarnya.service.StatementService;
import com.epam.likarnya.service.TreatmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Data
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/likarnya")
@Controller
public class NurseController {
    private final StatementService statementService;
    private final PatientRepository patientRepository;
    private final TreatmentService treatmentService;
    private final PatientService patientService;
    private final MedicalCardService medicalCardService;
    private final MedicalCardRepository repository;

    @GetMapping(value = "/nurse-cabinet")
    public String handleTreatmentByNurse(Model model) {
        var patientsForTreatingByProcedureOrDrug = patientService.getPatientsForTreatmentByNurse();
        model.addAttribute("patientsForTreatingByProcedureOrDrug", patientsForTreatingByProcedureOrDrug);
        return "/nurse/nursePage";
    }

    @GetMapping(value = "/nurse-cabinet/execute-treatment/{patient_id}")
    public String getHandleTreatmentByNurse(@PathVariable("patient_id") long patientId, Model model) {
        var patient = patientService.getPatientByIdForTreatmentByNurse(patientId);
        model.addAttribute("patientForTreatmentByNurse", patient);
        return "/nurse/nurseTreatment";
    }

    @PostMapping(value = "/nurse-cabinet/execute-treatment")
    public String postHandleTreatmentByNurse(@RequestParam long treatmentId, @RequestParam long statementId, HttpSession session, Model model) {
        User nurse = (User) session.getAttribute("nurse");
        Treatment treatment = treatmentService.getById(treatmentId);
        treatment.setAppointmentStatus(Treatment.AppointmentStatus.EXECUTED);
        treatment.setExecutorId(nurse.getId());
        treatmentService.createOrUpdate(treatment);
        Statement statement = statementService.getById(statementId);
        statement.setPatientStatus(Statement.PatientStatus.DISCHARGED);
        statementService.createOrUpdate(statement);
        return "redirect:/likarnya/nurse-cabinet";
    }

    @GetMapping(value = "/nurse-cabinet/history")
    public String nurseTreatmentHistory(@PageableDefault(size = 3) Pageable pageable, HttpSession session, Model model) {
        User nurse = (User) session.getAttribute("nurse");
        var nurseTreatmentHistory = patientService.getNurseTreatmentHistoryById(nurse.getId(), pageable);
        model.addAttribute("nurseTreatmentHistory", nurseTreatmentHistory.getContent());
        model.addAttribute("page", nurseTreatmentHistory);
        model.addAttribute("totalElements", nurseTreatmentHistory.getTotalElements());
        return "/nurse/nurseTreatmentHistory";
    }

}

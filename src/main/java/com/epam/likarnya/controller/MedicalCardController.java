package com.epam.likarnya.controller;

import com.epam.likarnya.model.Category;
import com.epam.likarnya.model.MedicalCard;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.RequiresQualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value="/admin/create-medical-card/{patient_id}")
    public String openMedicalCard(@PathVariable("patient_id") long patientId ,  Model model){
        Map<String, List<String>> map = new HashMap<>();
        List<String> list1 = List.of("Kyiv, Lviv, Kharkiv");
        List<String> list2 = List.of("Vashington, New York, LA");

        map.put("Ukraine", list1);
        map.put("USA", list2);

        List<Category> categories = categoryService.getAll();
        Patient patient = patientService.findById(patientId);
        model.addAttribute("medicalCard", new MedicalCard());
        model.addAttribute("patient", patient);
        model.addAttribute("categories", categories);
        model.addAttribute("itemsMap", map);
        return "medicalCard";
    }


    @GetMapping(value="/admin/create-medical-card/{patient_id}/{cat}")
    public String openMedicalCard5(@PathVariable("patient_id") long patientId , @PathVariable (value = "cat", required = false) String catjjj,  Model model){
        String csss = catjjj;
        Map<String, List<String>> map = new HashMap<>();
        List<String> list1 = List.of("Kyiv, Lviv, Kharkiv");
        List<String> list2 = List.of("Vashington, New York, LA");

        map.put("Ukraine", list1);
        map.put("USA", list2);

        String ff= (String) model.getAttribute("choiceid");

        List<Category> categories = categoryService.getAll();
        Patient patient = patientService.findById(patientId);
        model.addAttribute("medicalCard", new MedicalCard());
        model.addAttribute("patient", patient);
        model.addAttribute("categories", categories);
        model.addAttribute("itemsMap", map);
        return "medicalCard";
    }

















//    @GetMapping(value="/admin/create-medical-card/{patient_id}/add/{cat}")
//    public String openMedicalCard2(@PathVariable("patient_id") long patientId, @PathVariable("cat") String cat, Model model){
////       , @RequestParam("category") String category
////        String name =category;
////        if (name!=null){
////
////        }
//        List<Category> categories = categoryService.getAll();
//        Patient patient = patientService.findById(patientId);
//        model.addAttribute("medicalCard", new MedicalCard());
//        model.addAttribute("patient", patient);
//        model.addAttribute("categories", categories);
//        return "medicalCard";
//    }

}

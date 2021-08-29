package com.epam.likarnya.service.impl;

import com.epam.likarnya.model.Patient;
import com.epam.likarnya.repository.PatientRepository;
import com.epam.likarnya.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository repository;
    @Override
    public Patient createOrUpdate(Patient patient) {
        if (patient.getId()!=null){
            Optional<Patient> patientOptional = repository.findById(patient.getId());
            if (patientOptional.isPresent()){
              Patient newPatient = patientOptional.get();

            }
        }
        return repository.save(patient);
    }
}

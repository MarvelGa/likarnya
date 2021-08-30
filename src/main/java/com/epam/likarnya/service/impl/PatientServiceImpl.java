package com.epam.likarnya.service.impl;

import com.epam.likarnya.model.Patient;
import com.epam.likarnya.repository.PatientRepository;
import com.epam.likarnya.service.PatientService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
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

//    @Override
//    public List<Patient> getPatients() {
//        return Lists.newArrayList (repository.findAll()) ;
//    }

    @Override
    public Page<Patient> getPatients(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

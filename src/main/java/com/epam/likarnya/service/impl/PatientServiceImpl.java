package com.epam.likarnya.service.impl;

import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.repository.PatientRepository;
import com.epam.likarnya.service.PatientService;
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
    public Page<Patient> getPatientsWithOutMedicCard(Pageable pageable) {
        return repository.patientsWithOutMedicCard(pageable);
    }

    @Override
    public Page<Patient> getDischargedPatients(Pageable pageable) {
        return repository.dischargedPatients(pageable);
    }

    @Override
    public Patient createOrUpdate(Patient patient) {
        if (patient.getId() != null) {
            Optional<Patient> patientOptional = repository.findById(patient.getId());
            if (patientOptional.isPresent()) {
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

    @Override
    public Patient findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Patient by id = %s was not found", id)));
    }

    @Override
    public List<Patient> getPatientForDiagnosis(Long doctorId) {
        return repository.getPatientsForDiagnosis(doctorId);
    }

    @Override
    public List<TreatmentPatientDto> getPatientsForTreatment(Long doctorId) {
        return repository.patientsForTreatment(doctorId);
    }

    @Override
    public TreatmentPatientDto getPatientForTreatment(Long doctorId, Long patientId) {
        return repository.patientForTreatment(doctorId, patientId);
    }

    @Override
    public List<TreatmentPatientDto> getHistoryByDoctorId(Long doctorId) {
        return repository.patientsHistoryByDoctorId(doctorId);
    }

    @Override
    public List<TreatmentPatientDto> getPatientsForTreatmentByNurse() {
        return repository.getPatientsForTreatmentByNurse();
    }

    @Override
    public List<TreatmentPatientDto> getNurseTreatmentHistoryById(Long id) {
        return repository.getNurseTreatmentHistoryById(id);
    }
}

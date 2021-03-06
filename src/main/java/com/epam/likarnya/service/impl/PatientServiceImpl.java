package com.epam.likarnya.service.impl;

import com.epam.likarnya.dto.PatientDataDto;
import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.exception.BadRequestException;
import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.model.User;
import com.epam.likarnya.repository.PatientRepository;
import com.epam.likarnya.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository repository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Patient createOrUpdate(Patient patient) {
        return repository.save(patient);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public Patient findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Patient by id = %s was not found", id)));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<TreatmentPatientDto> getPatientsForTreatment(Long doctorId) {
        return repository.patientsForTreatment(doctorId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public TreatmentPatientDto getPatientForTreatment(Long doctorId, Long patientId) {
        return repository.patientForTreatment(doctorId, patientId);
    }

    @Override
    public Page<TreatmentPatientDto> getHistoryByDoctorId(Long doctorId, Pageable pageable) {
        return repository.patientsHistoryByDoctorId(doctorId, pageable);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<TreatmentPatientDto> getPatientsForTreatmentByNurse() {
        return repository.getPatientsForTreatmentByNurse();
    }

    @Override
    public Page<TreatmentPatientDto> getNurseTreatmentHistoryById(Long id, Pageable pageable) {
        return repository.getNurseTreatmentHistoryById(id, pageable);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public TreatmentPatientDto getPatientByIdForTreatmentByNurse(Long patientId) {
        return repository.getPatientByIdForTreatmentByNurse(patientId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Page<TreatmentPatientDto> getHistory(Pageable pageable) {
        return repository.getHistory(pageable);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<Patient> patientsWithOutMedicCard() {
        return repository.patientsWithOutMedicCard();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<PatientDataDto> getPatientsForDiagnosis(User user) {
        if (user != null) {
            return repository.getPatientsForDiagnosis(user.getId());
        }
        throw new BadRequestException("Access not allowed!");
    }
}

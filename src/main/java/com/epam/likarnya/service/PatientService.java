package com.epam.likarnya.service;

import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {
    Patient createOrUpdate(Patient patient);
//    List<Patient> getPatients();
    Page<Patient> getPatients(Pageable pageable);

    Patient findById(Long id);

    List<Patient> getPatientForDiagnosis(Long doctorId);
    List<TreatmentPatientDto> getPatientsForTreatment(Long doctorId);
    TreatmentPatientDto getPatientForTreatment(Long doctorId, Long patientId);
    List<TreatmentPatientDto> getHistoryByDoctorId(Long doctorId);
}

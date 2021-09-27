package com.epam.likarnya.service;

import com.epam.likarnya.dto.PatientDataDto;
import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {

    Patient createOrUpdate(Patient patient);

    Patient findById(Long id);

    List<TreatmentPatientDto> getPatientsForTreatment(Long doctorId);

    TreatmentPatientDto getPatientForTreatment(Long doctorId, Long patientId);

    Page<TreatmentPatientDto> getHistoryByDoctorId(Long doctorId, Pageable pageable);

    List<TreatmentPatientDto> getPatientsForTreatmentByNurse();

    Page<TreatmentPatientDto> getNurseTreatmentHistoryById(Long id, Pageable pageable);

    TreatmentPatientDto getPatientByIdForTreatmentByNurse(Long patientId);

    Page<TreatmentPatientDto> getHistory(Pageable pageable);

    List<Patient> patientsWithOutMedicCard();

    List<PatientDataDto> getPatientsForDiagnosis(User user);
}

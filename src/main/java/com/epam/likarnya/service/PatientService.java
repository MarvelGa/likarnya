package com.epam.likarnya.service;

import com.epam.likarnya.dto.PatientDataDTO;
import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {
    Page<Patient> getPatientsWithOutMedicCard(Pageable pageable);

    Page<Patient> getDischargedPatients(Pageable pageable);

    Patient createOrUpdate(Patient patient);

    Page<Patient> getPatients(Pageable pageable);

    Patient findById(Long id);

//    List<Patient> getPatientForDiagnosis(Long doctorId);

    List<TreatmentPatientDto> getPatientsForTreatment(Long doctorId);

    TreatmentPatientDto getPatientForTreatment(Long doctorId, Long patientId);

    List<TreatmentPatientDto> getHistoryByDoctorId(Long doctorId);

    List<TreatmentPatientDto> getPatientsForTreatmentByNurse();

    List<TreatmentPatientDto> getNurseTreatmentHistoryById(Long id);

    TreatmentPatientDto getPatientByIdForTreatmentByNurse(Long patientId);

    Page<TreatmentPatientDto> getHistory(Pageable pageable);

    List<Patient> patientsWithOutMedicCard();

    List<PatientDataDTO> getPatientsForDiagnosis(User user);
}

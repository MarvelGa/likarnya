package com.epam.likarnya.repository;

import com.epam.likarnya.dto.PatientDataDTO;
import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long>, PagingAndSortingRepository<Patient, Long> {

    @Query(value = "SELECT * FROM patients p WHERE p.id IN (SELECT st.patient_id FROM statements st, medical_card mc, users u WHERE st.id=mc.statement_id AND mc.doctor_id=u.id AND st.patient_status='NEW' AND u.id=:doctorId)", nativeQuery = true)
    List<Patient> getPatientsForDiagnosis(Long doctorId);

    @Query(value = "SELECT p.id AS id, p.first_name AS firstName, p.last_name AS lastName, p.birth_day as dateOfBirth, p.gender AS gender, mc.complaints as complaints FROM patients p, statements st, medical_cards mc WHERE p.id=st.patient_id AND mc.statement_id =st.id AND st.patient_status='NEW' AND p.id IN (SELECT st.patient_id FROM statements st, medical_cards mc, users u WHERE st.id=mc.statement_id AND mc.doctor_id=u.id AND st.patient_status='NEW' AND u.id=:doctorId)", nativeQuery = true)
    List<PatientDataDTO> getPatientsForDiagnosis2(Long doctorId);

//    @Query(value = "SELECT p.id AS id, p.first_name AS firstName, p.last_name AS lastName, p.birth_day as dateOfBirth, p.gender AS gender, mc.complaints as complaints, mc.diagnosis AS diagnosis, tr.appointment AS appointment, tr.appointment_status AS appointmentStatus  FROM patients p, statements st, medical_card mc, treatment tr WHERE p.id=st.patient_id AND tr.id=mc.treatment_id AND mc.statement_id =st.id AND p.id IN (SELECT st.patient_id FROM statements st, medical_card mc, users u, treatment tr WHERE tr.id=mc.treatment_id AND st.id=mc.statement_id AND mc.doctor_id=u.id AND tr.appointment_status='NOT_EXECUTED' AND st.patient_status='DIAGNOSED' AND u.id=:doctorId)", nativeQuery = true)
//    List<TreatmentPatientDto> patientsForTreatment(Long doctorId);

    @Query(value = "SELECT p.id AS id, p.first_name AS firstName, p.last_name AS lastName, p.birth_day as dateOfBirth, p.gender AS gender, mc.complaints as complaints, mc.diagnosis AS diagnosis, tr.appointment AS appointment, tr.appointment_status AS appointmentStatus, u.first_name AS doctorFirstName, u.last_name AS doctorLastName, c.title AS doctorCategory FROM patients p, statements st, medical_cards mc, treatments tr, users u, categories c WHERE c.id=u.category_id AND u.id=mc.doctor_id AND p.id=st.patient_id AND mc.id=tr.m_card_id AND mc.statement_id =st.id AND p.id NOT IN (SELECT st.patient_id FROM statements st WHERE st.patient_status='DISCHARGED') AND p.id IN (SELECT st.patient_id FROM statements st, medical_cards mc, users u, treatments tr WHERE mc.id=tr.m_card_id AND st.id=mc.statement_id AND mc.doctor_id=u.id AND tr.appointment_status='NOT_EXECUTED' AND st.patient_status='DIAGNOSED' AND u.id=:doctorId)", nativeQuery = true)
    List<TreatmentPatientDto> patientsForTreatment(Long doctorId);

    @Query(value = "SELECT p.id AS id, p.first_name AS firstName, p.last_name AS lastName, p.birth_day as dateOfBirth, p.gender AS gender, mc.complaints as complaints, mc.diagnosis AS diagnosis, tr.appointment AS appointment, tr.appointment_status AS appointmentStatus, u.first_name AS doctorFirstName, u.last_name AS doctorLastName, c.title AS doctorCategory, tr.id AS treatmentId, st.id AS statementId FROM patients p, statements st, medical_cards mc, treatments tr, users u, categories c WHERE c.id=u.category_id AND u.id=mc.doctor_id AND p.id=st.patient_id AND mc.id=tr.m_card_id AND mc.statement_id =st.id AND p.id IN (SELECT st.patient_id FROM statements st, medical_cards mc, users u, treatments tr, patients p WHERE p.id=st.patient_id AND mc.id=tr.m_card_id AND st.id=mc.statement_id AND mc.doctor_id=u.id AND tr.appointment_status='NOT_EXECUTED' AND st.patient_status='DIAGNOSED' AND u.id=:doctorId AND p.id=:patientId)", nativeQuery = true)
    TreatmentPatientDto patientForTreatment(Long doctorId, Long patientId);

    @Query(value = "SELECT p.id AS id, p.first_name AS firstName, p.last_name AS lastName, p.birth_day as dateOfBirth, p.gender AS gender, mc.complaints as complaints, mc.diagnosis AS diagnosis, tr.appointment AS appointment, tr.appointment_status AS appointmentStatus, u.first_name AS doctorFirstName, u.last_name AS doctorLastName, c.title AS doctorCategory FROM patients p, statements st, medical_cards mc, treatments tr, users u, categories c WHERE c.id=u.category_id AND u.id=mc.doctor_id AND p.id=st.patient_id AND mc.id=tr.m_card_id AND mc.statement_id =st.id AND p.id IN (SELECT st.patient_id FROM statements st, medical_cards mc, users u, treatments tr WHERE mc.id=tr.m_card_id AND st.id=mc.statement_id AND mc.doctor_id=u.id AND tr.appointment_status='EXECUTED' AND st.patient_status='DISCHARGED' AND u.id=:doctorId)", nativeQuery = true)
    List<TreatmentPatientDto> patientsHistoryByDoctorId(Long doctorId);

    @Query(value = " SELECT * FROM patients p WHERE p.id NOT IN (SELECT st.patient_id FROM statements st WHERE st.patient_status='NEW' OR st.patient_status='DISCHARGED' OR st.patient_status='DIAGNOSED') ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Patient> patientsWithOutMedicCard(Pageable pageable);

    @Query(value = " SELECT * FROM patients p WHERE p.id NOT IN (SELECT st.patient_id FROM statements st WHERE st.patient_status='NEW' OR st.patient_status='DIAGNOSED') AND p.id IN(SELECT st.patient_id FROM statements st WHERE st.patient_status='DISCHARGED') ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Patient> dischargedPatients(Pageable pageable);
}

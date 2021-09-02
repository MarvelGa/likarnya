package com.epam.likarnya.repository;

import com.epam.likarnya.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long>, PagingAndSortingRepository<Patient, Long> {

    @Query(value = "SELECT * FROM patients p WHERE p.id IN (SELECT st.patient_id FROM statements st, medical_card mc, users u WHERE st.id=mc.statement_id AND mc.doctor_id=u.id AND st.patient_status='NEW' AND u.id=:doctorId)", nativeQuery = true)
    List<Patient> getPatientsForDiagnosis(Long doctorId);
}

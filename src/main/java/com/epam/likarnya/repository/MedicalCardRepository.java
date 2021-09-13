package com.epam.likarnya.repository;

import com.epam.likarnya.model.MedicalCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalCardRepository extends CrudRepository<MedicalCard, Long> {

   @Query(value ="SELECT * FROM medical_cards mc WHERE mc.id=(SELECT mc.id FROM medical_cards mc, statements st, patients p WHERE st.id=mc.statement_id AND st.patient_id=p.id AND st.patient_status='NEW' AND p.id=:patientId)", nativeQuery = true)
    MedicalCard getMedicalCardById(Long patientId);
}

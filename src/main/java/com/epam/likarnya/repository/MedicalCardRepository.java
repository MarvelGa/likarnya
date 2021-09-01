package com.epam.likarnya.repository;

import com.epam.likarnya.model.MedicalCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalCardRepository extends CrudRepository<MedicalCard, Long> {
}

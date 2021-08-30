package com.epam.likarnya.repository;

import com.epam.likarnya.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long>, PagingAndSortingRepository<Patient, Long> {
}

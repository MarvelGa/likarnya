package com.epam.likarnya.repository;

import com.epam.likarnya.model.Statement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepository extends CrudRepository<Statement, Long> {
}
